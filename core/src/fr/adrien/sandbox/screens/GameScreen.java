package fr.adrien.sandbox.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import fr.adrien.sandbox.MyGame;
import fr.adrien.sandbox.bo.Player;
import fr.adrien.sandbox.bo.LevelBackground;
import fr.adrien.sandbox.bo.Reaper;
import fr.adrien.sandbox.controller.GamePhaseTimer;

public class GameScreen implements Screen {

    // ATTRIBUTES

    final MyGame game;
    public boolean hasWon;
    public boolean hasLost;
    public OrthographicCamera camera;
    private LevelBackground lvlBackground;
    private Player player;
    private final int PLAYER_HEIGHT = 200;
    private final int PLAYER_WIDTH = 150;
    private final int PLAYER_START_X = 150;
    private final int PLAYER_START_Y = 200;
    private float playerXBuffer;
    private float playerYBuffer;
    private Reaper reaper;
    private final int REAPER_SIZE = 250;
    private GlyphLayout glyphLayout[];
    private final String STONES_BACKGROUND_FILENAME = "textures/floor-tile.png";
    private TextureRegion frameBuffer ,
                          currentFrameCharacter,
                          currentFrameReaperNotWatching,
                          currentFrameReaperReturning,
                          currentFrameReaperWatching;
    private GamePhaseTimer timer;



    // CONSTRUCTOR

    public GameScreen(final MyGame game) {

        this.game = game;
        this.hasWon = false;
        this.frameBuffer = null;

        this.setGlyphLayouts();

        // create the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1600, 900);

//        this.lvlBackground = new LevelBackground(800, 1600, GRASS_BACKGROUND_FILENAME, camera.viewportHeight);
        this.lvlBackground = new LevelBackground(900, 1600, STONES_BACKGROUND_FILENAME, camera.viewportHeight);

        this.player = new Player(PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_START_X, Math.round(camera.viewportHeight / 2));

        this.reaper = new Reaper(REAPER_SIZE, REAPER_SIZE, 1300, Math.round(camera.viewportHeight / 2 - 150));

        this.glyphLayout = new GlyphLayout[3];

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

        Gdx.graphics.setWindowedMode(1366, 768);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // DRAW
        game.batch.begin();

        player.setStateTime();
        reaper.setStateTime();

        if (timer.isReturning()) {
            reaper.setReturningStateTime();
        }

        if (timer.isWatching()) {
            reaper.setWatchingStateTime();
        }

        getAnimationsFrames();

        // flip character sprite to look at movement direction
        player.flip();

        // BACKGROUND

        drawBackground();

        // FINISHLINE

        drawFinishLine();

        // CHARACTER

        drawCharacter(currentFrameCharacter);

        // REAPER

        drawReaper(currentFrameReaperNotWatching, currentFrameReaperReturning, currentFrameReaperWatching);

        // VICTORY MESSAGE TODO refacto condition to function
        if (player.getCharacterRec().overlaps(lvlBackground.getFinishRectangle())) {
            displayVictoryMsg();
        }

        // LOOSE MESSAGE TODO refacto condition to function
        if(timer.isWatching() && (playerXBuffer != player.getCharacterRec().x || playerYBuffer != player.getCharacterRec().y) || this.hasLost){
            displayLooseMsg();
        }

        game.batch.end();
        ////////////////

        timer.update(delta);

        // si x pos actuel != x pox précédent on actualise le buffer (controle pour flip)
        if(player.getCharacterRec().getX() != player.getxBuffer()) {
            player.setxBuffer(player.getCharacterRec().getX());
        }

        if(player.getCharacterRec().getY() != player.getyBuffer()) {
            player.setyBuffer(player.getCharacterRec().getY());
        }

        if(!this.hasWon && !this.hasLost) {
            player.move();
        }

        if (this.hasLost) {
            restartLVL();
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
        player.getWalkSheet().dispose();
        reaper.getNotWatchingSheet().dispose();
        reaper.getReturningSheet().dispose();
        reaper.getWatchingSheet().dispose();
        game.batch.dispose();
    }

    // FRAMES METHODS

    private void getAnimationsFrames(){

        currentFrameCharacter = player.getWalkAnimation().getKeyFrame(player.getStateTime(), true);

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
                lvlBackground.getImgTextureRegion(),
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
        if(player.getCharacterRec().getX() != player.getxBuffer() || player.getCharacterRec().getY() != player.getyBuffer())  {
            game.batch.draw(
                currentFrame,
                player.getCharacterRec().getX(),
                player.getCharacterRec().getY(),
                player.getCharacterRec().getWidth(),
                player.getCharacterRec().getHeight()
            );

            frameBuffer = currentFrame; // dans le cas ou le personnage bouge on actualise le frameBuffer
            // pour pouvoir le figer quand il s'arrete.
        } else {
            game.batch.draw(
                frameBuffer,
                player.getCharacterRec().getX(),
                player.getCharacterRec().getY(),
                player.getCharacterRec().getWidth(),
                player.getCharacterRec().getHeight()
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

            this.playerXBuffer = player.getCharacterRec().x;
            this.playerYBuffer = player.getCharacterRec().y;

            reaper.resetReturningStateTime();


        } else if(timer.isReturning()) {
            game.batch.draw(
                    returningFrame,
                    reaper.getRectangle().x,
                    reaper.getRectangle().y,
                    reaper.getRectangle().width,
                    reaper.getRectangle().height
            );

            this.playerXBuffer = player.getCharacterRec().x;
            this.playerYBuffer = player.getCharacterRec().y;

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

    private void setGlyphLayouts() {
        //TODO bug on glyphLayout init
        this.glyphLayout = new GlyphLayout[3];

        this.glyphLayout[0]=new GlyphLayout(game.font, "VICTOIRE !");
        this.glyphLayout[1]=new GlyphLayout(game.font, "DEFAITE...");
        this.glyphLayout[2]=new GlyphLayout(game.font, "Appuyez sur espace [___] pour recommencer");
    }

    private void displayLooseMsg() {
        game.font.draw(game.batch, glyphLayout[1], this.viewportWidthCenter() - (glyphLayout[1].width / 2), camera.viewportHeight / 2 + (glyphLayout[1 ].height / 2));

        game.font.draw(game.batch, glyphLayout[2], this.viewportWidthCenter() - (glyphLayout[1].width / 2), camera.viewportHeight / 2 + (glyphLayout[1 ].height / 2));

        this.hasLost = true;
    }// Eo displayLooseMsg()

    private void displayVictoryMsg() {

        game.font.draw(game.batch, this.glyphLayout[0], this.viewportWidthCenter() - (glyphLayout[0].width / 2), this.viewportHeightCenter() + (glyphLayout[0].height / 2));

        this.hasWon = true;

    }// Eo displayVictoryMsg()

    // I/O METHODS

    private void restartLVL() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            System.out.println("Pressing space-bar");

            this.hasLost = false;

            timer.setRandomTimer();

            player.getCharacterRec().setX(PLAYER_START_X);

            // to fix the player looking on wrong direction on respawn. (associate to PLayer.flip() conditions)
            player.setxBuffer(player.getCharacterRec().x - 1);

            player.getCharacterRec().setY(Math.round(this.viewportHeightCenter()));
        }
    }// Eo restartLVL()

    // UTILS METHODS

    private float viewportWidthCenter() {

        return this.viewportWidthCenter() ;

    }// Eo viewportWidthCenter()

    private float viewportHeightCenter() {

        return camera.viewportHeight / 2 ;

    }// Eo viewportHeightCenter()



}// Eo GameScreen class
