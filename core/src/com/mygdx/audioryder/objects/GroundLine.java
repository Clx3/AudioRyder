package com.mygdx.audioryder.objects;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * Created by Joonas on 25.2.2018.
 */

public class GroundLine extends GameObject {

    ModelInstance model;

    public GroundLine(float i, Model model) {
        this.model = new ModelInstance(model,i,-0.4f,-25f);
        setX(i);
    }

    public void render(ModelBatch modelBatch, Environment environment){
        modelBatch.render(model, environment);
    }
}
