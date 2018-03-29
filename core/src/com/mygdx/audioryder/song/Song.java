package com.mygdx.audioryder.song;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.audioryder.AudioRyder;

/**
 * Created by Teemu on 17.3.2018.
 */

public class Song {

    private String name;

    private String songFileString;
    private String noteFileString;

    //private Music soundFile;

    private FileHandle noteFile;

    public Song(String name, String songFile, String noteFile) {
        this.name = name;
        this.songFileString = songFile;
        this.noteFile = Gdx.files.internal(AudioRyder.SONGS_PATH + noteFile);
    }

    //Temp thing (MAYBE) not sure if this is goiug to be needed:

    /*public void loadSong(AudioRyder game) {
        soundFile = game.assets.get(getSongFileString());
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileHandle getNoteFile() {
        return noteFile;
    }

    public void setNoteFile(FileHandle noteFile) {
        this.noteFile = noteFile;
    }

    public String getNoteFileString() {
        return noteFileString;
    }

    public void setNoteFileString(String noteFileString) {
        this.noteFileString = noteFileString;
    }

    public String getSongFileString() {

        return songFileString;
    }

    public void setSongFileString(String noteFileString) {
        this.noteFileString = noteFileString;
    }
}