package tutortrack.logic.parser;

import static tutortrack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutortrack.logic.parser.CliSyntax.PREFIX_LESSON_PLAN;

import java.util.stream.Stream;

import tutortrack.commons.core.index.Index;
import tutortrack.logic.commands.EditPlanCommand;
import tutortrack.logic.parser.exceptions.ParseException;
import tutortrack.model.lesson.LessonPlan;

/**
 * Parses input arguments and creates a new EditPlanCommand object.
 */
public class EditPlanCommandParser implements Parser<EditPlanCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditPlanCommand
     * and returns an EditPlanCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public EditPlanCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_LESSON_PLAN);

        if (!arePrefixesPresent(argMultimap, PREFIX_LESSON_PLAN)
                    || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditPlanCommand.MESSAGE_USAGE));
        }

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble());
        String lpString = argMultimap.getValue(PREFIX_LESSON_PLAN).get();
        LessonPlan updatedPlan = ParserUtil.parseLessonPlan(lpString);

        return new EditPlanCommand(index, updatedPlan);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
