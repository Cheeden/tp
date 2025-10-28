package tutortrack.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tutortrack.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static tutortrack.logic.commands.CommandTestUtil.VALID_CONTACT_BOB;
import static tutortrack.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static tutortrack.logic.commands.CommandTestUtil.VALID_NOK_CONTACT_BOB;
import static tutortrack.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static tutortrack.testutil.Assert.assertThrows;
import static tutortrack.testutil.TypicalPersons.ALICE;
import static tutortrack.testutil.TypicalPersons.BOB;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import tutortrack.model.lesson.LessonProgress;
import tutortrack.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same name, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(ALICE)
                                     .withSelfContact(VALID_CONTACT_BOB)
                                     .withNokContact(VALID_NOK_CONTACT_BOB)
                                     .withAddress(VALID_ADDRESS_BOB)
                                     .withTags(VALID_TAG_HUSBAND)
                                     .build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BOB.isSamePerson(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSamePerson(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different selfContact -> returns false
        editedAlice = new PersonBuilder(ALICE).withSelfContact(VALID_CONTACT_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different nokContact -> returns false
        editedAlice = new PersonBuilder(ALICE).withNokContact(VALID_NOK_CONTACT_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName()
                                  + "{name=" + ALICE.getName()
                                  + ", selfContact=" + ALICE.getSelfContact()
                                  + ", nokContact=" + ALICE.getNokContact()
                                  + ", subjectLevel=" + ALICE.getSubjectLevel()
                                  + ", dayTime=" + ALICE.getDayTime()
                                  + ", cost=" + ALICE.getCost()
                                  + ", address=" + ALICE.getAddress()
                                  + ", tags=" + ALICE.getTags()
                                  + ", lesson plan=" + ALICE.getLessonPlanList()
                                  + ", lesson progress=" + ALICE.getLessonProgressList() + "}";
        assertEquals(expected, ALICE.toString());
    }

    @Test
    public void withProgressRemovedOnDate_existingProgress_success() {
        // Create person with lesson progress
        Person person = new PersonBuilder(ALICE).build();
        LocalDate date = LocalDate.of(2024, 10, 15);
        LessonProgress progress = new LessonProgress(date, "Completed Chapter 1");
        person.addLessonProgress(progress);

        // Remove the progress
        Person updatedPerson = person.withProgressRemovedOnDate(date);

        // Progress removed from new person
        assertFalse(updatedPerson.hasProgressOnDate(date));
        // Original person unchanged (immutability)
        assertTrue(person.hasProgressOnDate(date));
        // Different instances
        assertNotSame(person, updatedPerson);
    }
}

