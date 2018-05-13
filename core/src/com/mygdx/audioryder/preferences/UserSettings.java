package com.mygdx.audioryder.preferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.mygdx.audioryder.properties.Properties;

/**
 * This class handles loading and saving the user settings.
 * User settings are saved on the device's memory, and they survive
 * app updates.
 *
 * @version 2018.0509
 * @author Teemu Salminen
 * @author Joonas Salojärvi
 */

public class UserSettings {

    public static String playerName;

    /** Used for determining how fast the note objects move in game */
    public static float noteSpeed;
    public static float sensitivityLeft;
    public static float sensitivityRight;
    public static float sensitivityDown;
    public static float sensitivityUp;

    /** Calibration for x-axis */
    public static float xCalib;
    /** Calibration for y-axis */
    public static float yCalib;

    /** Defines if X-accelerometer should be used instead */
    public static boolean useXAccelMeter;

    public static float musicVol;

    /**
     * Loads all user settings from preferences.
     */
    public static void loadPlayerSettings() {
        Preferences userSettings = Gdx.app.getPreferences("userSettings");
        playerName = userSettings.getString("playerName", "Guest");
        String gameLanguage = userSettings.getString("language", "en");
        if(gameLanguage.equals("en")){
            Properties.currentLocale = Properties.localeEN;
        } else if(gameLanguage.equals("fi")){
            Properties.currentLocale = Properties.localeFI;
        }
        Properties.updateProperties();
        noteSpeed = userSettings.getFloat("gameSpeed", 2f);
        sensitivityLeft = userSettings.getFloat("sensitivityLeft", 1.5f);
        sensitivityDown = userSettings.getFloat("sensitivityDown", 2f);
        sensitivityRight = userSettings.getFloat("sensitivityRight", 1.5f);
        sensitivityUp = userSettings.getFloat("sensitivityUp", 2f);
        xCalib = userSettings.getFloat("xCalib",0f);
        yCalib = userSettings.getFloat("yCalib",0f);
        useXAccelMeter = userSettings.getBoolean("useXAccelMeter",false);
        musicVol = userSettings.getFloat("musicVol",0.7f);
    }

    /**
     * Saves all player settings to preferences.
     */
    public static void savePlayerSettings() {
        Preferences userSettings = Gdx.app.getPreferences("userSettings");
        userSettings.putFloat("sensitivityLeft",sensitivityLeft);
        userSettings.putFloat("sensitivityRight",sensitivityRight);
        userSettings.putFloat("sensitivityUp",sensitivityUp);
        userSettings.putFloat("sensitivityDown",sensitivityDown);
        userSettings.putFloat("gameSpeed",noteSpeed);
        userSettings.putString("playerName",playerName);
        userSettings.putFloat("musicVol",musicVol);
        userSettings.flush();
    }
    public static void calibrate(){
        Preferences userSettings = Gdx.app.getPreferences("userSettings");
        xCalib = Gdx.input.getAccelerometerY();
        if(Gdx.input.getAccelerometerZ() < 5 && Gdx.input.getAccelerometerZ() > -5){
            yCalib = Gdx.input.getAccelerometerZ();
            useXAccelMeter = false;
        } else {
            yCalib = Gdx.input.getAccelerometerX();
            useXAccelMeter = true;
        }
        userSettings.putFloat("xCalib",xCalib);
        userSettings.putFloat("yCalib",yCalib);
        userSettings.putBoolean("useXAccelMeter",useXAccelMeter);
        userSettings.flush();
    }

}
