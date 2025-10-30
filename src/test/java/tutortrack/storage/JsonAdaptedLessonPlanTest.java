package tutortrack.storage;

import static tutortrack.testutil.TypicalLessonPlans.LESSON_PLAN_1;

import tutortrack.model.lesson.LessonPlan;

public class JsonAdaptedLessonPlanTest extends JsonAdaptedLessonTest<LessonPlan> {
    private static final String VALID_DATE = "2025-10-20";
    private static final String VALID_PLAN = "Cover Chapter 1";
    private static final String INVALID_DATE = "invalid-date";

    @Override
    protected JsonAdaptedLessonItem<LessonPlan> getValidItem() {
        return new JsonAdaptedLessonPlan(VALID_DATE, VALID_PLAN);
    }

    @Override
    protected LessonPlan getExpectedModel() {
        return LESSON_PLAN_1;
    }

    @Override
    protected JsonAdaptedLessonItem<LessonPlan> getInvalidDateItem() {
        return new JsonAdaptedLessonPlan(INVALID_DATE, VALID_PLAN);
    }

    @Override
    protected JsonAdaptedLessonItem<LessonPlan> getNullDateItem() {
        return new JsonAdaptedLessonPlan(null, VALID_PLAN);
    }

    @Override
    protected JsonAdaptedLessonItem<LessonPlan> getNullContentItem() {
        return new JsonAdaptedLessonPlan(VALID_DATE, null);
    }

    @Override
    protected String getTypeName() {
        return "LessonPlan";
    }

    @Override
    protected String getContentFieldName() {
        return "plan";
    }
}
