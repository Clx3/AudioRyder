package com.mygdx.audioryder.properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

/**
 * Created by Teemu on 7.4.2018.
 */

public class Properties {

    public static Locale currentLocale;

    //Default languange is localeEN
    public static Locale localeEN = new Locale("en", "US");
    public static Locale localeFI = new Locale("fi", "FI");

    public static String playText;
    public static String settingsText;
    public static String exitText;
    public static String returnText;

    public static void updateProperties() {
        I18NBundle myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"), currentLocale);

        playText = myBundle.get("playText");
        settingsText = myBundle.get("settingsText");
        exitText = myBundle.get("exitText");
        returnText = myBundle.get("returnText");
    }

}
