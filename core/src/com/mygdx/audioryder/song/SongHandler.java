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

    public static void addNotesToGame(AudioRyder game) {
        char notePosition;
        char noteHeight;
        game.songTimer += Gdx.graphics.getDeltaTime();
            if (songPointer < noteArray.length - 1 && Float.parseFloat(noteArray[songPointer].replaceAll("[a-zA-Z]", "")) < game.songTimer + (5f / game.noteSpeed) + game.songOffset) {
                notePosition = noteArray[songPointer].replaceAll("[0-9]", "").charAt(1);
                noteHeight = noteArray[songPointer].replaceAll("[0-9]", "").charAt(2);

                spawnNote(game, notePosition, noteHeight);
                songPointer++;
            }
    }

    public static void spawnNote(AudioRyder game, char position, char height) {
        float x;
        float y;

        switch(position) {
            case 'U': x = 0f; break;
            case 'D': x = 0f; break;
            case 'L': x = -4.5f; break;
            case 'R': x = 4.5f; break;

            default: x = 0f;
        }

        switch(height) {
            case 'H': y = 2f; break;
            case 'M': y = 1f; break;
            case 'L': y = -1f; break;

            default: y = -1f; break;
        }

        game.gameScreen.notes.add(new Note(game, x, y, -50f, game.box, game.noteSpeed));

    }

}
