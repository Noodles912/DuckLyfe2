package Application;

/**
 * The GameSettingsAndStats class encapsulates the game settings and statistics.
 * <p>
 * This class manages settings such as volume and time limit, as well as statistics like the
 * number of levels completed, total time played, and the identifier of the last played game element.
 * Additionally, it provides methods to enable or disable parental lock.
 * </p>
 *
 * @author Zachary Goodman
 */
public class GameSettingsAndStats {
    // Game volume (0 to 100)
    private int volume;
    // Number of levels completed or current level count
    private int levels;
    // Total time played (in seconds or another time unit)
    private int timePlayed;
    // Identifier or description of the last played element (e.g., duck or level)
    private String lastPLayed;
    // Time limit for the game (-1 indicates no limit/infinite)
    private int timeLimit;
    // Flag to indicate if the parental lock is active
    private boolean parentalLockActive; // New field to track parental lock status
    // Number of sessions played to calculate average
    private int numberOfSessions; 

    /**
     * Constructs a new GameSettingsAndStats object with default settings.
     * <p>
     * Default values:
     * <ul>
     *     <li>Volume: 50</li>
     *     <li>Levels: 0</li>
     *     <li>Time Played: 0</li>
     *     <li>Last Played: empty string</li>
     *     <li>Time Limit: -1 (infinite)</li>
     *     <li>Parental Lock: inactive</li>
     * </ul>
     * </p>
     */
    public GameSettingsAndStats() {
        // Set default volume to 50
        this.volume = 50;
        // Initialize levels to 0
        this.levels = 0;
        // Initialize time played to 0
        this.timePlayed = 0;
        // Set last played to an empty string by default
        this.lastPLayed = "";
        // Default time limit set to -1 (indicates infinite time)
        this.timeLimit = -1;
        // Parental lock is inactive by default
        this.parentalLockActive = false;
        // Setting number sessions 0 by default;
        this.numberOfSessions = 0;
    }

    /**
     * Retrieves the current volume level.
     *
     * @return the volume level as an integer
     */
    public int getVolume() {
        // Return the current volume setting
        return volume;
    }

    /**
     * Retrieves the current level count.
     *
     * @return the number of levels completed
     */
    public int getLevels() {
        // Return the current level count
        return levels;
    }
    
    /**
     * Retrieves the number of sessions played
     *
     * @return the number of sessions played
     */
    public int getSessions() {
        // Return the current level count
        return numberOfSessions;
    }
    /**
     * Updates the number of sessions played
     *
     * @param the number that sessions will be updated to
     */
    public void updateNumSessions(int change) {
    	// Update sessions as needed
    	numberOfSessions += change;
    }
    
    /**
     * Updates the number of sessions played by 1
     *
     */
    public void addSession() {
    	numberOfSessions += 1;
    }

    /**
     * Retrieves the total time played.
     *
     * @return the total time played
     */
    public int timePlayed() {
        // Return the total time played
        return timePlayed;
    }

    /**
     * Retrieves the last played game element (e.g., duck or level).
     *
     * @return the identifier of the last played element
     */
    public String lastPlayed() {
        // Return the last played game element as a string
        return lastPLayed;
    }

    /**
     * Retrieves the current time limit setting.
     *
     * @return the time limit, where -1 indicates no limit
     */
    public int getTimeLimit() {
        // Return the current time limit
        return timeLimit;
    }

    /**
     * Sets the time limit for the game.
     *
     * @param limit the new time limit (use -1 for infinite)
     */
    public void setTimeLimit(int limit) {
        // Update the time limit with the provided value
        this.timeLimit = limit;
    }

    /**
     * Sets the volume level for the game.
     *
     * @param newV the new volume level
     */
    public void setVolume(int newV) {
        // Update the volume with the provided value
        volume = newV;
    }

    /**
     * Sets the level count for the game.
     *
     * @param newL the new level count
     */
    public void setLevel(int newL) {
        // Update the levels with the provided value
        levels = newL;
    }

    /**
     * Increments the total time played by a specified amount.
     *
     * @param change the amount of time to add to the total time played
     */
    public void setTimePLayed(int change) {
        // Increase the time played by the given amount
        timePlayed += change;
    }

    /**
     * Sets the last played game element (e.g., duck or level).
     *
     * @param duck the identifier of the last played element
     */
    public void setLastPlayed(String duck) {
        // Update the last played game element with the provided string
        lastPLayed = duck;
    }

    /**
     * Checks if the parental lock is active.
     *
     * @return true if the parental lock is active, false otherwise
     */
    public boolean isParentalLockActive() {
        // Return the current status of the parental lock
        return parentalLockActive;
    }

    /**
     * Sets the parental lock status.
     *
     * @param parentalLockActive true to activate the parental lock, false to deactivate it
     */
    public void setParentalLockActive(boolean parentalLockActive) {
        // Update the parental lock status with the provided value
        this.parentalLockActive = parentalLockActive;
    }

    /**
     * Prints the current volume level to the console.
     */
    public void printV() {
        // Print the current volume to the console for debugging or informational purposes
        System.out.println(volume);
    }
}
