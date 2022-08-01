package fr.adrien.sandbox.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import fr.adrien.sandbox.Sandbox;

public class GameScreen implements Screen {
    final Sandbox game;
    OrthographicCamera camera;
    Texture grassImage;
    Texture dogImage;
    Rectangle grassBackgroung;
    Rectangle dog;

    public GameScreen(final Sandbox game) {
        this.game = game;

        grassImage = new Texture(Gdx.files.internal("grass.png"));
        dogImage = new Texture(Gdx.files.internal("dog.png"));

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1600, 800);

        grassBackgroung = new Rectangle();
        grassBackgroung.height = 800;
        grassBackgroung.width = 1600;
        grassBackgroung.x = 0;
        grassBackgroung.y = 0;


        dog = new Rectangle();
        dog.height = 100;
        dog.width = 150;
        dog.x = 100;
        dog.y = 100;

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

        game.batch.draw(grassImage, grassBackgroung.x, grassBackgroung.y, grassBackgroung.width, grassBackgroung.height);

        game.batch.draw(dogImage, dog.x, dog.y, dog.width, dog.height);

        game.batch.end();

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

    }
}
