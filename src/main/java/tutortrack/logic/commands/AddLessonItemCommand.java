package tutortrack.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import tutortrack.commons.core.index.Index;
import tutortrack.logic.Messages;
import tutortrack.logic.commands.exceptions.CommandException;
import tutortrack.model.Model;
import tutortrack.model.person.Person;

/**
 * Abstract base class for adding lesson items (plans or progress).
 * Extract common addition logic to adhere to DRY principle.
 * Subclasses only need to specify item-specific behavior via template methods.
 */
public abstract class AddLessonItemCommand extends Command {
    protected final Index index;

    /**
     * Creates a AddLessonItemCommand.
     *
     * @param index The index of the person in the displayed list.
     */
    public AddLessonItemCommand(Index index) {
        requireNonNull(index);
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        Person updatedPerson = getPersonWithLessonItemAdded(personToEdit);

        model.setPerson(personToEdit, updatedPerson);
        return new CommandResult(getSuccessMessage());
    }

    protected abstract Person getPersonWithLessonItemAdded(Person personToEdit) throws CommandException;

    protected abstract String getSuccessMessage();
}
