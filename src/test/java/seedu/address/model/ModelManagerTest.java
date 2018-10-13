package seedu.address.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_TASKS;
import static seedu.address.testutil.TypicalTasks.A_TASK;
import static seedu.address.testutil.TypicalTasks.B_TASK;

import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.task.NameContainsKeywordsPredicate;
import seedu.address.testutil.TaskManagerBuilder;

public class ModelManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ModelManager modelManager = new ModelManager();

    @Test
    public void hasTask_nullTask_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.hasTask(null);
    }

    @Test
    public void hasTask_taskNotInTaskManager_returnsFalse() {
        assertFalse(modelManager.hasTask(A_TASK));
    }

    @Test
    public void hasTask_taskInTaskManager_returnsTrue() {
        modelManager.addTask(A_TASK);
        assertTrue(modelManager.hasTask(A_TASK));
    }

    @Test
    public void getFilteredTaskList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredTaskList().remove(0);
    }

    @Test
    public void equals() {
        TaskManager taskManager = new TaskManagerBuilder().withTask(A_TASK).withTask(B_TASK).build();
        TaskManager differentTaskManager = new TaskManager();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(taskManager, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(taskManager, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different taskManager -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentTaskManager, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = A_TASK.getName().fullName.split("\\s+");
        modelManager.updateFilteredTaskList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(taskManager, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);

        // different userPrefs -> returns true
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setTaskManagerFilePath(Paths.get("differentFilePath"));
        assertTrue(modelManager.equals(new ModelManager(taskManager, differentUserPrefs)));
    }
}
