package tutortrack.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import tutortrack.commons.exceptions.IllegalValueException;
import tutortrack.model.lesson.LessonPlan;

/**
 * Jackson-friendly version of {@link LessonPlan}.
 */
public class JsonAdaptedLessonPlan extends JsonAdaptedLessonItem<LessonPlan> {

    /**
     * Constructs a {@code JsonAdaptedLessonPlan} with the given {@code date and plan}.
     */
    @JsonCreator
    public JsonAdaptedLessonPlan(@JsonProperty("date") String date, @JsonProperty("plan") String plan) {
        super(date, plan);
    }

    /**
     * Converts a given {@code LessonPlan} into this class for Jackson use.
     */
    public JsonAdaptedLessonPlan(LessonPlan source) {
        super(source.getDate().toString(), source.getPlan());
    }

    /**
     * Converts this Jackson-friendly adapted lesson plan object into the model's {@code LessonPlan} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted lesson plan.
     */
    public LessonPlan toModelType() throws IllegalValueException {
        validateFields("LessonPlan");
        return new LessonPlan(parseDate("LessonPlan"), description);
    }
}
