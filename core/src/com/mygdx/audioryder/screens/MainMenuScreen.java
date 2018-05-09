package com.mygdx.audioryder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.audioryder.AudioRyder;
import com.mygdx.audioryder.preferences.UserSettings;
import com.mygdx.audioryder.properties.Properties;
import com.mygdx.audioryder.song.Song;

/**
 * This class is the Main menu of our game.
 * It contains all the logic and function that
 * the main menu needs.
 *
 * @author Teemu Salminen
 * @author Joonas Salojärvi
 */
public class MainMenuScreen implements Screen {

    AudioRyder game;

    /** This is the "main" stage or the first stage of the main menu. */
    private Stage mainStage;

    /** This is the Stage where the user can select the song to play. */
    private Stage selectSongStage;

    /** This is the info Stage of the main menu. */
    private Stage infoStage;

    private Stage infoArtistsStage;

    /** This is the background Stage what is rendered behind all of the other Stages. */
    public Stage backgroundStage;

    /** This is the settings Stage where user can change the settings of the game. */
    public Stage settingsStage;

    /**
     * This is the Stage that is going to be rendered, so all
     * the Stages we have in this class will be given to this variable
     * if it is the chosen Stage that is going to be rendered.
     */
    public Stage currentStage;

    private Image audioRyderText;


    Label sensitivityText;
    Label sensitivityText2;
    Label leftSensText;
    Label rightSensText;
    Label downSensText;
    Label upSensText;
    Label gameSpeedText;
    Label gameSpeedText2;
    TextButton calibrateButton;

    /** This is the field where player enters his/hers name. */
    private TextField nameField;

    public MainMenuScreen(AudioRyder game) {
        this.game = game;

        game.cam2D.setToOrtho(false, game.ORTHOCAM_VIEWPORT_WIDTH, game.ORTHOCAM_VIEWPORT_HEIGHT);
        game.batch.setProjectionMatrix(game.cam2D.combined);

        Texture audioRyderTextTexture = new Texture(Gdx.files.internal(game.SPRITES_PATH + "audioryder.png"));
        TextureRegion audioRyderTextRegion = new TextureRegion(audioRyderTextTexture);
        audioRyderText = new Image(audioRyderTextRegion);
        audioRyderText.setSize(750f, 200f);
        audioRyderText.setPosition(game.WINDOW_WIDTH / 2 - audioRyderText.getWidth() / 2, game.WINDOW_HEIGHT - audioRyderText.getHeight());

        /* Initializing all the Stages: */
        setupBackgroundStage();

        setupStages();
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
        backgroundStage = new Stage(game.viewport, game.batch);

        Texture backgroundTexture = new Texture(Gdx.files.internal(game.SPRITES_PATH + "menubackground.jpg"));
        TextureRegion backgroundRegion = new TextureRegion(backgroundTexture,0,0, game.WINDOW_WIDTH, game.WINDOW_HEIGHT);
        Image background = new Image(backgroundRegion);

        backgroundStage.addActor(background);
    }

