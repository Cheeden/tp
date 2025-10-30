package tutortrack.storage;

import java.time.LocalDate;

import tutortrack.commons.exceptions.IllegalValueException;

/**
 * Abstract base class for Jackson-friendly lesson items.
 * Provides common date parsing and null-checking logic.
 */
public abstract class JsonAdaptedLessonItem<T> {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "%s's %s field is missing!";

    protected final String date;
    protected final String description;

    protected JsonAdaptedLessonItem(String date, String description) {
        this.date = date;
        this.description = description;
    }

    /**
     * Converts this adapted lesson item into the model type.
     */
    public abstract T toModelType() throws IllegalValueException;

    /**
     * Validates that both date and description fields exist.
     */
    protected void validateFields(String typeName) throws IllegalValueException {
        if (date == null || description == null) {
            throw new IllegalValueException(String.format(
                    MISSING_FIELD_MESSAGE_FORMAT, typeName, "date or description"));
        }
    }

    protected LocalDate parseDate(String typeName) throws IllegalValueException {
        try {
            return LocalDate.parse(date);
        } catch (Exception e) {
            throw new IllegalValueException(String.format("%s has invalid date format: %s", typeName, date));
        }
    }
}
