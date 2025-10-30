package tutortrack.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutortrack.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static tutortrack.testutil.Assert.assertThrows;
import static tutortrack.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import tutortrack.logic.parser.exceptions.ParseException;
import tutortrack.model.lesson.LessonPlan;
import tutortrack.model.lesson.LessonProgress;
import tutortrack.model.person.Address;
import tutortrack.model.person.Cost;
import tutortrack.model.person.DayTime;
import tutortrack.model.person.Name;
import tutortrack.model.person.Phone;
import tutortrack.model.person.SubjectLevel;
import tutortrack.model.tag.Tag;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_SUBJECTLEVEL = "P4Math";
    private static final String INVALID_DAYTIME = "Monday 2500";
    private static final String INVALID_COST = "50";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_SUBJECTLEVEL = "P4-Math";
    private static final String VALID_COST = "$50";
    private static final String VALID_DAYTIME = "Monday 1200";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_blank_throwsParseExceptionWithMessage() {
        assertThrows(ParseException.class, Name.MESSAGE_BLANK, () -> ParserUtil.parseName(" "));
    }

    @Test
    public void parseName_tooShort_throwsParseExceptionWithMessage() {
        assertThrows(ParseException.class, Name.MESSAGE_TOO_SHORT, () -> ParserUtil.parseName("A"));
    }

    @Test
    public void parseName_invalidChars_throwsParseExceptionWithMessage() {
        assertThrows(ParseException.class, Name.MESSAGE_INVALID_CHARS, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_blank_throwsParseExceptionWithMessage() {
        assertThrows(ParseException.class, Phone.MESSAGE_BLANK, () -> ParserUtil.parsePhone(" "));
    }

    @Test
    public void parsePhone_tooShort_throwsParseExceptionWithMessage() {
        assertThrows(ParseException.class, Phone.MESSAGE_TOO_SHORT, () -> ParserUtil.parsePhone("12"));
    }

    @Test
    public void parsePhone_invalidChars_throwsParseExceptionWithMessage() {
        assertThrows(ParseException.class, Phone.MESSAGE_INVALID_CHARS, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseSubjectLevel_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseSubjectLevel((String) null));
    }

    @Test
    public void parseSubjectLevel_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseSubjectLevel(INVALID_SUBJECTLEVEL)); // missing dash
    }

    @Test
    public void parseSubjectLevel_blank_throwsParseExceptionWithMessage() {
        assertThrows(ParseException.class, SubjectLevel.MESSAGE_BLANK, () -> ParserUtil.parseSubjectLevel(" "));
    }

    @Test
    public void parseSubjectLevel_invalidFormat_throwsParseExceptionWithMessage() {
        assertThrows(ParseException.class, SubjectLevel.MESSAGE_INVALID_FORMAT, () ->
                ParserUtil.parseSubjectLevel(INVALID_SUBJECTLEVEL));
    }

    @Test
    public void parseSubjectLevel_invalidChars_throwsParseExceptionWithMessage() {
        assertThrows(ParseException.class, SubjectLevel.MESSAGE_INVALID_CHARS, () ->
                ParserUtil.parseSubjectLevel("P4-M4th"));
    }

    @Test
    public void parseSubjectLevel_validValueWithoutWhitespace_returnsSubjectLevel() throws Exception {
        SubjectLevel expectedSubjectLevel = new SubjectLevel(VALID_SUBJECTLEVEL);
        assertEquals(expectedSubjectLevel, ParserUtil.parseSubjectLevel(VALID_SUBJECTLEVEL));
    }

    @Test
    public void parseSubjectLevel_validValueWithWhitespace_returnsTrimmedSubjectLevel() throws Exception {
        String subjectLevelWithWhitespace = WHITESPACE + VALID_SUBJECTLEVEL + WHITESPACE;
        SubjectLevel expectedSubjectLevel = new SubjectLevel(VALID_SUBJECTLEVEL);
        assertEquals(expectedSubjectLevel, ParserUtil.parseSubjectLevel(subjectLevelWithWhitespace));
    }

    @Test
    public void parseDayTime_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseDayTime((String) null));
    }

    @Test
    public void parseDayTime_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, "Invalid time: '2500' is not a valid 24-hour time (HHMM).", () ->
                ParserUtil.parseDayTime(INVALID_DAYTIME));
    }

    @Test
    public void parseDayTime_validValueWithoutWhitespace_returnsDayTime() throws Exception {
        DayTime expectedDayTime = new DayTime(VALID_DAYTIME);
        assertEquals(expectedDayTime, ParserUtil.parseDayTime(VALID_DAYTIME));
    }

    @Test
    public void parseDayTime_validValueWithWhitespace_returnsTrimmedDayTime() throws Exception {
        String dayTimeWithWhitespace = WHITESPACE + VALID_DAYTIME + WHITESPACE;
        DayTime expectedDayTime = new DayTime(VALID_DAYTIME);
        assertEquals(expectedDayTime, ParserUtil.parseDayTime(dayTimeWithWhitespace));
    }

    @Test
    public void parseCost_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseCost((String) null));
    }

    @Test
    public void parseCost_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, Cost.MESSAGE_MISSING_DOLLAR, () ->
                ParserUtil.parseCost(INVALID_COST)); // missing $
    }

    @Test
    public void parseCost_tooManyDecimalPlaces_throwsParseException() {
        final String invalidTooMany = "$50.123";
        assertThrows(ParseException.class, Cost.MESSAGE_TOO_MANY_DECIMALS, () ->
                ParserUtil.parseCost(invalidTooMany));
    }

    @Test
    public void parseCost_validValueWithoutWhitespace_returnsCost() throws Exception {
        Cost expectedCost = new Cost(VALID_COST);
        assertEquals(expectedCost, ParserUtil.parseCost(VALID_COST));
    }

    @Test
    public void parseCost_validValueWithWhitespace_returnsTrimmedCost() throws Exception {
        String costWithWhitespace = WHITESPACE + VALID_COST + WHITESPACE;
        Cost expectedCost = new Cost(VALID_COST);
        assertEquals(expectedCost, ParserUtil.parseCost(costWithWhitespace));
    }

    @Test
    public void parseAddress_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress((String) null));
    }

    @Test
    public void parseAddress_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAddress(INVALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithoutWhitespace_returnsAddress() throws Exception {
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(VALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(addressWithWhitespace));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }

    @Test
    public void parseLessonProgress_validInput_success() throws Exception {
        LessonProgress pr = ParserUtil.parseLessonProgress("2025-10-25|Covered Chapter 1");
        assertEquals(LocalDate.of(2025, 10, 25), pr.getDate());
        assertEquals("Covered Chapter 1", pr.getProgress());
    }

    @Test
    public void parseLessonProgress_missingProgress_throwsParseException() {
        assertThrows(ParseException.class,
                tutortrack.model.lesson.LessonProgress.MESSAGE_CONSTRAINTS, () ->
                        ParserUtil.parseLessonProgress("2025-10-25"));
    }

    @Test
    public void parseLessonProgress_nullProgress_throwsParseException() {
        assertThrows(ParseException.class,
                "Progress cannot be empty.", () ->
                        ParserUtil.parseLessonProgress("2025-10-25|null"));
    }

    @Test
    public void parseLessonProgress_invalidDateFormat_throwsParseException() {
        assertThrows(ParseException.class,
                "Invalid date format. Use yyyy-MM-dd (e.g., 2025-10-15).", () ->
                        ParserUtil.parseLessonProgress("25-10-2025|Covered Chapter 1"));
    }

    @Test
    public void parseLessonProgress_swappedDayMonth_throwsParseException() {
        assertThrows(ParseException.class,
                "Invalid date: you may have swapped day and month. The format is yyyy-MM-dd.", () ->
                        ParserUtil.parseLessonProgress("2025-25-10|Covered Chapter 1"));
    }

    @Test
    public void parseLessonProgress_invalidDay_throwsParseException() {
        assertThrows(ParseException.class,
                "Invalid day for the given month. Please check your date.", () ->
                        ParserUtil.parseLessonProgress("2025-02-30|Covered Chapter 1"));
    }

    @Test
    public void parseLessonProgress_invalidMonth_throwsParseException() {
        assertThrows(ParseException.class,
                "Invalid month: must be between 01 and 12.", () ->
                        ParserUtil.parseLessonProgress("2025-13-15|Covered Chapter 1"));
    }

    @Test
    public void parseLessonProgress_withNewlineEscapeSequence_success() throws Exception {
        LessonProgress pr = ParserUtil.parseLessonProgress("2025-10-25|Line 1\\nLine 2\\nLine 3");
        assertEquals(LocalDate.of(2025, 10, 25), pr.getDate());
        assertEquals("Line 1\nLine 2\nLine 3", pr.getProgress());
    }

    @Test
    public void parseLessonProgress_withTabEscapeSequence_success() throws Exception {
        LessonProgress pr = ParserUtil.parseLessonProgress("2025-10-25|Item 1\\tItem 2");
        assertEquals(LocalDate.of(2025, 10, 25), pr.getDate());
        assertEquals("Item 1\tItem 2", pr.getProgress());
    }

    @Test
    public void parseLessonProgress_withBackslashEscapeSequence_success() throws Exception {
        LessonProgress pr = ParserUtil.parseLessonProgress("2025-10-25|Path: C:\\\\Users\\\\file");
        assertEquals(LocalDate.of(2025, 10, 25), pr.getDate());
        assertEquals("Path: C:\\Users\\file", pr.getProgress());
    }

    @Test
    public void parseLessonPlan_validInput_success() throws Exception {
        LessonPlan lp = ParserUtil.parseLessonPlan("2025-10-25|Review algebra");
        assert lp.getDate().equals(LocalDate.of(2025, 10, 25));
        assert lp.getPlan().equals("Review algebra");
    }

    @Test
    public void parseLessonPlan_missingPipe_throwsParseException() {
        assertThrows(ParseException.class,
                LessonPlan.MESSAGE_CONSTRAINTS, () ->
                        ParserUtil.parseLessonPlan("2025-10-25"));
    }

    @Test
    public void parseLessonPlan_emptyPlan_throwsParseException() {
        assertThrows(ParseException.class,
                "Plan cannot be empty.", () ->
                        ParserUtil.parseLessonPlan("2025-10-25|"));
    }

    @Test
    public void parseLessonPlan_invalidDateFormat_throwsParseException() {
        assertThrows(ParseException.class,
                "Invalid date format. Use yyyy-MM-dd (e.g., 2025-10-15).", () ->
                        ParserUtil.parseLessonPlan("25-10-2025|Review algebra"));
    }

    @Test
    public void parseLessonPlan_swappedDayMonth_throwsParseException() {
        assertThrows(ParseException.class,
                "Invalid date: you may have swapped day and month. The format is yyyy-MM-dd.", () ->
                        ParserUtil.parseLessonPlan("2025-30-09|Review algebra"));
    }

    @Test
    public void parseLessonPlan_invalidMonth_throwsParseException() {
        assertThrows(ParseException.class,
                "Invalid month: must be between 01 and 12.", () ->
                        ParserUtil.parseLessonPlan("2025-17-16|Review algebra"));
    }

    @Test
    public void parseLessonPlan_invalidDay_throwsParseException() {
        assertThrows(ParseException.class,
                "Invalid day for the given month. Please check your date.", () ->
                        ParserUtil.parseLessonPlan("2025-04-31|Review algebra"));
    }

    @Test
    public void parseLessonPlan_withNewlineEscapeSequence_success() throws Exception {
        LessonPlan lp = ParserUtil.parseLessonPlan("2025-10-25|Topic 1\\nTopic 2\\nTopic 3");
        assertEquals(LocalDate.of(2025, 10, 25), lp.getDate());
        assertEquals("Topic 1\nTopic 2\nTopic 3", lp.getPlan());
    }

    @Test
    public void parseLessonPlan_withTabEscapeSequence_success() throws Exception {
        LessonPlan lp = ParserUtil.parseLessonPlan("2025-10-25|Section A\\tSection B");
        assertEquals(LocalDate.of(2025, 10, 25), lp.getDate());
        assertEquals("Section A\tSection B", lp.getPlan());
    }

    @Test
    public void parseLessonPlan_withBackslashEscapeSequence_success() throws Exception {
        LessonPlan lp = ParserUtil.parseLessonPlan("2025-10-25|Formula: a\\\\b");
        assertEquals(LocalDate.of(2025, 10, 25), lp.getDate());
        assertEquals("Formula: a\\b", lp.getPlan());
    }

}
