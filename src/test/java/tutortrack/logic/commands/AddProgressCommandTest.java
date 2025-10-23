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
    void execute_addDuplicateProgress_throwsCommandException() {
        Person validPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        LessonProgress existingLp = new LessonProgress(LocalDate.of(2025, 10, 21),
                "Introduced new algebra concepts");
        validPerson.addLessonProgress(existingLp);

        LessonProgress duplicateLp = new LessonProgress(LocalDate.of(2025, 10, 21),
                "Repeated entry");
        AddProgressCommand command = new AddProgressCommand(Index.fromOneBased(1), duplicateLp);

        String expectedMessage = String.format(AddProgressCommand.MESSAGE_DUPLICATE_PROGRESS,
                duplicateLp.getDate());
        assertThrows(
                CommandException.class, expectedMessage, () -> command.execute(model));
    }

    @Test
    void parseLessonProgress_invalidDateFormat_throwsParseException() {
        assertThrows(ParseException.class, () ->
                ParserUtil.parseLessonProgress("invalid-date|Some progress"));
    }
}
