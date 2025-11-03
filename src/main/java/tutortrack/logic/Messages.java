package tutortrack.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import tutortrack.logic.parser.Prefix;
import tutortrack.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid.";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_NO_PERSONS_FOUND =
            "Contact list is unchanged: No students match your search criteria.";
    public static final String MESSAGE_INVALID_DAY =
            "Invalid day: Please enter a valid day of the week without abbreviations (e.g. Monday to Sunday)";
    public static final String MESSAGE_FIND_MULTIPLE_PREFIXES =
            "Only one search type can be used at a time. "
            + "Please use either t/ (tag), s/ (subject), or d/ (day), but not multiple prefixes.";
    public static final String MESSAGE_FIND_DUPLICATE_TAG =
            "Duplicate t/ prefix found. To search multiple tags, separate keywords with spaces. "
            + "Example: find t/jake john";
    public static final String MESSAGE_FIND_DUPLICATE_DAY =
            "Duplicate d/ prefix found. Only one day can be searched at a time.";
    public static final String MESSAGE_FIND_DUPLICATE_SUBJECT =
            "Duplicate s/ prefix found. Only one subject can be searched at a time.";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Contact: ")
                .append(person.getSelfContact())
                .append("; NOK Contact: ")
                .append(person.getNokContact())
                .append(", SubjectLevel: ")
                .append(person.getSubjectLevel())
                .append(", DayTime: ")
                .append(person.getDayTime())
                .append(", HourlyRate: ")
                .append(person.getHourlyRate())
                .append("; Address: ")
                .append(person.getAddress())
                .append("; Tags: ");
        person.getTags().forEach(builder::append);
        return builder.toString();
    }

}
