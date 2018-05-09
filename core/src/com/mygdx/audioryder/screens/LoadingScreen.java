package com.mygdx.audioryder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.audioryder.AudioRyder;


/**
 * This class is the first Screen that the game
 * uses and it is used for loading necessary assets for the game
 * and it also splashes all of the three logos that are related
 * to this project(TAMK, Tiko and Exerium).
 *
 * @version 2018.0509
 * @author Teemu Salminen
 * @author Joonas Salojärvi
 */
public class LoadingScreen implements Screen {

    //TODO: LOADING TEXT

    AudioRyder game;

    /** This is the Stage that this LoadingScreen uses. */
    private Stage loadingStage;

    /** This Image is the TAMK logo. Initialized in show(). */
    private Image tamkLogo;

    /** This Image is the Exerium logo. Initialized in show(). */
    private Image exeriumLogo;

    /** This image is the Tiko logo. Initialized in show(). */
    private Image tikoLogo;

    /** This is used to check the "stage" of the logo splashes. */
    private int actionStage = 0;

    /** This boolean checks if there is currently a logo Actor acting in the Stage. */
    private boolean actionRunning = false;

    /** This boolean is set to true when all three
     * (TAMK, Tiko, Exerium) logos have been
     * shown on the screen even if the assets would have been loaded.
     * If this is false, the Screen wont change.*/
    private boolean allLogosActed = false;

    public LoadingScreen(AudioRyder game) {
        this.game = game;
    }

    @Override
    public void show() {

        game.cam2D.setToOrtho(false, game.ORTHOCAM_VIEWPORT_WIDTH, game.ORTHOCAM_VIEWPORT_HEIGHT);
        game.batch.setProjectionMatrix(game.cam2D.combined);

        /* Setting the Stage: */
        loadingStage = new Stage(game.viewport, game.batch);

        Gdx.input.setInputProcessor(loadingStage);

        /* Creating the Images & Actors: */
        Texture backgroundTexture = new Texture(Gdx.files.internal(game.SPRITES_PATH + "menubackground.jpg"));
        Image background = new Image(backgroundTexture);
        background.setSize(game.ORTHOCAM_VIEWPORT_WIDTH, game.ORTHOCAM_VIEWPORT_HEIGHT);

        Texture audioRyderTextTexture = new Texture(Gdx.files.internal(game.SPRITES_PATH + "audioryder.png"));
        TextureRegion audioRyderTextRegion = new TextureRegion(audioRyderTextTexture);
        Image audioRyderText = new Image(audioRyderTextRegion);
        audioRyderText.setSize(750f, 200f);
        audioRyderText.setPosition(game.WINDOW_WIDTH / 2 - audioRyderText.getWidth() / 2, game.WINDOW_HEIGHT - audioRyderText.getHeight());

        Texture tamkTexture = new Texture(Gdx.files.internal(game.SPRITES_PATH + "tamk.png"));
        TextureRegion tamkTextureReg = new TextureRegion(tamkTexture);
        tamkLogo = new Image(tamkTextureReg);
        tamkLogo.setPosition(game.ORTHOCAM_VIEWPORT_WIDTH / 2 - tamkLogo.getWidth() / 2 - 10f,20f);
        tamkLogo.getColor().a = 0f;

        Texture exeriumTexture = new Texture(Gdx.files.internal(game.SPRITES_PATH + "exerium.png"));
        TextureRegion exeriumTextureReg = new TextureRegion(exeriumTexture);
        exeriumLogo = new Image(exeriumTextureReg);
        exeriumLogo.setPosition(game.ORTHOCAM_VIEWPORT_WIDTH / 2 - exeriumLogo.getWidth() / 2,20f);
        exeriumLogo.getColor().a = 0f;

        Texture tikoTexture = new Texture(Gdx.files.internal(game.SPRITES_PATH + "tiko.png"));
        TextureRegion tikoTextureReg = new TextureRegion(tikoTexture);
        tikoLogo = new Image(tikoTextureReg);
        tikoLogo.setPosition(game.ORTHOCAM_VIEWPORT_WIDTH / 2 - tikoLogo.getWidth() / 2,20f);
        tikoLogo.getColor().a = 0f;

        Label loadingText = new Label("Loading...", game.skin, "xolonium", Color.WHITE);
        loadingText.setPosition(game.ORTHOCAM_VIEWPORT_WIDTH / 2 - loadingText.getWidth() / 2, game.ORTHOCAM_VIEWPORT_HEIGHT / 2);

        /* Adding the actors to the Stage: */
        loadingStage.addActor(background);
        loadingStage.addActor(audioRyderText);
        loadingStage.addActor(tamkLogo);
        loadingStage.addActor(exeriumLogo);
        loadingStage.addActor(tikoLogo);
        loadingStage.addActor(loadingText);

        /* -- ASSETS THAT ARE GOING TO BE LOADED HERE: -- */

        /* Models */
        game.assets.load(AudioRyder.MODELS_PATH + "Spaceship.g3db", Model.class);
        game.assets.load(AudioRyder.MODELS_PATH + "Pyramid_green.g3db", Model.class);
        game.assets.load(AudioRyder.MODELS_PATH + "Pyramid_yellow.g3db", Model.class);
        game.assets.load(AudioRyder.MODELS_PATH + "Pyramid_red.g3db", Model.class);
        game.assets.load(AudioRyder.MODELS_PATH + "TrackRE.g3db", Model.class);
        game.assets.load(AudioRyder.MODELS_PATH + "Skydome_WIP.g3db", Model.class);
        game.assets.load(AudioRyder.MODELS_PATH + "Planet1.g3db", Model.class);
        game.assets.load(AudioRyder.MODELS_PATH + "Planet2.g3db", Model.class);
        game.assets.load(AudioRyder.MODELS_PATH + "Planet3.g3db", Model.class);
        game.assets.load(AudioRyder.MODELS_PATH + "Asteroid.g3db", Model.class);

        /* Sounds */
        game.assets.load(AudioRyder.SOUNDS_PATH + "notehit.wav", Sound.class);
        game.assets.load(AudioRyder.SOUNDS_PATH + "click.mp3", Sound.class);
    }

    /**
     * This Action method is used to create
     * a basic action that fades the actor in and then
     * fades the actor out.
     *
     * @return Returns the created Action.
     */
    private Action createFadeInOutAction() {
        Action out = new SequenceAction(Actions.fadeIn(0.7f), Actions.fadeOut(0.7f), Actions.run(new Runnable() {
            public void run () {
                actionRunning = false;

                if(actionStage == 2 && !allLogosActed)
                    allLogosActed = true;

                if(actionStage < 2)
                    actionStage ++;
                else
                    actionStage = 0;
            }
        }));
        return out;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.67f, 0.67f, 0.67f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        loadingStage.act(Gdx.graphics.getDeltaTime());
        loadingStage.draw();

        /* Please no hate, at least it works :--D : -Teemu */
        if(!actionRunning) {
            actionRunning = true;
            if (actionStage == 0) {
                tamkLogo.addAction(createFadeInOutAction());
            } else if(actionStage == 1) {
                exeriumLogo.addAction(createFadeInOutAction());
            } else if(actionStage == 2) {
                tikoLogo.addAction(createFadeInOutAction());
            }
        }

        if(game.assets.update() && allLogosActed) {
            game.mainMenuScreen = new MainMenuScreen(game);
            game.setScreen(game.mainMenuScreen);
        }
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
