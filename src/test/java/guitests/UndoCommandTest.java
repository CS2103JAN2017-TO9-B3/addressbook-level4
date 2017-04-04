package guitests;

//@@author A0125221Y
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import guitests.guihandles.TaskCardHandle;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.EditCommand;
import seedu.address.testutil.TaskBuilder;
import seedu.address.testutil.TestTask;

public class UndoCommandTest extends AddressBookGuiTest{

	@Test
	public void undoWithOneModification_Success(){

		TestTask[] currentList = td.getTypicalTasks();

		//Begin by adding one task
		TestTask taskToAdd = td.hoon;
		commandBox.runCommand(taskToAdd.getAddCommand());
		assertListSize(currentList.length + 1);

		//Next undo the previous add command
		commandBox.runCommand("undo");
		assertListSize(currentList.length);
		assertTrue(taskListPanel.isListMatching(currentList));

		/*
		//Now test deletion of one single task
		commandBox.runCommand("delete 1");
		assertListSize(currentList.length - 1);
		currentList = TestUtil.removeTaskFromList(currentList, 1);

		//Try to undo the previous delete command
		commandBox.runCommand("undo");
		currentList = TestUtil.addTasksToListAtIndex(currentList, 0, td.alice);
		assertListSize(currentList.length);
		assertTrue(taskListPanel.isListMatching(currentList));
		*/
	}

	@Test
	public void undo_clear_success() {
		TestTask[] allTaskList= td.getTypicalTasks();

		//clear all tasks
		commandBox.runCommand("clear");
		assertListSize(0);

		//undo
		commandBox.runCommand("undo");
		assertTrue(taskListPanel.isListMatching(allTaskList));
	}

	@Test
	public void undo_edit_success() throws IllegalValueException {
		TestTask[] oldTaskList = td.getTypicalTasks();

		//edit a task
        String detailsToEdit = "start/9 Nov 2010 12pm";
        int addressBookIndex = 2;

        TestTask taskToEdit = oldTaskList[addressBookIndex - 1];
        TestTask editedTask = new TaskBuilder(taskToEdit).withStartDateTime("9 Nov 2010 12pm").build();

        assertEditSuccess(addressBookIndex, addressBookIndex, detailsToEdit, editedTask);

		//undo
		commandBox.runCommand("undo");
		assertTrue(taskListPanel.isListMatching(oldTaskList));

	}

	private void assertEditSuccess(int filteredTaskListIndex, int addressBookIndex,
			String detailsToEdit, TestTask editedTask) {

		TestTask[] oldTaskList = td.getTypicalTasks();
		commandBox.runCommand("edit " + filteredTaskListIndex + " " + detailsToEdit);

		// confirm the new card contains the right data
		TaskCardHandle editedCard = taskListPanel.navigateToTask(editedTask.getTitle().fullTitle);
		assertMatching(editedTask, editedCard);

		// confirm the list now contains all previous tasks plus the task with updated details
		oldTaskList[addressBookIndex - 1] = editedTask;
		assertTrue(taskListPanel.isListMatching(oldTaskList));
		assertResultMessage(String.format(EditCommand.MESSAGE_EDIT_TASK_SUCCESS, editedTask));
	}

}
