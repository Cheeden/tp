package tutortrack.logic.parser;

import static tutortrack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutortrack.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tutortrack.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static tutortrack.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import tutortrack.logic.commands.DeleteProgressCommand;

/**
 * Contains unit tests for DeleteProgressCommandParser.
 */
public class DeleteProgressCommandParserTest {

    private DeleteProgressCommandParser parser = new DeleteProgressCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteProgressCommand() {
        assertParseSuccess(parser, "1 2025-10-15",
                new DeleteProgressCommand(INDEX_FIRST_PERSON, LocalDate.of(2025, 10, 15)));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteProgressCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingDate_throwsParseException() {
        assertParseFailure(parser, "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteProgressCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_extraArguments_throwsParseException() {
        assertParseFailure(parser, "1 2025-10-15 extra",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteProgressCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDateFormat_throwsParseException() {
        assertParseFailure(parser, "1 15-10-2025",
                "Invalid date format. Use yyyy-MM-dd (e.g., 2025-10-15).");
    }

    @Test
    public void parse_invalidDateValues_throwsParseException() {
        assertParseFailure(parser, "1 2025-13-01",
                "Invalid date: month must be 01-12 and day must be valid for that month.");
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, "0 2025-10-15",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteProgressCommand.MESSAGE_USAGE));
    }
}

