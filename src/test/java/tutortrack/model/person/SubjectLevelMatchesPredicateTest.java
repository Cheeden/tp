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
}
