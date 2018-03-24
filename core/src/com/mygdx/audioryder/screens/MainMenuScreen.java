package com.mygdx.audioryder.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by Teemu on 17.3.2018.
 */

public class MainMenuScreen implements Screen {

    class MenuButton extends TextButton {

        public MenuButton(String text, Skin skin) {
            super(text, skin);
            setWidth(200f);
            setHeight(100f);
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
