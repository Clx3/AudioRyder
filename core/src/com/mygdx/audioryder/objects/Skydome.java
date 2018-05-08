package com.mygdx.audioryder.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.mygdx.audioryder.AudioRyder;

/**
 * Created by Joonas on 28.3.2018.
 */

public class Skydome extends GameObject {

    ModelInstance model;
    float speed;

    public Skydome(AudioRyder game, Model model) {
        super(game);
        setPosition(0f, 0f, 100f);
        this.model = new ModelInstance(model, getX(), getY(), getZ());
        this.model.transform.setToTranslationAndScaling(getX(),getY(),getZ(),0.9f,0.9f,0.9f);
        this.model.transform.rotate(1f,-1f,0f,-50f);
        this.model.calculateTransforms();
    }

    @Override
    public void renderAndUpdate(ModelBatch modelBatch, Environment environment) {
        model.transform.rotate(1f,-1f,0f,0.7f * Gdx.graphics.getDeltaTime());
        model.calculateTransforms();
        modelBatch.render(model,environment);
    }
}
