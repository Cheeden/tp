package tutortrack.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import tutortrack.commons.core.index.Index;
import tutortrack.commons.util.ToStringBuilder;
import tutortrack.logic.Messages;
import tutortrack.logic.commands.exceptions.CommandException;
import tutortrack.model.Model;
import tutortrack.model.person.Person;

/**
 * Shows lesson progress in a separate window.
 */
public class ViewLessonProgressCommand extends Command {

    public static final String COMMAND_WORD = "viewlessons";
    public static final String MESSAGE_SUCCESS = "Opened lesson summary window for %1$s.";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Shows the lesson summary of the person identified by the index number used in the displayed "
            + "person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    private final Index targetIndex;

    /**
     * Creates a ViewLessonProgressCommand to view the lesson progress of the person at the specified index.
     */
    public ViewLessonProgressCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToView = lastShownList.get(targetIndex.getZeroBased());
        return new CommandResult(String.format(MESSAGE_SUCCESS, personToView.getName()),
                false, false, true, personToView);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ViewLessonProgressCommand)) {
            return false;
        }

        ViewLessonProgressCommand otherCommand = (ViewLessonProgressCommand) other;
        return targetIndex.equals(otherCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}

