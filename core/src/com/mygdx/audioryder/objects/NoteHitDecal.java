package com.mygdx.audioryder.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.mygdx.audioryder.AudioRyder;
import com.mygdx.audioryder.preferences.UserSettings;

/**
 * This object is our only object class
 * that doesn't extend GameObject because
 * everything needed for this was pretty much in the Decal class.
 * This class is a Decal that is basically a indicator
 * for when player hits a note, it will create this object to the
 * position of the note.
 *
 * @version 2018.0514
 * @author Teemu Salminen
 * @author Joonas Salojärvi
 */
public class NoteHitDecal {

    /** The Decal object of this decal. */
    private Decal decal;

    /**
     * This boolean checks if the object is active.
     * If false, the object will not be rendered and will be
     * removed from gameDecals ArrayList.
     */
    private boolean isActive;

    /** This is the starting height of the decal. Initialized in constructor. */
    private float startingHeight;

    /** Alpha value of the decal. */
    private float alpha = 0.9f;

    /**
     * This creates a new Decal at the given position.
     *
     * @param game Instance of the game.
     * @param x X position of the decal.
     * @param y Y position of the decal.
     * @param z Z position of the decal.
     * @param textureRegion Texture / TextureRegion of the decal.
     */
    public NoteHitDecal(AudioRyder game, float x, float y, float z, TextureRegion textureRegion) {
        setActive(true);

        decal = Decal.newDecal(textureRegion, true);
        decal.setScale(0.008f);
        decal.setColor(decal.getColor().r, decal.getColor().g, decal.getColor().b, alpha);
        decal.setPosition(x, y, z);

        startingHeight = decal.getY();
    }

    /**
     * This method will update this Decal
     * object. Called in GameScreen.java.
     */
    public void renderAndUpdate() {
        decal.setY(decal.getY() + 4f * Gdx.graphics.getDeltaTime());
        decal.setZ(decal.getZ() + Gdx.graphics.getDeltaTime() * 10f * UserSettings.noteSpeed);

        if(decal.getY() - startingHeight >= 2.3f)
            setActive(false);

        if(decal.getColor().a >= 0.03f)
            decal.setColor(decal.getColor().r, decal.getColor().g, decal.getColor().b, alpha);
        alpha -= 0.03f;

        decal.setPosition(decal.getX(), decal.getY(), decal.getZ());
    }

    /* Getters & setters: */

    public Decal getDecal() {
        return decal;
    }

    public void setDecal(Decal decal) {
        this.decal = decal;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public float getStartingHeight() {
        return startingHeight;
    }

    public void setStartingHeight(float startingHeight) {
        this.startingHeight = startingHeight;
    }

}
