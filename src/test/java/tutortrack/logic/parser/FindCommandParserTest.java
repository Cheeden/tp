package tutortrack.logic.parser;

import static tutortrack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutortrack.logic.Messages.MESSAGE_INVALID_DAY;
import static tutortrack.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tutortrack.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import tutortrack.logic.commands.FindCommand;
import tutortrack.model.person.LessonDayPredicate;
import tutortrack.model.person.NameContainsKeywordsPredicate;
import tutortrack.model.person.SubjectLevelMatchesPredicate;
import tutortrack.model.person.TagContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob"));
        FindCommand expectedFindCommand = new FindCommand(predicate, predicate.getComparator());
        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        NameContainsKeywordsPredicate predicate2 = new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob"));
        FindCommand expectedFindCommand2 = new FindCommand(predicate2, predicate2.getComparator());
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand2);
    }

    @Test
    public void parse_validTagArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new TagContainsKeywordsPredicate(Arrays.asList("friends", "colleagues")));
        assertParseSuccess(parser, " t/friends colleagues", expectedFindCommand);

        // single tag keyword
        expectedFindCommand = new FindCommand(new TagContainsKeywordsPredicate(Arrays.asList("friends")));
        assertParseSuccess(parser, " t/friends", expectedFindCommand);
    }

    @Test
    public void parse_emptyTagValue_throwsParseException() {
        assertParseFailure(parser, " t/", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validDayArgs_returnsFindCommand() {
        // valid day with proper capitalization
        LessonDayPredicate predicate = new LessonDayPredicate("Monday");
        FindCommand expectedFindCommand = new FindCommand(predicate, predicate.getComparator());
        assertParseSuccess(parser, " d/Monday", expectedFindCommand);

        // lowercase day
        LessonDayPredicate predicate2 = new LessonDayPredicate("tuesday");
        FindCommand expectedFindCommand2 = new FindCommand(predicate2, predicate2.getComparator());
        assertParseSuccess(parser, " d/tuesday", expectedFindCommand2);

        // uppercase day
        LessonDayPredicate predicate3 = new LessonDayPredicate("WEDNESDAY");
        FindCommand expectedFindCommand3 = new FindCommand(predicate3, predicate3.getComparator());
        assertParseSuccess(parser, " d/WEDNESDAY", expectedFindCommand3);
    }

    @Test
    public void parse_invalidDay_throwsParseException() {
        // Invalid day name with typo
        assertParseFailure(parser, " d/Mondayy", MESSAGE_INVALID_DAY);
        
        // Random text
        assertParseFailure(parser, " d/Tomorrow", MESSAGE_INVALID_DAY);
        
        // Abbreviation
        assertParseFailure(parser, " d/Mon", MESSAGE_INVALID_DAY);
        
        // Invalid day name
        assertParseFailure(parser, " d/Someday", MESSAGE_INVALID_DAY);
        
        // Number
        assertParseFailure(parser, " d/1", MESSAGE_INVALID_DAY);
    }

    @Test
    public void parse_validSubjectLevel_returnsFindCommand() throws Exception {
        // exact case
        FindCommand expected = new FindCommand(new SubjectLevelMatchesPredicate("P4-Math"));
        assertParseSuccess(parser, " s/P4-Math", expected);

        // different case should still work
        assertParseSuccess(parser, " s/p4-math", expected);
    }

    @Test
    public void parse_emptySubject_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        assertParseFailure(parser, " s/ ", expectedMessage);
    }
}
