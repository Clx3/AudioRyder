package com.mygdx.audioryder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.audioryder.AudioRyder;
import com.mygdx.audioryder.objects.Note;
import com.mygdx.audioryder.properties.Properties;
import com.mygdx.audioryder.song.SongHandler;

/**
 * Screen for when the game is paused. Simple menu, where you can access settings, continue,
 * restart or exit the game.
 *
 * @author Teemu Salminen
 * @author Joonas Salojärvi
 */

public class PauseScreen implements Screen {

    AudioRyder game;

    public Stage pauseStage;

    public PauseScreen(AudioRyder game) {
        this.game = game;
    }
    /**
     * Sets up all actor for pause screen.
     * Is called when the game screen is shown for the first time.
     * Creates actors, adds listeners to them and adds them to stage.
     */
    @Override
    public void show() {
        pauseStage = new Stage(game.viewport, game.batch);

        Gdx.input.setInputProcessor(pauseStage);

        Table pauseMenuTable = new Table();
        pauseMenuTable.setFillParent(true);

        TextButton continueGame = new TextButton(Properties.continueText, game.skin);
        continueGame.setSize(300f, 80f);

        TextButton settings = new TextButton(Properties.settingsText, game.skin);
        settings.setSize(300f, 80f);

        TextButton restart = new TextButton(Properties.restartText, game.skin);
        restart.setSize(300f, 80f);

        if (Properties.currentLocale == Properties.localeFI) {
            restart.getLabel().setFontScale(0.9f, 0.9f);
        }

        TextButton exit = new TextButton(Properties.exitText, game.skin);
        exit.setSize(300f, 80f);

        continueGame.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.gameScreen.GAME_PAUSED = false;
                Gdx.input.setInputProcessor(game.gameScreen.gameOverlay);
                SongHandler.gameMusic.play();
                game.setScreen(game.gameScreen);
            }
        });

        settings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SongHandler.gameMusic.pause();
                game.setScreen(game.mainMenuScreen);
                game.mainMenuScreen.currentStage = game.mainMenuScreen.settingsStage;
                Gdx.input.setInputProcessor(game.mainMenuScreen.currentStage);
            }
        });

        restart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (Note note : game.gameScreen.notes) {
                    note.setActive(false);
                    game.gameScreen.notesToRemove.add(note);
                    game.gameScreen.gameObjectsToRemove.add(note);
                }
                game.gameScreen.gameObjects.removeAll(game.gameScreen.gameObjectsToRemove);
                game.gameScreen.gameObjectsToRemove.clear();
                game.gameScreen.notes.removeAll(game.gameScreen.notesToRemove);
                game.gameScreen.notesToRemove.clear();
                game.songTimer = 0f;
                game.gameScreen.score = 0;
                SongHandler.setupSong(game, game.currentSong);
                Gdx.input.setInputProcessor(game.gameScreen.gameOverlay);
                SongHandler.gameMusic.play();
                game.gameScreen.GAME_PAUSED = false;
                game.setScreen(game.gameScreen);
            }
        });

        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //game.score = 0;
                game.setScreen(game.mainMenuScreen);
                game.GAME_IS_ON = false;
            }
        });


        pauseMenuTable.add(continueGame).size(continueGame.getWidth(), continueGame.getHeight()).pad(15f);
        pauseMenuTable.row();
        pauseMenuTable.add(settings).size(settings.getWidth(), settings.getHeight()).pad(15f);
        pauseMenuTable.row();
        pauseMenuTable.add(restart).size(restart.getWidth(), restart.getHeight()).pad(15f);
        pauseMenuTable.row();
        pauseMenuTable.add(exit).size(exit.getWidth(), exit.getHeight()).pad(15f);

        pauseStage.addActor(pauseMenuTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor( 0, 0, 0, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        pauseStage.act(Gdx.graphics.getDeltaTime());
        pauseStage.draw();
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
