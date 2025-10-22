package tutortrack.logic.commands;

import static java.util.Objects.requireNonNull;
import static tutortrack.logic.parser.CliSyntax.PREFIX_LESSON_PLAN;

import java.util.List;

import tutortrack.commons.core.index.Index;
import tutortrack.logic.Messages;
import tutortrack.logic.commands.exceptions.CommandException;
import tutortrack.model.Model;
import tutortrack.model.lesson.LessonPlan;
import tutortrack.model.person.Person;

/**
 * Adds a lesson plan entry to a person identified by the displayed index.
 */
public class AddPlanCommand extends Command {
    public static final String COMMAND_WORD = "addplan";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Add lesson plan of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_LESSON_PLAN + "DATE|PLAN\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_LESSON_PLAN + "2025-10-15|Cover Chapter 5\n";

    public static final String MESSAGE_SUCCESS = "New lesson plan added: %1$s";
    public static final String MESSAGE_DUPLICATE_PLAN =
            "You already has a plan for the student on %s. Try edit the plan using editplan.";

    private final Index index;
    private final LessonPlan toAdd;

    /**
     * The constructor of the class.
     */
    public AddPlanCommand(Index index, LessonPlan toAdd) {
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

        if (personToEdit.hasPlanOnDate(toAdd.getDate())) {
            throw new CommandException(String.format(
                    MESSAGE_DUPLICATE_PLAN,
                    toAdd.getDate()));
        }

        personToEdit.addLessonPlan(toAdd);

        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }
}
