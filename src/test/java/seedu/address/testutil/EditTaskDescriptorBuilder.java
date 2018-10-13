package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditCommand.EditTaskDescriptor;
import seedu.address.model.task.Description;
import seedu.address.model.task.DueDate;
import seedu.address.model.task.Name;
import seedu.address.model.task.PriorityValue;
import seedu.address.model.task.Status;
import seedu.address.model.task.Task;
import seedu.address.model.tag.Label;

/**
 * A utility class to help with building EditTaskDescriptor objects.
 */
public class EditTaskDescriptorBuilder {

    private EditTaskDescriptor descriptor;

    public EditTaskDescriptorBuilder() {
        descriptor = new EditTaskDescriptor();
    }

    public EditTaskDescriptorBuilder(EditTaskDescriptor descriptor) {
        this.descriptor = new EditTaskDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditTaskDescriptor} with fields containing {@code task}'s details
     */
    public EditTaskDescriptorBuilder(Task task) {
        descriptor = new EditTaskDescriptor();
        descriptor.setName(task.getName());
        descriptor.setDueDate(task.getDueDate());
        descriptor.setPriorityValue(task.getPriorityValue());
        descriptor.setDescription(task.getDescription());
        descriptor.setLabels(task.getLabels());
    }

    /**
     * Sets the {@code Name} of the {@code EditTaskDescriptor} that we are building.
     */
    public EditTaskDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code DueDate} of the {@code EditTaskDescriptor} that we are building.
     */
    public EditTaskDescriptorBuilder withDueDate(String phone) {
        descriptor.setDueDate(new DueDate(phone));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditTaskDescriptor} that we are building.
     */
    public EditTaskDescriptorBuilder withPriorityValue(String email) {
        descriptor.setPriorityValue(new PriorityValue(email));
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditTaskDescriptor} that we are building.
     */
    public EditTaskDescriptorBuilder withAddress(String address) {
        descriptor.setDescription(new Description(address));
        return this;
    }

    /**
     * Parses the {@code labels} into a {@code Set<Label>} and set it to the {@code EditTaskDescriptor}
     * that we are building.
     */
    public EditTaskDescriptorBuilder withLabels(String... labels) {
        Set<Label> labelSet = Stream.of(labels).map(Label::new).collect(Collectors.toSet());
        descriptor.setLabels(labelSet);
        return this;
    }

    /**
     * Sets the {@code Status} of the {@code EditTaskDescriptor} that we are building.
     */
    public EditTaskDescriptorBuilder withStatus(Status status) {
        descriptor.setStatus(status);
        return this;
    }

    public EditTaskDescriptor build() {
        return descriptor;
    }
}
