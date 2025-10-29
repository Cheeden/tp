package tutortrack.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static tutortrack.testutil.TypicalLessonPlans.LESSON_PLAN_1;

import org.junit.jupiter.api.Test;

import tutortrack.commons.exceptions.IllegalValueException;

public class JsonAdaptedLessonPlanTest {
    private static final String VALID_DATE = "2025-10-20";
    private static final String VALID_PLAN = "Cover Chapter 1";
    private static final String INVALID_DATE = "invalid-date";

    @Test
    public void toModelType_validLessonPlanDetails_returnsLessonPlan() throws Exception {
        JsonAdaptedLessonPlan lessonPlan = new JsonAdaptedLessonPlan(VALID_DATE, VALID_PLAN);
        assertEquals(LESSON_PLAN_1, lessonPlan.toModelType());
    }

    @Test
    public void toModelType_invalidDate_throwsIllegalValueException() {
        JsonAdaptedLessonPlan lessonPlan = new JsonAdaptedLessonPlan(INVALID_DATE, VALID_PLAN);
        assertThrows(Exception.class, lessonPlan::toModelType);
    }

    @Test
    public void toModelType_nullDate_throwsIllegalValueException() {
        JsonAdaptedLessonPlan lessonPlan = new JsonAdaptedLessonPlan(null, VALID_PLAN);
        String expectedMessage = String.format(JsonAdaptedLessonPlan.MISSING_FIELD_MESSAGE_FORMAT,
                "LessonPlan", "date or plan");
        assertThrows(IllegalValueException.class, lessonPlan::toModelType, expectedMessage);
    }

    @Test
    public void toModelType_nullPlan_throwsIllegalValueException() {
        JsonAdaptedLessonPlan lessonPlan = new JsonAdaptedLessonPlan(VALID_DATE, null);
        String expectedMessage = String.format(JsonAdaptedLessonPlan.MISSING_FIELD_MESSAGE_FORMAT,
                "LessonPlan", "date or plan");
        assertThrows(IllegalValueException.class, lessonPlan::toModelType, expectedMessage);
    }
}
