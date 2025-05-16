package Application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * Represents a Duck with various attributes such as health, hunger, happiness, sleep level, and a name.
 * <br><br>
 * Provides methods to modify these attributes and to trigger various animations based on the
 * duck's state (e.g., walking, eating, sleeping, receiving gifts, or dead).
 *
 * @author Daniel Harapiak
 * @author Brandon Nguyen
 * @author Dinith Nawaratne
 * @author Zachary Goodman
 * @author Atomm Li
 */
public class Duck {

    // Duck attributes
    private int health;
    private int tiredness;
    private int hunger;
    private int happiness;
    private int score;
    private int numCoins;
    private int dayCounter;
    private String name;
    private Boolean isDead;
    private boolean isDeadAnimationPlayed = false; // Tracks if dead animation has already been played
    private String colour;

    // JavaFX ImageView to display the duck and Timeline for animations
    private ImageView duckImageView;
    private Timeline duckAnimation;

    // For JUnit Testing
    protected transient com.sun.glass.ui.Timer pulseTimer;
    
    // Default duck animation frames for a white duck
    private String[] defaultDuckFrames = {
        "file:src/assets/duckSpriteSheet/duck002.png",
        "file:src/assets/duckSpriteSheet/duck003.png",
        "file:src/assets/duckSpriteSheet/duck060.png",
        "file:src/assets/duckSpriteSheet/duck090.png",
        "file:src/assets/duckSpriteSheet/duck061.png",
        "file:src/assets/duckSpriteSheet/duck062.png",
        "file:src/assets/duckSpriteSheet/duck061.png",
    };

    // Default animation frames for a pink duck
    private String[] defaulPinkDuckFrames = {
        "file:src/assets/pinkDuckSprite/tile002.png",
        "file:src/assets/pinkDuckSprite/tile003.png",
        "file:src/assets/pinkDuckSprite/tile060.png",
        "file:src/assets/pinkDuckSprite/tile090.png",
        "file:src/assets/pinkDuckSprite/tile061.png",
        "file:src/assets/pinkDuckSprite/tile062.png",
        "file:src/assets/pinkDuckSprite/tile061.png",
    };

    // Default animation frames for a blue duck
    private String[] defaultBlueDuckFrames = {
        "file:src/assets/blueDuckSprite/tile002.png",
        "file:src/assets/blueDuckSprite/tile003.png",
        "file:src/assets/blueDuckSprite/tile060.png",
        "file:src/assets/blueDuckSprite/tile090.png",
        "file:src/assets/blueDuckSprite/tile061.png",
        "file:src/assets/blueDuckSprite/tile062.png",
        "file:src/assets/blueDuckSprite/tile061.png",
    };
    
    // Walking animation frames (default white duck)
    private String[] walkingDuckFrames = {
        "file:src/assets/duckSpriteSheet/duck092.png",
        "file:src/assets/duckSpriteSheet/duck091.png",
        "file:src/assets/duckSpriteSheet/duck092.png",
        "file:src/assets/duckSpriteSheet/duck093.png",
    };

    // Walking animation frames (pink duck)
    private String[] pinkWalkingDuckFrames = {
        "file:src/assets/pinkDuckSprite/tile092.png",
        "file:src/assets/pinkDuckSprite/tile091.png",
        "file:src/assets/pinkDuckSprite/tile092.png",
        "file:src/assets/pinkDuckSprite/tile093.png",
    };

    // Walking animation frames (blue duck)
    private String[] blueWalkingDuckFrames = {
        "file:src/assets/blueDuckSprite/tile092.png",
        "file:src/assets/blueDuckSprite/tile091.png",
        "file:src/assets/blueDuckSprite/tile092.png",
        "file:src/assets/blueDuckSprite/tile093.png",
    };

    // Eating animation frames (default white duck)
    private String[] eatingDuckFrames = {
        "file:src/assets/duckSpriteSheet/duck225.png",
        "file:src/assets/duckSpriteSheet/duck226.png",
        "file:src/assets/duckSpriteSheet/duck227.png",
        "file:src/assets/duckSpriteSheet/duck228.png",
        "file:src/assets/duckSpriteSheet/duck229.png",
    };

