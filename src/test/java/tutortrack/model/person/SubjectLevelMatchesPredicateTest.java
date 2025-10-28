package tutortrack.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import tutortrack.testutil.PersonBuilder;

/**
 * Unit tests for {@link SubjectLevelMatchesPredicate}.
 */
public class SubjectLevelMatchesPredicateTest {

    @Test
    public void test_matchesCaseInsensitive_exactMatch() {
        SubjectLevelMatchesPredicate predicate = new SubjectLevelMatchesPredicate("p4-math");
        SubjectLevelMatchesPredicate predicate2 = new SubjectLevelMatchesPredicate("P4-Math");
        assertTrue(predicate.equals(predicate2));

        // Person with P4-Math
        assertTrue(predicate.test(new PersonBuilder().withSubjectLevel("P4-Math").build()));
        // Different case
        assertTrue(predicate.test(new PersonBuilder().withSubjectLevel("p4-math").build()));
    }

    @Test
    public void test_notMatches_differentSubject() {
        SubjectLevelMatchesPredicate predicate = new SubjectLevelMatchesPredicate("P4-Math");
        // Person with P5-Math should not match
        assertFalse(predicate.test(new PersonBuilder().withSubjectLevel("P5-Math").build()));
    }

    @Test
    public void equalsAndToStringBehaviour() {
        SubjectLevelMatchesPredicate p1 = new SubjectLevelMatchesPredicate("P4-Math");
        SubjectLevelMatchesPredicate p2 = new SubjectLevelMatchesPredicate("p4-math");
        SubjectLevelMatchesPredicate p3 = new SubjectLevelMatchesPredicate("P5-Math");

        // same object
        assertTrue(p1.equals(p1));

        // equals is case-insensitive
        assertTrue(p1.equals(p2));

        // different subject
        assertFalse(p1.equals(p3));

        // different type
        assertFalse(p1.equals("some string"));

        // toString returns a non-empty representation
        String s = p1.toString();
        assertTrue(s != null && !s.isEmpty());
    }

    @Test
    public void test_personWithNullSubject_returnsFalse() throws Exception {
        SubjectLevelMatchesPredicate predicate = new SubjectLevelMatchesPredicate("P4-Math");

        // Build a regular person then null out its subjectLevel via reflection to simulate missing subject
        tutortrack.model.person.Person person = new PersonBuilder().withSubjectLevel("P4-Math").build();

        java.lang.reflect.Field field = person.getClass().getDeclaredField("subjectLevel");
        field.setAccessible(true);
        field.set(person, null);

        assertFalse(predicate.test(person));
    }
}
