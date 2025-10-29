package tutortrack.logic.commands;

import java.time.LocalDate;

import tutortrack.commons.core.index.Index;
import tutortrack.model.person.Person;

/**
 * Deletes a lesson progress entry by date from a person identified by the displayed index.
 */
public class DeleteProgressCommand extends DeleteLessonItemCommand {

    public static final String COMMAND_WORD = "deleteprogress";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the lesson progress on the specified date for the person identified "
            + "by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) DATE (must be in format YYYY-MM-DD)\n"
            + "Example: " + COMMAND_WORD + " 1 2025-10-15";

    public static final String MESSAGE_SUCCESS = "Lesson progress on %1$s deleted";
    public static final String MESSAGE_NO_PROGRESS_ON_DATE = "No lesson progress found on %1$s for this student";

    /**
     * Creates a DeleteProgressCommand to delete the lesson progress at the specified date.
     *
     * @param index The index of the person in the displayed list.
     * @param date The date of the lesson progress to delete.
     */
    public DeleteProgressCommand(Index index, LocalDate date) {
        super(index, date);
    }

    @Override
    protected Person createPersonWithItemRemoved(Person person, LocalDate date) {
        return person.withProgressRemovedOnDate(date);
    }

    @Override
    protected String getSuccessMessage() {
        return String.format(MESSAGE_SUCCESS, date);
    }

    @Override
    protected String getNoItemMessage() {
        return String.format(MESSAGE_NO_PROGRESS_ON_DATE, date);
    }
}