    /**
     * This method setups the "Main" stage
     * of the menu. This is made
     * only for readability purposes.
     */
    private void setupMainStage() {
        mainStage = new Stage(game.viewport, game.batch);

        Table mainMenuTable = new Table();
        mainMenuTable.setFillParent(true);

        /* Creating the buttons: */
        MenuButton playButton = new MenuButton(Properties.playText, game.skin);
        final MenuButton settingsButton = new MenuButton(Properties.settingsText, game.skin);
        MenuButton exitButton = new MenuButton(Properties.exitText, game.skin);

        ImageButton changeLanguangeButton = new ImageButton(game.skin, "flag");
        changeLanguangeButton.setSize(50f,50f);
        changeLanguangeButton.setPosition(game.ORTHOCAM_VIEWPORT_WIDTH / 2 - changeLanguangeButton.getWidth() / 2, 50f);

        /* Configuring the change language buttton: */
        if(Properties.currentLocale == Properties.localeEN) {
            changeLanguangeButton.setChecked(false);
        } else {
            changeLanguangeButton.setChecked(true);
        }

        /* Adding listeners for buttons: */
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //game.loadingScreen = new LoadingScreen(game);
                //game.setScreen(game.loadingScreen);
                setCurrentStage(selectSongStage);
            }
        });
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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
                Preferences userSettings = Gdx.app.getPreferences("userSettings");
                if(Properties.currentLocale == Properties.localeEN) {
                    Properties.currentLocale = Properties.localeFI;
                    userSettings.putString("language","fi");
                    settingsButton.getLabel().setFontScale(0.8f,0.8f);

                } else {
                    Properties.currentLocale = Properties.localeEN;
                    userSettings.putString("language","en");
                    settingsButton.getLabel().setFontScale(1f,1f);
                }
                Properties.updateProperties();
                setupStages();
                userSettings.flush();

                setCurrentStage(mainStage);

                //game.mainMenuScreen = new MainMenuScreen(game);
                //game.setScreen(game.mainMenuScreen);
            }
        });

        /* Configuring the main menu table: */
        mainMenuTable.add(playButton).size(playButton.getWidth(), playButton.getHeight()).pad(15f);
        mainMenuTable.row();
        mainMenuTable.add(settingsButton).size(settingsButton.getWidth(), settingsButton.getHeight()).pad(15f);
        mainMenuTable.row();
        mainMenuTable.add(exitButton).size(exitButton.getWidth(), exitButton.getHeight()).pad(15f);

        nameField = new TextField(UserSettings.playerName, game.skin);

        nameField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                UserSettings.playerName = nameField.getText();
                UserSettings.savePlayerSettings();
            }
        });

        nameField.setMaxLength(8);
        nameField.setSize(200f,50f);
        nameField.setPosition(game.ORTHOCAM_VIEWPORT_WIDTH - nameField.getWidth() - 50f, game.ORTHOCAM_VIEWPORT_HEIGHT - 250f);
        nameField.setAlignment(1);

        mainStage.getRoot().addCaptureListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (!(event.getTarget() instanceof TextField)){
                    mainStage.setKeyboardFocus(null);
                    nameField.getOnscreenKeyboard().show(false);
                }

                return false;
            }
        });

        Label playerNameText = new Label(Properties.playerText, game.skin,"xolonium", Color.WHITE);
        playerNameText.setPosition(nameField.getX() + (nameField.getWidth() / 2) - (playerNameText.getWidth() / 2),nameField.getY() + nameField.getHeight());

        /** Creating and adding the listener for the Info button: */
        final ImageButton infoButton = new ImageButton(game.skin, "info");
        infoButton.setPosition(50f, game.ORTHOCAM_VIEWPORT_HEIGHT - infoButton.getHeight() - 50f);

        infoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setCurrentStage(infoStage);
            }
        });

        /* Adding all the actors to the Stage: */
        mainStage.addActor(changeLanguangeButton);
        mainStage.addActor(audioRyderText);
        mainStage.addActor(mainMenuTable);
        mainStage.addActor(nameField);
        mainStage.addActor(playerNameText);
        mainStage.addActor(infoButton);
    }

    /**
     * This method setups the selectSongStage
     * that contains a list of the Songs of the game
     * that can be selected and played from here.
     */
    private void setupSelectSongStage() {
        selectSongStage = new Stage(game.viewport, game.batch);

        //final TextButton returnButton = new TextButton(Properties.returnText, testSkin);
        final TextButton playButtonn = new TextButton(Properties.playText, game.skin);
        HorizontalGroup playAndReturn = new HorizontalGroup();
        playAndReturn.space(50f);
        final TextButton returnButton = new MenuButton(Properties.returnText, game.skin);
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

        /* Initializing the game.gameMusic to the first song in the songlist: */
        game.currentSong = game.songList.get(0);

        /* Adding listeners for return and play: */
        playButtonn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.levelLoadingScreen = new LevelLoadingScreen(game, game.currentSong);
                game.setScreen(game.levelLoadingScreen);
            }
        });

        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setCurrentStage(mainStage);
            }
        });

    }

    public class SensitivitySlider extends Slider{
        String direction;

        private SensitivitySlider(boolean vertical, Skin skin, float min, float max, String direction){
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

        private void setSensitivity(){
            if(direction.equals("left")){
                UserSettings.sensitivityLeft = getValue() * -1;
            } else if(direction.equals("down")){
                UserSettings.sensitivityDown = getValue() * -1;
            } else if(direction.equals("right")){
                UserSettings.sensitivityRight = getValue();
            } else if(direction.equals("up")){
                UserSettings.sensitivityUp = getValue();
            }
            MainMenuScreen.this.updateSensitivityTexts();

        }
    }

    /**
     * This method setups the "Settings" stage
     * of the menu. This is made
     * only for readability purposes.
     */
    private void setupSettingsStage() {
        settingsStage = new Stage(game.viewport, game.batch);

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

        leftSensText = new Label(UserSettings.sensitivityLeft + "", game.skin, "xolonium", Color.WHITE);
        leftSensText.setPosition(100f,315f);
        rightSensText = new Label(UserSettings.sensitivityRight + "", game.skin, "xolonium", Color.WHITE);
        rightSensText.setPosition(540f,315f);
        downSensText = new Label(UserSettings.sensitivityDown + "", game.skin, "xolonium", Color.WHITE);
        downSensText.setPosition(360f,130f);
        upSensText = new Label(UserSettings.sensitivityUp + "", game.skin, "xolonium", Color.WHITE);
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
                UserSettings.xCalib = Gdx.input.getAccelerometerY();
                UserSettings.yCalib = Gdx.input.getAccelerometerZ();
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
                UserSettings.noteSpeed = gameSpeed.getValue();
                updateSensitivityTexts();
            }
        });

        gameSpeedText = new Label(UserSettings.noteSpeed + "", game.skin, "xolonium", Color.WHITE);
        gameSpeedText.setPosition(650f + (gameSpeed.getWidth() / 2f) - (gameSpeedText.getWidth() / 2),150f - gameSpeed.getHeight());
        settingsStage.addActor(gameSpeedText);

        gameSpeedText2 = new Label(Properties.gameSpeedText, game.skin, "xolonium", Color.WHITE);
        gameSpeedText2.setPosition(650f + (gameSpeed.getWidth() / 2f) - (gameSpeedText2.getWidth() / 2),150f + gameSpeed.getHeight() + gameSpeed.getHeight());
        settingsStage.addActor(gameSpeedText2);

        /* Set values for sliders */
        left.setValue(UserSettings.sensitivityLeft * -1f);
        down.setValue(UserSettings.sensitivityDown * -1f);
        right.setValue(UserSettings.sensitivityRight);
        up.setValue(UserSettings.sensitivityUp);
        gameSpeed.setValue(UserSettings.noteSpeed);

        /* Adding buttons: */
        final MenuButton returnButton = new MenuButton(Properties.returnText, game.skin);

        /* Adding listeners: */
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserSettings.savePlayerSettings();


                if(game.GAME_IS_ON) {
                    game.setScreen(game.pauseScreen);
                } else {
                    setCurrentStage(mainStage);
                }
            }
        });

        settingsStage.addActor(returnButton);
    }


    //TODO: TÄMÄ. Voin jatkaa tätä kun tuun takasin mutta voit Joonas jatkaa ihan vapaasti kanssa jos haluat :--) t teemu.
    private void setupInfoStage() {
        infoStage = new Stage(game.viewport, game.batch);

        /* ----------- First info stage: ----------- */
        MenuButton guideButton = new MenuButton(Properties.guideText, game.skin);
        MenuButton artistsButton = new MenuButton(Properties.artistsText, game.skin);

        Table infoTable = new Table();
        infoTable.setFillParent(true);

        infoTable.add(guideButton).row();
        infoTable.add(artistsButton).row();

        /* Adding listeners: */
        artistsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setCurrentStage(infoArtistsStage);
            }
        });

        /* Adding the actors to the Stage: */
        infoStage.addActor(infoTable);
        infoStage.addActor(createReturnButton(mainStage, 20f, 20f));
    }

    private void setupInfoArtistsStage() {
        infoArtistsStage = new Stage(game.viewport, game.batch);

        Label artistStageTitle = new Label("Special thanks to these artists for letting\n us to use their music in AudioRyder.", game.skin, "xolonium", Color.WHITE);
        artistStageTitle.setPosition(game.ORTHOCAM_VIEWPORT_WIDTH / 2 - (artistStageTitle.getWidth() + artistStageTitle.getFontScaleX()) / 2, game.ORTHOCAM_VIEWPORT_HEIGHT - artistStageTitle.getHeight() - 50f);

        Window artist1 = createArtistWindow("SS-Berger", "http://soundcloud.com/ss-berger");
        artist1.setPosition(44.8f, game.ORTHOCAM_VIEWPORT_HEIGHT / 2f - artist1.getHeight() / 2);

        Window artist2 = createArtistWindow("mNoise", "http://soundcloud.com/min-olenmikko");
        artist2.setPosition(artist1.getX() + artist2.getWidth() + 44.8f, game.ORTHOCAM_VIEWPORT_HEIGHT / 2f - artist2.getHeight() / 2);

        Window artist3 = createArtistWindow("NoJustSe", "http://soundcloud.com/nojustse");
        artist3.setPosition(artist2.getX() + artist3.getWidth() + 44.8f, game.ORTHOCAM_VIEWPORT_HEIGHT / 2f - artist3.getHeight() / 2);

        Window artist4 = createArtistWindow("SS-Berger", "http:://google.fi");
        artist4.setPosition(artist3.getX() + artist4.getWidth() + 44.8f, game.ORTHOCAM_VIEWPORT_HEIGHT / 2f - artist4.getHeight() / 2);

        infoArtistsStage.addActor(artistStageTitle);
        infoArtistsStage.addActor(artist1);
        infoArtistsStage.addActor(artist2);
        infoArtistsStage.addActor(artist3);
        infoArtistsStage.addActor(artist4);
        infoArtistsStage.addActor(createReturnButton(infoStage, 20f, 20f));
    }

    /**
     * This method creates a Window object of an artist for
     * the artistsStage.
     *
     * @param artistName Name of the artist(title).
     * @param soundCloudURL Link to the artists SoundCloud(the link which the button will open).
     * @return Returns the created window.
     */
    private Window createArtistWindow(String artistName, final String soundCloudURL) {
        Window outputWindow = new Window(artistName, game.skin);
        outputWindow.setWidth(200f);
        outputWindow.setMovable(false);

        ImageButton tempButton = new ImageButton(game.skin, "info");
        tempButton.setPosition(outputWindow.getWidth() / 2 - tempButton.getWidth() / 2, outputWindow.getHeight() / 2 - tempButton.getWidth() / 2 - 10f);

        tempButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x , float y) {
                Gdx.net.openURI(soundCloudURL);
            }
        });
        outputWindow.addActor(tempButton);

        return outputWindow;
    }


    /**
     * This method creates a simple "Return" button
     * for you so you don't need to do it everytime you
     * create a new Stage.
     *
     * @param goToThisStage This is the Stage which the Return button will switch to.
     * @param x X position of the button.
     * @param y Y position of the button.
     * @return Returns the created button.
     */
    private MenuButton createReturnButton(final Stage goToThisStage, float x, float y) {
        MenuButton outputButton = new MenuButton(Properties.returnText, game.skin);
        outputButton.setPosition(x, y);

        outputButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setCurrentStage(goToThisStage);
            }
        });

        return outputButton;
    }


    /**
     * This method is used to setup all the
     * Stages that the main menu uses. The reason for this
     * having an own method is that this is called when the
     * language is changed so the texts will update.
     */
    public void setupStages() {
        setupMainStage();
        setupInfoStage();
        setupSelectSongStage();
        setupSettingsStage();
        setupInfoStage();
        setupInfoArtistsStage();
    }

    /**
     * Called whenever the sliders are moved on the settings stage.
     * Updates correct numbers next to the sliders.
     */
    public void updateSensitivityTexts(){
        upSensText.setText(UserSettings.sensitivityUp + "");
        downSensText.setText(UserSettings.sensitivityDown + "");
        leftSensText.setText(UserSettings.sensitivityLeft + "");
        rightSensText.setText(UserSettings.sensitivityRight + "");
        gameSpeedText.setText(UserSettings.noteSpeed + "");
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
