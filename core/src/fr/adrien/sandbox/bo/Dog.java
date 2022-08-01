package fr.adrien.sandbox.bo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Dog {

    private Texture dogImage;
    private Rectangle dogRectangle;
//    private int xPos;
//    private int yPos;
//    private int height;
//    private int width;

    public Dog(int height, int width, int xPos, int yPos) {

        this.setDogImage();

        this.setDogRectangle(height, width, xPos, yPos);

    }

    public static void move() {


    }

    // ACCESSORS

    public Texture getDogImage() {
        return this.dogImage;
    }

    public Rectangle getDogRectangle() {
        return this.dogRectangle;
    }

    private void setDogImage() {
        this.dogImage =  new Texture(Gdx.files.internal("dog.png"));
    }

    private void setDogRectangle(int height, int width, int xPos, int yPos) {

        Rectangle dog = new Rectangle();
        dog.height = height;
        dog.width = width;
        dog.x = xPos;
        dog.y = yPos;

        this.dogRectangle = dog;
    }

/*    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }*/

}// Eo Dog class
