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
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * The LoadSaves class is responsible for creating the user interface that displays
 * a list of saved game states (saved ducks) that the user can choose to load. 
 * <br><br>
 * It reads saved data from the SaveLoad utility, dynamically generates load buttons for each save,
 * and also provides a back button to return to the main menu.
 */
public class LoadSaves {
    private Scene scene;
    private VBox savesList = new VBox(15);
    private StackPane root;

    /**
     * Constructs the LoadSaves screen by loading all saved game states, setting up the
     * background, title, load buttons for each saved duck, and a back button to return to
     * the main menu.
     *
     * @param main The main application instance used for updating global state and managing scenes.
     */
    public LoadSaves(Main main) {
        // Create a SaveLoad instance to handle file operations
        SaveLoad saveLoad = new SaveLoad();
        // Load all saved game states into a HashMap (each save contains an array of objects)
        HashMap<String, Object[]> saves = saveLoad.loadAllSaves(); // Using HashMap instead of Map

        // Load the background image from the assets folder
        Image backgroundImage = new Image("file:src/assets/menu.png");
        // Create a BackgroundImage with no repeat and centered position
        BackgroundImage background = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );

        // Create a BorderPane layout and set the created background
        BorderPane layout = new BorderPane();
        layout.setBackground(new Background(background));

        // Create a title label for the screen
        Label title = new Label("Select a Saved Duck");
        // Set font style for the title
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        // Set text color to white for the title
        title.setTextFill(Color.WHITE);
        // Create a drop shadow effect for the title
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setSpread(0.8);
        shadow.setRadius(1.5);
        title.setEffect(shadow);

        // Wrap the title in a VBox to center it and add padding
        VBox top = new VBox(title);
        top.setAlignment(Pos.CENTER);
        top.setPadding(new Insets(30, 0, 10, 0));
        layout.setTop(top);

        // Configure the VBox that will hold the load buttons for each save
        savesList.setAlignment(Pos.CENTER);
        savesList.setPadding(new Insets(20));

        // Iterate over each saved duck in the saves HashMap
        for (String duckName : saves.keySet()) {
            // Retrieve the saved data associated with the current duck name
            Object[] data = saves.get(duckName);
            Duck duck = (Duck) data[0];
            Inventory inventory = (Inventory) data[1];
            GameSettingsAndStats settings = (GameSettingsAndStats) data[2];

            // Create a button for loading the saved duck
            Button loadButton = new Button("Load " + duckName);
            loadButton.setPrefWidth(240);
            // Set styling for the load button
            loadButton.setStyle("-fx-font-size: 16px; -fx-background-color: #0078d7; -fx-text-fill: white;" +
                                "-fx-background-radius: 8; -fx-font-weight: bold;");

            // Set the action when the load button is clicked
            loadButton.setOnAction(e -> {
                // Update the global state with the loaded save data
                main.updateGlobalState(duck, inventory, settings);
                // Start tracking playtime for the current settings
                main.startPlaytimeTracking(settings);
                // Create a new Home screen using the loaded save data
                Home home = new Home(main, duck, inventory, settings);
                // Set the current scene to the Home screen
                main.setScene(home.getScene());
                // Start generating coins in the game
                main.startCoinGeneration();
            });

            // Add the load button to the saves list VBox
            savesList.getChildren().add(loadButton);
        }

        // Place the saves list in the center of the layout
        layout.setCenter(savesList);

        // Create a back button to return to the main menu
        Button backButton = new Button("Back");
        // Style the back button
        backButton.setStyle("-fx-background-color: gray; -fx-text-fill: white; -fx-font-weight: bold;");
        // Set the action for the back button click event
        backButton.setOnAction(e -> {
            // Load the default duck, inventory, and settings for the main menu
            SaveLoad s = new SaveLoad();
            Duck d = s.loadDuck();
            Inventory i = s.loadInv();
            GameSettingsAndStats g = s.loadSettings();
            // Create a new MainMenu instance with the loaded data
            MainMenu mainMenu = new MainMenu(main, d, i, g);
            // Set the current scene to the MainMenu
            main.setScene(mainMenu.getScene());
        });

        // Position the back button at the bottom left of the screen
        StackPane.setAlignment(backButton, Pos.BOTTOM_LEFT);
        StackPane.setMargin(backButton, new Insets(0, 0, 20, 20));

        // Combine the layout and back button into a single root StackPane
        root = new StackPane(layout, backButton);
        // Create the final scene with the root StackPane
        this.scene = new Scene(root, 800, 600);
    }

    /**
     * Retrieves the JavaFX Scene for the LoadSaves screen.
     *
     * @return The scene containing the load saves user interface.
     */
    public Scene getScene() {
        return scene;
    }
}

