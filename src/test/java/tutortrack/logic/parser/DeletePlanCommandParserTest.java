package tutortrack.logic.parser;

import static tutortrack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutortrack.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tutortrack.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static tutortrack.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
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
                MESSAGE_INVALID_INDEX + "\n" + DeletePlanCommand.MESSAGE_USAGE);
    }

    // EP: Invalid input - date values out of range
    @Test
    public void parse_invalidDateValues_throwsParseException() {
        // Month 13 is invalid
        String args = "1 2025-13-15";
        assertParseFailure(parser, args,
                "Invalid month: must be between 01 and 12.\n" + DeletePlanCommand.MESSAGE_USAGE);
    }

    // EP: Invalid input - day invalid for given month
    @Test
    public void parse_invalidDayForMonth_throwsParseException() {
        String args = "1 2025-02-31";
        assertParseFailure(parser, args,
                "Invalid day for the given month. Please check your date.\n" + DeletePlanCommand.MESSAGE_USAGE);
    }

    // EP: Invalid input - incorrect date format
    @Test
    public void parse_invalidDateFormat_throwsParseException() {
        String args = "1 15-10-2025";
        assertParseFailure(parser, args,
                "Invalid date format. Use yyyy-MM-dd (e.g., 2025-10-15).\n" + DeletePlanCommand.MESSAGE_USAGE);
    }

    // EP: Invalid input with missing date argument
    @Test
    public void parse_missingDate_throwsParseException() {
        String args = "1";
        assertParseFailure(parser, args,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePlanCommand.MESSAGE_USAGE));
    }

    // EP: Invalid input with extra arguments
    @Test
    public void parse_extraArguments_throwsParseException() {
        String args = "1 2025-10-15 extra";
        assertParseFailure(parser, args,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePlanCommand.MESSAGE_USAGE));
    }

    // EP: Invalid input with no arguments provided
    @Test
    public void parse_noArguments_throwsParseException() {
        String args = "";
        assertParseFailure(parser, args,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePlanCommand.MESSAGE_USAGE));
    }
}
