package tutortrack.logic.commands;

import static java.util.Objects.requireNonNull;
import static tutortrack.logic.parser.CliSyntax.PREFIX_LESSON_PROGRESS;

import java.util.List;
import java.util.stream.Collectors;

import tutortrack.commons.core.index.Index;
import tutortrack.logic.Messages;
import tutortrack.logic.commands.exceptions.CommandException;
import tutortrack.model.Model;
import tutortrack.model.lesson.LessonProgress;
import tutortrack.model.person.Person;

/**
 * Edits a lesson progress entry of a person identified by the displayed index.
 */
public class EditProgressCommand extends Command {
    public static final String COMMAND_WORD = "editprogress";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edit lesson progress of the person identified "
                                                       + "by the index number used in the displayed person list. "
                                                       + "If the date does not exist, add new lesson progress.\n"
                                                       + "Parameters: INDEX (must be a positive integer) "
                                                       + PREFIX_LESSON_PROGRESS + "DATE|NEW_PROGRESS\n"
                                                       + "Example: " + COMMAND_WORD + " 1 "
                                                       + PREFIX_LESSON_PROGRESS + "2025-10-15|Reviewed Chapter 5\n";

    public static final String MESSAGE_SUCCESS_EDIT = "Lesson progress on %1$s updated: %2$s";
    public static final String MESSAGE_SUCCESS_ADD = "No progress on %1$s existed. Added new progress: %2$s";
    public static final String MESSAGE_DUPLICATE_DATE = "Multiple progress entries exist on %1$s. Cannot edit.";

    private final Index index;
    private final LessonProgress toEdit;

    /**
     * The constructor of the class.
     * @param index the index of the person in the displayed list to be edited
     * @param toEdit the new lesson progress
     */
    public EditProgressCommand(Index index, LessonProgress toEdit) {
        this.index = index;
        this.toEdit = toEdit;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

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

        editedPerson.getLessonProgressList().addAll(personToEdit.getLessonProgressList());
        editedPerson.getLessonPlanList().addAll(personToEdit.getLessonPlanList());

        List<LessonProgress> sameDateList = editedPerson.getLessonProgressList().stream()
                                                    .filter(lp -> lp.getDate().equals(toEdit.getDate()))
                                                    .collect(Collectors.toList());

        if (sameDateList.size() > 1) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_DATE, toEdit.getDate()));
        } else if (sameDateList.size() == 1) {
            LessonProgress oldProgress = sameDateList.get(0);
            editedPerson.getLessonProgressList().remove(oldProgress);
            editedPerson.getLessonProgressList().add(toEdit);
            model.setPerson(personToEdit, editedPerson);
            return new CommandResult(String.format(MESSAGE_SUCCESS_EDIT, toEdit.getDate(), toEdit.getProgress()));
        } else {
            editedPerson.getLessonProgressList().add(toEdit);
            model.setPerson(personToEdit, editedPerson);
            return new CommandResult(String.format(MESSAGE_SUCCESS_ADD, toEdit.getDate(), toEdit.getProgress()));
        }
    }
}
