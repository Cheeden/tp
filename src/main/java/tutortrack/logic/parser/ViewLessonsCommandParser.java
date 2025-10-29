package tutortrack.logic.parser;

import static tutortrack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import tutortrack.commons.core.index.Index;
import tutortrack.logic.commands.ViewLessonsCommand;
import tutortrack.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ViewLessonsCommand object
 */
public class ViewLessonsCommandParser implements Parser<ViewLessonsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ViewLessonsCommand
     * and returns a ViewLessonsCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ViewLessonsCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new ViewLessonsCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewLessonsCommand.MESSAGE_USAGE), pe);
        }
    }

}
