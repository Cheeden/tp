package tutortrack.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tutortrack.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import tutortrack.commons.exceptions.IllegalValueException;

public abstract class JsonAdaptedLessonTest<T> {
    /** Subclass must provide a valid instance of the adapted item. */
    protected abstract JsonAdaptedLessonItem<T> getValidItem();

    /** Subclass must provide the expected model object for the valid item. */
    protected abstract T getExpectedModel();

    /** Subclass must provide an adapted item with invalid date. */
    protected abstract JsonAdaptedLessonItem<T> getInvalidDateItem();

    /** Subclass must provide an adapted item with null date. */
    protected abstract JsonAdaptedLessonItem<T> getNullDateItem();

    /** Subclass must provide an adapted item with null content (plan/progress). */
    protected abstract JsonAdaptedLessonItem<T> getNullContentItem();

    /** Subclass must provide the type name (e.g., "LessonPlan"). */
    protected abstract String getTypeName();

    /** Subclass must provide the content field name (e.g., "plan", "progress"). */
    protected abstract String getContentFieldName();

    @Test
    public void toModelType_validItem_returnsModel() throws Exception {
        JsonAdaptedLessonItem<T> item = getValidItem();
        T model = item.toModelType();
        assertEquals(model, model);
    }

    @Test
    public void toModelType_invalidDate_throwsIllegalValueException() {
        JsonAdaptedLessonItem<T> item = getInvalidDateItem();
        assertThrows(IllegalValueException.class, item::toModelType);
    }

    @Test
    public void toModelType_nullDate_throwsIllegalValueException() {
        JsonAdaptedLessonItem<T> item = getNullDateItem();
        String expectedMessage = String.format(
                JsonAdaptedLessonItem.MISSING_FIELD_MESSAGE_FORMAT,
                getTypeName(),
                "date or " + getContentFieldName()
        );
        assertThrows(IllegalValueException.class, item::toModelType);
    }

    @Test
    public void toModelType_nullContent_throwsIllegalValueException() {
        JsonAdaptedLessonItem<T> item = getNullContentItem();
        String expectedMessage = String.format(
                JsonAdaptedLessonItem.MISSING_FIELD_MESSAGE_FORMAT,
                getTypeName(),
                "date or " + getContentFieldName()
        );
        assertThrows(IllegalValueException.class, item::toModelType);
    }
}
