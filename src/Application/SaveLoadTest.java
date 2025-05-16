package Application;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import javax.swing.SwingUtilities;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javafx.embed.swing.JFXPanel;

/**
 * Unit Test suite for the {@link SaveLoad} class.
 * <p>
 * This class validates the functionality of saving and loading game data, inventory,
 * and settings. It ensures data persistence, integrity, and accurate restoration of game states.
 * </p>
 * <p>
 * <b>Testing Approach:</b> White-box structural testing, ensuring methods handle file operations
 * correctly, preserve data integrity, and handle expected data boundary conditions.
 * </p>
 *
 * <b>Test Category:</b> Unit Testing (Persistence and Data Integrity)<br>
 * <b>Automation:</b> Automated using JUnit<br>
 * <b>Requirement:</b> Persistent storage and accurate retrieval of game state data and settings.<br>
 */
public class SaveLoadTest {

    private SaveLoad saveLoad;
    private Duck testDuck;
    private Inventory testInv;
    private GameSettingsAndStats testSettings;

    /**
     * Initializes JavaFX runtime environment required for SaveLoad class operations.
     */
    @BeforeClass
    public static void initToolkit() throws InterruptedException {
        System.setProperty("java.awt.headless", "false");
        System.setErr(new java.io.PrintStream(new java.io.OutputStream() {
            public void write(int b) {}
        }));

        final CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> {
            new JFXPanel();
            latch.countDown();
        });
        latch.await();
    }

    /**
     * Prepares fresh instances of SaveLoad, Duck, Inventory, and GameSettingsAndStats
     * before each test to guarantee isolation.
     */
    @Before
    public void setUp() {
        saveLoad = new SaveLoad();
        testDuck = createTestDuck();
        testInv = createTestInventory();
        testSettings = createTestSettings();
    }

    /**
     * Helper method to create a Duck instance with predefined attributes.
     * @return fully initialized Duck instance for testing.
     */
    private Duck createTestDuck() {
        Duck duck = new Duck("TestDuck") {{
            initializePulseTimer();
        }};
        duck.setHealth(8);
        duck.setTiredness(2);
        duck.setHunger(7);
        duck.setHappiness(90);
        duck.setDead(false);
        duck.setColour("blue");
        duck.setDay(3);
        duck.setCoins(15);
        return duck;
    }

    /**
     * Helper method to create an Inventory instance with predefined item counts.
     * @return fully initialized Inventory instance for testing.
     */
    private Inventory createTestInventory() {
        Inventory inv = new Inventory();
        inv.setFood(5);
        inv.setToy(3);
        inv.setNightCap(2);
        return inv;
    }

    /**
     * Helper method to create a GameSettingsAndStats instance with predefined values.
     * @return fully initialized GameSettingsAndStats instance for testing.
     */
    private GameSettingsAndStats createTestSettings() {
        GameSettingsAndStats settings = new GameSettingsAndStats();
        settings.setVolume(70);
        settings.setLevel(4);
        settings.setTimePLayed(120);
        settings.setLastPlayed("Level 3");
        settings.setTimeLimit(10);
        settings.setParentalLockActive(true);
        return settings;
    }

    /**
     * Cleans up generated files after each test execution.
     */
    @After
    public void tearDown() {
        deleteFile("allData.json");
        deleteFile("settings.json");
    }

    /**
     * Deletes specified file from the filesystem.
     * @param filename Name of the file to delete.
     */
    private void deleteFile(String filename) {
        File file = new File(filename);
        if (file.exists() && !file.delete()) {
            System.err.println("Failed to delete " + filename);
        }
    }

    /**
     * Test Case Name: Save and Load All Data
     * <p>
     * <b>Test Case Description:</b><br>
     * Ensures complete game state (Duck, Inventory, Settings) is saved and accurately restored.
     * </p>
     *
     * <b>Test Steps:</b>
     * <ol>
     *   <li>Call saveAllData with test Duck, Inventory, and Settings.</li>
     *   <li>Call loadAllSaves and retrieve stored data.</li>
     *   <li>Verify all loaded attributes match initial data.</li>
     * </ol>
     *
     * <b>Pre-Requisites:</b> None<br>
     * <b>Expected Results:</b> All data attributes restored exactly as saved.<br>
     * <b>Date Run:</b> (to be filled upon execution)<br>
     * <b>Pass/Fail:</b> (to be filled upon execution)<br>
     * <b>Test Results:</b> (to be filled upon execution)<br>
     * <b>Remarks:</b> (to be filled upon execution)
     */
    @Test
    public void testSaveAndLoadAllData() {
        saveLoad.saveAllData(testDuck, testInv, testSettings);
        HashMap<String, Object[]> savesMap = saveLoad.loadAllSaves();

        assertNotNull(savesMap);
        assertTrue(savesMap.containsKey("TestDuck"));
        Object[] data = savesMap.get("TestDuck");
        assertNotNull(data);
        assertEquals(3, data.length);

        Duck loadedDuck = (Duck) data[0];
        Inventory loadedInv = (Inventory) data[1];
        GameSettingsAndStats loadedSettings = (GameSettingsAndStats) data[2];

        assertEquals(testDuck.getName(), loadedDuck.getName());
        assertEquals(testDuck.getHealth(), loadedDuck.getHealth());
        assertEquals(testDuck.getTiredness(), loadedDuck.getTiredness());
        assertEquals(testDuck.getHunger(), loadedDuck.getHunger());
        assertEquals(testDuck.getHappiness(), loadedDuck.getHappiness());
        assertEquals(testDuck.isDead(), loadedDuck.isDead());
        assertEquals(testDuck.getColour(), loadedDuck.getColour());
        assertEquals(testDuck.getDay(), loadedDuck.getDay());
        assertEquals(testDuck.getCoins(), loadedDuck.getCoins());

        assertEquals(testInv.getFood(), loadedInv.getFood());
        assertEquals(testInv.getToy(), loadedInv.getToy());
        assertEquals(testInv.getNightCap(), loadedInv.getNightCap());

        assertEquals(testSettings.getVolume(), loadedSettings.getVolume());
        assertEquals(testSettings.getLevels(), loadedSettings.getLevels());
        assertEquals(testSettings.timePlayed(), loadedSettings.timePlayed());
        assertEquals(testSettings.lastPlayed(), loadedSettings.lastPlayed());
        assertEquals(testSettings.getTimeLimit(), loadedSettings.getTimeLimit());
        assertEquals(testSettings.isParentalLockActive(), loadedSettings.isParentalLockActive());
    }

    /**
     * Test Case Name: Save and Load Settings
     * <p>
     * <b>Test Case Description:</b><br>
     * Ensures game settings alone can be independently saved and accurately retrieved.
     * </p>
     *
     * <b>Test Steps:</b>
     * <ol>
     *   <li>Save test settings using saveSettings().</li>
     *   <li>Retrieve settings using loadSettings().</li>
     *   <li>Verify loaded settings match saved values exactly.</li>
     * </ol>
     *
     * <b>Pre-Requisites:</b> None<br>
     * <b>Expected Results:</b> Loaded settings match initial test settings precisely.<br>
     * <b>Date Run:</b> (to be filled upon execution)<br>
     * <b>Pass/Fail:</b> (to be filled upon execution)<br>
     * <b>Test Results:</b> (to be filled upon execution)<br>
     * <b>Remarks:</b> (to be filled upon execution)
     */
    @Test
    public void testSaveAndLoadSettings() {
        saveLoad.saveSettings(testSettings);
        GameSettingsAndStats loadedSettings = saveLoad.loadSettings();

        assertNotNull(loadedSettings);
        assertEquals(testSettings.getVolume(), loadedSettings.getVolume());
        assertEquals(testSettings.getLevels(), loadedSettings.getLevels());
        assertEquals(testSettings.timePlayed(), loadedSettings.timePlayed());
        assertEquals(testSettings.lastPlayed(), loadedSettings.lastPlayed());
        assertEquals(testSettings.getTimeLimit(), loadedSettings.getTimeLimit());
        assertEquals(testSettings.isParentalLockActive(), loadedSettings.isParentalLockActive());
    }
}
