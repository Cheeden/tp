package tutortrack.model.person;

import static java.util.Objects.requireNonNull;
import static tutortrack.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's tuition day and time in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDayTime(String)}
 */
public class DayTime {

    public static final String MESSAGE_CONSTRAINTS =
            "DayTime should be in the format 'Day HHMM', e.g., 'Monday 1200' or 'Tuesday 1600'.";

    // Regex explanation:
    // ^(Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday) - valid day names
    // \\s - exactly one space
    // (0[0-9]|1[0-9]|2[0-3])[0-5][0-9]) - time in HHMM (00:00–23:59)
    // $ - end
    public static final String VALIDATION_REGEX =
            "^(?i)(Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday)\\s((0[0-9]|1[0-9]|2[0-3])[0-5][0-9])$";

    public final String value;

    /**
     * Constructs a {@code DayTime}.
     *
     * @param dayTime A valid day-time string.
     */
    public DayTime(String dayTime) {
        requireNonNull(dayTime);
        checkArgument(isValidDayTime(dayTime), MESSAGE_CONSTRAINTS);
        value = dayTime;
    }

    /**
     * Returns true if a given string is a valid day-time.
     */
    public static boolean isValidDayTime(String test) {
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
        if (!(other instanceof DayTime)) {
            return false;
        }

        DayTime otherDayTime = (DayTime) other;
        return value.equals(otherDayTime.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
