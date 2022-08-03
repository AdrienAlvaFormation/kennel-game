package fr.adrien.sandbox.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import fr.adrien.sandbox.Sandbox;
import fr.adrien.sandbox.bo.Dog;
import fr.adrien.sandbox.bo.LevelBackground;

public class GameScreen implements Screen {

    //

    final Sandbox game;
    public boolean hasWon;
    public OrthographicCamera camera;
    private LevelBackground lvlBackground;
    private Dog dog;
    private GlyphLayout glyphLayout[];
    private final String GRASS_BACKGROUND_FILENAME = "grass.png";
    private TextureRegion frameBuffer = null;

    // CONSTRUCTOR

    public GameScreen(final Sandbox game) {

        this.game = game;
        this.hasWon = false;

        // create the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1600, 800);

        this.lvlBackground = new LevelBackground(800, 1600, GRASS_BACKGROUND_FILENAME, camera.viewportHeight);

        this.dog = new Dog(200, 150, Math.round(camera.viewportWidth / 2), Math.round(camera.viewportHeight / 2));

        this.glyphLayout = new GlyphLayout[1];

        glyphLayout[0]=new GlyphLayout(game.font, "VICTOIRE !");


    }// Eo constructor

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

        dog.flip(); // flip character sprite to look at movement direction

        game.batch.draw(lvlBackground.getBackgroundImage(),
                        lvlBackground.getBackgroundRectangle().x,
                        lvlBackground.getBackgroundRectangle().y,
                        lvlBackground.getBackgroundRectangle().width,
                        lvlBackground.getBackgroundRectangle().height);

        game.batch.draw(lvlBackground.getFinishLine(),
                        lvlBackground.getFinishRectangle().x - 200,
                        lvlBackground.getFinishRectangle().y,
                        lvlBackground.getFinishRectangle().width + 200,
                        lvlBackground.getFinishRectangle().height);

        if(dog.getDogRectangle().getX() != dog.getxBuffer() || dog.getDogRectangle().getY() != dog.getyBuffer())  {
            game.batch.draw(currentFrame,
                    dog.getDogRectangle().getX(),
                    dog.getDogRectangle().getY(),
                    dog.getDogRectangle().getWidth(),
                    dog.getDogRectangle().getHeight());

            frameBuffer = currentFrame; // dans le cas ou le personnage bouge on actualise le frameBuffer
                                       // pour pouvoir le figer quand il s'arrete.
        } else {
            game.batch.draw(frameBuffer,
                    dog.getDogRectangle().getX(),
                    dog.getDogRectangle().getY(),
                    dog.getDogRectangle().getWidth(),
                    dog.getDogRectangle().getHeight());
        }

        if (dog.getDogRectangle().overlaps(lvlBackground.getFinishRectangle())) {

            for (int i=0;i<glyphLayout.length;i++) {
                game.font.draw(game.batch, glyphLayout[i], camera.viewportWidth / 2 - (glyphLayout[i].width / 2), camera.viewportHeight / 2 + (glyphLayout[i].height / 2));
            }

            this.hasWon = true;

        }

        game.batch.end();

        // si x pos actuel != x pox précédent on actualise le buffer (controle pour flip)
        if(dog.getDogRectangle().getX() != dog.getxBuffer()) {
            dog.setxBuffer(dog.getDogRectangle().getX());
        }

        if(dog.getDogRectangle().getY() != dog.getyBuffer()) {
            dog.setyBuffer(dog.getDogRectangle().getY());
        }

        if (!this.hasWon) {
            dog.move();
        }


    }// Eo render()

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        lvlBackground.getBackgroundImage().dispose();
        dog.getDogImage().dispose();
        game.batch.dispose();
        dog.getWalkSheet().dispose();
    }

}// Eo GameScreen class
