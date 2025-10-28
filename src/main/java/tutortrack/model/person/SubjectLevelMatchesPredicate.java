package tutortrack.model.person;

import java.util.function.Predicate;

import tutortrack.commons.util.StringUtil;
import tutortrack.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code SubjectLevel} matches the given subject string (case-insensitive).
 */
public class SubjectLevelMatchesPredicate implements Predicate<Person> {
    private final String subject;

    /**
     * Constructor for SujectLevelMatchesPredicate
     * @param subject
     */
    public SubjectLevelMatchesPredicate(String subject) {
        assert subject != null : "Subject cannot be null";
        this.subject = subject;
    }

    @Override
    public boolean test(Person person) {
        if (person.getSubjectLevel() == null) {
            return false;
        }
        return StringUtil.containsWordIgnoreCase(person.getSubjectLevel().value, subject)
                || person.getSubjectLevel().value.equalsIgnoreCase(subject);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof SubjectLevelMatchesPredicate)) {
            return false;
        }

        SubjectLevelMatchesPredicate otherPred = (SubjectLevelMatchesPredicate) other;
        return subject.equalsIgnoreCase(otherPred.subject);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("subject", subject).toString();
    }
}
