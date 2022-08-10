package fr.adrien.sandbox.bo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Reaper {

    private Texture redTexture;
    private Texture orangeTexture;
    private Texture greenTexture;
    private Rectangle rectangle;
    private final int REAPER_HEIGHT = 200;
    private final int REAPER_WIDTH = 125;

    // Animation

    private static final int NOT_WATCHING_FRAME_COLS = 5, NOT_WATCHING_FRAME_ROWS = 1;
    private static final int RETURNING_FRAME_COLS = 10, RETURNING_FRAME_ROWS = 1;
    private static final int WATCHING_FRAME_COLS = 20, WATCHING_FRAME_ROWS = 1;
    private Animation<TextureRegion> notWatchingAnimation;
    private Animation<TextureRegion> returningAnimation;
    private Animation<TextureRegion> watchingAnimation;
    private Texture notWatchingSheet;
    private Texture returningSheet;
    private Texture watchingSheet;
    private float notWatchingStateTime;
    private float returningStateTime;
    private float watchingStateTime;

    // CONSTRUCTOR


    public Reaper(int height, int width, int xPos, int yPos) {
        this.setGreenTexture();
        this.setOrangeTexture();
        this.setRedTexture();
        this.setRectangle(height, width, xPos, yPos);

        this.loadNotWatchingAnimation();
        this.loadReturningAnimation();
        this.loadWatchingAnimation();
    }

    // METHODS

    private void loadNotWatchingAnimation() {
        // Load the sprite sheet as a Texture
        notWatchingSheet = new Texture(Gdx.files.internal("animations/reaper/PassiveIdleReaper-Sheet.png"));

        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(notWatchingSheet,
                notWatchingSheet.getWidth() / NOT_WATCHING_FRAME_COLS,
                notWatchingSheet.getHeight() / NOT_WATCHING_FRAME_ROWS);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        TextureRegion[] notWatchingFrames = new TextureRegion[NOT_WATCHING_FRAME_COLS * NOT_WATCHING_FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < NOT_WATCHING_FRAME_ROWS; i++) {
            for (int j = 0; j < NOT_WATCHING_FRAME_COLS; j++) {
                tmp[i][j].flip(true, false);
                notWatchingFrames[index++] = tmp[i][j];
            }
        }

        // Initialize the Animation with the frame interval and array of frames
        notWatchingAnimation = new Animation<TextureRegion>(0.15f, notWatchingFrames);

        // time to 0
        notWatchingStateTime = 0f;
    }// Eo loadAnimation()

    public void loadReturningAnimation() {
        // Load the sprite sheet as a Texture
        returningSheet = new Texture(Gdx.files.internal("animations/reaper/WieldWeaponReaper-Sheet.png"));

        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(returningSheet,
                returningSheet.getWidth() / RETURNING_FRAME_COLS,
                returningSheet.getHeight() / RETURNING_FRAME_ROWS);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        TextureRegion[] returningFrames = new TextureRegion[RETURNING_FRAME_COLS * RETURNING_FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < RETURNING_FRAME_ROWS; i++) {
            for (int j = 0; j < RETURNING_FRAME_COLS; j++) {
                tmp[i][j].flip(true, false);
                returningFrames[index++] = tmp[i][j];
            }
        }

        // Initialize the Animation with the frame interval and array of frames
        returningAnimation = new Animation<TextureRegion>(0.1f, returningFrames);

        returningStateTime = 0f;
    }// Eo loadAnimation()

    public void loadWatchingAnimation() {
        watchingSheet = new Texture(Gdx.files.internal("animations/reaper/watching.png"));


        TextureRegion[][] tmp = TextureRegion.split(watchingSheet,
                watchingSheet.getWidth() / WATCHING_FRAME_COLS,
                watchingSheet.getHeight() / WATCHING_FRAME_ROWS);

        TextureRegion[] watchingFrames = new TextureRegion[WATCHING_FRAME_COLS * WATCHING_FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < WATCHING_FRAME_ROWS; i++) {
            for (int j = 0; j < WATCHING_FRAME_COLS; j++) {
                tmp[i][j].flip(true, false);
                watchingFrames[index++] = tmp[i][j];
            }
        }

        watchingAnimation = new Animation<TextureRegion>(0.1f, watchingFrames);

        watchingStateTime = 0f;
    }// Eo loadWatchingAnimation()

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

    public Animation<TextureRegion> getNotWatchingAnimation() {
        return notWatchingAnimation;
    }

    public void setNotWatchingAnimation(Animation<TextureRegion> notWatchingAnimation) {
        this.notWatchingAnimation = notWatchingAnimation;
    }

    public Texture getNotWatchingSheet() {
        return notWatchingSheet;
    }

    public void setNotWatchingSheet(Texture notWatchingSheet) {
        this.notWatchingSheet = notWatchingSheet;
    }

    public float getNotWatchingStateTime() {
        return notWatchingStateTime;
    }

    public void setStateTime() {
        this.notWatchingStateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
    }

    public float getReturningStateTime() {
        return returningStateTime;
    }

    public void setReturningStateTime() {
        this.returningStateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
    }

    public void resetReturningStateTime() {

        this.returningStateTime = 0f;

    }

    public Animation<TextureRegion> getReturningAnimation() {
        return returningAnimation;
    }

    public void setReturningAnimation(Animation<TextureRegion> returningAnimation) {
        this.returningAnimation = returningAnimation;
    }

    public Texture getReturningSheet() {
        return returningSheet;
    }

    public void setReturningSheet(Texture returningSheet) {
        this.returningSheet = returningSheet;
    }

    public Animation<TextureRegion> getWatchingAnimation() {
        return watchingAnimation;
    }

    public void setWatchingAnimation(Animation<TextureRegion> watchingAnimation) {
        this.watchingAnimation = watchingAnimation;
    }

    public Texture getWatchingSheet() {
        return watchingSheet;
    }

    public void setWatchingSheet(Texture watchingSheet) {
        this.watchingSheet = watchingSheet;
    }

    public float getWatchingStateTime() {
        return watchingStateTime;
    }

    public void setWatchingStateTime() {
        this.watchingStateTime += Gdx.graphics.getDeltaTime();;
    }

    public void resetWatchingStateTime() {

        this.watchingStateTime = 0f;

    }
}// Eo Reaper class
