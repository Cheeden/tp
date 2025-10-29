package tutortrack.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static tutortrack.testutil.TypicalLessonProgresses.LESSON_PROGRESS_1;

import org.junit.jupiter.api.Test;

import tutortrack.commons.exceptions.IllegalValueException;

public class JsonAdaptedLessonProgressTest {
    private static final String VALID_DATE = "2024-01-15";
    private static final String VALID_PROGRESS = "Completed Chapter 1";
    private static final String INVALID_DATE = "invalid-date";

    @Test
    public void toModelType_validLessonProgressDetails_returnsLessonProgress() throws Exception {
        JsonAdaptedLessonProgress lessonProgress = new JsonAdaptedLessonProgress(VALID_DATE, VALID_PROGRESS);
        assertEquals(LESSON_PROGRESS_1, lessonProgress.toModelType());
    }

    @Test
    public void toModelType_invalidDate_throwsIllegalValueException() {
        JsonAdaptedLessonProgress lessonProgress = new JsonAdaptedLessonProgress(INVALID_DATE, VALID_PROGRESS);
        assertThrows(Exception.class, lessonProgress::toModelType);
    }

    @Test
    public void toModelType_nullDate_throwsIllegalValueException() {
        JsonAdaptedLessonProgress lessonProgress = new JsonAdaptedLessonProgress(null, VALID_PROGRESS);
        String expectedMessage = String.format(JsonAdaptedLessonProgress.MISSING_FIELD_MESSAGE_FORMAT,
                "LessonProgress", "date or progress");
        assertThrows(IllegalValueException.class, lessonProgress::toModelType, expectedMessage);
    }

    @Test
    public void toModelType_nullProgress_throwsIllegalValueException() {
        JsonAdaptedLessonProgress lessonProgress = new JsonAdaptedLessonProgress(VALID_DATE, null);
        String expectedMessage = String.format(JsonAdaptedLessonProgress.MISSING_FIELD_MESSAGE_FORMAT,
                "LessonProgress", "date or progress");
        assertThrows(IllegalValueException.class, lessonProgress::toModelType, expectedMessage);
    }
}
