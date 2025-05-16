package Application.GameScreens;

import java.util.HashMap;

import Application.Duck;
import Application.GameSettingsAndStats;
import Application.Inventory;
import Application.Main;
import Application.SaveLoad;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Represents the Parental Controls feature in the DuckLyfe2 application.
 * <br><br>
 * This class provides functionality for managing parental controls, including
 * sign-in, setting playtime limits, reviving pets, and toggling audio features.
 * It also displays various game statistics.
 */
public class ParentalControls {
    private Scene signInScene;
    private Scene menuScene;
    private Scene reviveScene;
    private Scene timeScene;

    /**
     * Constructs the ParentalControls and initializes its UI components.
     * <br><br>
     * This constructor sets up the sign-in screen, menu screen, revive scene,
     * and time limit scene.
     *
     * @param main          The main application instance.
     * @param settingsMenu  The settings menu instance.
     * @param duck          The current duck instance.
     * @param inventory     The player's inventory.
     * @param settings      The game settings and statistics.
     */

    public ParentalControls(Main main, SettingsMenu settingsMenu, Duck duck, Inventory inventory, GameSettingsAndStats settings) {
        // Initialize both screens
        createSignInScreen(main, settingsMenu);
        createMenuScreen(main, settingsMenu, duck, inventory, settings);
        createReviveScene(main, settingsMenu);
        createTimeScene(main, settingsMenu, duck, inventory, settings);
    }


