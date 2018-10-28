package seedu.address.model.achievement;

/**
 * Enums are virtually singletons in Java applications, as such
 * should remain immutable (No mutable fields) to prevent
 * it's value from changing globally throughout the application.
 *
 * At the same time, it should remain immutable as a mutable singleton
 * in a multi-threaded application can have unforeseen implications.
 *
 * Represent the level of achievement in reached by the user.
 * There are only 5 valid levels, LEVEL_1 to LEVEL_5.
 * Each level has a name and a range of xp that the level corresponds to.
 */
public enum Level {
    LEVEL_1("lvl.1", 0, 500),
    LEVEL_2("lvl.2", 500, 1000),
    LEVEL_3("lvl.3", 1000, 2000),
    LEVEL_4("lvl.4", 2000, 4000),
    LEVEL_5("lvl.5", 4000, 100000);

    public static final String MESSAGE_LEVEL_CONSTRAINTS =
            "Level should only have the value lvl.n, where n is integer from 1 to 5";
    public static final String LEVEL_VALIDATION_REGEX = "lvl.[1-5]";
    private String levelName;
    private Integer minXp;
    private Integer maxXp;

    /**
     * Constructs a {@code Level}.
     *
     * @param levelName a string with corresponding Level object.
     */
    Level(String levelName, Integer minXp, Integer maxXp) {
        this.levelName = levelName;
        this.minXp = minXp;
        this.maxXp = maxXp;
    }

    public Integer getMinXp() {
        return this.minXp;
    }

    public Integer getMaxXp() {
        return this.maxXp;
    }

    /**
     * Returns true if a given string is a valid Level value.
     */
    public static boolean isValidLevel(String name) {
        try {
            return name.matches(LEVEL_VALIDATION_REGEX);
        } catch (NullPointerException ex) {
            return false;
        }
    }

    /**
     * Returns the corresponding level object of {@param string}.
     */
    public static Level fromString(String string) {
        if (!isValidLevel(string)) {
            throw new IllegalArgumentException();
        }
        int offset = -1;
        int index = Character.digit(string.charAt(4), 10) + offset;
        return (Level.values())[index];
    }

    @Override
    public String toString() {
        return levelName;
    }
}
