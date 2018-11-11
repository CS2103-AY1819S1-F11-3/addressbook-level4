package seedu.address.model.task;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import seedu.address.model.tag.Label;

/**
 * Represents a Task in the TaskManager.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Task {

    // Identity fields
    private final Name name;
    private final DueDate dueDate;
    private final PriorityValue priorityValue;
    private final Status status;

    // Data fields
    private final Description description;
    private final Set<Label> labels = new HashSet<>();
    private final Dependencies dependency;

    /**
     * Every field must be present and not null.
     * Status of a new task is initialized to in progress unless specified.
     */
    public Task(Name name, DueDate dueDate, PriorityValue priorityValue, Description description, Set<Label> labels) {
        requireAllNonNull(name, dueDate, priorityValue, description, labels);
        this.name = name;
        this.dueDate = dueDate;
        this.priorityValue = priorityValue;
        this.description = description;
        this.labels.addAll(labels);
        this.status = Status.IN_PROGRESS;
        this.dependency = new Dependencies();
    }

    public Task(Name name, DueDate dueDate, PriorityValue priorityValue, Description description, Set<Label> labels,
                Status status, Dependencies dependency) {
        requireAllNonNull(name, dueDate, priorityValue, description, labels, status, dependency);
        this.name = name;
        this.dueDate = dueDate;
        this.priorityValue = priorityValue;
        this.description = description;
        this.labels.addAll(labels);
        this.status = status;
        this.dependency = dependency;
    }

    public Name getName() {
        return name;
    }

    public DueDate getDueDate() {
        return dueDate;
    }

    public PriorityValue getPriorityValue() {
        return priorityValue;
    }

    public Description getDescription() {
        return description;
    }

    /**
     * Returns an immutable label set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Label> getLabels() {
        return Collections.unmodifiableSet(labels);
    }

    public Status getStatus() {
        return this.status;
    }

    public Dependencies getDependency() {
        return this.dependency;
    }

    public boolean isStatusCompleted() {
        return this.status == Status.COMPLETED;
    }

    public boolean isStatusOverdue() {
        return this.status == Status.OVERDUE;
    }

    public boolean isStatusInProgress() {
        return this.status == Status.IN_PROGRESS;
    }

    public boolean isOverdue() {
        return this.dueDate.isOverdue();
    }

    /**
     * Returns a human-readable information about time until due date of task.
     * If task is {@code Status.OVERDUE} or {@code Status.COMPLETED}, a dummy value will be returned.
     */
    public String getTimeToDueDate() {
        if (this.status != Status.IN_PROGRESS || this.isOverdue()) {
            return "------";
        }
        long timeMilliseconds = this.dueDate.timeToDueDate();
        return millisecondsToString(timeMilliseconds);
    }

    /**
     * Converts a given millisecond duration to a human readable duration of type string
     * @param timeMilliseconds
     * @return
     */
    private String millisecondsToString(long timeMilliseconds) {
        //Declaring constants to be used.
        long secondInMs = 1000;
        long minuteInMs = secondInMs * 60;
        long hourInMs = minuteInMs * 60;
        long dayInMs = hourInMs * 24;

        long days = TimeUnit.MILLISECONDS.toDays(timeMilliseconds);
        timeMilliseconds -= days * dayInMs;
        long hours = TimeUnit.MILLISECONDS.toHours(timeMilliseconds);
        timeMilliseconds -= hours * hourInMs;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeMilliseconds);
        timeMilliseconds -= minutes * minuteInMs;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeMilliseconds);
        return String.format("%d day(s) %d hour(s) %d minute(s) %d second(s)", days, hours, minutes, seconds);
    }

    /**
     * Returns a new Task object with the specified dependee task removed [Non-mutating]
     */
    public Task spliceDependency(Task dependeeTask) {
        return new Task(
                this.name,
                this.dueDate,
                this.priorityValue,
                this.description,
                this.labels,
                this.status,
                this.dependency.spliceDependency(dependeeTask)
        );
    }

    /**
     * Returns a new Task object with the specified dependee task added [Non-mutating]
     */
    public Task concatDependency(Task dependeeTask) {
        return new Task(
                this.name,
                this.dueDate,
                this.priorityValue,
                this.description,
                this.labels,
                this.status,
                this.dependency.concatDependency(dependeeTask)
        );
    }

    /**
     * Returns a new Task object with the specified dependency updated [Non-mutating]
     */
    public Task updateHash(String oldHash, String newHash) {
        return new Task(
                this.name,
                this.dueDate,
                this.priorityValue,
                this.description,
                this.labels,
                this.status,
                this.dependency.updateHash(oldHash, newHash)
        );
    }

    /**
     * Returns true if both tasks of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two tasks.
     */
    public boolean isSameTask(Task othertask) {
        if (othertask == this) {
            return true;
        }

        return othertask != null
                && othertask.getName().equals(getName())
                && (othertask.getDueDate().equals(getDueDate())
                || othertask.getPriorityValue().equals(getPriorityValue()));
    }

    public boolean isDependentOn(Task otherTask) {
        return dependency.containsDependency(otherTask);
    }

    /**
     * Returns true if both tasks have the same identity and data fields.
     * This defines a stronger notion of equality between two tasks.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Task)) {
            return false;
        }

        Task otherTask = (Task) other;
        return otherTask.getName().equals(getName())
                && otherTask.getDueDate().equals(getDueDate())
                && otherTask.getPriorityValue().equals(getPriorityValue())
                && otherTask.getDescription().equals(getDescription())
                && otherTask.getLabels().equals(getLabels())
                && otherTask.getStatus().equals(getStatus());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        //status's string is used because status is an Enum, so each enum has a unique hashcode
        return Objects.hash(name, dueDate, priorityValue, description, labels, status.toString());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" DueDate: ")
                .append(getDueDate())
                .append(" PriorityValue: ")
                .append(getPriorityValue())
                .append(" Description: ")
                .append(getDescription())
                .append(" Labels: ");
        getLabels().forEach(builder::append);
        builder.append(" Status: ")
                .append(getStatus());
        return builder.toString();
    }

}
