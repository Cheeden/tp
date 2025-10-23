package tutortrack.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

import tutortrack.commons.core.LogsCenter;
import tutortrack.commons.util.ToStringBuilder;
import tutortrack.logic.Messages;
import tutortrack.model.Model;
import tutortrack.model.person.Person;

/**
 * Finds and lists all persons in address book whose name, tags, or lesson day match the search criteria.
 * Keyword matching is case insensitive and uses prefix matching for names.
 */
public class FindCommand extends Command {

    private static final Logger logger = LogsCenter.getLogger(FindCommand.class);

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names start with any of "
            + "the specified keywords (case-insensitive prefix matching) or whose tags contain the specified keywords, "
            + "and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]... OR t/TAG_KEYWORD [MORE_TAG_KEYWORDS]... OR d/DAY\n"
            + "Examples:\n"
            + COMMAND_WORD + " alice bob charlie (searches by name prefix)\n"
            + COMMAND_WORD + " t/Exams (searches by tag)\n"
            + COMMAND_WORD + " d/Monday (searches by lesson day, sorted by time)";

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
        
        int resultCount = model.getFilteredPersonList().size();
        logger.info("Find command executed successfully. Found " + resultCount + " person(s)");
        
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, resultCount));
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