    // Eating animation frames (pink duck)
    private String[] pinkEatingDuckFrames = {
        "file:src/assets/pinkDuckSprite/tile225.png",
        "file:src/assets/pinkDuckSprite/tile226.png",
        "file:src/assets/pinkDuckSprite/tile227.png",
        "file:src/assets/pinkDuckSprite/tile228.png",
        "file:src/assets/pinkDuckSprite/tile229.png",
    };

    // Eating animation frames (blue duck)
    private String[] blueEatingDuckFrames = {
        "file:src/assets/blueDuckSprite/tile225.png",
        "file:src/assets/blueDuckSprite/tile226.png",
        "file:src/assets/blueDuckSprite/tile227.png",
        "file:src/assets/blueDuckSprite/tile228.png",
        "file:src/assets/blueDuckSprite/tile229.png",
    };

    // Sleeping animation frames (default white duck)
    private String[] sleepingDuckFrames = {
        "file:src/assets/duckSpriteSheet/duck210.png",
        "file:src/assets/duckSpriteSheet/duck211.png",
        "file:src/assets/duckSpriteSheet/duck212.png",
        "file:src/assets/duckSpriteSheet/duck213.png",
        "file:src/assets/duckSpriteSheet/duck214.png",
        "file:src/assets/duckSpriteSheet/duck215.png",
        "file:src/assets/duckSpriteSheet/duck216.png",
        "file:src/assets/duckSpriteSheet/duck217.png",
        "file:src/assets/duckSpriteSheet/duck218.png",
        "file:src/assets/duckSpriteSheet/duck219.png",
        "file:src/assets/duckSpriteSheet/duck220.png",
        "file:src/assets/duckSpriteSheet/duck215.png",
        "file:src/assets/duckSpriteSheet/duck216.png",
        "file:src/assets/duckSpriteSheet/duck217.png",
        "file:src/assets/duckSpriteSheet/duck218.png",
        "file:src/assets/duckSpriteSheet/duck219.png",
        "file:src/assets/duckSpriteSheet/duck220.png",
        "file:src/assets/duckSpriteSheet/duck221.png",
        "file:src/assets/duckSpriteSheet/duck222.png",
        "file:src/assets/duckSpriteSheet/duck223.png",
        "file:src/assets/duckSpriteSheet/duck224.png",
    };

    // Sleeping animation frames (pink duck)
    private String[] pinkSleepingDuckFrames = {
        "file:src/assets/pinkDuckSprite/tile210.png",
        "file:src/assets/pinkDuckSprite/tile211.png",
        "file:src/assets/pinkDuckSprite/tile212.png",
        "file:src/assets/pinkDuckSprite/tile213.png",
        "file:src/assets/pinkDuckSprite/tile214.png",
        "file:src/assets/pinkDuckSprite/tile215.png",
        "file:src/assets/pinkDuckSprite/tile216.png",
        "file:src/assets/pinkDuckSprite/tile217.png",
        "file:src/assets/pinkDuckSprite/tile218.png",
        "file:src/assets/pinkDuckSprite/tile219.png",
        "file:src/assets/pinkDuckSprite/tile220.png",
        "file:src/assets/pinkDuckSprite/tile215.png",
        "file:src/assets/pinkDuckSprite/tile216.png",
        "file:src/assets/pinkDuckSprite/tile217.png",
        "file:src/assets/pinkDuckSprite/tile218.png",
        "file:src/assets/pinkDuckSprite/tile219.png",
        "file:src/assets/pinkDuckSprite/tile220.png",
        "file:src/assets/pinkDuckSprite/tile221.png",
        "file:src/assets/pinkDuckSprite/tile222.png",
        "file:src/assets/pinkDuckSprite/tile223.png",
        "file:src/assets/pinkDuckSprite/tile224.png",
    };

