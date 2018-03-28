package com.mygdx.audioryder.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * Created by Joonas on 28.3.2018.
 */

public class Skydome{

    ModelInstance model;
    float y;
    float x;
    float speed;
    public Skydome(Model model) {
        this.model = new ModelInstance(model,0f,0f,0f);
        this.model.transform.setToTranslationAndScaling(0f,0f,0f,0.9f,0.9f,0.9f);
        this.model.transform.rotate(1f,-1f,0f,-50f);
        this.model.calculateTransforms();
    }

    public void render(ModelBatch modelBatch, Environment environment){
        model.transform.rotate(1f,-1f,0f,0.7f * Gdx.graphics.getDeltaTime());
        model.calculateTransforms();
        modelBatch.render(model,environment);
    }
    public void move3d() {
        model.calculateTransforms();

    }
}
