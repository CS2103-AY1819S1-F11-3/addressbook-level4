package systemtests;

import static org.junit.Assert.assertFalse;
import static seedu.address.commons.core.Messages.MESSAGE_TASKS_LISTED_OVERVIEW;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.TypicalTasks.B_TASK;
import static seedu.address.testutil.TypicalTasks.C_TASK;
import static seedu.address.testutil.TypicalTasks.D_TASK;
import static seedu.address.testutil.TypicalTasks.KEYWORD_MATCHING_TUTORIAL;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.tag.Label;

public class FindCommandSystemTest extends TaskManagerSystemTest {

    @Test
    public void find() {
        /* Case: find multiple persons in task manager, command with leading spaces and trailing spaces
         * -> 2 persons found
         */
        String command = "   " + FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_TUTORIAL + "   ";
        Model expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, B_TASK, D_TASK); // first names of Benson and Daniel are "Meier"
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: repeat previous find command where task list is displaying the persons we are finding
         * -> 2 persons found
         */
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_TUTORIAL;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find task where task list is not displaying the task we are finding -> 1 task found */
        command = FindCommand.COMMAND_WORD + " medical";
        ModelHelper.setFilteredList(expectedModel, C_TASK);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple persons in task manager, 2 keywords -> 2 persons found */
        command = FindCommand.COMMAND_WORD + " Build Do";
        ModelHelper.setFilteredList(expectedModel, B_TASK, D_TASK);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple persons in task manager, 2 keywords in reversed order -> 2 persons found */
        command = FindCommand.COMMAND_WORD + " Do Build";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple persons in task manager, 2 keywords with 1 repeat -> 2 persons found */
        command = FindCommand.COMMAND_WORD + " Do Build Do";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple persons in task manager, 2 matching keywords and 1 non-matching keyword
         * -> 2 persons found
         */
        command = FindCommand.COMMAND_WORD + " Do Build NonMatchingKeyWord";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: undo previous find command -> rejected */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: redo previous find command -> rejected */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: find same persons in task manager after deleting 1 of them -> 1 task found */
        executeCommand(DeleteCommand.COMMAND_WORD + " 1");
        assertFalse(getModel().getTaskManager().getTaskList().contains(B_TASK));
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_TUTORIAL;
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, D_TASK);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find task in task manager, keyword is same as name but of different case -> 1 task found */
        command = FindCommand.COMMAND_WORD + " tuToRiaL";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find task in task manager, keyword is substring of name -> 0 persons found */
        command = FindCommand.COMMAND_WORD + " tut";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find task in task manager, name is substring of specified keyword -> 0 persons found */
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_TUTORIAL + "s";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find task not in task manager -> 0 persons found */
        command = FindCommand.COMMAND_WORD + " Mark";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find phone number of task in task manager -> 0 persons found */
        command = FindCommand.COMMAND_WORD + " " + D_TASK.getDueDate().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find address of task in task manager -> 0 persons found */
        command = FindCommand.COMMAND_WORD + " " + D_TASK.getDescription().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find email of task in task manager -> 0 persons found */
        command = FindCommand.COMMAND_WORD + " " + D_TASK.getPriorityValue().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find labels of task in task manager -> 0 persons found */
        List<Label> labels = new ArrayList<>(D_TASK.getLabels());
        command = FindCommand.COMMAND_WORD + " " + labels.get(0).labelName;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find while a task is selected -> selected card deselected */
        showAllPersons();
        selectPerson(Index.fromOneBased(1));
        assertFalse(getPersonListPanel().getHandleToSelectedCard().getName().equals(D_TASK.getName().fullName));
        command = FindCommand.COMMAND_WORD + " Do CS2106";
        ModelHelper.setFilteredList(expectedModel, D_TASK);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardDeselected();

        /* Case: find task in empty task manager -> 0 persons found */
        deleteAllPersons();
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_TUTORIAL;
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, D_TASK);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: mixed case command word -> rejected */
        command = "FiNd " + KEYWORD_MATCHING_TUTORIAL;
        assertCommandFailure(command, MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code Messages#MESSAGE_TASKS_LISTED_OVERVIEW} with the number of people in the filtered list,
     * and the model related components equal to {@code expectedModel}.
     * These verifications are done by
     * {@code TaskManagerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the status bar remains unchanged, and the command box has the default style class, and the
     * selected card updated accordingly, depending on {@code cardStatus}.
     *
     * @see TaskManagerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel) {
        String expectedResultMessage = String.format(
                MESSAGE_TASKS_LISTED_OVERVIEW, expectedModel.getFilteredTaskList().size());

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code TaskManagerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command box has the
     * error style.
     *
     * @see TaskManagerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
