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
        controller.setAnimation("Bend", -1);
    }

    public void draw3d(ModelBatch modelBatch, Environment environment){
        controller.update(Gdx.graphics.getDeltaTime());
        modelBatch.render(spaceShipModel, environment);
    }

    public void move() {
        keyboardInput();

        spaceShipModel.transform.setToTranslationAndScaling(getX(),-0.5f,0f,-1f,1f,-1f);
        spaceShipModel.calculateTransforms();
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
