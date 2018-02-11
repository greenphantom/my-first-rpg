package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.effect.*;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.geometry.VPos;

import java.io.File;
import java.net.MalformedURLException;

public class MainMenu {
    private Stage mainStage;
    private Scene mainScene, startingScene, characterCreationScene, gameOverScene, introScene;
    private Player player;
    private Main main;
    private boolean isMenuOpen = true;
    private int spriteNumber;
    public MainMenu(Player player, GraphicsContext gc, Canvas canvas, Stage mainStage, Scene mainScene, Main main) {
        this.player = player;
        this.mainStage = mainStage;
        this.mainScene = mainScene;
        this.main = main;
    }

    public void openMainMenu()
    {
        GridPane mainMenu = new GridPane();
        mainMenu.setAlignment(Pos.TOP_CENTER);
        mainMenu.setVgap(40);

        GridPane titleArea = new GridPane();
        titleArea.setAlignment(Pos.CENTER);
        //titleArea.setVgap(40);

        //Main Menu (first scene)
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(mainMenu);
        titleArea.setPrefHeight(200);
        borderPane.setTop(titleArea);


        startingScene = new Scene( borderPane , 512 , 512);
        mainStage.setScene( startingScene );



        //Set Background Image
        File file = new File(System.getProperty("user.dir") + "/Game/src/sample/sprites/lava.png");
        Image i = null;
        //System.out.println(System.getProperty("user.dir"));
        try {
            i = new Image(file.toURI().toURL().toString(), mainMenu.getWidth(), mainMenu.getHeight(), false, false);
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        BackgroundImage im = new BackgroundImage(i, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, false, false, false, true));

        borderPane.setBackground(new Background(im));

        //Main Menu title text
        Text gameTitle = new Text("THE SEARCH FOR THE HYPE");
        Bloom bloom = new Bloom();
        bloom.setThreshold(0.5);
        DropShadow titleShadow = new DropShadow();
        titleShadow.setOffsetY(3.0f);
        titleShadow.setColor(Color.BLACK);
        gameTitle.setEffect(bloom);
        gameTitle.setCache(true);
        //gameTitle.setX(50.0f);
        //gameTitle.setY(0.0f);
        gameTitle.setFill(Color.WHITE);
        gameTitle.setFont(Font.font(null, FontWeight.BOLD, 32));



        //Three main menu buttons
        Button newGameButton = new Button("New Game");
        newGameButton.setOnAction(e -> characterCreation());//mainStage.setScene(characterCreationScene));

        Button loadGameButton = new Button("Load Game");
        loadGameButton.setOnAction(e -> {
            //workingDir = System.getProperty("user.dir");
            // if workingDir.save.exists TODO implement this
            main.loadGame();
            mainStage.setScene(mainScene);
        }); // Change to Load() later

        Button exitGameButton = new Button("Exit Game");
        exitGameButton.setOnAction(e -> mainStage.close());

        titleArea.add( gameTitle,0,0);

        mainMenu.add( newGameButton, 0,0);
        mainMenu.add( loadGameButton, 0,1);
        mainMenu.add( exitGameButton, 0,2);
    }

