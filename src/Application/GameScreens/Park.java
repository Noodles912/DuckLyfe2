package Application.GameScreens;

import Application.Components.PauseButton;
import Application.Duck;
import Application.GameSettingsAndStats;
import Application.Inventory;
import Application.Main;
import Application.UserInterface;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Represents the Park screen in the DuckLyfe2 application.
 * <br><br>
 * This class provides functionality for walking the pet, managing background
 * scrolling animations, and updating the user interface. It also tracks the
 * pet's stats such as happiness and tiredness during the walk.
 * 
 * @author Dinith Nawaratne
 * @author Daniel Harapiak 
 * @author Brandon Nguyen
 */
public class Park {
    private Scene scene;
    private Duck pet;
    private Timeline walkTimeline;
    private Timeline backgroundScrollTimeline;
    private ImageView bg1, bg2;
    private double scrollSpeed = 2;
    private double backgroundWidth;
    private Button walkButton;
    private Button stopButton;
    private boolean isWalking = false;
    private PauseTransition walkDelayStart;
    private UserInterface userInterface;

    /**
     * Constructs the Park screen and initializes its UI components.
     * <br><br>
     * This constructor sets up the layout, buttons, background animations,
     * and user interface for the Park screen.
     *
     * @param main The main application instance.
     */

    public Park(Main main) {
        this.pet = main.getDuck();
        Inventory inventory = main.getInventory();
        GameSettingsAndStats settings = main.getSettings();

        main.initializeStatsTracking(pet);
        main.startStatsTracking();

        walkButton = new Button("Go for a (W)alk");
        stopButton = new Button("Stop");

        String buttonStyle = "-fx-font-size: 16px; -fx-background-color: rgb(255, 248, 59); -fx-text-fill: black; -fx-background-radius: 10; -fx-padding: 10 20;";
        walkButton.setStyle(buttonStyle);
        stopButton.setStyle(buttonStyle);

        Label popupLabel = new Label();
        popupLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75); -fx-text-fill: white; -fx-font-size: 18px; -fx-padding: 10 20; -fx-background-radius: 10;");
        popupLabel.setVisible(false);
        popupLabel.setId("popupLabel");

        if (pet.getTiredness() >= 100) {
            walkButton.setDisable(true);
        }

        walkButton.setOnAction(e -> {
            isWalking = true;
            walkButton.setDisable(true);
            stopButton.setDisable(false);
            pet.startWalkingAnimation();
            startWalking();
        });

        stopButton.setOnAction(e -> {
            isWalking = false;
            walkButton.setDisable(false);
            stopButton.setDisable(true);
            if (walkTimeline != null) walkTimeline.stop();
            pet.stopWalkingAnimation();
            stopBackgroundScroll();
        });

        stopButton.setDisable(true);

        Image bgImage = new Image("file:src/assets/parkBackground.png");
        backgroundWidth = bgImage.getWidth();
        bg1 = new ImageView(bgImage);
        bg2 = new ImageView(bgImage);
        bg1.setPreserveRatio(false);
        bg2.setPreserveRatio(false);
        bg1.setSmooth(true);
        bg2.setSmooth(true);
        Pane backgroundLayer = new Pane(bg1, bg2);
        bg1.setX(0);
        bg2.setX(backgroundWidth);
        bg1.fitHeightProperty().bind(backgroundLayer.heightProperty());
        bg2.fitHeightProperty().bind(backgroundLayer.heightProperty());

        BorderPane layout = new BorderPane();
        layout.setTop(new HBox(new PauseButton(main, pet, inventory, settings)));

        VBox actionBox = new VBox(20, walkButton, stopButton);
        actionBox.setStyle("-fx-alignment: center-left; -fx-padding: 200 0 0 50;");
        layout.setLeft(actionBox);

        StackPane centerStack = new StackPane(pet.getDuckImageView());
        centerStack.setStyle("-fx-alignment: bottom-center;");
        pet.getDuckImageView().setTranslateY(-60);
        pet.getDuckImageView().setTranslateX(-100);
        layout.setCenter(centerStack);

