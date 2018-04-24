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
import com.mygdx.audioryder.screens.GameScreen;

/**
 * Created by Joonas on 22.2.2018.
 */

public class SpaceShip extends GameObject {

    AudioRyder game;

    ModelInstance spaceShipModel;

    public BoundingBox collisionBox;
    public Vector3 minPointBox;
    public Vector3 maxPointBox;

    int rollingAverageCount;

    Texture img;
    int moveAverageIndex;
    float[][] latestMovement;
    float sensitivity;
    AnimationController controller;

    private float speed = 14f;
    private float velocityY = -5f;

    public SpaceShip(AudioRyder game, Model model){
        super(game);
        this.game = game;
        rollingAverageCount = 20;
        moveAverageIndex = 0;
        latestMovement = new float[2][rollingAverageCount];
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < rollingAverageCount; j++){
                latestMovement[i][j] = 0f;
            }
        }
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

        this.sensitivity = sensitivity;
        controller = new AnimationController(spaceShipModel);
        controller.setAnimation("Float", -1);


    }

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

    public void rollingAverageMovement(){
        float accelX = 0f;
        float accelY = 0f;
        if(Gdx.input.getAccelerometerY() - game.xCalib > 4f / game.sensitivityRight){
            accelX = 4.5f;
        } else if (Gdx.input.getAccelerometerY() - game.xCalib < -4f / game.sensitivityLeft) {
            accelX = -4.5f;
        } else if (Gdx.input.getAccelerometerY() - game.xCalib < 2f / game.sensitivityRight && Gdx.input.getAccelerometerY() - game.xCalib > -2f / game.sensitivityLeft) {
            accelX = -0f;
        }

        if(Gdx.input.getAccelerometerZ() - game.yCalib > 4f / (game.sensitivityDown)){
            accelY = 0f;
        } else if (Gdx.input.getAccelerometerZ() - game.yCalib < -4f / (game.sensitivityUp)) {
            accelY = 2f;
        } else if (Gdx.input.getAccelerometerZ() - game.yCalib < 4f / (game.sensitivityDown) && Gdx.input.getAccelerometerZ() - game.yCalib > -4f / (game.sensitivityUp)) {
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

    public void accelMovement(){
        //VANHA SHITTI
        /*if(Gdx.input.getAccelerometerY() - game.xCalib > 5f / game.sensitivityRight && getX() < 4f){
            setX(getX() + ((Gdx.input.getAccelerometerY() - game.xCalib) * (Gdx.graphics.getDeltaTime() * (game.sensitivityRight / 1f))));
        } else if (Gdx.input.getAccelerometerY() - game.xCalib <  -5f / game.sensitivityLeft && getX() > -4f) {
            setX(getX() + ((Gdx.input.getAccelerometerY() - game.xCalib) * (Gdx.graphics.getDeltaTime() * (game.sensitivityLeft / 1f))));
        }

        if(Gdx.input.getAccelerometerZ() - game.yCalib >  3f / game.sensitivityDown && getY() > 0f){
            setY(getY() - ((Gdx.input.getAccelerometerZ() - game.yCalib) * (Gdx.graphics.getDeltaTime() * (game.sensitivityDown / 2f))));
        } else if (Gdx.input.getAccelerometerZ() - game.yCalib < -3f / game.sensitivityUp && getY() < 4f) {
            setY(getY() - ((Gdx.input.getAccelerometerZ() - game.yCalib) * (Gdx.graphics.getDeltaTime() * (game.sensitivityUp / 2f))));
        }*/

        if(Gdx.input.getAccelerometerY() - game.xCalib > 5f - game.sensitivityRight && getX() < 4.5f){
            setX(getX() + (8f * Gdx.graphics.getDeltaTime()));
        } else if (Gdx.input.getAccelerometerY() - game.xCalib <  -5f + game.sensitivityLeft && getX() > -4.5f) {
            setX(getX() - (8f * Gdx.graphics.getDeltaTime()));
        }

        if(Gdx.input.getAccelerometerZ() - game.yCalib >  5f - game.sensitivityDown && getY() > -0.5f){
            setY(getY() - (4f * Gdx.graphics.getDeltaTime()));
        } else if (Gdx.input.getAccelerometerZ() - game.yCalib < - 5f + game.sensitivityUp && getY() < 4f) {
            setY(getY() + (4f * Gdx.graphics.getDeltaTime()));
        }
    }
}
