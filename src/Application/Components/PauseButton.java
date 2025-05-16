package Application.Components;

import Application.Duck;
import Application.GameSettingsAndStats;
import Application.Inventory;
import Application.Main;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

/**
 * Represents a pause button component in the DuckLyfe2 application.
 * <br><br>
 * This button allows the user to pause the game and switch to the PauseMenu.
 * It also stops the tracking of game statistics when clicked.
 */

public class PauseButton extends StackPane {
        /**
     * Constructs a PauseButton and initializes its UI and functionality.
     * <br><br>
     * This constructor creates a styled button, adds it to the layout, and
     * sets up an action to switch to the PauseMenu when clicked.
     *
     * @param main      The main application instance.
     * @param duck      The current duck instance.
     * @param inventory The player's inventory.
     * @param settings  The game settings and statistics.
     */
    public PauseButton(Main main, Duck duck, Inventory inventory, GameSettingsAndStats settings) {
        // Create the button
        Button pauseButton = new Button("||");
        pauseButton.setStyle(
            "-fx-background-radius: 50%; " +
            "-fx-min-width: 40px; " +
            "-fx-min-height: 40px; " +
            "-fx-max-width: 40px; " +
            "-fx-max-height: 40px; " +
            "-fx-background-color: #28a745; " + // Green color
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold;"
        );
        
     // Add action to switch to PauseMenu
        pauseButton.setOnAction(e -> {
            System.out.println("Pause button clicked. Switching to PauseMenu...");
            main.stopStatsTracking(duck);
            main.switchToPauseMenu();
        });


        // Add the button to the StackPane
        this.getChildren().add(pauseButton);
        this.setStyle("-fx-alignment: top-left; -fx-padding: 10;");
    }
}