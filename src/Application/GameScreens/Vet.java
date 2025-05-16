package Application.GameScreens;

import Application.Components.PauseButton;
import Application.Duck;
import Application.GameSettingsAndStats;
import Application.Inventory;
import Application.Main;
import Application.UserInterface;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * The Vet class represents the veterinary screen within the game where players can heal their pet.
 * <br><br>
 * The screen provides a heal button that deducts coins and restores the pet's
 * health if conditions are met, displays a popup message for feedback, and includes navigation
 * elements such as a pause button, a home button, and a user interface overlay.
 *
 * @author Dinith Nawaratne
 * @author Daniel Harapiak
 * @author Brandon Nguyen
 */
public class Vet {
    private Scene scene;

    /**
     * Constructs the Vet screen, initializing the UI elements and their corresponding actions.
     * This includes displaying the pet's health, coin counter, popup messages, and handling the
     * healing process. It also sets up the background, navigation buttons, and overlays the
     * user interface.
     *
     * @param main The main application instance used for scene management and global state updates.
     */
    public Vet(Main main) {
        // Retrieve the current game state objects.
        Duck pet = main.getDuck();
        Inventory inventory = main.getInventory();
        GameSettingsAndStats settings = main.getSettings();

        // Initialize and start the pet's stats tracking.
        main.initializeStatsTracking(pet);
        main.startStatsTracking();

        // Create a popup label for feedback messages (e.g., "Pet healed!").
        Label popupLabel = new Label();
        popupLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75); " +
                              "-fx-text-fill: white; -fx-font-size: 18px; " +
                              "-fx-padding: 10 20; -fx-background-radius: 10;");
        popupLabel.setVisible(false);       // Initially hidden.
        popupLabel.setId("popupLabel");       // Set an ID for potential styling.
        popupLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        // Position the popup label at the top center of the screen.
        StackPane.setAlignment(popupLabel, javafx.geometry.Pos.TOP_CENTER);
        popupLabel.setTranslateY(50);

        // Create a shared pause transition to hide the popup after 2 seconds.
        PauseTransition popupDelay = new PauseTransition(Duration.seconds(2));
        popupDelay.setOnFinished(event -> popupLabel.setVisible(false));

        // Create a heal button with text and a coin icon.
        Button healButton = new Button("Restore (H)ealth: 20 x");
        ImageView coinIcon = new ImageView(new Image("file:src/assets/coinFront.png"));
        coinIcon.setFitWidth(16);
        coinIcon.setFitHeight(16);
        // Set the coin icon to appear on the right side of the text.
        healButton.setGraphic(coinIcon);
        healButton.setContentDisplay(javafx.scene.control.ContentDisplay.RIGHT);

        // Initialize the user interface overlay (e.g., coin counter, stats display).
        UserInterface userInterface = new UserInterface(settings, pet, main.getCoinLabel());

        // When clicked, the heal button will attempt to restore the pet's health.
        healButton.setOnAction(e -> {
            // Check if the pet has enough coins and is not fully healed.
            if (pet.getCoins() >= 20 && pet.getHealth() < 10) {
                pet.setCoins(-20);  // Deduct 20 coins.
                int healAmount = 10 - pet.getHealth(); // Calculate the amount needed to fully heal.
                pet.setHealth(healAmount); // Restore health.
                // Update the coin display and health stats in the UI.
                userInterface.updateCoinDisplay();
                userInterface.updateHealth();
                popupLabel.setText("Pet healed!");
            } else if (pet.getHealth() >= 10) {
                popupLabel.setText("Pet is already fully healed!");
            } else {
                popupLabel.setText("Not enough money to heal!");
            }
            // Display the popup message and restart the timer.
            popupLabel.setVisible(true);
            popupDelay.playFromStart();
        });

        // Update the main UI with the current user interface settings.
        main.updateUserInterface(userInterface);

        // Load the background image for the Vet screen.
        BackgroundImage background = new BackgroundImage(
            new Image("file:src/assets/vet.png"),
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );

        // Create the main layout using a BorderPane and set its background.
        BorderPane layout = new BorderPane();
        layout.setBackground(new Background(background));

        // Create and place the pause button at the top of the screen.
        HBox pauseBox = new HBox(new PauseButton(main, pet, inventory, settings));
        layout.setTop(pauseBox);

        // Style the heal button and place it in a VBox for layout positioning.
        healButton.setStyle("-fx-font-size: 16px; -fx-background-color: rgb(255, 248, 59); " +
                            "-fx-text-fill: black; -fx-background-radius: 10; -fx-padding: 10 20;");
        VBox healBox = new VBox(20, healButton);
        // Align the heal box to the left with specified padding.
        healBox.setStyle("-fx-alignment: center-left; -fx-padding: 400 0 80 50;");
        layout.setLeft(healBox);

        // Create a StackPane to overlay the user interface elements on top of the background.
        StackPane uiLayer = new StackPane(userInterface.getScene().getRoot());
        // Position the UI overlay at the bottom-right.
        StackPane.setAlignment(userInterface.getScene().getRoot(), javafx.geometry.Pos.BOTTOM_RIGHT);
        uiLayer.setMouseTransparent(true);  // Allow clicks to pass through.

        // Create a Home button to return to the main Home screen.
        Button homeButton = new Button("Home");
        homeButton.setStyle("-fx-font-size: 16px; -fx-background-color: rgb(135, 206, 250); " +
                            "-fx-text-fill: black; -fx-background-radius: 10; -fx-padding: 20 40;");
        homeButton.setOnAction(e -> {
            // Stop the stats tracking and navigate to the Home screen.
            main.stopStatsTracking(pet);
            main.setScene(new Home(main, pet, inventory, settings).getScene());
        });
        // Position the Home button at the bottom-left.
        StackPane.setAlignment(homeButton, javafx.geometry.Pos.BOTTOM_LEFT);
        homeButton.setTranslateX(15);
        homeButton.setTranslateY(-15);

        // Combine all layout components into a single StackPane.
        StackPane vetScreen = new StackPane(layout, uiLayer, homeButton, popupLabel);
        // Create the final scene with the assembled layout.
        this.scene = new Scene(vetScreen, 800, 600);
        // Attach this Vet instance as user data to the scene.
        this.scene.setUserData(this);
    }

    /**
     * Retrieves the JavaFX Scene representing the Vet screen.
     *
     * @return The scene containing the Vet interface.
     */
    public Scene getScene() {
        return scene;
    }
}
