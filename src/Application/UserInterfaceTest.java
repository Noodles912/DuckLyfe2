package Application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;

/**
 * Unit Test suite for the {@link UserInterface} class.
 * <p>
 * These tests ensure that JavaFX-based UI components are correctly instantiated
 * and dynamically reflect changes in the Duck's game state, specifically the coin count and score.
 * </p>
 * <p>
 * <b>Testing Approach:</b> Black-box (functional) tests verifying correctness of UI component state updates.
 * Detailed visual/UI testing requires additional frameworks such as TestFX.
 * </p>
 *
 * <b>Test Category:</b> Unit Testing (JavaFX GUI Components)<br>
 * <b>Automation:</b> Automated using JUnit<br>
 * <b>Requirement:</b> UI accurately reflects Duck’s state (coin count, score, scene dimensions)<br>
 */
public class UserInterfaceTest {

    private UserInterface ui;
    private Duck testDuck;
    private GameSettingsAndStats testSettings;
    private Label coinLabel;

    /**
     * Initializes JavaFX toolkit for all tests.
     * Necessary for testing JavaFX components in isolation.
     */
    @BeforeClass
    public static void initJfx() throws Exception {
        Platform.startup(() -> {});
    }

    /**
     * Sets up the test environment before each test case execution.
     * Initializes test objects with predefined attributes to guarantee test isolation.
     */
    @Before
    public void setUp() {
        testDuck = new Duck("TestDuck");
        testDuck.setHealth(7);
        testDuck.setHunger(4);
        testDuck.setHappiness(85);
        testDuck.setTiredness(3);
        testDuck.setScore(150);
        testDuck.setCoins(20);

        testSettings = new GameSettingsAndStats();
        coinLabel = new Label();

        ui = new UserInterface(testSettings, testDuck, coinLabel);
    }

    /**
     * Test Case: Verify Scene Initialization.
     * <p>
     * Ensures that the UserInterface initializes a Scene with the specified dimensions (800x600).
     * </p>
     *
     * <b>Test Steps:</b>
     * <ol>
     *   <li>Create a UserInterface instance with valid Duck and settings.</li>
     *   <li>Retrieve the Scene object via getScene().</li>
     *   <li>Check that Scene object is non-null and has correct dimensions.</li>
     * </ol>
     *
     * <b>Expected Results:</b> A valid Scene object with width = 800 and height = 600.
     */
    @Test
    public void testGetScene() {
        Scene scene = ui.getScene();
        assertNotNull("Scene should not be null", scene);
        assertEquals("Scene width should be 800", 800, scene.getWidth(), 0.01);
        assertEquals("Scene height should be 600", 600, scene.getHeight(), 0.01);
    }

    /**
     * Test Case: Verify Coin Display Update.
     * <p>
     * Validates that the coin label updates correctly when the Duck's coin count changes.
     * </p>
     *
     * <b>Test Steps:</b>
     * <ol>
     *   <li>Update Duck's coin count.</li>
     *   <li>Invoke updateCoinDisplay() from UserInterface.</li>
     *   <li>Check coin label reflects new total coin count.</li>
     * </ol>
     *
     * <b>Expected Results:</b> Coin label text correctly shows updated coin total (initial 20 + added 35 = 55).
     */
    @Test
    public void testUpdateCoinDisplay() {
        testDuck.setCoins(35);
        ui.updateCoinDisplay();
        assertEquals("Coin display should update to new coin count", " x 55", ui.getCoinLabel().getText());
    }

    /**
     * Test Case: Verify Score Display Update.
     * <p>
     * Checks that the score label updates to accurately reflect the Duck’s current score.
     * </p>
     *
     * <b>Test Steps:</b>
     * <ol>
     *   <li>Modify the Duck's score attribute.</li>
     *   <li>Call updateScore() from UserInterface.</li>
     *   <li>Verify score label text matches updated Duck's score.</li>
     * </ol>
     *
     * <b>Expected Results:</b> Score label correctly displays the Duck’s updated score.
     */
    @Test
    public void testUpdateScore() {
        testDuck.setScore(200);
        ui.updateScore();
        assertEquals("Score: " + testDuck.getScore(), ui.getScoreLabel().getText());
    }
}
