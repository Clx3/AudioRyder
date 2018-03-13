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

	LoadingScreen loadingScreen;
	public GameScreen gameScreen;

	public SpaceShip spaceShip;

	public Model levelModel;

	public OrthographicCamera cam;
	public PerspectiveCamera cam3d;

	public SpriteBatch batch;
	public ModelBatch modelBatch;

	public Environment environment;

	public ArrayList<Note> notes = new ArrayList<Note>();

	FileHandle biisu;
	public String[] noteArray;

	public int songPointer = 0;
	public int score;
	public int streak;
	public int multiplier;
	public float hitOrMissTimer;
	public boolean hitOrMiss = true;

	public BitmapFont text;
	public char direction;
	public Model box;
	Model groundLine;
	public Float songTimer = 0f;
	public boolean loading;
	public float songOffset;
	public ArrayList<GroundLine> groundLines;
	public float noteSpeed;
	float sensitivity;

	@Override
	public void create () {
		loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);

		//load files
		biisu = Gdx.files.internal("erika.txt");
		text = new BitmapFont();

		//create array for notes
		String noteData = biisu.readString();
		noteArray = new String[noteData.split(" ").length - 1];
		noteArray = noteData.split(" ");

		//set variables
		score = 0;
		multiplier = 1;
		streak = 0;
		hitOrMissTimer = 0f;


		//create models if needed
		ModelBuilder modelBuilder = new ModelBuilder();

		groundLine = modelBuilder.createBox(0.05f, 0.05f, 55f, new Material(ColorAttribute.createDiffuse(Color.WHITE)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);



		//set cameras, batches and environment
		cam3d = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam3d.position.set(0f,3f,3f);
		cam3d.lookAt(0f,1f,0f);
		cam3d.near = 0.1f;
		cam3d.far = 300.0f;
		cam = new OrthographicCamera();
		cam.setToOrtho(false, 10f,6f);

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 2.0f));

		modelBatch = new ModelBatch();
		batch = new SpriteBatch();

		groundLines = new ArrayList<GroundLine>();

		//settings
		noteSpeed = 3f;
		sensitivity = 1f;
		songOffset = -0.1f;
	}

	@Override
	public void render () {
		/*if(loading){
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
			loadingScreen();
		}
		//if (loading && assets.update())
			//doneLoading();
		if (!(loading)) {
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

			addBlocks();
			checkNoteHit();
			spaceShip.move();
			cam3d.update();
			modelBatch.begin(cam3d);
			for (Note obj : harrit) {
				obj.move3d();
				obj.render(modelBatch, environment);
			}
			for (GroundLine obj : groundLines){
				obj.render(modelBatch, environment);
			}
			spaceShip.draw3d(modelBatch, environment);
			modelBatch.end();

			drawTextAndSprites();

		}*/

		super.render();

	}

	@Override
	public void dispose () {
		batch.dispose();
//	TEEMU	mp3biisu.dispose();

	}

}