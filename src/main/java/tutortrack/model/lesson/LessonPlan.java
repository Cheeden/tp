package tutortrack.model.lesson;

import java.time.LocalDate;

/**
 * Represents a lesson plan entry in TutorTrack.
 * Guarantees: immutable; date and plan are valid as declared in their respective classes
 */
public class LessonPlan extends LessonItem {
    public static final String MESSAGE_CONSTRAINTS =
            "Lesson plan should be in the format DATE|DESCRIPTION (e.g. 2025-10-12|Covered Chapter 5).";

    /**
     * Constructs a {@code LessonPlan}.
     *
     * @param date A valid date.
     * @param plan A valid plan description.
     */
    public LessonPlan(LocalDate date, String plan) {
        super(date, plan);
    }

    public String getPlan() {
        return getDescription();
    }

    @Override
    protected String getTypeName() {
        return "Plan";
    }
}
