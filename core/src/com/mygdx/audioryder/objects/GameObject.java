package com.mygdx.audioryder.objects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.mygdx.audioryder.AudioRyder;

/**
 * Created by Teemu on 1.3.2018.
 */


/**
 * This class is very simple. It contains only few
 * variables and methods. It is our GameObject class
 * and all of our rendered objects in our game
 * will extend this class.
 *
 * @author Teemu Salminen
 * @author Joonas Salojärvi
 */
public abstract class GameObject {

    /** Instance of the game. */
    public AudioRyder game;

    /**
     * This boolean checks if the object is active.
     * If false, the object will not be rendered and will be
     * removed gameObjects ArrayList.
     */
    private boolean isActive;

    /* X, Y, Z coordinates */
    private float x, y, z;

    public GameObject(AudioRyder game) {
        this.game = game;
        setActive(true);
        game.gameScreen.gameObjects.add(this);
    }

    /**
     * This method will be called on all of the GameObjects
     * in the main game loop which is located in GameScreen.java.
     * This methods purpose is to handle the rendering and updating
     * of the GameObject.
     *
     * @param modelBatch This ModelBatch will be the one in the GameScreen.java.
     * @param environment This environment is the one in the GameScreen.java.
     */
    public abstract void renderAndUpdate(ModelBatch modelBatch, Environment environment);

    /**
     * This method will set the position of the GameObject.
     *
     * @param x X position of the object.
     * @param y Y position of the object.
     * @param z Z position of the object.
     */
    public void setPosition(float x, float y, float z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    /* Getters & setters: */

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
