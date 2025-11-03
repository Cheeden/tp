package tutortrack.model.person;

import static java.util.Objects.requireNonNull;
import static tutortrack.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's tuition hourly rate in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidHourlyRate(String)}
 */
public class HourlyRate {

    public static final String MESSAGE_CONSTRAINTS =
            "Hourly rate should start with '$' followed by a valid number, e.g., '$50' or '$12.50'.";
    /** Specific validation messages */
    public static final String MESSAGE_TOO_MANY_DECIMALS = "Hourly rate may have 0 or 2 decimal places.";
    public static final String MESSAGE_MISSING_DOLLAR = "Hourly rate should start with '$'.";

    // Regex explanation:
    // ^\\$ - starts with a dollar sign
    // \\d+ - one or more digits
    // (\\.\\d{1,2})? - optional decimal point followed by 2 digits
    // $ - end of string
    public static final String VALIDATION_REGEX = "^\\$\\d+(\\.\\d{2})?$";

    public final String value;

    /**
     * Constructs a {@code HourlyRate}.
     *
     * @param hourlyRate A valid hourlyRate string.
     */
    public HourlyRate(String hourlyRate) {
        requireNonNull(hourlyRate);
        checkArgument(isValidHourlyRate(hourlyRate), MESSAGE_CONSTRAINTS);
        value = hourlyRate;
    }

    /**
     * Returns true if a given string is a valid cost.
     */
    public static boolean isValidHourlyRate(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /** Returns true if the given cost string has exactly 1 or more than 2 decimal places. */
    public static boolean hasIncorrectDecimalPlaces(String test) {
        if (test == null) {
            return false;
        }
        return test.matches("^\\$\\d+\\.\\d{1}$") || test.matches("^\\$\\d+\\.\\d{3,}$");
    }

    /** Returns true if the given cost string is missing the leading dollar sign. */
    public static boolean isMissingDollar(String test) {
        if (test == null) {
            return true;
        }
        return !test.startsWith("$");
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
        if (!(other instanceof HourlyRate)) {
            return false;
        }

        HourlyRate otherCost = (HourlyRate) other;
        return value.equals(otherCost.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
