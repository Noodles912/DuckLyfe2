package Application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Manages the graphical user interface for the game.
 * <p>
 * The UserInterface class is responsible for constructing and updating the game's UI elements,
 * including the pet's health, hunger, score, and coin display. It arranges various labels and image containers
 * in a JavaFX scene and provides methods to update these UI components in response to game events.
 * </p>
 *
 * @author Daniel Harapiak
 * @author Brandon Nguyen
 * @author Dinith Nawaratne
 * @author Zachary Goodman
 * @author Atomm Li
 */
public class UserInterface {
    // The main scene that contains the user interface.
    private Scene scene;
    // The pet (Duck) whose status is displayed.
    private final Duck pet;
    // Labels for displaying money, happiness, tiredness, and score.
    private Label moneyLabel;
    private Label happinessLabel;
    private Label tirednessLabel;
    private Label scoreLabel;

    // Images representing full and empty heart icons for health.
    private final Image heartFull = new Image("file:src/assets/heartFull.png");
    private final Image heartEmpty = new Image("file:src/assets/heartEmpty.png");
    // Images representing full and empty hunger icons.
    private final Image hungerFull = new Image("file:src/assets/hungerFull.png");
    private final Image hungerEmpty = new Image("file:src/assets/hungerEmpty.png");

    // Containers to hold the heart and hunger icons.
    private final HBox heartsContainer = new HBox(0);
    private final HBox hungerContainer = new HBox(0);

    /**
     * Constructs the UserInterface with the provided game settings, pet, and coin label.
     * <p>
     * This constructor initializes all UI components, configures styles and effects,
     * arranges them into a layout, and creates the Scene.
     * </p>
     *
     * @param settings  the GameSettingsAndStats object (currently used for layout purposes)
     * @param pet       the Duck object representing the pet whose stats are shown
     * @param coinLabel the Label used to display the current coin count
     */
    public UserInterface(GameSettingsAndStats settings, Duck pet, Label coinLabel) {
        // Initialize pet and coin display label.
        this.pet = pet;
        this.moneyLabel = coinLabel;

        // Configure the hearts and hunger containers alignment.
        heartsContainer.setAlignment(Pos.CENTER_RIGHT);
        hungerContainer.setAlignment(Pos.CENTER_RIGHT);

        // Create a drop shadow effect for text to improve readability.
        DropShadow textOutline = new DropShadow();
        textOutline.setColor(Color.BLACK);
        textOutline.setRadius(2);
        textOutline.setSpread(0.8);

        // Create and style the happiness label using the pet's current happiness value.
        happinessLabel = new Label("Happiness: " + pet.getHappiness());
        happinessLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: rgb(255, 112, 112);");
        happinessLabel.setEffect(textOutline); // Apply text effect

        // Create and style the tiredness label using the pet's current tiredness value.
        tirednessLabel = new Label("Tiredness: " + pet.getTiredness());
        tirednessLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: rgb(255, 112, 112);");
        tirednessLabel.setEffect(textOutline); // Apply text effect

        // Create a vertical box (VBox) to hold the health and hunger information.
        VBox healthHungerBar = new VBox(0, happinessLabel, tirednessLabel, heartsContainer, hungerContainer);
        healthHungerBar.setAlignment(Pos.CENTER_RIGHT);
        healthHungerBar.setStyle("-fx-padding: 0 20 50 0;");

        // Create and style the score label using the pet's current score.
        scoreLabel = new Label("Score: " + pet.getScore());
        scoreLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: black; -fx-font-weight: bold;");

        // Create a vertical box to hold the coin display and score.
        VBox statsCounter = new VBox(moneyLabel, scoreLabel);
        statsCounter.setAlignment(Pos.TOP_RIGHT);
        statsCounter.setStyle("-fx-padding: 25;");

        // Create the main layout using a BorderPane.
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: transparent;");
        // Place the health and hunger bar at the bottom-right of the layout.
        layout.setBottom(healthHungerBar);
        BorderPane.setAlignment(healthHungerBar, Pos.BOTTOM_RIGHT);

        // Create a top pane to hold the stats counter.
        BorderPane topPane = new BorderPane();
        topPane.setRight(statsCounter);
        layout.setTop(topPane);

        // Update the UI icons for health and hunger.
        updateHealth();
        updateHunger();

        // Create the Scene with the constructed layout.
        this.scene = new Scene(layout, 800, 600);
    }

    /**
     * Retrieves the JavaFX Scene representing the user interface.
     *
     * @return the Scene containing the UI elements
     */
    public Scene getScene() {
        // Return the constructed Scene.
        return scene;
    }

    /**
     * Updates the health display by modifying the hearts container.
     * <p>
     * This method clears the current hearts and adds 10 ImageViews,
     * using a full heart image for each health point and an empty heart image for the remainder.
     * </p>
     */
    public void updateHealth() {
        // Clear any existing heart icons.
        heartsContainer.getChildren().clear();
        // Loop through 10 positions to represent health.
        for (int i = 0; i < 10; i++) {
            // Use a full heart if the index is less than the pet's health; otherwise, use an empty heart.
            ImageView heart = new ImageView(i < pet.getHealth() ? heartFull : heartEmpty);
            heart.setFitWidth(25);
            heart.setFitHeight(25);
            // Add the heart icon to the container.
            heartsContainer.getChildren().add(heart);
        }
    }

    /**
     * Updates the hunger display by modifying the hunger container.
     * <p>
     * This method clears the current hunger icons and adds 10 ImageViews,
     * using a full hunger image for each hunger point and an empty hunger image for the remainder.
     * </p>
     */
    public void updateHunger() {
        // Clear any existing hunger icons.
        hungerContainer.getChildren().clear();
        // Loop through 10 positions to represent hunger.
        for (int i = 0; i < 10; i++) {
            // Use a full hunger image if the index is less than the pet's hunger; otherwise, use an empty hunger image.
            ImageView hunger = new ImageView(i < pet.getHunger() ? hungerFull : hungerEmpty);
            hunger.setFitWidth(25);
            hunger.setFitHeight(25);
            // Add the hunger icon to the container.
            hungerContainer.getChildren().add(hunger);
        }
    }

    /**
     * Updates the score display to reflect the pet's current score.
     */
    public void updateScore() {
        // Set the score label text using the pet's current score.
        scoreLabel.setText("Score: " + pet.getScore());
    }

    /**
     * Updates the coin display to reflect the pet's current coin count.
     */
    public void updateCoinDisplay() {
        // Update the coin label with the current coin count.
        moneyLabel.setText(" x " + pet.getCoins());
    }

    /**
     * Updates the statistics display (happiness and tiredness) to reflect the pet's current state.
     */
    public void updateStatsDisplay() {
        // Update the happiness label with the pet's current happiness.
        happinessLabel.setText("Happiness: " + pet.getHappiness());
        // Update the tiredness label with the pet's current tiredness.
        tirednessLabel.setText("Tiredness: " + pet.getTiredness());
    }

    /**
     * Retrieves the Label used for displaying coins.
     *
     * @return the coin display Label
     */
    public Label getCoinLabel() {
        // Return the moneyLabel reference.
        return moneyLabel;
    }

    /**
    * Retrieves the Label used for displaying score.
    *
    * @return the coin display Label
    */
    public Label getScoreLabel() {
        return scoreLabel;
    }
}
