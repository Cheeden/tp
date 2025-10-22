package tutortrack.logic.parser;

import static tutortrack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutortrack.logic.parser.CliSyntax.PREFIX_LESSON_PLAN;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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
    @Override
    public EditPlanCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_LESSON_PLAN);

        String preamble = argMultimap.getPreamble();
        if (preamble.isEmpty() || !argMultimap.getValue(PREFIX_LESSON_PLAN).isPresent()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditPlanCommand.MESSAGE_USAGE));
        }

        Index index;
        try {
            index = ParserUtil.parseIndex(preamble);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditPlanCommand.MESSAGE_USAGE), pe);
        }

        String lpInput = argMultimap.getValue(PREFIX_LESSON_PLAN).get();
        String[] parts = lpInput.split("\\|", 2);
        if (parts.length < 2) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditPlanCommand.MESSAGE_USAGE));
        }

        LocalDate date;
        try {
            date = LocalDate.parse(parts[0].trim());
        } catch (DateTimeParseException e) {
            throw new ParseException("Invalid date format. Use YYYY-MM-DD.");
        }

        String plan = parts[1].trim();
        if (plan.isEmpty()) {
            throw new ParseException("Lesson plan description cannot be empty.");
        }

        LessonPlan toEdit = new LessonPlan(date, plan);

        return new EditPlanCommand(index, toEdit);
    }
}
