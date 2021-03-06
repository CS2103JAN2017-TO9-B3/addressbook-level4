package seedu.mypotato.logic.parser;

import java.io.File;

import seedu.mypotato.logic.commands.Command;
import seedu.mypotato.logic.commands.SaveCommand;

//@@author A0135807A
/**
 * Parses input arguments and creates a new SaveCommand object
 */
public class SaveCommandParser {

    /**
     * Parses the given {@code String args} of arguments in the context of the SaveCommand
     * and returns an SaveCommand object for execution.
     */
    public Command parse(String args) {
        if (args.trim().isEmpty()) {
            File file = null;
            return new SaveCommand(file);
        }
        File file = new File(args.trim());
        return new SaveCommand(file);
    }

}
