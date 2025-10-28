package tutortrack.model.person;

import static java.util.Objects.requireNonNull;
import static tutortrack.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's phone number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPhone(String)}
 */
public class Phone {


    public static final String MESSAGE_CONSTRAINTS =
            "Phone numbers should only contain numbers, and it should be at least 3 digits long, "
                    + "please check if you have input invalid characters.";
    /** Specific validation messages */
    public static final String MESSAGE_BLANK = "Phone number cannot be blank.";
    public static final String MESSAGE_INVALID_CHARS = "Phone numbers should only contain digits.";
    public static final String MESSAGE_TOO_SHORT = "Phone number is too short; it should be at least 3 digits.";
    public static final String VALIDATION_REGEX = "\\d{3,}";
    public final String value;

    /**
     * Constructs a {@code Phone}.
     *
     * @param phone A valid phone number.
     */
    public Phone(String phone) {
        requireNonNull(phone);
        checkArgument(isValidPhone(phone), MESSAGE_CONSTRAINTS);
        value = phone;
    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidPhone(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /** Returns true if the given phone string is blank (null or empty after trimming). */
    public static boolean isBlank(String test) {
        return test == null || test.trim().isEmpty();
    }

    /** Returns true if the given phone string contains non-digit characters. */
    public static boolean hasInvalidChars(String test) {
        if (test == null) {
            return true;
        }
        return !test.matches("\\d+");
    }

    /** Returns true if the given phone string is shorter than the minimum length. */
    public static boolean isTooShort(String test) {
        if (test == null) {
            return true;
        }
        return test.trim().length() < 3;
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
        if (!(other instanceof Phone)) {
            return false;
        }

        Phone otherPhone = (Phone) other;
        return value.equals(otherPhone.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
