package tutortrack.ui;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a lesson entry for display in the UI table.
 * Combines lesson progress and lesson plan for the same date.
 */
public class LessonDisplay {
    private final LocalDate date;
    private final SimpleStringProperty progress;
    private final SimpleStringProperty plan;

    /**
     * Constructor for lesson with both progress and plan.
     */
    public LessonDisplay(LocalDate date, String progress, String plan) {
        this.date = date;
        this.progress = new SimpleStringProperty(progress != null ? progress : "");
        this.plan = new SimpleStringProperty(plan != null ? plan : "");
    }

    /**
     * Constructor for lesson with only progress.
     */
    public LessonDisplay(LocalDate date, String progress) {
        this(date, progress, "");
    }

    public LocalDate getDate() {
        return date;
    }

    public String getProgress() {
        return progress.get();
    }

    public void setProgress(String progress) { this.progress.set(progress); }

    public SimpleStringProperty progressProperty() {
        return progress;
    }

    public String getPlan() {
        return plan.get();
    }

    public void setPlan(String plan) {this.plan.set(plan); }

    public SimpleStringProperty planProperty() {
        return plan;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof LessonDisplay)) {
            return false;
        }

        LessonDisplay otherDisplay = (LessonDisplay) other;
        return date.equals(otherDisplay.date)
                && progress.equals(otherDisplay.progress)
                && plan.equals(otherDisplay.plan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, progress, plan);
    }

    @Override
    public String toString() {
        return String.format("Date: %s, Progress: %s, Plan: %s", date, progress, plan);
    }
}
