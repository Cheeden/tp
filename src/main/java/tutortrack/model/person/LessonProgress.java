package tutortrack.model.person;

import java.time.LocalDate;

/**
 * Represents the lesson progress of a person in the address book.
 */
public class LessonProgress {
    private final LocalDate date;
    private final String progress;

    /**
     * Creates a new LessonProgress with the given date and progress description.
     *
     * @param date The date of the lesson
     * @param progress The progress notes
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
}
