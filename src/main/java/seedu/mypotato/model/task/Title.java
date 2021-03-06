package seedu.mypotato.model.task;

import seedu.mypotato.commons.exceptions.IllegalValueException;

//@@author A0144895N - reused
/**
 * Represents a Task's title in the task manager.
 * Guarantees: immutable; is valid as declared in {@link #isValidTitle(String)}
 */
public class Title {

    public static final String MESSAGE_TITLE_CONSTRAINTS =
            "Task title should only contain alphanumeric characters, spaces, dot, comma and hyphen,"
            + " and it should not be blank";

    /*
     * The first character of the task title must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String CONTENT_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ][-\\p{Alnum}.,/ ]*";

    public final String fullTitle;

    /**
     * Validates given content.
     *
     * @throws IllegalValueException if given name string is invalid.
     */
    public Title(String title) throws IllegalValueException {
        assert title != null;
        String trimmedTitle = title.trim();
        if (!isValidTitle(trimmedTitle)) {
            throw new IllegalValueException(MESSAGE_TITLE_CONSTRAINTS);
        }
        this.fullTitle = trimmedTitle;
    }

    /**
     * Returns true if a given string is a valid person name.
     */
    public static boolean isValidTitle(String test) {
        return test.matches(CONTENT_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullTitle;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Title // instanceof handles nulls
                && this.fullTitle.equals(((Title) other).fullTitle)); // state check
    }

    @Override
    public int hashCode() {
        return fullTitle.hashCode();
    }

}
