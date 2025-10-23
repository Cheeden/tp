package tutortrack.ui;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import tutortrack.commons.core.LogsCenter;
import tutortrack.model.lesson.LessonPlan;
import tutortrack.model.lesson.LessonProgress;
import tutortrack.model.person.Person;

/**
 * Controller for the lesson window showing both progress and plans.
 */
public class LessonWindow extends UiPart<Stage> {

    private static final String FXML = "LessonWindow.fxml";
    private static final Logger logger = LogsCenter.getLogger(LessonWindow.class);

    @FXML
    private TableView<LessonDisplay> lessonTable;

    @FXML
    private TableColumn<LessonDisplay, String> dateColumn;

    @FXML
    private TableColumn<LessonDisplay, String> progressColumn;

    @FXML
    private TableColumn<LessonDisplay, String> planColumn;

    private final ObservableList<LessonDisplay> lessonData = FXCollections.observableArrayList();

    /**
     * Creates a new LessonWindow.
     *
     * @param root Stage to use as the root of the LessonWindow.
     */
    public LessonWindow(Stage root) {
        super(FXML, root);

        // Set up table columns with custom cell value factories
        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate().toString()));
        progressColumn.setCellValueFactory(cellData ->
                cellData.getValue().progressProperty());
        planColumn.setCellValueFactory(cellData ->
                cellData.getValue().planProperty());

        // load the data into the table
        lessonTable.setItems(lessonData);
    }

    /**
     * Creates a new LessonWindow.
     */
    public LessonWindow() {
        this(new Stage());
    }

    /**
     * Sets the person whose lessons should be displayed.
     * Combines lesson progress and lesson plans by date.
     *
     * @param person The person whose lessons to display.
     */
    public void setPerson(Person person) {
        getRoot().setTitle("Lessons - " + person.getName().fullName);
        logger.fine("Loading lessons summary for: " + person.getName());

        // Create a map to combine progress and plans by date
        Map<LocalDate, LessonDisplay> lessonMap = new HashMap<>();

        // Add all progress entries
        for (LessonProgress progress : person.getLessonProgressList()) {
            LocalDate date = progress.getDate();
            lessonMap.put(date, new LessonDisplay(date, progress.getProgress(), ""));
        }

        // Add/merge lesson plans
        for (LessonPlan plan : person.getLessonPlanList()) {
            LocalDate date = plan.getDate();
            if (lessonMap.containsKey(date)) {
                // Both progress and plan exist for this date
                LessonDisplay existing = lessonMap.get(date);
                lessonMap.put(date, new LessonDisplay(date, existing.getProgress(), plan.getPlan()));
            } else {
                // Only plan exists for this date
                lessonMap.put(date, new LessonDisplay(date, "", plan.getPlan()));
            }
        }

        // Convert map to sorted list (by date)
        List<LessonDisplay> lessonList = lessonMap.values().stream()
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .collect(Collectors.toList());

        lessonData.clear();
        lessonData.addAll(lessonList);
        lessonTable.setItems(lessonData);

        person.getLessonProgressList().addListener(
                (ListChangeListener<? super LessonProgress>) change -> {
                    while (change.next()) {
                        if (change.wasAdded()) {
                            for (LessonProgress progress : change.getAddedSubList()) {
                                addOrEditUpdateLessonDisplay(progress.getDate(), progress.getProgress(), null);
                            }
                        }
                    }
                });

        person.getLessonPlanList().addListener(
                (ListChangeListener<? super LessonPlan>) change -> {
                    while (change.next()) {
                        if (change.wasAdded()) {
                            for (LessonPlan plan : change.getAddedSubList()) {
                                addOrEditUpdateLessonDisplay(plan.getDate(), null, plan.getPlan());
                            }
                        }
                    }
                });

        logger.fine("Loaded " + lessonList.size() + " lesson entries for: " + person.getName().fullName);
    }

    /**
     * Shows the lesson window.
     */
    public void show() {
        logger.fine("Showing lesson window.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the lesson window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the lesson window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the lesson window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    private void addOrEditUpdateLessonDisplay(LocalDate date, String progress, String plan) {
        for (LessonDisplay ld : lessonData) {
            if (ld.getDate().equals(date)) {
                if (progress != null) {
                    ld.setProgress(progress);
                }
                if (plan != null) {
                    ld.setPlan(plan);
                }
                return;
            }
        }
        lessonData.add(new LessonDisplay(date, progress == null ? "" : progress, plan == null ? "" : plan));
    }

    /**
     * Handles the close button action.
     */
    @FXML
    private void handleClose() {
        hide();
    }
}
