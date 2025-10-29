package tutortrack.logic.parser;

import static tutortrack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutortrack.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tutortrack.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static tutortrack.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import tutortrack.logic.commands.ViewLessonsCommand;

public class ViewLessonsCommandParserTest {

    private ViewLessonsCommandParser parser = new ViewLessonsCommandParser();

    @Test
    public void parse_validArgs_returnsViewLessonsCommand() {
        assertParseSuccess(parser, "1", new ViewLessonsCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ViewLessonsCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ViewLessonsCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_negativeIndex_throwsParseException() {
        assertParseFailure(parser, "-1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ViewLessonsCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_zeroIndex_throwsParseException() {
        assertParseFailure(parser, "0", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ViewLessonsCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_multipleArgs_throwsParseException() {
        assertParseFailure(parser, "1 2", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ViewLessonsCommand.MESSAGE_USAGE));
    }
}
