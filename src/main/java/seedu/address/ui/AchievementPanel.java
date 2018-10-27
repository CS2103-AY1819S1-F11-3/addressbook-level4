package seedu.address.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AchievementsUpdatedEvent;
import seedu.address.model.achievement.AchievementRecord;
import seedu.address.model.util.DateFormatUtil;

/**
 * A ui for the achievement field that is displayed on top of the status bar.
 */
public class AchievementPanel extends UiPart<Region> {

    private static final Logger logger = LogsCenter.getLogger(AchievementPanel.class);

    private static final String FXML = "AchievementPanel.fxml";

    @FXML
    private Label timeSpanLabel;
    @FXML
    private Label xpValueLabel;
    @FXML
    private Label levelValueLabel;
    @FXML
    private Label numTasksLabel;

    public AchievementPanel(AchievementRecord achievements) {
        super(FXML);
        setLabelValues(achievements);
        registerAsAnEventHandler(this);
    }

    public void setLabelValues(AchievementRecord achievements) {
        String level = achievements.getLevel().toString();
        int displayOption = achievements.getDisplayOption();
        levelValueLabel.setText(level);
        switch (displayOption) {
            case 1:
                timeSpanLabel.setText("All-time achievements:");
                String xp = Integer.toString(achievements.getXpValue());
                xpValueLabel.setText(xp + " / " + achievements.getLevelMaxXp());
                numTasksLabel.setText(Integer.toString(achievements.getNumTaskCompleted()));
                break;
            case 2:
                timeSpanLabel.setText("Achievements since " +
                        getLastDayBreakPoint(achievements.getNextDayBreakPoint()) + " :");
                xpValueLabel.setText(Integer.toString(achievements.getXpValueByDay()));
                numTasksLabel.setText(Integer.toString(achievements.getNumTaskCompletedByDay()));
                break;
            case 3:
                timeSpanLabel.setText("Achievements since " +
                        getLastWeekBreakPoint(achievements.getNextWeekBreakPoint()) + " :");
                xpValueLabel.setText(Integer.toString(achievements.getXpValueByWeek()));
                numTasksLabel.setText(Integer.toString(achievements.getNumTaskCompletedByWeek()));
                break;
            default:
                assert false;
        }
        
    }

    private String getLastDayBreakPoint(Calendar nextDayBreakPoint) {
        Calendar copy = (GregorianCalendar) nextDayBreakPoint.clone();
        copy.add(Calendar.DAY_OF_MONTH, -1);
        return DateFormatUtil.FORMAT_MINIMAL.format(copy.getTime());
        
    }

    private String getLastWeekBreakPoint(Calendar nextWeekBreakPoint) {
        Calendar copy = (GregorianCalendar) nextWeekBreakPoint.clone();
        copy.add(Calendar.DAY_OF_MONTH, -7);
        return DateFormatUtil.FORMAT_MINIMAL.format(copy.getTime());

    }

    @Subscribe
    public void handleAchievementsUpdatedEvent(AchievementsUpdatedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        setLabelValues(event.data);
    }

}
