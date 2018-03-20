package com.mygdx.audioryder.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Joonas on 15.2.2018.
 */

public class Note extends GameObject {

    ModelInstance model;

    private int direction;

    float speed;
    AnimationController controller;

    public Note(float i, int i2, Model model, float speed) {
        direction = i2;
        this.model = new ModelInstance(model,i,0f,-54f);
        setX(i);
        setY(-50f);
        this.speed = speed;
        controller = new AnimationController(this.model);
        controller.setAnimation("Bend", -1);

    }

    public void render(ModelBatch modelBatch, Environment environment){
        controller.update(Gdx.graphics.getDeltaTime());
        modelBatch.render(model,environment);
    }

    public void move3d() {
        setY(getY() + Gdx.graphics.getDeltaTime() * 10f * speed);
        model.transform.setToTranslation(getX(),-1.0f, getY());
        model.calculateTransforms();

    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
