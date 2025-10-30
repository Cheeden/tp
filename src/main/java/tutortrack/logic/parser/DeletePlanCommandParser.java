package tutortrack.logic.parser;

import static tutortrack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.time.LocalDate;

import tutortrack.commons.core.index.Index;
import tutortrack.logic.commands.DeletePlanCommand;
import tutortrack.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeletePlanCommand object.
 */
public class DeletePlanCommandParser implements Parser<DeletePlanCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeletePlanCommand
     * and returns a DeletePlanCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeletePlanCommand parse(String args) throws ParseException {
        String trimmedArguments = args.trim();
        String[] parts = trimmedArguments.split("\\s+");

        if (parts.length != 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    DeletePlanCommand.MESSAGE_USAGE));
        }

        try {
            Index index = ParserUtil.parseIndex(parts[0]);
            LocalDate date = ParserUtil.parseDate(parts[1]);
            return new DeletePlanCommand(index, date);
        } catch (ParseException pe) {
            // Preserve the specific error message and append usage information for context
            throw new ParseException(pe.getMessage() + "\n" + DeletePlanCommand.MESSAGE_USAGE, pe);
        }
    }
}

