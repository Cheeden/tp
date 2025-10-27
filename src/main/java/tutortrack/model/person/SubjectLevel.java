package tutortrack.model.person;

import static java.util.Objects.requireNonNull;
import static tutortrack.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's subject and level in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidSubjectLevel(String)}
 */
public class SubjectLevel {
    public static final String MESSAGE_CONSTRAINTS =
            "Subject level must be in the format 'Level-Subject', e.g., 'P4-Math'. "
            + "Both parts must be non-empty and separated by a single dash. "
            + "Please check if you have input invalid characters.";

    // Regex explanation:
    // ^ - start of string
    // [A-Za-z0-9]+ - one or more alphanumeric characters for the level (e.g., P4, Sec2)
    // - - must include a dash
    // [A-Za-z]+ - one or more alphabetic characters for the subject (e.g., Math, English)
    // $ - end of string
    public static final String VALIDATION_REGEX = "^[A-Za-z0-9]+-[A-Za-z]+$";

    public final String value;

    /**
     * Constructs a {@code SubjectLevel}.
     *
     * @param subjectLevel A valid subject-level string.
     */
    public SubjectLevel(String subjectLevel) {
        requireNonNull(subjectLevel);
        checkArgument(isValidSubjectLevel(subjectLevel), MESSAGE_CONSTRAINTS);
        value = subjectLevel;
    }

    /**
     * Returns true if a given string is a valid subject-level.
     */
    public static boolean isValidSubjectLevel(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SubjectLevel)) {
            return false;
        }

        SubjectLevel otherSubjectLevel = (SubjectLevel) other;
        return value.equals(otherSubjectLevel.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
