package com.mygdx.audioryder.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.mygdx.audioryder.AudioRyder;

/**
 * Created by Joonas on 15.2.2018.
 */

public class Note extends GameObject {

    AudioRyder game;

    ModelInstance noteModel;

    public BoundingBox collisionBox;
    public Vector3 minPoint;
    public Vector3 maxPoint;

    private int direction;

    private float collisionboxWidth = 1.5f;
    private float collisionboxHeight = 2.3f;
    private float collisionboxDepth = 0.7f;

    float speed;
    AnimationController controller;

    public Note(AudioRyder game, float x, int i2, Model model, float speed) {
        super(game);
        this.game = game;
        direction = i2;
        setX(x);
        setY(-1.0f);
        setZ(-50f);

        this.noteModel = new ModelInstance(model,getX(),getY(),getZ());

        this.speed = speed;
        controller = new AnimationController(this.noteModel);
        controller.setAnimation("Bend", -1);

        collisionBox = new BoundingBox();
        minPoint = new Vector3(getX() - collisionboxWidth/2, getY(), getZ() - collisionboxDepth);
        maxPoint = new Vector3(getX() + collisionboxWidth/2, getY() + collisionboxHeight / 2, getZ() + collisionboxDepth);
        collisionBox.set(minPoint, maxPoint);

    }

    float moveSpeed;
    @Override
    public void renderAndUpdate(ModelBatch modelBatch, Environment environment) {
        controller.update(Gdx.graphics.getDeltaTime());
        modelBatch.render(noteModel, environment);

        moveSpeed = Gdx.graphics.getDeltaTime() * 10f * speed;
        setZ(getZ() + moveSpeed);

        minPoint.z += moveSpeed;
        maxPoint.z += moveSpeed;
        noteModel.transform.setToTranslation(getX(),getY(), getZ());

        collisionBox.set(minPoint, maxPoint);

        if(getZ() >= 0) {
            setActive(false);
            game.gameScreen.notesToRemove.add(this);
            game.streak = 0;
            game.multiplier = 1;
            game.hitOrMiss = false;
            game.hitOrMissTimer = 0f;
        }
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
