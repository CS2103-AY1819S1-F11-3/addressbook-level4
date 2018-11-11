package seedu.address.logic.commands;

import static junit.framework.TestCase.assertEquals;
import static seedu.address.commons.core.Messages.MESSAGE_TASKS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showTaskAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_TASK;
import static seedu.address.testutil.TypicalTasks.J_TASK;
import static seedu.address.testutil.TypicalTasks.K_TASK;
import static seedu.address.testutil.TypicalTasks.getTypicalTaskManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.task.Status;
import seedu.address.model.task.Task;
import seedu.address.testutil.TaskBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalTaskManager(), new UserPrefs());
        expectedModel = new ModelManager(model.getTaskManager(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListCommand(), model, commandHistory, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showTaskAtIndex(model, INDEX_FIRST_TASK);
        assertCommandSuccess(new ListCommand(), model, commandHistory, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listFiltered_beforeToday() {
        String expectedMessage = String.format(MESSAGE_TASKS_LISTED_OVERVIEW, 1);
        ListCommand.ListFilter filter = ListCommand.ListFilter.DUE_TODAY;
        ListCommand command = new ListCommand(filter);

        model.clearTaskData();
        model.addTask(J_TASK);
        model.addTask(K_TASK);

        expectedModel.clearTaskData();
        expectedModel.addTask(J_TASK);
        expectedModel.addTask(K_TASK);
        expectedModel.updateFilteredTaskList(t -> !t.equals(K_TASK));

        Assert.assertNotEquals(model, expectedModel);
        assertCommandSuccess(command, model, commandHistory,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_listFiltered_beforeEndOfWeek() {
        String expectedMessage = String.format(MESSAGE_TASKS_LISTED_OVERVIEW, 1);
        ListCommand.ListFilter filter = ListCommand.ListFilter.DUE_END_OF_WEEK;
        ListCommand command = new ListCommand(filter);

        // Get a string representation for a time almost at the end of the week.
        Calendar c = Calendar.getInstance();
        c = new Calendar.Builder()
                .setWeekDate(c.get(Calendar.YEAR), c.get(Calendar.WEEK_OF_YEAR), Calendar.SUNDAY)
                .setTimeOfDay(23, 59, 59)
                .build();
        Date date = c.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY HHmm");
        String strDate = formatter.format(date);

        // Build a task with the due date as the end of the week
        Task taskDueThisWeek = new TaskBuilder()
                .withName("Finish my homework due this week")
                .withDueDate(strDate)
                .build();

        model.clearTaskData();
        model.addTask(taskDueThisWeek);
        model.addTask(K_TASK);

        expectedModel.clearTaskData();
        expectedModel.addTask(taskDueThisWeek);
        expectedModel.addTask(K_TASK);
        expectedModel.updateFilteredTaskList(t -> !t.equals(K_TASK));

        Assert.assertNotEquals(model, expectedModel);
        assertCommandSuccess(command, model, commandHistory,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_listFiltered_beforeEndOfMonth() {
        String expectedMessage = String.format(MESSAGE_TASKS_LISTED_OVERVIEW, 1);
        ListCommand.ListFilter filter = ListCommand.ListFilter.DUE_END_OF_MONTH;
        ListCommand command = new ListCommand(filter);

        // Get a string representation for a time almost at the end of the month.
        Calendar c = Calendar.getInstance();
        c = new Calendar.Builder()
                .set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH))
                .setTimeOfDay(23, 59, 59)
                .build();
        Date date = c.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY HHmm");
        String strDate = formatter.format(date);

        // Build a task with the due date as the end of the month
        Task taskDueThisMonth = new TaskBuilder()
                .withName("Finish my homework due this month")
                .withDueDate(strDate)
                .build();

        model.clearTaskData();
        model.addTask(taskDueThisMonth);
        model.addTask(K_TASK);

        expectedModel.clearTaskData();
        expectedModel.addTask(taskDueThisMonth);
        expectedModel.addTask(K_TASK);
        expectedModel.updateFilteredTaskList(t -> !t.equals(K_TASK));

        Assert.assertNotEquals(model, expectedModel);
        assertCommandSuccess(command, model, commandHistory,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_listFiltered_nonBlockedNoDepenencies() {
        String expectedMessage = String.format(MESSAGE_TASKS_LISTED_OVERVIEW, 6);
        ListCommand.ListFilter filter = ListCommand.ListFilter.NOT_BLOCKED;
        ListCommand command = new ListCommand(filter);

        // Add dependency model
        Task dependantTask = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        Task dependeeTask = model.getFilteredTaskList().get(INDEX_SECOND_TASK.getZeroBased());
        Task newTask = DependencyCommand.createDependantTask(dependantTask, dependeeTask);
        model.updateTask(dependantTask, newTask);

        // Build expected model
        expectedModel.updateFilteredTaskList(x -> !x.equals(dependantTask));

        Assert.assertNotEquals(model.getFilteredTaskList(), expectedModel.getFilteredTaskList());
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(model.getFilteredTaskList(), expectedModel.getFilteredTaskList());
    }

    @Test
    public void execute_listFiltered_nonBlockedCompletedDependencies() {
        ListCommand.ListFilter filter = ListCommand.ListFilter.NOT_BLOCKED;
        ListCommand command = new ListCommand(filter);

        // Add dependency model
        Task dependantTask = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        Task dependeeTask = model.getFilteredTaskList().get(INDEX_SECOND_TASK.getZeroBased());

        // Update task to Completed in both model and expectedModel
        Task expectedModelDependeeTask = model.getFilteredTaskList().get(INDEX_SECOND_TASK.getZeroBased());
        Task completedDependeeTask = new TaskBuilder(dependeeTask).withStatus(Status.COMPLETED).build();
        model.updateTask(dependeeTask, completedDependeeTask);
        expectedModel.updateTask(expectedModelDependeeTask, completedDependeeTask);

        // Create dependency to the completed task
        Task newTask = DependencyCommand.createDependantTask(dependantTask, completedDependeeTask);
        model.updateTask(dependantTask, newTask);

        // Dependant is shown because it's dependee is completed
        assertCommandSuccess(command, model, commandHistory, ListCommand.MESSAGE_SUCCESS, expectedModel);
        assertEquals(model.getFilteredTaskList(), expectedModel.getFilteredTaskList());
    }
}
