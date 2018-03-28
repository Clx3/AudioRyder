package com.mygdx.audioryder.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * Created by Joonas on 25.2.2018.
 */

public class GroundLine {
    ModelInstance model;
    public float y;
    public float x;
    float speed;
    public GroundLine(float i, Model model, float speed) {
        this.model = new ModelInstance(model,i,-2f,-25f);
        x = 0;
        y = i;
        this.speed = speed;
    }
    public void render(ModelBatch modelBatch, Environment environment){
        modelBatch.render(model,environment);
    }
    public void move3d() {
        y += Gdx.graphics.getDeltaTime() * 10f * speed;
        model.transform.setToTranslationAndScaling(x,-2f,y,1.6f,1f,1f);
        model.calculateTransforms();

    }
}
