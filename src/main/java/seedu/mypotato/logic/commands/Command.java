package seedu.mypotato.logic.commands;

import seedu.mypotato.commons.core.Messages;
import seedu.mypotato.logic.commands.exceptions.CommandException;
import seedu.mypotato.model.Model;
import seedu.mypotato.model.tag.UniqueTagList.DuplicateTagException;
import seedu.mypotato.model.task.UniqueTaskList.DuplicateTaskException;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {
    protected Model model;

    /**
     * Constructs a feedback message to summarise an operation that displayed a listing of tasks.
     *
     * @param displaySize used to generate summary
     * @return summary message for tasks displayed
     */
    public static String getMessageForTaskListShownSummary(int displaySize) {
        return String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, displaySize);
    }

    /**
     * Executes the command and returns the result message.
     *
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     * @throws DuplicateTaskException \
     * @throws DuplicateTagException
     */
    public abstract CommandResult execute() throws CommandException;

    /**
     * Provides any needed dependencies to the command.
     * Commands making use of any of these should override this method to gain
     * access to the dependencies.
     */
    public void setData(Model model) {
        this.model = model;
    }
}