package seedu.address.model.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Before;
import org.junit.Test;

import seedu.address.model.task.Status;
import seedu.address.model.task.Task;
import seedu.address.testutil.TaskBuilder;

// @@author chikchengyao
public class GameManagerTest {

    private GameManager gm;

    static class GameModeStub extends GameMode {

        @Override
        public int appraiseXpChange(Task taskFrom, Task taskTo) {
            return 50;
        }

        @Override
        public GameMode copy() {
            return null;
        }

        @Override
        public int getPeriod() {
            return 0;
        }

        @Override
        public int getLowXp() {
            return 0;
        }

        @Override
        public int getHighXp() {
            return 0;
        }

        @Override
        public String getDescription() {
            return "";
        }

        @Override
        public String getName() {
            throw new AssertionError("This method should not be called.");

        }
    }

    @Before
    public void setUp() {
        gm = new GameManager(new GameModeStub());
    }

    @Test
    public void constructor_called_noException() {
        GameManager gm1 = new GameManager();
        GameManager gm2 = new GameManager(new GameModeStub());
    }

    @Test
    public void null_appraiseXpChange() {

        Task task = new TaskBuilder().build();

        assertThrows(NullPointerException.class, () -> gm.appraiseXpChange(null, null));
        assertThrows(NullPointerException.class, () -> gm.appraiseXpChange(task, null));
        assertThrows(NullPointerException.class, () -> gm.appraiseXpChange(null, task));
    }

    @Test
    public void appraiseXpChange() {
        // check that method appraiseTaskXp is callable
        // checking of results is deferred to specific game modes
        Task inProgressTask = new TaskBuilder().withStatus(Status.IN_PROGRESS).build();
        Task completedTask = new TaskBuilder().withStatus(Status.COMPLETED).build();

        assertEquals(50, gm.appraiseXpChange(inProgressTask, completedTask));
    }
}
