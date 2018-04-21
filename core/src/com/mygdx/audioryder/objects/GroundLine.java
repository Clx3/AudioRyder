package com.mygdx.audioryder.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.mygdx.audioryder.AudioRyder;

/**
 * Created by Joonas on 25.2.2018.
 */

public class GroundLine extends GameObject {

    ModelInstance model;
    float speed;

    public GroundLine(AudioRyder game, Model model, float x, float y, float z, float speed) {
        super(game);
        setPosition(x, y, z);
        this.model = new ModelInstance(model, getX(),getY(),getZ());
        this.speed = speed;
    }

    @Override
    public void renderAndUpdate(ModelBatch modelBatch, Environment environment) {
        modelBatch.render(model,environment);

        setZ(getZ() + Gdx.graphics.getDeltaTime() * 10f * speed);
        model.transform.setToTranslationAndScaling(getX(),getY(), getZ(),1.6f,1f,1f);
        model.calculateTransforms();
    }

}
