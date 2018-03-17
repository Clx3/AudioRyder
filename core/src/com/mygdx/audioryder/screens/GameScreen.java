package com.mygdx.audioryder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.mygdx.audioryder.AudioRyder;
import com.mygdx.audioryder.objects.GroundLine;
import com.mygdx.audioryder.objects.Note;
import com.mygdx.audioryder.objects.SpaceShip;
import com.mygdx.audioryder.song.Song;
import com.mygdx.audioryder.song.SongHandler;

/**
 * Created by Teemu on 1.3.2018.
 */

public class GameScreen implements Screen {

    AudioRyder game;

    OrthographicCamera cam;
    PerspectiveCamera cam3d;

    SpriteBatch batch;
    ModelBatch modelBatch;

    BitmapFont text;

    Texture hit;
    Texture miss;

    Music currentSong;

    float levelTimer = 0f;

    Song erikaSong;

    /* Messy lets just use this temporarily: */
    public String[] noteArray;

    public GameScreen(AudioRyder game) {
        this.game = game;
    }

    @Override
    public void show() {
        //set cameras, batches and environment
        cam3d = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam3d.position.set(0f,3f,3f);
        cam3d.lookAt(0f,1f,0f);
        cam3d.near = 0.1f;
        cam3d.far = 300.0f;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, 10f,6f);

        modelBatch = new ModelBatch();
        batch = new SpriteBatch();

        text = new BitmapFont();

        Model tempModel;

        tempModel = game.assets.get("Spaceship_Animated_WIP.g3db");
        game.spaceShip = new SpaceShip(tempModel, 1f);

        tempModel = game.assets.get("Pyramid_Animated.g3db");
        game.box = (tempModel);

        tempModel = game.assets.get("Track_WIP_3_Lanes.g3db",Model.class);
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

        //create array for notes
        String noteData = SongHandler.currentNoteFile.readString();
        noteArray = new String[noteData.split(" ").length - 1];
        noteArray = noteData.split(" ");

        SongHandler.currentSong.play();

        hit = new Texture("hit.png");
        miss = new Texture("miss.png");
    }

    @Override
    public void render(float delta) {
        System.out.println("GameSCreen");

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        addBlocks();
        checkNoteHit();
        game.spaceShip.move();
        cam3d.update();
        modelBatch.begin(cam3d);
        for (Note obj : game.notes) {
            obj.move3d();
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

        drawTextAndSprites();
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
        batch.dispose();
        modelBatch.dispose();
        hit.dispose();
        miss.dispose();
        currentSong.dispose();
        text.dispose();
    }

   public void addBlocks(){
       game.songTimer += Gdx.graphics.getDeltaTime();
        if (game.songPointer < noteArray.length - 1 && Float.parseFloat(noteArray[game.songPointer].replaceAll("[a-zA-Z]", "")) < game.songTimer + (5f / game.noteSpeed) + game.songOffset) {
            game.direction = noteArray[game.songPointer].replaceAll("[0-9]", "").charAt(1);
            if (game.direction == 'U') {
                game.notes.add(new Note(0f, 0, game.box, game.noteSpeed));
            } else if (game.direction == 'D') {
                game.notes.add(new Note(0f, 2, game.box ,game.noteSpeed));
            } else if (game.direction == 'L') {
                game.notes.add(new Note(-4.5f, 1, game.box ,game.noteSpeed));
            } else if (game.direction == 'R') {
                game.notes.add(new Note(4.5f, 3, game.box, game.noteSpeed));

            }
            game.songPointer++;
        }
    }

    public void checkNoteHit(){
        for(int i = game.notes.size() - 1; i >= 0; i--){
            if(game.notes.get(i).getY() > -4f) {
                if (game.notes.get(i).getY() > -2.8f) {
                    game.notes.remove(i);
                    game.streak = 0;
                    game.hitOrMiss = false;
                    game.hitOrMissTimer = 0;
                    continue;
                }

                if (game.notes.get(i).getDirection() == 0 && (Gdx.input.isKeyJustPressed(Input.Keys.DPAD_UP) ||
                        (game.spaceShip.getX() < 1f && game.spaceShip.getX() > -1f))) {
                    game.score = game.score + game.multiplier;
                    game.streak++;
                    game.notes.remove(i);
                    game.hitOrMiss = true;
                    game.hitOrMissTimer = 0;
                    continue;
                } else if (game.notes.get(i).getDirection() == 2 && (Gdx.input.isKeyJustPressed(Input.Keys.DPAD_DOWN) ||
                        (game.spaceShip.getX() > -1f && game.spaceShip.getX() < 1f))) {
                    game.score = game.score + game.multiplier;
                    game.streak++;
                    game.notes.remove(i);
                    game.hitOrMiss = true;
                    game.hitOrMissTimer = 0;
                    continue;
                } else if (game.notes.get(i).getDirection() == 1 && (Gdx.input.isKeyJustPressed(Input.Keys.DPAD_LEFT) ||
                        game.spaceShip.getX() < -1.5f)) {
                    game.score = game.score + game.multiplier;
                    game.streak++;
                    game.notes.remove(i);
                    game.hitOrMiss = true;
                    game.hitOrMissTimer = 0;
                    continue;
                } else if (game.notes.get(i).getDirection() == 3 && (Gdx.input.isKeyJustPressed(Input.Keys.DPAD_RIGHT) ||
                        game.spaceShip.getX() > 1.5f)) {
                    game.score = game.score + game.multiplier;
                    game.streak++;
                    game.notes.remove(i);
                    game.hitOrMiss = true;
                    game.hitOrMissTimer = 0;
                }
            }

            if (game.streak >= 30){
                game.multiplier = 8;
            } else if (game.streak >= 20){
                game.multiplier = 4;
            } else if(game.streak >= 10){
                game.multiplier = 2;
            } else if (game.streak < 10){
                game.multiplier = 1;
            }
        }

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

    public void doneLoading(){
      /*  submarine = assets.get("Spaceship_Animated_WIP.g3db",Model.class);
        mp3biisu = assets.get("erika.mp3",Music.class);
        spaceShip = new SpaceShip(submarine, sensitivity);
        loading = false;
        mp3biisu.play();
        songTimer = 0f;
        //tähti = new Star(hit);*/
    }
}
