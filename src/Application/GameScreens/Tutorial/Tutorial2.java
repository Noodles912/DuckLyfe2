package Application.GameScreens.Tutorial;

import Application.Duck;
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
 * Represents the second tutorial screen in the DuckLyfe2 application.
 * <br><br>
 * This class provides the layout and functionality for the second tutorial screen,
 * including navigation to the previous and next tutorial screens.
 * <br><br>
 * The screen includes a background image and navigation buttons for user interaction.
 * The background image dynamically adjusts to the size of the application window.
 */
public class Tutorial2 {
    private Scene scene;

    /**
     * Constructs the Tutorial2 screen and initializes its UI components.
     * <br><br>
     * This constructor sets up the navigation buttons, background image, and layout
     * for the second tutorial screen.
     *
     * @param main      The main application instance.
     * @param duck      The current duck instance.
     * @param inventory The player's inventory.
     * @param settings  The game settings and statistics.
     */
    public Tutorial2(Main main, Duck duck, Inventory inventory, GameSettingsAndStats settings) {
        // Create navigation buttons
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            Tutorial1 tutorial1 = new Tutorial1(main, duck, inventory, settings);
            main.setScene(tutorial1.getScene());
        });

        Button nextButton = new Button("Next");
        nextButton.setOnAction(e -> {
            Tutorial3 tutorial3 = new Tutorial3(main, duck, inventory, settings);
            main.setScene(tutorial3.getScene());
        });

        HBox navigationButtons = new HBox(10, backButton, nextButton);
        navigationButtons.setStyle("-fx-alignment: center; -fx-padding: 30;"); // Added padding

        // Create the background image
        Image backgroundImage = new Image("file:src/assets/tutorialScreens/tutorial2.png");
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
     * Retrieves the scene associated with the second tutorial screen.
     * <br><br>
     * This method returns the JavaFX Scene object that represents the second tutorial screen.
     *
     * @return The Scene object for the second tutorial screen.
     */

    public Scene getScene() {
        return scene;
    }
}