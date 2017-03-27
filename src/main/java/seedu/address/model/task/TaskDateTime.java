package seedu.address.model.task;

import java.util.Optional;

import seedu.address.commons.exceptions.IllegalValueException;

//@@author A0144895N
/**
 * Represents a Task's due date in the task manager. Guarantees: immutable; is
 * valid as declared in {@link #isValidDate(String)}
 */
public class TaskDateTime {

    public static final String MESSAGE_START_END_INVALID = "Start time should be before end time";
    public static final String DEFAULT_VALUE = "";

    private final DateValue startDateTime;
    private final DateValue endDateTime;

    /**
     * Validates given name.
     *
     * @throws IllegalValueException
     *             if given name string is invalid.
     */
    public TaskDateTime(String startDateTime, String endDateTime) throws IllegalValueException {

        String trimmedStartDateTime = startDateTime.trim();
        String trimmedEndDateTime = endDateTime.trim();

        DateMaker maker = new DateMaker();

        maker.makeDate(trimmedStartDateTime);
        this.startDateTime = maker.getDateValue();

        maker.makeDate(trimmedEndDateTime);
        this.endDateTime = maker.getDateValue();

        alignStartDateTime();

        if (!isValidStartAndEndDateTime()) {
            throw new IllegalValueException(MESSAGE_START_END_INVALID);
        }
    }

    public TaskDateTime(DateValue startDateTime, DateValue endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    /*
     * Returns true if start date comes before end date or there is no either start date or end date
     */
    private boolean isValidStartAndEndDateTime() {
        return ((!isThereStartDateTime() || (!isThereEndDateTime())
                || isThereStartDateTime() && isThereEndDateTime()
                && this.startDateTime.getFullDate().before(this.endDateTime.getFullDate())));
    }

    /*
     * Assigns date of end date to start date (if there is end date) or current date (if there is no end date)
     * if start date has only time
     */
    private void alignStartDateTime() {
        if (isThereStartDateTime() && this.startDateTime.getYear() < DateMaker.OLDEST_YEAR) {
            if (isThereEndDateTime()) {
                this.startDateTime.setDate(this.endDateTime.getDate());
                this.startDateTime.setMonth(this.endDateTime.getMonth());
                this.startDateTime.setYear(this.endDateTime.getYear());
            } else {
                DateValue currentTime = DateMaker.getCurrentTime();
                this.startDateTime.setDate(currentTime.getDate());
                this.startDateTime.setMonth(currentTime.getMonth());
                this.startDateTime.setYear(currentTime.getYear());
            }
        } else if (isThereEndDateTime() && this.endDateTime.getYear() < DateMaker.OLDEST_YEAR) {
            DateValue currentTime = DateMaker.getCurrentTime();
            this.endDateTime.setDate(currentTime.getDate());
            this.endDateTime.setMonth(currentTime.getMonth());
            this.endDateTime.setYear(currentTime.getYear());
        }
    }

    /**
     * Returns true if the start time is not null
     */
    public boolean isThereStartDateTime() {
        return this.startDateTime != null;
    }

    /**
     * Returns true if the end time is not null
     */
    public boolean isThereEndDateTime() {
        return this.endDateTime != null;
    }

    public Optional<DateValue> getStartDateTime() {
        return isThereStartDateTime() ? Optional.of(this.startDateTime) : Optional.empty();
    }

    public Optional<DateValue> getEndDateTime() {
        return isThereEndDateTime() ? Optional.of(this.endDateTime) : Optional.empty();
    }

    @Override
    public String toString() {
        if (!isThereStartDateTime() && !isThereEndDateTime()) {
            return "";
        } else if (!isThereStartDateTime()) {
            return this.endDateTime.getStringValue();
        } else if (!isThereEndDateTime()) {
            return this.startDateTime.getStringValue();
        } else {
            if (this.startDateTime.getDateValue().equals(this.endDateTime.getDateValue())) {
                return this.startDateTime.getDateValue() + " "
                        + this.startDateTime.getTimeValue() + " - " + this.endDateTime.getTimeValue();
            } else {
                return this.startDateTime.getStringValue() + " - " + this.endDateTime.getStringValue();
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskDateTime // instanceof handles nulls
                        && this.toString().equals(((TaskDateTime) other).toString())); // state
        // check
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

}
