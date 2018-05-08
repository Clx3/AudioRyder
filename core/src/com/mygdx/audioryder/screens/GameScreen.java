package com.mygdx.audioryder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.audioryder.AudioRyder;
import com.mygdx.audioryder.objects.BackgroundObject;
import com.mygdx.audioryder.objects.GameObject;
import com.mygdx.audioryder.objects.GroundLine;
import com.mygdx.audioryder.objects.Note;
import com.mygdx.audioryder.objects.Skydome;
import com.mygdx.audioryder.objects.SpaceShip;
import com.mygdx.audioryder.properties.Properties;
import com.mygdx.audioryder.song.SongHandler;

import java.util.ArrayList;

/**
 * This class contains the logic and rendering
 * of our game and it contains the main loop of the
 * game. It handles everything that is needed
 * in the gameplay view "GameScreen".
 *
 * @author Teemu Salminen
 * @author Joonas Salojärvi
 */
public class GameScreen implements Screen {

    //TODO: PARTICLE FUCKING EFFECTS VOI VITTU I FUCK EMMI :---D

    AudioRyder game;

    /**
     * This ArrayList contains all the GameObjects in our game.
     */
    public ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();

    /**
     * This ArrayList contain all the GameObjects that are going to be removed from
     * the gameObjects ArrayList after every iteration.
     */
    public ArrayList<GameObject> gameObjectsToRemove = new ArrayList<GameObject>();

    /**
     * This ArrayList contains all the active Notes of our game.
     */
    public ArrayList<Note> notes = new ArrayList<Note>();

    /**
     * This ArrayList contains all the Notes that are going to be removed
     * from the notes ArrayList and this is action is done every iteration.
     */
    public ArrayList<Note> notesToRemove = new ArrayList<Note>();

    /**
     * This ArrayList contains all the GroundLine objects of our game.
     * To be more precise GroundLines are the "roads" which the spaceship
     * will fly on.
     */
    public ArrayList<GroundLine> groundLines = new ArrayList<GroundLine>();

    /**
     * This PerspectiveCamera is used for our GameScreen to
     * render our 3D world and 3D objects.
     */
    PerspectiveCamera cam3D;

    /**
     * This is the ModelBatch our game uses for our
     * models.
     */
    ModelBatch modelBatch;

    /**
     * This Array of Model's contains all of the pyramid
     * models used as our notes in the game.
     * 0 = green, 1 = yellow and 2 = red.
     */
    public Model[] pyramids = new Model[3];

    /**
     * This Model is the road or ground
     * which the SpaceShip will fly on.
     */
    private Model groundModel;

    /**
     * This Model is the sky or space in the background
     * of the game.
     */
    private Model skyModel;

    /**
     * This is the background of the game that uses the
     * skyModel.
     */
    private Skydome skydome;

    /**
     * This is the SpaceShip Object of our game.
     */
    private SpaceShip spaceShip;

    boolean GAME_PAUSED;

    Stage gameOverlay;

    /**
     * This Label is the Score text on top of the screen in the game.
     */
    private Label score;

    //TODO: REMOVE THIS BEFORE GOOGLE PLAY
    ShapeRenderer shapeRenderer;

    public GameScreen(AudioRyder game) {
        this.game = game;
    }

