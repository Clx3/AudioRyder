package com.mygdx.audioryder.song;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.audioryder.AudioRyder;
import com.mygdx.audioryder.objects.Note;
import com.mygdx.audioryder.preferences.UserSettings;

/**
 * Handles the logic behind spawning new notes/pyramids in the game.
 * Doesn't actually load the song, as it is loaded in the loading screen.
 * Gets the float time values from the .txt file and puts them in to array.
 *
 * @version 2018.0509
 * @author Teemu Salminen
 * @author Joonas Salojärvi
 */
public class SongHandler {

    /** Music file to be played */
    public static Music gameMusic;

    /** The .txt file for note positioning */
    public static FileHandle currentNoteFile;

    /**
     * Tells, which point in the song is going on. Used to pick correct value
     * from the array list, without going through the whole list.
     */
    private static int songPointer = 0;

    /** Contains all the notes from the .txt file, including position and timing */
    private static String noteArray[];

    /**
     * Sets up the song before the game actually starts. Gets the raw
     * data from a .txt file and arranges it in to an array. The data
     * includes time value as a float and the position of each note/
     * pyramid.
     *
     * @param game
     * @param song
     */

    /**
     * True if the music has started playing, used to prevent premature end screen
     */
    public static boolean musicIsStarted;

    public static void setupSong(AudioRyder game, Song song) {
        songPointer = 0;
        gameMusic = game.assets.get(AudioRyder.SONGS_PATH + song.getSongFileString());
        gameMusic.stop();
        musicIsStarted = false;
        currentNoteFile = song.getNoteFile();

        String noteData = currentNoteFile.readString();
        noteArray = new String[noteData.split(" ").length - 1];
        noteArray = noteData.split(" ");

    }

    /**
     * Checks the latest time value of a note, using song pointer's value as index. If the time
     * value is lower than song timer's spawns a new note at the correct position. Because of
     * song pointer, it doesn't need to go through whole array every time.
     * @param game
     */
    public static void addNotesToGame(AudioRyder game, float songTimer) {
        char notePosition;
        char noteHeight;
            if (songPointer < noteArray.length - 1 && Float.parseFloat(noteArray[songPointer].replaceAll("[a-zA-Z]", "")) < songTimer + (5f / UserSettings.noteSpeed) + game.songOffset) {
                notePosition = noteArray[songPointer].replaceAll("[0-9]", "").charAt(1);
                noteHeight = noteArray[songPointer].replaceAll("[0-9]", "").charAt(2);
                game.gameScreen.notes.add(new Note(game, notePosition, noteHeight, UserSettings.noteSpeed));
                songPointer++;
            }
    }

}
