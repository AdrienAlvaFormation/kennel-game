package fr.adrien.sandbox.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import fr.adrien.sandbox.Sandbox;
import fr.adrien.sandbox.bo.Dog;

public class GameScreen implements Screen {
    final Sandbox game;
    OrthographicCamera camera;

    Dog dog;
    Texture grassImage;
    Rectangle grassBackground;

    public GameScreen(final Sandbox game) {
        this.game = game;

        this.dog = new Dog(100, 150, 100, 100);

        grassImage = new Texture(Gdx.files.internal("grass.png"));

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1600, 800);

        grassBackground = new Rectangle();
        grassBackground.height = 800;
        grassBackground.width = 1600;
        grassBackground.x = 0;
        grassBackground.y = 0;

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

        game.batch.begin();

        game.batch.draw(grassImage, grassBackground.x, grassBackground.y, grassBackground.width, grassBackground.height);

        game.batch.draw(dog.getDogImage(), dog.getDogRectangle().x, dog.getDogRectangle().y, dog.getDogRectangle().getWidth(), dog.getDogRectangle().getHeight());

        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            dog.getDogRectangle().x -= 200 * Gdx.graphics.getDeltaTime();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            dog.getDogRectangle().x += 200 * Gdx.graphics.getDeltaTime();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            dog.getDogRectangle().y += 200 * Gdx.graphics.getDeltaTime();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            dog.getDogRectangle().y -= 200 * Gdx.graphics.getDeltaTime();
        }

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
        grassImage.dispose();
        dog.getDogImage().dispose();
    }
}
