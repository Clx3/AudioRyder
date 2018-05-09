package com.mygdx.audioryder.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.mygdx.audioryder.AudioRyder;
import com.mygdx.audioryder.preferences.UserSettings;
import com.mygdx.audioryder.screens.GameScreen;

/**
 * Players game object, which is a spaceship. This class
 * handles the movement and drawing of the spaceship, player
 * input, collision detection and animation.
 *
 * @author Teemu Salminen
 * @author Joonas Salojärvi
 */

public class SpaceShip extends GameObject {

    /**
     * Player model. Spaceship in the final game.
     */
    ModelInstance spaceShipModel;

    /**
     * Bounding box of the player model, which is used for collision detection.
     */
    public BoundingBox collisionBox;

    /**
     * Used for creating collision box
     */
    public Vector3 minPointBox;
    /**
     * Used for creating collision box
     */
    public Vector3 maxPointBox;

    /**
     * Handles player models animations. Default: floating spaceship
     */
    AnimationController controller;

    public SpaceShip(AudioRyder game, Model model){
        super(game);

        setX(0f);
        setY(0.5f);
        setZ(0f);
        spaceShipModel = new ModelInstance(model, getX(),getY(),getZ());
        spaceShipModel.transform.rotate(1, 0, 0, -90);

        collisionBox = new BoundingBox();
        spaceShipModel.calculateBoundingBox(collisionBox);

        System.out.println(collisionBox.getDepth() + "DEP");
        System.out.println(collisionBox.getHeight() + "H");
        System.out.println(collisionBox.getWidth() + "W");


        /*FIXME: HARDCODE TEST*/
        minPointBox = new Vector3(getX() - collisionBox.getWidth() / 2, getY(), getZ() + collisionBox.getHeight() / 2);
        maxPointBox = new Vector3(getX() + collisionBox.getWidth() / 2, getY() + collisionBox.getDepth(), getZ() - collisionBox.getHeight() / 2);

        collisionBox.set(minPointBox, maxPointBox);

        controller = new AnimationController(spaceShipModel);
        controller.setAnimation("Float", -1);

    }

    /**
     * Handles all input from the player and then movement and rendering of the object.
     *
     * @param modelBatch This ModelBatch will be the one in the GameScreen.java.
     * @param environment This environment is the one in the GameScreen.java.
     */
    @Override
    public void renderAndUpdate(ModelBatch modelBatch, Environment environment) {
        accelMovement();
        //rollingAverageMovement();
        keyboardInput();

        minPointBox.set(getX() - collisionBox.getWidth() / 2, getY(), getZ() + collisionBox.getHeight() / 2);
        maxPointBox.set(getX() + collisionBox.getWidth() / 2, getY() + collisionBox.getDepth(), getZ() - collisionBox.getHeight() / 2);
        /*minPointBox.x = getX() - 1f;
        minPointBox.y = getY() - 1f;
        maxPointBox.x = getX() + 1f;
        maxPointBox.y = getY() + 1f;*/
        collisionBox.set(minPointBox, maxPointBox);


        spaceShipModel.transform.setToTranslation(getX(),getY(),getZ());
        checkCollisions();

        controller.update(Gdx.graphics.getDeltaTime());
        modelBatch.render(spaceShipModel, environment);
    }

