package tutortrack.logic.parser;

import static java.util.Objects.requireNonNull;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import tutortrack.commons.core.index.Index;
import tutortrack.commons.util.StringUtil;
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

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        // Provide more specific error messages for common failure modes.
        if (Name.isBlank(trimmedName)) {
            throw new ParseException(Name.MESSAGE_BLANK);
        }
        if (Name.isTooShort(trimmedName)) {
            throw new ParseException(Name.MESSAGE_TOO_SHORT);
        }
        if (Name.hasInvalidChars(trimmedName)) {
            throw new ParseException(Name.MESSAGE_INVALID_CHARS);
        }
        // final defensive check
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        // Provide more specific error messages for phone
        if (Phone.isBlank(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_BLANK);
        }
        if (Phone.hasInvalidChars(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_INVALID_CHARS);
        }
        if (Phone.isTooShort(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_TOO_SHORT);
        }
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String subjectLevel}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code subjectLevel} is invalid.
     */
    public static SubjectLevel parseSubjectLevel(String subjectLevel) throws ParseException {
        requireNonNull(subjectLevel);
        String trimmedSubjectLevel = subjectLevel.trim();
        if (SubjectLevel.isBlank(trimmedSubjectLevel)) {
            throw new ParseException(SubjectLevel.MESSAGE_BLANK);
        }
        if (SubjectLevel.hasInvalidFormat(trimmedSubjectLevel)) {
            throw new ParseException(SubjectLevel.MESSAGE_INVALID_FORMAT);
        }
        if (SubjectLevel.hasInvalidChars(trimmedSubjectLevel)) {
            throw new ParseException(SubjectLevel.MESSAGE_INVALID_CHARS);
        }
        if (!SubjectLevel.isValidSubjectLevel(trimmedSubjectLevel)) {
            throw new ParseException(SubjectLevel.MESSAGE_CONSTRAINTS);
        }
        return new SubjectLevel(trimmedSubjectLevel);
    }

    /**
     * Parses a {@code String dayTime}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code dayTime} is invalid.
     */
    public static DayTime parseDayTime(String dayTime) throws ParseException {
        requireNonNull(dayTime);
        String trimmedDayTime = dayTime.trim();
        if (!DayTime.isValidDayTime(trimmedDayTime)) {
            throw new ParseException(DayTime.MESSAGE_CONSTRAINTS);
        }
        return new DayTime(trimmedDayTime);
    }

    /**
     * Parses a {@code String subjectLevel}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code cost} is invalid.
     */
    public static Cost parseCost(String cost) throws ParseException {
        requireNonNull(cost);
        String trimmedCost = cost.trim();
        if (!Cost.isValidCost(trimmedCost)) {
            throw new ParseException(Cost.MESSAGE_CONSTRAINTS);
        }
        return new Cost(trimmedCost);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String lesson progress}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static LessonProgress parseLessonProgress(String input) throws ParseException {
        requireNonNull(input);
        String[] parts = input.split("\\|", 2);

        if (parts.length < 2) {
            throw new ParseException(tutortrack.model.lesson.LessonProgress.MESSAGE_CONSTRAINTS);
        }

        String dateString = parts[0].trim();
        String progress = parts[1].trim();

        if (progress.isEmpty() || progress.equals("null")) {
            throw new ParseException("Progress cannot be empty.");
        }

        try {
            LocalDate date = LocalDate.parse(dateString);
            return new LessonProgress(date, progress);
        } catch (DateTimeParseException e) {
            if (!dateString.matches("\\d{4}-\\d{2}-\\d{2}")) {
                throw new ParseException("Invalid date format. Use yyyy-MM-dd.", e);
            }

            String[] dateParts = dateString.split("-");
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int day = Integer.parseInt(dateParts[2]);

            if (month > 12 && day <= 12) {
                throw new ParseException(
                        "Invalid date: you may have swapped day and month. The format is yyyy-MM-dd.", e);
            }

            if (month < 1 || month > 12) {
                throw new ParseException("Invalid month: must be between 01 and 12.", e);
            }

            try {
                LocalDate.of(year, month, day);
                return new LessonProgress(LocalDate.of(year, month, day), progress);
            } catch (DateTimeException ex) {
                throw new ParseException("Invalid day for the given month. Please check your date.", e);
            }
        }
    }

    /**
     * Parses a {@code String lesson plan}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static LessonPlan parseLessonPlan(String input) throws ParseException {
        requireNonNull(input);
        String[] parts = input.split("\\|", 2);

        if (parts.length < 2) {
            throw new ParseException(tutortrack.model.lesson.LessonPlan.MESSAGE_CONSTRAINTS);
        }

        String dateString = parts[0].trim();
        String plan = parts[1].trim();
        if (plan.isEmpty()) {
            throw new ParseException("Plan cannot be empty.");
        }

        try {
            LocalDate date = LocalDate.parse(dateString);
            return new LessonPlan(date, plan);
        } catch (DateTimeParseException e) {
            if (!dateString.matches("\\d{4}-\\d{2}-\\d{2}")) {
                throw new ParseException("Invalid date format. Use yyyy-MM-dd.", e);
            }
            String[] dateParts = dateString.split("-");
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int day = Integer.parseInt(dateParts[2]);

            if (month > 12 && day <= 12) {
                throw new ParseException(
                        "Invalid date: you may have swapped day and month. The format is yyyy-MM-dd.", e);
            }

            if (month < 1 || month > 12) {
                throw new ParseException("Invalid month: must be between 01 and 12.", e);
            }

            try {
                LocalDate.of(year, month, day);
                return new LessonPlan(LocalDate.of(year, month, day), plan);
            } catch (DateTimeException ex) {
                throw new ParseException("Invalid day for the given month. Please check your date.", e);
            }
        }
    }
}
