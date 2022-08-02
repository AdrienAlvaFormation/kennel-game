package fr.adrien.sandbox.bo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class LevelBackground {

    private Texture backgroundImage;
    private Rectangle backgroundRectangle;
    private final int X_POS = 0;
    private final int Y_POS = 0;

    public LevelBackground(int height, int width, String fileName) {

        this.setBackgroundImage(fileName);

        this.setBackgroundRectangle(height, width);

    }

    // ACCESSORS

    public Texture getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String fileName) {
        this.backgroundImage = new Texture(Gdx.files.internal(fileName));
    }

    public Rectangle getBackgroundRectangle() {
        return backgroundRectangle;
    }

    public void setBackgroundRectangle(int height, int width) {
        Rectangle background = new Rectangle();
        background.height = height;
        background.width = width;
        background.x = X_POS;
        background.y = Y_POS;
        this.backgroundRectangle = background;
    }


}// Eo LevelBackground class