    /**
     * Creates the sign-in screen for parental controls.
     * <br><br>
     * This screen allows users to log in using a username and password.
     * Upon successful login, the user is navigated to the parental control menu.
     *
     * @param main          The main application instance.
     * @param settingsMenu  The settings menu instance.
     */
    // Create the Sign-In Screen
    private void createSignInScreen(Main main, SettingsMenu settingsMenu) {
        // Username and Password fields
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        // Title Label
        Label titleLabel = new Label("Parental Controls Sign-In");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;"); // White text with bold styling

        // Login button
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-font-size: 16px; -fx-background-color: #0078d7; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 10 20;");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Check credentials
            if (username.equals("admin") && password.equals("duckpass")) {
                System.out.println("Login successful!");
                main.setScene(menuScene); // Navigate to the Parental Control Menu
            } else {
                System.out.println("Invalid credentials!");
            }
        });

        // Back button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 16px; -fx-background-color: #0078d7; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 10 20;");
        backButton.setOnAction(e -> main.setScene(settingsMenu.getScene())); // Navigate back to SettingsMenu

        // Layout
        VBox layout = new VBox(20, titleLabel, usernameField, passwordField, loginButton, backButton);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center;");

        // Set the background image
        Image backgroundImage = new Image("file:src/assets/pausemenu.png");
        BackgroundImage background = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );

        BorderPane root = new BorderPane();
        root.setBackground(new Background(background));
        root.setCenter(layout);

        // Scene
        this.signInScene = new Scene(root, 800, 600);
    }

    /**
     * Creates the main menu screen for parental controls.
     * <br><br>
     * This screen provides options to set playtime limits, toggle audio features,
     * revive pets, and view game statistics.
     *
     * @param main          The main application instance.
     * @param settingsMenu  The settings menu instance.
     * @param duck          The current duck instance.
     * @param inventory     The player's inventory.
     * @param settings      The game settings and statistics.
     */

    private void createMenuScreen(Main main, SettingsMenu settingsMenu, Duck duck, Inventory inventory, GameSettingsAndStats settings) {
    	
        // Left-side menu buttons
        Button playtimeButton = new Button("Set allowed Playtime Hours");
        Button featuresButton = new Button("Audio Enabled");
        Button revivePetButton = new Button("Revive Pet");
        Button closeButton = new Button("Close");
        Button resetTimes = new Button("Reset Playtime Stats");
        
        SaveLoad saveLoad = new SaveLoad(); // Save load class for json modifying
    
        // Style buttons
        String buttonStyle = "-fx-font-size: 16px; -fx-background-color: #0078d7; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 10 20;";
        playtimeButton.setStyle(buttonStyle);
        featuresButton.setStyle(buttonStyle);
        revivePetButton.setStyle(buttonStyle);
        closeButton.setStyle(buttonStyle);
        resetTimes.setStyle(buttonStyle);
    
        playtimeButton.setOnAction(e -> {
    		System.out.println("Time Limit");
    		createTimeScene(main, settingsMenu, duck, inventory, settings);
    		main.setScene(timeScene);
        }); 
        
        // Close button action
        closeButton.setOnAction(e -> main.setScene(settingsMenu.getScene())); // Navigate back to SettingsMenu
        
        revivePetButton.setOnAction(e -> {
        	System.out.println("Revive");
        	createReviveScene(main, settingsMenu);
        	main.setScene(reviveScene);
        }); 

       // Add action to toggle audio
        featuresButton.setOnAction(e -> {
            if (main.isAudioEnabled()) {
                main.disableAudio(); // Disable audio
                featuresButton.setText("Audio Disabled"); // Update button text
                featuresButton.setStyle(buttonStyle + "-fx-background-color: gray;"); // Change to gray
            } else {
                main.enableAudio(); // Enable audio
                featuresButton.setText("Audio Enabled"); // Update button text
                featuresButton.setStyle(buttonStyle + "-fx-background-color:  #0078d7;"); // Change to green
            }
        });
        
        Label infoLabel = new Label();
        infoLabel.setStyle("-fx-text-fill: yellow; -fx-font-size: 14px;");
        infoLabel.setText(""); // Empty by default

        resetTimes.setOnAction(e -> {
            System.out.println("Reset");
            settings.updateNumSessions(0 - settings.getSessions()); // Reset Num Session to 0 to reset average
            settings.setTimePLayed(0 - settings.timePlayed());
            saveLoad.saveSettings(settings);
            
            infoLabel.setText("Changes will show on exit.");
        });
    
        VBox leftMenu = new VBox(20, playtimeButton, featuresButton, revivePetButton, resetTimes, infoLabel, closeButton);

        leftMenu.setPadding(new Insets(20));
        leftMenu.setStyle("-fx-alignment: center-left;");
    
        // Right-side stats as a TableView
        TableView<Statistic> statsTable = new TableView<>();
        statsTable.setPrefWidth(400);
        statsTable.setFixedCellSize(30); // Set fixed cell size to avoid extra rows
    
        // Define columns
        TableColumn<Statistic, String> statNameColumn = new TableColumn<>("Statistic");
        statNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        statNameColumn.setPrefWidth(200);
    
        TableColumn<Statistic, String> statValueColumn = new TableColumn<>("Value");
        statValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        statValueColumn.setPrefWidth(200);
    
        // Add columns to the table
        statsTable.getColumns().addAll(statNameColumn, statValueColumn);
    

        HashMap<String, Object[]> allSaves = saveLoad.loadAllSaves();

        int totalCoins = 0;
        int totalFood = 0;
        int totalToys = 0;
        int totalCaps = 0;

        // Loop through all saves and aggregate
        for (Object[] save : allSaves.values()) {
            Duck duckStat = (Duck) save[0];
            Inventory inv = (Inventory) save[1];

            totalCoins += duckStat.getCoins();
            totalFood += inv.getFood();
            totalToys += inv.getToy();
            totalCaps += inv.getNightCap();
        }

        // Get global time played from settings
        int totalTimePlayed = saveLoad.loadSettings().timePlayed(); // in seconds
        int averageSession = 0;
        
        if(saveLoad.loadSettings().getSessions() != 0) {
        	averageSession = (totalTimePlayed / 60)/saveLoad.loadSettings().getSessions();
        }

        // Populate the table
        statsTable.getItems().clear();
        statsTable.getItems().addAll(
            new Statistic("Total Time Played", (totalTimePlayed / 60) + " minutes"),
            new Statistic("Average Time PLayed", averageSession + " minutes"),
            new Statistic("Total Coins", String.valueOf(totalCoins)),
            new Statistic("Total Ducks", String.valueOf(allSaves.size())),
            new Statistic("Total Food", String.valueOf(totalFood)),
            new Statistic("Total Toys", String.valueOf(totalToys)),
            new Statistic("Total Night Caps", String.valueOf(totalCaps))
        );
    
        // Set the table height to match the number of rows
        statsTable.setPrefHeight(statsTable.getFixedCellSize() * statsTable.getItems().size() + 28); // Add padding for headers
    
        // Apply a white background directly to the table
        statsTable.setStyle("-fx-background-color: white; -fx-border-color: lightgray; -fx-border-radius: 10;");
    
        // Wrap the table in a VBox to control its alignment and padding
        VBox tableContainer = new VBox(statsTable);
        tableContainer.setPadding(new Insets(0, 50, 0, 0)); // Add padding to the right (50px)
        tableContainer.setStyle("-fx-alignment: center;"); // Center the table vertically
    
        // Set the background image
        Image backgroundImage = new Image("file:src/assets/pausemenu.png");
        BackgroundImage background = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
    
        BorderPane layout = new BorderPane();
        layout.setBackground(new Background(background));
        layout.setLeft(leftMenu);
        layout.setRight(tableContainer); // Place the table container on the right side
    
        // Scene
        this.menuScene = new Scene(layout, 800, 600);
    }

    /**
     * Creates the revive scene for parental controls.
     * <br><br>
     * This screen allows users to revive dead pets. If a pet is already alive,
     * it cannot be revived.
     *
     * @param main          The main application instance.
     * @param settingsMenu  The settings menu instance.
     */
    
    private void createReviveScene(Main main, SettingsMenu settingsMenu) {
        VBox duckListBox = new VBox(20);
        duckListBox.setPadding(new Insets(20));
        duckListBox.setAlignment(Pos.CENTER);

        SaveLoad save = new SaveLoad();
        HashMap<String, Object[]> saves = save.loadAllSaves();

        for (String duckName : saves.keySet()) {
            Object[] data = saves.get(duckName);
            Duck duck = (Duck) data[0];
            Inventory inventory = (Inventory) data[1];
            GameSettingsAndStats settings = (GameSettingsAndStats) data[2];

            VBox duckBox = new VBox(10);
            duckBox.setAlignment(Pos.CENTER);
            duckBox.setStyle("-fx-border-color: white; -fx-border-radius: 10; -fx-padding: 10;");
            
            Label nameLabel = new Label(duck.getName());
            nameLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");
            duckBox.getChildren().add(nameLabel);

            if (duck.isDead()) {
                Button reviveButton = new Button("Revive");
                reviveButton.setStyle("-fx-font-size: 14px; -fx-background-color: #28a745; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 5 15;");
                reviveButton.setOnAction(e -> {
                    duck.flipLivingState();
                    System.out.println(duck.getName() + " has been revived!");
                    duck.printStats();

                    // Overwrite with updated duck
                    save.saveAllData(duck, inventory, settings);

                    // Rebuild screen to reflect updated state
                    createReviveScene(main, settingsMenu);
                    main.setScene(reviveScene);
                });
                duckBox.getChildren().add(reviveButton);
            } else {
                Label aliveLabel = new Label("Alive - Cannot Revive");
                aliveLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 14px;");
                duckBox.getChildren().add(aliveLabel);
            }

            duckListBox.getChildren().add(duckBox);
        }

        // Back Button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 16px; -fx-background-color: #0078d7; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 10 20;");
        backButton.setOnAction(e -> main.setScene(menuScene));

        VBox mainBox = new VBox(30, duckListBox, backButton);
        mainBox.setAlignment(Pos.CENTER);

        // Background
        Image backgroundImage = new Image("file:src/assets/pausemenu.png");
        BackgroundImage background = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );

        BorderPane layout = new BorderPane();
        layout.setBackground(new Background(background));
        layout.setCenter(mainBox);

        this.reviveScene = new Scene(layout, 800, 600);
    }

    /**
     * Creates the time limit scene for parental controls.
     * <br><br>
     * This screen allows users to set a daily playtime limit in minutes, remove
     * the limit, or reset the playtime timer.
     *
     * @param main          The main application instance.
     * @param settingsMenu  The settings menu instance.
     * @param duck          The current duck instance.
     * @param inventory     The player's inventory.
     * @param settings      The game settings and statistics.
     */

    private void createTimeScene(Main main, SettingsMenu settingsMenu, Duck duck, Inventory inventory, GameSettingsAndStats settings) {
        Label titleLabel = new Label("Set Daily Playtime Limit (in minutes)");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
        
        SaveLoad save = new SaveLoad();

        TextField timeInput = new TextField();
        timeInput.setPromptText("e.g., 60 for 1 hour");
        timeInput.setMaxWidth(200);

        Label feedbackLabel = new Label();
        feedbackLabel.setStyle("-fx-text-fill: #ff4444; -fx-font-size: 14px;");

        // Set Limit Button
        Button setLimitButton = new Button("Set Limit");
        setLimitButton.setStyle("-fx-font-size: 16px; -fx-background-color: #28a745; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 10 20;");
        setLimitButton.setOnAction(e -> {
            try {
                int limit = Integer.parseInt(timeInput.getText());
                if (limit < 0) {
                    feedbackLabel.setText("Limit must be 0 or greater.");
                } else {
                    settings.setTimeLimit(limit); // Set the time limit in minutes
                    feedbackLabel.setStyle("-fx-text-fill: lightgreen;");
                    feedbackLabel.setText("Time limit set to " + limit + " minutes.");
                    
                   
                    save.saveAllData(duck, inventory, settings);
                    
        
                    // Reset session time and start tracking
                    main.resetSessionTime(); // Reset session time to 0
                    main.startPlaytimeTracking(settings); // Start tracking playtime
        
                    System.out.println("Time limit set to " + limit + " minutes. Countdown started.");
                }
            } catch (NumberFormatException ex) {
                feedbackLabel.setText("Invalid input. Please enter a valid number.");
            }
        });

        // No Limit Button
        Button noLimitButton = new Button("No Limit");
        noLimitButton.setStyle("-fx-font-size: 16px; -fx-background-color: #f39c12; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 10 20;");
        noLimitButton.setOnAction(e -> {
            settings.setTimeLimit(-1);
            feedbackLabel.setStyle("-fx-text-fill: lightgreen;");
            feedbackLabel.setText("Playtime limit removed. No restrictions set.");
            System.out.println("Time limit set to: No Limit");
            save.saveAllData(duck, inventory, settings);
        });

        // Reset Timer Button
        Button resetTimerButton = new Button("Reset Timer");
        resetTimerButton.setStyle("-fx-font-size: 16px; -fx-background-color: #d35400; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 10 20;");
        resetTimerButton.setOnAction(e -> {
            // TODO: Add reset logic here
            System.out.println("Reset Timer button clicked.");
            main.resetSessionTime();
            settings.setParentalLockActive(false);
            save.saveAllData(duck, inventory, settings);
            main.startPlaytimeTracking(settings);
        });

        // Back Button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 16px; -fx-background-color: #0078d7; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 10 20;");
        backButton.setOnAction(e -> {
            createMenuScreen(main, settingsMenu, duck, inventory, settings);
            main.setPreviousScene(null);
            main.setScene(menuScene);
        });

        VBox layout = new VBox(20, titleLabel, timeInput, setLimitButton, noLimitButton, resetTimerButton, feedbackLabel, backButton);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-alignment: center;");

        // Background
        Image backgroundImage = new Image("file:src/assets/pausemenu.png");
        BackgroundImage background = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );

        BorderPane root = new BorderPane();
        root.setBackground(new Background(background));
        root.setCenter(layout);

        this.timeScene = new Scene(root, 800, 600);
    }


    /**
     * Retrieves the sign-in scene for parental controls.
     * <br><br>
     * This method returns the JavaFX Scene object that represents the sign-in screen.
     *
     * @return The Scene object for the sign-in screen.
     */
    // Get the Sign-In Scene
    public Scene getSignInScene() {
        return signInScene;
    }

    /**
     * Represents a statistic displayed in the parental controls menu.
     * <br><br>
     * This inner class encapsulates a statistic's name and value.
     */
    // Inner class to represent a statistic
    public static class Statistic {
        private final String name;
        private final String value;

        public Statistic(String name, String value) {
            this.name = name;
            this.value = value;
        }

        /**
         * Retrieves the name of the statistic.
         *
         * @return The name of the statistic.
         */

        public String getName() {
            return name;
        }

        /**
         * Retrieves the value of the statistic.
         *
         * @return The value of the statistic.
         */

        public String getValue() {
            return value;
        }
    }
}
