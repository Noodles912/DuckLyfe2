package Application;

import java.io.File;

import Application.GameScreens.Home;
import Application.GameScreens.MainMenu;
import Application.GameScreens.Park;
import Application.GameScreens.PauseMenu;
import Application.GameScreens.Store;
import Application.GameScreens.Vet;
import javafx.animation.KeyFrame; // for time played tracking
import javafx.animation.Timeline; // for time played tracking
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage; // for time played tracking
import javafx.util.Duration;

/**
 * Represents the main application class for DuckLyfe2.
 * <br><br>
 * This class initializes the application, manages scene transitions, tracks
 * game statistics, and enforces parental controls. It also handles audio playback
 * and coin generation.
 */
public class Main extends Application {

    private Stage primaryStage; // Store the primary stage for scene switching
    private StackPane rootWrapper; // Persistent wrapper for all scenes
    private Scene wrapperScene; // Persistent scene with global key handling
    private Scene currentScene; // Track the current active scene
    private Scene previousScene; // Track the previous scene before PauseMenu
    private MediaPlayer mediaPlayer; // MediaPlayer for the soundtrack
    private Timeline playtimeTimer;
    private Timeline depleteHappiness;
    private Timeline updateTiredness;
    private Timeline updateHealth;
    private Timeline updateHunger;
    private Timeline updateUserInterface;
    private Timeline updateScore;
    private int sessionTime = 0;
    
    //Placeholders for future when data being passed between classes is needed
    SaveLoad dSave = new SaveLoad();
    private Duck duck = dSave.loadDuck();
    private Inventory inventory = dSave.loadInv();
    private GameSettingsAndStats settings = dSave.loadSettings();
    

        /**
     * Starts the application and initializes the primary stage.
     * <br><br>
     * This method sets up the main menu, initializes the media player for the
     * soundtrack, and configures the persistent wrapper for global key handling.
     *
     * @param primaryStage The primary stage for the application.
     */

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        settings.addSession();

        // Initialize the MediaPlayer for the soundtrack
        String mp3Path = "src/assets/soundtrack.mp3"; 
        Media media = new Media(new File(mp3Path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        
        // Initialize global coin label
        coinLabel.setText(" x " + duck.getCoins());
        coinLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: black; -fx-font-weight: bold;");
        ImageView coinIcon = new ImageView(new Image("file:src/assets/coinFront.png"));
        coinIcon.setFitWidth(30);
        coinIcon.setFitHeight(30);
        coinLabel.setGraphic(coinIcon);

		
		double savedVolume = settings.getVolume() / 100.0;  // Temp holder for settins, will be updated when gameSettings finished

        
        mediaPlayer.setVolume(savedVolume); // Loaded volume from JSON
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the soundtrack
        mediaPlayer.play(); // Start playing the soundtrack

        // Create the persistent wrapper
        rootWrapper = new StackPane();
        wrapperScene = new Scene(rootWrapper, 800, 600);

        // Launch the MainMenu
        MainMenu mainMenu = new MainMenu(this, duck, inventory, settings);
        setScene(mainMenu.getScene());

        primaryStage.setTitle("DuckLyfe2");
        primaryStage.setScene(wrapperScene); // Use the persistent wrapper scene
        primaryStage.show();
    }

        /**
     * Enforces parental lock restrictions on the current scene.
     * <br><br>
     * This method disables buttons and restricts access to certain features
     * based on the parental lock settings.
     *
     * @param scene The current scene to enforce parental lock on.
     */

    private void enforceParentalLock(Scene scene) {
    	
    	GameSettingsAndStats setCheck = dSave.loadSettings();
    	
        if (!setCheck.isParentalLockActive()) {
            return; // Do nothing if parental lock is not active
        }
    
        // Check the type of the current screen and disable buttons accordingly
        if (scene.getUserData() instanceof Home) {
            disableHomeButtons((Home) scene.getUserData());
        } else if (scene.getUserData() instanceof Vet) {
            disableVetButtons((Vet) scene.getUserData());
        } else if (scene.getUserData() instanceof Store) {
            disableStoreButtons((Store) scene.getUserData());
        } else if (scene.getUserData() instanceof Park) {
            disableParkButtons((Park) scene.getUserData());
        } else if (scene.getRoot() instanceof BorderPane) {
            // Handle MainMenu
            BorderPane root = (BorderPane) scene.getRoot();
            if (root.getCenter() instanceof VBox) {
                VBox menuBox = (VBox) root.getCenter();
                for (javafx.scene.Node node : menuBox.getChildren()) {
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
                }
            }
        }
    }

        /**
     * Sets the current scene for the application.
     * <br><br>
     * This method replaces the content of the persistent wrapper with the new
     * scene's root and enforces parental lock if active.
     *
     * @param newScene The new scene to set.
     */

    public void setScene(Scene newScene) {
        // Replace the content of the wrapper with the new scene's root
        rootWrapper.getChildren().setAll(newScene.getRoot());
        currentScene = newScene; // Track the current active scene
    
        // Enforce parental lock if active
        enforceParentalLock(newScene);
    }

        /**
     * Switches to the pause menu.
     * <br><br>
     * This method stores the current scene as the previous scene and transitions
     * to the pause menu.
     */
    public void switchToPauseMenu() {
        // Store the current scene as the previous scene before switching
        previousScene = currentScene;

        PauseMenu pauseMenu = new PauseMenu(this, duck, inventory, settings);

        // Set the PauseMenu scene
        setScene(pauseMenu.getScene());
    }

        /**
     * Resumes the previous scene.
     * <br><br>
     * If a previous scene exists, this method transitions back to it. Otherwise,
     * it logs a message indicating no previous scene is available.
     */
    public void resumePreviousScene() {
        // Resume the previous scene if it exists
        if (previousScene != null) {
            setScene(previousScene);
        } else {
            System.out.println("No previous scene to resume.");
        }
    }

    /**
     * Retrieves the primary stage of the application.
     *
     * @return The primary stage.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Retrieves the MediaPlayer instance for audio playback.
     *
     * @return The MediaPlayer instance.
     */
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer; // Provide access to the MediaPlayer for other classes
    }
    /**
     * Retrieves the current scene of the application.
     *
     * @return The current scene.
     */

