package tutortrack.model.person;

import static java.util.Objects.requireNonNull;
import static tutortrack.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain alphanumeric characters and spaces, it should not be blank, "
                    + "and please check if you have input invalid characters.";

    /** Specific validation messages */
    public static final String MESSAGE_BLANK = "Name cannot be blank.";
    public static final String MESSAGE_INVALID_CHARS = "Names should only contain alphanumeric characters and spaces.";
    public static final String MESSAGE_TOO_SHORT = "Name is too short. It should be at least 2 characters long.";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = name;
    }

    /**
     * Returns true if the given name is blank (null or empty after trimming).
     */
    public static boolean isBlank(String test) {
        return test == null || test.trim().isEmpty();
    }

    /**
     * Returns true if the given name is shorter than the minimum allowed length.
     */
    public static boolean isTooShort(String test) {
        if (test == null) {
            return true;
        }
        return test.trim().length() < 2;
    }

    /**
     * Returns true if the given name contains invalid characters according to the validation regex.
     */
    public static boolean hasInvalidChars(String test) {
        if (test == null) {
            return true;
        }
        return !test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
