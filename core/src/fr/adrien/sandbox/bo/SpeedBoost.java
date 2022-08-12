package fr.adrien.sandbox.bo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class SpeedBoost {

    private Texture BoostTexture;
    private Rectangle boostRec;
    private float boostHeight;
    private float boostWidth;
    private float boostX;
    private float boostY;
    private boolean consume;

    public SpeedBoost() {

        this.consume = false;

        this.boostHeight = 60;
        this.boostWidth = 60;

        this.boostX = 500;
        this.boostY = 500;

        this.setBoostTexture();
        this.setBoostRec();
    }

    public Texture getBoostTexture() {
        return BoostTexture;
    }

    public void setBoostTexture() {
        BoostTexture  = new Texture(Gdx.files.internal("textures/apple.png"));
    }

    public Rectangle getBoostRec() {
        return boostRec;
    }

    public void setBoostRec() {
        Rectangle rec = new Rectangle();
        rec.height = boostHeight;
        rec.width = boostWidth;
        rec.x = getBoostX();
        rec.y = getBoostY();
        this.boostRec = rec;
    }

    public float getBoostX() {
        return boostX;
    }

    public void setBoostX(float boostX) {
        this.boostX = boostX;
    }

    public float getBoostY() {
        return boostY;
    }

    public void setBoostY(float boostY) {
        this.boostY = boostY;
    }

    public boolean isConsume() {
        return consume;
    }

    public void setConsume(boolean consume) {
        this.consume = consume;
    }
}// Eo SpeedBoost class
