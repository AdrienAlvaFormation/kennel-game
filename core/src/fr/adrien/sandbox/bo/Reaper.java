package fr.adrien.sandbox.bo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Reaper {

    private Texture redTexture;
    private Texture orangeTexture;
    private Texture greenTexture;
    private Rectangle rectangle;
    private final int REAPER_HEIGHT = 200;
    private final int REAPER_WIDTH = 125;

    // CONSTRUCTOR


    public Reaper(int height, int width, int xPos, int yPos) {
        this.setGreenTexture();
        this.setOrangeTexture();
        this.setRedTexture();
        this.setRectangle(height, width, xPos, yPos);
    }

    // ACCESSORS

    public Texture getRedTexture() {
        return redTexture;
    }

    public void setRedTexture() {
        this.redTexture =  new Texture(Gdx.files.internal("red-square.png"));
    }

    public Texture getOrangeTexture() {
        return orangeTexture;
    }

    public void setOrangeTexture() {
        this.orangeTexture =  new Texture(Gdx.files.internal("orange-square.png"));

    }

    public Texture getGreenTexture(){
        return greenTexture;
    }

    public void setGreenTexture() {
        this.greenTexture =  new Texture(Gdx.files.internal("green-square.png"));
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(int height, int width, int xPos, int yPos) {
        Rectangle rec = new Rectangle();
        rec.height = height;
        rec.width = width;
        rec.x = xPos;
        rec.y = yPos;

        this.rectangle = rec;
    }
}// Eo Reaper class
