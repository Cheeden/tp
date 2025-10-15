package tutortrack.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tutortrack.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static tutortrack.testutil.Assert.assertThrows;
import static tutortrack.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import tutortrack.commons.exceptions.IllegalValueException;
import tutortrack.model.person.Address;
import tutortrack.model.person.Name;
import tutortrack.model.person.Phone;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_SUBJECTLEVEL = BENSON.getSubjectLevel();
    private static final String VALID_DAYTIME = BENSON.getDayTime();
    private static final String VALID_COST = BENSON.getCost();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());
    private static final List<JsonAdaptedLessonProgress> VALID_LESSONPROGRESSES = BENSON.getLessonProgressList().stream()
            .map(JsonAdaptedLessonProgress::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(INVALID_NAME, VALID_PHONE, VALID_SUBJECTLEVEL,
                        VALID_DAYTIME, VALID_COST, VALID_ADDRESS, VALID_TAGS, VALID_LESSONPROGRESSES);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(null, VALID_PHONE,
                VALID_SUBJECTLEVEL, VALID_DAYTIME, VALID_COST, VALID_ADDRESS, VALID_TAGS, VALID_LESSONPROGRESSES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, INVALID_PHONE,
                        VALID_SUBJECTLEVEL, VALID_DAYTIME, VALID_COST, VALID_ADDRESS, VALID_TAGS, VALID_LESSONPROGRESSES);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, null,
                VALID_SUBJECTLEVEL, VALID_DAYTIME, VALID_COST, VALID_ADDRESS, VALID_TAGS, VALID_LESSONPROGRESSES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE,
                        VALID_SUBJECTLEVEL, VALID_DAYTIME, VALID_COST, INVALID_ADDRESS, VALID_TAGS, VALID_LESSONPROGRESSES);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE,
                VALID_SUBJECTLEVEL, VALID_DAYTIME, VALID_COST, null, VALID_TAGS, VALID_LESSONPROGRESSES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE,
                        VALID_SUBJECTLEVEL, VALID_DAYTIME, VALID_COST, VALID_ADDRESS, invalidTags, VALID_LESSONPROGRESSES);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

}
