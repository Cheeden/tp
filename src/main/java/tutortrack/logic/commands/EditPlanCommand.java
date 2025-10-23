package tutortrack.logic.commands;

import static java.util.Objects.requireNonNull;
import static tutortrack.logic.parser.CliSyntax.PREFIX_LESSON_PLAN;

import java.util.List;
import java.util.stream.Collectors;

import tutortrack.commons.core.index.Index;
import tutortrack.logic.Messages;
import tutortrack.logic.commands.exceptions.CommandException;
import tutortrack.model.Model;
import tutortrack.model.lesson.LessonPlan;
import tutortrack.model.person.Person;

/**
 * Edits a lesson plan entry of a person identified by the displayed index.
 */
public class EditPlanCommand extends Command {
    public static final String COMMAND_WORD = "editplan";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edit lesson plan of the person identified "
                                                       + "by the index number used in the displayed person list. "
                                                       + "If the date does not exist, add new lesson plan.\n"
                                                       + "Parameters: INDEX (must be a positive integer) "
                                                       + PREFIX_LESSON_PLAN + "DATE|NEW_PLAN\n"
                                                       + "Example: " + COMMAND_WORD + " 1 "
                                                       + PREFIX_LESSON_PLAN + "2025-10-15|Cover Chapter 6\n";

    public static final String MESSAGE_SUCCESS_EDIT = "Lesson plan on %1$s updated: %2$s";
    public static final String MESSAGE_SUCCESS_ADD = "No lesson plan on %1$s existed. Added new plan: %2$s";
    public static final String MESSAGE_DUPLICATE_DATE = "Multiple lesson plans exist on %1$s. Cannot edit.";

    private final Index index;
    private final LessonPlan toEdit;

    /**
     * The constructor of the class.
     * @param index the index of the person in the displayed list to be edited
     * @param toEdit the new lesson plan
     */
    public EditPlanCommand(Index index, LessonPlan toEdit) {
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
                personToEdit.getPhone(),
                personToEdit.getSubjectLevel(),
                personToEdit.getDayTime(),
                personToEdit.getCost(),
                personToEdit.getAddress(),
                personToEdit.getTags()
        );

        editedPerson.getLessonPlanList().addAll(personToEdit.getLessonPlanList());
        editedPerson.getLessonProgressList().addAll(personToEdit.getLessonProgressList());

        List<LessonPlan> sameDateList = editedPerson.getLessonPlanList().stream()
                                                .filter(lp -> lp.getDate().equals(toEdit.getDate()))
                                                .collect(Collectors.toList());

        if (sameDateList.size() > 1) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_DATE, toEdit.getDate()));
        } else if (sameDateList.size() == 1) {
            LessonPlan oldPlan = sameDateList.get(0);
            editedPerson.getLessonPlanList().remove(oldPlan);
            editedPerson.getLessonPlanList().add(toEdit);
            model.setPerson(personToEdit, editedPerson);
            return new CommandResult(String.format(MESSAGE_SUCCESS_EDIT, toEdit.getDate(), toEdit.getPlan()));
        } else {
            editedPerson.getLessonPlanList().add(toEdit);
            model.setPerson(personToEdit, editedPerson);
            return new CommandResult(String.format(MESSAGE_SUCCESS_ADD, toEdit.getDate(), toEdit.getPlan()));
        }
    }
}
