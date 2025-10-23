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
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob")
                                          .withSelfContact("12345678").withNokContact("87654321").build()));

        // One keyword - prefix match
        predicate = new NameContainsKeywordsPredicate(Collections.singletonList("Ali"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob")
                                          .withSelfContact("12345678").withNokContact("87654321").build()));

        // Multiple keywords
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob")
                                          .withSelfContact("12345678").withNokContact("87654321").build()));

        // Multiple keywords - prefix match
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("Al", "Bo"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob")
                                          .withSelfContact("12345678").withNokContact("87654321").build()));

        // Only one matching keyword
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("Bob", "Carol"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Carol")
                                          .withSelfContact("12345678").withNokContact("87654321").build()));

        // Mixed-case keywords
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("aLIce", "bOB"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").withSelfContact("12345678")
                                          .withNokContact("87654321").build()));

        // Mixed-case prefix match
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("aLI", "bO"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").withSelfContact("12345678")
                                          .withNokContact("87654321").build()));

        // Prefix matches second name token
        predicate = new NameContainsKeywordsPredicate(Collections.singletonList("Bo"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob")
                                          .withSelfContact("12345678")
                                          .withNokContact("87654321").build()));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice")
                                           .withSelfContact("12345678")
                                           .withNokContact("87654321").build()));

        // Non-matching keyword
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("Carol"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob")
                                           .withSelfContact("12345678")
                                           .withNokContact("87654321").build()));

        // Keyword is substring but not a prefix of any token
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("lice"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob")
                                           .withSelfContact("12345678")
                                           .withNokContact("87654321").build()));

        // Keywords match contact and address, but not name
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("12345678", "Main", "Street"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice")
                                           .withSelfContact("12345678").withNokContact("87654321")
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
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("Jo"));

        Person johnDoe = new PersonBuilder().withName("John Doe")
                                 .withSelfContact("12345678").withNokContact("87654321").build();
        Person maryJoe = new PersonBuilder().withName("Mary Joe")
                                 .withSelfContact("12345678").withNokContact("87654321").build();

        assertTrue(predicate.getComparator().compare(johnDoe, maryJoe) < 0);
    }

    @Test
    public void comparator_sameRank_alphabeticalOrder() {
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("J"));

        Person alice = new PersonBuilder().withName("Alice Johnson")
                               .withSelfContact("12345678").withNokContact("87654321").build();
        Person bob = new PersonBuilder().withName("Bob Jackson")
                             .withSelfContact("12345678").withNokContact("87654321").build();

        assertTrue(predicate.getComparator().compare(alice, bob) < 0);
    }

    @Test
    public void comparator_firstNameVsLastName_correctRanking() {
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("Jo"));

        Person johnSmith = new PersonBuilder().withName("John Smith")
                                   .withSelfContact("12345678").withNokContact("87654321").build();
        Person josephTan = new PersonBuilder().withName("Joseph Tan")
                                   .withSelfContact("12345678").withNokContact("87654321").build();
        Person aliceJones = new PersonBuilder().withName("Alice Jones")
                                    .withSelfContact("12345678").withNokContact("87654321").build();

        assertTrue(predicate.getComparator().compare(johnSmith, josephTan) < 0);
        assertTrue(predicate.getComparator().compare(johnSmith, aliceJones) < 0);
        assertTrue(predicate.getComparator().compare(josephTan, aliceJones) < 0);
    }

    @Test
    public void comparator_noMatch_lowestRank() {
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("xyz"));

        Person alice = new PersonBuilder().withName("Alice Bob")
                               .withSelfContact("12345678").withNokContact("87654321").build();
        Person charlie = new PersonBuilder().withName("Charlie David")
                                 .withSelfContact("12345678").withNokContact("87654321").build();

        assertTrue(predicate.getComparator().compare(alice, charlie) < 0);
    }

    @Test
    public void comparator_emptyName_handledGracefully() {
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("test"));

        Person normalPerson = new PersonBuilder().withName("Test Person")
                                      .withSelfContact("12345678").withNokContact("87654321").build();
        Person anotherPerson = new PersonBuilder().withName("Another Person")
                                       .withSelfContact("12345678").withNokContact("87654321").build();

        int result = predicate.getComparator().compare(normalPerson, anotherPerson);
        assertTrue(result < 0);
    }

    @Test
    public void comparator_multipleKeywords_firstMatchWins() {
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("Jo", "Al"));

        Person john = new PersonBuilder().withName("John Smith")
                              .withSelfContact("12345678").withNokContact("87654321").build();
        Person alice = new PersonBuilder().withName("Alice Bob")
                               .withSelfContact("12345678").withNokContact("87654321").build();

        assertTrue(predicate.getComparator().compare(alice, john) < 0);
    }

    @Test
    public void comparator_caseInsensitive_worksCorrectly() {
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("jo"));

        Person johnDoe = new PersonBuilder().withName("JOHN DOE")
                                 .withSelfContact("12345678").withNokContact("87654321").build();
        Person maryJoe = new PersonBuilder().withName("MARY JOE")
                                 .withSelfContact("12345678").withNokContact("87654321").build();

        assertTrue(predicate.getComparator().compare(johnDoe, maryJoe) < 0);
    }
}
