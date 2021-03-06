package com.mygdx.audioryder.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.mygdx.audioryder.AudioRyder;

/**
 * This class is the background object of the game,
 * the skydome / skybox. It's a huge model that
 * rotates in the background. This class handles creating
 * and positioning of the skydome and rendering and rotating.
 *
 * @version 2018.0509
 * @author Teemu Salminen
 * @author Joonas Salojärvi
 */

public class Skydome extends GameObject {

    /**
     * Used model. ModelInstance is created in the constructor from given model.
     */
    ModelInstance model;

    /**
     * @param game Instance of the game.
     * @param model The model that should be used as background (dome)
     */
    public Skydome(AudioRyder game, Model model) {
        super(game);
        setPosition(0f, 0f, 200f);
        this.model = new ModelInstance(model, getX(), getY(), getZ());
        this.model.transform.setToTranslationAndScaling(getX(),getY(),getZ(),0.9f,0.9f,0.9f);
        this.model.transform.rotate(1f,-1f,0f,-50f);
        this.model.calculateTransforms();
    }

    /**
     * Rotates and renders the skydome.
     * @param modelBatch This ModelBatch will be the one in the GameScreen.java.
     * @param environment This environment is the one in the GameScreen.java.
     */
    @Override
    public void renderAndUpdate(ModelBatch modelBatch, Environment environment) {
        model.transform.rotate(1f,-1f,0f,0.7f * Gdx.graphics.getDeltaTime());
        model.calculateTransforms();
        modelBatch.render(model,environment);
    }
}
