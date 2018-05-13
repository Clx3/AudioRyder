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
 * This class handles the notes
 * also known as the pyramids of the game.
 * It contains their movement, spawning and logic etc.
 * Collision detection of the notes is done in SpaceShip.java
 *
 * @version 2018.0509
 * @author Teemu Salminen
 * @author Joonas Salojärvi
 */
public class Note extends GameObject {

    //TODO: IS THE U DIRECTION NEEDED?

    /** This is the instance of this Notes pyramid model. */
    ModelInstance noteModel;

    /** This is this notes BoundingBox which is used for collision detection. */
    public BoundingBox collisionBox;

    /** This Vector3 is the minium point of the BoundingBox collisionBox. */
    public Vector3 minPoint;

    /* This Vector3 is the maximum point of the BoundingBox collisionBox. */
    public Vector3 maxPoint;

    /** Width of the collisionBox when it is created. */
    private float collisionboxWidth = 1.5f;

    /** Height of the collisionBox when it is created. */
    private float collisionboxHeight = 1f;

    /** Depth of the collisionBox when it is created. */
    private float collisionboxDepth = 0.7f;

    /** Movement speed of the note. */
    private float speed;

    /** This is this notes Models AnimationController */
    AnimationController controller;

    /**
     * This constructor will spawn a note
     * to the game.
     *
     * @param game Instance of the game.
     * @param position This char is the position or the lane where the note will spawn.
     *                 U = center lane, L = left lane, R = right lane.
     * @param height This char is the height where the note will spawn.
     *               H = note will spawn high height, M = note will spawn medium height , L = note will spawn low height.
     * @param speed Movement speed of the note.
     */
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
                setY(3f);
                this.noteModel = new ModelInstance(game.gameScreen.pyramids[2], getX(), getY(), getZ());
                break;

            case 'M':
                setY(1.5f);
                this.noteModel = new ModelInstance(game.gameScreen.pyramids[1], getX(), getY(), getZ());
                break;

            case 'L':
                setY(0f);
                this.noteModel = new ModelInstance(game.gameScreen.pyramids[0], getX(), getY(), getZ());
                break;

            default:
                setY(0f);
                this.noteModel = new ModelInstance(game.gameScreen.pyramids[0], getX(), getY(), getZ());
                break;
        }

        this.speed = speed;
        controller = new AnimationController(this.noteModel);
        controller.setAnimation("RotateScale", -1);

        collisionBox = new BoundingBox();
        minPoint = new Vector3(getX() - collisionboxWidth/2, getY(), getZ() - collisionboxDepth);
        maxPoint = new Vector3(getX() + collisionboxWidth/2, getY() + collisionboxHeight, getZ() + collisionboxDepth);
        collisionBox.set(minPoint, maxPoint);

    }

    /* Is this moveSpeed needed?: */
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

        }
    }
}
