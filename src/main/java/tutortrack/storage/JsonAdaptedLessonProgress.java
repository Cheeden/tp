package tutortrack.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import tutortrack.commons.exceptions.IllegalValueException;
import tutortrack.model.lesson.LessonProgress;

/**
 * Jackson-friendly version of {@link LessonProgress}.
 */
public class JsonAdaptedLessonProgress extends JsonAdaptedLessonItem<LessonProgress> {

    /**
     * Constructs a {@code JsonAdaptedLessonProgress} with the given {@code date and progress}.
     */
    @JsonCreator
    public JsonAdaptedLessonProgress(@JsonProperty("date") String date, @JsonProperty("progress") String progress) {
        super(date, progress);
    }

    /**
     * Converts a given {@code LessonProgress} into this class for Jackson use.
     */
    public JsonAdaptedLessonProgress(LessonProgress source) {
        super(source.getDate().toString(), source.getProgress());
    }

    /**
     * Converts this Jackson-friendly adapted lesson progress object into the model's {@code LessonProgress} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted lesson progress.
     */
    public LessonProgress toModelType() throws IllegalValueException {
        validateFields("LessonProgress");
        return new LessonProgress(parseDate("LessonProgress"), description);
    }
}
