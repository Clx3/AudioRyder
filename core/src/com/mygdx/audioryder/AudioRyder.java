package com.mygdx.audioryder;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.audioryder.objects.Skydome;
import com.mygdx.audioryder.preferences.UserSettings;
import com.mygdx.audioryder.properties.Properties;
import com.mygdx.audioryder.screens.EndScreen;
import com.mygdx.audioryder.screens.GameScreen;
import com.mygdx.audioryder.screens.LevelLoadingScreen;
import com.mygdx.audioryder.screens.LoadingScreen;
import com.mygdx.audioryder.screens.MainMenuScreen;
import com.mygdx.audioryder.screens.PauseScreen;
import com.mygdx.audioryder.song.Song;

import java.util.ArrayList;

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
	public static final String EFFECTS_PATH = "effects/";

	/** Width of the application window. */
	public static final int WINDOW_WIDTH = 1024;

    /** Height of the application window. */
	public static final int WINDOW_HEIGHT = 600;

	public OrthographicCamera cam2D;
	public static final float ORTHOCAM_VIEWPORT_WIDTH = WINDOW_WIDTH;
	public static final float ORTHOCAM_VIEWPORT_HEIGHT = WINDOW_HEIGHT;

	public Viewport viewport;

	public SpriteBatch batch;
	public BitmapFont font;

	public Skin skin;
	public TextureAtlas textureAtlas;

	/* Screens: */
	public GameScreen gameScreen;
	public MainMenuScreen mainMenuScreen;
	public LoadingScreen loadingScreen;
	public LevelLoadingScreen levelLoadingScreen;
	public PauseScreen pauseScreen;
	public EndScreen endScreen;

	public Environment environment;
    public PointLight light;
    public PointLight light2;


	public int score;
	public int streak;
	public int multiplier;
	public float hitOrMissTimer;
	public boolean hitOrMiss = true;

    /**
     * Determines if the game is currently playing. Used for correct menu navigation when changing
     * settings during gameplay.
     */
	public boolean GAME_IS_ON = false;

	public Float songTimer = 0f;
	public float songOffset;


	/** This Song is basically the "gameplay" Song that
	 * the game uses to play and spawn notes from while in game.
	 */
	public Song currentSong;

	/** This ArrayList holds all the songs in the game and
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

		skin = new Skin(Gdx.files.internal("skins/jarno/AudioRyderUI.json"));
		textureAtlas = new TextureAtlas("skins/jarno/AudioRyderUI.atlas");


        /* Creating songs: */
		songList.add(new Song("Nopee hatanen","Äss Berger - Nopee ja hätäne.mp3", "nopeehatane.txt"));
		songList.add(new Song("Chinese Song","chinese.mp3","chinese.txt"));
		songList.add(new Song("Reverie","reverie.mp3","reverie.txt"));
		songList.add(new Song("Z Z Z","zzz.mp3","zzz.txt"));
		songList.add(new Song("Heatenings","Äss Berger - Heatenings.mp3","Heatenings.txt"));
		songList.add(new Song("Octopus","mNoise - Octopus.mp3","mNoise - Octopus.txt"));
		songList.add(new Song("Light in the dark","NoJustSe - Light in the dark.mp3","NoJustSe - Light In The Dark.txt"));
		songList.add(new Song("Melroom","mNoise - Melroom.mp3","mNoise - Melroom.txt"));
		songList.add(new Song("Turmoil","NoJustSe - Turmoil.mp3","NoJustSe - Turmoil.txt"));
		songList.add(new Song("Continuum","NoJustSe feat Chrysalid - Continuum.mp3","NoJustSe feat Chrysalid - Continuum.txt"));
		songList.add(new Song("Sunrise","N-Tech - Sunrise.mp3","N-Tech - Sunrise.txt"));
		songList.add(new Song("Sleepless Hours","N-Tech - Sleepless Hours.mp3","N-Tech - Sleepless Hours.txt"));
		songList.add(new Song("Guardian","NoJustSe - The Guardian.mp3","NoJustSe - The Guardian.txt"));
		songList.add(new Song("Frozen North","NoJustSe feat Chrysalid - Frozen north.mp3","NoJustSe feat Chrysalid - Frozen north.txt"));

        //set variables
        score = 0;
        multiplier = 1;
        streak = 0;
        hitOrMissTimer = 0f;

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 2.0f));
        light = new PointLight();
        light.setIntensity(5000f);
        light.setColor(Color.WHITE);
        light.setPosition(0f,70f,-25f);
        environment.add(light);
        light2 = new PointLight();
        light2.setIntensity(5000f);
        light2.setColor(Color.WHITE);
        light2.setPosition(0f,70f,-150f);
        environment.add(light2);

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
	}


}