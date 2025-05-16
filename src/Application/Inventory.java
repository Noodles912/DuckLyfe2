package Application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Represents the in-game inventory system.
 * <p>
 * The Inventory class manages three types of items: food, toy, and night cap. It loads images for each item,
 * creates corresponding labels, and arranges them in a JavaFX scene. The class provides getter and setter methods 
 * to update the item counts and refreshes the display accordingly.
 * </p>
 *
 * @author Brandon Nguyen
 * @author Dinith Nawaratne
 * @author Zachary Goodman
 */
public class Inventory {
    // Fields to track the quantity of each inventory item.
    private int food;
    private int toy;
    private int nightCap;
    // The JavaFX Scene that holds the inventory display.
    private Scene scene;

    // Label references for each inventory item used to update the UI.
    private Label foodLabel;
    private Label toyLabel;
    private Label nightCapLabel;

    /**
     * Constructs a new Inventory instance.
     * <p>
     * This constructor loads images for food, toy, and night cap items, creates corresponding ImageViews and labels,
     * and arranges them within a VBox which is then added to a BorderPane layout. A Scene is created to encapsulate
     * this layout.
     * </p>
     */
    public Inventory() {
        // Load the image for food and create an ImageView.
        Image foodImage = new Image("file:src/assets/food011.png");
        ImageView foodImageView = new ImageView(foodImage);
        foodImageView.setFitWidth(30);  // Set image width
        foodImageView.setFitHeight(30); // Set image height

        // Load the image for toy and create an ImageView.
        Image toyImage = new Image("file:src/assets/toy.png");
        ImageView toyImageView = new ImageView(toyImage);
        toyImageView.setFitWidth(30);  // Set image width
        toyImageView.setFitHeight(30); // Set image height

        // Load the image for night cap and create an ImageView.
        Image nightCapImage = new Image("file:src/assets/sleepingMask.png");
        ImageView nightCapImageView = new ImageView(nightCapImage);
        nightCapImageView.setFitWidth(30);  // Set image width
        nightCapImageView.setFitHeight(30); // Set image height

        // Create labels for each item with their current count (initially 0) and assign the corresponding ImageView.
        foodLabel = new Label(" x " + food);
        foodLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: black; -fx-font-weight: bold;");
        foodLabel.setGraphic(foodImageView); // Attach food image to the label

        toyLabel = new Label(" x " + toy);
        toyLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: black; -fx-font-weight: bold;");
        toyLabel.setGraphic(toyImageView); // Attach toy image to the label

        nightCapLabel = new Label(" x " + nightCap);
        nightCapLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: black; -fx-font-weight: bold;");
        nightCapLabel.setGraphic(nightCapImageView); // Attach night cap image to the label

        // Arrange the labels in a vertical box layout.
        VBox inventoryDisplay = new VBox();
        inventoryDisplay.getChildren().addAll(foodLabel, toyLabel, nightCapLabel);
        inventoryDisplay.setAlignment(Pos.TOP_LEFT); // Align items to the top left corner
        inventoryDisplay.setStyle("-fx-padding: 150 0 0 30;"); // Set padding for the display

        // Create a BorderPane layout and place the inventory display on the left side.
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: transparent;"); // Set transparent background
        layout.setLeft(inventoryDisplay);

        // Create a new Scene with the layout and set the dimensions.
        this.scene = new Scene(layout, 800, 600);
    }

    /**
     * Retrieves the JavaFX Scene containing the inventory display.
     *
     * @return the Scene representing the inventory UI
     */
    public Scene getScene() {
        // Return the inventory Scene.
        return scene;
    }

    /**
     * Retrieves the current quantity of food in the inventory.
     *
     * @return the food count
     */
    public int getFood() {
        // Return the current food count.
        return food;
    }

    /**
     * Retrieves the current quantity of toys in the inventory.
     *
     * @return the toy count
     */
    public int getToy() {
        // Return the current toy count.
        return toy;
    }

    /**
     * Retrieves the current quantity of night caps in the inventory.
     *
     * @return the night cap count
     */
    public int getNightCap() {
        // Return the current night cap count.
        return nightCap;
    }

    /**
     * Updates the quantity of food in the inventory.
     * <p>
     * The provided change is added to the current food count, and the UI labels are updated accordingly.
     * </p>
     *
     * @param change the amount to change the food count (can be positive or negative)
     */
    public void setFood(int change) {
        food += change;  // Update food count
        updateLabels();  // Refresh the UI labels to show the new count
    }

    /**
     * Updates the quantity of toys in the inventory.
     * <p>
     * The provided change is added to the current toy count, and the UI labels are updated accordingly.
     * </p>
     *
     * @param change the amount to change the toy count (can be positive or negative)
     */
    public void setToy(int change) {
        toy += change;   // Update toy count
        updateLabels();  // Refresh the UI labels to show the new count
    }

    /**
     * Updates the quantity of night caps in the inventory.
     * <p>
     * The provided change is added to the current night cap count, and the UI labels are updated accordingly.
     * </p>
     *
     * @param change the amount to change the night cap count (can be positive or negative)
     */
    public void setNightCap(int change) {
        nightCap += change;  // Update night cap count
        updateLabels();      // Refresh the UI labels to show the new count
    }

    /**
     * Refreshes the UI labels to match the current inventory values.
     * <p>
     * This method updates the text of each label (food, toy, night cap) to reflect the current counts.
     * </p>
     */
    public void updateLabels() {
        // Update each label with the new counts.
        foodLabel.setText(" x " + food);
        toyLabel.setText(" x " + toy);
        nightCapLabel.setText(" x " + nightCap);
    }

    /**
     * Prints the current inventory counts to the console.
     * <p>
     * This is useful for debugging or logging the current state of the inventory.
     * </p>
     */
    public void printItems() {
        // Output the current counts of each item.
        System.out.println("Food: " + food);
        System.out.println("Toy: " + toy);
        System.out.println("Nightcap: " + nightCap);
    }
}
