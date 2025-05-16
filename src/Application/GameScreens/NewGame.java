package Application.GameScreens;

import Application.Duck;
import Application.GameSettingsAndStats;
import Application.Inventory;
import Application.Main;
import Application.SaveLoad;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.util.Duration;

/**
 * The NewGame class represents the new game creation screen where the player selects
 * a starting duck companion.
 * <br><br>
 * This screen displays an animated preview of available ducks, shows duck details (name and description),
 * and provides navigation buttons to cycle through the available options. The user may confirm their selection with a custom name
 * via a confirmation dialog or close the screen to return to the main menu.
 *
 * @author Daniel Harapiak
 * @author Brandon Nguyen
 * @author Zachary Goodman
 * @author Atomm Li
 */
public class NewGame {
    private Scene scene;
    private int currentDuckIndex = 0;
    private Duck[] ducks;
    private StackPane root;
    private ImageView duckImageView;
    private Timeline duckAnimation;
    private Label duckNameLabel;
    private Label duckDescriptionLabel;
    private HBox animationBox;

    /**
     * Constructs the NewGame screen with the provided main application, available ducks,
     * inventory, and game settings. This constructor sets up all UI elements, animations,
     * and event handlers required for selecting a starting duck companion.
     *
     * @param main      The main application instance for scene management and global state updates.
     * @param ducks     An array of available Duck objects for selection.
     * @param inventory The game's inventory instance.
     * @param settings  The game's settings and statistics instance.
     */
    public NewGame(Main main, Duck[] ducks, Inventory inventory, GameSettingsAndStats settings) {
        // Initialize the ducks array with provided duck options.
        this.ducks = ducks;

        // ----------------- Create Welcome Label -----------------
        // Build a welcome label with styling and drop shadow effect.
        Label welcomeLabel = createWelcomeLabel();

        // ----------------- Button Style Definitions -----------------
        // Define styling for standard and navigation buttons.
        String buttonStyle = createButtonStyle();
        String navButtonStyle = createNavButtonStyle();

        // ----------------- Create Control Buttons -----------------
        // Create a "Close" button to return to the main menu.
        Button closeButton = createCloseButton(main, ducks[currentDuckIndex], inventory, settings, buttonStyle);
        // Create a "Select" button to confirm the duck selection.
        Button selectButton = createSelectButton(main, ducks[currentDuckIndex], inventory, settings, buttonStyle);
        // Create left/right navigation buttons to cycle through available ducks.
        Button leftButton = createLeftButton(navButtonStyle, ducks);
        Button rightButton = createRightButton(navButtonStyle, ducks);

        // ----------------- Set Up Duck Animation Display -----------------
        // Initialize the ImageView for duck animation and set its properties.
        duckImageView = new ImageView();
        duckImageView.setFitWidth(200);
        duckImageView.setFitHeight(200);
        duckImageView.setPreserveRatio(true);
        duckImageView.setSmooth(true);
        // Wrap the duck image in an HBox for proper centering.
        animationBox = new HBox(duckImageView);
        animationBox.setAlignment(Pos.CENTER);

        // ----------------- Create Duck Info Panel -----------------
        // Create a label to display the duck's name.
        duckNameLabel = new Label(ducks[currentDuckIndex].getName());
        duckNameLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: white; -fx-font-weight: bold;");
        // Apply a black outline effect to the duck name.
        DropShadow nameOutline = new DropShadow();
        nameOutline.setColor(Color.BLACK);
        nameOutline.setRadius(1);
        nameOutline.setSpread(0.9);
        duckNameLabel.setEffect(nameOutline);

        // Create a label for the duck's description.
        duckDescriptionLabel = new Label(getDuckDescription(currentDuckIndex));
        duckDescriptionLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-wrap-text: true;");
        duckDescriptionLabel.setMaxWidth(300);
        // Apply a black outline effect to the description text.
        DropShadow textOutline = new DropShadow();
        textOutline.setColor(Color.BLACK);
        textOutline.setRadius(1);
        textOutline.setSpread(0.9);
        duckDescriptionLabel.setEffect(textOutline);

        // Group the duck name and description into an info panel.
        VBox infoPanel = new VBox(10, duckNameLabel, duckDescriptionLabel);
        infoPanel.setAlignment(Pos.CENTER_LEFT);
        infoPanel.setStyle("-fx-padding: 20; -fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 10;");

        // ----------------- Combine Animation and Info Panels -----------------
        // Combine the duck animation and info panel side by side.
        HBox duckDisplay = new HBox(30, animationBox, infoPanel);
        duckDisplay.setAlignment(Pos.CENTER);
        duckDisplay.setStyle("-fx-padding: 0 50;");

        // ----------------- Center Container -----------------
        // Create a vertical container for the duck display.
        VBox centerContainer = new VBox(20, duckDisplay);
        centerContainer.setAlignment(Pos.CENTER);

        // ----------------- Bottom Container -----------------
        // Create a bottom container for the "Select" button.
        HBox bottomContainer = new HBox(selectButton);
        bottomContainer.setAlignment(Pos.CENTER);
        bottomContainer.setPadding(new Insets(0, 0, 40, 0));

        // ----------------- Background Setup -----------------
        // Load and set the background image for the NewGame screen.
        Image backgroundImage = new Image("file:src/assets/menu.png");
        BackgroundImage background = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );

