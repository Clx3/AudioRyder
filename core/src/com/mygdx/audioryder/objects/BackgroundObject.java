package com.mygdx.audioryder.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.audioryder.AudioRyder;

/**
 * This class is used for the objects in the background
 * of the game, for example asteroids, planets etc. that
 * just float around to create a feeling that you're
 * flying in space. Simple as that nothing too hardcore here.
 *
 * @version 2018.0509
 * @author Teemu Salminen
 * @author Joonas Salojärvi
 */
public class BackgroundObject extends GameObject {

    //TODO: Implement rotation for the objects.
    //TODO: (MAYBE) Tweak the values around to achieve more realistic look for the game.

    /** This is the ModelInstance of the object. */
    ModelInstance modelInstance;

    /** Speed of the object this is initialized in reset() method. */
    private float speed;
    private float rotationSpeed;

    /**
     * Creates a new Background object from a wanted Model.
     *
     * @param game Instance of the AudioRyder.
     * @param model This is the model that the object is going to use.
     */
    public BackgroundObject(AudioRyder game, Model model) {
        super(game);

        modelInstance = new ModelInstance(model);
        reset();

        //This is here to prevent all objects from spawning at the end of the map so it randomizes its spawning location.
        setZ(MathUtils.random(30, -150f));
        rotationSpeed = MathUtils.random(0.1f,20f);

        modelInstance.transform.setToTranslation(getX(), getY(), getZ());
        modelInstance.calculateTransforms();
    }

    /**
     * This method is used for resetting values(size, speed, position)
     * of this object. Basically when the object goes off screen, this
     * method will be called and this object will be "reborn".
     * This method is also used in the constructor for initializion.
     */
    private void reset() {
        float size = MathUtils.random(0.5f, 4f);
        modelInstance.nodes.get(0).scale.set(size, size, size);

        speed = MathUtils.random(7f, 20f);

        if(MathUtils.random(1, 2) == 1)
            setPosition(MathUtils.random(-15f, -70f), MathUtils.random(-40f, 40f), -150f);
        else
            setPosition(MathUtils.random(15f, 70f), MathUtils.random(-40f, 40f), -150f);
    }

    @Override
    public void renderAndUpdate(ModelBatch modelBatch, Environment environment) {
        setZ(getZ() + speed * Gdx.graphics.getDeltaTime());

        if(getZ() > 30)
            reset();

        modelBatch.render(modelInstance, environment);
        modelInstance.transform.setToTranslation(getX(),getY(), getZ());
    }
}
