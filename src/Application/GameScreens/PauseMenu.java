package Application.GameScreens;

import Application.Duck;
import Application.GameSettingsAndStats;
import Application.Inventory;
import Application.Main;
import Application.SaveLoad;
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
 * The PauseMenu class creates a pause screen for the game.
 * <br><br>
 * This screen provides buttons to resume the game, return to the main menu,
 * or navigate to the settings menu. It also sets a custom background image
 * and applies a consistent style to all buttons.
 *
 * @author Daniel Harapiak
 * @author Atomm Li
 */
public class PauseMenu {
    private Scene scene;

    /**
     * Constructs a PauseMenu screen with resume, main menu, and settings buttons.
     * Each button has an associated action to control the game's state.
     *
     * @param main     The main application instance for managing scenes and game state.
     * @param duck     The current Duck instance.
     * @param inventory The current Inventory instance.
     * @param settings  The current GameSettingsAndStats instance.
     */
    public PauseMenu(Main main, Duck duck, Inventory inventory, GameSettingsAndStats settings) {

        // Create the Resume, Main Menu, and Settings buttons.
        Button resumeButton = new Button("Resume");
        Button mainMenuButton = new Button("Main Menu");
        Button settingsButton = new Button("Settings");

        // Resume Button: Resumes the game by restarting tracking and restoring the previous scene.
        resumeButton.setOnAction(e -> {
            System.out.println("Resuming previous scene...");
            main.startPlaytimeTracking(settings);
            main.startStatsTracking();
            main.resumePreviousScene(); // Restore the previous scene
        });

        // Main Menu Button: Loads updated game data, applies parental lock if needed, and navigates to the main menu.
        mainMenuButton.setOnAction(e -> {
        	
            SaveLoad saveLoad = new SaveLoad();
            saveLoad.saveAllData(duck, inventory, settings);

        	
        	Duck updatedDuck = duck;
        	Inventory updatedInventory = inventory;
        	GameSettingsAndStats updatedSettings = settings;

            main.stopCoinGeneration();

            // Create a new MainMenu instance using the reloaded data.
            MainMenu mainMenu = new MainMenu(main, updatedDuck, updatedInventory, updatedSettings);

            // Enforce parental lock by disabling specific buttons if necessary.
            if (updatedSettings.isParentalLockActive()) {
                System.out.println("Parental lock is active. Disabling buttons in MainMenu.");
                mainMenu.getScene().getRoot().lookupAll(".button").forEach(node -> {
                    if (node instanceof Button) {
                        Button button = (Button) node;
                        if (button.getText().equals("Continue") || button.getText().equals("New Game")) {
                            button.setDisable(true);
                            button.setStyle("-fx-font-size: 16px; " +
                                            "-fx-background-color: #d3d3d3; " +
                                            "-fx-text-fill: #a9a9a9; " +
                                            "-fx-background-radius: 10; " +
                                            "-fx-padding: 10 20; " +
                                            "-fx-font-weight: bold; " +
                                            "-fx-pref-width: 200px; " +
                                            "-fx-alignment: center;");
                        }
                    }
                });
            }

            // Navigate to the MainMenu scene.
            main.setScene(mainMenu.getScene());
        });

        // Settings Button: Navigates the user to the Settings Menu.
        settingsButton.setOnAction(e -> {
            System.out.println("Navigating to Settings Menu...");
            SettingsMenu settingsMenu = new SettingsMenu(main, duck, inventory, settings);
            main.setScene(settingsMenu.getScene()); // Navigate to Settings Menu
        });

        // Define a common style for all pause menu buttons.
        String buttonStyle = "-fx-font-size: 16px; " +
                             "-fx-background-color: #0078d7; " +
                             "-fx-text-fill: white; " +
                             "-fx-background-radius: 10; " +
                             "-fx-padding: 10 20; " +
                             "-fx-font-weight: bold; " +
                             "-fx-pref-width: 200px; " + // Fixed width for all buttons
                             "-fx-alignment: center;";   // Center-align text
        resumeButton.setStyle(buttonStyle);
        mainMenuButton.setStyle(buttonStyle);
        settingsButton.setStyle(buttonStyle);

        // Create a VBox to vertically stack the buttons with 20px spacing.
        VBox buttonBox = new VBox(20); // VBox with 20px spacing between buttons
        buttonBox.getChildren().addAll(resumeButton, mainMenuButton, settingsButton);
        buttonBox.setStyle("-fx-alignment: center;"); // Center-align the VBox

        // Load the background image for the pause menu.
        Image backgroundImage = new Image("file:src/assets/pauseMenu.png");
        BackgroundImage background = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );

        // Create a BorderPane layout, set the background image, and place the button box in the center.
        BorderPane layout = new BorderPane();
        layout.setBackground(new Background(background));
        layout.setCenter(buttonBox);

        // Create the final scene with the configured layout and fixed dimensions.
        this.scene = new Scene(layout, 800, 600);
    }

    /**
     * Returns the JavaFX Scene representing the PauseMenu.
     *
     * @return The scene containing the pause menu interface.
     */
    public Scene getScene() {
        return scene;
    }
}
