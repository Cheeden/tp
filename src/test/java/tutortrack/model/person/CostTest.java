package tutortrack.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutortrack.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class CostTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Cost(null));
    }

    @Test
    public void constructor_invalidCost_throwsIllegalArgumentException() {
        String invalidCost = "50"; // missing $
        assertThrows(IllegalArgumentException.class, () -> new Cost(invalidCost));
    }

    @Test
    public void hasTooManyDecimalPlaces_returnsTrueWhenMoreThanTwoDecimals() {
        assertTrue(Cost.hasTooManyDecimalPlaces("$50.123"));
        assertTrue(Cost.hasTooManyDecimalPlaces("$0.001"));
    }

    @Test
    public void hasTooManyDecimalPlaces_returnsFalseForValidOrNull() {
        assertFalse(Cost.hasTooManyDecimalPlaces("$50.12"));
        assertFalse(Cost.hasTooManyDecimalPlaces("$50"));
        assertFalse(Cost.hasTooManyDecimalPlaces(null));
    }

    @Test
    public void isMissingDollar_detectsMissingOrNull() {
        assertTrue(Cost.isMissingDollar(null));
        assertTrue(Cost.isMissingDollar("50"));
        assertFalse(Cost.isMissingDollar("$50"));
    }

    @Test
    public void isValidCost() {
        // null cost
        assertThrows(NullPointerException.class, () -> Cost.isValidCost(null));

        // invalid costs
        assertFalse(Cost.isValidCost("")); // empty string
        assertFalse(Cost.isValidCost(" ")); // spaces only
        assertFalse(Cost.isValidCost("50")); // missing $
        assertFalse(Cost.isValidCost("$")); // only $
        assertFalse(Cost.isValidCost("$12.345")); // too many decimal digits

        // valid costs
        assertTrue(Cost.isValidCost("$50"));
        assertTrue(Cost.isValidCost("$12.5"));
        assertTrue(Cost.isValidCost("$10.00"));
    }

    @Test
    public void equals() {
        Cost cost = new Cost("$50");

        // same values -> returns true
        assertTrue(cost.equals(new Cost("$50")));

        // same object -> returns true
        assertTrue(cost.equals(cost));

        // null -> returns false
        assertFalse(cost.equals(null));

        // different types -> returns false
        assertFalse(cost.equals(5.0f));

        // different values -> returns false
        assertFalse(cost.equals(new Cost("$100")));
    }
}
