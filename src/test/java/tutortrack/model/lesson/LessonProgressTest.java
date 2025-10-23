package tutortrack.model.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutortrack.testutil.Assert.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class LessonProgressTest {

    @Test
    void constructor_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new LessonProgress(null, "Discussed functions"));
    }

    @Test
    void constructor_nullProgress_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new LessonProgress(LocalDate.now(), null));
    }

    @Test
    public void constructor_validInputs_success() {
        LocalDate date = LocalDate.of(2023, 10, 15);
        String progress = "Completed Chapter 1";
        LessonProgress lessonProgress = new LessonProgress(date, progress);

        assertEquals(date, lessonProgress.getDate());
        assertEquals(progress, lessonProgress.getProgress());
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        LocalDate date = LocalDate.of(2023, 10, 15);
        String progress = "Completed Chapter 1";
        LessonProgress lessonProgress1 = new LessonProgress(date, progress);
        LessonProgress lessonProgress2 = new LessonProgress(date, progress);

        assertTrue(lessonProgress1.equals(lessonProgress2));
    }

    @Test
    public void equals_differentValues_returnsFalse() {
        LessonProgress lessonProgress1 = new LessonProgress(LocalDate.of(2023, 10, 15), "Chapter 1");
        LessonProgress lessonProgress2 = new LessonProgress(LocalDate.of(2023, 10, 16), "Chapter 2");

        assertFalse(lessonProgress1.equals(lessonProgress2));
    }
}
