package seedu.mypotato.logic.parser;

import static seedu.mypotato.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Optional;

import seedu.mypotato.logic.commands.Command;
import seedu.mypotato.logic.commands.DeleteCommand;
import seedu.mypotato.logic.commands.IncorrectCommand;

//@@author A0135807A
/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * DeleteCommand and returns an DeleteCommand object for execution.
     */
    public Command parse(String args) {

        Optional<String> deadline = ParserUtil.parseDeadline(args);
        Optional<Integer> index = ParserUtil.parseIndex(args);
        if (!index.isPresent()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
        if (!deadline.isPresent()) {
            return new DeleteCommand(index.get());
        }
        return new DeleteCommand(index.get(), deadline.get());
    }

}
