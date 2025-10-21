package tutortrack.logic.commands;

import static tutortrack.logic.commands.CommandTestUtil.assertCommandFailure;
import static tutortrack.testutil.Assert.assertThrows;
import static tutortrack.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import tutortrack.commons.core.index.Index;
import tutortrack.logic.Messages;
import tutortrack.logic.parser.ParserUtil;
import tutortrack.logic.parser.exceptions.ParseException;
import tutortrack.model.Model;
import tutortrack.model.ModelManager;
import tutortrack.model.UserPrefs;
import tutortrack.model.lesson.LessonProgress;

public class AddProgressCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    void execute_invalidIndexOutOfBounds_throwsCommandException() throws ParseException {
        Index targetIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        LessonProgress progress = ParserUtil.parseLessonProgress("2025-10-21|Test Progress");
        AddProgressCommand command = new AddProgressCommand(targetIndex, progress);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    void parseLessonProgress_invalidDateFormat_throwsParseException() {
        assertThrows(ParseException.class, () ->
                ParserUtil.parseLessonProgress("invalid-date|Some progress"));
    }
}
