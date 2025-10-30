package tutortrack.logic.commands;

import static java.util.Objects.requireNonNull;
import static tutortrack.logic.parser.CliSyntax.PREFIX_LESSON_PLAN;

import tutortrack.commons.core.index.Index;
import tutortrack.logic.commands.exceptions.CommandException;
import tutortrack.model.lesson.LessonPlan;
import tutortrack.model.person.Person;

/**
 * Edits an existing lesson plan entry of a person identified by the displayed index.
 */
public class EditPlanCommand extends AddLessonItemCommand {
    public static final String COMMAND_WORD = "editplan";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits an existing lesson plan of the person "
                                                       + "identified by the index number used in the displayed person "
                                                       + "list.\n"
                                                       + "Parameters: INDEX (must be a positive integer) "
                                                       + PREFIX_LESSON_PLAN + "DATE|NEW_PLAN\n"
                                                       + "Example: " + COMMAND_WORD + " 1 "
                                                       + PREFIX_LESSON_PLAN + "2025-10-15|Cover Chapter 6\n";

    public static final String MESSAGE_SUCCESS_EDIT = "Lesson plan on %1$s updated: %2$s";
    public static final String MESSAGE_NOT_FOUND =
            "No lesson plan found on %1$s. You might want to use 'addplan' instead.";

    private final LessonPlan toEdit;

    /**
     * The constructor of the class.
     */
    public EditPlanCommand(Index index, LessonPlan toEdit) {
        super(index);
        requireNonNull(toEdit);
        this.toEdit = toEdit;
    }

    @Override
    protected Person getPersonWithLessonItemAdded(Person personToEdit) throws CommandException {
        if (!personToEdit.hasPlanOnDate(toEdit.getDate())) {
            throw new CommandException(String.format(MESSAGE_NOT_FOUND, toEdit.getDate()));
        }

        personToEdit = personToEdit.withPlanRemovedOnDate(toEdit.getDate());
        personToEdit.addLessonPlan(toEdit);
        return personToEdit;
    }

    @Override
    protected String getSuccessMessage() {
        return String.format(MESSAGE_SUCCESS_EDIT, toEdit.getDate(), toEdit.getPlan());
    }
}
