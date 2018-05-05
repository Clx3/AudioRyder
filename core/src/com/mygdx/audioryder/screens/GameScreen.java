package com.mygdx.audioryder.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.audioryder.AudioRyder;
import com.mygdx.audioryder.objects.BackgroundObject;
import com.mygdx.audioryder.objects.GameObject;
import com.mygdx.audioryder.objects.GroundLine;
import com.mygdx.audioryder.objects.Note;
import com.mygdx.audioryder.objects.BackgroundObject;
import com.mygdx.audioryder.objects.Skydome;
import com.mygdx.audioryder.objects.SpaceShip;
import com.mygdx.audioryder.properties.Properties;
import com.mygdx.audioryder.song.SongHandler;

import java.util.ArrayList;


/**
 * Created by Teemu on 1.3.2018.
 */

public class GameScreen implements Screen {

    AudioRyder game;

    public ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
    public ArrayList<GameObject> gameObjectsToRemove = new ArrayList<GameObject>();

    public ArrayList<Note> notes = new ArrayList<Note>();
    public ArrayList<Note> notesToRemove = new ArrayList<Note>();

    public ArrayList<GroundLine> groundLines = new ArrayList<GroundLine>();

    PerspectiveCamera cam3D;

    ModelBatch modelBatch;

    BitmapFont font;

    /**
     * This Array of Model's contains all of the pyramid
     * models used as our notes in the game.
     * 0 = green, 1 = yellow and 2 = red.
     */
    public Model[] pyramids = new Model[3];

    Texture hit;
    Texture miss;

    Music currentSong;

    float levelTimer = 0f;
    boolean GAME_PAUSED;

    Stage pauseStage;
    Stage gameOverlay;
    Viewport viewport;
    Skin testSkin;

    TextButton continueGame;
    TextButton settings;
    TextButton restart;
    TextButton exit;
    Table pauseMenuTable;
    Label score;

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
            //game.cam2D.setToOrtho(false, 10f,6f);

            modelBatch = new ModelBatch();

            font = game.font;

            Model tempModel;

            tempModel = game.assets.get(AudioRyder.MODELS_PATH + "Spaceship.g3db");
            game.spaceShip = new SpaceShip(game, tempModel);

            tempModel = game.assets.get(AudioRyder.MODELS_PATH + "Pyramid_green.g3db");
            pyramids[0] = (tempModel);

            tempModel = game.assets.get(AudioRyder.MODELS_PATH + "Pyramid_yellow.g3db");
            pyramids[1] = (tempModel);

            tempModel = game.assets.get(AudioRyder.MODELS_PATH + "Pyramid_red.g3db");
            pyramids[2] = (tempModel);

            tempModel = game.assets.get(AudioRyder.MODELS_PATH + "TrackRE.g3db", Model.class);
            game.levelModel = (tempModel);

            tempModel = game.assets.get(AudioRyder.MODELS_PATH + "Skydome_WIP.g3db", Model.class);
            game.skyModel = (tempModel);
            game.skydome = new Skydome(game, game.skyModel);

            for(int i = 0; i < MathUtils.random(15, 30); i++) {
                int planetType = MathUtils.random(1, 3);
                gameObjects.add(new BackgroundObject(game, game.assets.get(AudioRyder.MODELS_PATH + "Planet" + planetType + ".g3db", Model.class)));
            }

            tempModel = game.assets.get(AudioRyder.MODELS_PATH + "Asteroid.g3db", Model.class);
            for(int i = 0; i < MathUtils.random(15, 30); i++) {
                gameObjects.add(new BackgroundObject(game, tempModel));
            }

            groundLines.add(new GroundLine(game, game.levelModel, 0, -2f, 2f, game.noteSpeed * 2.5f));


            //Using the songhandler now, this will become usefull when we add multiple levels and
            //a loading screen from main menu to game.
            SongHandler.setupSong(game, game.currentSong);
            SongHandler.currentSong.play();

            hit = new Texture("hit.png");
            miss = new Texture("miss.png");

            viewport = new FitViewport(game.ORTHOCAM_VIEWPORT_WIDTH, game.ORTHOCAM_VIEWPORT_HEIGHT, game.cam2D);
            viewport.apply();

