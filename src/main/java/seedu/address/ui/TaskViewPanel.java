package seedu.address.ui;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.TaskManagerChangedEvent;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.commons.events.ui.TaskPanelSelectionChangedEvent;
import seedu.address.logic.Logic;
import seedu.address.model.task.Task;

/**
 * An UI component that displays all information of a {@code Task}.
 */
public class TaskViewPanel extends UiPart<Region> {
    private static final String FXML = "TaskViewPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(TaskViewPanel.class);

    private Logic logic;

    private Optional<Task> displayedTask;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label dueDate;
    @FXML
    private Label remainingTime;
    @FXML
    private Label description;
    @FXML
    private Label priorityValue;
    @FXML
    private Label status;
    @FXML
    private Label dependencies;
    @FXML
    private FlowPane tags;
    @FXML
    private Label earliestTimeOfChildren;

    public TaskViewPanel(Logic logic) {
        super(FXML);
        this.logic = logic;
        this.displayedTask = logic.getFilteredTaskList().stream().findFirst();
        displayTask();
        registerAsAnEventHandler(this);
    }

    /**
     * Updates the display to show the information regarding the task.
     */
    private void displayTask() {
        if (displayedTask.isPresent()) {
            Task task = displayedTask.get();
            name.setText(task.getName().fullName);
            dueDate.setText(task.getDueDate().value);
            remainingTime.setText(getRemainingTime(task));
            earliestTimeOfChildren.setText(getChildTime());
            description.setText(task.getDescription().value);
            priorityValue.setText(task.getPriorityValue().value);
            status.setText(task.getStatus().toString());
            dependencies.setText(getDependencies(task));
            tags.getChildren().setAll(task
                    .getLabels()
                    .stream()
                    .map(t -> new Label(t.labelName))
                    .collect(Collectors.toList()));
        }
    }

    /**
     * Clears the display on the TaskView to show blank entries for Task information.
     */
    private void clearDisplay() {
        name.setText("");
        dueDate.setText("");
        remainingTime.setText("");
        earliestTimeOfChildren.setText("");
        description.setText("");
        priorityValue.setText("");
        status.setText("");
        dependencies.setText("");
        tags.getChildren().removeAll(tags.getChildren());
    }

    private String getDependencies(Task task) {
        ObservableList<Task> tasks = logic.getFilteredTaskList();
        List<String> names = task
                .getDependency()
                .getHashes()
                .stream()
                .map(hash -> tasks.stream().filter(t -> String.valueOf(t.hashCode()).equals(hash)).findFirst().get())
                .map(t -> t.getName().toString())
                .map(str -> str.length() > 20 ? str.substring(0, 12) + "..." : str)
                .collect(Collectors.toList());
        return String.join(", ", names);
    }

    private String getRemainingTime(Task task) {
        return task.getTimeToDueDate();
    }

    private String getChildTime() {
        try {
            return logic.getTaskManager().getEarliestDependentTimeForNode(this.displayedTask.get()).value;
        } catch (Exception e) {
            return "";
        }
    }


    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TaskViewPanel)) {
            return false;
        }

        // state check
        TaskViewPanel panel = (TaskViewPanel) other;
        return displayedTask.map(t -> t.hashCode()).equals(panel.displayedTask.map(t -> t.hashCode()));
    }

    @Subscribe
    private void handleTaskPanelSelectionChangedEvent(TaskPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        displayedTask = Optional.of(event.getNewSelection());
        displayTask();
    }

    @Subscribe
    public void handleTaskManagerChangedEvent(TaskManagerChangedEvent tmce) {
        clearDisplay();
        try {
            earliestTimeOfChildren.setText(tmce.data.getEarliestDependentTimeForNode(this.displayedTask.get()).value);
        } catch (Exception e) {
            return;
        }
    }

    @Subscribe
    public void handleNewResultEvent(NewResultAvailableEvent abce) {
        if (displayedTask.isPresent()) {
            Platform.runLater(() -> remainingTime.setText(getRemainingTime(displayedTask.get())));
        }
    }
}
