package tutortrack.model.person;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import tutortrack.commons.util.StringUtil;
import tutortrack.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given using prefix matching.
 */
public class NameContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public NameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsPrefixIgnoreCase(person.getName().fullName, keyword));
    }

    /**
     * Returns the match rank for a person.
     * Lower rank means higher priority in results.
     * Rank 1: First token matches
     * Rank 2: Other token matches
     * Rank 3: No match (defensively handle this case for safety)
     */
    private int getRanking(Person person) {
        String fullName = person.getName().fullName;
        String[] tokens = fullName.split("\\s+");

        if (tokens.length == 0) {
            return 3;
        }

        // Check if first token matches any keyword
        String firstToken = tokens[0];
        for (String keyword : keywords) {
            String firstTokenLowerCase = firstToken.toLowerCase();
            String keywordLowerCase = keyword.toLowerCase();

            if (firstTokenLowerCase.startsWith(keywordLowerCase)) {
                return 1;
            }
        }

        // Check if any other token matches
        for (int i = 1; i < tokens.length; i++) {
            for (String keyword : keywords) {
                if (tokens[i].toLowerCase().startsWith(keyword.toLowerCase())) {
                    return 2;
                }
            }
        }

        // No match found
        return 3;
    }

    /**
     * Returns a comparator that ranks persons by match quality.
     * Ranking from highest to lowest priority:
     * 1. First token prefix match
     * 2. Any other token prefix match
     * 3. Alphabetical by display name (tiebreaker)
     */
    public Comparator<Person> getComparator() {
        return (person1, person2) -> {
            int rank1 = getRanking(person1);
            int rank2 = getRanking(person2);

            if (rank1 != rank2) {
                // Lower rank means higher priority
                return Integer.compare(rank1, rank2);
            }

            // Tiebreaker done through alphabetical ordering by name
            return person1.getName().fullName.compareToIgnoreCase(person2.getName().fullName);
        };
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameContainsKeywordsPredicate)) {
            return false;
        }

        NameContainsKeywordsPredicate otherNameContainsKeywordsPredicate = (NameContainsKeywordsPredicate) other;
        return keywords.equals(otherNameContainsKeywordsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
