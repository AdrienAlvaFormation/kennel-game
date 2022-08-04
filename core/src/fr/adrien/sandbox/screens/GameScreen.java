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
import fr.adrien.sandbox.bo.Reaper;
import fr.adrien.sandbox.controller.GamePhaseTimer;

public class GameScreen implements Screen {

    // ATTRIBUTES

    final Sandbox game;
    public boolean hasWon;
    public boolean hasLost;
    public OrthographicCamera camera;
    private LevelBackground lvlBackground;
    private Dog dog;
    private float playerXBuffer;
    private float playerYBuffer;
    private Reaper reaper;
    private GlyphLayout glyphLayout[];
    private final String GRASS_BACKGROUND_FILENAME = "grass.png";
    private TextureRegion frameBuffer = null;
    private GamePhaseTimer timer;



    // CONSTRUCTOR

    public GameScreen(final Sandbox game) {

        this.game = game;
        this.hasWon = false;

        // create the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1600, 800);

        this.lvlBackground = new LevelBackground(800, 1600, GRASS_BACKGROUND_FILENAME, camera.viewportHeight);

//        this.dog = new Dog(200, 150, Math.round(camera.viewportWidth / 2), Math.round(camera.viewportHeight / 2));

        this.dog = new Dog(200, 150, 100, Math.round(camera.viewportHeight / 2));

        this.reaper = new Reaper(100, 100, 1400, Math.round(camera.viewportHeight / 2 - 50));

        this.glyphLayout = new GlyphLayout[2];

        glyphLayout[0]=new GlyphLayout(game.font, "VICTOIRE !");
        glyphLayout[1]=new GlyphLayout(game.font, "DEFAITE...");

        this.timer = new GamePhaseTimer();


    }// Eo constructor

    // LIBGDX METHODS

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

        // BACKGROUND

        drawBackground();

        // FINISHLINE

        drawFinishLine();

        // CHARACTER

        drawCharacter(currentFrame);

        // REAPER

        drawReaper();

        // VICTORY MESSAGE
        if (dog.getDogRectangle().overlaps(lvlBackground.getFinishRectangle())) {

            game.font.draw(game.batch, glyphLayout[0], camera.viewportWidth / 2 - (glyphLayout[0].width / 2), camera.viewportHeight / 2 + (glyphLayout[0].height / 2));

            this.hasWon = true;

        }

        // LOOSE MESSAGE
        if(timer.isWatching() && (playerXBuffer != dog.getDogRectangle().x || playerYBuffer != dog.getDogRectangle().y)){

            game.font.draw(game.batch, glyphLayout[1], camera.viewportWidth / 2 - (glyphLayout[1].width / 2), camera.viewportHeight / 2 + (glyphLayout[1 ].height / 2));

            this.hasLost = true;
        }

        game.batch.end();
        ////////////////

        timer.update(delta);

        // si x pos actuel != x pox précédent on actualise le buffer (controle pour flip)
        if(dog.getDogRectangle().getX() != dog.getxBuffer()) {
            dog.setxBuffer(dog.getDogRectangle().getX());
        }

        if(dog.getDogRectangle().getY() != dog.getyBuffer()) {
            dog.setyBuffer(dog.getDogRectangle().getY());
        }

        if(!this.hasWon) {
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
        lvlBackground.getFinishLine().dispose();
        dog.getDogImage().dispose();
        dog.getWalkSheet().dispose();
        reaper.getGreenTexture().dispose();
        reaper.getOrangeTexture().dispose();
        reaper.getRedTexture().dispose();
        game.batch.dispose();
    }

    // DRAW METHOD

    private void drawBackground() {
        game.batch.draw(
            lvlBackground.getBackgroundImage(),
            lvlBackground.getBackgroundRectangle().x,
            lvlBackground.getBackgroundRectangle().y,
            lvlBackground.getBackgroundRectangle().width,
            lvlBackground.getBackgroundRectangle().height
        );
    }

    private void drawFinishLine() {
        game.batch.draw(
            lvlBackground.getFinishLine(),
            lvlBackground.getFinishRectangle().x - 200,
            lvlBackground.getFinishRectangle().y,
            lvlBackground.getFinishRectangle().width + 200,
            lvlBackground.getFinishRectangle().height
        );
    }

    private void drawCharacter(TextureRegion currentFrame) {
        if(dog.getDogRectangle().getX() != dog.getxBuffer() || dog.getDogRectangle().getY() != dog.getyBuffer())  {
            game.batch.draw(
                currentFrame,
                dog.getDogRectangle().getX(),
                dog.getDogRectangle().getY(),
                dog.getDogRectangle().getWidth(),
                dog.getDogRectangle().getHeight()
            );

            frameBuffer = currentFrame; // dans le cas ou le personnage bouge on actualise le frameBuffer
            // pour pouvoir le figer quand il s'arrete.
        } else {
            game.batch.draw(
                frameBuffer,
                dog.getDogRectangle().getX(),
                dog.getDogRectangle().getY(),
                dog.getDogRectangle().getWidth(),
                dog.getDogRectangle().getHeight()
            );
        }
    }

    private void drawReaper() {
        if(timer.isNotWatching()) {
            game.batch.draw(
                    reaper.getGreenTexture(),
                    reaper.getRectangle().x,
                    reaper.getRectangle().y,
                    reaper.getRectangle().width,
                    reaper.getRectangle().height
            );

            this.playerXBuffer = dog.getDogRectangle().x;
            this.playerYBuffer = dog.getDogRectangle().y;

        } else if(timer.isReturning()) {
            game.batch.draw(
                    reaper.getOrangeTexture(),
                    reaper.getRectangle().x,
                    reaper.getRectangle().y,
                    reaper.getRectangle().width,
                    reaper.getRectangle().height
            );

            this.playerXBuffer = dog.getDogRectangle().x;
            this.playerYBuffer = dog.getDogRectangle().y;

        } else if(timer.isWatching()) {
            game.batch.draw(
                    reaper.getRedTexture(),
                    reaper.getRectangle().x,
                    reaper.getRectangle().y,
                    reaper.getRectangle().width,
                    reaper.getRectangle().height
            );
        }
    }

}// Eo GameScreen class
