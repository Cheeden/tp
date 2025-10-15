package tutortrack.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tutortrack.commons.core.LogsCenter;
import tutortrack.model.lesson.LessonProgress;

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

        // Set up table columns
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        progressColumn.setCellValueFactory(new PropertyValueFactory<>("progress"));
    }

    /**
     * Creates a new LessonProgressWindow.
     */
    public LessonProgressWindow() {
        this(new Stage());
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
