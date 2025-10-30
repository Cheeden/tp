package tutortrack.logic.commands;

import static java.util.Objects.requireNonNull;
import static tutortrack.logic.parser.CliSyntax.PREFIX_LESSON_PROGRESS;

import tutortrack.commons.core.index.Index;
import tutortrack.logic.commands.exceptions.CommandException;
import tutortrack.model.lesson.LessonProgress;
import tutortrack.model.person.Person;

/**
 * Edits an existing lesson progress entry of a person identified by the displayed index.
 */
public class EditProgressCommand extends ModifyLessonItemCommand {
    public static final String COMMAND_WORD = "editprogress";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits an existing lesson progress of the person "
                                                       + "identified by the index number used in the displayed person "
                                                       + "list.\n"
                                                       + "Parameters: INDEX (must be a positive integer) "
                                                       + PREFIX_LESSON_PROGRESS + "DATE|NEW_PROGRESS\n"
                                                       + "Example: " + COMMAND_WORD + " 1 "
                                                       + PREFIX_LESSON_PROGRESS + "2025-10-15|Reviewed Chapter 5\n";

    public static final String MESSAGE_SUCCESS_EDIT = "Lesson progress on %1$s updated: %2$s";
    public static final String MESSAGE_NOT_FOUND =
            "No lesson progress found on %1$s. You might want to use 'addprogress' instead.";

    private final LessonProgress toEdit;

    /**
     * The constructor of the class.
     */
    public EditProgressCommand(Index index, LessonProgress toEdit) {
        super(index);
        requireNonNull(toEdit);
        this.toEdit = toEdit;
    }

    @Override
    protected Person getPersonWithLessonItemModified(Person personToEdit) throws CommandException {
        if (!personToEdit.hasProgressOnDate(toEdit.getDate())) {
            throw new CommandException(String.format(MESSAGE_NOT_FOUND, toEdit.getDate()));
        }
        personToEdit = personToEdit.withProgressRemovedOnDate(toEdit.getDate());
        personToEdit.addLessonProgress(toEdit);
        return personToEdit;
    }

    @Override
    protected String getSuccessMessage() {
        return String.format(MESSAGE_SUCCESS_EDIT, toEdit.getDate(), toEdit.getProgress());
    }
}