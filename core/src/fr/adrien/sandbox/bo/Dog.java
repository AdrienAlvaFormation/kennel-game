package fr.adrien.sandbox.bo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Dog {

    private Texture dogImage;
    private Rectangle dogRectangle;
    private int dogSpeed;

    private final int DOG_HEIGHT = 200;
    private final int DOG_WIDTH = 125;

    // ANIMATION
    // Constant rows and columns of the sprite sheet
    private static final int FRAME_COLS = 6, FRAME_ROWS = 5;
    private Animation<TextureRegion> walkAnimation;
    private Texture walkSheet;
    private float stateTime;

    private boolean goingRight;

    public Dog(int height, int width, int xPos, int yPos) {

        this.setDogImage();

        this.setDogRectangle(height, width, xPos, yPos);

        this.dogSpeed = 300;

        this.goingRight = true;

        this.loadAnimation();



    }

    /**
     * Dog's movements
     */
    public void move() {

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.dogRectangle.x -= this.dogSpeed * Gdx.graphics.getDeltaTime();
            this.setGoingRight(false);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.dogRectangle.x += this.dogSpeed * Gdx.graphics.getDeltaTime();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.dogRectangle.y += this.dogSpeed * Gdx.graphics.getDeltaTime();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            this.dogRectangle.y -= this.dogSpeed * Gdx.graphics.getDeltaTime();
        }

        // make sure the bucket stays within the screen bounds
        if (dogRectangle.x < 0) {
            dogRectangle.x = 0;
        }

        if (dogRectangle.x > 1600 - DOG_WIDTH){
            dogRectangle.x = 1600 - DOG_WIDTH;
        }

        if (dogRectangle.y < 0) {
            dogRectangle.y = 0;
        }

        if (dogRectangle.y > 600) {
            dogRectangle.y = 600;
        }

    }

    private void loadAnimation() {

        // Load the sprite sheet as a Texture
        walkSheet = new Texture(Gdx.files.internal("animation_sheet.png"));

        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / FRAME_COLS,
                walkSheet.getHeight() / FRAME_ROWS);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        // Initialize the Animation with the frame interval and array of frames
        walkAnimation = new Animation<TextureRegion>(0.025f, walkFrames);

        // time to 0
        stateTime = 0f;

    }// Eo loadAnimation()

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

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime() {
        this.stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
    }

    public Animation<TextureRegion> getWalkAnimation() {
        return walkAnimation;
    }

    private void setWalkAnimation(Animation<TextureRegion> walkAnimation) {
        this.walkAnimation = walkAnimation;
    }

    public Texture getWalkSheet() {
        return walkSheet;
    }

    private void setWalkSheet(Texture walkSheet) {
        this.walkSheet = walkSheet;
    }

    public boolean isGoingRight() {
        return goingRight;
    }

    public void setGoingRight(boolean goingRight) {
        this.goingRight = goingRight;
    }
}// Eo Dog class
