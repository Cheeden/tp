package tutortrack.model.lesson;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a lesson plan entry in TutorTrack.
 * Guarantees: immutable; date and plan are valid as declared in their respective classes
 */
public class LessonPlan {
    public static final String MESSAGE_CONSTRAINTS = "Lesson Plan should take two fields, date and description";

    private final LocalDate date;
    private final String plan;

    /**
     * Constructs a {@code LessonPlan}.
     *
     * @param date A valid date.
     * @param plan A valid plan description.
     */
    public LessonPlan(LocalDate date, String plan) {
        this.date = requireNonNull(date, "Date cannot be null");
        this.plan = requireNonNull(plan, "Plan cannot be null");
    }

    public LocalDate getDate() {
        return date;
    }

    public String getPlan() {
        return plan;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof LessonPlan)) {
            return false;
        }

        LessonPlan otherPlan = (LessonPlan) other;
        return date.equals(otherPlan.date) && plan.equals(otherPlan.plan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, plan);
    }

    @Override
    public String toString() {
        return String.format("Date: %s, Plan: %s", date.toString(), plan);
    }
}
