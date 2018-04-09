package com.mygdx.audioryder;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.audioryder.objects.GroundLine;
import com.mygdx.audioryder.objects.Skydome;
import com.mygdx.audioryder.objects.SpaceShip;
import com.mygdx.audioryder.properties.Properties;
import com.mygdx.audioryder.screens.GameScreen;
import com.mygdx.audioryder.screens.LevelLoadingScreen;
import com.mygdx.audioryder.screens.LoadingScreen;
import com.mygdx.audioryder.screens.MainMenuScreen;
import com.mygdx.audioryder.song.Song;

import java.util.ArrayList;
import java.util.Locale;

public class AudioRyder extends Game {

	public AssetManager assets = new AssetManager();

	public static final String MODELS_PATH = "models/";
	public static final String SONGS_PATH = "songs/";
	public static final String SPRITES_PATH = "sprites/";

	public static final int WINDOW_WIDTH = 1024;
	public static final int WINDOW_HEIGHT = 600;

	public OrthographicCamera cam2D;
	public static final float ORTHOCAM_VIEWPORT_WIDTH = WINDOW_WIDTH;
	public static final float ORTHOCAM_VIEWPORT_HEIGHT = WINDOW_HEIGHT;
	public Viewport viewport;

	public SpriteBatch batch;
	public BitmapFont font;

	public GameScreen gameScreen;
	public MainMenuScreen mainMenuScreen;
	public LoadingScreen loadingScreen;
	public LevelLoadingScreen levelLoadingScreen;

	public SpaceShip spaceShip;

	public Model levelModel;

	public Model skyModel;

	public Skydome skydome;

	public Environment environment;

	public int score;
	public int streak;
	public int multiplier;
	public float hitOrMissTimer;
	public boolean hitOrMiss = true;

	public Model box;
	public Float songTimer = 0f;
	public float songOffset;
	public float noteSpeed;
	public float sensitivityLeft;
	public float sensitivityRight;
	public float sensitivityDown;
	public float sensitivityUp;
	public float xCalib;
	public float yCalib;

	public Song currentSong;

	public Song erikaSong;
	public Song nopeeHatane;
	public Song chinese;
	public Song reverie;
	public Song zzz;

	@Override
	public void create () {
		Properties.currentLocale = Properties.localeEN;
		Properties.updateProperties();

        cam2D = new OrthographicCamera();
        cam2D.setToOrtho(false, ORTHOCAM_VIEWPORT_WIDTH, ORTHOCAM_VIEWPORT_HEIGHT);

        batch = new SpriteBatch();
        font = new BitmapFont();

        /* initializing songs: */
		erikaSong = new Song("Marssilaulu","erika.mp3", "erika.txt");
		nopeeHatane = new Song("Nopee hatanen","Äss Berger - Nopee ja hätäne.mp3", "nopeehatane.txt");
		chinese = new Song("Chinese Song","chinese.mp3","chinese.txt");
        reverie = new Song("Reverie","reverie.mp3","reverie.txt");
        zzz = new Song("Z Z Z","zzz.mp3","zzz.txt");

        //set variables
        score = 0;
        multiplier = 1;
        streak = 0;
        hitOrMissTimer = 0f;

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 2.0f));

        //settings
        noteSpeed = 1.5f;
        sensitivityLeft = 1.5f;
        sensitivityDown = 2f;
        sensitivityRight = 1.5f;
        sensitivityUp = 2f;
        songOffset = -0.1f;

		loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		spaceShip.dispose();
		batch.dispose();
		font.dispose();
	}

}