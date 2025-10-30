package tutortrack.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.logging.Logger;

import tutortrack.commons.core.index.Index;
import tutortrack.logic.Messages;
import tutortrack.logic.commands.exceptions.CommandException;
import tutortrack.model.Model;
import tutortrack.model.person.Person;

/**
 * Abstract base class for commands that modify a person's lesson items
 * (e.g., adding or editing plans/progress).
 * Extracts common logic for finding a person by index and updating them.
 */
public abstract class ModifyLessonItemCommand extends Command {
    protected static final Logger LOGGER = Logger.getLogger(ModifyLessonItemCommand.class.getName());

    protected final Index index;

    /**
     * Creates a ModifyLessonItemCommand.
     *
     * @param index The index of the person in the displayed list.
     */
    public ModifyLessonItemCommand(Index index) {
        requireNonNull(index);
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            LOGGER.warning("Invalid index: " + index.getOneBased()
                    + ". List size: " + lastShownList.size());
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        Person updatedPerson = getPersonWithLessonItemModified(personToEdit);

        model.setPerson(personToEdit, updatedPerson);
        return new CommandResult(getSuccessMessage());
    }

    protected abstract Person getPersonWithLessonItemModified(Person personToEdit) throws CommandException;

    protected abstract String getSuccessMessage();
}
