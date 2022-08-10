package fr.adrien.sandbox.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import fr.adrien.sandbox.Sandbox;
import fr.adrien.sandbox.bo.Character;
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
    private Character character;
    private float playerXBuffer;
    private float playerYBuffer;
    private Reaper reaper;
    private GlyphLayout glyphLayout[];
    private final String GRASS_BACKGROUND_FILENAME = "grass.png";
    private TextureRegion frameBuffer ,
                          currentFrameCharacter,
                          currentFrameReaperNotWatching,
                          currentFrameReaperReturning,
                          currentFrameReaperWatching;
    private GamePhaseTimer timer;



    // CONSTRUCTOR

    public GameScreen(final Sandbox game) {

        this.game = game;
        this.hasWon = false;
        this.frameBuffer = null;

        // create the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1600, 800);

        this.lvlBackground = new LevelBackground(800, 1600, GRASS_BACKGROUND_FILENAME, camera.viewportHeight);

        this.character = new Character(200, 150, 100, Math.round(camera.viewportHeight / 2));

        this.reaper = new Reaper(300, 300, 1300, Math.round(camera.viewportHeight / 2 - 150));

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

        character.setStateTime();
        reaper.setStateTime();

        if (timer.isReturning()) {
            reaper.setReturningStateTime();
        }

        if (timer.isWatching()) {
            reaper.setWatchingStateTime();
        }

        getAnimationsFrames();

        // flip character sprite to look at movement direction
        character.flip();

        // BACKGROUND

        drawBackground();

        // FINISHLINE

        drawFinishLine();

        // CHARACTER

        drawCharacter(currentFrameCharacter);

        // REAPER

        drawReaper(currentFrameReaperNotWatching, currentFrameReaperReturning, currentFrameReaperWatching);

        // VICTORY MESSAGE TODO refacto condition to function
        if (character.getCharacterRec().overlaps(lvlBackground.getFinishRectangle())) {
            displayVictoryMsg();
        }

        // LOOSE MESSAGE TODO refacto condition to function
        if(timer.isWatching() && (playerXBuffer != character.getCharacterRec().x || playerYBuffer != character.getCharacterRec().y)){
            displayLooseMsg();
        }

        game.batch.end();
        ////////////////

        timer.update(delta);

        // si x pos actuel != x pox précédent on actualise le buffer (controle pour flip)
        if(character.getCharacterRec().getX() != character.getxBuffer()) {
            character.setxBuffer(character.getCharacterRec().getX());
        }

        if(character.getCharacterRec().getY() != character.getyBuffer()) {
            character.setyBuffer(character.getCharacterRec().getY());
        }

        if(!this.hasWon && !this.hasLost) {
            character.move();
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
        character.getWalkSheet().dispose();
        reaper.getNotWatchingSheet().dispose();
        reaper.getGreenTexture().dispose();
        reaper.getOrangeTexture().dispose();
        reaper.getRedTexture().dispose();
        game.batch.dispose();
    }

    // FRAMES METHODS

    private void getAnimationsFrames(){

        currentFrameCharacter = character.getWalkAnimation().getKeyFrame(character.getStateTime(), true);

        currentFrameReaperNotWatching = reaper.getNotWatchingAnimation().getKeyFrame(reaper.getNotWatchingStateTime(), true);

        currentFrameReaperReturning = null;

        currentFrameReaperWatching = null;

        if (timer.isReturning()) {
            currentFrameReaperReturning = reaper.getReturningAnimation().getKeyFrame(reaper.getReturningStateTime(), true);
        }

        if (timer.isWatching()) {
            currentFrameReaperWatching = reaper.getWatchingAnimation().getKeyFrame(reaper.getWatchingStateTime(), true);
        }

        if (frameBuffer == null) {
            frameBuffer = currentFrameCharacter;
        }

    }// Eo of getAnimationsFrames()

    // DRAW METHODS

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
        if(character.getCharacterRec().getX() != character.getxBuffer() || character.getCharacterRec().getY() != character.getyBuffer())  {
            game.batch.draw(
                currentFrame,
                character.getCharacterRec().getX(),
                character.getCharacterRec().getY(),
                character.getCharacterRec().getWidth(),
                character.getCharacterRec().getHeight()
            );

            frameBuffer = currentFrame; // dans le cas ou le personnage bouge on actualise le frameBuffer
            // pour pouvoir le figer quand il s'arrete.
        } else {
            game.batch.draw(
                frameBuffer,
                character.getCharacterRec().getX(),
                character.getCharacterRec().getY(),
                character.getCharacterRec().getWidth(),
                character.getCharacterRec().getHeight()
            );
        }
    }

    private void drawReaper(TextureRegion notWatchingFrame, TextureRegion returningFrame, TextureRegion watchingFrame) {
        if(timer.isNotWatching()) {
            game.batch.draw(
                    notWatchingFrame,
                    reaper.getRectangle().x,
                    reaper.getRectangle().y,
                    reaper.getRectangle().width,
                    reaper.getRectangle().height
            );

            this.playerXBuffer = character.getCharacterRec().x;
            this.playerYBuffer = character.getCharacterRec().y;

            reaper.resetReturningStateTime();


        } else if(timer.isReturning()) {
            game.batch.draw(
                    returningFrame,
                    reaper.getRectangle().x,
                    reaper.getRectangle().y,
                    reaper.getRectangle().width,
                    reaper.getRectangle().height
            );

            this.playerXBuffer = character.getCharacterRec().x;
            this.playerYBuffer = character.getCharacterRec().y;

            reaper.resetWatchingStateTime();

        } else if(timer.isWatching()) {
            game.batch.draw(
                    watchingFrame,
                    reaper.getRectangle().x,
                    reaper.getRectangle().y,
                    reaper.getRectangle().width,
                    reaper.getRectangle().height
            );
        }
    }// Eo drawReaper()

    // MESSAGE DISPLAY

    private void displayLooseMsg() {
        game.font.draw(game.batch, glyphLayout[1], camera.viewportWidth / 2 - (glyphLayout[1].width / 2), camera.viewportHeight / 2 + (glyphLayout[1 ].height / 2));

        this.hasLost = true;
    }// Eo displayLooseMsg()

    private void displayVictoryMsg() {

        game.font.draw(game.batch, glyphLayout[0], camera.viewportWidth / 2 - (glyphLayout[0].width / 2), camera.viewportHeight / 2 + (glyphLayout[0].height / 2));

        this.hasWon = true;

    }// Eo displayVictoryMsg()

}// Eo GameScreen class
