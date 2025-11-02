package tutortrack.ui;

import static java.util.Objects.requireNonNull;

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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
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
    private Person currentPerson; // Track the person being displayed
    private ObservableList<Person> personList; // Observable list to monitor for changes
    private ListChangeListener<Person> personListListener; // Listener for person list changes

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

        // Enable text wrapping for progress and plan columns
        configureWrappingColumn(progressColumn);
        configureWrappingColumn(planColumn);

        // load the data into the table
        lessonTable.setItems(lessonData);

        // Set up listener for person list changes
        setupPersonListListener();
    }

    /**
     * Creates a new LessonWindow.
     */
    public LessonWindow() {
        this(new Stage());
    }

    /**
     * Configures a table column to wrap text in its cells.
     *
     * @param column The column to configure for text wrapping.
     */
    private void configureWrappingColumn(TableColumn<LessonDisplay, String> column) {
        column.setCellFactory(tc -> {
            TableCell<LessonDisplay, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(javafx.scene.control.Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(column.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
    }

    /**
     * Sets up a listener that closes the window if the displayed person is deleted.
     */
    private void setupPersonListListener() {
        personListListener = change -> {
            if (currentPerson == null || personList == null) {
                return;
            }

            while (change.next()) {
                if (change.wasRemoved() && !change.wasReplaced()) {
                    // Only close if person was removed WITHOUT being replaced
                    // (wasReplaced() = true means it's an edit/update, not a deletion)
                    for (Person removed : change.getRemoved()) {
                        if (removed.isSamePerson(currentPerson)) {
                            logger.info("Displayed person was deleted, closing lesson window");
                            hide();
                            return;
                        }
                    }
                }
            }
        };
    }

    /**
     * Sets the person whose lessons should be displayed and registers listener to person list.
     * Combines lesson progress and lesson plans by date.
     *
     * @param person The person whose lessons to display.
     * @param personList The observable list of persons to monitor for changes.
     */
    public void setPerson(Person person, ObservableList<Person> personList) {
        requireNonNull(person);
        requireNonNull(personList);

        // Remove old listener if exists
        if (this.personList != null && personListListener != null) {
            this.personList.removeListener(personListListener);
        }

        // Store references
        this.currentPerson = person;
        this.personList = personList;

        // Add listener to new list
        personList.addListener(personListListener);

        // Update window display
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

        logger.fine("Loaded " + lessonList.size() + " lesson entries for: " + person.getName().fullName);
    }

    /**
     * Refreshes the lesson window with updated person data from the model.
     *
     * @param updatedPersonList The updated list of persons from the model.
     */
    public void refresh(ObservableList<Person> updatedPersonList) {
        if (currentPerson == null || !isShowing()) {
            return;
        }

        // Find the updated person by matching the name (since person objects are immutable)
        Person updatedPerson = updatedPersonList.stream()
                                .filter(p -> p.isSamePerson(currentPerson))
                                .findFirst()
                                .orElse(null);

        if (updatedPerson != null) {
            setPerson(updatedPerson, updatedPersonList);
        }
    }

    /**
     * Shows the lesson window.
     */
    public void show() {
        logger.fine("Showing lesson window.");
        Stage stage = getRoot();

        // If window is minimized (iconified), restore it
        if (stage.isIconified()) {
            stage.setIconified(false);
        }

        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Returns true if the lesson window is currently being shown.
     */
    public boolean isShowing() {
        Stage stage = getRoot();
        // Consider it showing if it's displayed (even if minimized)
        return stage.isShowing() || stage.isIconified();
    }

    /**
     * Hides the lesson window and cleans up listeners.
     */
    public void hide() {
        // Clean up listener before hiding
        if (personList != null && personListListener != null) {
            personList.removeListener(personListListener);
        }
        getRoot().hide();
    }

    /**
     * Focuses on the lesson window.
     */
    public void focus() {
        Stage stage = getRoot();

        // If minimized, restore it first
        if (stage.isIconified()) {
            stage.setIconified(false);
        }

        stage.show();
        stage.toFront();
        stage.requestFocus();
    }

    /**
     * Handles the close button action.
     */
    @FXML
    private void handleClose() {
        hide();
    }
}
