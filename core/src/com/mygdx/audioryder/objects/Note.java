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

    public Note(float x, int i2, Model model, float speed) {
        direction = i2;
        setX(x);
        setY(-1.0f);
        setZ(-54f);

        this.noteModel = new ModelInstance(model,getX(),getY(),getZ());

        this.speed = speed;
        controller = new AnimationController(this.noteModel);
        controller.setAnimation("Bend", -1);

        collisionBox = new BoundingBox();
        minPoint = new Vector3(getX() - collisionboxWidth/2, getY(), getZ() - collisionboxDepth);
        maxPoint = new Vector3(getX() + collisionboxWidth/2, getY() + collisionboxHeight / 2, getZ() + collisionboxDepth);
        collisionBox.set(minPoint, maxPoint);

    }

    public void render(ModelBatch modelBatch, Environment environment){
        controller.update(Gdx.graphics.getDeltaTime());
        modelBatch.render(noteModel, environment);
    }

    float moveSpeed;
    public void move3d(AudioRyder game) {
        moveSpeed = Gdx.graphics.getDeltaTime() * 10f * speed;
        setZ(getZ() + moveSpeed);

        minPoint.z += moveSpeed;
        maxPoint.z += moveSpeed;
        noteModel.transform.setToTranslation(getX(),getY(), getZ());

        collisionBox.set(minPoint, maxPoint);

        if(getZ() >= 0)
            game.gameScreen.notesToRemove.add(this);

    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
