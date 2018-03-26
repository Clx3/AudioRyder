package com.mygdx.audioryder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
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

    OrthographicCamera cam;
    SpriteBatch batch;

    BitmapFont text;

    public LoadingScreen(AudioRyder game) {
        this.game = game;

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter());

        cam = new OrthographicCamera();
        cam.setToOrtho(false,500f,300f);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(cam.combined);

        text = new BitmapFont();

        game.assets.load(AudioRyder.MODELS_PATH + "Spaceship.g3db",Model.class);
        game.assets.load(AudioRyder.MODELS_PATH + "Pyramid.g3db",Model.class);
        game.assets.load(AudioRyder.SONGS_PATH + "erika.mp3", Music.class);
        game.assets.load(AudioRyder.MODELS_PATH + "Track3Lanes.g3db",Model.class);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if(game.assets.update()) {
            System.out.println("LOL");
            if(Gdx.input.isTouched()) {
                game.gameScreen = new GameScreen(game);
                game.setScreen(game.gameScreen);
            }
        }
        cam.update();
        batch.begin();
        text.draw(batch, "Loading...", 230, 170);
        batch.end();
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
    }
}
