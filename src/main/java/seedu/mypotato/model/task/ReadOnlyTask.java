package seedu.mypotato.model.task;

import seedu.mypotato.model.tag.UniqueTagList;

/**
 * A read-only immutable interface for a Task in the task manager.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyTask {

    Title getTitle();
    Content getContent();
    TaskDateTime getDateTime();
    Status getStatus();

    /**
     * The returned TagList is a deep copy of the internal TagList,
     * changes on the returned list will not affect the person's internal tags.
     */
    UniqueTagList getTags();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyTask other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getTitle().equals(this.getTitle())); // state checks here onwards
    }

    /**
     * Formats the task as text, showing all contact details.
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getTitle() + "\n")
               .append("Content: " + getContent() + "\n")
               .append("Start: " + getDateTime().getStartDateTimeString() + "\n")
               .append("End: " + getDateTime().getEndDateTimeString() + "\n")
               .append("Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

}
