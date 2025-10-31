package tutortrack.logic.parser;

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
        ParserUtil.IndexDatePair parsed = ParserUtil.parseIndexAndDate(args, DeletePlanCommand.MESSAGE_USAGE);
        return new DeletePlanCommand(parsed.getIndex(), parsed.getDate());
    }
}

