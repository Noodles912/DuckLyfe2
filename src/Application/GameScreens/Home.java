package Application.GameScreens;

import Application.Components.PauseButton;
import Application.Duck;
import Application.GameSettingsAndStats;
import Application.Inventory;
import Application.Main;
import Application.SaveLoad;
import Application.UserInterface;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * The Home class represents the main game screen where the user interacts with their pet duck.
 * <br><br>
 * It sets up the user interface including action buttons (feed, play, gift, sleep), displays
 * pet status messages, handles animations for pet behavior, and provides navigation to other screens
 * such as the Vet, Park, and Store. This class also continuously checks the pet's status (e.g., anger,
 * health, and death) and updates the UI accordingly.
 */
public class Home {
    // Variable initializations and declarations
    private Scene scene;
    private Duck pet;
    private Main main;
    private boolean wasAngry = false;
    private Button feedPetButton, petPlayButton, giveGiftButton, sleepButton;
    private Button vetButton, parkButton, storeButton;
    private Label popupLabel;
    private PauseTransition popupDelay;
    private boolean statsStopped = false;

    /**
     * Constructs the Home screen and initializes the UI components, animations, and event handlers.
     * The constructor always fetches the updated global state from the Main object, sets up UI elements,
     * and starts background processes such as stats tracking and coin generation.
     *
     * @param main            The main application instance containing the global state and scene management.
     * @param petIgnored      Ignored parameter; the current pet is fetched from the main instance.
     * @param invIgnored      Ignored parameter; the current inventory is fetched from the main instance.
     * @param settingsIgnored Ignored parameter; the current game settings are fetched from the main instance.
     */
    public Home(Main main, Duck petIgnored, Inventory invIgnored, GameSettingsAndStats settingsIgnored) {
        // Assign main instance to a local variable
        this.main = main;
    
        // Always fetch the updated global state from the main object
        Duck pet = main.getDuck();
        Inventory inventory = main.getInventory();
        GameSettingsAndStats settings = main.getSettings();
        this.pet = pet;
    
        // Initialize pet stats tracking
        main.initializeStatsTracking(pet);

        // Save the current game state (pet, inventory, settings)
        SaveLoad save = new SaveLoad();
        save.saveAllData(pet, inventory, settings);

        // Initialize the user interface with current settings and pet data
        final UserInterface userInterface = new UserInterface(settings, pet, main.getCoinLabel());
        main.startStatsTracking();
        main.updateUserInterface(userInterface);
        main.startCoinGeneration();
        
        // Initialize control buttons for pet interactions
        feedPetButton = new Button("(F)eed Pet");
        petPlayButton = new Button("(P)lay with Pet");
        giveGiftButton = new Button("Give (G)ift");
        sleepButton = new Button("(S)leep");

        // Setup popup label for displaying feedback messages
        popupLabel = new Label();
        popupLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75); -fx-text-fill: white; -fx-font-size: 18px; -fx-padding: 10 20; -fx-background-radius: 10;");
        popupLabel.setVisible(false);
        popupLabel.setId("popupLabel");
        popupLabel.setMouseTransparent(true);
        StackPane.setAlignment(popupLabel, Pos.TOP_CENTER);
        popupLabel.setTranslateY(50);

        // Setup a label to indicate when the pet is angry
        Label angryStateLabel = new Label();
        angryStateLabel.setStyle("-fx-background-color: rgba(255, 0, 0, 0.75); -fx-text-fill: white; -fx-font-size: 18px; -fx-padding: 10 20; -fx-background-radius: 10;");
        angryStateLabel.setVisible(false);
        StackPane.setAlignment(angryStateLabel, Pos.TOP_CENTER);
        angryStateLabel.setTranslateY(-50);

        // Create a timeline to update the pet's angry state every second
        Timeline angryStateUpdater = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            // Check if pet is angry and update the angry state label
            boolean isAngry = pet.isAngry();
            angryStateLabel.setVisible(isAngry);
        
