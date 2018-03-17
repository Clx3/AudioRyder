package com.mygdx.audioryder.song;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.audioryder.AudioRyder;

/**
 * Created by Teemu on 2.3.2018.
 */

public class SongHandler {

    public static Music currentSong;
    public static FileHandle currentNoteFile;

    //123test
    public static void setupSong(AudioRyder game, Song song) {
        currentSong = game.assets.get(song.getSongFileString());
        currentNoteFile = song.getNoteFile();
    }

}
