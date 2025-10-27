package tutortrack.logic.parser;

import static tutortrack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutortrack.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tutortrack.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static tutortrack.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import tutortrack.logic.commands.DeletePlanCommand;

public class DeletePlanCommandParserTest {
    private DeletePlanCommandParser parser = new DeletePlanCommandParser();
    // Successful test cases

    // EP: Valid input - index and date are valid
    @Test
    public void parse_validInput_returnsDeletePlanCommand() {
        String args = "1 2025-10-15";
        DeletePlanCommand expectedCommand = new DeletePlanCommand(INDEX_FIRST_PERSON,
                LocalDate.of(2025, 10, 15));
        assertParseSuccess(parser, args, expectedCommand);
    }

    // EP: Invalid input - index is invalid
    @Test
    public void parse_invalidIndex_throwsParseException() {
        String args = "a 2025-10-15";
        assertParseFailure(parser, args,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePlanCommand.MESSAGE_USAGE));
    }

    // EP: Invalid input - date values out of range
    @Test
    public void parse_invalidDateValues_throwsParseException() {
        // Month 13 is invalid
        String args = "1 2025-13-15";
        assertParseFailure(parser, args,
                "Invalid date: month must be 01-12 and day must be valid for that month.");
    }
}
