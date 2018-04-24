package com.mygdx.audioryder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.audioryder.AudioRyder;
import com.mygdx.audioryder.properties.Properties;
import com.mygdx.audioryder.screens.GameScreen;
import com.mygdx.audioryder.song.Song;

public class EndScreen implements Screen {

    AudioRyder game;
    Stage backgroundStage;
    Stage endStage;
    Image background;
    Viewport viewport;
    Skin testSkin;
    TextureAtlas testAtlas;
    Label scoreText1;
    Label scoreText2;
    TextButton returnButton;
    int score;
    Song currentSong;

    public EndScreen(AudioRyder game, int score, Song currentSong) {
        this.game = game;
        this.score = score;
        this.currentSong = currentSong;

        testSkin = new Skin();
        testSkin = game.mainMenuScreen.testSkin;
        testAtlas = new TextureAtlas();
        testAtlas = game.mainMenuScreen.testAtlas;


        viewport = new FitViewport(game.ORTHOCAM_VIEWPORT_WIDTH, game.ORTHOCAM_VIEWPORT_HEIGHT, game.cam2D);
        viewport.apply();

        backgroundStage = new Stage(viewport, game.batch);
        endStage = new Stage(viewport, game.batch);

        setupActors();
    }

    private void setupActors() {
        returnButton = new TextButton(Properties.returnText, testSkin);
        returnButton.setSize(250f,80f);
        returnButton.setPosition(game.ORTHOCAM_VIEWPORT_WIDTH - returnButton.getWidth(), returnButton.getY());

        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                returnButton.setChecked(false);
                game.score = 0;
                game.setScreen(game.mainMenuScreen);
            }
        });
        endStage.addActor(returnButton);

        scoreText1 = new Label(Properties.scoreText,testSkin, "nasalization", Color.WHITE);
        scoreText1.setFontScale(1f);
        scoreText1.setPosition(50f,game.ORTHOCAM_VIEWPORT_HEIGHT - scoreText1.getHeight() - 30f);
        scoreText2 = new Label(score + "",testSkin,"nasalization", Color.WHITE);
        scoreText2.setFontScale(1.5f);
        scoreText2.setPosition(50f,scoreText1.getY() - scoreText1.getHeight());


        endStage.addActor(scoreText1);
        endStage.addActor(scoreText2);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(endStage);

        backgroundStage = game.mainMenuScreen.backgroundStage;

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        backgroundStage.draw();
        endStage.act();
        endStage.draw();

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
