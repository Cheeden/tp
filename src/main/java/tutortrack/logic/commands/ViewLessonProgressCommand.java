package tutortrack.logic.commands;

import tutortrack.model.Model;

/**
 * Shows lesson progress in a separate window.
 */
public class ViewLessonProgressCommand extends Command {

    public static final String COMMAND_WORD = "viewprogress";
    public static final String MESSAGE_SUCCESS = "Opened lesson progress window.";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(MESSAGE_SUCCESS, false, false, true);
    }
}
