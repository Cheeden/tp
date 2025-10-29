package tutortrack.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutortrack.logic.commands.CommandTestUtil.assertCommandFailure;
import static tutortrack.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tutortrack.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static tutortrack.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static tutortrack.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tutortrack.commons.core.index.Index;
import tutortrack.logic.Messages;
import tutortrack.model.AddressBook;
import tutortrack.model.Model;
import tutortrack.model.ModelManager;
import tutortrack.model.UserPrefs;
import tutortrack.model.lesson.LessonProgress;
import tutortrack.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteProgressCommand}.
 */
public class DeleteProgressCommandTest {

    private Model model;
    private final LocalDate testDate1 = LocalDate.of(2025, 10, 15);
    private final LocalDate testDate2 = LocalDate.of(2025, 11, 20);

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    // EP: Valid index within bounds of unfiltered list and progress exists on date
    @Test
    public void execute_validIndexWithinBoundsUnfilteredList_success() {
        // Get the first person from the filtered list
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // Simulate adding of a lesson progress to the person on testDate1
        LessonProgress lessonProgressToAdd = new LessonProgress(testDate1, "Covered Chapter 5");
        personToEdit.addLessonProgress(lessonProgressToAdd);

        // Create the delete command
        DeleteProgressCommand deleteProgressCommand = new DeleteProgressCommand(INDEX_FIRST_PERSON, testDate1);

        // The success message should be returned
        String expectedMessage = String.format(DeleteProgressCommand.MESSAGE_SUCCESS, testDate1);

        // Create what the model should look like after the command
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        Person expectedPerson = expectedModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // Create a new person without the lesson progress to be deleted
        Person editedPerson = expectedPerson.withProgressRemovedOnDate(testDate1);

        // Update the expected model with the edited person
        expectedModel.setPerson(expectedPerson, editedPerson);

        // Runs the delete progress command and checks if the command is successful
        assertCommandSuccess(deleteProgressCommand, model, expectedMessage, expectedModel);
    }

    // EP: Valid date but no lesson progress exists on that date
    @Test
    public void execute_validDateButNoProgressExists_throwsCommandException() {
        DeleteProgressCommand deleteProgressCommand = new DeleteProgressCommand(INDEX_FIRST_PERSON, testDate1);

        // No lesson progress exists on the date
        String outputMessage = String.format(DeleteProgressCommand.MESSAGE_NO_PROGRESS_ON_DATE, testDate1);
        assertCommandFailure(deleteProgressCommand, model, outputMessage);
    }

    // EP: Invalid index in unfiltered list (out of bounds as index is greater than list size)
    @Test
    public void execute_invalidIndexOutOfBounds_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteProgressCommand deleteProgressCommand = new DeleteProgressCommand(outOfBoundIndex, testDate1);

        assertCommandFailure(deleteProgressCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    // EP: Invalid index in filtered list (valid in unfiltered but not in filtered)
    @Test
    public void execute_invalidIndexInFilteredList_throwsCommandException() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // Filter to show only first person
        model.updateFilteredPersonList(p -> p.equals(personToDelete));

        // Try to delete using INDEX_SECOND_PERSON (out of bounds in filtered list)
        DeleteProgressCommand deleteProgressCommand = new DeleteProgressCommand(INDEX_SECOND_PERSON, testDate1);

        assertCommandFailure(deleteProgressCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    // EP: equals() tests which checks if same value returns true
    @Test
    public void equals_sameObject_returnsTrue() {
        DeleteProgressCommand command = new DeleteProgressCommand(INDEX_FIRST_PERSON, testDate1);
        assertTrue(command.equals(command));
    }

    // EP: equals() which checks if different index returns false
    @Test
    public void equals_differentIndex_returnsFalse() {
        DeleteProgressCommand command1 = new DeleteProgressCommand(INDEX_FIRST_PERSON, testDate1);
        DeleteProgressCommand command2 = new DeleteProgressCommand(INDEX_SECOND_PERSON, testDate1);
        assertFalse(command1.equals(command2));
    }

    // EP: equals() which checks if different date returns false
    @Test
    public void equals_differentDate_returnsFalse() {
        DeleteProgressCommand command1 = new DeleteProgressCommand(INDEX_FIRST_PERSON, testDate1);
        DeleteProgressCommand command2 = new DeleteProgressCommand(INDEX_FIRST_PERSON, testDate2);
        assertFalse(command1.equals(command2));
    }

    // EP: toString() tests
    @Test
    public void toStringTest() {
        DeleteProgressCommand command = new DeleteProgressCommand(INDEX_FIRST_PERSON, testDate1);
        String expected = DeleteProgressCommand.class.getCanonicalName()
                + "{index=" + INDEX_FIRST_PERSON + ", date=" + testDate1 + "}";
        assertEquals(expected, command.toString());
    }
}

