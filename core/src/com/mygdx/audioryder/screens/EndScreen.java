package com.mygdx.audioryder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
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
import com.mygdx.audioryder.AudioRyder;
import com.mygdx.audioryder.preferences.UserSettings;
import com.mygdx.audioryder.properties.Properties;
import com.mygdx.audioryder.screens.GameScreen;
import com.mygdx.audioryder.song.Song;
import com.mygdx.audioryder.song.SongHandler;

/**
 * Ending screen. Shown after the song has ended.
 * Shows your score and local high scores. Does
 * also the logic behind highscore checking.
 *
 * @version 2018.0509
 * @author Teemu Salminen
 * @author Joonas Salojärvi
 */
public class EndScreen implements Screen {

    /**This is a very simple variable */
    AudioRyder game;

    /**
     * Background image. Same background stage as in main menu.
     */
    Stage backgroundStage;

    /**
     * Stage for all actor of this screen.
     */
    Stage endStage;

    Label scoreText1;
    Label scoreText2;
    Label highScores1;
    Label highScores2;
    TextButton returnButton;

    /**
     * Your score in the game you just played.
     */
    int score;

    /**
     * The song you played. Used for saving high scores.
     */
    Song currentSong;

    public EndScreen(AudioRyder game, int score, Song currentSong) {
        this.game = game;
        this.score = score;
        this.currentSong = currentSong;
    }

    /**
     * Sets up all actors for the main stage of this screen.
     */
    private void setupActors() {
        returnButton = new TextButton(Properties.returnText, game.skin);
        returnButton.setSize(250f,80f);
        returnButton.setPosition(game.ORTHOCAM_VIEWPORT_WIDTH - returnButton.getWidth(), returnButton.getY());

        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                returnButton.setChecked(false);
                //game.score = 0;
                game.setScreen(game.mainMenuScreen);
            }
        });
        endStage.addActor(returnButton);

        scoreText1 = new Label(Properties.scoreText, game.skin, "xolonium", Color.WHITE);
        scoreText1.setFontScale(1f);
        scoreText1.setPosition(50f,game.ORTHOCAM_VIEWPORT_HEIGHT - scoreText1.getHeight() - 30f);
        scoreText2 = new Label(score + "", game.skin,"xolonium", Color.WHITE);
        scoreText2.setFontScale(1.5f);
        scoreText2.setPosition(50f,scoreText1.getY() - scoreText1.getHeight());


        endStage.addActor(scoreText1);
        endStage.addActor(scoreText2);

        //high scores
        checkHighScore();
        Preferences prefs = Gdx.app.getPreferences(game.currentSong.getName());
        highScores1 = new Label(Properties.highScoreText + "\n1. " + prefs.getString("entry" + 1 + "name") + "\n" +
                "2. " + prefs.getString("entry" + 2 + "name")+ "\n" +
                "3. " + prefs.getString("entry" + 3 + "name")+ "\n" +
                "4. " + prefs.getString("entry" + 4 + "name")+ "\n" +
                "5. " + prefs.getString("entry" + 5 + "name"), game.skin,"xolonium", Color.WHITE);

        highScores1.setPosition(300f,scoreText2.getY() - highScores1.getHeight() - 50f);
        endStage.addActor(highScores1);

        highScores2 = new Label( prefs.getInteger("entry" + 1) + "\n" +
                prefs.getInteger("entry" + 2)+ "\n" +
                prefs.getInteger("entry" + 3)+ "\n" +
                prefs.getInteger("entry" + 4)+ "\n" +
                prefs.getInteger("entry" + 5), game.skin,"xolonium", Color.WHITE);

        highScores2.setPosition(highScores1.getX() + highScores1.getWidth() + 50f, highScores1.getY());
        endStage.addActor(highScores2);
    }

    @Override
    public void show() {

        backgroundStage = new Stage(game.viewport, game.batch);
        endStage = new Stage(game.viewport, game.batch);

        setupActors();

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

    /**
     * Logic behind checking high scores. Gets preferences and if empty, puts
     * empty scores there. Then checks if the player got a score higher than
     * any of the saved scores. If yes, moves all scores down on the list
     * and puts player's score on the right spot.
     *
     */
    public void checkHighScore(){
        Preferences prefs = Gdx.app.getPreferences(game.currentSong.getName());
        for(int i = 1; i <= 6; i++) {
            if (!(prefs.contains("entry" + i))) {
                prefs.putInteger("entry" + i, 0);
                prefs.putString("entry" + i +"name", "NaN");

            }
        }
        prefs.flush();

        for(int i = 1; i <= 6; i++){
            if(prefs.getInteger("entry" + i) < score){
                for(int j = 6; j >= i; j--){
                    int next = j + 1;
                    prefs.putInteger("entry" + next,prefs.getInteger("entry" + j));
                    prefs.putString("entry" + next +"name", prefs.getString("entry" + j + "name"));
                }
                prefs.putInteger("entry" + i,score);
                prefs.putString("entry" + i +"name", UserSettings.playerName);
                break;
            }
        }


        prefs.flush();
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
