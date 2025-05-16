package Application.GameScreens;

import Application.Duck;
import Application.GameSettingsAndStats;
import Application.Inventory;
import Application.Main;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;


/**
 * Represents the audio settings menu in the DuckLyfe2 application.
 * <br><br>
 * This class provides functionality for adjusting the game's audio volume
 * and navigating back to the settings menu. It includes a slider for volume
 * control and a "Back" button for navigation.
 * 
 * @author Brandon Nguyen
 */
public class AudioMenu {
    private Scene scene;

    /**
     * Constructs the AudioMenu and initializes its UI components.
     * <br><br>
     * This constructor sets up the volume slider, volume label, and navigation
     * button. It also adjusts the MediaPlayer volume based on user input.
     *
     * @param main          The main application instance.
     * @param settingsMenu  The settings menu instance.
     * @param duck          The current duck instance.
     * @param inventory     The player's inventory.
     * @param settings      The game settings and statistics.
     */

    public AudioMenu(Main main, SettingsMenu settingsMenu, Duck duck, Inventory inventory, GameSettingsAndStats settings) {
        MediaPlayer mediaPlayer = main.getMediaPlayer();
        
        // Get the current volume from the MediaPlayer (convert from 0.0-1.0 to 0-100)
        double currentVolume = mediaPlayer != null ? mediaPlayer.getVolume() * 100 : 50;
            
        // Create a label to display the volume
        Label volumeLabel = new Label("Volume: " + (int) currentVolume + "%");
        volumeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;"); // Set text color to white and adjust font size
        
        // Create a slider for volume control
        Slider volumeSlider = new Slider(0, 100, currentVolume); // Set initial value to current volume
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setMajorTickUnit(10);
        volumeSlider.setBlockIncrement(1);

        // Add a listener to update the label and adjust the MediaPlayer volume
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int volume = newValue.intValue();
            volumeLabel.setText("Volume: " + volume + "%");
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(volume / 100.0); // Set volume (MediaPlayer expects a value between 0.0 and 1.0)
                settings.setVolume(volume);
                
            }
        });

        // Create a "Back" button to return to the SettingsMenu
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            main.setScene(settingsMenu.getScene()); // Navigate back to the SettingsMenu
        });

        // Style the button
        String buttonStyle = "-fx-font-size: 16px; " +
                             "-fx-background-color: #0078d7; " +
                             "-fx-text-fill: white; " +
                             "-fx-background-radius: 10; " +
                             "-fx-padding: 10 20; " +
                             "-fx-font-weight: bold; " +
                             "-fx-pref-width: 200px; " + // Fixed width for all buttons
                             "-fx-alignment: center;";   // Center-align text
        backButton.setStyle(buttonStyle);

        // Layout for the controls
        VBox controlBox = new VBox(20);
        controlBox.setPadding(new Insets(20));
        controlBox.getChildren().addAll(volumeLabel, volumeSlider, backButton);
        controlBox.setStyle("-fx-alignment: center;"); // Center-align the VBox

        // Set the background image
        Image backgroundImage = new Image("file:src/assets/pausemenu.png");
        BackgroundImage background = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );

        // Create the main layout
        BorderPane layout = new BorderPane();
        layout.setBackground(new Background(background));
        layout.setCenter(controlBox);

        // Create the scene
        this.scene = new Scene(layout, 800, 600); // Match the size of the SettingsMenu
    }

        /**
     * Retrieves the scene associated with the audio menu.
     * <br><br>
     * This method returns the JavaFX Scene object that represents the audio menu.
     *
     * @return The Scene object for the audio menu.
     */

    public Scene getScene() {
        return scene;
    }
}