    // Sleeping animation frames (blue duck)
    private String[] blueSleepingDuckFrames = {
        "file:src/assets/blueDuckSprite/tile210.png",
        "file:src/assets/blueDuckSprite/tile211.png",
        "file:src/assets/blueDuckSprite/tile212.png",
        "file:src/assets/blueDuckSprite/tile213.png",
        "file:src/assets/blueDuckSprite/tile214.png",
        "file:src/assets/blueDuckSprite/tile215.png",
        "file:src/assets/blueDuckSprite/tile216.png",
        "file:src/assets/blueDuckSprite/tile217.png",
        "file:src/assets/blueDuckSprite/tile218.png",
        "file:src/assets/blueDuckSprite/tile219.png",
        "file:src/assets/blueDuckSprite/tile220.png",
        "file:src/assets/blueDuckSprite/tile215.png",
        "file:src/assets/blueDuckSprite/tile216.png",
        "file:src/assets/blueDuckSprite/tile217.png",
        "file:src/assets/blueDuckSprite/tile218.png",
        "file:src/assets/blueDuckSprite/tile219.png",
        "file:src/assets/blueDuckSprite/tile220.png",
        "file:src/assets/blueDuckSprite/tile221.png",
        "file:src/assets/blueDuckSprite/tile222.png",
        "file:src/assets/blueDuckSprite/tile223.png",
        "file:src/assets/blueDuckSprite/tile224.png",
    };

    // Gift-receiving animation frames (default white duck)
    private String[] receivedGiftDuckFrames = {
        "file:src/assets/duckSpriteSheet/duck030.png",
        "file:src/assets/duckSpriteSheet/duck031.png",
        "file:src/assets/duckSpriteSheet/duck032.png",
        "file:src/assets/duckSpriteSheet/duck033.png",
        "file:src/assets/duckSpriteSheet/duck034.png",
        "file:src/assets/duckSpriteSheet/duck035.png",
    };

    // Gift-receiving animation frames (pink duck)
    private String[] pinkReceivedGiftDuckFrames = {
        "file:src/assets/pinkDuckSprite/tile030.png",
        "file:src/assets/pinkDuckSprite/tile031.png",
        "file:src/assets/pinkDuckSprite/tile032.png",
        "file:src/assets/pinkDuckSprite/tile033.png",
        "file:src/assets/pinkDuckSprite/tile034.png",
        "file:src/assets/pinkDuckSprite/tile035.png",
    };

    // Gift-receiving animation frames (blue duck)
    private String[] blueReceivedGiftDuckFrames = {
        "file:src/assets/blueDuckSprite/tile030.png",
        "file:src/assets/blueDuckSprite/tile031.png",
        "file:src/assets/blueDuckSprite/tile032.png",
        "file:src/assets/blueDuckSprite/tile033.png",
        "file:src/assets/blueDuckSprite/tile034.png",
        "file:src/assets/blueDuckSprite/tile035.png",
    };
   
    // Dead animation frames (default white duck)
    private String[] deadDuckFrames = {
        "file:src/assets/duckSpriteSheet/duck195.png",
        "file:src/assets/duckSpriteSheet/duck196.png",
        "file:src/assets/duckSpriteSheet/duck197.png",
        "file:src/assets/duckSpriteSheet/duck198.png",
        "file:src/assets/duckSpriteSheet/duck199.png",
        "file:src/assets/duckSpriteSheet/duck200.png",
        "file:src/assets/duckSpriteSheet/duck200.png",
    };
    
    // Dead animation frames (pink duck)
    private String[] pinkDeadDuckFrames = {
        "file:src/assets/pinkDuckSprite/tile195.png",
        "file:src/assets/pinkDuckSprite/tile196.png",
        "file:src/assets/pinkDuckSprite/tile197.png",
        "file:src/assets/pinkDuckSprite/tile198.png",
        "file:src/assets/pinkDuckSprite/tile199.png",
        "file:src/assets/pinkDuckSprite/tile200.png",
        "file:src/assets/pinkDuckSprite/tile200.png",
    };
    
    // Dead animation frames (blue duck)
    private String[] blueDeadDuckFrames = {
        "file:src/assets/blueDuckSprite/tile195.png",
        "file:src/assets/blueDuckSprite/tile196.png",
        "file:src/assets/blueDuckSprite/tile197.png",
        "file:src/assets/blueDuckSprite/tile198.png",
        "file:src/assets/blueDuckSprite/tile199.png",
        "file:src/assets/blueDuckSprite/tile200.png",
        "file:src/assets/blueDuckSprite/tile200.png",
    };

