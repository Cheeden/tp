package tutortrack.logic.commands;

import static java.util.Objects.requireNonNull;
import static tutortrack.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static tutortrack.logic.parser.CliSyntax.PREFIX_COST;
import static tutortrack.logic.parser.CliSyntax.PREFIX_DAYTIME;
import static tutortrack.logic.parser.CliSyntax.PREFIX_EMAIL;
import static tutortrack.logic.parser.CliSyntax.PREFIX_LESSON_PROGRESS;
import static tutortrack.logic.parser.CliSyntax.PREFIX_NAME;
import static tutortrack.logic.parser.CliSyntax.PREFIX_PHONE;
import static tutortrack.logic.parser.CliSyntax.PREFIX_SUBJECTLEVEL;
import static tutortrack.logic.parser.CliSyntax.PREFIX_TAG;
import static tutortrack.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import tutortrack.commons.core.index.Index;
import tutortrack.commons.util.CollectionUtil;
import tutortrack.commons.util.ToStringBuilder;
import tutortrack.logic.Messages;
import tutortrack.logic.commands.exceptions.CommandException;
import tutortrack.model.Model;
import tutortrack.model.person.Address;
import tutortrack.model.person.Email;
import tutortrack.model.lesson.LessonProgress;
import tutortrack.model.person.Name;
import tutortrack.model.person.Person;
import tutortrack.model.person.Phone;
import tutortrack.model.tag.Tag;

/**
 * Edits the details of an existing lesson in the address book.
 */
public class AddProgressCommand extends Command {
    public static final String COMMAND_WORD = "add progress";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Add lesson progress of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_LESSON_PROGRESS + "DATE|PROGRESS]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_LESSON_PROGRESS + "2025-10-15 \"Covered Chapter 5\"\n";

    public static final String MESSAGE_SUCCESS = "New lesson progress added: %1$s";

    private final Index index;
    private final LessonProgress toAdd;

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

        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

}