        // ----------------- Main Layout -----------------
        // Create a BorderPane layout and assign the background.
        BorderPane layout = new BorderPane();
        layout.setBackground(new Background(background));
        // Set the welcome label at the top.
        VBox topContainer = new VBox(welcomeLabel);
        topContainer.setStyle("-fx-alignment: center; -fx-padding: 40 0 0 0;");
        layout.setTop(topContainer);
        // Place the center container with duck display in the center.
        layout.setCenter(centerContainer);
        // Place the bottom container with the select button at the bottom.
        layout.setBottom(bottomContainer);

        // ----------------- StackPane for Overlay Elements -----------------
        // Use a StackPane to overlay navigation and close buttons over the main layout.
        root = new StackPane();
        root.getChildren().addAll(layout, closeButton, leftButton, rightButton);
        // Position the close button at the bottom left.
        StackPane.setAlignment(closeButton, Pos.BOTTOM_LEFT);
        StackPane.setMargin(closeButton, new Insets(0, 0, 20, 20));
        // Position the left navigation button at the center left.
        StackPane.setAlignment(leftButton, Pos.CENTER_LEFT);
        StackPane.setMargin(leftButton, new Insets(0, 0, 0, 40));
        // Position the right navigation button at the center right.
        StackPane.setAlignment(rightButton, Pos.CENTER_RIGHT);
        StackPane.setMargin(rightButton, new Insets(0, 40, 0, 0));

        // ----------------- Initialize Duck Animation -----------------
        // Update the display to show the first duck's animation and information.
        updateDuckDisplay(ducks[currentDuckIndex]);