    /**
     * Starts the dead animation sequence if it has not already been played.
     * <p>
     * This method checks if the dead animation has been played. If not, it selects the correct set of frames
     * based on the duck's colour, stops any current animation, and sets up a new animation to play each frame.
     * Once finished, it explicitly sets the final frame.
     * </p>
     */
    public void startDeadAnimation() {
        // Exit if the dead animation was already played
        if (isDeadAnimationPlayed) {
            return;
        }
        // Mark the animation as played
        isDeadAnimationPlayed = true;
    
        // Select the proper dead animation frames based on duck colour
        String[] framesToPlay;
        switch (colour.toLowerCase()) {
            case "blue":
                framesToPlay = blueDeadDuckFrames;
                break;
            case "pink":
                framesToPlay = pinkDeadDuckFrames;
                break;
            default:
                framesToPlay = deadDuckFrames;
                break;
        }
    
        // If an animation is already running, stop it and clear its key frames
        if (duckAnimation != null) {
            duckAnimation.stop();
            duckAnimation.getKeyFrames().clear();
        }
    
        final int[] frameIndex = {0}; // Index to keep track of the current frame
        // Add a new key frame to update the duck image every 200 milliseconds
        duckAnimation.getKeyFrames().add(new KeyFrame(
                Duration.millis(200),
                e -> {
                    // Play each frame until the sequence is complete
                    if (frameIndex[0] < framesToPlay.length) {
                        duckImageView.setImage(new Image(framesToPlay[frameIndex[0]], 200, 200, true, true));
                        frameIndex[0]++;
                    }
                }
        ));
        // Set the cycle count to the number of frames so the animation plays once
        duckAnimation.setCycleCount(framesToPlay.length);
        // When finished, ensure the final frame is explicitly set
        duckAnimation.setOnFinished(event -> {
            duckImageView.setImage(new Image(framesToPlay[framesToPlay.length - 1], 200, 200, true, true));
        });
        // Start the dead animation
        duckAnimation.play();
    }

    /**
     * Constructs a new Duck instance with the specified name.
     * <p>
     * Initializes default attributes, configures the image view, and sets up the initial animation based on colour.
     * </p>
     *
     * @param name the name of the duck
     */
    public Duck(String name) {
        this.name = name;
        this.health = 10;
        this.hunger = 10;
        this.happiness = 100;
        this.tiredness = 0;
        this.score = 0;
        // Ensure numCoins is initialized to zero if not set externally
        this.numCoins = 0;
        this.dayCounter = 0;
        this.isDead = false;
        this.colour = "white"; // Default colour; can be updated later

        // Initialize the ImageView and configure its properties
        this.duckImageView = new ImageView();
        this.duckImageView.setFitWidth(200);
        this.duckImageView.setFitHeight(200);
        this.duckImageView.setPreserveRatio(true);
        this.duckImageView.setSmooth(true);
        // Adjust vertical translation for display purposes
        this.duckImageView.setTranslateY(80);
    
        // Initialize the animation Timeline and set the initial animation based on colour
        this.duckAnimation = new Timeline();
        updateAnimationByColour();

        // For JUnit Testing; pulseTimer
        this.pulseTimer = new com.sun.glass.ui.Timer(() -> {}) {
            @Override
            protected long _start(Runnable runnable, int priority) { return 0L; }
            @Override
            protected long _start(Runnable runnable) { return 0L; }
            @Override
            protected void _stop(long time) { }
            @Override
            protected void _pause(long time) { }
            @Override
            protected void _resume(long time) { }
        };
        initializePulseTimer();
    }
    
    /**
     * Updates the default animation of the duck based on its current colour.
     * <p>
     * Selects the appropriate set of animation frames (blue, pink, or default white) and updates the animation.
     * </p>
     */
    private void updateAnimationByColour() {
        switch (colour.toLowerCase()) {
            case "blue":
                updateDuckAnimation(defaultBlueDuckFrames);
                break;
            case "pink":
                updateDuckAnimation(defaulPinkDuckFrames);
                break;
            default:
                updateDuckAnimation(defaultDuckFrames);
                break;
        }
    }

