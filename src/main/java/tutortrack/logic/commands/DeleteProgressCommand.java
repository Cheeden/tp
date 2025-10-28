package tutortrack.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import tutortrack.commons.core.index.Index;
import tutortrack.commons.util.ToStringBuilder;
import tutortrack.logic.Messages;
import tutortrack.logic.commands.exceptions.CommandException;
import tutortrack.model.Model;
import tutortrack.model.person.Person;

/**
 * Delete a lesson progress entry by date from a person identified by a given index.
 */
public class DeleteProgressCommand extends Command {
    public static final String COMMAND_WORD = "deleteprogress";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Deletes the lesson progress on the specified date for the person identified "
        + "by the index number used in the displayed person list.\n"
        + "Parameters: INDEX (must be a positive integer) DATE (must be in format YYYY-MM-DD)\n"
        + "Example: " + COMMAND_WORD + " 1 2025-10-15";

    public static final String MESSAGE_SUCCESS = "Lesson progress on %1$s deleted";
    public static final String MESSAGE_NO_PROGRESS_ON_DATE = "No lesson progress found on %1$s for this student";

    private static final Logger logger = Logger.getLogger(DeleteProgressCommand.class.getName());

    private final LocalDate date;
    private final Index index;

    /**
     * Creates a DeleteProgressCommand to delete the lesson progress at the specified date.
     *
     * @param index The index of the person in the displayed list.
     * @param date The date of the lesson progress to delete.
     */
    public DeleteProgressCommand(Index index, LocalDate date) {
        requireNonNull(index);
        requireNonNull(date);

        assert index.getZeroBased() >= 0 : "Index must be non-negative";

        this.index = index;
        this.date = date;

        logger.fine("Created DeleteProgressCommand with index: " + index + ", date: " + date);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        logger.info("Executing DeleteProgressCommand for zero-based index: " + index.getZeroBased());

        // Check if the index is valid
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        // Get the person to edit
        Person personToEdit = lastShownList.get(index.getZeroBased());

        // Check if a lesson progress exists on the date
        if (!personToEdit.hasProgressOnDate(date)) {
            throw new CommandException(String.format(MESSAGE_NO_PROGRESS_ON_DATE, date));
        }

        // Create new person and copy all the data from the original person
        Person newPerson = new Person(
                personToEdit.getName(),
                personToEdit.getSelfContact(),
                personToEdit.getNokContact(),
                personToEdit.getSubjectLevel(),
                personToEdit.getDayTime(),
                personToEdit.getCost(),
                personToEdit.getAddress(),
                personToEdit.getTags()
        );

        // Ensure updated person was created and is different from the original person
        assert newPerson != null : "Updated person should not be null after construction";
        assert newPerson != personToEdit : "Updated person should be a different object";

        // Copy all lesson plans and lesson progress to the new person
        newPerson.getLessonPlanList().addAll(personToEdit.getLessonPlanList());
        newPerson.getLessonProgressList().addAll(personToEdit.getLessonProgressList());

        // Remove the lesson progress from the new person
        newPerson.removeLessonProgressByDate(date);

        // Update the model with the edited person
        model.setPerson(personToEdit, newPerson);

        return new CommandResult(String.format(MESSAGE_SUCCESS, date));
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

        // Defensively check if the other object is an instance of DeleteProgressCommand
        if (!(other instanceof DeleteProgressCommand)) {
            return false;
        }

        DeleteProgressCommand otherDeleteProgressCommand = (DeleteProgressCommand) other;
        return index.equals(otherDeleteProgressCommand.index)
                && date.equals(otherDeleteProgressCommand.date);
    }
}