        // Create the final scene with the assembled root layout.
        this.scene = new Scene(root, 800, 600);
    }

    /**
     * Creates and returns a welcome label with a drop shadow effect.
     *
     * @return A Label containing the welcome message.
     */
    private Label createWelcomeLabel() {
        Label welcomeLabel = new Label("Welcome to the world of DuckLyfe2!\nPlease select your starting Duck companion");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        welcomeLabel.setTextFill(Color.WHITE);
        welcomeLabel.setStyle("-fx-text-alignment: center;");
        
        // Apply a drop shadow effect for a black outline.
        DropShadow textOutline = new DropShadow();
        textOutline.setColor(Color.BLACK);
        textOutline.setRadius(3);
        textOutline.setSpread(0.6);
        welcomeLabel.setEffect(textOutline);
        return welcomeLabel;
    }

    /**
     * Returns the CSS style string for standard buttons used in the NewGame screen.
     *
     * @return A String representing the button style.
     */
    private String createButtonStyle() {
        return "-fx-font-size: 18px; " +
               "-fx-background-color: #0078d7; " +
               "-fx-text-fill: white; " +
               "-fx-background-radius: 10; " +
               "-fx-padding: 8 15; " +
               "-fx-font-weight: bold; " +
               "-fx-pref-width: 120px;";
    }

    /**
     * Returns the CSS style string for navigation buttons (left/right arrows).
     *
     * @return A String representing the navigation button style.
     */
    private String createNavButtonStyle() {
        return "-fx-font-size: 24px; " +
               "-fx-background-color: rgba(0,120,215,0.7); " +
               "-fx-text-fill: white; " +
               "-fx-background-radius: 50%; " +
               "-fx-min-width: 50px; " +
               "-fx-min-height: 50px;";
    }

    /**
     * Creates a "Close" button that returns the user to the main menu.
     *
     * @param main        The main application instance.
     * @param duck        The currently selected duck.
     * @param inventory   The current inventory.
     * @param settings    The current game settings.
     * @param buttonStyle The CSS style to apply to the button.
     * @return A Button configured to close the NewGame screen.
     */
    private Button createCloseButton(Main main, Duck duck, Inventory inventory, GameSettingsAndStats settings, String buttonStyle) {
        Button closeButton = new Button("Close");
        closeButton.setStyle(buttonStyle);
        closeButton.setOnAction(e -> {
            // Transition back to the MainMenu screen.
            MainMenu mainMenu = new MainMenu(main, duck, inventory, settings);
            main.setScene(mainMenu.getScene());
        });
        return closeButton;
    }

    /**
     * Creates a "Select" button that shows a confirmation dialog for selecting the current duck.
     *
     * @param main        The main application instance.
     * @param duck        The currently selected duck.
     * @param inventory   The current inventory.
     * @param settings    The current game settings.
     * @param buttonStyle The CSS style to apply to the button.
     * @return A Button configured to prompt duck selection confirmation.
     */
    private Button createSelectButton(Main main, Duck duck, Inventory inventory, GameSettingsAndStats settings, String buttonStyle) {
        Button selectButton = new Button("Select");
        selectButton.setStyle(buttonStyle);
        selectButton.setOnAction(e -> showConfirmationDialog(main, duck, inventory, settings));
        return selectButton;
    }

    /**
     * Creates a left navigation button to cycle to the previous duck.
     *
     * @param navButtonStyle The CSS style for the navigation button.
     * @param ducks          An array of available ducks.
     * @return A Button configured for left navigation.
     */
    private Button createLeftButton(String navButtonStyle, Duck[] ducks) {
        Button leftButton = new Button("\u2190"); // Unicode left arrow
        leftButton.setStyle(navButtonStyle);
        leftButton.setOnAction(e -> {
            // Update the current duck index and refresh the display.
            currentDuckIndex = (currentDuckIndex - 1 + ducks.length) % ducks.length;
            updateDuckDisplay(ducks[currentDuckIndex]);
        });
        return leftButton;
    }

    /**
     * Creates a right navigation button to cycle to the next duck.
     *
     * @param navButtonStyle The CSS style for the navigation button.
     * @param ducks          An array of available ducks.
     * @return A Button configured for right navigation.
     */
    private Button createRightButton(String navButtonStyle, Duck[] ducks) {
        Button rightButton = new Button("\u2192"); // Unicode right arrow
        rightButton.setStyle(navButtonStyle);
        rightButton.setOnAction(e -> {
            // Update the current duck index and refresh the display.
            currentDuckIndex = (currentDuckIndex + 1) % ducks.length;
            updateDuckDisplay(ducks[currentDuckIndex]);
        });
        return rightButton;
    }

    /**
     * Updates the duck display including the animated image, name, and description
     * based on the currently selected duck.
     *
     * @param duck The current Duck object to be displayed.
     */
    private void updateDuckDisplay(Duck duck) {
        // Stop the existing animation if one is running.
        if (duckAnimation != null) {
            duckAnimation.stop();
        }

        // Update the duck's name and description labels.
        duckNameLabel.setText(duck.getName());
        duckDescriptionLabel.setText(getDuckDescription(currentDuckIndex));

        // Determine duck color based on the current index.
        String duckColor;
        switch (currentDuckIndex) {
            case 0: // Athletic Duck
                duckColor = "blue";
                break;
            case 1: // Energetic Duck
                duckColor = "pink";
                break;
            case 2: // Fasting Duck
                duckColor = "white";
                break;
            default:
                duckColor = "white"; // Default color
        }
        // Update the duck's color property.
        duck.setColour(duckColor);

        // Set the base path for duck sprite assets.
        String basePath = "file:src/assets/";
        String[] duckFrames;

        // Select the appropriate sprite frames based on the duck's color.
        switch (duckColor.toLowerCase()) {
            case "blue":
                duckFrames = new String[] {
                    basePath + "blueDuckSprite/tile030.png",
                    basePath + "blueDuckSprite/tile031.png",
                    basePath + "blueDuckSprite/tile032.png",
                    basePath + "blueDuckSprite/tile033.png",
                    basePath + "blueDuckSprite/tile034.png",
                    basePath + "blueDuckSprite/tile035.png"
                };
                break;
            case "pink":
                duckFrames = new String[] {
                    basePath + "pinkDuckSprite/tile030.png",
                    basePath + "pinkDuckSprite/tile031.png",
                    basePath + "pinkDuckSprite/tile032.png",
                    basePath + "pinkDuckSprite/tile033.png",
                    basePath + "pinkDuckSprite/tile034.png",
                    basePath + "pinkDuckSprite/tile035.png"
                };
                break;
            default: // white
                duckFrames = new String[] {
                    basePath + "duckSpriteSheet/duck030.png",
                    basePath + "duckSpriteSheet/duck031.png",
                    basePath + "duckSpriteSheet/duck032.png",
                    basePath + "duckSpriteSheet/duck033.png",
                    basePath + "duckSpriteSheet/duck034.png",
                    basePath + "duckSpriteSheet/duck035.png"
                };
        }

        // Create a new Timeline for the duck animation.
        duckAnimation = new Timeline();
        for (int i = 0; i < duckFrames.length; i++) {
            final String frame = duckFrames[i];
            // Schedule each frame with a delay.
            duckAnimation.getKeyFrames().add(new KeyFrame(
                Duration.millis(i * 200),
                e -> {
                    try {
                        duckImageView.setImage(new Image(frame, 200, 200, true, true));
                    } catch (Exception ex) {
                        System.err.println("Error loading duck frame: " + frame);
                    }
                }
            ));
        }
        // Add a pause at the end of the animation cycle.
        duckAnimation.getKeyFrames().add(new KeyFrame(
            Duration.millis(duckFrames.length * 200 + 1000)
        ));

        // Loop the animation indefinitely.
        duckAnimation.setCycleCount(Timeline.INDEFINITE);
        duckAnimation.play();
    }

    /**
     * Retrieves a description for the duck corresponding to the given index.
     *
     * @param index The index of the duck.
     * @return A String containing the duck's description.
     */
    private String getDuckDescription(int index) {     
        switch (index) {
            case 0: // Athletic Duck
                return "An athletic duck with superior stamina.\n" +
                       "Loses health 10% slower than other ducks.\n" +
                       "Loves to exercise!";
            case 1: // Energetic Duck
                return "A highly energetic duck that rarely tires.\n" +
                       "Loses sleep 10% slower than other ducks.\n" +
                       "Ready to play more often!";
            case 2: // Fast Duck
                return "A duck that mastered fasting.\n" +
                       "Loses hunger 10% slower than other ducks.\n" +
                       "Perfect at survival!";
            default:
                return "A unique duck with special traits.";
        }
    }

    /**
     * Displays a confirmation dialog prompting the user to select the current duck as their companion.
     * If confirmed, the dialog further prompts for a custom duck name, updates the global state,
     * saves game data, and transitions to the Home screen.
     *
     * @param main      The main application instance.
     * @param duck      The currently selected duck.
     * @param inventory The current inventory.
     * @param settings  The current game settings.
     */
    private void showConfirmationDialog(Main main, Duck duck, Inventory inventory, GameSettingsAndStats settings) {
        // Create a VBox to serve as the dialog box container.
        VBox dialogBox = new VBox(6);  // Reduced vertical spacing
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.setPadding(new Insets(10, 20, 10, 20));  // Adjusted padding
        dialogBox.setStyle("-fx-background-color: rgba(255,235,59,0.6); " +
                           "-fx-background-radius: 8; " +
                           "-fx-border-color: rgba(255,193,7,0.8); " +
                           "-fx-border-width: 2; " +
                           "-fx-border-radius: 8;");
        dialogBox.setMaxWidth(400);
        dialogBox.setMaxHeight(220);

        // Create a label to ask for confirmation of the duck selection.
        Label confirmLabel = new Label("Select " + duckNameLabel.getText() + " as your companion?");
        confirmLabel.setStyle("-fx-font-size: 22px; " +
                              "-fx-text-fill: black; " +
                              "-fx-font-weight: bold; " +
                              "-fx-text-alignment: center;");
        confirmLabel.setWrapText(true);
        confirmLabel.setMaxWidth(280);

        Button yesButton = new Button("Yes");
        yesButton.setStyle("-fx-background-color: rgba(76,175,80,0.9); " +
                           "-fx-text-fill: white; " +
                           "-fx-font-weight: bold; " +
                           "-fx-min-width: 80px; " +
                           "-fx-font-size: 14px; " +
                           "-fx-padding: 5 10;");
        Button noButton = new Button("No");
        noButton.setStyle("-fx-background-color: rgba(244,67,54,0.9); " +
                          "-fx-text-fill: white; " +
                          "-fx-font-weight: bold; " +
                          "-fx-min-width: 80px; " +
                          "-fx-font-size: 14px; " +
                          "-fx-padding: 5 10;");

        // Group the yes and no buttons into an HBox.
        HBox buttonBox = new HBox(15, yesButton, noButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(5, 0, 0, 0));

        // Add the confirmation label and buttons to the dialog box.
        dialogBox.getChildren().addAll(confirmLabel, buttonBox);

        // Create a StackPane to serve as an overlay for the dialog.
        StackPane dialogPane = new StackPane(dialogBox);
        dialogPane.setStyle("-fx-background-color: rgba(0,0,0,0.4);");
        dialogPane.setOpacity(0);

        // Add the dialog pane to the root StackPane.
        root.getChildren().add(dialogPane);
        StackPane.setAlignment(dialogPane, Pos.CENTER);

        // Animate the dialog's opacity from 0 to 1 to fade it in.
        Timeline fadeIn = new Timeline(
            new KeyFrame(Duration.millis(0), new KeyValue(dialogPane.opacityProperty(), 0)),
            new KeyFrame(Duration.millis(150), new KeyValue(dialogPane.opacityProperty(), 1))
        );
        fadeIn.play();

        yesButton.setOnAction(e -> {
            // Prompt the user for a custom duck name.
            TextInputDialog nameDialog = new TextInputDialog();
            nameDialog.setTitle("Name Your Duck");
            nameDialog.setHeaderText(null);
            nameDialog.setContentText("Enter a name for your duck:");
            nameDialog.initModality(Modality.APPLICATION_MODAL);
            nameDialog.showAndWait().ifPresent(name -> {
                if (!name.trim().isEmpty()) {
                    // Create a new duck with the custom name.
                    Duck selected = new Duck(name.trim());
                    // Use the color from the current duck.
                    selected.setColour(ducks[currentDuckIndex].getColour());
                    System.out.println("Selected duck color: " + selected.getColour());
                    // Initialize new inventory and settings for the game.
                    Inventory newInventory = new Inventory();
                    GameSettingsAndStats newSettings = new GameSettingsAndStats();
                    // Update the global state with the new duck, inventory, and settings.
                    main.updateGlobalState(selected, newInventory, newSettings);
                    // Save the new game state.
                    SaveLoad saver = new SaveLoad();
                    saver.saveAllData(selected, newInventory, newSettings);
                    // Start playtime tracking.
                    main.startPlaytimeTracking(settings);
                    // Transition to the Home screen.
                    Home home = new Home(main, selected, newInventory, newSettings);
                    main.setScene(home.getScene());
                    // Begin coin generation.
                    main.startCoinGeneration();
                }
            });

            // Fade out the confirmation dialog after selection.
            Timeline fadeOut = new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(dialogPane.opacityProperty(), 1)),
                new KeyFrame(Duration.millis(150), new KeyValue(dialogPane.opacityProperty(), 0))
            );
            fadeOut.setOnFinished(ev -> root.getChildren().remove(dialogPane));
            fadeOut.play();
        });

        noButton.setOnAction(e -> {
            // Fade out the confirmation dialog without making any changes.
            Timeline fadeOut = new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(dialogPane.opacityProperty(), 1)),
                new KeyFrame(Duration.millis(150), new KeyValue(dialogPane.opacityProperty(), 0))
            );
            fadeOut.setOnFinished(ev -> root.getChildren().remove(dialogPane));
            fadeOut.play();
        });
    }

    /**
     * Returns the JavaFX Scene representing the NewGame screen.
     *
     * @return The Scene containing the NewGame interface.
     */
    public Scene getScene() {
        return scene;
    }
}
