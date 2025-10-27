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
import tutortrack.model.lesson.LessonPlan;
import tutortrack.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests
 * {@code DeletePlanCommand}.
 */
public class DeletePlanCommandTest {

    private Model model;
    private final LocalDate testDate1 = LocalDate.of(2025, 10, 15);
    private final LocalDate testDate2 = LocalDate.of(2025, 11, 20);

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    // EP: Valid index within bounds of unfiltered list and plan exists on date
    @Test
    public void execute_validIndexWithinBoundsUnfilteredList_success() {
        // Get the first person from the filtered list
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // Simulate adding of a lesson plan to the person on testDate1
        LessonPlan lessonPlanToAdd = new LessonPlan(testDate1, "Test lesson plan");
        personToEdit.addLessonPlan(lessonPlanToAdd);

        // Create the delete command
        DeletePlanCommand deletePlanCommand = new DeletePlanCommand(INDEX_FIRST_PERSON, testDate1);
        // The success message should be returned
        String expectedMessage = String.format(DeletePlanCommand.MESSAGE_SUCCESS, testDate1);

        // Create what the model should look like after the command
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        Person expectedPerson = expectedModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // Create a new person without the lesson plan to be deleted
        Person editedPerson = new Person(
                expectedPerson.getName(),
                expectedPerson.getSelfContact(),
                expectedPerson.getNokContact(),
                expectedPerson.getSubjectLevel(),
                expectedPerson.getDayTime(),
                expectedPerson.getCost(),
                expectedPerson.getAddress(),
                expectedPerson.getTags()
        );

        // Copy all lesson plans and progress from the original person
        editedPerson.getLessonPlanList().addAll(expectedPerson.getLessonPlanList());
        editedPerson.getLessonProgressList().addAll(expectedPerson.getLessonProgressList());

        // Remove the specific lesson plan by date
        editedPerson.removeLessonPlanByDate(testDate1);

        // Update the expected model with the edited person
        expectedModel.setPerson(expectedPerson, editedPerson);

        // Runs the delete plan command and checks if the command is successful
        assertCommandSuccess(deletePlanCommand, model, expectedMessage, expectedModel);
    }

    // EP: Valid date but no lesson plan exists on that date
    @Test
    public void execute_validDateButNoPlanExists_throwsCommandException() {
        DeletePlanCommand deletePlanCommand = new DeletePlanCommand(INDEX_FIRST_PERSON, testDate1);

        // No lesson plan exists on the date
        String outputMessage = String.format(DeletePlanCommand.MESSAGE_NO_PLAN_ON_DATE, testDate1);
        assertCommandFailure(deletePlanCommand, model, outputMessage);
    }

    // EP: Invalid index in unfiltered list (out of bounds as index is greater than list size)
    @Test
    public void execute_invalidIndexOutOfBounds_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeletePlanCommand deletePlanCommand = new DeletePlanCommand(outOfBoundIndex, testDate1);

        assertCommandFailure(deletePlanCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    // EP: Invalid index in filtered list (valid in unfiltered but not in filtered)
    @Test
    public void execute_invalidIndexInFilteredList_throwsCommandException() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // Filter to show only first person
        model.updateFilteredPersonList(p -> p.equals(personToDelete));

        // Try to delete using INDEX_SECOND_PERSON (out of bounds in filtered list)
        DeletePlanCommand deletePlanCommand = new DeletePlanCommand(INDEX_SECOND_PERSON, testDate1);

        assertCommandFailure(deletePlanCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    // EP: equals() tests - same value returns true
    @Test
    public void equals_sameObject_returnsTrue() {
        DeletePlanCommand command = new DeletePlanCommand(INDEX_FIRST_PERSON, testDate1);
        assertTrue(command.equals(command));
    }

    // EP: equals() - different index returns false
    @Test
    public void equals_differentIndex_returnsFalse() {
        DeletePlanCommand command1 = new DeletePlanCommand(INDEX_FIRST_PERSON, testDate1);
        DeletePlanCommand command2 = new DeletePlanCommand(INDEX_SECOND_PERSON, testDate1);
        assertFalse(command1.equals(command2));
    }

    // EP: toString() tests
    @Test
    public void toStringTest() {
        DeletePlanCommand command = new DeletePlanCommand(INDEX_FIRST_PERSON, testDate1);
        String expected = DeletePlanCommand.class.getCanonicalName()
                + "{index=" + INDEX_FIRST_PERSON + ", date=" + testDate1 + "}";
        assertEquals(expected, command.toString());
    }
}
