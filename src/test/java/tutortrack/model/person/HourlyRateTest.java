package tutortrack.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutortrack.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class HourlyRateTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new HourlyRate(null));
    }

    @Test
    public void constructor_invalidCost_throwsIllegalArgumentException() {
        String invalidCost = "50"; // missing $
        assertThrows(IllegalArgumentException.class, () -> new HourlyRate(invalidCost));
    }

    @Test
    public void hasIncorrectDecimalPlaces_returnsTrueWhenMoreThanTwoDecimals() {
        assertTrue(HourlyRate.hasIncorrectDecimalPlaces("$50.123"));
        assertTrue(HourlyRate.hasIncorrectDecimalPlaces("$0.001"));
    }

    @Test
    public void hasIncorrectDecimalPlaces_returnsFalseForValidOrNull() {
        assertFalse(HourlyRate.hasIncorrectDecimalPlaces("$50.12"));
        assertFalse(HourlyRate.hasIncorrectDecimalPlaces("$50"));
        assertFalse(HourlyRate.hasIncorrectDecimalPlaces(null));
    }

    @Test
    public void isMissingDollar_detectsMissingOrNull() {
        assertTrue(HourlyRate.isMissingDollar(null));
        assertTrue(HourlyRate.isMissingDollar("50"));
        assertFalse(HourlyRate.isMissingDollar("$50"));
    }

    @Test
    public void isValidCost() {
        // null cost
        assertThrows(NullPointerException.class, () -> HourlyRate.isValidHourlyRate(null));

        // invalid costs
        assertFalse(HourlyRate.isValidHourlyRate("")); // empty string
        assertFalse(HourlyRate.isValidHourlyRate(" ")); // spaces only
        assertFalse(HourlyRate.isValidHourlyRate("50")); // missing $
        assertFalse(HourlyRate.isValidHourlyRate("$")); // only $
        assertFalse(HourlyRate.isValidHourlyRate("$12.5")); // cannot have 1 decimal digit
        assertFalse(HourlyRate.isValidHourlyRate("$12.345")); // too many decimal digits

        // valid costs
        assertTrue(HourlyRate.isValidHourlyRate("$50"));
        assertTrue(HourlyRate.isValidHourlyRate("$10.00"));
    }

    @Test
    public void equals() {
        HourlyRate cost = new HourlyRate("$50");

        // same values -> returns true
        assertTrue(cost.equals(new HourlyRate("$50")));

        // same object -> returns true
        assertTrue(cost.equals(cost));

        // null -> returns false
        assertFalse(cost.equals(null));

        // different types -> returns false
        assertFalse(cost.equals(5.0f));

        // different values -> returns false
        assertFalse(cost.equals(new HourlyRate("$100")));
    }
}
