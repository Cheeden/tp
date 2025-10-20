package tutortrack.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutortrack.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class SubjectLevelTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SubjectLevel(null));
    }

    @Test
    public void constructor_invalidSubjectLevel_throwsIllegalArgumentException() {
        String invalidSubjectLevel = "P4Math"; // missing dash
        assertThrows(IllegalArgumentException.class, () -> new SubjectLevel(invalidSubjectLevel));
    }

    @Test
    public void isValidSubjectLevel() {
        // null subject-level
        assertThrows(NullPointerException.class, () -> SubjectLevel.isValidSubjectLevel(null));

        // invalid subject-levels
        assertFalse(SubjectLevel.isValidSubjectLevel("")); // empty string
        assertFalse(SubjectLevel.isValidSubjectLevel(" ")); // spaces only
        assertFalse(SubjectLevel.isValidSubjectLevel("P4Math")); // no dash
        assertFalse(SubjectLevel.isValidSubjectLevel("P4-")); // missing subject
        assertFalse(SubjectLevel.isValidSubjectLevel("-Math")); // missing level
        assertFalse(SubjectLevel.isValidSubjectLevel("P4-Math-Extra")); // too many dashes

        // valid subject-levels
        assertTrue(SubjectLevel.isValidSubjectLevel("P4-Math"));
        assertTrue(SubjectLevel.isValidSubjectLevel("Sec2-English"));
        assertTrue(SubjectLevel.isValidSubjectLevel("J1-Physics"));
    }

    @Test
    public void equals() {
        SubjectLevel subjectLevel = new SubjectLevel("P4-Math");

        // same values -> returns true
        assertTrue(subjectLevel.equals(new SubjectLevel("P4-Math")));

        // same object -> returns true
        assertTrue(subjectLevel.equals(subjectLevel));

        // null -> returns false
        assertFalse(subjectLevel.equals(null));

        // different types -> returns false
        assertFalse(subjectLevel.equals(5.0f));

        // different values -> returns false
        assertFalse(subjectLevel.equals(new SubjectLevel("P5-Science")));
    }
}
