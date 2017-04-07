package seedu.address.model;

import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.events.model.ReadFirstTaskEvent;
import seedu.address.commons.events.storage.ChangedFileLocationRequestEvent;
import seedu.address.commons.events.ui.LoadFirstTaskEvent;
import seedu.address.commons.events.ui.UpdateStatusBarFooterEvent;
import seedu.address.commons.events.ui.UpdateUiTaskDescriptionEvent;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.tag.UniqueTagList.DuplicateTagException;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;
import seedu.address.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.address.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Represents the in-memory model of the address book data. All changes to any
 * model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook taskManager;
    private final FilteredList<ReadOnlyTask> filteredTasks;

    private String currentList;

    private final Stack<String> stackOfUndo;
    private final Stack<ReadOnlyTask> stackOfDeletedTasksAdd;
    private final Stack<ReadOnlyTask> stackOfDeletedTasks;
    private final Stack<Integer> stackOfDeletedTaskIndex;
    private final Stack<ReadOnlyTask> stackOfOldTask;
    private final Stack<ReadOnlyTask> stackOfCurrentTask;
    private final Stack<ReadOnlyTask> stackOfOldNextTask;
    private final Stack<ReadOnlyTask> stackOfNewNextTask;
    private final Stack<ReadOnlyAddressBook> stackOfAddressBook;

    private Config config;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     *
     * @throws DuplicateTaskException
     * @throws DuplicateTagException
     */
    public ModelManager(ReadOnlyAddressBook addressBook, UserPrefs userPrefs, Config config)
            throws DuplicateTagException, DuplicateTaskException {
        super();
        assert !CollectionUtil.isAnyNull(addressBook, userPrefs);

        stackOfUndo = new Stack<>();
        stackOfDeletedTasksAdd = new Stack<>();
        stackOfDeletedTasks = new Stack<>();
        stackOfDeletedTaskIndex = new Stack<>();
        stackOfAddressBook = new Stack<>();
        stackOfOldTask = new Stack<>();
        stackOfCurrentTask = new Stack<>();
        stackOfOldNextTask = new Stack<>();
        stackOfNewNextTask = new Stack<>();
        this.config = config;

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.taskManager = new AddressBook(addressBook);
        filteredTasks = new FilteredList<>(this.taskManager.getTaskList());
        this.currentList = "all";
    }

    public ModelManager() throws DuplicateTagException, DuplicateTaskException {
        this(new AddressBook(), new UserPrefs(), new Config());
    }

    //@@author A0125221Y
    @Override
    public void resetData(ReadOnlyAddressBook newData) {
        stackOfAddressBook.push(new AddressBook(taskManager));
        taskManager.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void revertData() {
        resetData(this.stackOfAddressBook.pop());
        // AddressBook.revertEmptyAddressBook(stackOfAddressBook.pop());
        indicateAddressBookChanged();
    }
    //@@author

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return taskManager;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new AddressBookChangedEvent(taskManager));
    }

    //@@author A0135807A
    /** Raises events to update the file location in storage and status bar in UI */
    @Override
    public void updateFileLocation() {
        raise(new ChangedFileLocationRequestEvent(config));
        raise(new UpdateStatusBarFooterEvent());
        indicateAddressBookChanged();
    }

    /** Raises event to update the Ui TaskDescription when task is edited. */
    public void updateUiTaskDescription(ReadOnlyTask editedtask) {
        raise(new UpdateUiTaskDescriptionEvent(editedtask));
    }
    //@@author

    //@@author A0125221Y
    @Override
    public synchronized int deleteTask(ReadOnlyTask target) throws TaskNotFoundException {
        int indexRemoved = taskManager.removeTask(target);
        indicateAddressBookChanged();
        return indexRemoved;
    }
    //@@author

    @Override
    public synchronized void addTask(Task task) throws UniqueTaskList.DuplicateTaskException {
        taskManager.addTask(task);
        updateFilteredListBasedOnTab();
        indicateAddressBookChanged();
    }

    @Override
    public void updateTask(int filteredTaskListIndex, ReadOnlyTask editedTask)
            throws UniqueTaskList.DuplicateTaskException {
        assert editedTask != null;

        int taskIndex = filteredTasks.getSourceIndex(filteredTaskListIndex);
        taskManager.updateTask(taskIndex, editedTask);
        updateUiTaskDescription(editedTask);
        updateFilteredListBasedOnTab();
        indicateAddressBookChanged();
    }

    //@@author A0125221Y
    @Override
    public synchronized void updateTask(ReadOnlyTask old, Task toUpdate)
            throws TaskNotFoundException, DuplicateTaskException {

        taskManager.updateTask(old, toUpdate);
        updateUiTaskDescription(toUpdate);
        indicateAddressBookChanged();
    }

    //@@author A0125221Y
    @Override
    public Stack<String> getUndoStack() {
        return stackOfUndo;
    }

    @Override
    public Stack<ReadOnlyTask> getDeletedStackOfTasksAdd() {
        return stackOfDeletedTasksAdd;
    }

    @Override
    public Stack<ReadOnlyTask> getDeletedStackOfTasks() {
        return stackOfDeletedTasks;
    }

    @Override
    public Stack<Integer> getDeletedStackOfTasksIndex() {
        return stackOfDeletedTaskIndex;
    }

    @Override
    public Stack<ReadOnlyTask> getOldTask() {
        return stackOfOldTask;
    }

    @Override
    public Stack<ReadOnlyTask> getCurrentTask() {
        return stackOfCurrentTask;
    }

    @Override
    public Stack<ReadOnlyTask> getOldNextTask() {
        return stackOfOldNextTask;
    }

    @Override
    public Stack<ReadOnlyTask> getNewNextTask() {
        return stackOfNewNextTask;
    }
    //@@author

    @Override
    public void setCurrentList(String currentList) {
        this.currentList = currentList;
    }

    // =========== Filtered Person List Accessors
    // =============================================================
    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList() {
        // here to change the list order according to the date comparator.
        return new UnmodifiableObservableList<>(filteredTasks);
        // return new UnmodifiableObservableList<>(filteredTasks.sorted(new
        // DateComparator()));
    }

    @Override
    public void updateFilteredListBasedOnTab() {
        if (currentList.equalsIgnoreCase("today")) {
            updateFilteredListToShowToday();
        } else if (currentList.equalsIgnoreCase("completed")) {
            updateFilteredListToShowCompleted();
        } else {
            updateFilteredListToShowAll();
        }
    }

    @Override
    public void updateFilteredListToShowAll() {
        ListFilter.filterAll(filteredTasks);
    }

    @Override
    public void updateFilteredListToShowToday() {
        ListFilter.filterToday(filteredTasks);
    }

    @Override
    public void updateFilteredTaskList(boolean isInContent, Set<String> keywords) {
        ListFilter.filterKeywords(filteredTasks, isInContent, keywords);
    }

    @Override
    public void updateFilteredListToShowCompleted() {
        ListFilter.filterCompleted(filteredTasks);
    }

    //@@author A0135753A
    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getDoneTaskList() {
        FilteredList<ReadOnlyTask> newList = filteredTasks;
        for (ReadOnlyTask task : newList) {
            if (!task.getStatus().status) {
                newList.remove(task);
            }
        }
        return new UnmodifiableObservableList<>(newList);
    }

    // =========== Events Handling
    // =============================================================

    //@@author A0135807A
    @Override
    @Subscribe
    public void handleLoadFirstTaskEvent(LoadFirstTaskEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Initializing task"));
        if (taskManager.getTaskList().size() > 0) {
            raise(new ReadFirstTaskEvent(taskManager.getTaskList().get(0)));
        }
    }
}
