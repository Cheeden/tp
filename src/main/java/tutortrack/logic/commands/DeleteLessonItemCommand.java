package tutortrack.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import tutortrack.commons.core.LogsCenter;
import tutortrack.commons.core.index.Index;
import tutortrack.commons.util.ToStringBuilder;
import tutortrack.logic.Messages;
import tutortrack.logic.commands.exceptions.CommandException;
import tutortrack.model.Model;
import tutortrack.model.person.Person;

/**
 * Abstract base class for deleting lesson items (plans or progress) by date.
 * Extract common deletion logic to adhere to DRY principle.
 * Subclasses only need to specify item-specific behavior via template methods.
 */
public abstract class DeleteLessonItemCommand extends Command {

    private static final Logger logger = LogsCenter.getLogger(DeleteLessonItemCommand.class);

    protected final Index index;
    protected final LocalDate date;

    /**
     * Creates a DeleteLessonItemCommand.
     *
     * @param index The index of the person in the displayed list.
     * @param date The date of the lesson item to delete.
     */
    public DeleteLessonItemCommand(Index index, LocalDate date) {
        requireNonNull(index);
        requireNonNull(date);
        this.index = index;
        this.date = date;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        assert lastShownList != null : "Filtered person list should never be null";

        // Validate index
        if (index.getZeroBased() >= lastShownList.size()) {
            logger.warning(String.format("Invalid index %d for list of size %d",
                    index.getOneBased(), lastShownList.size()));
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        // Let Person handle the deletion and validation to adhere to Tell, Don't Ask principle.
        try {
            Person updatedPerson = createPersonWithItemRemoved(personToEdit, date);
            assert updatedPerson != null : "Updated person should not be null";

            model.setPerson(personToEdit, updatedPerson);

            logger.info(String.format("Successfully deleted lesson item on %s for person: %s",
                    date, personToEdit.getName()));

            return new CommandResult(getSuccessMessage());
        } catch (IllegalArgumentException e) {
            logger.warning(String.format("No lesson item found on %s for person: %s",
                    date, personToEdit.getName()));
            throw new CommandException(getNoItemMessage());
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("date", date)
                .toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // Ensure that the other object is an instance of DeleteLessonItemCommand and that it is the same class
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        DeleteLessonItemCommand that = (DeleteLessonItemCommand) other;
        return index.equals(that.index) && date.equals(that.date);
    }

    // Creates a new person with the specific item removed.
    // Delegates to Person's removeLessonPlanByDate or removeLessonProgressByDate methods (Tell, Don't Ask).
    protected abstract Person createPersonWithItemRemoved(Person person, LocalDate date);

    /**
     * Returns the success message for this specific deletion.
     *
     * @return The success message format string.
     */
    protected abstract String getSuccessMessage();

    /**
     * Returns the error message when item not found.
     *
     * @return The error message format string.
     */
    protected abstract String getNoItemMessage();
}