    /**
     * Starts the animation sequence for when the duck is receiving a gift.
     * <p>
     * The animation plays twice using the frames corresponding to the duck's colour and then reverts back to the default animation.
     * </p>
     */
    public void startReceivingGiftAnimation() {
        String[] framesToPlay;
        // Select gift-receiving frames based on duck colour
        switch (colour.toLowerCase()) {
            case "blue":
                framesToPlay = blueReceivedGiftDuckFrames;
                break;
            case "pink":
                framesToPlay = pinkReceivedGiftDuckFrames;
                break;
            default:
                framesToPlay = receivedGiftDuckFrames;
                break;
        }
    
        // Stop and clear any existing animation before starting the gift animation
        if (duckAnimation != null) {
            duckAnimation.stop();
            duckAnimation.getKeyFrames().clear();
        }
    
        final int[] frameIndex = {0}; // Frame counter
        // Create a key frame to update the image every 200ms
        duckAnimation.getKeyFrames().add(new KeyFrame(
                Duration.millis(200),
                e -> {
                    // Reset frame counter if it exceeds available frames
                    if (frameIndex[0] >= framesToPlay.length) frameIndex[0] = 0;
                    // Update the duck image to the current frame
                    duckImageView.setImage(new Image(framesToPlay[frameIndex[0]], 200, 200, true, true));
                    frameIndex[0]++;
                }
        ));
        // Play the animation twice (cycle count = frames * 2)
        duckAnimation.setCycleCount(framesToPlay.length * 2);
        // After finishing, revert to the default animation based on colour
        duckAnimation.setOnFinished(event -> updateAnimationByColour());
        duckAnimation.play();
    }

    /**
     * Starts the eating animation sequence for the duck.
     * <p>
     * The eating animation plays twice and then reverts back to the default animation.
     * </p>
     */
    public void startEatingAnimation() {
        String[] framesToPlay;
        // Choose eating animation frames based on duck colour
        switch (colour.toLowerCase()) {
            case "blue":
                framesToPlay = blueEatingDuckFrames;
                break;
            case "pink":
                framesToPlay = pinkEatingDuckFrames;
                break;
            default:
                framesToPlay = eatingDuckFrames;
                break;
        }
    
        // Stop any currently running animation
        if (duckAnimation != null) {
            duckAnimation.stop();
            duckAnimation.getKeyFrames().clear();
        }
    
        final int[] frameIndex = {0}; // Initialize frame counter
        // Add key frame to update duck image every 200ms during eating animation
        duckAnimation.getKeyFrames().add(new KeyFrame(
                Duration.millis(200),
                e -> {
                    if (frameIndex[0] >= framesToPlay.length) frameIndex[0] = 0;
                    duckImageView.setImage(new Image(framesToPlay[frameIndex[0]], 200, 200, true, true));
                    frameIndex[0]++;
                }
        ));
        // Set cycle count to play animation twice
        duckAnimation.setCycleCount(framesToPlay.length * 2);
        // Once finished, revert back to the default animation
        duckAnimation.setOnFinished(event -> updateAnimationByColour());
        duckAnimation.play();
    }

    /**
     * Starts the sleeping animation sequence for the duck.
     * <p>
     * Plays the sleeping animation once and then reverts back to the default animation.
     * </p>
     */
    public void startSleepingAnimation() {
        String[] framesToPlay;
        // Select sleeping frames based on duck colour
        switch (colour.toLowerCase()) {
            case "blue":
                framesToPlay = blueSleepingDuckFrames;
                break;
            case "pink":
                framesToPlay = pinkSleepingDuckFrames;
                break;
            default:
                framesToPlay = sleepingDuckFrames;
                break;
        }
    
        // Stop and clear any current animation
        if (duckAnimation != null) {
            duckAnimation.stop();
            duckAnimation.getKeyFrames().clear();
        }
    
        final int[] frameIndex = {0}; // Frame counter initialization
        // Create key frame that updates the duck image every 200ms
        duckAnimation.getKeyFrames().add(new KeyFrame(
                Duration.millis(200),
                e -> {
                    if (frameIndex[0] >= framesToPlay.length) frameIndex[0] = 0;
                    duckImageView.setImage(new Image(framesToPlay[frameIndex[0]], 200, 200, true, true));
                    frameIndex[0]++;
                }
        ));
        // Set the animation to play one full cycle
        duckAnimation.setCycleCount(framesToPlay.length);
        // Revert to default animation once sleeping animation is finished
        duckAnimation.setOnFinished(event -> updateAnimationByColour());
        duckAnimation.play();
    }
    
