package Application.GameScreens;

import Application.Components.PauseButton;
import Application.Duck;
import Application.GameSettingsAndStats;
import Application.Inventory;
import Application.Main;
import Application.SaveLoad;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 * Represents the main menu screen of the DuckLyfe2 application.
 * <br><br>
 * This class initializes and manages the main menu UI, including buttons for
 * starting a new game, loading saves, accessing settings, and exiting the application.
 * It also handles button actions and applies parental lock restrictions if enabled.
 * <br><br>
 * Example Use:
 * <pre>
 * MainMenu mainMenu = new MainMenu(main, duck, inventory, settings);
 * Scene mainMenuScene = mainMenu.getScene();
 * </pre>
 * 
 * @author Daniel Harapiak
 * @author Brandon Nguyen
 * @author Dinith Nawaratne
 * @author Zachary Goodman
 */
public class MainMenu {
    private Scene scene;

        /**
     * Constructs the MainMenu and initializes its UI components.
     * <br><br>
     * This constructor sets up the main menu layout, styles the buttons, and
     * configures their actions. It also applies parental lock restrictions
     * to disable certain buttons if the parental lock is active.
     * <br><br>
     * Example Use:
     * <pre>
     * MainMenu mainMenu = new MainMenu(main, duck, inventory, settings);
     * </pre>
     *
     * @param main      The main application instance.
     * @param duck      The current duck instance.
     * @param inventory The player's inventory.
     * @param settings  The game settings and statistics.
     */

