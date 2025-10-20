package tutortrack.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import tutortrack.commons.exceptions.IllegalValueException;
import tutortrack.model.lesson.LessonPlan;

import java.time.LocalDate;

public class JsonAdaptedLessonPlan {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "LessonPlan's %s field is missing!";

    private final String date;
    private final String plan;

    /**
     * Constructs a {@code JsonAdaptedLessonPlan} with the given {@code date and plan}.
     */
    @JsonCreator
    public JsonAdaptedLessonPlan(@JsonProperty("date") String date, @JsonProperty("plan") String plan) {
        this.date = date;
        this.plan = plan;
    }

    /**
     * Converts a given {@code LessonPlan} into this class for Jackson use.
     */
    public JsonAdaptedLessonPlan(LessonPlan source) {
        date = source.getDate().toString();
        plan = source.getPlan();
    }

    /**
     * Converts this Jackson-friendly adapted lesson plan object into the model's {@code LessonPlan} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted lesson plan.
     */
    public LessonPlan toModelType() throws IllegalValueException {
        if (date == null || plan == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "date or plan"));
        }
        LocalDate parsedDate = LocalDate.parse(date);
        return new LessonPlan(parsedDate, plan);
    }
}
