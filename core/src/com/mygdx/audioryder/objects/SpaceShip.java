package com.mygdx.audioryder.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Joonas on 22.2.2018.
 */

public class SpaceShip extends GameObject {

    ModelInstance spaceShipModel;

    Texture img;
    int moveAverageIndex;
    float[][] latestMovement;
    float sensitivity;
    AnimationController controller;

    public SpaceShip(Model model, float sensitivity){
        moveAverageIndex = 0;
        latestMovement = new float[2][15];
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 15; j++){
                latestMovement[i][j] = 0f;
            }
        }
        setX(0f);
        setY(0f);
        spaceShipModel = new ModelInstance(model, 0f,0f,0f);
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
        if(Gdx.input.getAccelerometerY() > 5f / sensitivity){
            accelX = 5f;
        } else if (Gdx.input.getAccelerometerY() < -5f / sensitivity) {
            accelX = -5f;
        } else {
            accelX = Gdx.input.getAccelerometerY() * sensitivity;
        }

        latestMovement[0][moveAverageIndex] = accelX;
        latestMovement[1][moveAverageIndex] = Gdx.input.getAccelerometerZ() / 4f;
        if(moveAverageIndex < 9) {
            moveAverageIndex++;
        } else {
            moveAverageIndex = 0;
        }
        float totalX = 0f;
        float totalY = 0f;
        for(int i = 0; i < 15; i++){
            totalX = totalX + latestMovement[0][i];
            totalY = totalY + latestMovement[1][i];
        }
        totalY = totalY / 15f;
        totalX = totalX / 15f;


        spaceShipModel.transform.setToTranslation(getX(),-0.5f,0f);
        spaceShipModel.calculateTransforms();

        setX(totalX);
        setY(totalY);
    }

    private void keyboardInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if(getX() != 3f)
                setX(getX() + 3f);
        } else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if(getX() != -3f)
                setX(getX() - 3f);
        }
    }

    public ModelInstance getSpaceShipModel() {
        return spaceShipModel;
    }

    public void setSpaceShipModel(ModelInstance spaceShipModel) {
        this.spaceShipModel = spaceShipModel;
    }

}
