package fr.adrien.sandbox.bo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class LevelBackground {

    private Texture backgroundImage;
    private Rectangle backgroundRectangle;
    private Texture finishLine;
    private Rectangle finishRectangle;
    private final int BCK_X_POS = 0;
    private final int BCK_Y_POS = 0;


    public LevelBackground(int height, int width, String fileName, float cameraHeight) {

        this.setBackgroundImage(fileName);

        this.setBackgroundRectangle(height, width);

        this.setFinishLine();

        this.setFinishRectangle(cameraHeight);

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
        background.x = BCK_X_POS;
        background.y = BCK_Y_POS;
        this.backgroundRectangle = background;
    }

    public Texture getFinishLine() {
        return finishLine;
    }

    private void setFinishLine() {
        this.finishLine = new Texture(Gdx.files.internal("finish.png"));
    }

    public Rectangle getFinishRectangle() {
        return finishRectangle;
    }

    private void setFinishRectangle(float cameraHeight) {
        Rectangle rec = new Rectangle();
        rec.height = cameraHeight;
        rec.width = 50;
        rec.x = 1550;
        rec.y = 0;
        this.finishRectangle = rec;
    }
}// Eo LevelBackground class
