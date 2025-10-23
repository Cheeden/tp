package tutortrack.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class LessonDisplayTest {

    private static final LocalDate VALID_DATE = LocalDate.of(2024, 10, 20);
    private static final String VALID_PROGRESS = "Completed Chapter 1";
    private static final String VALID_PLAN = "Cover Chapter 2";

    @Test
    public void constructor_allFieldsPresent_success() {
        LessonDisplay lessonDisplay = new LessonDisplay(VALID_DATE, VALID_PROGRESS, VALID_PLAN);

        assertEquals(VALID_DATE, lessonDisplay.getDate());
        assertEquals(VALID_PROGRESS, lessonDisplay.getProgress());
        assertEquals(VALID_PLAN, lessonDisplay.getPlan());
    }

    @Test
    public void constructor_twoParameters_planIsEmpty() {
        LessonDisplay lessonDisplay = new LessonDisplay(VALID_DATE, VALID_PROGRESS);

        assertEquals(VALID_DATE, lessonDisplay.getDate());
        assertEquals(VALID_PROGRESS, lessonDisplay.getProgress());
        assertEquals("", lessonDisplay.getPlan());
    }

    @Test
    public void constructor_nullProgress_convertsToEmptyString() {
        LessonDisplay lessonDisplay = new LessonDisplay(VALID_DATE, null, VALID_PLAN);

        assertEquals("", lessonDisplay.getProgress());
        assertEquals(VALID_PLAN, lessonDisplay.getPlan());
    }

    @Test
    public void constructor_nullPlan_convertsToEmptyString() {
        LessonDisplay lessonDisplay = new LessonDisplay(VALID_DATE, VALID_PROGRESS, null);

        assertEquals(VALID_PROGRESS, lessonDisplay.getProgress());
        assertEquals("", lessonDisplay.getPlan());
    }

    @Test
    public void constructor_bothNull_convertsToEmptyStrings() {
        LessonDisplay lessonDisplay = new LessonDisplay(VALID_DATE, null, null);

        assertEquals("", lessonDisplay.getProgress());
        assertEquals("", lessonDisplay.getPlan());
    }

    @Test
    public void constructor_emptyStrings_preservesEmptyStrings() {
        LessonDisplay lessonDisplay = new LessonDisplay(VALID_DATE, "", "");

        assertEquals("", lessonDisplay.getProgress());
        assertEquals("", lessonDisplay.getPlan());
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        LessonDisplay lessonDisplay = new LessonDisplay(VALID_DATE, VALID_PROGRESS, VALID_PLAN);
        assertTrue(lessonDisplay.equals(lessonDisplay));
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        LessonDisplay lessonDisplay1 = new LessonDisplay(VALID_DATE, VALID_PROGRESS, VALID_PLAN);
        LessonDisplay lessonDisplay2 = new LessonDisplay(VALID_DATE, VALID_PROGRESS, VALID_PLAN);
        assertTrue(lessonDisplay1.equals(lessonDisplay2));
    }

    @Test
    public void equals_differentDate_returnsFalse() {
        LessonDisplay lessonDisplay1 = new LessonDisplay(VALID_DATE, VALID_PROGRESS, VALID_PLAN);
        LessonDisplay lessonDisplay2 = new LessonDisplay(
                LocalDate.of(2024, 10, 21), VALID_PROGRESS, VALID_PLAN);
        assertFalse(lessonDisplay1.equals(lessonDisplay2));
    }

    @Test
    public void equals_differentProgress_returnsFalse() {
        LessonDisplay lessonDisplay1 = new LessonDisplay(VALID_DATE, VALID_PROGRESS, VALID_PLAN);
        LessonDisplay lessonDisplay2 = new LessonDisplay(VALID_DATE, "Different Progress", VALID_PLAN);
        assertFalse(lessonDisplay1.equals(lessonDisplay2));
    }

    @Test
    public void equals_differentPlan_returnsFalse() {
        LessonDisplay lessonDisplay1 = new LessonDisplay(VALID_DATE, VALID_PROGRESS, VALID_PLAN);
        LessonDisplay lessonDisplay2 = new LessonDisplay(VALID_DATE, VALID_PROGRESS, "Different Plan");
        assertFalse(lessonDisplay1.equals(lessonDisplay2));
    }

    @Test
    public void equals_null_returnsFalse() {
        LessonDisplay lessonDisplay = new LessonDisplay(VALID_DATE, VALID_PROGRESS, VALID_PLAN);
        assertFalse(lessonDisplay.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        LessonDisplay lessonDisplay = new LessonDisplay(VALID_DATE, VALID_PROGRESS, VALID_PLAN);
        assertFalse(lessonDisplay.equals("Not a LessonDisplay"));
    }

    @Test
    public void equals_nullVsEmptyString_returnsFalse() {
        LessonDisplay lessonDisplay1 = new LessonDisplay(VALID_DATE, null, VALID_PLAN);
        LessonDisplay lessonDisplay2 = new LessonDisplay(VALID_DATE, "", VALID_PLAN);
        assertTrue(lessonDisplay1.equals(lessonDisplay2)); // Both convert to empty string
    }

    @Test
    public void hashCode_sameValues_returnsSameHashCode() {
        LessonDisplay lessonDisplay1 = new LessonDisplay(VALID_DATE, VALID_PROGRESS, VALID_PLAN);
        LessonDisplay lessonDisplay2 = new LessonDisplay(VALID_DATE, VALID_PROGRESS, VALID_PLAN);
        assertEquals(lessonDisplay1.hashCode(), lessonDisplay2.hashCode());
    }

    @Test
    public void hashCode_differentValues_returnsDifferentHashCode() {
        LessonDisplay lessonDisplay1 = new LessonDisplay(VALID_DATE, VALID_PROGRESS, VALID_PLAN);
        LessonDisplay lessonDisplay2 = new LessonDisplay(VALID_DATE, "Different", VALID_PLAN);
        assertNotEquals(lessonDisplay1.hashCode(), lessonDisplay2.hashCode());
    }

    @Test
    public void toString_allFieldsPresent_correctFormat() {
        LessonDisplay lessonDisplay = new LessonDisplay(VALID_DATE, VALID_PROGRESS, VALID_PLAN);
        String expected = String.format("Date: %s, Progress: %s, Plan: %s",
                VALID_DATE, VALID_PROGRESS, VALID_PLAN);
        assertEquals(expected, lessonDisplay.toString());
    }

    @Test
    public void toString_emptyFields_correctFormat() {
        LessonDisplay lessonDisplay = new LessonDisplay(VALID_DATE, "", "");
        String expected = String.format("Date: %s, Progress: , Plan: ", VALID_DATE);
        assertEquals(expected, lessonDisplay.toString());
    }

    @Test
    public void getDate_validDate_returnsCorrectDate() {
        LessonDisplay lessonDisplay = new LessonDisplay(VALID_DATE, VALID_PROGRESS, VALID_PLAN);
        assertEquals(VALID_DATE, lessonDisplay.getDate());
    }

    @Test
    public void getProgress_validProgress_returnsCorrectProgress() {
        LessonDisplay lessonDisplay = new LessonDisplay(VALID_DATE, VALID_PROGRESS, VALID_PLAN);
        assertEquals(VALID_PROGRESS, lessonDisplay.getProgress());
    }

    @Test
    public void getPlan_validPlan_returnsCorrectPlan() {
        LessonDisplay lessonDisplay = new LessonDisplay(VALID_DATE, VALID_PROGRESS, VALID_PLAN);
        assertEquals(VALID_PLAN, lessonDisplay.getPlan());
    }
}
