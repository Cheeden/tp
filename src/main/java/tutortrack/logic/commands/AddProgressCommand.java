package tutortrack.logic.commands;

import static java.util.Objects.requireNonNull;
import static tutortrack.logic.parser.CliSyntax.PREFIX_LESSON_PROGRESS;

import java.util.List;

import tutortrack.commons.core.index.Index;
import tutortrack.logic.Messages;
import tutortrack.logic.commands.exceptions.CommandException;
import tutortrack.model.Model;
import tutortrack.model.lesson.LessonProgress;
import tutortrack.model.person.Person;

/**
 * Adds a lesson progress entry to a person identified by the displayed index.
 */
public class AddProgressCommand extends Command {
    public static final String COMMAND_WORD = "addprogress";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Add lesson progress of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_LESSON_PROGRESS + "DATE|PROGRESS\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_LESSON_PROGRESS + "2025-10-15|Covered Chapter 5\n";

    public static final String MESSAGE_SUCCESS = "New lesson progress added: %1$s";
    public static final String MESSAGE_DUPLICATE_PROGRESS =
            "This student already has a progress on %s. Try edit the progress using editprogress.";

    private final Index index;
    private final LessonProgress toAdd;

    /**
     * The constructor of the class.
     */
    public AddProgressCommand(Index index, LessonProgress toAdd) {
        this.index = index;
        this.toAdd = toAdd;
    }


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        if (personToEdit.hasProgressOnDate(toAdd.getDate())) {
            throw new CommandException(String.format(
                    MESSAGE_DUPLICATE_PROGRESS,
                    toAdd.getDate()));
        }

        personToEdit.addLessonProgress(toAdd);

        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

}

