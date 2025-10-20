package tutortrack.model.person;

import static java.util.Objects.requireNonNull;
import static tutortrack.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's tuition cost in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidCost(String)}
 */
public class Cost {

    public static final String MESSAGE_CONSTRAINTS =
            "Cost should start with '$' followed by a valid number, e.g., '$50' or '$12.50'.";

    // Regex explanation:
    // ^\\$ - starts with a dollar sign
    // \\d+ - one or more digits
    // (\\.\\d{1,2})? - optional decimal point followed by 1 or 2 digits
    // $ - end of string
    public static final String VALIDATION_REGEX = "^\\$\\d+(\\.\\d{1,2})?$";

    public final String value;

    /**
     * Constructs a {@code Cost}.
     *
     * @param cost A valid cost string.
     */
    public Cost(String cost) {
        requireNonNull(cost);
        checkArgument(isValidCost(cost), MESSAGE_CONSTRAINTS);
        value = cost;
    }

    /**
     * Returns true if a given string is a valid cost.
     */
    public static boolean isValidCost(String test) {
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
        if (!(other instanceof Cost)) {
            return false;
        }

        Cost otherCost = (Cost) other;
        return value.equals(otherCost.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}