package tutortrack.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import tutortrack.testutil.PersonBuilder;

public class NameContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        NameContainsKeywordsPredicate firstPredicate = new NameContainsKeywordsPredicate(firstPredicateKeywordList);
        NameContainsKeywordsPredicate secondPredicate = new NameContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        NameContainsKeywordsPredicate firstPredicateCopy = new NameContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nameContainsKeywords_returnsTrue() {
        // One keyword - full match
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Collections.singletonList("Alice"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // One keyword - prefix match
        predicate = new NameContainsKeywordsPredicate(Collections.singletonList("Ali"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Multiple keywords
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Multiple keywords - prefix match
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("Al", "Bo"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Only one matching keyword
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("Bob", "Carol"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Carol").build()));

        // Mixed-case keywords
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("aLIce", "bOB"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Mixed-case prefix match
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("aLI", "bO"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Prefix matches second name token
        predicate = new NameContainsKeywordsPredicate(Collections.singletonList("Bo"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Non-matching keyword
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("Carol"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Keyword is substring but not a prefix of any token
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("lice"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Keyword is substring but not a prefix of any token
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("ice"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Keywords match phone and address, but does not match name
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("12345", "Main", "Street"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withPhone("12345")
                .withAddress("Main Street").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(keywords);

        String expected = NameContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }

    @Test
    public void comparator_firstNameMatch_rankedHigher() {
        // Test that first name matches are ranked higher than last name matches
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("Jo"));

        Person johnDoe = new PersonBuilder().withName("John Doe").build();
        Person maryJoe = new PersonBuilder().withName("Mary Joe").build();

        // John should come before Mary (first name "John" vs last name "Joe")
        assertTrue(predicate.getComparator().compare(johnDoe, maryJoe) < 0);
    }

    @Test
    public void comparator_sameRank_alphabeticalOrder() {
        // Test that same-rank results are sorted alphabetically
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("J"));

        Person alice = new PersonBuilder().withName("Alice Johnson").build();
        Person bob = new PersonBuilder().withName("Bob Jackson").build();

        // Both match on last name (rank 2), so alphabetical: Alice < Bob
        assertTrue(predicate.getComparator().compare(alice, bob) < 0);
    }

    @Test
    public void comparator_firstNameVsLastName_correctRanking() {
        // Test complete ranking: first name > last name > alphabetical
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("Jo"));

        Person johnSmith = new PersonBuilder().withName("John Smith").build();
        Person josephTan = new PersonBuilder().withName("Joseph Tan").build();
        Person aliceJones = new PersonBuilder().withName("Alice Jones").build();

        // John and Joseph both match first name (rank 1), alphabetically John < Joseph
        assertTrue(predicate.getComparator().compare(johnSmith, josephTan) < 0);

        // John (rank 1) should be before Alice (rank 2)
        assertTrue(predicate.getComparator().compare(johnSmith, aliceJones) < 0);

        // Joseph (rank 1) should be before Alice (rank 2)
        assertTrue(predicate.getComparator().compare(josephTan, aliceJones) < 0);
    }

    @Test
    public void comparator_noMatch_lowestRank() {
        // Test that non-matching persons get lowest rank
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("xyz"));

        Person alice = new PersonBuilder().withName("Alice Bob").build();
        Person charlie = new PersonBuilder().withName("Charlie David").build();

        // Both don't match, so alphabetical order
        assertTrue(predicate.getComparator().compare(alice, charlie) < 0);
    }

    @Test
    public void comparator_emptyName_handledGracefully() {
        // Test edge case with empty name tokens
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("test"));

        Person normalPerson = new PersonBuilder().withName("Test Person").build();
        Person anotherPerson = new PersonBuilder().withName("Another Person").build();

        // Should not throw exception
        int result = predicate.getComparator().compare(normalPerson, anotherPerson);
        assertTrue(result < 0); // Test < Another
    }

    @Test
    public void comparator_multipleKeywords_firstMatchWins() {
        // Test that first matching keyword determines rank
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("Jo", "Al"));

        Person john = new PersonBuilder().withName("John Smith").build();
        Person alice = new PersonBuilder().withName("Alice Bob").build();

        // Both match first name with different keywords, alphabetical tiebreaker
        assertTrue(predicate.getComparator().compare(alice, john) < 0);
    }

    @Test
    public void comparator_caseInsensitive_worksCorrectly() {
        // Test case insensitive matching in ranking
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("jo"));

        Person johnDoe = new PersonBuilder().withName("JOHN DOE").build();
        Person maryJoe = new PersonBuilder().withName("MARY JOE").build();

        // Should work despite case differences
        assertTrue(predicate.getComparator().compare(johnDoe, maryJoe) < 0);
    }
}
