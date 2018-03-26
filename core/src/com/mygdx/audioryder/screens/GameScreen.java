package com.mygdx.audioryder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.mygdx.audioryder.AudioRyder;
import com.mygdx.audioryder.objects.GroundLine;
import com.mygdx.audioryder.objects.Note;
import com.mygdx.audioryder.objects.SpaceShip;
import com.mygdx.audioryder.song.Song;
import com.mygdx.audioryder.song.SongHandler;

import java.util.ArrayList;

/**
 * Created by Teemu on 1.3.2018.
 */

public class GameScreen implements Screen {

    AudioRyder game;

    public ArrayList<Note> notes = new ArrayList<Note>();
    public ArrayList<Note> notesToRemove = new ArrayList<Note>();

    OrthographicCamera cam;
    PerspectiveCamera cam3D;

    SpriteBatch batch;
    ModelBatch modelBatch;

    BitmapFont text;

    Texture hit;
    Texture miss;

    Music currentSong;

    float levelTimer = 0f;

    Song erikaSong;

    ShapeRenderer shapeRenderer;

    public GameScreen(AudioRyder game) {
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter());

        //set cameras, batches and environment
        cam3D = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam3D.position.set(0f,3f,3f);
        cam3D.lookAt(0f,1f,0f);
        cam3D.near = 0.1f;
        cam3D.far = 300.0f;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, 10f,6f);

        modelBatch = new ModelBatch();
        batch = new SpriteBatch();

        text = new BitmapFont();

        Model tempModel;

        tempModel = game.assets.get(AudioRyder.MODELS_PATH + "Spaceship.g3db");
        game.spaceShip = new SpaceShip(game, tempModel, 1f);

        tempModel = game.assets.get(AudioRyder.MODELS_PATH + "Pyramid.g3db");
        game.box = (tempModel);

        tempModel = game.assets.get(AudioRyder.MODELS_PATH + "Track3Lanes.g3db",Model.class);
        game.levelModel = (tempModel);

        for(float i = -15f; i > -392f; i -= 56f){
            game.groundLines.add(new GroundLine(i,game.levelModel,game.noteSpeed));
        }

        /* first prototype of using the new way of handling the levels aka "songs": */
        erikaSong = new Song("erika.mp3", "erika.txt");

        //Using the songhandler now, this will become usefull when we add multiple levels and
        //a loading screen from main menu to game.
        Song currentSong = erikaSong;
        SongHandler.setupSong(game, currentSong);
        SongHandler.currentSong.play();

        hit = new Texture("hit.png");
        miss = new Texture("miss.png");
    }

    @Override
    public void render(float delta) {
        //System.out.println("GameSCreen");

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        SongHandler.addNotesToGame(game);
        game.spaceShip.move();
        cam3D.update();
        modelBatch.begin(cam3D);
        for (Note obj : notes) {
            obj.move3d(game);
            obj.render(modelBatch, game.environment);
        }
        for (GroundLine obj : game.groundLines){
            obj.move3d();
            obj.render(modelBatch, game.environment);
        }
        levelTimer += Gdx.graphics.getDeltaTime();
        if(levelTimer > 56f / (game.noteSpeed * 10f)){
            game.groundLines.add(new GroundLine(-336f,game.levelModel,game.noteSpeed));
            levelTimer = 0f;
        }
        game.spaceShip.draw3d(modelBatch, game.environment);
        modelBatch.end();
        drawBoundingBox(game.spaceShip.minPointBox, game.spaceShip.maxPointBox);
        for (Note obj : notes) {
            drawBoundingBox(obj.minPoint, obj.maxPoint);
        }
        drawTextAndSprites();

        notes.removeAll(notesToRemove);
        notesToRemove.clear();
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

    public void drawBoundingBox(Vector3 vectorMin, Vector3 vectorMax) {

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam3D.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);

        shapeRenderer.line(vectorMin.x, vectorMin.y, vectorMin.z, vectorMax.x, vectorMin.y, vectorMin.z);
        shapeRenderer.line(vectorMin.x, vectorMin.y, vectorMin.z, vectorMin.x, vectorMin.y, vectorMax.z);
        shapeRenderer.line(vectorMin.x, vectorMin.y, vectorMax.z, vectorMax.x, vectorMin.y, vectorMax.z);
        shapeRenderer.line(vectorMax.x, vectorMin.y, vectorMin.z, vectorMax.x, vectorMin.y, vectorMax.z);

        shapeRenderer.line(vectorMin.x, vectorMax.y, vectorMin.z, vectorMax.x, vectorMax.y, vectorMin.z);
        shapeRenderer.line(vectorMin.x, vectorMax.y, vectorMin.z, vectorMin.x, vectorMax.y, vectorMax.z);
        shapeRenderer.line(vectorMin.x, vectorMax.y, vectorMax.z, vectorMax.x, vectorMax.y, vectorMax.z);
        shapeRenderer.line(vectorMax.x, vectorMax.y, vectorMin.z, vectorMax.x, vectorMax.y, vectorMax.z);

        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        modelBatch.dispose();
        hit.dispose();
        miss.dispose();
        currentSong.dispose();
        text.dispose();
        shapeRenderer.dispose();
    }

    public void drawTextAndSprites(){
        batch.begin();
        batch.setProjectionMatrix(cam.combined);

        game.hitOrMissTimer += Gdx.graphics.getDeltaTime();
        if (game.hitOrMiss && game.score > 0 && game.hitOrMissTimer < 0.5f) {
            batch.draw(hit, 1.5f, 3f, 1f, 0.5f);
        } else if (!(game.hitOrMiss) && game.hitOrMissTimer < 0.5f) {
            batch.draw(miss, 1.5f, 3f, 1f, 0.5f);
        }

        cam.setToOrtho(false,1000f,600f);
        batch.setProjectionMatrix(cam.combined);
        cam.update();
        text.draw(batch, "Score :" + game.score, 750, 170);
        text.draw(batch, "Streak :" + game.streak, 750, 190);
        text.draw(batch, "Multiplier " + game.multiplier + "X", 750, 210);
        text.draw(batch, "X: " + Gdx.input.getAccelerometerX(), 750, 230);
        text.draw(batch, "Y: " + Gdx.input.getAccelerometerY(), 750, 250);
        text.draw(batch, "Z: " + Gdx.input.getAccelerometerZ(), 750, 270);
        cam.setToOrtho(false,10f,6f);
        batch.setProjectionMatrix(cam.combined);
        cam.update();
        batch.end();
    }
}
