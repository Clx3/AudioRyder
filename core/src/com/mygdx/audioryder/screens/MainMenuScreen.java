package com.mygdx.audioryder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.audioryder.AudioRyder;
import com.mygdx.audioryder.properties.Properties;
import com.mygdx.audioryder.song.Song;

/**
 * Created by Teemu on 17.3.2018.
 */

public class MainMenuScreen implements Screen {

    AudioRyder game;

    Stage currentStage;
    Stage backgroundStage;
    private Stage mainStage;
    private Stage selectSongStage;
    Stage settingsStage;

    private Image background;
    private Image audioRyderText;

    MenuButton playButton;
    MenuButton settingsButton;
    MenuButton exitButton;
    Label playerNameText;
    //MenuButton returnButton;
    Label sensitivityText;
    Label sensitivityText2;
    Label leftSensText;
    Label rightSensText;
    Label downSensText;
    Label upSensText;
    Label gameSpeedText;
    Label gameSpeedText2;
    TextButton calibrateButton;
    TextField nameField;


    private Viewport viewport;

    public MainMenuScreen(AudioRyder game) {
        this.game = game;

        game.cam2D.setToOrtho(false, game.ORTHOCAM_VIEWPORT_WIDTH, game.ORTHOCAM_VIEWPORT_HEIGHT);
        game.batch.setProjectionMatrix(game.cam2D.combined);

        viewport = new FitViewport(game.ORTHOCAM_VIEWPORT_WIDTH, game.ORTHOCAM_VIEWPORT_HEIGHT, game.cam2D);
        viewport.apply();

        Texture backgroundTexture = new Texture(Gdx.files.internal(game.SPRITES_PATH + "menubackground.jpg"));
        TextureRegion backgroundRegion = new TextureRegion(backgroundTexture,0,0, game.WINDOW_WIDTH, game.WINDOW_HEIGHT);
        background = new Image(backgroundRegion);

        Texture audioRyderTextTexture = new Texture(Gdx.files.internal(game.SPRITES_PATH + "audioryder.png"));
        TextureRegion audioRyderTextRegion = new TextureRegion(audioRyderTextTexture);
        audioRyderText = new Image(audioRyderTextRegion);
        audioRyderText.setSize(750f, 200f);
        audioRyderText.setPosition(game.WINDOW_WIDTH / 2 - audioRyderText.getWidth() / 2, game.WINDOW_HEIGHT - audioRyderText.getHeight());


        setupBackgroundStage();
        setupMainStage();
        setupSelectSongStage();
        setupSettingsStage();
    }

    /**
     * This class is used to construct the buttons
     * used by the menu.
     */
    class MenuButton extends TextButton {

        public MenuButton(String text, Skin skin) {
            super(text, skin);
            setWidth(230f);
            setHeight(70f);
        }

    }

    class SongButton extends TextButton {

        private Song song;

        public SongButton(Song song, Skin skin) {
            super(song.getName(), skin, "songbutton");
            setSong(song);
            setWidth(230f);
            setHeight(60f);

            addListener(new ClickListener() {
               @Override
                public void clicked(InputEvent event, float x, float y) {
                   setChecked(true);
                   game.currentSong = getSong();
                   System.out.println(game.currentSong.getName());
                }
            });
        }

        public Song getSong() {
            return song;
        }

        public void setSong(Song song) {
            this.song = song;
        }
    }

    @Override
    public void show() {
        setCurrentStage(mainStage);
    }

    /**
     * This method basically setups the backgroundStage
     * which is used to draw the background of the
     * MainMenuScreen.
     */
    private void setupBackgroundStage() {
        backgroundStage = new Stage(viewport, game.batch);

        backgroundStage.addActor(background);
    }

