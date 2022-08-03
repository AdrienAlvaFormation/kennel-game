package fr.adrien.sandbox.bo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Reaper {

    private Texture redTexture;
    private Texture greenTexture;
    private Rectangle rectangle;
    private final int REAPER_HEIGHT = 200;
    private final int REAPER_WIDTH = 125;

    // CONSTRUCTOR


    public Reaper() {
        this.setGreenTexture();
        this.greenTexture = greenTexture;
        this.dogRectangle = dogRectangle;
    }

    // ACCESSORS


    public Texture getRedTexture() {
        return redTexture;
    }

    public void setRedTexture(Texture redTexture) {
        this.redTexture = redTexture;
    }

    public Texture getGreenTexture() {
        return greenTexture;
    }

    public void setGreenTexture(Texture greenTexture) {
        this.greenTexture = greenTexture;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }
}// Eo Reaper class
