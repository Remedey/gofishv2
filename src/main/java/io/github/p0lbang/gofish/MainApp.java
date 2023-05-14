package io.github.p0lbang.gofish;

import io.github.p0lbang.gofish.game.Game;
import javafx.animation.Interpolator;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

//Main class which extends from Application Class
public class MainApp extends Application {

    final int WINDOW_HEIGHT = 700;
    final int WINDOW_WIDTH = 1200;

    // This is our PrimaryStage (It contains everything)
    private Stage primaryStage;

    // This is the BorderPane of RootLayout
    @SuppressWarnings("FieldCanBeLocal")
    private StackPane rootLayout;

    private ArrayList<Button> playerDeckButtons = new ArrayList<>();
    private HashMap<Button, double[]> playerDeckButtonsSelected = new HashMap();

    Game gameLogic;
    @Override
    public void start(Stage primaryStage) {
        // 1) Declare a primary stage (Everything will be on this stage)
        this.primaryStage = primaryStage;

        // Optional: Set a title for primary stage
        this.primaryStage.setTitle("Go Fish");

        // 2) Initialize RootLayout
        initRootLayout();

        // 3) Display the EmployeeOperations View
        // showEmployeeOperationsView();
    }

    public void createSelectionButtons(String[] Ranks){
        ArrayList<Button> buttonlist = new ArrayList<>();

        int ranklen = 13;
        int halfrank = ranklen / 2;
        int transval = 25;

        for (int i = 0; i < ranklen ; i++) {
            Button temp = new Button(Ranks[i]);
            buttonlist.add(temp);
            temp.setOnAction(evt -> buttonClick(temp));
            temp.setTranslateX((i-halfrank)*transval);
        }
        rootLayout = new StackPane();
        rootLayout.getChildren().addAll(buttonlist);
    }


    public void displayPlayerDeck(String[] Deck){

        rootLayout.getChildren().removeAll(playerDeckButtons);
        playerDeckButtons.clear();

        int ranklen = Deck.length;
        int halfrank = ranklen / 2;
        int transval = 40;

        for (int i = 0; i < ranklen; i++) {
            String cardName = Deck[i].replace(":", "_"); // Replace ":" with "_"
            URL url = Objects.requireNonNull(MainApp.class.getResource(cardName + ".png"));
            Image cardImage = new Image(url.toString());
            ImageView imageView = new ImageView(cardImage);
            imageView.setFitWidth(75); // Set the desired width
            imageView.setFitHeight(125);
            Button temp = new Button();
            temp.setOnAction(evt -> giveCardAnimation(temp));
            temp.setGraphic(imageView);
            playerDeckButtons.add(temp);
            temp.setTranslateX((i - halfrank) * transval);
            temp.setTranslateY(200);
            rootLayout.getChildren().add(temp);
        }
    }

    public void giveCardAnimation(Button button) {
        final int MOVE = 50;
        // make button not clickable during transition
        button.setDisable(true);
        // dont change button color when disabled.
        button.setStyle("-fx-opacity: 1;");
        TranslateTransition trans_center = new TranslateTransition(new Duration(400), button);
        TranslateTransition trans_hide = new TranslateTransition(new Duration(400), button);
        TranslateTransition trans_pause = new TranslateTransition(new Duration(200), button);
        trans_center.setInterpolator(Interpolator.EASE_BOTH);
        trans_hide.setInterpolator(Interpolator.EASE_BOTH);

        SequentialTransition seqT = new SequentialTransition(trans_center);
        // on animation finish enable button
        seqT.setOnFinished(e -> {
            button.setDisable(false);
        });

        if (playerDeckButtonsSelected.containsKey(button)) {
            double[] coords = playerDeckButtonsSelected.get(button);
            trans_center.setToX(coords[0]);
            trans_center.setToY(coords[1]);
            playerDeckButtonsSelected.remove(button);
        } else {
            seqT = new SequentialTransition(trans_center, trans_pause, trans_hide);
            trans_center.setToX(0);
            trans_center.setToY(0);
            trans_hide.setToY(-500);

            double[] d = {button.getTranslateX(), button.getTranslateY()};
            playerDeckButtonsSelected.put(button, d);
        }
        seqT.play();
    }


    // Initializes the root layout.
    public void initRootLayout() {
        try {
            gameLogic = new Game();
            createSelectionButtons(gameLogic.deck.RANKS);
            Button gameloop = new Button("Game Loop");
            rootLayout.getChildren().add(gameloop);
            gameloop.setTranslateY(50);
            gameloop.setOnAction(evt -> runLoopAgain());

            //display the player's deck
            displayPlayerDeck(gameLogic.getPlayerHand("Player"));

            //Background Image
            URL url = Objects.requireNonNull(MainApp.class.getResource("background.png"));
            Image backgroundImage = new Image(url.toString());

            BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
            BackgroundImage backgroundImageObject = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
            Background background = new Background(backgroundImageObject);
            rootLayout.setBackground(background);
            /////////////////////////////////////////////////////////////
            // Second, show the scene containing the root layout.
            Scene scene = new Scene(rootLayout, WINDOW_WIDTH, WINDOW_HEIGHT);
            primaryStage.setScene(scene); // Set the scene in primary stage.
            primaryStage.setResizable(false);

            // Third, show the primary stage
            primaryStage.show(); // Display the primary stage
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buttonClick(Button button) {
        System.out.println(button.getText());
        gameLogic.displayPlayerHand("Bot");


    }

    private void runLoopAgain() {
        System.out.println("Run Gameloop again");
        gameLogic.gameloop();
        displayPlayerDeck(gameLogic.getPlayerHand("Player"));
    }

    // Shows the employee operations view inside the root layout.
    // public void showEmployeeOperationsView() {
    //     try {
    //         // First, load EmployeeOperationsView from EmployeeOperations.fxml
    //         FXMLLoader loader = new FXMLLoader();
    //         loader.setLocation(App.class.getResource("view/EmployeeOperations.fxml"));
    //         AnchorPane employeeOperationsView = (AnchorPane) loader.load();

    //         // Set Employee Operations view into the center of root layout.
    //         rootLayout.setCenter(employeeOperationsView);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
}