package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.game.GameMode;

/**
 * Changes the game mode.
 */
public class ModeCommand extends Command {
    public static final String FLAT_MODE = "flat";
    public static final String DECREASING_MODE = "decreasing";
    public static final String INCREASING_MODE = "increasing";
    public static final String PRIORITY_MODE = "priority";

    public static final String EASY_MODE = "easy";
    public static final String MEDIUM_MODE = "medium";
    public static final String HARD_MODE = "hard";
    public static final String EXTREME_MODE = "extreme";

    public static final String COMMAND_WORD = "mode";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Change the game mode.\n"
            + "Usage: mode [GAME_MODE] [Optional: GAME_DIFFICULTY]\n"
            + "GAME_MODE: 'flat', 'decreasing', 'increasing', or 'priority'.\n"
            + "GAME_DIFFICULTY: 'easy', 'medium', 'hard' or 'extreme'.\n"
            + "Example: " + COMMAND_WORD + " " + DECREASING_MODE + " " + EXTREME_MODE;

    public static final String MESSAGE_MODE_CHANGE_SUCCESS = "Game mode successfully changed!";
    public static final String MESSAGE_CURRENT_MODE = "You are currently using the %s mode! \n%s";

    private final String newGameModeName;
    private final String newGameDifficultyName;

    public ModeCommand() {
        this.newGameModeName = null;
        this.newGameDifficultyName = null;
    }

    public ModeCommand(String newGameModeName) {
        this.newGameModeName = newGameModeName;
        this.newGameDifficultyName = null;
    }

    public ModeCommand(String newGameModeName, String newGameDifficultyName) {
        this.newGameModeName = newGameModeName;
        this.newGameDifficultyName = newGameDifficultyName;
    }

    @Override
    public CommandResult executePrimitive(Model model, CommandHistory history) {
        requireNonNull(model);

        if (newGameModeName == null && newGameDifficultyName == null) {
            GameMode gameMode = model.getGameMode();
            String modeName = gameMode.getName();
            return new CommandResult(String.format(MESSAGE_CURRENT_MODE, modeName, gameMode.getDescription()));
        } else if (newGameDifficultyName == null) {
            model.updateGameMode(newGameModeName);
        } else {
            model.updateGameMode(newGameModeName, newGameDifficultyName);
        }
        model.commitTaskManager();
        return new CommandResult(MESSAGE_MODE_CHANGE_SUCCESS);
    }
}
