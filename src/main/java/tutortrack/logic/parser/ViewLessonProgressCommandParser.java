package tutortrack.logic.parser;

import static tutortrack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import tutortrack.commons.core.index.Index;
import tutortrack.logic.commands.ViewLessonProgressCommand;
import tutortrack.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ViewLessonProgressCommand object
 */
public class ViewLessonProgressCommandParser implements Parser<ViewLessonProgressCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ViewLessonProgressCommand
     * and returns a ViewLessonProgressCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ViewLessonProgressCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new ViewLessonProgressCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewLessonProgressCommand.MESSAGE_USAGE), pe);
        }
    }

}
