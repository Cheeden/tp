package tutortrack.testutil;

import java.time.LocalDate;

import tutortrack.model.lesson.LessonProgress;

/**
 * A utility class containing a list of {@code LessonProgress} objects to be used in tests.
 */
public class TypicalLessonProgresses {

    public static final LessonProgress LESSON_PROGRESS_1 = new LessonProgress(
            LocalDate.parse("2024-01-15"), "Completed Chapter 1");

    public static final LessonProgress LESSON_PROGRESS_2 = new LessonProgress(
            LocalDate.parse("2024-01-22"), "Completed Chapter 2");

    public static final LessonProgress LESSON_PROGRESS_3 = new LessonProgress(
            LocalDate.parse("2024-01-29"), "Reviewed Chapter 1-2");

    private TypicalLessonProgresses() {} // prevents instantiation
}
