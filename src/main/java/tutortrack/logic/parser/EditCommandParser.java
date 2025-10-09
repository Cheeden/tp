package tutortrack.logic.parser;

import static java.util.Objects.requireNonNull;
import static tutortrack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutortrack.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static tutortrack.logic.parser.CliSyntax.PREFIX_EMAIL;
import static tutortrack.logic.parser.CliSyntax.PREFIX_LESSON_PROGRESS;
import static tutortrack.logic.parser.CliSyntax.PREFIX_NAME;
import static tutortrack.logic.parser.CliSyntax.PREFIX_PHONE;
import static tutortrack.logic.parser.CliSyntax.PREFIX_TAG;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import tutortrack.commons.core.index.Index;
import tutortrack.logic.commands.EditCommand;
import tutortrack.logic.commands.EditCommand.EditPersonDescriptor;
import tutortrack.logic.parser.exceptions.ParseException;
import tutortrack.model.person.LessonProgress;
import tutortrack.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG,
                    PREFIX_LESSON_PROGRESS);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editPersonDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editPersonDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editPersonDescriptor.setAddress(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
        }
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editPersonDescriptor::setTags);
        parseLessonProgressForEdit(argMultimap.getAllValues(PREFIX_LESSON_PROGRESS))
                .ifPresent(editPersonDescriptor::setLessonProgressList);

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

    private Optional<List<LessonProgress>> parseLessonProgressForEdit(Collection<String> progresses)
            throws ParseException {
        if (progresses.isEmpty()) {
            return Optional.empty();
        }
        List<LessonProgress> lessonProgressList = new ArrayList<>();
        for (String progressStr : progresses) {
            // 假设格式是 "yyyy-MM-dd Progress description"
            String[] parts = progressStr.split(" ", 2);
            if (parts.length < 2) {
                throw new ParseException("Lesson progress must be in format: yyyy-MM-dd description");
            }
            LocalDate date;
            try {
                date = LocalDate.parse(parts[0]);
            } catch (Exception e) {
                throw new ParseException("Invalid date format for lesson progress: " + parts[0]);
            }
            lessonProgressList.add(new LessonProgress(date, parts[1]));
        }
        return Optional.of(lessonProgressList);
    }
}
