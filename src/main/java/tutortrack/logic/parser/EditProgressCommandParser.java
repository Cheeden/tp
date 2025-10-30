package tutortrack.logic.parser;

import static tutortrack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutortrack.logic.parser.CliSyntax.PREFIX_LESSON_PROGRESS;

import java.util.stream.Stream;

import tutortrack.commons.core.index.Index;
import tutortrack.logic.commands.EditProgressCommand;
import tutortrack.logic.parser.exceptions.ParseException;
import tutortrack.model.lesson.LessonProgress;

/**
 * Parses input arguments and creates a new EditProgressCommand object.
 */
public class EditProgressCommandParser implements Parser<EditProgressCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditProgressCommand
     * and returns an EditProgressCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public EditProgressCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_LESSON_PROGRESS);

        if (!arePrefixesPresent(argMultimap, PREFIX_LESSON_PROGRESS)
                    || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditProgressCommand.MESSAGE_USAGE));
        }

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble());

        String prString = argMultimap.getValue(PREFIX_LESSON_PROGRESS).get();
        LessonProgress updatedProgress = ParserUtil.parseLessonProgress(prString);

        return new EditProgressCommand(index, updatedProgress);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values
     * in the given {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
