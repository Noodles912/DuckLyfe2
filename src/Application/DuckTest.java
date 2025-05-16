package Application;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit Test suite for the {@link Duck} class.
 * <p>
 * This class ensures correctness of all key attributes, value clamping logic,
 * behavior changes (like death and anger), and internal state updates.
 * </p>
 * <p>
 * <b>Testing Approach:</b> White-box testing for internal attribute logic and
 * black-box functional testing for public method behavior and output.
 * </p>
 *
 * <b>Test Category:</b> Unit Testing (Game Logic Entity)<br>
 * <b>Automation:</b> Automated using JUnit<br>
 * <b>Requirement:</b> Duck class must maintain valid internal state and respond to method calls properly.<br>
 */
public class DuckTest {

    private Duck duck;

    static {
        new javafx.embed.swing.JFXPanel(); // Initialize JavaFX for testing environment
    }

    private static class DummyTimer extends com.sun.glass.ui.Timer {
        public DummyTimer() { super(() -> {}); }
        @Override protected long _start(Runnable runnable, int priority) { return 0L; }
        @Override protected long _start(Runnable runnable) { return 0L; }
        @Override protected void _stop(long time) {}
        @Override protected void _pause(long time) {}
        @Override protected void _resume(long time) {}
    }

    private class TestDuck extends Duck {
        public TestDuck(String name) {
            super(name);
            try {
                Field timerField = Duck.class.getDeclaredField("pulseTimer");
                timerField.setAccessible(true);
                timerField.set(this, new DummyTimer());
            } catch (Exception e) {
                e.printStackTrace();
                fail("Failed to inject DummyTimer: " + e.getMessage());
            }
        }
    }

    @Before
    public void setUp() {
        duck = new TestDuck("TestDuck");
    }

    /**
     * Test Case Name: Default Duck Initialization
     * <p><b>Test Case Description:</b> Verifies all default Duck attributes on creation.</p>
     */
    @Test
    public void testInitialValues() {
        assertEquals("TestDuck", duck.getName());
        assertEquals(10, duck.getHealth());
        assertEquals(10, duck.getHunger());
        assertEquals(100, duck.getHappiness());
        assertEquals(0, duck.getTiredness());
        assertEquals(0, duck.getScore());
        assertEquals(0, duck.getCoins());
        assertEquals(0, duck.getDay());
        assertFalse(duck.isDead());
        assertEquals("white", duck.getColour());
    }

    /**
     * Test Case Name: Set Hunger
     * <p><b>Test Case Description:</b> Confirms hunger value updates and clamping between 0-10.</p>
     */
    @Test
    public void testSetHunger() {
        duck.setHunger(-5);
        assertEquals(5, duck.getHunger());
        duck.setHunger(10);
        assertEquals(10, duck.getHunger());
        duck.setHunger(-20);
        assertEquals(0, duck.getHunger());
    }

    /**
     * Test Case Name: Set Happiness
     * <p><b>Test Case Description:</b> Ensures happiness stays between 0–100 and updates properly.</p>
     */
    @Test
    public void testSetHappiness() {
        duck.setHappiness(-50);
        assertEquals(50, duck.getHappiness());
        duck.setHappiness(60);
        assertEquals(100, duck.getHappiness());
        duck.setHappiness(-150);
        assertEquals(0, duck.getHappiness());
    }

    /**
     * Test Case Name: Set Tiredness
     * <p><b>Test Case Description:</b> Verifies correct tiredness value handling and clamping.</p>
     */
    @Test
    public void testSetTiredness() {
        duck.setTiredness(30);
        assertEquals(30, duck.getTiredness());
        duck.setTiredness(80);
        assertEquals(100, duck.getTiredness());
        duck.setTiredness(-150);
        assertEquals(0, duck.getTiredness());
    }

    /**
     * Test Case Name: Set Health and Trigger Death
     * <p><b>Test Case Description:</b> Tests health reduction and death state logic.</p>
     */
    @Test
    public void testSetHealthAndFlipLivingState() {
        duck.setHealth(-5);
        assertEquals(5, duck.getHealth());
        assertFalse(duck.isDead());

        duck.setHealth(-10);
        assertEquals(0, duck.getHealth());
        assertTrue(duck.isDead());
    }

    /**
     * Test Case Name: Score, Coins, and Day Updates
     * <p><b>Test Case Description:</b> Verifies correct handling of score, coin, and day values.</p>
     */
    @Test
    public void testScoreCoinsAndDay() {
        duck.setScore(5);
        assertEquals(5, duck.getScore());
        duck.setScore(-10);
        assertEquals(0, duck.getScore());

        duck.setCoins(10);
        assertEquals(10, duck.getCoins());
        duck.setCoins(-5);
        assertEquals(5, duck.getCoins());

        duck.setDay(3);
        assertEquals(3, duck.getDay());
        duck.increaseDay();
        assertEquals(4, duck.getDay());
    }

    /**
     * Test Case Name: Set and Get Colour
     * <p><b>Test Case Description:</b> Ensures correct color setting and retrieval.</p>
     */
    @Test
    public void testSetAndGetColour() {
        assertEquals("white", duck.getColour());
        duck.setColour("blue");
        assertEquals("blue", duck.getColour());
        duck.setColour("pink");
        assertEquals("pink", duck.getColour());
    }

    /**
     * Test Case Name: Is Angry
     * <p><b>Test Case Description:</b> Validates Duck's anger detection based on happiness value.</p>
     */
    @Test
    public void testIsAngry() {
        duck.setHappiness(-80);
        assertTrue(duck.isAngry());
        duck.setHappiness(10);
        assertFalse(duck.isAngry());
    }

    /**
     * Test Case Name: Flip Living State
     * <p><b>Test Case Description:</b> Ensures living state toggles without affecting health.</p>
     */
    @Test
    public void testFlipLivingState() {
        assertFalse(duck.isDead());
        int health = duck.getHealth();
        duck.flipLivingState();
        assertTrue(duck.isDead());
        assertEquals(health, duck.getHealth());
        duck.flipLivingState();
        assertFalse(duck.isDead());
    }
}
