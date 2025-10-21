package tutortrack.logic.commands;

import org.junit.jupiter.api.Test;
import tutortrack.commons.core.index.Index;
import tutortrack.logic.Messages;
import tutortrack.logic.parser.ParserUtil;
import tutortrack.logic.parser.exceptions.ParseException;
import tutortrack.model.lesson.LessonPlan;
import tutortrack.model.Model;
import tutortrack.model.ModelManager;
import tutortrack.model.UserPrefs;

import static tutortrack.logic.commands.CommandTestUtil.assertCommandFailure;
import static tutortrack.testutil.Assert.assertThrows;
import static tutortrack.testutil.TypicalPersons.getTypicalAddressBook;

public class AddPlanCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    void execute_invalidIndex_zero_throwsCommandException() throws ParseException {
        Index targetIndex = Index.fromZeroBased(0);
        LessonPlan plan = ParserUtil.parseLessonPlan("2025-10-21|Test Plan");
        AddPlanCommand command = new AddPlanCommand(targetIndex, plan);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    void execute_invalidIndex_outOfBounds_throwsCommandException() throws ParseException {
        Index targetIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        LessonPlan plan = ParserUtil.parseLessonPlan("2025-10-21|Test Plan");
        AddPlanCommand command = new AddPlanCommand(targetIndex, plan);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    void parseLessonPlan_invalidDateFormat_throwsParseException() {
        assertThrows(ParseException.class, () ->
                ParserUtil.parseLessonPlan("invalid-date|Some plan"));
    }
}
