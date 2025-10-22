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
import tutortrack.model.lesson.LessonPlan;
import tutortrack.model.person.Person;

public class AddPlanCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    void execute_invalidIndexOutOfBounds_throwsCommandException() throws ParseException {
        Index targetIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        LessonPlan plan = ParserUtil.parseLessonPlan("2025-10-21|Test Plan");
        AddPlanCommand command = new AddPlanCommand(targetIndex, plan);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    void execute_addDuplicatePlan_throwsCommandException() {
        Person validPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        LessonPlan existingLl = new LessonPlan(
                LocalDate.of(2025, 10, 22), "Introduced new algebra concepts");
        validPerson.addLessonPlan(existingLl);

        LessonPlan duplicateLl = new LessonPlan(
                LocalDate.of(2025, 10, 22), "Repeated entry");
        AddPlanCommand command = new AddPlanCommand(Index.fromOneBased(1), duplicateLl);

        String expectedMessage = String.format(AddPlanCommand.MESSAGE_DUPLICATE_PLAN,
                duplicateLl.getDate());
        assertThrows(CommandException.class, expectedMessage, () -> command.execute(model));
    }

    @Test
    void parseLessonPlan_invalidDateFormat_throwsParseException() {
        assertThrows(ParseException.class, () ->
                ParserUtil.parseLessonPlan("invalid-date|Some plan"));
    }
}
