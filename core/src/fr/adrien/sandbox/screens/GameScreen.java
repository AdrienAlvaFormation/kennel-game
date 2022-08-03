package fr.adrien.sandbox.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fr.adrien.sandbox.Sandbox;
import fr.adrien.sandbox.bo.Dog;
import fr.adrien.sandbox.bo.LevelBackground;

public class GameScreen implements Screen {
    final Sandbox game;
    OrthographicCamera camera;
    LevelBackground lvlBackground;
    Dog dog;
    private final String GRASS_BACKGROUND_FILENAME = "grass.png";
    private float xBuffer;

    private TextureRegion frameBuffer = null;

    public GameScreen(final Sandbox game) {

        this.game = game;

        // create the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1600, 800);

        this.lvlBackground = new LevelBackground(800, 1600, GRASS_BACKGROUND_FILENAME, camera.viewportHeight);

        this.dog = new Dog(200, 150, 100, 100);

        System.out.println(camera.viewportHeight);



    }

    /**
     *
     */
    @Override
    public void show() {

    }

    /**
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {



        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // DRAW
        game.batch.begin();

        dog.setStateTime();

        TextureRegion currentFrame = dog.getWalkAnimation().getKeyFrame(dog.getStateTime(), true);

        if (frameBuffer == null) {
            frameBuffer = currentFrame;
        }

        // TODO refacto -> to Dog
        for (TextureRegion frame: dog.getWalkAnimation().getKeyFrames()) {
            if(dog.getDogRectangle().getX() < xBuffer && !frame.isFlipX()) {
                frame.flip(true, false);
            }
        }

        for (TextureRegion frame: dog.getWalkAnimation().getKeyFrames()) {
            if(dog.getDogRectangle().getX() > xBuffer && frame.isFlipX()) {
                frame.flip(true, false);
            }
        }

        game.batch.draw(lvlBackground.getBackgroundImage(),
                        lvlBackground.getBackgroundRectangle().x,
                        lvlBackground.getBackgroundRectangle().y,
                        lvlBackground.getBackgroundRectangle().width,
                        lvlBackground.getBackgroundRectangle().height);

        game.batch.draw(lvlBackground.getFinishLine(),
                        lvlBackground.getFinishRectangle().x,
                        lvlBackground.getFinishRectangle().y,
                        lvlBackground.getFinishRectangle().width,
                        lvlBackground.getFinishRectangle().height);

        if(dog.getDogRectangle().getX() != this.xBuffer) {
            game.batch.draw(currentFrame,
                    dog.getDogRectangle().getX(),
                    dog.getDogRectangle().getY(),
                    dog.getDogRectangle().getWidth(),
                    dog.getDogRectangle().getHeight());

            frameBuffer = currentFrame;

        } else {

            game.batch.draw(frameBuffer,
                    dog.getDogRectangle().getX(),
                    dog.getDogRectangle().getY(),
                    dog.getDogRectangle().getWidth(),
                    dog.getDogRectangle().getHeight());

        }

        game.batch.end();



        if(dog.getDogRectangle().getX() != this.xBuffer) {
            this.xBuffer = dog.getDogRectangle().getX();
        }



        // TODO refacto -> to Dog
        dog.move();




    }

    /**
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     *
     */
    @Override
    public void pause() {

    }

    /**
     *
     */
    @Override
    public void resume() {

    }

    /**
     *
     */
    @Override
    public void hide() {

    }

    /**
     *
     */
    @Override
    public void dispose() {
        lvlBackground.getBackgroundImage().dispose();
        dog.getDogImage().dispose();
        game.batch.dispose();
        dog.getWalkSheet().dispose();
    }
}
