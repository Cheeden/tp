package tutortrack.logic.commands;

// import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutortrack.logic.commands.CommandTestUtil.assertCommandFailure;
import static tutortrack.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tutortrack.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static tutortrack.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import tutortrack.commons.core.index.Index;
import tutortrack.logic.Messages;
import tutortrack.model.Model;
import tutortrack.model.ModelManager;
import tutortrack.model.UserPrefs;
import tutortrack.model.person.Person;

public class ViewLessonProgressCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToView = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        ViewLessonProgressCommand command = new ViewLessonProgressCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(ViewLessonProgressCommand.MESSAGE_SUCCESS,
                personToView.getName());

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true, personToView);
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(command, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        ViewLessonProgressCommand command = new ViewLessonProgressCommand(outOfBoundIndex);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        ViewLessonProgressCommand viewFirstCommand = new ViewLessonProgressCommand(INDEX_FIRST_PERSON);
        ViewLessonProgressCommand viewSecondCommand = new ViewLessonProgressCommand(
                Index.fromOneBased(2));

        // same object -> returns true
        assertTrue(viewFirstCommand.equals(viewFirstCommand));

        // same values -> returns true
        ViewLessonProgressCommand viewFirstCommandCopy = new ViewLessonProgressCommand(INDEX_FIRST_PERSON);
        assertTrue(viewFirstCommand.equals(viewFirstCommandCopy));

        // different types -> returns false
        assertFalse(viewFirstCommand.equals(1));

        // null -> returns false
        assertFalse(viewFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(viewFirstCommand.equals(viewSecondCommand));
    }
}
