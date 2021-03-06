package guitests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import seedu.mypotato.logic.parser.CliSyntax;
import seedu.mypotato.ui.TaskDescription;

//@@author A0135807A
public class TaskDescriptionTest extends TaskManagerGuiTest {

    private static final String CONTENT_THAT_SUCCEEDS = "Amazon Buy 1 Get 1 Free";
    private static final String CONTENT_THAT_FAILS = "invalid command./";
    private static final String NEW_LINE = "\n";

    private ArrayList<String> defaultStyleOfTaskDecription;
    private ArrayList<String> errorStyleOfTaskDescription;

    @Before
    public void setUp() {
        defaultStyleOfTaskDecription = new ArrayList<>(taskDescription.getStyleClass());
        assertFalse("TaskDescription default style classes should not contain error style class.",
                defaultStyleOfTaskDecription.contains(TaskDescription.ERROR_STYLE_CLASS));

        // build style class for error
        errorStyleOfTaskDescription = new ArrayList<>(defaultStyleOfTaskDecription);
        errorStyleOfTaskDescription.add(TaskDescription.ERROR_STYLE_CLASS);
    }

    @Test
    public void taskDescription_commandSucceeds_textClearedAndStyleClassRemainsTheSame() {
        taskDescription.runContent(CONTENT_THAT_SUCCEEDS);

        assertEquals(CliSyntax.EMPTY_STRING, commandBox.getCommandInput());
        assertEquals(defaultStyleOfTaskDecription, taskDescription.getStyleClass());
    }

    @Test
    public void taskDescription_commandFails_textStaysAndErrorStyleClassAdded() {

        commandBox.clickOnTextField();
        taskDescription.runContent(CONTENT_THAT_FAILS);
        assertEquals(CONTENT_THAT_FAILS + NEW_LINE, taskDescription.getContentInput());
        assertEquals(errorStyleOfTaskDescription, taskDescription.getStyleClass());
    }

    @Test
    public void taskDescription_commandSucceedsAfterFailedCommand_textClearedAndErrorStyleClassRemoved() {
        // add error style to simulate a failed command
        taskDescription.getStyleClass().add(TaskDescription.ERROR_STYLE_CLASS);
        taskDescription.runContent(CONTENT_THAT_SUCCEEDS);
        assertEquals(CONTENT_THAT_SUCCEEDS, taskDescription.getContentInput());
        assertEquals(defaultStyleOfTaskDecription, taskDescription.getStyleClass());
    }

}
