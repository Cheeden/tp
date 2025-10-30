package tutortrack.model.lesson;

import java.time.LocalDate;

/**
 * Represents a lesson progress entry in TutorTrack.
 * Guarantees: immutable; date and progress are valid as declared in their respective classes
 */
public class LessonProgress extends LessonItem {

    public static final String MESSAGE_CONSTRAINTS =
            "Lesson progress should be in the format DATE|DESCRIPTION (e.g. 2025-10-12|Covered Chapter 5).";

    /**
     * Constructs a {@code LessonProgress}.
     *
     * @param date A valid date.
     * @param progress A valid progress description.
     */
    public LessonProgress(LocalDate date, String progress) {
        super(date, progress);
    }

    public String getProgress() {
        return getDescription();
    }

    @Override
    protected String getTypeName() {
        return "Progress";
    }
}
