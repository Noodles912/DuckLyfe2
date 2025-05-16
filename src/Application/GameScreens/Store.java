package Application.GameScreens;

import java.util.HashMap;
import java.util.Map;

import Application.Components.PauseButton;
import Application.Duck;
import Application.GameSettingsAndStats;
import Application.Inventory;
import Application.Main;
import javafx.animation.PauseTransition;
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
 * The Store class represents the in-game store where players can purchase items
 * (Food, Toy, and Nightcap) using coins.
 * <br><br>
 * This screen displays the available items,handles purchase actions, updates the inventory,
 * and shows a popup message for feedback. It also provides navigation elements like the pause button,
 * coin counter, and a Home button.
 *
 * @author Dinith Nawaratne
 * @author Daniel Harapiak
 * @author Brandon Nguyen
 */
public class Store {
    private Scene scene;
    
    // Keeps track of the number of times each item has been purchased.
    private final Map<String, Integer> purchaseCounts = new HashMap<>();
    
    // A shared PauseTransition timer to control the visibility duration of popup messages.
    private final PauseTransition popupDelay = new PauseTransition(Duration.seconds(2));

    /**
     * Constructs the Store screen. Initializes UI elements, such as item buttons,
     * background images, coin counter, pause button, and Home button, and sets up their actions.
     *
     * @param main The main application instance used for scene management and global state.
     */
    public Store(Main main) {
        // Retrieve current game state objects.
        Duck pet = main.getDuck();
        Inventory inventory = main.getInventory();
        GameSettingsAndStats settings = main.getSettings();
        
        // Load the coin image and create an ImageView for displaying coins.
        Image coinImage = new Image("file:src/assets/coinFront.png");
        ImageView coinImageView = new ImageView(coinImage);
        coinImageView.setFitWidth(30);
        coinImageView.setFitHeight(30);
        
        // Stop pet stat tracking while in the store.
        main.stopStatsTracking(pet);

        // Retrieve and update the money label (coin counter) from the main application.
        Label moneyLabel = main.getCoinLabel(); 
        main.updateCoinLabelReference(moneyLabel);
        moneyLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: black; -fx-font-weight: bold;");
        moneyLabel.setGraphic(coinImageView);

        // Place the coin counter in an HBox, aligned at the top-right.
        HBox moneyCounter = new HBox(moneyLabel);
        moneyCounter.setAlignment(Pos.TOP_RIGHT);
        moneyCounter.setStyle("-fx-padding: 25;");

        // Create a pause button and place it in a container aligned at the top-left.
        PauseButton pauseButton = new PauseButton(main, pet, inventory, settings);
        HBox pauseContainer = new HBox(pauseButton);
        pauseContainer.setAlignment(Pos.TOP_LEFT);
        pauseContainer.setStyle("-fx-padding: 10;");

        // Create the main layout using a BorderPane and add it to a root StackPane.
        BorderPane layout = new BorderPane();
        StackPane root = new StackPane(layout);

        // Create a popup label for purchase confirmation and error messages.
        Label popupLabel = new Label();
        popupLabel.setStyle(
            "-fx-background-color: rgba(0, 0, 0, 0.75); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 18px; " +
            "-fx-padding: 10 20; " +
            "-fx-background-radius: 10;"
        );
        popupLabel.setVisible(false);
        popupLabel.setId("popupLabel");
        // Position the popup label at the top-center with a slight vertical offset.
        StackPane.setAlignment(popupLabel, Pos.TOP_CENTER);
        popupLabel.setTranslateY(50);
        // Configure the popupDelay to hide the popup label after the specified time.
        popupDelay.setOnFinished(event -> popupLabel.setVisible(false));
        // Add the popup label to the root container.
        root.getChildren().add(popupLabel);

        // Create buttons for each purchasable item.
        Button foodButton = createItemButton("Food", "file:src/assets/food011.png", 2, popupLabel, inventory, pet, main);
        Button toyButton = createItemButton("Toy", "file:src/assets/toy.png", 5, popupLabel, inventory, pet, main);
        Button nightcapButton = createItemButton("Nightcap", "file:src/assets/sleepingMask.png", 10, popupLabel, inventory, pet, main);
        
        // Load the background image for the store.
        var backgroundImage = new Image("file:src/assets/storeBackground.png");
        BackgroundImage background = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        // Set the background for the layout.
        layout.setBackground(new Background(background));
        // Place the pause button container on the left and the money counter on the right.
        layout.setLeft(pauseContainer);
        layout.setRight(moneyCounter);

        // Define a common style for item buttons.
        String buttonStyle = "-fx-font-size: 16px; -fx-background-color: rgb(83, 69, 67); " +
                             "-fx-text-fill: black; -fx-background-radius: 10; -fx-padding: 10 20;";
        foodButton.setStyle(buttonStyle);
        toyButton.setStyle(buttonStyle);
        nightcapButton.setStyle(buttonStyle);

        // Create descriptive labels for each item.
        Label foodDescription = new Label("Restores hunger");
        foodDescription.setStyle("-fx-font-size: 14px; -fx-text-fill: white; " +
                                  "-fx-background-color: rgb(83, 69, 67); -fx-background-radius: 10; -fx-padding: 10 30;");
        VBox foodBox = new VBox(5, foodButton, foodDescription);
        foodBox.setStyle("-fx-alignment: center;");

        Label toyDescription = new Label("Increases happiness");
        toyDescription.setStyle("-fx-font-size: 14px; -fx-text-fill: white; " +
                                 "-fx-background-color: rgb(83, 69, 67); -fx-background-radius: 10; -fx-padding: 10 20;");
        VBox toyBox = new VBox(5, toyButton, toyDescription);
        toyBox.setStyle("-fx-alignment: center;");

        Label nightcapDescription = new Label("Improves sleep");
        nightcapDescription.setStyle("-fx-font-size: 14px; -fx-text-fill: white; " +
                                      "-fx-background-color: rgb(83, 69, 67); -fx-background-radius: 10; -fx-padding: 10 30;");
        VBox nightcapBox = new VBox(5, nightcapButton, nightcapDescription);
        nightcapBox.setStyle("-fx-alignment: center;");

        // Arrange the item boxes in an HBox with spacing.
        HBox itemBox = new HBox(30, foodBox, toyBox, nightcapBox);
        itemBox.setStyle("-fx-alignment: center;");

        // Create a container for the store items and position it in the center of the layout.
        VBox storeBox = new VBox(20, itemBox);
        storeBox.setStyle("-fx-alignment: center; -fx-padding: 200 0 0 0;");
        layout.setCenter(storeBox);

        // Create a Home button to navigate back to the Home screen.
        Button homeButton = new Button("Home");
        homeButton.setStyle("-fx-font-size: 16px; -fx-background-color: rgb(135, 206, 250); " +
                            "-fx-text-fill: black; -fx-background-radius: 10; -fx-padding: 20 40;");
        homeButton.setOnAction(e -> {
            // Navigate to the Home screen.
            Home home = new Home(main, pet, inventory, settings);
            main.setScene(home.getScene());
        });
        // Position the Home button at the bottom-left of the screen.
        StackPane.setAlignment(homeButton, Pos.BOTTOM_LEFT);
        homeButton.setTranslateX(15);
        homeButton.setTranslateY(-15);
        root.getChildren().add(homeButton);

        // Initialize the scene with the root layout and fixed dimensions.
        this.scene = new Scene(root, 800, 600);
        this.scene.setUserData(this);
    }

