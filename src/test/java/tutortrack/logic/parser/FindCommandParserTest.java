package tutortrack.logic.parser;

import static tutortrack.logic.Messages.MESSAGE_FIND_DUPLICATE_DAY;
import static tutortrack.logic.Messages.MESSAGE_FIND_DUPLICATE_SUBJECT;
import static tutortrack.logic.Messages.MESSAGE_FIND_DUPLICATE_TAG;
import static tutortrack.logic.Messages.MESSAGE_FIND_MULTIPLE_PREFIXES;
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

    // EP: Empty arguments
    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    // EP: Valid name search with single and multiple keywords
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

    // EP: Valid tag search with single and multiple keywords
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

    // EP: Empty tag value
    @Test
    public void parse_emptyTagValue_throwsParseException() {
        assertParseFailure(parser, " t/", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    // EP: Valid day search with different case variations
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

    // EP: Invalid day inputs - typo, natural language, abbreviation, nonsense word, numeric
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

    // EP: Valid subject level search with different case variations
    @Test
    public void parse_validSubjectLevel_returnsFindCommand() throws Exception {
        // exact case
        FindCommand expected = new FindCommand(new SubjectLevelMatchesPredicate("P4-Math"));
        assertParseSuccess(parser, " s/P4-Math", expected);

        // different case should still work
        assertParseSuccess(parser, " s/p4-math", expected);
    }

    // EP: Empty subject value
    @Test
    public void parse_emptySubject_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        assertParseFailure(parser, " s/ ", expectedMessage);
    }

    // EP: Preamble with tag prefix (invalid format)
    @Test
    public void parse_preambleWithTagPrefix_throwsParseException() {
        // Should reject when both preamble and tag prefix are present
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "john t/exams", expectedMessage);
        assertParseFailure(parser, "alice bob t/friends", expectedMessage);
    }

    // EP: Preamble with day prefix (invalid format)
    @Test
    public void parse_preambleWithDayPrefix_throwsParseException() {
        // Should reject when both preamble and day prefix are present
        String expectedMessage = "Invalid day search format. Use 'find d/Monday' "
            + "(use full weekday names and no abbreviations).";
        assertParseFailure(parser, "alice d/Monday", expectedMessage);
        assertParseFailure(parser, "john doe d/Friday", expectedMessage);
    }

    // EP: Preamble with subject prefix (invalid format)
    @Test
    public void parse_preambleWithSubjectPrefix_throwsParseException() {
        // Should reject when both preamble and subject prefix are present
        String expectedMessage = "Invalid subject search format. The subject-level must be a single token "
                + "in the form Level-Subject (no spaces). Example: find s/P4-Math";
        assertParseFailure(parser, "bob s/P4-Math", expectedMessage);
        assertParseFailure(parser, "alice s/S2-Science", expectedMessage);
    }

    // EP: Multiple different prefixes used together (invalid)
    @Test
    public void parse_multiplePrefixes_throwsParseException() {
        // Should reject when multiple different prefixes are used together
        assertParseFailure(parser, " t/friends d/Monday", MESSAGE_FIND_MULTIPLE_PREFIXES);
        assertParseFailure(parser, " d/Tuesday s/P4-Math", MESSAGE_FIND_MULTIPLE_PREFIXES);
        assertParseFailure(parser, " s/Sec1-English t/exams", MESSAGE_FIND_MULTIPLE_PREFIXES);
        assertParseFailure(parser, " t/friends d/Monday s/P4-Math", MESSAGE_FIND_MULTIPLE_PREFIXES);
    }

    // EP: Duplicate tag prefix (invalid)
    @Test
    public void parse_duplicateTagPrefix_throwsParseException() {
        // Should reject when tag prefix is used multiple times
        assertParseFailure(parser, " t/friends t/colleagues", MESSAGE_FIND_DUPLICATE_TAG);
        assertParseFailure(parser, " t/exams t/homework t/test", MESSAGE_FIND_DUPLICATE_TAG);
    }

    // EP: Duplicate day prefix (invalid)
    @Test
    public void parse_duplicateDayPrefix_throwsParseException() {
        // Should reject when day prefix is used multiple times
        assertParseFailure(parser, " d/Monday d/Tuesday", MESSAGE_FIND_DUPLICATE_DAY);
        assertParseFailure(parser, " d/Friday d/Friday", MESSAGE_FIND_DUPLICATE_DAY);
    }

    // EP: Duplicate subject prefix (invalid)
    @Test
    public void parse_duplicateSubjectPrefix_throwsParseException() {
        // Should reject when subject prefix is used multiple times
        assertParseFailure(parser, " s/P4-Math s/Sec1-English", MESSAGE_FIND_DUPLICATE_SUBJECT);
        assertParseFailure(parser, " s/P4-Math s/P4-Science s/P5-Math", MESSAGE_FIND_DUPLICATE_SUBJECT);
    }
}