        userInterface = new UserInterface(settings, pet, main.getCoinLabel());
        StackPane uiLayer = new StackPane(userInterface.getScene().getRoot());
        StackPane.setAlignment(userInterface.getScene().getRoot(), javafx.geometry.Pos.BOTTOM_RIGHT);
        uiLayer.setMouseTransparent(true);
        main.updateUserInterface(userInterface);

        Button homeButton = new Button("Home");
        homeButton.setStyle("-fx-font-size: 16px; -fx-background-color: rgb(135, 206, 250); -fx-text-fill: black; -fx-background-radius: 10; -fx-padding: 20 40;");
        homeButton.setOnAction(e -> {
            if (walkDelayStart != null) walkDelayStart.stop();
            if (walkTimeline != null) walkTimeline.stop();
            if (backgroundScrollTimeline != null) backgroundScrollTimeline.stop();
            isWalking = false;
            pet.stopWalkingAnimation();
            walkButton.setDisable(false);
            stopButton.setDisable(true);
            main.stopStatsTracking(pet);
            main.setScene(new Home(main, pet, inventory, settings).getScene());
        });

        StackPane.setAlignment(homeButton, javafx.geometry.Pos.BOTTOM_LEFT);
        homeButton.setTranslateX(15);
        homeButton.setTranslateY(-15);

        StackPane root = new StackPane(backgroundLayer, layout, uiLayer, homeButton, popupLabel);
        this.scene = new Scene(root, 800, 600);
        this.scene.setUserData(this);

        backgroundLayer.prefWidthProperty().bind(scene.widthProperty().multiply(2));
        backgroundLayer.prefHeightProperty().bind(scene.heightProperty());
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            bg2.setX(bg1.getX() + backgroundWidth);
        });
    }

    /**
     * Starts the walking activity for the pet.
     * <br><br>
     * This method increases the pet's happiness and tiredness over time while
     * the walk is active. It also starts the background scrolling animation.
     * If the pet's tiredness reaches 100, the walk is stopped automatically.
     */

    private void startWalking() {
        walkTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (pet.getTiredness() < 100) {
                pet.setHappiness(2);
                pet.setTiredness(5);
                userInterface.updateStatsDisplay();
                userInterface.updateHealth();
                userInterface.updateHunger();
                userInterface.updateCoinDisplay();
            } else {
                System.out.println("Pet is too tired to walk!");
                walkTimeline.stop();
                stopBackgroundScroll();
                walkButton.setDisable(true);
                stopButton.setDisable(true);
                pet.stopWalkingAnimation();
            }
        }));
        walkTimeline.setCycleCount(Timeline.INDEFINITE);

        walkDelayStart = new PauseTransition(Duration.millis(50));
        walkDelayStart.setOnFinished(e -> {
            walkTimeline.play();
            startBackgroundScroll();
        });
        walkDelayStart.play();
    }

    /**
     * Starts the background scrolling animation.
     * <br><br>
     * This method creates a smooth scrolling effect for the background images
     * to simulate movement during the walk.
     */

    private void startBackgroundScroll() {
        backgroundScrollTimeline = new Timeline(new KeyFrame(Duration.millis(20), e -> {
            bg1.setX(bg1.getX() - scrollSpeed);
            bg2.setX(bg2.getX() - scrollSpeed);
            if (bg1.getX() + backgroundWidth <= 0) {
                bg1.setX(bg2.getX() + backgroundWidth);
            }
            if (bg2.getX() + backgroundWidth <= 0) {
                bg2.setX(bg1.getX() + backgroundWidth);
            }
        }));
        backgroundScrollTimeline.setCycleCount(Timeline.INDEFINITE);
        backgroundScrollTimeline.play();
    }

    /**
     * Stops the background scrolling animation.
     * <br><br>
     * This method halts the scrolling effect for the background images.
     */

    private void stopBackgroundScroll() {
        if (backgroundScrollTimeline != null) {
            backgroundScrollTimeline.stop();
        }
    }

    /**
     * Retrieves the scene associated with the Park screen.
     * <br><br>
     * This method returns the JavaFX Scene object that represents the Park screen.
     *
     * @return The Scene object for the Park screen.
     */

    public Scene getScene() {
        return scene;
    }
}