    @Override
    public void show() {
        if(!(game.GAME_IS_ON)) {
            Gdx.input.setInputProcessor(new InputAdapter());

            //set cameras, batches and environment
            cam3D = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            cam3D.position.set(0f, 3f, 3f);
            cam3D.lookAt(0f, 1f, 0f);
            cam3D.near = 0.1f;
            cam3D.far = 1000.0f;
            modelBatch = new ModelBatch();

            Model tempModel;

            tempModel = game.assets.get(AudioRyder.MODELS_PATH + "Spaceship.g3db");
            spaceShip = new SpaceShip(game, tempModel);

            tempModel = game.assets.get(AudioRyder.MODELS_PATH + "Pyramid_green.g3db");
            pyramids[0] = tempModel;

            tempModel = game.assets.get(AudioRyder.MODELS_PATH + "Pyramid_yellow.g3db");
            pyramids[1] = tempModel;

            tempModel = game.assets.get(AudioRyder.MODELS_PATH + "Pyramid_red.g3db");
            pyramids[2] = tempModel;

            tempModel = game.assets.get(AudioRyder.MODELS_PATH + "TrackRE.g3db", Model.class);
            groundModel = tempModel;

            tempModel = game.assets.get(AudioRyder.MODELS_PATH + "Skydome_WIP.g3db", Model.class);
            skyModel = tempModel;
            skydome = new Skydome(game, skyModel);

            for(int i = 0; i < MathUtils.random(3, 5); i++) {
                int planetType = MathUtils.random(1, 3);
                gameObjects.add(new BackgroundObject(game, game.assets.get(AudioRyder.MODELS_PATH + "Planet" + planetType + ".g3db", Model.class)));
            }

            tempModel = game.assets.get(AudioRyder.MODELS_PATH + "Asteroid.g3db", Model.class);
            for(int i = 0; i < MathUtils.random(5, 10); i++) {
                gameObjects.add(new BackgroundObject(game, tempModel));
            }

            groundLines.add(new GroundLine(game, groundModel, 0, -2f, 2f, 3.5f));


            //Using the songhandler now, this will become usefull when we add multiple levels and
            //a loading screen from main menu to game.
            SongHandler.setupSong(game, game.currentSong);
            SongHandler.gameMusic.play();
            game.songTimer = 0f;

            //LOLsetupPauseStage();
            setupGameOverlay();
            GAME_PAUSED = false;
            game.GAME_IS_ON = true;
        }


    }

