package com.mygdx.audioryder.song;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.audioryder.AudioRyder;

/**
 * Class which handles each songs variables, like names and filepaths.
 *
 * @author Teemu Salminen
 * @author Joonas Salojärvi
 */

public class Song {

    /**
     * Name of the song
     */
    private String name;

    /**
     * Path to the .mp3 file of the song
     */
    private String songFileString;
    private String noteFileString;

    /**
     * The actual .txt file, which contains all the data for pyramid positioning
     */
    private FileHandle noteFile;

    public Song(String name, String songFile, String noteFile) {
        this.name = name;
        this.songFileString = songFile;
        this.noteFile = Gdx.files.internal(AudioRyder.SONGS_PATH + noteFile);
    }



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
