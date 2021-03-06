//@@author A0135753A
package guitests;

import static org.junit.Assert.assertTrue;
import static seedu.mypotato.logic.commands.MarkAsUndoneCommand.MESSAGE_UNMARK_TASK_SUCCESS;

import org.junit.Test;

import seedu.mypotato.testutil.TestTask;

public class MarkAsUndoneCommandTest extends TaskManagerGuiTest {

    @Test
    public void unmark() {


        //mark the first task as done
        TestTask[] currentList = td.getTypicalTasks();
        int targetIndex = 1;
        assertUnmarkSuccess(targetIndex, currentList);

        //mark the last task as done
        targetIndex = currentList.length;
        assertUnmarkSuccess(targetIndex, currentList);

        //mark the middle task of the list as done
        targetIndex = currentList.length / 2;
        assertUnmarkSuccess(targetIndex, currentList);

        //invalid index
        commandBox.runCommand("unmark " + currentList.length + 1);
        assertResultMessage("The task index provided is invalid");

    }
    /**
     * Runs the mark as undone command to unmark the task at specified index and confirms the result is correct.
     * @param targetIndexOneIndexed e.g. index 1 to mark the first task in the list,
     * @param currentList A copy of the current list of tasks (before unmark).
     */
    private void assertUnmarkSuccess(int targetIndexOneIndexed, final TestTask[] currentList) {
        TestTask taskToUnmark = currentList[targetIndexOneIndexed - 1]; // -1 as array uses zero indexing
        boolean expectedStatus = false;
        taskToUnmark.getStatus().setStatus(false);

        commandBox.runCommand("unmark " + targetIndexOneIndexed);

        //confirm the list now contains all previous tasks except the deleted task
        assertTrue(taskToUnmark.getStatus().getStatus() == expectedStatus);

        //confirm the result message is correct
        assertResultMessage(String.format(MESSAGE_UNMARK_TASK_SUCCESS, taskToUnmark));
    }

}
