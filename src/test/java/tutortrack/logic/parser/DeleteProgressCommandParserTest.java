package tutortrack.logic.parser;

import static tutortrack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutortrack.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tutortrack.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static tutortrack.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static tutortrack.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import tutortrack.logic.commands.DeleteProgressCommand;

/**
 * Contains unit tests for DeleteProgressCommandParser.
 */
public class DeleteProgressCommandParserTest {

    private DeleteProgressCommandParser parser = new DeleteProgressCommandParser();

    // EP: Valid input - index and date are valid
    @Test
    public void parse_validArgs_returnsDeleteProgressCommand() {
        assertParseSuccess(parser, "1 2025-10-15",
                new DeleteProgressCommand(INDEX_FIRST_PERSON, LocalDate.of(2025, 10, 15)));
    }

    // EP: Invalid input - invalid arguments
    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteProgressCommand.MESSAGE_USAGE));
    }

    // EP: Invalid input - missing date
    @Test
    public void parse_missingDate_throwsParseException() {
        assertParseFailure(parser, "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteProgressCommand.MESSAGE_USAGE));
    }

    // EP: Invalid input - extra arguments
    @Test
    public void parse_extraArguments_throwsParseException() {
        assertParseFailure(parser, "1 2025-10-15 extra",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteProgressCommand.MESSAGE_USAGE));
    }

    // Invalid input - incorrect date format
    @Test
    public void parse_invalidDateFormat_throwsParseException() {
        assertParseFailure(parser, "1 15-10-2025",
                "Invalid date format. Use yyyy-MM-dd (e.g., 2025-10-15).\n" + DeleteProgressCommand.MESSAGE_USAGE);
    }

    // EP: Invalid input - invalid month
    @Test
    public void parse_invalidMonth_throwsParseException() {
        assertParseFailure(parser, "1 2025-13-20",
            "Invalid month: must be between 01 and 12.\n" + DeleteProgressCommand.MESSAGE_USAGE);
    }

    // EP: Invalid input - day invalid for given month
    @Test
    public void parse_invalidDayForMonth_throwsParseException() {
        assertParseFailure(parser, "1 2025-02-31",
                "Invalid day for the given month. Please check your date.\n"
                + DeleteProgressCommand.MESSAGE_USAGE);
    }

    // EP: Invalid input - swapped day and month
    @Test
    public void parse_swappedDayMonth_throwsParseException() {
        assertParseFailure(parser, "1 2025-13-01",
            "Invalid date: you may have swapped day and month. The format is yyyy-MM-dd.\n"
            + DeleteProgressCommand.MESSAGE_USAGE);
    }

    // EP: Invalid input - invalid index
    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, "0 2025-10-15",
                MESSAGE_INVALID_INDEX + "\n" + DeleteProgressCommand.MESSAGE_USAGE);
    }
}

