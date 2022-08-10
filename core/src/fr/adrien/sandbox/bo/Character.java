package fr.adrien.sandbox.bo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Character {

    private Rectangle characterRec;
    private int characterSpeed;

    private final int DOG_HEIGHT = 200;
    private final int DOG_WIDTH = 125;

    // ANIMATION
    private static final int FRAME_COLS = 6, FRAME_ROWS = 5;// Constant rows and columns of the sprite sheet
    private Animation<TextureRegion> walkAnimation;
    private Texture walkSheet;
    private float stateTime;
    private float xBuffer;
    private float yBuffer;

    // CONSTRUCTOR
    public Character(int height, int width, int xPos, int yPos) {

        this.setDogRectangle(height, width, xPos, yPos);

        this.characterSpeed = 300;

        this.loadAnimation();

    }

    // METHODS

    /**
     * Dog's movements
     */
    public void move() {

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.characterRec.x -= this.characterSpeed * Gdx.graphics.getDeltaTime();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.characterRec.x += this.characterSpeed * Gdx.graphics.getDeltaTime();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.characterRec.y += this.characterSpeed * Gdx.graphics.getDeltaTime();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            this.characterRec.y -= this.characterSpeed * Gdx.graphics.getDeltaTime();
        }

        // make sure the character stays within the screen bounds
        if (characterRec.x < 0) {
            characterRec.x = 0;
        }

        if (characterRec.x > 1600 - DOG_WIDTH){
            characterRec.x = 1600 - DOG_WIDTH;
        }

        if (characterRec.y < 0) {
            characterRec.y = 0;
        }

        if (characterRec.y > 600) {
            characterRec.y = 600;
        }

    }

    public void flip() {

        for (TextureRegion frame: this.walkAnimation.getKeyFrames()) {
            if(this.characterRec.getX() < xBuffer && !frame.isFlipX()) {
                frame.flip(true, false);
            }
        }

        for (TextureRegion frame: this.walkAnimation.getKeyFrames()) {
            if(this.characterRec.getX() > xBuffer && frame.isFlipX()) {
                frame.flip(true, false);
            }
        }

    }// Eo flip()

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

    public Rectangle getCharacterRec() {
        return this.characterRec;
    }

    private void setDogRectangle(int height, int width, int xPos, int yPos) {
        Rectangle dog = new Rectangle();
        dog.height = height;
        dog.width = width;
        dog.x = xPos;
        dog.y = yPos;

        this.characterRec = dog;
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

    public float getxBuffer() {
        return xBuffer;
    }

    public void setxBuffer(float xBuffer) {
        this.xBuffer = xBuffer;
    }

    public float getyBuffer() {
        return yBuffer;
    }

    public void setyBuffer(float yBuffer) {
        this.yBuffer = yBuffer;
    }
}// Eo Dog class
