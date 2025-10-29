package tutortrack.model.lesson;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a lesson item entry in TutorTrack.
 */
public abstract class LessonItem {
    private final LocalDate date;
    private final String description;

    /**
     * Constructs a {@code LessonItem}.
     */
    protected LessonItem(LocalDate date, String description) {
        this.date = requireNonNull(date, "Date cannot be null");
        this.description = requireNonNull(description, "Description cannot be null");
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LessonItem)) {
            return false;
        }
        LessonItem otherItem = (LessonItem) other;
        return date.equals(otherItem.date)
                && description.equals(otherItem.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, description);
    }

    @Override
    public String toString() {
        return String.format("Date: %s, %s: %s",
                date, getTypeName(), description);
    }

    /**
     * Returns the label for this lesson item type ("Plan" or "Progress").
     */
    protected abstract String getTypeName();
}
