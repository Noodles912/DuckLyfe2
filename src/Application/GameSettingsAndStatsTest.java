package Application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit Test suite for the {@link GameSettingsAndStats} class.
 * <p>
 * This class verifies the correctness of default initialization, getter, and setter
 * methods for game settings and statistical data such as volume, levels completed,
 * total playtime, time limit settings, last played state, and parental lock activation.
 * </p>
 * <p>
 * <b>Testing Approach:</b> White-box structural testing, focusing on verifying attribute values,
 * boundary conditions, and correct updates via class methods.
 * </p>
 *
 * <b>Test Category:</b> Unit Testing (Game Settings and Statistics Management)<br>
 * <b>Automation:</b> Automated using JUnit<br>
 * <b>Requirement:</b> Proper initialization and modification of game settings and stats.<br>
 * @Author: Atomm Li
 */
public class GameSettingsAndStatsTest {

    private GameSettingsAndStats settings;

    /**
     * Prepares a new GameSettingsAndStats instance before each test to ensure isolation.
     */
    @Before
    public void setUp() {
        settings = new GameSettingsAndStats();
    }

    /**
     * Test Case Name: Verify Default Values
     * <p>
     * <b>Test Case Description:</b><br>
     * Confirms default initialization values of game settings and statistics.
     * </p>
     *
     * <b>Test Steps:</b>
     * <ol>
     *   <li>Instantiate GameSettingsAndStats.</li>
     *   <li>Verify all attributes match expected default values.</li>
     * </ol>
     *
     * <b>Expected Results:</b> Default values set correctly:
     * Volume: 50, Levels: 0, Time Played: 0, Last Played: empty string,
     * Time Limit: -1 (infinite), Parental Lock: inactive.<br>
     * <b>Date Run:</b> (to be filled upon execution)<br>
     * <b>Pass/Fail:</b> (to be filled upon execution)<br>
     * <b>Test Results:</b> (to be filled upon execution)<br>
     * <b>Remarks:</b> (to be filled upon execution)
     */
    @Test
    public void testDefaultValues() {
        assertEquals("Default volume should be 50", 50, settings.getVolume());
        assertEquals("Default level count should be 0", 0, settings.getLevels());
        assertEquals("Default time played should be 0", 0, settings.timePlayed());
        assertEquals("Default last played should be empty", "", settings.lastPlayed());
        assertEquals("Default time limit should be -1 (infinite)", -1, settings.getTimeLimit());
        assertFalse("Parental lock should be inactive by default", settings.isParentalLockActive());
    }

    /**
     * Test Case Name: Set and Verify Volume
     * <p>
     * <b>Test Case Description:</b><br>
     * Validates correct functionality of the setVolume method.
     * </p>
     *
     * <b>Test Steps:</b>
     * <ol>
     *   <li>Set volume to 80.</li>
     *   <li>Verify updated volume.</li>
     * </ol>
     *
     * <b>Expected Results:</b> Volume updated to 80.<br>
     * <b>Date Run:</b> (to be filled upon execution)<br>
     * <b>Pass/Fail:</b> (to be filled upon execution)<br>
     * <b>Test Results:</b> (to be filled upon execution)<br>
     * <b>Remarks:</b> (to be filled upon execution)
     */
    @Test
    public void testSetVolume() {
        settings.setVolume(80);
        assertEquals("Volume should be set to 80", 80, settings.getVolume());
    }

    /**
     * Test Case Name: Set and Verify Levels
     * <p>
     * <b>Test Case Description:</b><br>
     * Confirms correct update of the levels completed counter.
     * </p>
     *
     * <b>Test Steps:</b>
     * <ol>
     *   <li>Set level count to 5.</li>
     *   <li>Verify level count is correctly updated.</li>
     * </ol>
     *
     * <b>Expected Results:</b> Level count correctly updated to 5.<br>
     * <b>Date Run:</b> (to be filled upon execution)<br>
     * <b>Pass/Fail:</b> (to be filled upon execution)<br>
     * <b>Test Results:</b> (to be filled upon execution)<br>
     * <b>Remarks:</b> (to be filled upon execution)
     */
    @Test
    public void testSetLevel() {
        settings.setLevel(5);
        assertEquals("Level count should be set to 5", 5, settings.getLevels());
    }

    /**
     * Test Case Name: Set and Verify Time Played
     * <p>
     * <b>Test Case Description:</b><br>
     * Ensures total playtime accurately increments with each update.
     * </p>
     *
     * <b>Test Steps:</b>
     * <ol>
     *   <li>Add 30 seconds to time played.</li>
     *   <li>Add another 20 seconds.</li>
     *   <li>Verify cumulative total playtime.</li>
     * </ol>
     *
     * <b>Expected Results:</b> Total playtime should equal 50 seconds.<br>
     */
    @Test
    public void testSetTimePlayed() {
        settings.setTimePLayed(30);
        assertEquals("Time played should be 30", 30, settings.timePlayed());
        settings.setTimePLayed(20);
        assertEquals("Time played should be 50", 50, settings.timePlayed());
    }

    /**
     * Test Case Name: Set and Verify Last Played
     * <p>
     * <b>Test Case Description:</b><br>
     * Checks correct storage of the last played game state.
     * </p>
     *
     * <b>Test Steps:</b>
     * <ol>
     *   <li>Set last played to "Duck Level 1".</li>
     *   <li>Verify correct string retrieval.</li>
     * </ol>
     *
     * <b>Expected Results:</b> "Duck Level 1" correctly stored and retrieved.<br>
     */
    @Test
    public void testSetLastPlayed() {
        settings.setLastPlayed("Duck Level 1");
        assertEquals("Last played should be 'Duck Level 1'", "Duck Level 1", settings.lastPlayed());
    }

    /**
     * Test Case Name: Set and Verify Time Limit
     * <p>
     * <b>Test Case Description:</b><br>
     * Ensures correct updating and retrieval of the time limit setting.
     * </p>
     */
    @Test
    public void testSetTimeLimit() {
        settings.setTimeLimit(120);
        assertEquals("Time limit should be 120", 120, settings.getTimeLimit());
    }

    /**
     * Test Case Name: Toggle Parental Lock
     * <p>
     * <b>Test Case Description:</b><br>
     * Verifies parental lock toggling functionality.
     * </p>
     */
    @Test
    public void testParentalLock() {
        settings.setParentalLockActive(true);
        assertTrue("Parental lock should be active", settings.isParentalLockActive());
        settings.setParentalLockActive(false);
        assertFalse("Parental lock should be inactive", settings.isParentalLockActive());
    }
}
