package tutortrack.logic.parser;

import static tutortrack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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
            String dateString = parts[1];

            // Try to parse the date
            LocalDate date = LocalDate.parse(dateString);

            return new DeleteProgressCommand(index, date);

        } catch (DateTimeParseException e) {
            String dateString = parts[1];

            // Check if format is correct (yyyy-MM-dd)
            if (!dateString.matches("\\d{4}-\\d{2}-\\d{2}")) {
                throw new ParseException(
                        "Invalid date format. Use yyyy-MM-dd (e.g., 2025-10-15).", e);
            }

            // Format is correct but values are invalid (e.g., month > 12, day > 31)
            throw new ParseException(
                    "Invalid date: month must be 01-12 and day must be valid for that month.", e);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    DeleteProgressCommand.MESSAGE_USAGE), pe);
        }
    }
}
