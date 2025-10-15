package tutortrack.storage;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;

import tutortrack.commons.exceptions.IllegalValueException;
import tutortrack.model.lesson.LessonProgress;

/**
 * Jackson-friendly version of {@link LessonProgress}.
 */
public class JsonAdaptedLessonProgress {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "LessonProgress's %s field is missing!";

    private final String date;
    private final String progress;

    /**
     * Constructs a {@code JsonAdaptedLessonProgress} with the given {@code date and progress}.
     */
    @JsonCreator
    public JsonAdaptedLessonProgress(String date, String progress) {
        this.date = date;
        this.progress = progress;
    }

    /**
     * Converts a given {@code LessonProgress} into this class for Jackson use.
     */
    public JsonAdaptedLessonProgress(LessonProgress source) {
        date = source.getDate().toString();
        progress = source.getProgress();
    }

    /**
     * Converts this Jackson-friendly adapted lesson progress object into the model's {@code LessonProgress} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted lesson progress.
     */
    public LessonProgress toModelType() throws IllegalValueException {
        if (date == null || progress == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "date or progress"));
        }
        LocalDate parsedDate = LocalDate.parse(date);
        return new LessonProgress(parsedDate, progress);
    }
}
