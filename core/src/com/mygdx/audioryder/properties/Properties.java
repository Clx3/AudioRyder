package com.mygdx.audioryder.properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

/**
 * This class handles locale settings. Loads all
 * strings with selected language.
 *
 * @version 2018.0509
 * @author Teemu Salminen
 * @author Joonas Salojärvi
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
    public static String sensitivityText;
    public static String sensitivityText2;
    public static String scoreText;
    public static String pauseText;
    public static String continueText;
    public static String restartText;
    public static String calibrateText;
    public static String highScoreText;
    public static String gameSpeedText;
    public static String playerText;
    public static String guideText;
    public static String artistsText;
    public static String artistsStageTitleText1;
    public static String artistsStageTitleText2;
    public static String tutorialText1;
    public static String tutorialText2;
    public static String musicVolText;
    public static String heightText;

    /**
     * Updates all strings with selected language.
     */
    public static void updateProperties() {
        I18NBundle myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"), currentLocale);

        playText = myBundle.get("playText");
        settingsText = myBundle.get("settingsText");
        exitText = myBundle.get("exitText");
        returnText = myBundle.get("returnText");
        sensitivityText = myBundle.get("sensitivityText");
        sensitivityText2 = myBundle.get("sensitivityText2");
        scoreText = myBundle.get("scoreText");
        pauseText = myBundle.get("pauseText");
        continueText = myBundle.get("continueText");
        restartText = myBundle.get("restartText");
        calibrateText = myBundle.get("calibrateText");
        highScoreText = myBundle.get("highScoreText");
        gameSpeedText = myBundle.get("gameSpeedText");
        playerText = myBundle.get("playerText");
        guideText = myBundle.get("guideText");
        artistsText = myBundle.get("artistsText");
        tutorialText1 = myBundle.get("tutorialText1");
        tutorialText2 = myBundle.get("tutorialText2");
        artistsStageTitleText1 = myBundle.get("artistsStageTitle1");
        artistsStageTitleText2 = myBundle.get("artistsStageTitle2");
        musicVolText = myBundle.get("musicVolText");
        heightText = myBundle.get("heightText");
    }

}
