package tutortrack.logic.parser;

import tutortrack.commons.core.index.Index;
import tutortrack.logic.commands.AddProgressCommand;
import tutortrack.logic.parser.exceptions.ParseException;
import tutortrack.model.lesson.LessonProgress;

import java.util.stream.Stream;

import static tutortrack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutortrack.logic.parser.CliSyntax.PREFIX_LESSON_PROGRESS;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddProgressCommandParser implements Parser<AddProgressCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddProgressCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_LESSON_PROGRESS);


        if (!arePrefixesPresent(argMultimap, PREFIX_LESSON_PROGRESS)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddProgressCommand.MESSAGE_USAGE));
        }

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble());

        String lpString = argMultimap.getValue(PREFIX_LESSON_PROGRESS).get();
        LessonProgress lp = ParserUtil.parseLessonProgress(lpString);

        return new AddProgressCommand(index, lp);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