    /**
     * Retrieves the ImageView displaying the duck.
     *
     * @return the ImageView associated with the duck
     */
    public ImageView getDuckImageView() {
        return duckImageView;
    }

    /**
     * Starts the walking animation sequence for the duck.
     * <p>
     * Chooses the walking animation frames based on the duck's colour.
     * </p>
     */
    public void startWalkingAnimation() {
        // Select walking frames based on the duck's colour and update the animation
        switch (colour.toLowerCase()) {
            case "blue":
                updateDuckAnimation(blueWalkingDuckFrames);
                break;
            case "pink":
                updateDuckAnimation(pinkWalkingDuckFrames);
                break;
            default:
                updateDuckAnimation(walkingDuckFrames);
                break;
        }
    }
    
    /**
     * Stops the walking animation and reverts to the idle (default) animation.
     */
    public void stopWalkingAnimation() {
        // Revert to the default idle animation based on the duck's colour
        updateAnimationByColour();
    }
    
    /**
     * Updates the duck's animation to cycle through the provided frames.
     * <p>
     * Stops any running animation, clears previous key frames, and sets up a new animation
     * that cycles indefinitely through the given frames.
     * </p>
     *
     * @param frames an array of file paths representing the animation frames
     */
    private void updateDuckAnimation(String[] frames) {
        if (duckAnimation != null) {
            duckAnimation.stop();
            duckAnimation.getKeyFrames().clear();
        }
        final int[] frameIndex = {0};
        duckAnimation.getKeyFrames().add(new KeyFrame(
            Duration.millis(200),
            e -> {
                if (frameIndex[0] >= frames.length) {
                    frameIndex[0] = 0;
                }
                duckImageView.setImage(new Image(frames[frameIndex[0]], 200, 200, true, true));
                frameIndex[0]++;
            }
        ));
        duckAnimation.setCycleCount(Timeline.INDEFINITE);
        duckAnimation.play();
    }

    /**
     * Returns the current hunger level of the duck.
     *
     * @return the hunger value
     */
    public int getHunger() {
        return hunger;
    }

    /**
     * Returns the current happiness level of the duck.
     *
     * @return the happiness value
     */
    public int getHappiness() {
        return happiness;
    }

    /**
     * Returns the current tiredness (sleep level) of the duck.
     *
     * @return the tiredness value
     */
    public int getTiredness() {
        return tiredness;
    }

    /**
     * Returns the current health of the duck.
     *
     * @return the health value
     */
    public int getHealth() {
        return health;
    }

    /**
     * Returns the current score of the duck.
     *
     * @return the score
     */
    public int getScore() {
        return score;
    }
    
    /**
     * Returns the current number of coins the duck has.
     *
     * @return the coin count
     */
    public int getCoins() {
        return numCoins;
    }

    /**
     * Returns the name of the duck.
     *
     * @return the duck's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the current day counter.
     *
     * @return the day count
     */
    public int getDay() {
        return dayCounter;
    }

    /**
     * Checks if the duck is angry.
     * <p>
     * A duck is considered angry if its happiness is below 25.
     * </p>
     *
     * @return true if the duck is angry, false otherwise
     */
    public boolean isAngry() {
        return happiness < 25;
    }
    
    /**
     * Checks whether the duck is dead.
     *
     * @return true if the duck is dead, false otherwise
     */
    public Boolean isDead() {
        return isDead;
    }
    
    /**
     * Retrieves the current colour of the duck.
     *
     * @return the duck's colour
     */
    public String getColour(){
        return colour;
    }
    
    /**
     * Sets the duck's colour and updates its default animation accordingly.
     *
     * @param change the new colour for the duck
     */
    public void setColour(String change) {
        this.colour = change;
        updateAnimationByColour(); // Immediately update the animation to reflect the new colour
    }
    
    /**
     * Sets the dead state of the duck.
     *
     * @param dead true if the duck should be marked as dead, false otherwise
     */
    public void setDead(boolean dead) {
        isDead = dead;
    }

    /**
     * Adjusts the duck's health by a given amount.
     * <p>
     * If the health falls to 0 or below (and the duck is not already dead), the living state is flipped
     * and health is set to 0.
     * </p>
     *
     * @param change the amount by which to adjust health (can be negative)
     */
    public void setHealth(int change) {
        health += change;

        // If health drops to or below 0 and duck is not yet dead, flip the living state
        if (health <= 0 && !isDead) {
            flipLivingState();
            health = 0;
        }
    }

