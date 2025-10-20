package tutortrack.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutortrack.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class DayTimeTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DayTime(null));
    }

    @Test
    public void constructor_invalidDayTime_throwsIllegalArgumentException() {
        String invalidDayTime = "Monday1200"; // missing space
        assertThrows(IllegalArgumentException.class, () -> new DayTime(invalidDayTime));
    }

    @Test
    public void isValidDayTime() {
        // null dayTime
        assertThrows(NullPointerException.class, () -> DayTime.isValidDayTime(null));

        // invalid dayTime
        assertFalse(DayTime.isValidDayTime("")); // empty string
        assertFalse(DayTime.isValidDayTime(" ")); // spaces only
        assertFalse(DayTime.isValidDayTime("Mon 1200")); // invalid day name
        assertFalse(DayTime.isValidDayTime("Monday1200")); // missing space
        assertFalse(DayTime.isValidDayTime("Monday 99:00")); // invalid format
        assertFalse(DayTime.isValidDayTime("Monday 2500")); // invalid time

        // valid dayTime
        assertTrue(DayTime.isValidDayTime("Monday 1200"));
        assertTrue(DayTime.isValidDayTime("Tuesday 1600"));
        assertTrue(DayTime.isValidDayTime("Friday 0830"));
    }

    @Test
    public void equals() {
        DayTime dayTime = new DayTime("Monday 1200");

        // same values -> returns true
        assertTrue(dayTime.equals(new DayTime("Monday 1200")));

        // same object -> returns true
        assertTrue(dayTime.equals(dayTime));

        // null -> returns false
        assertFalse(dayTime.equals(null));

        // different types -> returns false
        assertFalse(dayTime.equals(5.0f));

        // different values -> returns false
        assertFalse(dayTime.equals(new DayTime("Tuesday 1600")));
    }
}
