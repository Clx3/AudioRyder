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

    public Note(AudioRyder game, char position, char height, float speed) {
        super(game);

        setZ(-50f);

        switch(position) {
            case 'U': setX(0f); break;
            case 'D': setX(0f); break;
            case 'L': setX(-4.5f); break;
            case 'R': setX(4.5f); break;

            default: setX(0f);
        }

        switch(height) {
            case 'H':
                setY(2.2f);
                this.noteModel = new ModelInstance(game.gameScreen.pyramids[2], getX(), getY(), getZ());
                break;

            case 'M':
                setY(0.5f);
                this.noteModel = new ModelInstance(game.gameScreen.pyramids[1], getX(), getY(), getZ());
                break;

            case 'L':
                setY(-1f);
                this.noteModel = new ModelInstance(game.gameScreen.pyramids[0], getX(), getY(), getZ());
                break;

            default:
                setY(-1f);
                this.noteModel = new ModelInstance(game.gameScreen.pyramids[0], getX(), getY(), getZ());
                break;
        }

        this.speed = speed;
        controller = new AnimationController(this.noteModel);
        controller.setAnimation("RotateScale", -1);

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

        if(getZ() >= 10f) {
            setActive(false);
            game.gameScreen.notesToRemove.add(this);
            game.streak = 0;
            game.multiplier = 1;
            game.hitOrMiss = false;
            game.hitOrMissTimer = 0f;
        }
    }
}
