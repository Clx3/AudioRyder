package com.mygdx.audioryder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.mygdx.audioryder.AudioRyder;
import com.mygdx.audioryder.song.Song;

/**
 * Created by Teemu on 28.3.2018.
 */

public class LevelLoadingScreen implements Screen {

    AudioRyder game;
    Song songToBeLoaded;

    public LevelLoadingScreen(AudioRyder game, Song songToBeLoaded) {
        this.game = game;
        this.songToBeLoaded = songToBeLoaded;
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter());

        game.cam2D.setToOrtho(false,500f,300f);
        game.batch.setProjectionMatrix(game.cam2D.combined);

        game.assets.load(AudioRyder.SONGS_PATH + songToBeLoaded.getSongFileString(), Music.class);
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
