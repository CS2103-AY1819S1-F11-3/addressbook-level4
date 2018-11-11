package seedu.address.model.task;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.Set;


/**
 * Represents all of a Task's dependencies to other tasks in the task manager.
 * Hashes contained in the hashes field are hash codes of tasks that a Task is dependent on.
 *
 * When "this task" is used in the comments below, it refers to the task that has this dependencies object.
 * This task is the dependent task, and the tasks that it is dependent on can be referred to as the dependee tasks.
 * Guarantees: immutable;
 */
public class Dependencies {

    //Prevents reassignment
    private final Set<String> hashes;

    /**
     * Constructs a {@code Dependencies}.
     *
     * @param hashes A list of hashes of task dependencies.
     */
    public Dependencies(Set<String> hashes) {
        this.hashes = new HashSet<>(hashes);
    }

    /**
     * Constructs an empty dependencies object
     */
    public Dependencies() {
        this.hashes = new HashSet<>();
    }

    /**
     * Adds a task that this task is dependent on.
     * @param task
     * @return new dependencies object with the additional dependee task's hashcode
     */
    public Dependencies addDependency(Task task) {
        Set<String> newValue = new HashSet<>(hashes);
        newValue.add(Integer.toString(task.hashCode()));
        return new Dependencies(newValue);
    }

    /**
     * Removes a dependency to a task
     * @param task task to remove dependency to
     * @return new dependencies object without hashcode of given task
     */
    public Dependencies removeDependency(Task task) {
        Set<String> newValue = new HashSet<>(hashes);
        newValue.remove(Integer.toString(task.hashCode()));
        return new Dependencies(newValue);
    }

    /**
     * Checks if this task is dependent on given task
     * @param task given task to check
     * @return result whether task is contained within current set of dependencies
     */
    public boolean containsDependency(Task task) {
        return hashes.contains(Integer.toString(task.hashCode()));
    }

    /**
     * Returns the hashes of all the tasks conatined within the dependencies object
     * @return set of all hashes
     */
    public Set<String> getHashes() {
        return hashes;
    }

    /**
     * Returns a new dependencies object with the old hash updated to new hash
     * @return set of all hashes
     */
    public Dependencies updateHash(String oldHash, String newHash) {
        requireNonNull(oldHash);
        requireNonNull(newHash);
        Set<String> newValue = new HashSet<>(hashes);
        newValue.remove(oldHash);
        newValue.add(newHash);
        return new Dependencies(newValue);
    }

    /**
     * Returns the number of dependencies stored.
     *
     * @return the number of dependencies in this Dependencies object
     */
    public Integer getDependencyCount() {
        return hashes.size();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String hash: hashes) {
            builder.append(hash);
            builder.append(" ");
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Dependencies // instanceof handles nulls
                && hashes.equals(((Dependencies) other).hashes)); // state check
    }
    @Override
    public int hashCode() {
        return hashes.hashCode();
    }

}