    /**
     * Sets up all actor for game overlay.
     * Is called when the game screen is shown for the first time.
     * Creates actors, adds listeners to them and adds them to stage.
     */
    private void setupGameOverlay() {
        gameOverlay = new Stage(game.viewport, game.batch);

        final TextButton pauseButton = new TextButton(Properties.pauseText, game.skin);
        pauseButton.setSize(150f,50f);
        pauseButton.setPosition(game.ORTHOCAM_VIEWPORT_WIDTH - pauseButton.getWidth() - 10f,game.ORTHOCAM_VIEWPORT_HEIGHT - pauseButton.getHeight() - 10f);
        pauseButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pause();
            }
        });
        gameOverlay.addActor(pauseButton);
        score = new Label(Properties.scoreText + "\n" + game.score, game.skin,"xolonium",Color.WHITE);
        score.setAlignment(1);
        score.setPosition((game.ORTHOCAM_VIEWPORT_WIDTH / 2f) - (score.getWidth() / 2),(game.ORTHOCAM_VIEWPORT_HEIGHT) - (score.getHeight()) - 10f);
        gameOverlay.addActor(score);
        Gdx.input.setInputProcessor(gameOverlay);
    }

    @Override
    public void render(float delta) {
        if(!(GAME_PAUSED)) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

            SongHandler.addNotesToGame(game);

            updateCamera();
            renderGameObjects();
            removeAndSpawnLevel();
            //drawTextAndSprites(); //debug
            removeNonActive();
            drawOverlay();

            checkSongStatus();
        }


        /*
        //FIXME: nopee hätänen testausosio ending-screenille
        if(levelTimer > 2f){
            game.GAME_IS_ON = false;
            dispose();
            SongHandler.gameMusic.stop();
            game.endScreen = new EndScreen(game,1234567897, game.gameMusic);
            game.setScreen(game.endScreen);
        }*/

    }

    /**
     * Ends the game, when the music ends.
     * Checks if the music is playing. If not, sets GAME_IS_ON to false and
     * changes screen to endScreen with the correct variables, like score
     * and song name.
     */
    private void checkSongStatus() {
        if (!(SongHandler.gameMusic.isPlaying())) {
            game.GAME_IS_ON = false;
            dispose();
            SongHandler.gameMusic.stop();
            game.endScreen = new EndScreen(game,game.score, game.currentSong);
            game.setScreen(game.endScreen);
        }
    }

    /**
     * Updates and draws the game overlay.
     * Includes score and pause button.
     */
    private void drawOverlay() {
        //Overlay:
        score.setText(Properties.scoreText + "\n" + game.score);
        gameOverlay.act();
        gameOverlay.draw();
    }

    /**
     * Removes all game objects with isActive = false.
     * Removes non active objects from all the array lists.
     */
    private void removeNonActive() {
        gameObjects.removeAll(gameObjectsToRemove);
        gameObjectsToRemove.clear();

        notes.removeAll(notesToRemove);
        notesToRemove.clear();
    }

    /**
     * Removes off screen level and spawns new level.
     * Removes level blocks when they go off screen, and
     * spawns new level blocks far away.
     */
    private void removeAndSpawnLevel() {
        /* Spawning and removing the groundlines: */
        if (groundLines.get(groundLines.size() - 1).getZ() > -200f) {
            groundLines.add(new GroundLine(game, groundModel, 0, -2f, (groundLines.get(groundLines.size() - 1).getZ()) - 17.9f, 3.5f));
        }
        if (groundLines.get(0).getZ() > 30) {
            groundLines.get(0).setActive(false);
            groundLines.remove(0);
        }
    }

    /**
     * Renders all active 3D game objects.
     * Goes through the whole list of game objects, and checks if they are active.
     * For all the active ones, calls their renderAndUpdate() method. For all else,
     * adds to the to-be-removed list.
     */
    private void renderGameObjects() {
        modelBatch.begin(cam3D);
        for (GameObject object : gameObjects) {
            if (object.isActive())
                object.renderAndUpdate(modelBatch, game.environment);
            else
                gameObjectsToRemove.add(object);
        }

        modelBatch.end();
    }

    /**
     * Sets camera to correct position, which is behind the spaceship.
     */
    private void updateCamera() {
        cam3D.position.set(spaceShip.getX(), 2f + spaceShip.getY(), 6f);
        cam3D.lookAt(spaceShip.getX(), spaceShip.getY() + 1f, spaceShip.getZ());
        cam3D.update();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        GAME_PAUSED = true;
        SongHandler.gameMusic.pause();
        game.pauseScreen = new PauseScreen(game);
        game.setScreen(game.pauseScreen);
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    /**
     * Draws the bounding box of two given vectors.
     * Used for collision box debugging.
     * @param vectorMin
     * @param vectorMax
     */
    public void drawBoundingBox(Vector3 vectorMin, Vector3 vectorMax) {

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam3D.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);

        shapeRenderer.line(vectorMin.x, vectorMin.y, vectorMin.z, vectorMax.x, vectorMin.y, vectorMin.z);
        shapeRenderer.line(vectorMin.x, vectorMin.y, vectorMin.z, vectorMin.x, vectorMin.y, vectorMax.z);
        shapeRenderer.line(vectorMin.x, vectorMin.y, vectorMax.z, vectorMax.x, vectorMin.y, vectorMax.z);
        shapeRenderer.line(vectorMax.x, vectorMin.y, vectorMin.z, vectorMax.x, vectorMin.y, vectorMax.z);

        shapeRenderer.line(vectorMin.x, vectorMax.y, vectorMin.z, vectorMax.x, vectorMax.y, vectorMin.z);
        shapeRenderer.line(vectorMin.x, vectorMax.y, vectorMin.z, vectorMin.x, vectorMax.y, vectorMax.z);
        shapeRenderer.line(vectorMin.x, vectorMax.y, vectorMax.z, vectorMax.x, vectorMax.y, vectorMax.z);
        shapeRenderer.line(vectorMax.x, vectorMax.y, vectorMin.z, vectorMax.x, vectorMax.y, vectorMax.z);

        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        spaceShip.dispose();
        groundModel.dispose();
        skyModel.dispose();

        for(int i = 0; i < pyramids.length-1; i++) {
            pyramids[i].dispose();
        }
    }


    /**
     * Draws BitMapFont and textures.
     * Not in use anymore expect for debugging, replaced by stages.
     */
    public void drawTextAndSprites(){
        game.batch.begin();
        game.font.draw(game.batch, "Score :" + game.score, 750, 170);
        game.font.draw(game.batch, "Streak :" + game.streak, 750, 190);
        game.font.draw(game.batch, "Multiplier " + game.multiplier + "X", 750, 210);
        game.font.draw(game.batch, "X: " + Gdx.input.getAccelerometerX(), 750, 230);
        game.font.draw(game.batch, "Y: " + Gdx.input.getAccelerometerY(), 750, 250);
        game.font.draw(game.batch, "Z: " + Gdx.input.getAccelerometerZ(), 750, 270);
        game.font.draw(game.batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 750, 290);


        game.batch.end();
    }
}
