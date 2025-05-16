package Application.GameScreens.Tutorial;

import Application.Duck;
import Application.GameScreens.SettingsMenu;
import Application.GameSettingsAndStats;
import Application.Inventory;
import Application.Main;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * Represents the fourth tutorial screen in the DuckLyfe2 application.
 * <br><br>
 * This class provides the layout and functionality for the fourth tutorial screen,
 * including navigation to the previous tutorial screen and the settings menu.
 * <br><br>
 * The screen includes a background image and navigation buttons for user interaction.
 * The background image dynamically adjusts to the size of the application window.
 */
public class Tutorial4 {
    private Scene scene;

    /**
     * Constructs the Tutorial4 screen and initializes its UI components.
     * <br><br>
     * This constructor sets up the navigation buttons, background image, and layout
     * for the fourth tutorial screen.
     *
     * @param main      The main application instance.
     * @param duck      The current duck instance.
     * @param inventory The player's inventory.
     * @param settings  The game settings and statistics.
     */

    public Tutorial4(Main main, Duck duck, Inventory inventory, GameSettingsAndStats settings) {
        // Create navigation buttons
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            Tutorial3 tutorial3 = new Tutorial3(main, duck, inventory, settings);
            main.setScene(tutorial3.getScene());
        });

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> {
            SettingsMenu settingsMenu = new SettingsMenu(main, duck, inventory, settings);
            main.setScene(settingsMenu.getScene());
        });

        HBox navigationButtons = new HBox(10, backButton, exitButton);
        navigationButtons.setStyle("-fx-alignment: center; -fx-padding: 30;"); // Added padding

        // Create the background image
        Image backgroundImage = new Image("file:src/assets/tutorialScreens/tutorial4.png");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setPreserveRatio(false); // Allow stretching
        backgroundImageView.setSmooth(true); // Smooth scaling

        // Bind the background image size to the scene size
        backgroundImageView.fitWidthProperty().bind(main.getPrimaryStage().widthProperty());
        backgroundImageView.fitHeightProperty().bind(main.getPrimaryStage().heightProperty());

        // Create the layout
        BorderPane layout = new BorderPane();
        layout.setBottom(navigationButtons);

        // Use a StackPane to layer the background image and the layout
        StackPane root = new StackPane(backgroundImageView, layout);

        // Create the scene
        this.scene = new Scene(root, 800, 600);
    }

    /**
     * Retrieves the scene associated with the fourth tutorial screen.
     *
     * @return The scene of the fourth tutorial screen.
     */

    public Scene getScene() {
        return scene;
    }
}