    /**
     * Handles movement when using a keyboard to play. Mainly for debugging on computer.
     */
    private void keyboardInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            setX(getX() + (Gdx.graphics.getDeltaTime() * 25f));

        } else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {

            setX(getX() - (Gdx.graphics.getDeltaTime() * 25f));

        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            setY(getY() + (Gdx.graphics.getDeltaTime() * 25f));

        } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {

            setY(getY() - (Gdx.graphics.getDeltaTime() * 25f));

        }
    }

    /**
     * Checks if the player's model collides with any of the notes/pyramids in the game.
     */
    public void checkCollisions(){
        for(Note note: game.gameScreen.notes) {
            if(collisionBox.intersects(note.collisionBox) && note.isActive()) {
                note.setActive(false);
                //game.assets.get(game.SOUNDS_PATH + "notehit.wav", Sound.class).play();
                game.gameScreen.notesToRemove.add(note);
                System.out.println("HITS");
                game.score++;
                game.hitOrMiss = true;
                game.hitOrMissTimer = 0f;
                game.streak++;
            }
        }
    }

    public void dispose() {

    }

    public ModelInstance getSpaceShipModel() {
        return spaceShipModel;
    }

    public void setSpaceShipModel(ModelInstance spaceShipModel) {
        this.spaceShipModel = spaceShipModel;
    }

    /**
     * Old style of movement. Replaced with a better movement style, but this is preserved for
     * possible later use.
     */
    public void rollingAverageMovement(){
        int rollingAverageCount;
        int moveAverageIndex;
        float[][] latestMovement;
        //this should be in the constructor if used
        rollingAverageCount = 20;
        moveAverageIndex = 0;
        latestMovement = new float[2][rollingAverageCount];
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < rollingAverageCount; j++){
                latestMovement[i][j] = 0f;
            }
        }

        float accelX = 0f;
        float accelY = 0f;
        if(Gdx.input.getAccelerometerY() - UserSettings.xCalib > 4f / UserSettings.sensitivityRight){
            accelX = 4.5f;
        } else if (Gdx.input.getAccelerometerY() - UserSettings.xCalib < -4f / UserSettings.sensitivityLeft) {
            accelX = -4.5f;
        } else if (Gdx.input.getAccelerometerY() - UserSettings.xCalib < 2f / UserSettings.sensitivityRight && Gdx.input.getAccelerometerY() - UserSettings.xCalib > -2f / UserSettings.sensitivityLeft) {
            accelX = -0f;
        }

        if(Gdx.input.getAccelerometerZ() - UserSettings.yCalib > 4f / (UserSettings.sensitivityDown)){
            accelY = 0f;
        } else if (Gdx.input.getAccelerometerZ() - UserSettings.yCalib < -4f / (UserSettings.sensitivityUp)) {
            accelY = 2f;
        } else if (Gdx.input.getAccelerometerZ() - UserSettings.yCalib < 4f / (UserSettings.sensitivityDown) && Gdx.input.getAccelerometerZ() - UserSettings.yCalib > -4f / (UserSettings.sensitivityUp)) {
            accelY = 1f;
        }

        latestMovement[0][moveAverageIndex] = accelX;
        latestMovement[1][moveAverageIndex] = accelY;
        if(moveAverageIndex < rollingAverageCount - 1) {
            moveAverageIndex++;
        } else {
            moveAverageIndex = 0;
        }
        float totalX = 0f;
        float totalY = 0f;

        //FIXME: Y AKSELI PÄIN VITTUA!!

        for(int i = 0; i < rollingAverageCount; i++){
            totalX = totalX + latestMovement[0][i];
            totalY = (totalY + latestMovement[1][i]);
        }
        totalY = totalY / rollingAverageCount;
        totalX = totalX / rollingAverageCount;

        setX(totalX);
        setY(totalY);
    }

    /**
     * Handles movement of the player model. Uses accelerometer
     * values to determine speed of the spaceship.
     */
    public void accelMovement(){

        if(Gdx.input.getAccelerometerY() - UserSettings.xCalib > 1f && getX() < 4.5f){
            setX(getX() + (2f * Gdx.graphics.getDeltaTime() * (UserSettings.sensitivityRight / 1.5f) *(Gdx.input.getAccelerometerY() - UserSettings.xCalib)));
        } else if (Gdx.input.getAccelerometerY() - UserSettings.xCalib <  1f && getX() > -4.5f) {
            setX(getX() - (2f * Gdx.graphics.getDeltaTime() * (UserSettings.sensitivityLeft / 1.5f) *-(Gdx.input.getAccelerometerY() - UserSettings.xCalib)));
        }

        if(Gdx.input.getAccelerometerZ() - UserSettings.yCalib >  1f && getY() > -0.5f){
            setY(getY() - (1f * Gdx.graphics.getDeltaTime() * (UserSettings.sensitivityDown / 1.5f) * (Gdx.input.getAccelerometerZ() - UserSettings.yCalib)));
        } else if (Gdx.input.getAccelerometerZ() - UserSettings.yCalib < 1f && getY() < 3f) {
            setY(getY() + (1f * Gdx.graphics.getDeltaTime() * (UserSettings.sensitivityUp / 1.5f) * -(Gdx.input.getAccelerometerZ() - UserSettings.yCalib)));
        }

        if(getY() < -0.5f){
            setY(-0.5f);
        } else if(getY() > 3f){
            setY(3f);
        }
        if(getX() < -4.5f){
            setX(-4.5f);
        } else if(getX() > 4.5f){
            setX(4.5f);
        }

    }
}
