package tutortrack.logic.parser;

import static tutortrack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutortrack.logic.parser.CliSyntax.PREFIX_LESSON_PROGRESS;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import tutortrack.logic.commands.EditProgressCommand;
import tutortrack.logic.parser.exceptions.ParseException;
import tutortrack.model.lesson.LessonProgress;
import tutortrack.commons.core.index.Index;

/**
 * Parses input arguments and creates a new EditProgressCommand object
 */
public class EditProgressCommandParser implements Parser<EditProgressCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditProgressCommand
     * and returns an EditProgressCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public EditProgressCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_LESSON_PROGRESS);

        String preamble = argMultimap.getPreamble();
        if (preamble.isEmpty() || !argMultimap.getValue(PREFIX_LESSON_PROGRESS).isPresent()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditProgressCommand.MESSAGE_USAGE));
        }

        Index index;
        try {
            index = ParserUtil.parseIndex(preamble);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditProgressCommand.MESSAGE_USAGE), pe);
        }

        String lpInput = argMultimap.getValue(PREFIX_LESSON_PROGRESS).get();
        String[] parts = lpInput.split("\\|", 2);
        if (parts.length < 2) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditProgressCommand.MESSAGE_USAGE));
        }

        LocalDate date;
        try {
            date = LocalDate.parse(parts[0].trim());
        } catch (DateTimeParseException e) {
            throw new ParseException("Invalid date format. Use YYYY-MM-DD.");
        }

        String progress = parts[1].trim();
        if (progress.isEmpty()) {
            throw new ParseException("Lesson progress description cannot be empty.");
        }

        LessonProgress toEdit = new LessonProgress(date, progress);

        return new EditProgressCommand(index, toEdit);
    }
}
