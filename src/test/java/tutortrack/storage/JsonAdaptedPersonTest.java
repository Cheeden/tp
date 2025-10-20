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
import tutortrack.model.person.Cost;
import tutortrack.model.person.DayTime;
import tutortrack.model.person.Name;
import tutortrack.model.person.Phone;
import tutortrack.model.person.SubjectLevel;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_SUBJECTLEVEL = "P4Math";
    private static final String INVALID_DAYTIME = "Monday 2500";
    private static final String INVALID_COST = "50";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_SUBJECTLEVEL = BENSON.getSubjectLevel().toString();
    private static final String VALID_DAYTIME = BENSON.getDayTime().toString();
    private static final String VALID_COST = BENSON.getCost().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());
    private static final List<JsonAdaptedLessonProgress> VALID_LESSONPROGRESSES = BENSON
            .getLessonProgressList().stream()
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
                        VALID_SUBJECTLEVEL, VALID_DAYTIME, VALID_COST, VALID_ADDRESS, VALID_TAGS,
                        VALID_LESSONPROGRESSES);
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
    public void toModelType_invalidSubjectLevel_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE,
                        INVALID_SUBJECTLEVEL, VALID_DAYTIME, VALID_COST, VALID_ADDRESS, VALID_TAGS,
                        VALID_LESSONPROGRESSES);
        String expectedMessage = SubjectLevel.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullSubjectLevel_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE,
                        null, VALID_DAYTIME, VALID_COST, VALID_ADDRESS, VALID_TAGS, VALID_LESSONPROGRESSES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, SubjectLevel.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidDayTime_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE,
                        VALID_SUBJECTLEVEL, INVALID_DAYTIME, VALID_COST, VALID_ADDRESS, VALID_TAGS,
                        VALID_LESSONPROGRESSES);
        String expectedMessage = DayTime.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullDayTime_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE,
                        VALID_SUBJECTLEVEL, null, VALID_COST, VALID_ADDRESS, VALID_TAGS, VALID_LESSONPROGRESSES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, DayTime.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidCost_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE,
                        VALID_SUBJECTLEVEL, VALID_DAYTIME, INVALID_COST, VALID_ADDRESS, VALID_TAGS,
                        VALID_LESSONPROGRESSES);
        String expectedMessage = Cost.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullCost_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE,
                        VALID_SUBJECTLEVEL, VALID_DAYTIME, null, VALID_ADDRESS, VALID_TAGS, VALID_LESSONPROGRESSES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Cost.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE,
                        VALID_SUBJECTLEVEL, VALID_DAYTIME, VALID_COST, INVALID_ADDRESS, VALID_TAGS,
                        VALID_LESSONPROGRESSES);
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
                        VALID_SUBJECTLEVEL, VALID_DAYTIME, VALID_COST, VALID_ADDRESS, invalidTags,
                        VALID_LESSONPROGRESSES);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

}
