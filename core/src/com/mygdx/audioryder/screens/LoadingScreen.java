package com.mygdx.audioryder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g3d.Model;
import com.mygdx.audioryder.AudioRyder;

/**
 * Created by Teemu on 1.3.2018.
 */

public class LoadingScreen implements Screen {

    AudioRyder game;

    public LoadingScreen(AudioRyder game) {
        this.game = game;
    }

    @Override
    public void show() {
        game.assets.load("Spaceship_Animated_WIP.g3db",Model.class);
        game.assets.load("Pyramid_Animated.g3db",Model.class);
        game.assets.load("Pyramid_Animated.g3db", Model.class);
        game.assets.load("erika.mp3", Music.class);
    }

    @Override
    public void render(float delta) {
        if(game.assets.update()) {
            System.out.println("LOL");
            if(Gdx.input.isTouched()) {
                game.gameScreen = new GameScreen(game);
                game.setScreen(game.gameScreen);
            }
        }

        game.batch.begin();
        game.cam.setToOrtho(false,500f,300f);
        game.batch.setProjectionMatrix(game.cam.combined);
        game.cam.update();
        game.text.draw(game.batch, "Loading...", 230, 170);
        game.cam.setToOrtho(false,10f,6f);
        game.batch.setProjectionMatrix(game.cam.combined);
        game.cam.update();
        game.batch.end();
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