            testSkin = game.mainMenuScreen.testSkin;

            setupPauseStage();
            setupGameOverlay();
            GAME_PAUSED = false;
            game.GAME_IS_ON = true;
        }


    }

    private void setupGameOverlay() {
        gameOverlay = new Stage(viewport, game.batch);

        final TextButton pauseButton = new TextButton(Properties.pauseText,testSkin);
        pauseButton.setSize(150f,50f);
        pauseButton.setPosition(game.ORTHOCAM_VIEWPORT_WIDTH - pauseButton.getWidth() - 10f,game.ORTHOCAM_VIEWPORT_HEIGHT - pauseButton.getHeight() - 10f);
        pauseButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GAME_PAUSED = true;
                pauseButton.setChecked(false);
                SongHandler.currentSong.pause();
            }
        });
        gameOverlay.addActor(pauseButton);
        score = new Label(Properties.scoreText + "\n" + game.score,testSkin,"nasalization",Color.WHITE);
        score.setAlignment(1);
        score.setPosition((game.ORTHOCAM_VIEWPORT_WIDTH / 2f) - (score.getWidth() / 2),(game.ORTHOCAM_VIEWPORT_HEIGHT) - (score.getHeight()) - 10f);
        gameOverlay.addActor(score);
        Gdx.input.setInputProcessor(gameOverlay);
    }

    private void setupPauseStage() {

        pauseStage = new Stage(viewport, game.batch);


        pauseMenuTable = new Table();
        pauseMenuTable.setFillParent(true);


        continueGame = new TextButton(Properties.continueText,testSkin);
        continueGame.setSize(300f,80f);
        settings = new TextButton(Properties.settingsText,testSkin);
        settings.setSize(300f,80f);
        restart = new TextButton(Properties.restartText,testSkin);
        restart.setSize(300f,80f);
        if(Properties.currentLocale == Properties.localeFI) {
            restart.getLabel().setFontScale(0.9f,0.9f);
        }
        exit = new TextButton(Properties.exitText,testSkin);
        exit.setSize(300f,80f);

        continueGame.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                GAME_PAUSED = false;
                Gdx.input.setInputProcessor(gameOverlay);
                continueGame.setChecked(false);
                SongHandler.currentSong.play();
            }
        });

        settings.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settings.setChecked(false);
                SongHandler.currentSong.pause();
                game.setScreen(game.mainMenuScreen);
                game.mainMenuScreen.currentStage = game.mainMenuScreen.settingsStage;
                Gdx.input.setInputProcessor(game.mainMenuScreen.currentStage);
            }
        });

        restart.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for(Note note : notes){
                    note.setActive(false);
                    notesToRemove.add(note);
                    gameObjectsToRemove.add(note);
                }
                gameObjects.removeAll(gameObjectsToRemove);
                gameObjectsToRemove.clear();
                notes.removeAll(notesToRemove);
                notesToRemove.clear();
                game.songTimer = 0f;
                SongHandler.setupSong(game,game.currentSong);
                Gdx.input.setInputProcessor(gameOverlay);
                SongHandler.currentSong.play();
                GAME_PAUSED = false;
                restart.setChecked(false);
            }
        });

        exit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.score = 0;
                game.setScreen(game.mainMenuScreen);
                exit.setChecked(false);
                game.GAME_IS_ON = false;
            }
        });


        pauseMenuTable.add(continueGame).size(continueGame.getWidth(),continueGame.getHeight()).pad(15f);
        pauseMenuTable.row();
        pauseMenuTable.add(settings).size(settings.getWidth(),settings.getHeight()).pad(15f);
        pauseMenuTable.row();
        pauseMenuTable.add(restart).size(restart.getWidth(),restart.getHeight()).pad(15f);
        pauseMenuTable.row();
        pauseMenuTable.add(exit).size(exit.getWidth(),exit.getHeight()).pad(15f);

        pauseStage.addActor(pauseMenuTable);

    }

    @Override
    public void render(float delta) {
        //FIXME: testaus printtausta :--D moro teme :--D :---D homo joonas vittu :---D
        System.out.println(game.xCalib + "    " + game.yCalib);

        if(!(GAME_PAUSED)) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

            SongHandler.addNotesToGame(game);

            cam3D.position.set(game.spaceShip.getX(), 2f + game.spaceShip.getY(), 6f);
            cam3D.lookAt(game.spaceShip.getX(), game.spaceShip.getY() + 1f, game.spaceShip.getZ());
            cam3D.update();

            modelBatch.begin(cam3D);
            for (GameObject object : gameObjects) {
                if (object.isActive())
                    object.renderAndUpdate(modelBatch, game.environment);
                else
                    gameObjectsToRemove.add(object);
            }
            modelBatch.end();
            levelTimer += Gdx.graphics.getDeltaTime();


            /* Spawning and removing the groundlines: */
            if (groundLines.get(groundLines.size() - 1).getZ() > -200f) {
                groundLines.add(new GroundLine(game, game.levelModel, 0, -2f, (groundLines.get(groundLines.size() - 1).getZ()) - 17.9f, game.noteSpeed * 2.5f));
            }
            if (groundLines.get(0).getZ() > 30) {
                groundLines.get(0).setActive(false);
                groundLines.remove(0);
            }

            drawTextAndSprites();

            gameObjects.removeAll(gameObjectsToRemove);
            gameObjectsToRemove.clear();


            notes.removeAll(notesToRemove);
            notesToRemove.clear();

            //Overlay:
            score.setText(Properties.scoreText + "\n" + game.score);
            gameOverlay.act();
            gameOverlay.draw();


            if (!(SongHandler.currentSong.isPlaying())) {
                game.GAME_IS_ON = false;
                dispose();
                SongHandler.currentSong.stop();
                game.endScreen = new EndScreen(game,game.score, game.currentSong);
                game.setScreen(game.endScreen);
            }
        }
        if(GAME_PAUSED){
            Gdx.input.setInputProcessor(pauseStage);
            pause();
        }

        /*
        //FIXME: nopee hätänen testausosio ending-screenille
        if(levelTimer > 2f){
            game.GAME_IS_ON = false;
            dispose();
            SongHandler.currentSong.stop();
            game.endScreen = new EndScreen(game,1234567890, game.currentSong);
            game.setScreen(game.endScreen);
        }
        */
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        Gdx.gl.glClearColor( 0, 0, 0, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
        pauseStage.act(Gdx.graphics.getDeltaTime());
        pauseStage.draw();
    }

    @Override
    public void resume() {
        GAME_PAUSED = true;
    }

    @Override
    public void hide() {


    }

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
        /*modelBatch.dispose();
        hit.dispose();
        miss.dispose();
        currentSong.dispose();
        shapeRenderer.dispose();*/
        levelTimer = 0f;
        game.songTimer = 0f;
    }

    public void drawTextAndSprites(){
        game.batch.begin();

        /*game.cam2D.setToOrtho(false,10f,6f);
        game.batch.setProjectionMatrix(game.cam2D.combined);
        game.cam2D.update();*/
        game.hitOrMissTimer += Gdx.graphics.getDeltaTime();
        if (game.hitOrMiss && game.score > 0 && game.hitOrMissTimer < 0.5f) {
            game.batch.draw(hit, 1.5f, 3f, 1f, 0.5f);
        } else if (!(game.hitOrMiss) && game.hitOrMissTimer < 0.5f) {
            game.batch.draw(miss, 1.5f, 3f, 1f, 0.5f);
        }

        /*game.cam2D.setToOrtho(false,game.ORTHOCAM_VIEWPORT_WIDTH,game.ORTHOCAM_VIEWPORT_HEIGHT);
        game.batch.setProjectionMatrix(game.cam2D.combined);
        game.cam2D.update();*/
        font.draw(game.batch, "Score :" + game.score, 750, 170);
        font.draw(game.batch, "Streak :" + game.streak, 750, 190);
        font.draw(game.batch, "Multiplier " + game.multiplier + "X", 750, 210);
        font.draw(game.batch, "X: " + Gdx.input.getAccelerometerX(), 750, 230);
        font.draw(game.batch, "Y: " + Gdx.input.getAccelerometerY(), 750, 250);
        font.draw(game.batch, "Z: " + Gdx.input.getAccelerometerZ(), 750, 270);
        font.draw(game.batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 750, 290);


        game.batch.end();
    }
}