    public MainMenu(Main main, Duck duck, Inventory inventory, GameSettingsAndStats settings) {
        // Create the main menu layout

        Label titleLabel = new Label("DuckLyfe2");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setStyle("-fx-text-fill: #0078d7; -fx-padding: 20;");
        titleLabel.setTextAlignment(TextAlignment.CENTER);

        Button newGameButton = new Button("New Game");
        Button loadSavesButton = new Button("Load Saves"); //  Load Saves button
        Button settingsButton = new Button("Settings");
        Button exitButton = new Button("Exit");

        // Style the buttons

        String disabledButtonStyle = "-fx-font-size: 16px; " +
                "-fx-background-color: #d3d3d3; " +
                "-fx-text-fill: #a9a9a9; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 10 20; " +
                "-fx-font-weight: bold; " +
                "-fx-pref-width: 200px; " +
                "-fx-alignment: center;";

        String buttonStyle = "-fx-font-size: 16px; " +
                "-fx-background-color: #0078d7; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 10 20; " +
                "-fx-font-weight: bold; " +
                "-fx-pref-width: 200px; " +
                "-fx-alignment: center;";

        /**
         * Applies parental lock restrictions to the main menu buttons.
         * <br><br>
         * If the parental lock is active, the "New Game" and "Load Saves" buttons
         * are disabled and styled to indicate they are unavailable. This ensures
         * restricted access to certain features based on parental control settings.
         * <br><br>
         * Example Use:
         * <pre>
         * if (settings.isParentalLockActive()) {
         *     loadSavesButton.setDisable(true);
         *     loadSavesButton.setStyle(disabledButtonStyle);
         *     newGameButton.setDisable(true);
         *     newGameButton.setStyle(disabledButtonStyle);
         * }
         * </pre>
         */
        
        if (settings.isParentalLockActive()) {
            loadSavesButton.setDisable(true);
            loadSavesButton.setStyle(disabledButtonStyle);
            newGameButton.setDisable(true);
            newGameButton.setStyle(disabledButtonStyle);
            System.out.println("DEBUG: Parental lock is active. Graying out 'Load Saves' and 'New Game' buttons.");
        } else {
            loadSavesButton.setStyle(buttonStyle);
            newGameButton.setStyle(buttonStyle);
        }

        settingsButton.setStyle(buttonStyle);
        exitButton.setStyle(buttonStyle);
        
        /**
         * Sets the action for the "Load Saves" button.
         * <br><br>
         * When clicked, this button navigates the user to the load saves screen
         * where they can load a previously saved game state.
         * <br><br>
         * Example Use:
         * <pre>
         * loadSavesButton.setOnAction(e -> {
         *     LoadSaves loadSaves = new LoadSaves(main);
         *     main.setScene(loadSaves.getScene());
         * });
         * </pre>
         */
        loadSavesButton.setOnAction(e -> { // Load Saves button action
            System.out.println("Load Saves button clicked");
            LoadSaves loadSaves = new LoadSaves(main);
            main.setScene(loadSaves.getScene());
        });

        /**
         * Sets the action for the "New Game" button.
         * <br><br>
         * When clicked, this button starts a new game by initializing a set of
         * starter ducks and navigating the user to the new game screen.
         * If parental lock is active, this button is disabled.
         * <br><br>
         * Example Use:
         * <pre>
         * newGameButton.setOnAction(e -> {
         *     Duck athleticDuck = new Duck("Athletic");
         *     athleticDuck.setColour("blue");
         *     Duck[] starterDucks = new Duck[] { athleticDuck, ... };
         *     NewGame newGame = new NewGame(main, starterDucks, inventory, settings);
         *     main.setScene(newGame.getScene());
         * });
         * </pre>
         */

        newGameButton.setOnAction(e -> {
            if (!settings.isParentalLockActive()) {
                System.out.println("New Game button clicked");
                Duck athleticDuck = new Duck("Athletic");
                athleticDuck.setColour("blue");
                Duck energeticDuck = new Duck("Energetic");
                energeticDuck.setColour("pink");
                Duck fastingDuck = new Duck("Fasting");
                fastingDuck.setColour("white");
                Duck[] starterDucks = new Duck[] { athleticDuck, energeticDuck, fastingDuck };
                NewGame newGame = new NewGame(main, starterDucks, inventory, settings);
                main.setScene(newGame.getScene());
            }
        });


        /**
         * Sets the action for the "Settings" button.
         * <br><br>
         * When clicked, this button navigates the user to the settings menu
         * where they can modify game settings and preferences.
         * <br><br>
         * Example Use:
         * <pre>
         * settingsButton.setOnAction(e -> {
         *     SettingsMenu settingsMenu = new SettingsMenu(main, duck, inventory, settings);
         *     main.setScene(settingsMenu.getScene());
         * });
         * </pre>
         */

        settingsButton.setOnAction(e -> {
            main.setPreviousScene(scene); // Track main menu as previous scene
            SettingsMenu settingsMenu = new SettingsMenu(main, duck, inventory, settings);
            main.setScene(settingsMenu.getScene());
        });
        

        /**
         * Sets the action for the "Exit" button.
         * <br><br>
         * When clicked, this button saves the current game state and exits the application.
         * <br><br>
         * Example Use:
         * <pre>
         * exitButton.setOnAction(e -> {
         *     SaveLoad dSave = new SaveLoad();
         *     dSave.saveAllData(duck, inventory, settings);
         *     System.exit(0);
         * });
         * </pre>
         */

        exitButton.setOnAction(e -> {
            SaveLoad dSave = new SaveLoad();
            System.out.println("Saving before exit");
            main.stopPlaytimeTracking();
            dSave.saveAllData(duck, inventory, settings);
            System.exit(0);
        });

        VBox menuBox = new VBox(20);
        menuBox.getChildren().addAll(
                titleLabel,
                newGameButton,
                loadSavesButton, // Add Load Saves here
                settingsButton,
                exitButton
        );
        menuBox.setStyle("-fx-alignment: center-right; -fx-padding: 0 50 0 0;");

        ImageView duckImageView = new ImageView();
        duckImageView.setFitWidth(200);
        duckImageView.setFitHeight(200);
        duckImageView.setPreserveRatio(true);
        duckImageView.setSmooth(true);

        // Load duck sprite frames

        String[] duckFrames = {
                "file:src/assets/duckSpriteSheet/duck030.png",
                "file:src/assets/duckSpriteSheet/duck031.png",
                "file:src/assets/duckSpriteSheet/duck032.png",
                "file:src/assets/duckSpriteSheet/duck033.png",
                "file:src/assets/duckSpriteSheet/duck034.png",
                "file:src/assets/duckSpriteSheet/duck035.png"
        };

         /**
         * Initializes and plays the duck animation on the main menu.
         * <br><br>
         * This animation cycles through a series of duck sprite frames to create
         * a smooth animation effect. The animation is set to loop indefinitely.
         * <br><br>
         * Example Use:
         * <pre>
         * Timeline duckAnimation = new Timeline();
         * duckAnimation.getKeyFrames().add(new KeyFrame(...));
         * duckAnimation.setCycleCount(Timeline.INDEFINITE);
         * duckAnimation.play();
         * </pre>
         */
        Timeline duckAnimation = new Timeline();
        for (int i = 0; i < duckFrames.length; i++) {
            String frame = duckFrames[i];
            duckAnimation.getKeyFrames().add(new KeyFrame(
                    Duration.millis(i * 200),
                    e -> duckImageView.setImage(new Image(frame, 200, 200, true, true))
            ));
        }
        duckAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(duckFrames.length * 200 + 1000)));
        duckAnimation.setCycleCount(Timeline.INDEFINITE);
        duckAnimation.play();

        HBox duckContainer = new HBox(duckImageView);
        duckContainer.setStyle("-fx-alignment: center-left; -fx-padding: 0 0 0 50;");

        // Set the background image
        Image backgroundImage = new Image("file:src/assets/menu.png");
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
        layout.setLeft(duckContainer);
        layout.setCenter(menuBox);

        PauseButton pauseButton = new PauseButton(main, duck, inventory, settings);
        layout.setTop(pauseButton);

        this.scene = new Scene(layout, 800, 600);
    }

        /**
     * Retrieves the scene associated with the main menu.
     * <br><br>
     * This method returns the JavaFX Scene object that represents the main menu.
     * It can be used to set the main menu as the active scene in the application.
     * <br><br>
     * Example Use:
     * <pre>
     * Scene mainMenuScene = mainMenu.getScene();
     * primaryStage.setScene(mainMenuScene);
     * </pre>
     *
     * @return The Scene object representing the main menu.
     */

    public Scene getScene() {
        return scene;
    }
}
