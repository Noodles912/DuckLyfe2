package Application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javafx.application.Platform;
import javafx.scene.Scene;

/**
 * Unit Test suite for the {@link Inventory} class.
 * <p>
 * This test suite ensures Inventory functionality including initialization,
 * item count adjustments, and proper JavaFX Scene creation for UI integration.
 * </p>
 * <p>
 * <b>Testing Approach:</b> White-box testing verifying correct state manipulation and boundary handling,
 * combined with basic functional testing for UI component instantiation.
 * </p>
 *
 * <b>Test Category:</b> Unit Testing (Inventory Management and UI Components)<br>
 * <b>Automation:</b> Automated using JUnit<br>
 * <b>Requirement:</b> Inventory correctly manages item counts and provides functional UI Scene.<br>
 * 
 * @Author: Atomm Li
 */
public class InventoryTest {

    private Inventory inventory;

    /**
     * Initializes the JavaFX runtime required by Inventory's UI components.
     */
    @BeforeClass
    public static void initJfx() throws Exception {
        Platform.startup(() -> {});
    }

    /**
     * Sets up a fresh Inventory instance before each test execution to maintain isolation.
     */
    @Before
    public void setUp() {
        inventory = new Inventory();
    }

    /**
     * Test Case Name: Default Inventory Values
     * <p>
     * <b>Test Case Description:</b><br>
     * Verifies Inventory initializes with default item counts (food, toys, night caps).
     * </p>
     *
     * <b>Test Steps:</b>
     * <ol>
     *   <li>Create new Inventory instance.</li>
     *   <li>Check initial counts for food, toys, and night caps.</li>
     * </ol>
     *
     * <b>Expected Results:</b> All item counts are initialized to 0.<br>
     * <b>Date Run:</b> (to be filled upon execution)<br>
     * <b>Pass/Fail:</b> (to be filled upon execution)<br>
     * <b>Test Results:</b> (to be filled upon execution)<br>
     * <b>Remarks:</b> (to be filled upon execution)
     */
    @Test
    public void testDefaultInventoryValues() {
        assertEquals("Default food count should be 0", 0, inventory.getFood());
        assertEquals("Default toy count should be 0", 0, inventory.getToy());
        assertEquals("Default night cap count should be 0", 0, inventory.getNightCap());
    }

    /**
     * Test Case Name: Inventory Scene Creation
     * <p>
     * <b>Test Case Description:</b><br>
     * Ensures that the Inventory provides a valid, non-null JavaFX Scene for UI display.
     * </p>
     *
     * <b>Test Steps:</b>
     * <ol>
     *   <li>Create a new Inventory instance.</li>
     *   <li>Call getScene() method.</li>
     *   <li>Verify the returned Scene is non-null.</li>
     * </ol>
     *
     * <b>Expected Results:</b> Non-null JavaFX Scene object.<br>
     * <b>Date Run:</b> (to be filled upon execution)<br>
     * <b>Pass/Fail:</b> (to be filled upon execution)<br>
     * <b>Test Results:</b> (to be filled upon execution)<br>
     * <b>Remarks:</b> (to be filled upon execution)
     */
    @Test
    public void testGetScene() {
        Scene scene = inventory.getScene();
        assertNotNull("Scene should not be null", scene);
    }

    /**
     * Test Case Name: Update Food Count
     * <p>
     * <b>Test Case Description:</b><br>
     * Verifies updating the food count accurately changes the stored value.
     * </p>
     *
     * <b>Test Steps:</b>
     * <ol>
     *   <li>Increase food count by 5.</li>
     *   <li>Decrease food count by 2.</li>
     *   <li>Verify the final count matches expected value.</li>
     * </ol>
     *
     * <b>Expected Results:</b> Final food count equals 3 (5 - 2).<br>
     * <b>Date Run:</b> (to be filled upon execution)<br>
     * <b>Pass/Fail:</b> (to be filled upon execution)<br>
     * <b>Test Results:</b> (to be filled upon execution)<br>
     * <b>Remarks:</b> (to be filled upon execution)
     */
    @Test
    public void testSetFood() {
        inventory.setFood(5);
        assertEquals("Food count should be updated to 5", 5, inventory.getFood());

        inventory.setFood(-2);
        assertEquals("Food count should be updated to 3", 3, inventory.getFood());
    }

    /**
     * Test Case Name: Update Toy Count
     * <p>
     * <b>Test Case Description:</b><br>
     * Ensures toy count adjustments correctly reflect changes to Inventory state.
     * </p>
     *
     * <b>Test Steps:</b>
     * <ol>
     *   <li>Add 4 toys.</li>
     *   <li>Remove 1 toy.</li>
     *   <li>Check final toy count.</li>
     * </ol>
     *
     * <b>Expected Results:</b> Final toy count equals 3 (4 - 1).<br>
     * <b>Date Run:</b> (to be filled upon execution)<br>
     * <b>Pass/Fail:</b> (to be filled upon execution)<br>
     * <b>Test Results:</b> (to be filled upon execution)<br>
     * <b>Remarks:</b> (to be filled upon execution)
     */
    @Test
    public void testSetToy() {
        inventory.setToy(4);
        assertEquals("Toy count should be updated to 4", 4, inventory.getToy());

        inventory.setToy(-1);
        assertEquals("Toy count should be updated to 3", 3, inventory.getToy());
    }

    /**
     * Test Case Name: Update Night Cap Count
     * <p>
     * <b>Test Case Description:</b><br>
     * Validates that night cap count updates are properly applied to the Inventory.
     * </p>
     *
     * <b>Test Steps:</b>
     * <ol>
     *   <li>Add 2 night caps.</li>
     *   <li>Remove 1 night cap.</li>
     *   <li>Verify final night cap count.</li>
     * </ol>
     *
     * <b>Expected Results:</b> Final night cap count equals 1 (2 - 1).<br>
     * <b>Date Run:</b> (to be filled upon execution)<br>
     * <b>Pass/Fail:</b> (to be filled upon execution)<br>
     * <b>Test Results:</b> (to be filled upon execution)<br>
     * <b>Remarks:</b> (to be filled upon execution)
     */
    @Test
    public void testSetNightCap() {
        inventory.setNightCap(2);
        assertEquals("Night cap count should be updated to 2", 2, inventory.getNightCap());

        inventory.setNightCap(-1);
        assertEquals("Night cap count should be updated to 1", 1, inventory.getNightCap());
    }
}
