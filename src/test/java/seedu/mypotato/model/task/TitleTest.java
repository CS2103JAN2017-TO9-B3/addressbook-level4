package seedu.mypotato.model.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TitleTest {

    @Test
    public void isValidContent() {
        // invalid title
        assertFalse(Title.isValidTitle("")); // empty string
        assertFalse(Title.isValidTitle(" ")); // spaces only
        assertFalse(Title.isValidTitle("^")); // only non-alphanumeric characters
        assertFalse(Title.isValidTitle("meeting*")); // contains non-alphanumeric characters

        // valid title
        assertTrue(Title.isValidTitle("peter jack")); // alphabets only
        assertTrue(Title.isValidTitle("12345")); // numbers only
        assertTrue(Title.isValidTitle("peter the 2nd")); // alphanumeric characters
        assertTrue(Title.isValidTitle("Capital Tan")); // with capital letters
        assertTrue(Title.isValidTitle("David Roger Jackson Ray Jr 2nd")); // long names
    }
}
