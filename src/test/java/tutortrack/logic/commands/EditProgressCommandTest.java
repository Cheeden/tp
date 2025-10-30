package tutortrack.logic.commands;

import static tutortrack.logic.commands.CommandTestUtil.assertCommandFailure;
import static tutortrack.testutil.Assert.assertThrows;
import static tutortrack.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static tutortrack.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import tutortrack.commons.core.index.Index;
import tutortrack.logic.Messages;
import tutortrack.logic.commands.exceptions.CommandException;
import tutortrack.logic.parser.ParserUtil;
import tutortrack.logic.parser.exceptions.ParseException;
import tutortrack.model.Model;
import tutortrack.model.ModelManager;
import tutortrack.model.UserPrefs;
import tutortrack.model.lesson.LessonProgress;
import tutortrack.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditProgressCommand.
 */
public class EditProgressCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    void execute_invalidIndexOutOfBounds_throwsCommandException() throws ParseException {
        Index targetIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        LessonProgress progress = ParserUtil.parseLessonProgress("2025-10-21|Updated Progress");
        EditProgressCommand command = new EditProgressCommand(targetIndex, progress);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    void execute_noExistingProgress_throwsCommandException() throws ParseException {
        Person person = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        LessonProgress toEdit = ParserUtil.parseLessonProgress("2025-10-31|Reviewed algebra test");
        EditProgressCommand command = new EditProgressCommand(Index.fromOneBased(1), toEdit);

        String expectedMessage = String.format(EditProgressCommand.MESSAGE_NOT_FOUND, toEdit.getDate());
        assertThrows(CommandException.class, expectedMessage, () -> command.execute(model));
    }

    @Test
    void execute_successfullyEditsExistingProgress() throws ParseException, CommandException {
        Person person = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        LocalDate targetDate = LocalDate.of(2025, 10, 25);
        LessonProgress original = new LessonProgress(targetDate, "Original Progress");
        person.addLessonProgress(original);

        LessonProgress updated = new LessonProgress(targetDate, "Updated Progress");
        EditProgressCommand command = new EditProgressCommand(Index.fromOneBased(1), updated);

        CommandResult result = command.execute(model);

        String expectedMessage = String.format(EditProgressCommand.MESSAGE_SUCCESS_EDIT,
                updated.getDate(), updated.getProgress());
        assert result.getFeedbackToUser().equals(expectedMessage);
    }

    @Test
    void parseLessonProgress_invalidFormat_throwsParseException() {
        assertThrows(ParseException.class, () ->
                                                   ParserUtil.parseLessonProgress("invalid date|description"));
    }
}
