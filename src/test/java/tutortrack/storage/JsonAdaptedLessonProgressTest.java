package tutortrack.storage;

import static tutortrack.testutil.TypicalLessonProgresses.LESSON_PROGRESS_1;

import tutortrack.model.lesson.LessonProgress;

public class JsonAdaptedLessonProgressTest extends JsonAdaptedLessonTest<LessonProgress> {
    private static final String VALID_DATE = "2024-01-15";
    private static final String VALID_PROGRESS = "Completed Chapter 1";
    private static final String INVALID_DATE = "invalid-date";

    @Override
    protected JsonAdaptedLessonItem<LessonProgress> getValidItem() {
        return new JsonAdaptedLessonProgress(VALID_DATE, VALID_PROGRESS);
    }

    @Override
    protected LessonProgress getExpectedModel() {
        return LESSON_PROGRESS_1;
    }

    @Override
    protected JsonAdaptedLessonItem<LessonProgress> getInvalidDateItem() {
        return new JsonAdaptedLessonProgress(INVALID_DATE, VALID_PROGRESS);
    }

    @Override
    protected JsonAdaptedLessonItem<LessonProgress> getNullDateItem() {
        return new JsonAdaptedLessonProgress(null, VALID_PROGRESS);
    }

    @Override
    protected JsonAdaptedLessonItem<LessonProgress> getNullContentItem() {
        return new JsonAdaptedLessonProgress(VALID_DATE, null);
    }

    @Override
    protected String getTypeName() {
        return "LessonProgress";
    }

    @Override
    protected String getContentFieldName() {
        return "progress";
    }
}
