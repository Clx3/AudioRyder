package com.mygdx.audioryder.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
        setY(0f);
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
        float accelX = 0f;
        float accelY = 0f;
        if(Gdx.input.getAccelerometerY() - game.xCalib > 4f / game.sensitivityRight){
            accelX = 4f;
        } else if (Gdx.input.getAccelerometerY() - game.xCalib < -4f / game.sensitivityLeft) {
            accelX = -4f;
        } else if (Gdx.input.getAccelerometerY() - game.xCalib < 0.8f / game.sensitivityRight && Gdx.input.getAccelerometerY() - game.xCalib > -0.8f / game.sensitivityLeft) {
            accelX = -0f;
        } else if (Gdx.input.getAccelerometerY() - game.xCalib > 0.8f / game.sensitivityRight) {
            accelX = Gdx.input.getAccelerometerY() * game.sensitivityRight;
        } else if (Gdx.input.getAccelerometerY() - game.xCalib < -0.8f / game.sensitivityLeft){
            accelX = Gdx.input.getAccelerometerY() * game.sensitivityLeft;
        }

        if(Gdx.input.getAccelerometerZ() - game.yCalib > 2f / (game.sensitivityDown)){
            accelY = -2f;
        } else if (Gdx.input.getAccelerometerZ() - game.yCalib < -2f / (game.sensitivityUp)) {
            accelY = 0f;
        } else if (Gdx.input.getAccelerometerZ() - game.yCalib < 2f / (game.sensitivityDown) && Gdx.input.getAccelerometerZ() - game.yCalib > -2f / (game.sensitivityUp)) {
            accelY = -1f;
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
            totalY = (totalY + latestMovement[1][i] + 2f);
        }
        totalY = totalY / rollingAverageCount;
        totalX = totalX / rollingAverageCount;

        setX(totalX);
        setY(totalY);

        keyboardInput();
        minPointBox.x = getX() - 1f;
        minPointBox.y = getY() - 1f;
        maxPointBox.x = getX() + 1f;
        maxPointBox.y = getY() + 1f;
        collisionBox.set(minPointBox, maxPointBox);


        spaceShipModel.transform.setToTranslation(getX(),getY(),getZ());


        checkCollisions();

        controller.update(Gdx.graphics.getDeltaTime());
        modelBatch.render(spaceShipModel, environment);
    }

    private void keyboardInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            setX(4);

        } else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {

            setX(-4);

        }
    }

    private void jump() {

    }

    public void checkCollisions(){
        for(Note note: game.gameScreen.notes) {
            if(collisionBox.intersects(note.collisionBox) && note.isActive()) {
                note.setActive(false);
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

}
