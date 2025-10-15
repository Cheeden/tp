package tutortrack.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import tutortrack.model.Model;
import tutortrack.model.ModelManager;

public class ViewLessonProgressCommandTest {

    private Model model = new ModelManager();

    @Test
    public void execute_emptyLessonProgress_success() {
        ViewLessonProgressCommand command = new ViewLessonProgressCommand();
        CommandResult result = command.execute(model);

        assertEquals(ViewLessonProgressCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        assertTrue(result.isShowLessonProgress());
    }
}
