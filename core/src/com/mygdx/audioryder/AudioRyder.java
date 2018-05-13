package com.mygdx.audioryder;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.audioryder.preferences.UserSettings;
import com.mygdx.audioryder.screens.EndScreen;
import com.mygdx.audioryder.screens.GameScreen;
import com.mygdx.audioryder.screens.LevelLoadingScreen;
import com.mygdx.audioryder.screens.LoadingScreen;
import com.mygdx.audioryder.screens.MainMenuScreen;
import com.mygdx.audioryder.screens.PauseScreen;
import com.mygdx.audioryder.song.Song;

import java.util.ArrayList;

/**
 * This class is the main class of the game.
 * It contains mostly all the global variables and
 * tools etc. that the game will use in different classes.
 * After the needed things are initialized here the,
 * this class will switch the screen to LoadingScreen.
 *
 * @version 2018.0509
 * @author Teemu Salminen
 * @author Joonas Salojärvi
 */
public class AudioRyder extends Game {

    /**
     * This AssetManager is used in couple of Classes.
     * It's used for loading the assets needed for the
     * game.
     */
	public AssetManager assets = new AssetManager();

	/* These Strings contains the path to different types of assets: */
	public static final String MODELS_PATH = "models/";
	public static final String SONGS_PATH = "songs/";
	public static final String SOUNDS_PATH = "sounds/";
	public static final String SPRITES_PATH = "sprites/";
	public static final String SKINS_PATH = "skins/";
	public static final String EFFECTS_PATH = "effects/";

	/** Width of the application window. */
	public static final int WINDOW_WIDTH = 1024;

    /** Height of the application window. */
	public static final int WINDOW_HEIGHT = 600;

	/** Camera used to draw 2D objects */
	public OrthographicCamera cam2D;

	/** Width of the OrthographicCamera viewport. */
	public static final float ORTHOCAM_VIEWPORT_WIDTH = WINDOW_WIDTH;

    /** Height of the OrthographicCamera viewport. */
	public static final float ORTHOCAM_VIEWPORT_HEIGHT = WINDOW_HEIGHT;

	/* Viewport that is used by Scene2D Stages. */
	public Viewport viewport;

	/** Batch for drawing 2D objects */
	public SpriteBatch batch;

	/** BitmapFont variable, used by fonts generated by FreeTypeFontGenerator */
	public BitmapFont font;

	/** For creating fonts from .ttf file */
	public FreeTypeFontGenerator fontGenerator;
	public FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;

	/** Default skin style for the game, mostly used by actors on menus */
	public Skin skin;
	public TextureAtlas textureAtlas;

	/* Screens: */
	public GameScreen gameScreen;
	public MainMenuScreen mainMenuScreen;
	public LoadingScreen loadingScreen;
	public LevelLoadingScreen levelLoadingScreen;
	public PauseScreen pauseScreen;
	public EndScreen endScreen;

    /**
     * Determines if the game is currently playing. Used for correct menu navigation when changing
     * settings during gameplay.
     */
	public boolean GAME_IS_ON = false;

	/** Affects, when the note objects are spawned. */
	public float songOffset;

	/**
     * This Song is basically the "gameplay" Song that
	 * the game uses to play and spawn notes from while in game.
	 */
	public Song currentSong;

	/**
     * This ArrayList holds all the songs in the game and
	 * it is used when selecting songs on main menu.
	 */
	public ArrayList<Song> songList = new ArrayList<Song>();

	@Override
	public void create () {

        cam2D = new OrthographicCamera();
        cam2D.setToOrtho(false, ORTHOCAM_VIEWPORT_WIDTH, ORTHOCAM_VIEWPORT_HEIGHT);

		viewport = new FitViewport(ORTHOCAM_VIEWPORT_WIDTH, ORTHOCAM_VIEWPORT_HEIGHT, cam2D);
		viewport.apply();

        batch = new SpriteBatch();
        font = new BitmapFont();

		skin = new Skin(Gdx.files.internal(SKINS_PATH + "AudioRyderUI.json"));
		textureAtlas = new TextureAtlas(SKINS_PATH + "AudioRyderUI.atlas");

		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(SKINS_PATH + "Xolonium.ttf"));
		fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        /* Creating songs: */
		songList.add(new Song("Äss Berger - Nopee hätänen","Äss Berger - Nopee ja hätäne.mp3", "nopeehatane.txt"));
		songList.add(new Song("Äss Berger - Chinese Beat","chinese.mp3","chinese.txt"));
		songList.add(new Song("Äss Berger - Reverie","reverie.mp3","reverie.txt"));
		songList.add(new Song("Äss Berger - Z Z Z","zzz.mp3","zzz.txt"));
		songList.add(new Song("Äss Berger - Heatenings","Äss Berger - Heatenings.mp3","Heatenings.txt"));
		songList.add(new Song("mNoise - Octopus","mNoise - Octopus.mp3","mNoise - Octopus.txt"));
		songList.add(new Song("NoJustSe - Light in the dark","NoJustSe - Light in the dark.mp3","NoJustSe - Light In The Dark.txt"));
		songList.add(new Song("mNoise - Melroom","mNoise - Melroom.mp3","mNoise - Melroom.txt"));
		songList.add(new Song("NoJustSe - Turmoil","NoJustSe - Turmoil.mp3","NoJustSe - Turmoil.txt"));
		songList.add(new Song("NoJustSe feat Chrysalid - Continuum","NoJustSe feat Chrysalid - Continuum.mp3","NoJustSe feat Chrysalid - Continuum.txt"));
		songList.add(new Song("NoJustSe - Sunrise","N-Tech - Sunrise.mp3","N-Tech - Sunrise.txt"));
		songList.add(new Song("NoJustSe - Sleepless Hours","N-Tech - Sleepless Hours.mp3","N-Tech - Sleepless Hours.txt"));
		songList.add(new Song("NoJustSe - Guardian","NoJustSe - The Guardian.mp3","NoJustSe - The Guardian.txt"));
		songList.add(new Song("NoJustSe - Frozen North","NoJustSe feat Chrysalid - Frozen north.mp3","NoJustSe feat Chrysalid - Frozen north.txt"));

        UserSettings.loadPlayerSettings();

        songOffset = -0.15f;

		loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);
	}

    @Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		assets.dispose();
		fontGenerator.dispose();

		if(gameScreen != null)
			gameScreen.dispose();
	}


}