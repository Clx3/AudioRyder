package com.mygdx.audioryder.song;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.audioryder.AudioRyder;
import com.mygdx.audioryder.objects.Note;

/**
 * Created by Teemu on 2.3.2018.
 */

public class SongHandler {

    public static Music currentSong;
    public static FileHandle currentNoteFile;

    private static int songPointer = 0;
    private static String noteArray[];

    //123test
    public static void setupSong(AudioRyder game, Song song) {
        songPointer = 0;
        currentSong = game.assets.get(AudioRyder.SONGS_PATH + song.getSongFileString());
        currentSong.stop();
        currentNoteFile = song.getNoteFile();

        String noteData = currentNoteFile.readString();
        noteArray = new String[noteData.split(" ").length - 1];
        noteArray = noteData.split(" ");

    }

    static boolean noteAdded = false;
    public static void addNotesToGame(AudioRyder game) {
        char direction;
        game.songTimer += Gdx.graphics.getDeltaTime();
        //if(!noteAdded) {
            if (songPointer < noteArray.length - 1 && Float.parseFloat(noteArray[songPointer].replaceAll("[a-zA-Z]", "")) < game.songTimer + (5f / game.noteSpeed) + game.songOffset) {
                direction = noteArray[songPointer].replaceAll("[0-9]", "").charAt(1);
                switch (direction) {
                    case 'U':
                        game.gameScreen.notes.add(new Note(game, 0f, 0, game.box, game.noteSpeed));
                        break;
                    case 'D':
                        game.gameScreen.notes.add(new Note(game, 0f, 2, game.box, game.noteSpeed));
                        break;
                    case 'L':
                        game.gameScreen.notes.add(new Note(game, -4.5f, 1, game.box, game.noteSpeed));
                        break;
                    case 'R':
                        game.gameScreen.notes.add(new Note(game, 4.5f, 3, game.box, game.noteSpeed));
                        break;
                }
                noteAdded = true;
                songPointer++;
            }
        //}
    }

}
