package tutortrack.model.person;

import java.time.LocalDate;
import java.util.Objects;

public class LessonProgress {
    private final LocalDate date;
    private final String progress;

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