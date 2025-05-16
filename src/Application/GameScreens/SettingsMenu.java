package Application.GameScreens;

import Application.Duck;
import Application.GameScreens.Tutorial.Tutorial1;
import Application.GameSettingsAndStats;
import Application.Inventory;
import Application.Main;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * The SettingsMenu class provides a user interface for configuring various settings in the game.
 * <br><br>
 * This menu allows the user to access the tutorial, adjust audio settings,
 * manage parental controls, and close the settings menu to return to the previous screen
 * or the main menu.
 */
public class SettingsMenu {
    private Scene scene;

    /**
     * Constructs a SettingsMenu screen using the provided Main, Duck, Inventory,
     * and GameSettingsAndStats instances. This constructor initializes and styles
     * the menu buttons, sets their actions for navigation, applies a background image,
     * and creates the final scene.
     *
     * @param main     The main application instance used for scene management and state updates.
     * @param duck     The current Duck instance.
     * @param inventory The current Inventory instance.
     * @param settings  The current GameSettingsAndStats instance.
     */
    public SettingsMenu(Main main, Duck duck, Inventory inventory, GameSettingsAndStats settings) {

        // Set a common style for all buttons in the settings menu.
        String buttonStyle = "-fx-font-size: 16px; " +
                             "-fx-background-color: #0078d7; " +
                             "-fx-text-fill: white; " +
                             "-fx-background-radius: 10; " +
                             "-fx-padding: 10 20; " +
                             "-fx-font-weight: bold; " +
                             "-fx-pref-width: 200px; " + // Fixed width for all buttons
                             "-fx-alignment: center;";   // Center-align text

        // Create a button to launch the tutorial.
        Button tutorialButton = new Button("Tutorial");
        tutorialButton.setStyle(buttonStyle);
        tutorialButton.setOnAction(e -> {
            // Navigate to the Tutorial screen with required arguments.
            Tutorial1 tutorial = new Tutorial1(main, duck, inventory, settings);
            main.setScene(tutorial.getScene());
        });

        // Create a button to open the audio settings menu.
        Button audioButton = new Button("Audio");
        audioButton.setStyle(buttonStyle);
        audioButton.setOnAction(e -> {
            // Navigate to the AudioMenu screen and pass the current SettingsMenu instance.
            AudioMenu audioMenu = new AudioMenu(main, this, duck, inventory, settings);
            main.setScene(audioMenu.getScene());
        });

        // Create a button to access parental controls.
        Button parentalControlsButton = new Button("Parental Controls");
        parentalControlsButton.setStyle(buttonStyle);
        parentalControlsButton.setOnAction(e -> {
            // Navigate to the Parental Controls Sign-In screen.
            ParentalControls parentalControls = new ParentalControls(main, this, duck, inventory, settings);
            main.setScene(parentalControls.getSignInScene());
        });

        // Create a button to close the settings menu and return to the previous screen or main menu.
        Button closeButton = new Button("Close");
        closeButton.setStyle(buttonStyle);
        closeButton.setOnAction(e -> {
            System.out.println("Returning to the previous screen...");
            if (main.getPreviousScene() != null) {
                // Resume the previous scene if it exists.
            	System.out.println("Not null Scene");
                main.resumePreviousScene();
            } else {
                // Otherwise, navigate to the Main Menu.
                System.out.println("No previous scene found. Redirecting to Main Menu...");
                MainMenu mainMenu = new MainMenu(main, duck, inventory, settings);
                main.setScene(mainMenu.getScene());
            }
        });

        // Create a VBox layout to arrange buttons vertically with 20px spacing.
        VBox buttonBox = new VBox(20, tutorialButton, audioButton, parentalControlsButton, closeButton);
        // Center-align the buttons and add padding.
        buttonBox.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Load the background image for the settings menu.
        Image backgroundImage = new Image("file:src/assets/pausemenu.png");
        // Create a BackgroundImage with no-repeat settings and center position.
        BackgroundImage background = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );

        // Create a BorderPane layout, apply the background image, and center the buttonBox.
        BorderPane layout = new BorderPane();
        layout.setBackground(new Background(background));
        layout.setCenter(buttonBox);

        // Initialize the scene with the layout and fixed dimensions.
        this.scene = new Scene(layout, 800, 600);
    }

    /**
     * Returns the JavaFX Scene representing the SettingsMenu.
     *
     * @return The scene containing the settings menu interface.
     */
    public Scene getScene() {
        return scene;
    }
}
