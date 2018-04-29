package com.mygdx.audioryder.objects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.mygdx.audioryder.AudioRyder;

/**
 * Created by Teemu on 1.3.2018.
 */

public abstract class GameObject {

    public AudioRyder game;

    private boolean isActive;
    private float x, y, z;

    public GameObject(AudioRyder game) {
        this.game = game;
        setActive(true);
        game.gameScreen.gameObjects.add(this);
    }

    public abstract void renderAndUpdate(ModelBatch modelBatch, Environment environment);

    public void setPosition(float x, float y, float z) {
        setX(x);
        setY(y);
        setZ(z);
    }

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