    // Generates the character creation menu
    public void characterCreation()
    {
        //Character Creation
        GridPane characterCreation = new GridPane();
        characterCreationScene = new Scene( characterCreation, 512, 512);
        characterCreation.setAlignment(Pos.CENTER);
        characterCreation.setVgap(30);
        characterCreation.setHgap(90);
        characterCreation.setBackground(new Background(new BackgroundFill(Color.ROSYBROWN, CornerRadii.EMPTY, Insets.EMPTY)));

        mainStage.setScene(characterCreationScene);

        // Player name box
        TextField nameInput = new TextField();

        // Labels for character creation
        Label chooseName = new Label("Type your character's name");
        chooseName.setFont(Font.font("Verdana",14));
        Label chooseSprite = new Label("Select a character class");
        chooseSprite.setFont(Font.font("Verdana",14));
        Label chooseColor = new Label("Select a stat to boost");
        chooseColor.setFont(Font.font("Verdana",14));

        // Character Creation Choices
        ChoiceBox characterStatAdvantage = new ChoiceBox(FXCollections.observableArrayList("Health","Attack","Defense"));
        characterStatAdvantage.setValue("Health"); //Setting a default choice

        ChoiceBox characterSprites = new ChoiceBox(FXCollections.observableArrayList("Guy","Girl","Adventurer","Soldier"));
        characterSprites.setValue("Guy"); //Setting a default choice

        Image characterSprite1 = new Image("file:" + System.getProperty("user.dir") + "/Game/src/sample/sprites/characterGuy.png");
        Image characterSprite2 = new Image("file:" + System.getProperty("user.dir") + "/Game/src/sample/sprites/characterGirl.png");
        Image characterSprite3 = new Image("file:" + System.getProperty("user.dir") + "/Game/src/sample/sprites/characterAdventurer.png");
        Image characterSprite4 = new Image("file:" + System.getProperty("user.dir") + "/Game/src/sample/sprites/characterSoldier.png");

        player.setPlayerSpriteNumber(1);

        ImageView imageView = new ImageView(characterSprite1);

        Image[] spriteChoices = {characterSprite1,characterSprite2,characterSprite3,characterSprite4};

        // Sets size of sprite choice
        imageView.setFitHeight(75);
        imageView.setFitWidth(75);

        // Watches for users choice in selection box
        characterSprites.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        //label.setText(spriteChoices[newValue.intValue()]);
                        imageView.setImage(spriteChoices[newValue.intValue()]);
                        spriteNumber = (newValue.intValue()+1);
                    }
                }
        );

        Text noNameCatch = new Text("PLEASE ENTER A NAME");
        noNameCatch.setFont(Font.font("Verdana",FontWeight.BOLD,16));
        noNameCatch.setFill(Color.RED);
        noNameCatch.setVisible(false);

        // Back Button
        Button characterCreationBackButton = new Button("Back");
        characterCreationBackButton.setOnAction(e -> mainStage.setScene(startingScene));

        // Continue Button
        Button characterCreationContinueButton = new Button("Continue");
        characterCreationContinueButton.setOnAction(e -> {

            if(nameInput.getText().isEmpty() == false)
            {
                // Applies advantage to player
                if (characterStatAdvantage.getValue() == "Health")
                    player.setHealth(110);
                else if (characterStatAdvantage.getValue() == "Attack")
                    player.setAttackPoints(8);
                else if (characterStatAdvantage.getValue() == "Defense")
                    player.setDefensePoints(8);
                player.setName(nameInput.getText());
                player.setPlayerSprite(imageView.getImage());
                main.newGame(player.getName(), spriteNumber);
                isMenuOpen = false;
                //System.out.println(player.getName());
                //mainStage.setScene(mainScene);
                //System.out.println(player.getPlayerSpriteNumber());
                openIntroMenu();
            }
            else {
                System.out.println("Enter a character name!");
                noNameCatch.setVisible(true);
            }
        });

        // Adding buttons and text to characterCreation
        characterCreation.add(chooseName,1,0);
        characterCreation.add(nameInput,1,1);
        characterCreation.add(chooseSprite,1,2);
        characterCreation.add(characterSprites,1,3);
        characterCreation.add(chooseColor,1,4);
        characterCreation.add(characterStatAdvantage,1,5);
        characterCreation.add(characterCreationContinueButton,2,7);
        characterCreation.add(characterCreationBackButton,0,7);
        characterCreation.add(imageView,2,3);
        characterCreation.add(noNameCatch,1,7);
    }

    private void openIntroMenu() {

        Text introText = new Text("Welcome to the world of the HYPE");
        introText.setFont(Font.font(50));
        introText.setTextOrigin(VPos.TOP);
        introText.setFill(Color.RED);
        Pane introMenu = new Pane(introText);

        introScene = new Scene(introMenu,512,512);
        double sceneWidth = introScene.getWidth();
        double msgWidth = introText.getLayoutBounds().getWidth();

        mainStage.setScene(introScene);

        // Sets the time frame for text scroll
        KeyValue initialKeyValue = new KeyValue(introText.translateXProperty(), sceneWidth);
        KeyFrame initialFrame = new KeyFrame(Duration.ZERO, initialKeyValue);
        KeyValue endKeyValue = new KeyValue(introText.translateXProperty(), -1.0 * msgWidth);
        KeyFrame endFrame = new KeyFrame(Duration.seconds(5), endKeyValue);
        Timeline timeline = new Timeline(initialFrame, endFrame);

        timeline.setCycleCount(Timeline.INDEFINITE); // Or change to an int value
        timeline.play();

        Button startGameButton = new Button("Start");
        startGameButton.setAlignment(Pos.CENTER);
        startGameButton.setLayoutX(185);
        startGameButton.setLayoutY(200);
        startGameButton.setMinSize(140,10);
        startGameButton.setStyle("-fx-font-size: 4em; "); //CSS
        startGameButton.setOnAction(e -> mainStage.setScene(mainScene));

        introMenu.getChildren().add(startGameButton);
    }

    public void openGameMenu() {

        GridPane gameMenu = new GridPane();
        gameMenu.setAlignment(Pos.CENTER);
        gameMenu.setVgap(40);
        Scene gameMenuScene = new Scene( gameMenu , 800 , 800);

        Button continueGameButton = new Button("Continue");
        continueGameButton.setOnAction(e -> mainStage.setScene(mainScene));

        Button saveGameButton = new Button("Save");
        saveGameButton.setOnAction(e -> {
            System.out.println("Save has been pressed");
            main.saveGame();
        }); //SaveGame()

        Button goToMainMenuButton = new Button("Exit to main menu");
        goToMainMenuButton.setOnAction(e -> openMainMenu()); //mainStage.setScene(startingScene));

        Button exitGameButton = new Button("Exit Game");
        exitGameButton.setOnAction(e -> mainStage.close());

        gameMenu.add( continueGameButton, 0,0);
        gameMenu.add( saveGameButton, 0,1);
        gameMenu.add( goToMainMenuButton, 0,2);
        gameMenu.add( exitGameButton, 0,3);

        mainStage.setScene(gameMenuScene);

    }




    // Opens gameOver screen
    public void gameOver()
    {
        GridPane gameOver = new GridPane();
        gameOver.setAlignment(Pos.CENTER);
        gameOver.setVgap(40);
        gameOver.setHgap(100);

        gameOverScene = new Scene( gameOver, 800, 800);

        Text gameOverText = new Text("Game Over");
        gameOverText.setFont(Font.font("Verdana",FontWeight.BOLD,60));
        gameOverText.setFill(Color.DARKRED);

        //Image sadCatFace = new Image("file:" + System.getProperty("user.dir") + "/Game/src/sample/sprites/crying-cat-face.png");
        //ImageView catView = new ImageView(sadCatFace);

        Button goToMainMenuButtonFromGameOver = new Button("  Exit  ");
        goToMainMenuButtonFromGameOver.setOnAction(e -> openMainMenu());//mainStage.setScene(startingScene));


        //Set Background Image
        File file = new File(System.getProperty("user.dir") + "/Game/src/sample/sprites/crying-cat-face.png");
        Image i = null;
        //System.out.println(System.getProperty("user.dir"));
        try {
            i = new Image(file.toURI().toURL().toString(), gameOver.getWidth(), gameOver.getHeight(), false, false);
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        BackgroundImage im = new BackgroundImage(i, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, false, false, false, true));

        gameOver.setBackground(new Background(im));

        gameOver.add(gameOverText,1,0);
        //gameOver.add(catView,1,3);
        gameOver.add(goToMainMenuButtonFromGameOver,2,6);
        mainStage.setScene(gameOverScene);
    }

    public boolean getIsMenuOpen()
    {
        return isMenuOpen;
    }

}
