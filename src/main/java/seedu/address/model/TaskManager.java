package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import javafx.collections.ObservableList;
import seedu.address.model.achievement.AchievementRecord;
import seedu.address.model.achievement.Level;
import seedu.address.model.game.GameManager;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;

/**
 * Wraps all data at the task-manager level
 * Duplicates are not allowed (by .isSameTask comparison)
 */
public class TaskManager implements ReadOnlyTaskManager {

    private final UniqueTaskList tasks;
    private final AchievementRecord achievements;
    private final GameManager gameManager;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        tasks = new UniqueTaskList();
        achievements = new AchievementRecord();
        gameManager = new GameManager();
    }

    public TaskManager() {
    }

    /**
     * Creates an TaskManager using the Tasks in the {@code toBeCopied}
     */
    public TaskManager(ReadOnlyTaskManager toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list and achievement overwrite operations

    /**
     * Replaces the contents of the task list with {@code tasks}.
     * {@code tasks} must not contain duplicate tasks.
     */
    public void setTasks(List<Task> tasks) {
        this.tasks.setTasks(tasks);
    }

    /**
     * Replaces the contents of the task list with {@code tasks}.
     * {@code tasks} must not contain duplicate tasks.
     */
    public void setAchievements(AchievementRecord achievements) {
        this.achievements.resetData(achievements);
    }

    /**
     * Resets the existing data of this {@code TaskManager} with {@code newData}.
     */
    public void resetData(ReadOnlyTaskManager newData) {
        requireNonNull(newData);

        setTasks(newData.getTaskList());
        setAchievements(newData.getAchievementRecord());
    }

    //// task-level operations

    /**
     * Returns true if a task with the same identity as {@code task} exists in the task manager.
     */
    public boolean hasTask(Task task) {
        requireNonNull(task);
        return tasks.contains(task);
    }

    /**
     * Adds a task to the task manager.
     * The task must not already exist in the task manager.
     */
    public void addTask(Task p) {
        tasks.add(p);
    }

    /**
     * Replaces the given task {@code target} in the list with {@code editedTask}.
     * {@code target} must exist in the task manager.
     * The task identity of {@code editedTask} must not be the same as another existing task in the task manager.
     */
    public void updateTask(Task target, Task editedTask) {
        requireNonNull(editedTask);

        tasks.setTask(target, editedTask);

    }

    /**
     * Removes {@code key} from this {@code TaskManager}.
     * {@code key} must exist in the task manager.
     */
    public void removeTask(Task key) {
        tasks.remove(key);
    }

    /**
     * Updates tasks that are overdue
     */
    public void checkOverdue() {
        tasks.updateOverdue();

    }

    //// achievement related operation

    /**
     * @return  the user's current level to the user.
     */
    public Level getLevel() {
        return achievements.getLevel();
    }

    /**
     * @return the {@code int} value representing the Xp.
     */
    public int getXpValue() {
        return achievements.getXpValue();
    }

    /**
     * Updates the Xp in the {@code AchievementRecord} of the {@code TaskManager} with the new xp value.
     */
    public void addXp(Integer xp) {
        requireNonNull(xp);

        achievements.updateXp(xp);
    }

    public int appraiseTaskXp(Task task) {
        return gameManager.appraiseTaskXp(task);
    }

    //// util methods

    @Override
    public String toString() {
        return tasks.asUnmodifiableObservableList().size() + " tasks";
        // TODO: refine later
    }

    @Override
    public ObservableList<Task> getTaskList() {
        return tasks.asUnmodifiableObservableList();
    }

    @Override
    public AchievementRecord getAchievementRecord() {
        AchievementRecord copy = new AchievementRecord();
        copy.resetData(achievements);
        return copy;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskManager // instanceof handles nulls
                && tasks.equals(((TaskManager) other).tasks)
                && achievements.equals(((TaskManager) other).achievements));
    }

    @Override
    public int hashCode() {
        return Objects.hash(tasks, achievements);
    }
}
