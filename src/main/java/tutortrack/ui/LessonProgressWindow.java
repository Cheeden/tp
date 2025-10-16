package tutortrack.ui;

import java.util.logging.Logger;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import tutortrack.commons.core.LogsCenter;
import tutortrack.model.lesson.LessonProgress;
import tutortrack.model.person.Person;

/**
 * Controller for the lesson progress window.
 */
public class LessonProgressWindow extends UiPart<Stage> {

    private static final String FXML = "LessonProgressWindow.fxml";
    private static final Logger logger = LogsCenter.getLogger(LessonProgressWindow.class);

    @FXML
    private TableView<LessonProgress> lessonProgressTable;

    @FXML
    private TableColumn<LessonProgress, String> dateColumn;

    @FXML
    private TableColumn<LessonProgress, String> progressColumn;

    /**
     * Creates a new LessonProgressWindow.
     *
     * @param root Stage to use as the root of the LessonProgressWindow.
     */
    public LessonProgressWindow(Stage root) {
        super(FXML, root);

        // Set up table columns with custom cell value factories
        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate().toString()));
        progressColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProgress()));
    }

    /**
     * Creates a new LessonProgressWindow.
     */
    public LessonProgressWindow() {
        this(new Stage());
    }

    /**
     * Sets the person whose lesson progress should be displayed.
     *
     * @param person The person whose lesson progress to display.
     */
    public void setPerson(Person person) {
        logger.fine("Loading lesson progress for: " + person.getName());
        lessonProgressTable.getItems().clear();
        lessonProgressTable.getItems().addAll(person.getLessonProgressList());
    }

    /**
     * Shows the lesson progress window.
     */
    public void show() {
        logger.fine("Showing lesson progress window.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the lesson progress window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the lesson progress window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the lesson progress window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Handles the close button action.
     */
    @FXML
    private void handleClose() {
        hide();
    }
}
