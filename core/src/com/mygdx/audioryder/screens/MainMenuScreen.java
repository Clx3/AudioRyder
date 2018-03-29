package com.mygdx.audioryder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.audioryder.AudioRyder;
import com.mygdx.audioryder.song.Song;

/**
 * Created by Teemu on 17.3.2018.
 */

public class MainMenuScreen implements Screen {

    AudioRyder game;

    private Stage currentStage;

    private Stage backgroundStage;
    private Stage mainStage;
    private Stage selectSongStage;
    private Stage settingsStage;

    Skin testSkin;
    TextureAtlas testAtlas;

    private Image background;
    private Image audioRyderText;

    MenuButton playButton;
    MenuButton settingsButton;
    MenuButton exitButton;
    MenuButton returnButton;

    private Viewport viewport;

    public MainMenuScreen(AudioRyder game) {
        this.game = game;
    }

    /**
     * This class is used to construct the buttons
     * used by the menu.
     */
    class MenuButton extends TextButton {

        public MenuButton(String text, Skin skin) {
            super(text, skin);
            setWidth(200f);
            setHeight(70f);
        }

    }

    private static Song selectedSong;

    class SongButton extends TextButton {

        private Song song;

        public SongButton(Song song, Skin skin) {
            super(song.getName(), skin);
            setSong(song);
            setWidth(200f);
            setHeight(70f);

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
        game.cam2D.setToOrtho(false, game.ORTHOCAM_VIEWPORT_WIDTH, game.ORTHOCAM_VIEWPORT_HEIGHT);
        game.batch.setProjectionMatrix(game.cam2D.combined);

        testSkin = new Skin(Gdx.files.internal("skins/plain-james-ui.json"));
        testAtlas = new TextureAtlas("skins/plain-james-ui.atlas");

        viewport = new FitViewport(game.ORTHOCAM_VIEWPORT_WIDTH, game.ORTHOCAM_VIEWPORT_HEIGHT, game.cam2D);
        viewport.apply();

        //testSkin = new Skin(Gdx.files.internal("skins/PlayButton.json"));
        //testAtlas = new TextureAtlas("skins/PlayButton.atlas");

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
        backgroundStage.addActor(audioRyderText);
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
        playButton = new MenuButton("Play", testSkin);
        settingsButton = new MenuButton("Settings", testSkin);
        exitButton = new MenuButton("Exit", testSkin);

        /* Adding listeners: */
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

        mainMenuTable.add(playButton).size(playButton.getWidth(), playButton.getHeight()).pad(15f);
        mainMenuTable.row();
        mainMenuTable.add(settingsButton).size(settingsButton.getWidth(), settingsButton.getHeight()).pad(15f);;
        mainMenuTable.row();
        mainMenuTable.add(exitButton).size(exitButton.getWidth(), exitButton.getHeight()).pad(15f);;

        mainStage.addActor(mainMenuTable);
    }

    /**
     * This method setups the selectSongStage
     * that contains a list of the Songs of the game
     * that can be selected and played from here.
     */
    private void setupSelectSongStage() {
        selectSongStage = new Stage(viewport, game.batch);

        TextButton returnButton = new TextButton("Return", testSkin);
        TextButton playButtonn = new TextButton("Play", testSkin);
        HorizontalGroup playAndReturn = new HorizontalGroup();
        playAndReturn.space(50f);
        playAndReturn.addActor(returnButton);
        playAndReturn.addActor(playButtonn);

        SongButton song1 = new SongButton(game.erikaSong, testSkin);
        SongButton song2 = new SongButton(game.nopeeHatane, testSkin);

        ButtonGroup songButtonGroup = new ButtonGroup();
        songButtonGroup.setUncheckLast(true);
        songButtonGroup.setMaxCheckCount(1);
        songButtonGroup.add(song1);
        songButtonGroup.add(song2);

        VerticalGroup songs = new VerticalGroup();
        songs.addActor(song1);
        songs.addActor(song2);

        ScrollPane songsPane = new ScrollPane(songs, testSkin);
        //songsPane.setSize(200f, 100f);

        Table songTable = new Table(testSkin);
         songTable.setSize(500f, 500f);
        songTable.debug();
        songTable.setPosition(game.WINDOW_WIDTH / 2 - songTable.getWidth() / 2, game.WINDOW_HEIGHT / 2 - songTable.getHeight() / 2);
        songTable.add(songsPane).row();
        songTable.add(playAndReturn);

        selectSongStage.addActor(songTable);

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

    /**
     * This method setups the "Settings" stage
     * of the menu. This is made
     * only for readability purposes.
     */
    private void setupSettingsStage() {
        settingsStage = new Stage(viewport, game.batch);

        /* Adding buttons: */
        returnButton = new MenuButton("Return", testSkin);

        /* Adding listeners: */
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setCurrentStage(mainStage);
            }
        });

        settingsStage.addActor(returnButton);
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