            if (isAngry) {
                angryStateLabel.setText("Your pet is angry!");
                // If pet just became angry, show a popup message with advice
                if (!wasAngry) {
                    popupLabel.setText("Your pet is angry! Try playing, giving a gift, or taking it for a walk.");
                    popupLabel.setVisible(true);
                    popupDelay.playFromStart();
                }
            }
            // Update the flag for previous angry state
            wasAngry = isAngry;
        }));
        angryStateUpdater.setCycleCount(Timeline.INDEFINITE);
        angryStateUpdater.play();
        
        // Create a timeline to check if the pet has died every second
        Timeline deathStateChecker = new Timeline(new KeyFrame(Duration.seconds(1), e -> checkIfPetDied()));
        deathStateChecker.setCycleCount(Timeline.INDEFINITE);
        deathStateChecker.play();

        // Setup a pause transition to hide the popup label after 2 seconds
        popupDelay = new PauseTransition(Duration.seconds(2));
        popupDelay.setOnFinished(event -> popupLabel.setVisible(false));

        // Event handler for feeding the pet
        feedPetButton.setOnAction(e -> {
            if (pet.isAngry()) {
                // Display message if pet is angry
                popupLabel.setText("Your pet is angry! Try playing, giving a gift, or taking it for a walk.");
            } else if (inventory.getFood() > 0 && pet.getHunger() < 10) {
                // Feed the pet if there is food and the pet is not too full
                inventory.setFood(-1);
                pet.setHunger(1);
                popupLabel.setText("Fed your pet!");
                pet.startEatingAnimation();
            } else {
                // Inform user if pet is too full or there is no food left
                popupLabel.setText(pet.getHunger() == 10 ? "Pet is too full to eat!" : "No food left!");
            }
            // Show popup and update hunger display
            popupLabel.setVisible(true);
            popupDelay.playFromStart();
            userInterface.updateHunger();
            checkIfPetDied();
        });

        // Event handler for playing with the pet
        petPlayButton.setOnAction(e -> {
            if (inventory.getToy() > 0 && pet.getHappiness() <= 90) {
                // Play with the pet if a toy is available and the pet is not overly excited
                inventory.setToy(-1);
                pet.setHappiness(10);
                popupLabel.setText("You played with your pet!");
            } else {
                // Inform user if pet is too excited or no toy is available
                popupLabel.setText(pet.getHappiness() > 90 ? "Pet is too excited to play!" : "No toy left!");
            }
            // Show popup and update stats display
            popupLabel.setVisible(true);
            popupDelay.playFromStart();
            userInterface.updateStatsDisplay();
            checkIfPetDied();
        });

        // Event handler for giving a gift to the pet
        giveGiftButton.setOnAction(e -> {
            if (inventory.getNightCap() > 0 && pet.getTiredness() > 0) {
                // Give a gift to the pet to reduce tiredness if a night cap is available
                inventory.setNightCap(-1);
                pet.setTiredness(0 - pet.getTiredness());
                popupLabel.setText("Gift given to pet! Your pet is no longer tired!");
                pet.startReceivingGiftAnimation();
            } else {
                // Inform user if no night cap is available or pet is not tired
                popupLabel.setText(inventory.getNightCap() == 0 ? "No night cap left!" : "Pet is already well-rested!");
            }
            // Show popup and update stats display
            popupLabel.setVisible(true);
            popupDelay.playFromStart();
            userInterface.updateStatsDisplay();
            checkIfPetDied();
        });

        // Event handler for putting the pet to sleep
        sleepButton.setOnAction(e -> {
            if (pet.isAngry()) {
                // Prevent sleeping if pet is angry
                popupLabel.setText("Your pet is angry! Try playing, giving a gift, or taking it for a walk.");
            } else if (pet.getTiredness() > 50) {
                // Allow sleep if pet is sufficiently tired and reset tiredness
                pet.setTiredness(0 - pet.getTiredness());
                popupLabel.setText("Pet took a nap!");
                pet.startSleepingAnimation();
            } else {
                // Inform user if pet is not tired enough
                popupLabel.setText("Pet is not tired enough to sleep!");
            }
            // Show popup and update stats display
            popupLabel.setVisible(true);
            popupDelay.playFromStart();
            userInterface.updateStatsDisplay();
            checkIfPetDied();
        });

        // Setup the background image for the home screen
        var backgroundImage = new Image("file:src/assets/homeBackground.gif");
        BackgroundImage background = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );

        // Create a border pane layout and set the background image
        BorderPane layout = new BorderPane();
        layout.setBackground(new Background(background));

        // Create and configure the pause button and add it to the top bar
        PauseButton pauseButton = new PauseButton(main, pet, inventory, settings);
        HBox topBar = new HBox(pauseButton);
        topBar.setAlignment(Pos.TOP_LEFT);
        topBar.setPadding(new Insets(15));
        layout.setTop(topBar);

        // Define a common style for the pet action buttons
        String buttonStyle = "-fx-font-size: 16px; -fx-background-color:rgb(255, 248, 59); -fx-text-fill: black; -fx-background-radius: 10; -fx-padding: 10 20;";
        feedPetButton.setStyle(buttonStyle);
        petPlayButton.setStyle(buttonStyle);
        giveGiftButton.setStyle(buttonStyle);
        sleepButton.setStyle(buttonStyle);

        // Create a vertical box to hold the action buttons and position it on the left
        VBox commandBox = new VBox(10, feedPetButton, petPlayButton, giveGiftButton, sleepButton);
        commandBox.setStyle("-fx-alignment: center-left; -fx-padding: 200 0 0 50;");
        layout.setLeft(commandBox);

        // Setup the Vet button with its style and event handler for navigation
        vetButton = new Button("Vet");
        vetButton.setStyle("-fx-font-size: 16px; -fx-pref-width: 150px; -fx-background-color: rgb(135, 250, 154); -fx-text-fill: black; -fx-background-radius: 10; -fx-padding: 20 40;");
        
        vetButton.setOnAction(e -> {
            if (pet.isAngry()) {
                // Advise user to calm the pet down if it is angry before going to the Vet
                popupLabel.setText("Your pet is angry! Take it to the park for a walk to calm it down.");
                popupLabel.setVisible(true);
                popupDelay.playFromStart();
            } else {
                // Stop pet stats tracking and navigate to the Vet screen
                main.stopStatsTracking(pet);
                main.setScene(new Vet(main).getScene());
            }
        });

        // Position the Vet button on the right side of the screen
        StackPane.setAlignment(vetButton, Pos.CENTER_RIGHT);
        vetButton.setTranslateX(-20);
        vetButton.setTranslateY(-80);

        // Setup the Park button with similar styling and navigation behavior
        parkButton = new Button("Park");
        parkButton.setStyle(vetButton.getStyle());
        parkButton.setOnAction(e -> {
            // Stop pet stats tracking and navigate to the Park screen
            main.stopStatsTracking(pet);
            main.setScene(new Park(main).getScene());
        });
        // Position the Park button on the right side of the screen
        StackPane.setAlignment(parkButton, Pos.CENTER_RIGHT);
        parkButton.setTranslateX(-20);
        parkButton.setTranslateY(0);

        // Setup the Store button with its style and event handler for navigation
        storeButton = new Button("Store");
        storeButton.setStyle(vetButton.getStyle());

        storeButton.setOnAction(e -> {
            // Allow navigation to the Store even if the pet is angry
            main.stopStatsTracking(pet);
            main.setScene(new Store(main).getScene());
        });
        
        // Position the Store button on the right side of the screen
        StackPane.setAlignment(storeButton, Pos.CENTER_RIGHT);
        storeButton.setTranslateX(-20);
        storeButton.setTranslateY(80);

        // Retrieve the pet's image view and prepare its display layer
        ImageView duckImageView = pet.getDuckImageView();
        StackPane duckLayer = new StackPane(duckImageView);
        duckLayer.setMouseTransparent(true);
        StackPane.setAlignment(duckImageView, Pos.BOTTOM_CENTER);
        duckImageView.setTranslateY(-30);

        // Setup the user interface layer and configure its alignment and transparency
        StackPane uiLayer = new StackPane(userInterface.getScene().getRoot());
        StackPane.setAlignment(userInterface.getScene().getRoot(), Pos.BOTTOM_RIGHT);
        uiLayer.setMouseTransparent(true);

        // Setup the inventory layer and configure its alignment and transparency
        StackPane inventoryLayer = new StackPane(inventory.getScene().getRoot());
        StackPane.setAlignment(inventoryLayer, Pos.TOP_LEFT);
        inventoryLayer.setMouseTransparent(true);

        // Perform an initial check to see if the pet is dead
        checkIfPetDied();
        
        // Save game state after initializing the UI
        save.saveAllData(pet, main.getInventory(), main.getSettings());

        // Combine all layers and UI elements into a single home screen layout
        StackPane homeScreen = new StackPane();
        homeScreen.getChildren().addAll(
            layout,
            duckLayer,
            uiLayer,
            inventoryLayer,
            popupLabel,
            angryStateLabel,
            vetButton,
            parkButton,
            storeButton
        );

        // Create the scene with the home screen and attach this Home instance as user data
        this.scene = new Scene(homeScreen, 800, 600);
        this.scene.setUserData(this);
    }

    /**
     * Returns the JavaFX scene representing the home screen.
     *
     * @return The scene of the home screen.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Checks whether the pet has died by evaluating its health or dead status.
     * If the pet is dead, this method disables all interactive buttons, displays a death message,
     * triggers the death animation, and stops game activities like stats tracking and coin generation.
     */
// Update the checkIfPetDied method to exclude the Store button
private void checkIfPetDied() {
    if (pet.getHealth() <= 0 || pet.isDead()) {
        // Disable all pet action buttons except the Store button
        feedPetButton.setDisable(true);
        petPlayButton.setDisable(true);
        giveGiftButton.setDisable(true);
        sleepButton.setDisable(true);
        vetButton.setDisable(true);
        parkButton.setDisable(true);
        
        // Keep the Store button enabled
        storeButton.setDisable(false);

        // Display a message that the pet has passed away
        popupLabel.setText("Your duck has passed away...");
        popupLabel.setVisible(true);
        popupDelay.playFromStart();

        // Start the pet's death animation
        pet.startDeadAnimation();

        // Mark pet as dead if it is not already to avoid repeated actions
        if (!pet.isDead()) {
            pet.setDead(true);
        }

        // Stop further game activities such as stats tracking and coin generation
        main.stopStatsTracking(pet);
        main.stopCoinGeneration();

        statsStopped = true;
    }
}
    
}
