package tutortrack.storage;

import com.fasterxml.jackson.annotation.JsonCreator;

import tutortrack.commons.exceptions.IllegalValueException;
import tutortrack.model.lesson.LessonProgress;

import java.time.LocalDate;

/**
 * Jackson-friendly version of {@link LessonProgress}.
 */
public class JsonAdaptedLessonProgress {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "LessonProgress's %s field is missing!";

    private final String date;
    private final String progress;

    @JsonCreator
    public JsonAdaptedLessonProgress(String date, String progress) {
        this.date = date;
        this.progress = progress;
    }

    public JsonAdaptedLessonProgress(LessonProgress source) {
        date = source.getDate().toString();
        progress = source.getProgress();
    }

    public LessonProgress toModelType() throws IllegalValueException {
        if (date == null || progress == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "date or progress"));
        }
        LocalDate parsedDate = LocalDate.parse(date);
        return new LessonProgress(parsedDate, progress);
    }
}
