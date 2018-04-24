package com.mygdx.audioryder.screens;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.mygdx.audioryder.AudioRyder;
import com.mygdx.audioryder.song.SongHandler;

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
        Gdx.input.setInputProcessor(new InputAdapter());

        game.cam2D.setToOrtho(false,500f,300f);
        game.batch.setProjectionMatrix(game.cam2D.combined);

        /* Models */
        game.assets.load(AudioRyder.MODELS_PATH + "Spaceship.g3db", Model.class);
        game.assets.load(AudioRyder.MODELS_PATH + "Pyramid_green.g3db", Model.class);
        game.assets.load(AudioRyder.MODELS_PATH + "Pyramid_yellow.g3db", Model.class);
        game.assets.load(AudioRyder.MODELS_PATH + "Pyramid_red.g3db", Model.class);
        game.assets.load(AudioRyder.MODELS_PATH + "TrackRE.g3db", Model.class);
        game.assets.load(AudioRyder.MODELS_PATH + "Skydome_WIP.g3db", Model.class);

        /* Sounds */
        game.assets.load(AudioRyder.SOUNDS_PATH + "notehit.wav", Sound.class);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if(game.assets.update()) {
            game.mainMenuScreen = new MainMenuScreen(game);
            game.setScreen(game.mainMenuScreen);

        }
        game.cam2D.update();
        game.batch.begin();
        game.font.draw(game.batch, "Loading...", 230, 170);
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
