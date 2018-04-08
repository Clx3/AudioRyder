package com.mygdx.audioryder.preferences;

import com.badlogic.gdx.Preferences;

/**
 * Created by Teemu on 3.4.2018.
 */

public class UserSettings {

    public static String playerName;

    public static float sensitivityX;
    public static float sensitivityY;

    public static boolean soundOn;
    public static boolean musicOn;

    public static void getPlayerSettings(Preferences prefs) {
        playerName = prefs.getString("playerName");
        sensitivityX = prefs.getFloat("sensitivityX");
        sensitivityY = prefs.getFloat("sensitivityY");
        soundOn = prefs.getBoolean("soundOn");
        musicOn = prefs.getBoolean("musicOn");
    }

    public static void savePlayerSettings(Preferences prefs) {
        prefs.putString("playerName", playerName);
        prefs.putFloat("sensitivityX", sensitivityX);
        prefs.putFloat("sensitivityY", sensitivityY);
    }

    public static void setDefaultSettings() {
        playerName = "Player";
        sensitivityX = 0;
        sensitivityY = 0;
        soundOn = true;
        musicOn = true;
    }
}
