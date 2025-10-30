package tutortrack.logic.parser;

import static tutortrack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.time.LocalDate;

import tutortrack.commons.core.index.Index;
import tutortrack.logic.commands.DeleteProgressCommand;
import tutortrack.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteProgressCommand object.
 */
public class DeleteProgressCommandParser implements Parser<DeleteProgressCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteProgressCommand
     * and returns a DeleteProgressCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteProgressCommand parse(String args) throws ParseException {
        String trimmedArguments = args.trim();
        String[] parts = trimmedArguments.split("\\s+");

        if (parts.length != 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    DeleteProgressCommand.MESSAGE_USAGE));
        }

        try {
            Index index = ParserUtil.parseIndex(parts[0]);

            // Parser Util handles the date parsing and validation to ensure dates are valid
            LocalDate date = ParserUtil.parseDate(parts[1]);
            return new DeleteProgressCommand(index, date);
        } catch (ParseException pe) {
            // Preserve the specific error message and append usage information for context
            throw new ParseException(pe.getMessage() + "\n" + DeleteProgressCommand.MESSAGE_USAGE, pe);
        }
    }
}
