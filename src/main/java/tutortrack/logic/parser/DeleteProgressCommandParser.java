package tutortrack.logic.parser;

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
        ParserUtil.IndexDatePair parsed = ParserUtil.parseIndexAndDate(args, DeleteProgressCommand.MESSAGE_USAGE);
        return new DeleteProgressCommand(parsed.getIndex(), parsed.getDate());
    }
}
