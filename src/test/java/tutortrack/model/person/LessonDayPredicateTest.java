package tutortrack.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import tutortrack.testutil.PersonBuilder;

/**
 * Tests for {@link LessonDayPredicate}, including equality semantics and
 * day/time filtering and ordering behavior.
 */
public class LessonDayPredicateTest {

    @Test
    public void equals_sameInstance_returnsTrue() {
        LessonDayPredicate mondayPredicate = new LessonDayPredicate("Monday");

        // Same instance -> returns true
        assertTrue(mondayPredicate.equals(mondayPredicate));
    }

    @Test
    public void equals_differentValue_returnsFalse() {
        LessonDayPredicate mondayPredicate = new LessonDayPredicate("Monday");
        LessonDayPredicate tuesdayPredicate = new LessonDayPredicate("Tuesday");

        // Different value -> returns false
        assertFalse(mondayPredicate.equals(tuesdayPredicate));
    }

    @Test
    public void equals_null_returnsFalse() {
        LessonDayPredicate mondayPredicate = new LessonDayPredicate("Monday");

        // Null -> returns false
        assertFalse(mondayPredicate.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        LessonDayPredicate mondayPredicate = new LessonDayPredicate("Monday");

        // Different type -> returns false
        assertFalse(mondayPredicate.equals(1));
        assertFalse(mondayPredicate.equals("Monday"));
    }

    // Filtering tests

    @Test
    public void test_sameDayDifferentTimes_allMatch() {
        // Ensures that all lessons on Monday are shown regardless of time
        LessonDayPredicate mondayPredicate = new LessonDayPredicate("Monday");

        // All these should match because they are on Monday
        Person monday0900 = new PersonBuilder().withDayTime("Monday 0900").build();
        Person monday1200 = new PersonBuilder().withDayTime("Monday 1200").build();
        Person monday1800 = new PersonBuilder().withDayTime("Monday 1800").build();

        assertTrue(mondayPredicate.test(monday0900));
        assertTrue(mondayPredicate.test(monday1200));
        assertTrue(mondayPredicate.test(monday1800));
    }

    @Test
    public void test_caseInsensitiveDay_matches() {
        // ensures that the predicate matches regardless of the case of the day keyword
        Person mondayPerson = new PersonBuilder().withDayTime("Monday 1200").build();

        assertTrue(new LessonDayPredicate("monday").test(mondayPerson));
        assertTrue(new LessonDayPredicate("MONDAY").test(mondayPerson));
        assertTrue(new LessonDayPredicate("Monday").test(mondayPerson));
    }

    @Test
    public void test_differentDay_doesNotMatch() {
        LessonDayPredicate mondayPredicate = new LessonDayPredicate("Monday");
        Person tuesdayPerson = new PersonBuilder().withDayTime("Tuesday 1200").build();

        // ensures that the predicate does not match if the day is different
        assertFalse(mondayPredicate.test(tuesdayPerson));
    }

    // Sorting tests

    @Test
    public void getComparator_sortsByTime_ascending() {
        // Lessons are sorted by time in ascending order
        LessonDayPredicate predicate = new LessonDayPredicate("Monday");

        Person monday1800 = new PersonBuilder().withName("Alice").withDayTime("Monday 1800").build();
        Person monday0900 = new PersonBuilder().withName("Bob").withDayTime("Monday 0900").build();
        Person monday1200 = new PersonBuilder().withName("Charlie").withDayTime("Monday 1200").build();

        // Earlier times come first: 0900 < 1200 < 1800
        assertTrue(predicate.getComparator().compare(monday0900, monday1200) < 0);
        assertTrue(predicate.getComparator().compare(monday1200, monday1800) < 0);
        assertTrue(predicate.getComparator().compare(monday0900, monday1800) < 0);

        // Reverse comparison
        assertTrue(predicate.getComparator().compare(monday1200, monday0900) > 0);
    }

    @Test
    public void getComparator_sameTime_sortsAlphabetically() {
        // When times are equal, sorts alphabetically by name
        LessonDayPredicate predicate = new LessonDayPredicate("Monday");

        Person alice = new PersonBuilder().withName("Alice").withDayTime("Monday 1200").build();
        Person charlie = new PersonBuilder().withName("Charlie").withDayTime("Monday 1200").build();

        // Same time: alphabetical order Alice < Charlie
        assertTrue(predicate.getComparator().compare(alice, charlie) < 0);

        // Reverse comparison
        assertTrue(predicate.getComparator().compare(charlie, alice) > 0);
    }

    @Test
    public void getComparator_samePerson_returnsZero() {
        // Comparing a person to itself returns zero
        LessonDayPredicate predicate = new LessonDayPredicate("Monday");

        Person alice = new PersonBuilder().withName("Alice").withDayTime("Monday 1200").build();

        // ensures that the comparator returns 0 if the person is the same
        assertEquals(0, predicate.getComparator().compare(alice, alice));
    }
}

