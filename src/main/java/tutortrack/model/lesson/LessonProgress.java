package tutortrack.model.lesson;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a lesson progress entry in TutorTrack.
 * Guarantees: immutable; date and progress are valid as declared in their respective classes
 */
public class LessonProgress {
    private final LocalDate date;
    private final String progress;

    /**
     * Constructs a {@code LessonProgress}.
     *
     * @param date A valid date.
     * @param progress A valid progress description.
     */
    public LessonProgress(LocalDate date, String progress) {
        this.date = date;
        this.progress = progress;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getProgress() {
        return progress;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof LessonProgress)) {
            return false;
        }

        LessonProgress otherProgress = (LessonProgress) other;
        return date.equals(otherProgress.date) && progress.equals(otherProgress.progress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, progress);
    }

    @Override
    public String toString() {
        return String.format("Date: %s, Progress: %s", date.toString(), progress);
    }
}