    /**
     * Creates a button for purchasing a specified item.
     *
     * @param itemName   The name of the item (e.g., "Food", "Toy", "Nightcap").
     * @param imagePath  The file path for the item's image.
     * @param cost       The cost of the item in coins.
     * @param popupLabel The label used to display purchase confirmation or error messages.
     * @param inventory  The Inventory instance to update on purchase.
     * @param pet        The current Duck instance used for coin transactions.
     * @param main       The main application instance for updating UI elements.
     * @return A Button configured to handle the purchase of the specified item.
     */
    private Button createItemButton(String itemName, String imagePath, int cost, Label popupLabel, Inventory inventory, Duck pet, Main main) {
        // Create an empty button; the content will be set using a graphic.
        Button itemButton = new Button();
        
        // Load the image for the item.
        Image itemImage = new Image(imagePath);
        ImageView itemImageView = new ImageView(itemImage);
        itemImageView.setFitWidth(80);
        itemImageView.setFitHeight(80);

        // Load the coin image for cost display.
        var coinImage = new Image("file:src/assets/coinFront.png");
        var coinImageView = new ImageView(coinImage);
        coinImageView.setFitWidth(20);
        coinImageView.setFitHeight(20);

        // Create a label to show the cost, using the coin image as a graphic.
        Label costLabel = new Label(" " + cost);
        costLabel.setGraphic(coinImageView);
        costLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        // Create a VBox to hold the item image and cost label.
        VBox buttonContent = new VBox(10, itemImageView, costLabel);
        buttonContent.setStyle("-fx-alignment: center; -fx-padding: 10; -fx-background-color: rgb(83, 69, 67); -fx-background-radius: 10;");
        buttonContent.setPrefSize(120, 150);
        // Set the VBox as the graphic for the item button.
        itemButton.setGraphic(buttonContent);

        // Set the action for when the item button is clicked.
        itemButton.setOnAction(e -> {
            // Check if the pet has enough coins to purchase the item.
            if (pet.getCoins() >= cost) {
                // Deduct the cost from the pet's coins.
                pet.setCoins(-cost);
                // Update the coin label in the UI.
                main.updateCoinLabel();
                System.out.println(itemName + " purchased!");
        
                // Update the purchase count for the item.
                int count = purchaseCounts.getOrDefault(itemName, 0) + 1;
                purchaseCounts.put(itemName, count);
        
                // Update the inventory based on the purchased item.
                switch (itemName) {
                    case "Food": inventory.setFood(1); break;
                    case "Toy": inventory.setToy(1); break;
                    case "Nightcap": inventory.setNightCap(1); break;
                }
        
                // Print the updated inventory to the console.
                inventory.printItems();
        
                // Show a popup message indicating the purchase.
                popupLabel.setText(itemName + " purchased! x" + count);
                popupLabel.setVisible(true);
                popupDelay.stop();
                popupDelay.play();
            } else {
                // Show an error message if there are not enough coins.
                popupLabel.setText("Not enough money for " + itemName + "!");
                popupLabel.setVisible(true);
                popupDelay.stop();
                popupDelay.play();
            }
        });

        return itemButton;
    }

    /**
     * Returns the JavaFX Scene representing the Store screen.
     *
     * @return The scene containing the store interface.
     */
    public Scene getScene() {
        return scene;
    }
}
