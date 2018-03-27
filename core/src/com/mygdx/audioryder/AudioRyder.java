package com.mygdx.audioryder;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.mygdx.audioryder.objects.GroundLine;
import com.mygdx.audioryder.objects.SpaceShip;
import com.mygdx.audioryder.screens.GameScreen;
import com.mygdx.audioryder.screens.LoadingScreen;
import com.mygdx.audioryder.screens.MainMenuScreen;

import java.util.ArrayList;

public class AudioRyder extends Game {

	public AssetManager assets = new AssetManager();

	public static final String MODELS_PATH = "models/";
	public static final String SONGS_PATH = "songs/";
	public static final String SPRITES_PATH = "sprites/";

	public static final int WINDOW_WIDTH = 1024;
	public static final int WINDOW_HEIGHT = 600;

	public OrthographicCamera orthoCamera;
	public static final float ORTHOCAM_VIEWPORT_WIDTH = WINDOW_WIDTH;
	public static final float ORTHOCAM_VIEWPORT_HEIGHT = WINDOW_HEIGHT;

	public LoadingScreen loadingScreen;
	public GameScreen gameScreen;
	public MainMenuScreen mainMenuScreen;

	public SpaceShip spaceShip;

	public Model levelModel;


	public Environment environment;

	public int score;
	public int streak;
	public int multiplier;
	public float hitOrMissTimer;
	public boolean hitOrMiss = true;

	public Model box;
	public Float songTimer = 0f;
	public float songOffset;
	public ArrayList<GroundLine> groundLines;
	public float noteSpeed;
	float sensitivity;

	@Override
	public void create () {
        orthoCamera = new OrthographicCamera();
        orthoCamera.setToOrtho(false, ORTHOCAM_VIEWPORT_WIDTH, ORTHOCAM_VIEWPORT_HEIGHT);

        //set variables
        score = 0;
        multiplier = 1;
        streak = 0;
        hitOrMissTimer = 0f;

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 2.0f));

        groundLines = new ArrayList<GroundLine>();

        //settings
        noteSpeed = 1.5f;
        sensitivity = 3f;
        songOffset = -0.1f;

		mainMenuScreen = new MainMenuScreen(this);
		setScreen(mainMenuScreen);
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		spaceShip.dispose();
	}

}