package com.mygdx.audioryder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Model;
import com.mygdx.audioryder.AudioRyder;
import com.mygdx.audioryder.objects.GroundLine;
import com.mygdx.audioryder.objects.Note;
import com.mygdx.audioryder.objects.SpaceShip;

/**
 * Created by Teemu on 1.3.2018.
 */

public class GameScreen implements Screen {

    AudioRyder game;

    public GameScreen(AudioRyder game) {
        this.game = game;
    }

    @Override
    public void show() {
        Model tempModel;

        tempModel = game.assets.get("Spaceship_Animated_WIP.g3db");
        game.spaceShip = new SpaceShip(tempModel, 1f);

        tempModel = game.assets.get("Pyramid_Animated.g3db");
        game.box = (tempModel);

        game.mp3biisu = game.assets.get("erika.mp3");
        game.mp3biisu.play();
    }

    @Override
    public void render(float delta) {
        System.out.println("GameSCreen");

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        addBlocks();
        checkNoteHit();
        game.spaceShip.move();
        game.cam3d.update();
        game.modelBatch.begin(game.cam3d);
        for (Note obj : game.notes) {
            obj.move3d();
            obj.render(game.modelBatch, game.environment);
        }
        for (GroundLine obj : game.groundLines){
            obj.render(game.modelBatch, game.environment);
        }
        game.spaceShip.draw3d(game.modelBatch, game.environment);
        game.modelBatch.end();

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

    }

   public void addBlocks(){
       game.songTimer += Gdx.graphics.getDeltaTime();
        if (game.songPointer < game.noteArray.length - 1 && Float.parseFloat(game.noteArray[game.songPointer].replaceAll("[a-zA-Z]", "")) < game.songTimer + (5f / game.noteSpeed) + game.songOffset) {
            game.direction = game.noteArray[game.songPointer].replaceAll("[0-9]", "").charAt(1);
            if (game.direction == 'U') {
                game.notes.add(new Note(1.45f, 0, game.box, game.noteSpeed));
            } else if (game.direction == 'D') {
                game.notes.add(new Note(1.40f, 2, game.box ,game.noteSpeed));
            } else if (game.direction == 'L') {
                game.notes.add(new Note(-0.85f, 1, game.box ,game.noteSpeed));
            } else if (game.direction == 'R') {
                game.notes.add(new Note(3.75f, 3, game.box, game.noteSpeed));

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
        game.batch.begin();
        game.batch.setProjectionMatrix(game.cam.combined);

        //tähti.move();
        //tähti.draw(batch);

        game.hitOrMissTimer += Gdx.graphics.getDeltaTime();
        if (game.hitOrMiss && game.score > 0 && game.hitOrMissTimer < 0.5f) {
            game.batch.draw(game.hit, 1.5f, 3f, 1f, 0.5f);
        } else if (!(game.hitOrMiss) && game.hitOrMissTimer < 0.5f) {
            game.batch.draw(game.miss, 1.5f, 3f, 1f, 0.5f);
        }

        game.cam.setToOrtho(false,1000f,600f);
        game.batch.setProjectionMatrix(game.cam.combined);
        game.cam.update();
        game.text.draw(game.batch, "Score :" + game.score, 750, 170);
        game.text.draw(game.batch, "Streak :" + game.streak, 750, 190);
        game.text.draw(game.batch, "Multiplier " + game.multiplier + "X", 750, 210);
        game.text.draw(game.batch, "X: " + Gdx.input.getAccelerometerX(), 750, 230);
        game.text.draw(game.batch, "Y: " + Gdx.input.getAccelerometerY(), 750, 250);
        game.text.draw(game.batch, "Z: " + Gdx.input.getAccelerometerZ(), 750, 270);
        game.cam.setToOrtho(false,10f,6f);
        game.batch.setProjectionMatrix(game.cam.combined);
        game.cam.update();
        game.batch.end();
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
