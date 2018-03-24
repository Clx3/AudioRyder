package com.mygdx.audioryder;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.mygdx.audioryder.objects.GroundLine;
import com.mygdx.audioryder.objects.Note;
import com.mygdx.audioryder.objects.SpaceShip;
import com.mygdx.audioryder.screens.GameScreen;
import com.mygdx.audioryder.screens.LoadingScreen;

import java.util.ArrayList;

public class AudioRyder extends Game {

	public AssetManager assets = new AssetManager();
	public static final String MODELS_PATH = "models/";
	public static final String SONGS_PATH = "songs/";

	LoadingScreen loadingScreen;
	public GameScreen gameScreen;

	public SpaceShip spaceShip;

	public Model levelModel;


	public Environment environment;

	FileHandle biisu;

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
		loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);

		//set variables
		score = 0;
		multiplier = 1;
		streak = 0;
		hitOrMissTimer = 0f;

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 2.0f));

		groundLines = new ArrayList<GroundLine>();

		//settings
		noteSpeed = 3f;
		sensitivity = 1f;
		songOffset = -0.1f;
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