package com.mygdx.audioryder.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.mygdx.audioryder.AudioRyder;

/**
 * This class handles the moving ground level.
 * The ground level of the game consists of many of these.
 * Moving and rendering of ground is handled in this class.
 * @version 2018.0509
 * @author Teemu Salminen
 * @author Joonas Salojärvi
 */

public class GroundLine extends GameObject {

    /**
     * ModelInstance of the level. Set in the constructor from the given model.
     */
    ModelInstance model;

    /**
     * Variable for how fast the ground is moving towards the player.
     */
    float speed;

    public GroundLine(AudioRyder game, Model model, float x, float y, float z, float speed) {
        super(game);
        setPosition(x, y, z);
        this.model = new ModelInstance(model, getX(),getY(),getZ());
        this.speed = speed;
    }

    /**
     * Handles moving and rendering the level block.
     *
     * @param modelBatch This ModelBatch will be the one in the GameScreen.java.
     * @param environment This environment is the one in the GameScreen.java.
     */
    @Override
    public void renderAndUpdate(ModelBatch modelBatch, Environment environment) {
        modelBatch.render(model,environment);

        setZ(getZ() + Gdx.graphics.getDeltaTime() * 15f * speed);
        model.transform.setToTranslationAndScaling(getX(),getY(), getZ(),1.6f,1f,1f);
        model.calculateTransforms();
    }

}