    public Scene getCurrentScene() {
        return currentScene;
    }
    /**
     * Retrieves the previous scene before the pause menu.
     *
     * @return The previous scene.
     */

    public Scene getPreviousScene() {
        return previousScene;
    }
    /**
     * Retrieves the duck instance.
     *
     * @return The duck instance.
     */
    
    public Duck getDuck() {
        return duck;
    }
    /**
     * Retrieves the inventory instance.
     *
     * @return The inventory instance.
     */

    public Inventory getInventory() {
        return inventory;
    }
    /**
     * Retrieves the game settings and statistics instance.
     *
     * @return The game settings and statistics instance.
     */

    public GameSettingsAndStats getSettings() {
        return settings;
    }
    /**
     * Retrieves the current scene of the application.
     *
     * @return The current scene.
     */
    private boolean parentalLockActivated = false;

        /**
     * Starts tracking playtime for the current session.
     * <br><br>
     * This method increments the playtime every second and enforces parental
     * controls if the time limit is reached.
     *
     * @param stats The game settings and statistics.
     */

public void startPlaytimeTracking(GameSettingsAndStats stats) {
    if (playtimeTimer != null) {
        playtimeTimer.stop(); // Stop any existing timer to avoid duplicates
    }
    playtimeTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
        stats.setTimePLayed(1); // Increase total playtime every second
        sessionTime++;

        // Convert time limit from minutes to seconds
        int timeLimit = stats.getTimeLimit() * 60;
        int remainingTime = timeLimit - sessionTime;

        // Log the countdown every second
        System.out.println("Session Time (seconds): " + sessionTime);

        if (timeLimit > 0 && remainingTime > 0) {
            System.out.println("Time remaining: " + remainingTime + " seconds");
        }

        if (timeLimit > 0 && sessionTime >= timeLimit && !parentalLockActivated) { // Directly compare seconds
            stopPlaytimeTracking(); // Stop the timer to prevent multiple triggers
                        
            stats.setParentalLockActive(true);
            System.out.println("DEBUG: Parental control timer expired. Locking user out.");

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Time Limit Reached");
                alert.setHeaderText(null);
                alert.setContentText("The time limit is up. Parental controls are now active.");
                alert.initOwner(getPrimaryStage());
                alert.showAndWait();

                enforceParentalLock(getCurrentScene());

                // Update button states if the current scene is MainMenu
                if (getCurrentScene().getRoot() instanceof BorderPane) {
                    BorderPane root = (BorderPane) getCurrentScene().getRoot();
                    if (root.getCenter() instanceof VBox) {
                        VBox menuBox = (VBox) root.getCenter();
                        for (javafx.scene.Node node : menuBox.getChildren()) {
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
                        }
                    }
                }

                // Update button states if the current scene is Home
                if (getCurrentScene().getUserData() instanceof Home) {
                    Home home = (Home) getCurrentScene().getUserData();
                    disableHomeButtons(home);
                }

                // Update button states if the current scene is Vet
                if (getCurrentScene().getUserData() instanceof Vet) {
                    Vet vet = (Vet) getCurrentScene().getUserData();
                    disableVetButtons(vet);
                }

                // Update button states if the current scene is Store
                if (getCurrentScene().getUserData() instanceof Store) {
                    Store store = (Store) getCurrentScene().getUserData();
                    disableStoreButtons(store);
                }

                // Update button states if the current scene is Park
                if (getCurrentScene().getUserData() instanceof Park) {
                    Park park = (Park) getCurrentScene().getUserData();
                    disableParkButtons(park);
                }
            });
        }
    }));
    playtimeTimer.setCycleCount(Timeline.INDEFINITE);
    playtimeTimer.play();
}

    /**
     * Disables buttons and displays a message when the time limit is reached.
     * <br><br>
     * This method disables all buttons except the home button and displays a
     * message indicating that parental controls are active.
     *
     * @param vet The Vet instance to disable buttons for.
     */

    private void disableVetButtons(Vet vet) {
        for (javafx.scene.Node node : vet.getScene().getRoot().lookupAll(".button")) {
            if (node instanceof Button) {
                Button button = (Button) node;

                // Skip disabling the home button
                if ("Home".equals(button.getText())) { // Assuming the home button's text is "Home"
                    continue;
                }

                button.setDisable(true);
                button.setStyle("-fx-font-size: 16px; " +
                                "-fx-background-color: #d3d3d3; " + // Light gray background
                                "-fx-text-fill: #a9a9a9; " +         // Dark gray text
                                "-fx-background-radius: 10; " +
                                "-fx-padding: 10 20; " +
                                "-fx-font-weight: bold; " +
                                "-fx-pref-width: 200px; " +
                                "-fx-alignment: center;");
            }
        }

        // Display a popup message
        Label popupLabel = (Label) vet.getScene().getRoot().lookup("#popupLabel");
        if (popupLabel != null) {
            popupLabel.setText("Time is up! Parental controls are active. Please return to the main menu.");
            popupLabel.setVisible(true);
        }
    }
    /**
     * Disables buttons and displays a message when the time limit is reached.
     * <br><br>
     * This method disables all buttons except the home button and displays a
     * message indicating that parental controls are active.
     *
     * @param store The Store instance to disable buttons for.
     */
    private void disableStoreButtons(Store store) {
        for (javafx.scene.Node node : store.getScene().getRoot().lookupAll(".button")) {
            if (node instanceof Button) {
                Button button = (Button) node;
    
                // Skip disabling the home button
                if ("Home".equals(button.getText())) { // Assuming the home button's text is "Home"
                    continue;
                }
    
                button.setDisable(true);
                button.setStyle("-fx-font-size: 16px; " +
                                "-fx-background-color: #d3d3d3; " + // Light gray background
                                "-fx-text-fill: #a9a9a9; " +         // Dark gray text
                                "-fx-background-radius: 10; " +
                                "-fx-padding: 10 20; " +
                                "-fx-font-weight: bold; " +
                                "-fx-pref-width: 200px; " +
                                "-fx-alignment: center;");
            }
        }
    
        // Display a popup message
        Label popupLabel = (Label) store.getScene().getRoot().lookup("#popupLabel");
        if (popupLabel != null) {
            popupLabel.setText("Time is up! Parental controls are active. Please return to the main menu.");
            popupLabel.setVisible(true);
        }
    }

    /**
     * Disables buttons and displays a message when the time limit is reached.
     * <br><br>
     * This method disables all buttons except the home button and displays a
     * message indicating that parental controls are active.
     *
     * @param home The Home instance to disable buttons for.
     */
    private void disableHomeButtons(Home home) {
        // Disable and gray out buttons, except the pause button
        for (javafx.scene.Node node : home.getScene().getRoot().lookupAll(".button")) {
            if (node instanceof Button) {
                Button button = (Button) node;
    
                // Skip disabling the pause button
                if ("||".equals(button.getText())) { // Assuming the pause button's text is "||"
                    continue;
                }
    
                button.setDisable(true);
                button.setStyle("-fx-font-size: 16px; " +
                                "-fx-background-color: #d3d3d3; " + // Light gray background
                                "-fx-text-fill: #a9a9a9; " +         // Dark gray text
                                "-fx-background-radius: 10; " +
                                "-fx-padding: 10 20; " +
                                "-fx-font-weight: bold; " +
                                "-fx-pref-width: 200px; " +
                                "-fx-alignment: center;");
            }
        }
    
        // Display a popup message
        Label popupLabel = (Label) home.getScene().getRoot().lookup("#popupLabel");
        if (popupLabel != null) {
            popupLabel.setText("Time is up! Parental controls are active. Please return to the main menu.");
            popupLabel.setVisible(true);
        }
    }
    /**
     * Disables buttons and displays a message when the time limit is reached.
     * <br><br>
     * This method disables all buttons except the home button and displays a
     * message indicating that parental controls are active.
     *
     * @param park The Park instance to disable buttons for.
     */

    private void disableParkButtons(Park park) {
        for (javafx.scene.Node node : park.getScene().getRoot().lookupAll(".button")) {
            if (node instanceof Button) {
                Button button = (Button) node;
    
                // Skip disabling the home button
                if ("Home".equals(button.getText())) { // Assuming the home button's text is "Home"
                    continue;
                }
    
                button.setDisable(true);
                button.setStyle("-fx-font-size: 16px; " +
                                "-fx-background-color: #d3d3d3; " + // Light gray background
                                "-fx-text-fill: #a9a9a9; " +         // Dark gray text
                                "-fx-background-radius: 10; " +
                                "-fx-padding: 10 20; " +
                                "-fx-font-weight: bold; " +
                                "-fx-pref-width: 200px; " +
                                "-fx-alignment: center;");
            }
        }
    
        // Display a popup message
        Label popupLabel = (Label) park.getScene().getRoot().lookup("#popupLabel");
        if (popupLabel != null) {
            popupLabel.setText("Time is up! Parental controls are active. Please return to the main menu.");
            popupLabel.setVisible(true);
        }
    }


    /**
     * Retrieves the session time in seconds.
     *
     * @return The session time in seconds.
     */
    
    public int getSessionTime() {
    	return sessionTime;
    }
    
    /**
     * Resets the session time to zero.
     * <br><br>
     * This method is used to reset the session time when starting a new game
     * or when the user chooses to reset the session.
     */
    public void resetSessionTime() {
    	sessionTime = 0;
    }
    /**
     * Stops tracking playtime for the current session.
     * <br><br>
     * This method stops the playtime timer and resets the session time.
     */

    public void stopPlaytimeTracking() {
        if (playtimeTimer != null) {
            playtimeTimer.stop();
        }
    }
    /**
     * Retrieves the coin label for the duck.
     *
     * @return The coin label.
     */

    public Label getCoinLabel() {
        return coinLabel;
    }
    /**
     * Sets the coin label for the duck.
     *
     * @param coinLabel The coin label to set.
     */
    
    public static void main(String[] args) {
        launch(args);
    }
    
    
    private boolean audioEnabled = true; // Track the audio state

    /**
     * Checks if audio is enabled.
     *
     * @return True if audio is enabled, false otherwise.
     */
    public boolean isAudioEnabled() {
        return audioEnabled;
    }
    /**
     * Enables audio playback.
     * <br><br>
     * This method resumes audio playback if it was previously disabled.
     */

    public void enableAudio() {
        audioEnabled = true;
        if (mediaPlayer != null) {
            mediaPlayer.play(); // Resume audio playback
        }
    }
    /**
     * Disables audio playback.
     * <br><br>
     * This method pauses audio playback if it was previously enabled.
     */

    public void disableAudio() {
        audioEnabled = false;
        if (mediaPlayer != null) {
            mediaPlayer.pause(); // Pause audio playback
        }
    }

    /**
     * Initializes the tracking of game statistics for the duck.
     * <br><br>
     * This method sets up timelines to update the duck's happiness, tiredness,
     * health, hunger, and score over time.
     *
     * @param duck The duck instance to track statistics for.
     */
    public void initializeStatsTracking(Duck duck) {
        depleteHappiness = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            if(!duck.isDead()) {
                duck.setHappiness(-1);
            }
        }));

        updateTiredness = new Timeline(new KeyFrame(Duration.seconds(duck.getColour().equals("pink") ? 3.5 : 3), e -> {
            if(!duck.isDead()) {
                duck.setTiredness(1);
            }
        }));

        updateHealth = new Timeline(new KeyFrame(Duration.seconds(duck.getColour().equals("blue") ? 8.5 : 7), e -> {
            if(!duck.isDead() && (duck.getHunger() <= 5 || duck.getTiredness() > 85)) {
                duck.setHealth(-1);
            }
        }));

        updateHunger = new Timeline(new KeyFrame(Duration.seconds((duck.getColour().equals("pink") || duck.getColour().equals("blue")) ? 7 : 8), e -> {
            if(!duck.isDead() && duck.getTiredness() > 50) {
                duck.setHunger(-1);
                System.out.println("Duck is HUNGRY!");
            }
        }));

        updateScore = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            if(!duck.isDead()) {
                if(duck.getHunger() > 7 && duck.getHealth() == 10 && duck.getHappiness() > 75 && duck.getTiredness() < 60) {
                    duck.setScore(10);
                } else if(duck.getHunger() > 4 && duck.getHealth() > 6 && duck.getHappiness() > 60 && duck.getTiredness() < 70) {
                    duck.setScore(5);
                } else {
                    duck.setScore(-2);
                }
            }
        }));

        depleteHappiness.setCycleCount(Timeline.INDEFINITE);
        updateTiredness.setCycleCount(Timeline.INDEFINITE);
        updateHealth.setCycleCount(Timeline.INDEFINITE);
        updateHunger.setCycleCount(Timeline.INDEFINITE);
        updateScore.setCycleCount(Timeline.INDEFINITE);
    }
     /**
     * Starts tracking game statistics for the duck.
     */

    public void startStatsTracking() {
        depleteHappiness.play();
        updateTiredness.play();
        updateHealth.play();
        updateHunger.play();
        updateScore.play();
    }

    /**
     * Stops tracking game statistics for the duck.
     *
     * @param duck The duck instance to stop tracking statistics for.
     */

    public void stopStatsTracking(Duck duck) {
        depleteHappiness.pause();
        updateTiredness.pause();
        updateHealth.pause();
        updateHunger.pause();
        updateScore.pause();
    }

    /**
     * Updates the user interface with the latest game statistics.
     *
     * @param userInterface The user interface instance to update.
     */

    public void updateUserInterface(UserInterface userInterface) {
        updateUserInterface = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            userInterface.updateStatsDisplay();
            userInterface.updateHealth();
            userInterface.updateHunger();
            userInterface.updateScore();
        }));
        updateUserInterface.setCycleCount(Timeline.INDEFINITE);
        updateUserInterface.play();
    }

    private Timeline coinTimeline;
    private Label coinLabel = new Label(); // Shared coin label
    private Label coinLabelRef;

    /**
     * Starts generating coins for the duck.
     * <br><br>
     * This method increments the duck's coins periodically and updates the coin label.
     */

    public void startCoinGeneration() {
        if (duck.isDead()) return; // Don't start if the duck is dead
    
        coinLabelRef = coinLabel;
    
        if (coinTimeline != null) {
            coinTimeline.stop();
        }
    
        // Ensure coin generation only runs in gameplay scenes
        if (currentScene != null && (currentScene.getUserData() instanceof Home ||
                                     currentScene.getUserData() instanceof Store ||
                                     currentScene.getUserData() instanceof Park ||
                                     currentScene.getUserData() instanceof Vet)) {
            coinTimeline = new Timeline(new KeyFrame(Duration.seconds(4.0), e -> {
                duck.setCoins(1); // Increment coins
                if (coinLabelRef != null) {
                    coinLabelRef.setText(" x " + duck.getCoins());
                }
            }));
            coinTimeline.setCycleCount(Timeline.INDEFINITE);
            coinTimeline.play();
        }
    }
    
    /**
     * Updates the coin label reference.
     * <br><br>
     * This method allows other classes to update the coin label reference
     * for displaying the duck's coins.
     *
     * @param newCoinLabel The new coin label reference.
     */

    public void updateCoinLabelReference(Label newCoinLabel) {
        this.coinLabelRef = newCoinLabel;
    }
    /**
     * Updates the coin label with the current coin value.
     * <br><br>
     * This method sets the text of the coin label to display the current
     * number of coins the duck has.
     */

    public void updateCoinLabel() {
        if (coinLabel != null) {
            coinLabel.setText(" x " + duck.getCoins());
        }
    }
    /**
     * Stops generating coins for the duck.
     */

    public void stopCoinGeneration() {
        if (coinTimeline != null) {
            coinTimeline.stop();
        }
    }
    
    
    /**
     * Updates the global state of the application.
     * <br><br>
     * This method updates the duck, inventory, and settings instances and resets
     * the coin label with the new duck's coin value.
     *
     * @param newDuck      The new duck instance.
     * @param newInventory The new inventory instance.
     * @param newSettings  The new game settings and statistics instance.
     */
    public void updateGlobalState(Duck newDuck, Inventory newInventory, GameSettingsAndStats newSettings) {
        this.duck = newDuck;
        this.inventory = newInventory;
        this.settings = newSettings;
    
        // Reset coin label with new duck's coin value
        coinLabel.setText(" x " + duck.getCoins());
        updateCoinLabelReference(coinLabel);
    }

    /**
     * Retrieves the primary stage of the application.
     *
     * @return The primary stage.
     */

    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }
    
    
}
