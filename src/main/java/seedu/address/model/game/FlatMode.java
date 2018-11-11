package seedu.address.model.game;

import seedu.address.model.task.Task;

// @@author chikchengyao

/**
 * A game mode where all completed tasks give the same XP.
 */
public class FlatMode extends GameMode {
    private int overdueXp;
    private int completedXp;

    public FlatMode() {
        this(30, 60);
    }

    /**
     * Sets the game mode to award {@code completedXp} XP for tasks completed on time, and
     * to award {@code overdueXp} for overdue tasks completed.
     *
     * @param overdueXp The XP to award to overdue tasks.
     * @param completedXp The XP to award to tasks completed on time.
     */
    public FlatMode(int overdueXp, int completedXp) {
        this.overdueXp = overdueXp;
        this.completedXp = completedXp;
    }

    public FlatMode(int period, int overdueXp, int completedXp) {
        this(overdueXp, completedXp);
    }

    @Override
    public int appraiseXpChange(Task taskFrom, Task taskTo) {

        // Check that the two tasks are the same, but with different statuses
        checkValidTasks(taskFrom, taskTo);

        // if a task is moving between inProgress to Overdue, award no XP
        if (taskFrom.isStatusInProgress() && taskTo.isStatusOverdue()) {
            return 0;
        }

        // Else award points
        if (taskFrom.isStatusInProgress() && taskTo.isStatusCompleted()) {
            return completedXp;
        }

        // At this point, then taskFrom.isStatusOverdue() && taskTo.isCompleted() is True
        return overdueXp;

    }

    @Override
    public FlatMode copy() {
        return new FlatMode(overdueXp, completedXp);
    }

    @Override
    public int getPeriod() {
        // Priority mode does not use period, so an arbitrary value is returned.
        return -1;
    }

    @Override
    public int getLowXp() {
        return overdueXp;
    }

    @Override
    public int getHighXp() {
        return completedXp;
    }
}
