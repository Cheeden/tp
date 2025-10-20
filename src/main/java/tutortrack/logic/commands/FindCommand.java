package tutortrack.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

import tutortrack.commons.util.ToStringBuilder;
import tutortrack.logic.Messages;
import tutortrack.model.Model;
import tutortrack.model.person.Person;

/**
 * Finds and lists all persons in address book whose name or tags match any of the argument keywords.
 * Keyword matching is case insensitive and uses prefix matching for names.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names start with any of "
            + "the specified keywords (case-insensitive prefix matching) or whose tags contain the specified keywords, "
            + "and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]... OR t/TAG_KEYWORD [MORE_TAG_KEYWORDS]...\n"
            + "Examples:\n"
            + COMMAND_WORD + " alice bob charlie (searches by name prefix)\n"
            + COMMAND_WORD + " t/friend (searches by tag)";

    private final Predicate<Person> searchPredicate;
    private final Optional<Comparator<Person>> comparator;

    /**
     * Creates a FindCommand with the specified predicate (no sorting).
     */
    public FindCommand(Predicate<Person> searchPredicate) {
        this.searchPredicate = searchPredicate;
        this.comparator = Optional.empty();
    }

    /**
     * Creates a FindCommand with the specified predicate and comparator (with sorting).
     */
    public FindCommand(Predicate<Person> searchPredicate, Comparator<Person> comparator) {
        this.searchPredicate = searchPredicate;
        this.comparator = Optional.of(comparator);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (comparator.isPresent()) {
            model.updateFilteredPersonList(searchPredicate, comparator.get());
        } else {
            model.updateFilteredPersonList(searchPredicate);
        }
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        
        // Check if both have comparators or both don't
        return searchPredicate.equals(otherFindCommand.searchPredicate)
                && comparator.isPresent() == otherFindCommand.comparator.isPresent();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", searchPredicate)
                .add("comparator", comparator)
                .toString();
    }
}
