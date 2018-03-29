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

    public SpaceShip(AudioRyder game, Model model, float sensitivity){
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

    public void draw3d(ModelBatch modelBatch, Environment environment){
        controller.update(Gdx.graphics.getDeltaTime());
        modelBatch.render(spaceShipModel, environment);
    }

    public void move() {
        keyboardInput();

        float accelX;
        if(Gdx.input.getAccelerometerY() > 4f / sensitivity){
            accelX = 4f;
        } else if (Gdx.input.getAccelerometerY() < -4f / sensitivity) {
            accelX = -4f;
        } else {
            accelX = Gdx.input.getAccelerometerY() * sensitivity;
        }

        latestMovement[0][moveAverageIndex] = accelX;
        latestMovement[1][moveAverageIndex] = Gdx.input.getAccelerometerZ() / 4f;
        if(moveAverageIndex < rollingAverageCount - 1) {
            moveAverageIndex++;
        } else {
            moveAverageIndex = 0;
        }
        float totalX = 0f;
        float totalY = 0f;
        for(int i = 0; i < rollingAverageCount; i++){
            totalX = totalX + latestMovement[0][i];
            totalY = totalY + latestMovement[1][i];
        }
        totalY = totalY / rollingAverageCount;
        totalX = totalX / rollingAverageCount;

        setX(totalX);
        setY(-0.5f);

        minPointBox.x = getX() - 1f;
        maxPointBox.x = getX() + 1f;
        collisionBox.set(minPointBox, maxPointBox);

        spaceShipModel.transform.setToTranslation(getX(),getY(),getZ());
        checkCollisions();


    }

    private void keyboardInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (getX() <= 4f) {
                setX(getX() + speed * Gdx.graphics.getDeltaTime());
                minPointBox.x += speed * Gdx.graphics.getDeltaTime();
                maxPointBox.x += speed * Gdx.graphics.getDeltaTime();
                //setX(getX() + 3f);
                collisionBox.set(minPointBox, maxPointBox);
        }
        } else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if(getX() >= -4f) {
                minPointBox.x -= speed * Gdx.graphics.getDeltaTime();
                maxPointBox.x -= speed * Gdx.graphics.getDeltaTime();
                setX(getX() - speed * Gdx.graphics.getDeltaTime());
                collisionBox.set(minPointBox, maxPointBox);
            }
        }
    }

    public void checkCollisions(){
        for(Note note: game.gameScreen.notes) {
            if(collisionBox.intersects(note.collisionBox)) {
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
