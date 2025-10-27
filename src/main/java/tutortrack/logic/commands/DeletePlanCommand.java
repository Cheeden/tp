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
 * Deletes a lesson plan entry by date from a person identified by the displayed index.
 */
public class DeletePlanCommand extends Command {

    public static final String COMMAND_WORD = "deleteplan";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Deletes the lesson plan on the specified date for the person identified "
        + "by the index number used in the displayed person list.\n"
        + "Parameters: INDEX (must be a positive integer) DATE (must be in format YYYY-MM-DD)\n"
        + "Example: " + COMMAND_WORD + " 1 2025-10-15";

    public static final String MESSAGE_SUCCESS = "Lesson plan on %1$s deleted";
    public static final String MESSAGE_NO_PLAN_ON_DATE = "No lesson plan found on %1$s for this student";

    private final LocalDate date;
    private final Index index;

    private static final Logger logger = Logger.getLogger(DeletePlanCommand.class.getName());

    /**
     * Creates a DeletePlanCommand to delete the lesson plan at the specified date.
     *
     * @param index the index of the person in the displayed list
     * @param date the date of the lesson plan to delete
     */
    public DeletePlanCommand(Index index, LocalDate date) {
        requireNonNull(index);
        requireNonNull(date);

        assert index.getZeroBased() >= 0 : "Index must be non-negative";

        this.index = index;
        this.date = date;

        Logger.fine("Created DeletePlanCommand with index: " + index + ", date: " + date);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        logger.info("Executing DeletePlanCommand for zero-based index: " + index.getZeroBased());

        // Check if the index is valid
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        // Get the person to edit
        Person personToEdit = lastShownList.get(index.getZeroBased());

        // Check if a lesson plan exists on the date 
        if (!personToEdit.hasPlanOnDate(date)) {
            throw new CommandException(String.format(MESSAGE_NO_PLAN_ON_DATE, date));
        }

        // Create new person and copy all the data from the original person
        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getSelfContact(),
                personToEdit.getNokContact(),
                personToEdit.getSubjectLevel(),
                personToEdit.getDayTime(),
                personToEdit.getCost(),
                personToEdit.getAddress(),
                personToEdit.getTags()
        );

        // Ensure new person was created and is different from the original person
        assert editedPerson != null : "Edited person should not be null after construction";
        assert editedPerson != personToEdit : "Edited person should be a different object";

        // Copy all lesson plans and lesson progress to the new person
        editedPerson.getLessonPlanList().addAll(personToEdit.getLessonPlanList());
        editedPerson.getLessonProgressList().addAll(personToEdit.getLessonProgressList());

        // Remove the lesson plan from the new person
        editedPerson.removeLessonPlanByDate(date);

        // Update the model with the edited person
        model.setPerson(personToEdit, editedPerson);

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

        // Defensively check if the other object is an instance of DeletePlanCommand
        if (!(other instanceof DeletePlanCommand)) {
            return false;
        }

        DeletePlanCommand otherDeletePlanCommand = (DeletePlanCommand) other;
        return index.equals(otherDeletePlanCommand.index) 
                && date.equals(otherDeletePlanCommand.date);
    }
}