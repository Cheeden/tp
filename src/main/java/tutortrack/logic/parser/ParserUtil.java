package tutortrack.logic.parser;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import tutortrack.commons.core.index.Index;
import tutortrack.commons.util.StringUtil;
import tutortrack.logic.parser.exceptions.ParseException;
import tutortrack.model.lesson.LessonProgress;
import tutortrack.model.person.Address;
import tutortrack.model.person.Name;
import tutortrack.model.person.Phone;
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
     */
    public static String parseSubjectLevel(String subjectLevel) {
        requireNonNull(subjectLevel);
        String trimmedSubjectLevel = subjectLevel.trim();
        return trimmedSubjectLevel;
    }

    /**
     * Parses a {@code String dayTime}.
     * Leading and trailing whitespaces will be trimmed.
     *
     */
    public static String parseDayTime(String dayTime) {
        requireNonNull(dayTime);
        String trimmedDayTime = dayTime.trim();
        return trimmedDayTime;
    }

    /**
     * Parses a {@code String subjectLevel}.
     * Leading and trailing whitespaces will be trimmed.
     *
     */
    public static String parseCost(String cost) {
        requireNonNull(cost);
        String trimmedCost = cost.trim();
        return trimmedCost;
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

        String dateStr = parts[0].trim();
        String progress = parts[1].trim();

        if (progress.isEmpty()) {
            throw new ParseException("Progress cannot be empty.");
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dateStr);
        } catch (Exception e) {
            throw new ParseException("Invalid date format for lesson progress: " + dateStr);
        }

        return new LessonProgress(date, progress);
    }
}