    /**
     * Adjusts the duck's hunger level by a given amount.
     * <p>
     * The hunger level is constrained between 0 and 10.
     * </p>
     *
     * @param change the amount to adjust hunger (can be negative)
     */
    public void setHunger(int change) {
        if(hunger + change < 0) {
            hunger = 0;
        } else if(hunger + change > 10) {
            hunger = 10;
        } else {
            hunger += change;
        }
    }

    /**
     * Adjusts the duck's happiness level by a given amount.
     * <p>
     * Ensures that the happiness level remains between 0 and 100.
     * </p>
     *
     * @param change the amount to adjust happiness (can be negative)
     */
    public void setHappiness(int change) {
        if(happiness + change > 100) {
            happiness = 100;
        } else if(happiness + change < 0) {
            happiness = 0;
        } else {
            happiness += change;
        }
    }

    /**
     * Adjusts the duck's tiredness (sleep level) by a given amount.
     * <p>
     * The tiredness level is constrained between 0 and 100.
     * </p>
     *
     * @param change the amount to adjust tiredness (can be negative)
     */
    public void setTiredness(int change) {
        if(tiredness + change > 100) {
            tiredness = 100;
        } else if(tiredness + change < 0) {
            tiredness = 0;
        } else {
            tiredness += change;
        }
    }
    
    /**
     * Adjusts the duck's coin count by a given amount.
     *
     * @param change the amount to adjust coins (can be negative)
     */
    public void setCoins(int change) {
        numCoins += change;
    }

    /**
     * Adjusts the duck's score by a given amount.
     * <p>
     * Ensures that the score does not drop below 0.
     * </p>
     *
     * @param change the amount to adjust the score (can be negative)
     */
    public void setScore(int change) {
        if (score + change < 0) {
            score = 0;
        } else {
            score += change;
        }
    }
    
    /**
     * Increments the day counter by one.
     */
    public void increaseDay() {
        dayCounter++;
    }
    
    /**
     * Sets the day counter to a specified value.
     *
     * @param num the new day count to set
     */
    public void setDay(int num) {
        dayCounter = num;
    }

    /**
     * Flips the living state of the duck.
     * <p>
     * If the duck's health is 0, its health is reset to 5 before flipping the state.
     * Also resets the dead animation flag.
     * </p>
     */
    public void flipLivingState() {
        if (health == 0) {
            health = 5;
        }
        isDead = !isDead;
        isDeadAnimationPlayed = false; // Reset flag so dead animation can play again if needed
    }

    /**
     * Prints the duck's current stats to the console.
     */
    public void printStats() {
        // Print each attribute of the duck to the console for debugging or informational purposes
        System.out.println("Name: " + name);
        System.out.println("Health: " + health);
        System.out.println("Hunger: " + hunger);
        System.out.println("Sleep: " + tiredness);
        System.out.println("Happiness: " + happiness);
        System.out.println("Dead?: " + isDead);
        System.out.println("Colour: " + colour);
    }
    
    public synchronized void ensureTimerInitialized() {
        if (pulseTimer == null) {
            pulseTimer = new com.sun.glass.ui.Timer(() -> {}) {
                @Override protected long _start(Runnable r, int p) { return 0L; }
                @Override protected long _start(Runnable r) { return 0L; }
                @Override protected void _stop(long t) {}
                @Override protected void _pause(long t) {}
                @Override protected void _resume(long t) {}
            };
        }
    }
    protected void initializePulseTimer() {
        if (pulseTimer == null) {
            pulseTimer = new com.sun.glass.ui.Timer(() -> {}) {
                @Override protected long _start(Runnable r, int p) { return 0L; }
                @Override protected long _start(Runnable r) { return 0L; }
                @Override protected void _stop(long t) {}
                @Override protected void _pause(long t) {}
                @Override protected void _resume(long t) {}
            };
        }
    }
    // Modify the readObject method to ensure timer initialization:
    private void readObject(java.io.ObjectInputStream ois) throws java.io.IOException, ClassNotFoundException {
        ois.defaultReadObject();
        ensureTimerInitialized();
    }
}
