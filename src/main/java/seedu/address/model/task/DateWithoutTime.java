package seedu.address.model.task;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateWithoutTime extends DateValue {

    private String value;
    private final Date date;

    public DateWithoutTime(Date date) {
        this.date = date;
        SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy");
        this.value = df.format(date);
    }

    @Override
    public String getStringValue() {
        return value;
    }

    @Override
    public Date getFullDate() {
        return this.date;
    }

    @Override
    public int getDate() {
        return this.date.getDate();
    }

    @Override
    public int getMonth() {
        return this.date.getMonth() + 1;
    }

    @Override
    public int getYear() {
        return this.date.getYear() + 1900;
    }

    @Override
    public int getHour() {
        return -1;
    }

    @Override
    public int getMinute() {
        return -1;
    }

    @Override
    public String getDateValue() {
        return this.value;
    }

    @Override
    public String getTimeValue() {
        return "";
    }

    @Override
    public void setDate(int date) {
        this.date.setDate(date);
        updateStringValue();
    }

    @Override
    public void setMonth(int month) {
        this.setMonth(month - 1);
        updateStringValue();
    }

    @Override
    public void setYear(int year) {
        this.date.setYear(year - 1900);
        updateStringValue();
    }

    @Override
    public void setHour(int hour) {
        this.date.setHours(0);
        updateStringValue();
    }

    @Override
    public void setMinute(int minute) {
        this.date.setMinutes(0);
        updateStringValue();
    }

    @Override
    public String toString() {
        return this.value;
    }

    private void updateStringValue() {
        SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm");
        this.value = df.format(date);
    }

}