    /**
     * This method setups the "Main" stage
     * of the menu. This is made
     * only for readability purposes.
     */
    private void setupMainStage() {
        mainStage = new Stage(viewport, game.batch);

        Table mainMenuTable = new Table();
        mainMenuTable.setFillParent(true);

        /* Creating the buttons: */
        playButton = new MenuButton(Properties.playText, game.skin);
        settingsButton = new MenuButton(Properties.settingsText, game.skin);
        exitButton = new MenuButton(Properties.exitText, game.skin);

        ImageButton changeLanguangeButton = new ImageButton(game.skin, "flag");
        changeLanguangeButton.setSize(50f,50f);
        changeLanguangeButton.setPosition(game.ORTHOCAM_VIEWPORT_WIDTH / 2 - changeLanguangeButton.getWidth() / 2, 50f);
        changeLanguangeButton.setChecked(true);

        /* Adding listeners: */
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //game.loadingScreen = new LoadingScreen(game);
                //game.setScreen(game.loadingScreen);
                playButton.setChecked(false);
                setCurrentStage(selectSongStage);
            }
        });
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settingsButton.setChecked(false);
                setCurrentStage(settingsStage);
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        changeLanguangeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(Properties.currentLocale == Properties.localeEN) {
                    Properties.currentLocale = Properties.localeFI;
                    settingsButton.getLabel().setFontScale(0.8f,0.8f);
                } else {
                    Properties.currentLocale = Properties.localeEN;
                    settingsButton.getLabel().setFontScale(1f,1f);
                }
                Properties.updateProperties();
                updateButtonTexts();
            }
        });

        mainMenuTable.add(playButton).size(playButton.getWidth(), playButton.getHeight()).pad(15f);
        mainMenuTable.row();
        mainMenuTable.add(settingsButton).size(settingsButton.getWidth(), settingsButton.getHeight()).pad(15f);
        mainMenuTable.row();
        mainMenuTable.add(exitButton).size(exitButton.getWidth(), exitButton.getHeight()).pad(15f);

        mainStage.addActor(changeLanguangeButton);
        mainStage.addActor(audioRyderText);
        mainStage.addActor(mainMenuTable);

        nameField = new TextField(game.playerName, game.skin);
        nameField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                game.playerName = nameField.getText();
                game.userSettings.putString("playerName",nameField.getText());
                game.userSettings.flush();
            }
        });
        nameField.setMaxLength(8);
        nameField.setPosition(10f,game.ORTHOCAM_VIEWPORT_HEIGHT - nameField.getHeight() - 150f);
        nameField.setSize(200f,50f);
        nameField.setAlignment(1);
        mainStage.addActor(nameField);
        mainStage.getRoot().addCaptureListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (!(event.getTarget() instanceof TextField)) mainStage.setKeyboardFocus(null);
                return false;
            }
        });

        playerNameText = new Label(Properties.playerText, game.skin,"xolonium", Color.WHITE);
        playerNameText.setPosition(nameField.getX() + (nameField.getWidth() / 2) - (playerNameText.getWidth() / 2),nameField.getY() + nameField.getHeight());
        mainStage.addActor(playerNameText);
    }

    /**
     * This method setups the selectSongStage
     * that contains a list of the Songs of the game
     * that can be selected and played from here.
     */
    private void setupSelectSongStage() {
        selectSongStage = new Stage(viewport, game.batch);

        //final TextButton returnButton = new TextButton(Properties.returnText, testSkin);
        final TextButton playButtonn = new TextButton(Properties.playText, game.skin);
        HorizontalGroup playAndReturn = new HorizontalGroup();
        playAndReturn.space(50f);
        final MenuButton returnButton = new MenuButton(Properties.returnText, game.skin);
        playAndReturn.addActor(returnButton);
        playAndReturn.addActor(playButtonn);

        ButtonGroup songButtonGroup = new ButtonGroup();
        songButtonGroup.setUncheckLast(true);
        songButtonGroup.setMaxCheckCount(1);

        VerticalGroup songs = new VerticalGroup();
        songs.padRight(5f);

        for(Song song : game.songList) {
            SongButton songButton = new SongButton(song, game.skin);
            songButtonGroup.add(songButton);
            songs.addActor(songButton);
        }

        ScrollPane songsPane = new ScrollPane(songs, game.skin);
        songsPane.setFadeScrollBars(false);
        songsPane.setScrollingDisabled(true,false);

        Table songTable = new Table(game.skin);
        songTable.setSize(500f, 500f);
        songTable.setPosition(game.WINDOW_WIDTH / 2 - songTable.getWidth() / 2, game.WINDOW_HEIGHT / 2 - songTable.getHeight() / 2);
        songTable.add(songsPane).row();
        songTable.add(playAndReturn);

        selectSongStage.addActor(songTable);

        /* Initializing the game.currentSong to the first song in the songlist: */
        game.currentSong = game.songList.get(0);

        /* Adding listeners for return and play: */
        playButtonn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playButtonn.setChecked(false);
                game.levelLoadingScreen = new LevelLoadingScreen(game, game.currentSong);
                game.setScreen(game.levelLoadingScreen);
            }
        });

        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                returnButton.setChecked(false);
                setCurrentStage(mainStage);
            }
        });

    }

    /**
     * This method setups the "Settings" stage
     * of the menu. This is made
     * only for readability purposes.
     */

    public class SensitivitySlider extends Slider{
        String direction;

        public SensitivitySlider(boolean vertical, Skin skin, float min, float max, String direction){
            super(min,max,0.5f, vertical, skin);
            this.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    setSensitivity();
                }
            });
            if(vertical){
                setWidth(20f);
                setHeight(230f);
            } else {
                setWidth(230f);
                setHeight(20f);
            }
            this.direction = direction;

        }
        public void setSensitivity(){
            if(direction.equals("left")){
                game.sensitivityLeft = getValue() * -1;
            } else if(direction.equals("down")){
                game.sensitivityDown = getValue() * -1;
            } else if(direction.equals("right")){
                game.sensitivityRight = getValue();
            } else if(direction.equals("up")){
                game.sensitivityUp = getValue();
            }
            MainMenuScreen.this.updateSensitivityTexts();

        }
    }

    private void setupSettingsStage() {
        settingsStage = new Stage(viewport, game.batch);

        /* Add all texts: */
        sensitivityText = new Label(Properties.sensitivityText, game.skin, "xolonium", Color.WHITE);
        sensitivityText.setAlignment(1);
        sensitivityText.setSize(150f,50f);
        sensitivityText.setPosition(80f,520f);

        sensitivityText2 = new Label(Properties.sensitivityText2, game.skin, "xolonium", Color.WHITE);
        sensitivityText2.setAlignment(1);
        sensitivityText2.setFontScale(0.7f);
        sensitivityText2.setSize(150f,50f);
        sensitivityText2.setPosition(80f,490f - sensitivityText2.getHeight());

        leftSensText = new Label(game.sensitivityLeft + "", game.skin, "xolonium", Color.WHITE);
        leftSensText.setPosition(100f,315f);
        rightSensText = new Label(game.sensitivityRight + "", game.skin, "xolonium", Color.WHITE);
        rightSensText.setPosition(540f,315f);
        downSensText = new Label(game.sensitivityDown + "", game.skin, "xolonium", Color.WHITE);
        downSensText.setPosition(360f,130f);
        upSensText = new Label(game.sensitivityUp + "", game.skin, "xolonium", Color.WHITE);
        upSensText.setPosition(360f,500f);



        settingsStage.addActor(sensitivityText);
        settingsStage.addActor(sensitivityText2);
        settingsStage.addActor(leftSensText);
        settingsStage.addActor(downSensText);
        settingsStage.addActor(rightSensText);
        settingsStage.addActor(upSensText);


        /* Add calibrate button: */
        calibrateButton = new TextButton(Properties.calibrateText, game.skin);
        calibrateButton.setPosition(650f, 400f);
        calibrateButton.getLabel().setStyle(new Label.LabelStyle(game.skin.getFont("xolonium"),Color.WHITE));
        calibrateButton.getLabel().setFontScale(1.2f);
        calibrateButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                calibrateButton.setChecked(false);
                game.xCalib = Gdx.input.getAccelerometerY();
                game.yCalib = Gdx.input.getAccelerometerZ();
            }
        });
        settingsStage.addActor(calibrateButton);

        /* Create sliders: */
        SensitivitySlider left = new SensitivitySlider(false, game.skin, -4f,-1f, "left");
        left.setPosition(90f,290f);
        SensitivitySlider down = new SensitivitySlider(true, game.skin, -4f, -1f,"down");
        down.setPosition(330f,280f - down.getHeight());
        SensitivitySlider right = new SensitivitySlider(false, game.skin, 1f,4f,"right");
        right.setPosition(360f,290f);
        SensitivitySlider up = new SensitivitySlider(true, game.skin, 1f, 4f,"up");
        up.setPosition(330f,320f);



        settingsStage.addActor(left);
        settingsStage.addActor(down);
        settingsStage.addActor(right);
        settingsStage.addActor(up);

        /* Gamespeed slider */
        final Slider gameSpeed = new Slider(1f,3f,0.5f,false, game.skin);
        gameSpeed.setWidth(230f);
        gameSpeed.setWidth(calibrateButton.getWidth());
        gameSpeed.setPosition(650f,150f);
        settingsStage.addActor(gameSpeed);
        gameSpeed.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.noteSpeed = gameSpeed.getValue();
                updateSensitivityTexts();
            }
        });

        gameSpeedText = new Label(game.noteSpeed + "", game.skin, "xolonium", Color.WHITE);
        gameSpeedText.setPosition(650f + (gameSpeed.getWidth() / 2f) - (gameSpeedText.getWidth() / 2),150f - gameSpeed.getHeight());
        settingsStage.addActor(gameSpeedText);

        gameSpeedText2 = new Label(Properties.gameSpeedText, game.skin, "xolonium", Color.WHITE);
        gameSpeedText2.setPosition(650f + (gameSpeed.getWidth() / 2f) - (gameSpeedText2.getWidth() / 2),150f + gameSpeed.getHeight() + gameSpeed.getHeight());
        settingsStage.addActor(gameSpeedText2);

        /* Set values for sliders */
        left.setValue(game.sensitivityLeft * -1f);
        down.setValue(game.sensitivityDown * -1f);
        right.setValue(game.sensitivityRight);
        up.setValue(game.sensitivityUp);
        gameSpeed.setValue(game.noteSpeed);

        /* Adding buttons: */
        final MenuButton returnButton = new MenuButton(Properties.returnText, game.skin);

        /* Adding listeners: */
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.userSettings.putFloat("sensitivityLeft",game.sensitivityLeft);
                game.userSettings.putFloat("sensitivityRight",game.sensitivityRight);
                game.userSettings.putFloat("sensitivityUp",game.sensitivityUp);
                game.userSettings.putFloat("sensitivityDown",game.sensitivityDown);
                game.userSettings.putFloat("xCalib",game.xCalib);
                game.userSettings.putFloat("yCalib",game.yCalib);
                game.userSettings.putFloat("gameSpeed",game.noteSpeed);
                game.userSettings.flush();

                if(game.GAME_IS_ON) {
                    returnButton.setChecked(false);
                    game.setScreen(game.gameScreen);
                } else {
                    returnButton.setChecked(false);
                    setCurrentStage(mainStage);
                }
            }
        });

        settingsStage.addActor(returnButton);
    }

    private void addActorToStage(Actor actor, Stage stage) {
        stage.addActor(actor);
    }


    /**
     * This method is used to update the Strings
     * that are shown on the buttons if the user
     * changes the language.
     */
    public void updateButtonTexts() {
        playButton.setText(Properties.playText);
        settingsButton.setText(Properties.settingsText);
        exitButton.setText(Properties.exitText);
        playerNameText.setText(Properties.playerText);
        playerNameText.setPosition(nameField.getX() + (nameField.getWidth() / 2) - (playerNameText.getWidth() / 2),nameField.getY() + nameField.getHeight());
        setupSelectSongStage();
        setupSettingsStage();


    }

    public void updateSensitivityTexts(){
        upSensText.setText(game.sensitivityUp + "");
        downSensText.setText(game.sensitivityDown + "");
        leftSensText.setText(game.sensitivityLeft + "");
        rightSensText.setText(game.sensitivityRight + "");
        gameSpeedText.setText(game.noteSpeed + "");
    }
    /**
     * This method is used to set what Stage
     * is being rendered.
     *
     * @param stage This is the Stage that is going to be rendered.
     */
    private void setCurrentStage(Stage stage) {
        currentStage = stage;
        Gdx.input.setInputProcessor(currentStage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor( 0, 0, 0, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        backgroundStage.draw();
        /*System.out.println("down: "+game.sensitivityDown);
        System.out.println("up: "+game.sensitivityUp);
        System.out.println("left: "+game.sensitivityLeft);
        System.out.println("right: "+game.sensitivityRight);*/
        currentStage.act(Gdx.graphics.getDeltaTime());
        currentStage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }
}
