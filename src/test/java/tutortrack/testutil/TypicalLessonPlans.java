package tutortrack.testutil;

import java.time.LocalDate;

import tutortrack.model.lesson.LessonPlan;

/**
 * A utility class containing a list of {@code LessonPlan} objects to be used in tests.
 */
public class TypicalLessonPlans {
    public static final LessonPlan LESSON_PLAN_1 = new LessonPlan(
            LocalDate.parse("2025-10-20"), "Cover Chapter 1");

    public static final LessonPlan LESSON_PROGRESS_2 = new LessonPlan(
            LocalDate.parse("2025-10-21"), "Cover Chapter 2");

    public static final LessonPlan LESSON_PROGRESS_3 = new LessonPlan(
            LocalDate.parse("2024-10-22"), "Review Chapter 1-2");

    private TypicalLessonPlans() {}
}
