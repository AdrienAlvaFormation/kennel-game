package fr.adrien.sandbox.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import fr.adrien.sandbox.MyGame;
import fr.adrien.sandbox.bo.Player;
import fr.adrien.sandbox.bo.LevelBackground;
import fr.adrien.sandbox.bo.Reaper;
import fr.adrien.sandbox.bo.SpeedBoost;
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

    private SpeedBoost speedBoost;
    private GlyphLayout glyphLayout[];
    public BitmapFont fontWinLooseTitle;
    public BitmapFont fontUnderTextLoose;
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

        // create the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1600, 900);

        // BO

        this.lvlBackground = new LevelBackground(900, 1600, STONES_BACKGROUND_FILENAME, camera.viewportHeight);

        this.player = new Player(PLAYER_HEIGHT, PLAYER_WIDTH, PLAYER_START_X, Math.round(this.viewportHeightCenter() - 100));

        this.reaper = new Reaper(REAPER_SIZE, REAPER_SIZE, 1300, Math.round(this.viewportHeightCenter() - 150));

        this.speedBoost = new SpeedBoost();

        /* */

        this.glyphLayout = new GlyphLayout[3];

        game.getFontParameter().size = 100;
        fontWinLooseTitle = game.getGenerator().generateFont(game.getFontParameter());
        game.getFontParameter().size = 50;
        fontUnderTextLoose = game.getGenerator().generateFont(game.getFontParameter());

        this.setGlyphLayouts();

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
        game.getBatch().setProjectionMatrix(camera.combined);

        // DRAW
        game.getBatch().begin();

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

        // SPEEDBOOST

        if (!this.speedBoost.isConsume()) {
            drawSpeedBoost();

            if (player.getCharacterRec().overlaps(speedBoost.getBoostRec())) {
                this.speedBoost.setConsume(true);
                this.player.setPlayerSpeed(600);
                this.player.setFrameDuration(0.0125f);
                this.player.loadAnimation();
            }
        }

        // VICTORY MESSAGE TODO refacto condition to function
        if (player.getCharacterRec().overlaps(lvlBackground.getFinishRectangle())) {
            displayVictoryMsg();
        }

        // LOOSE MESSAGE TODO refacto condition to function
        if(timer.isWatching() && (playerXBuffer != player.getCharacterRec().x || playerYBuffer != player.getCharacterRec().y) || this.hasLost){
            displayLooseMsg();
        }

        game.getBatch().end();
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

        if (this.hasLost || this.hasWon) {
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
        game.getBatch().dispose();
        this.fontWinLooseTitle.dispose();
        this.fontUnderTextLoose.dispose();
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

        game.getBatch().draw(
                lvlBackground.getImgTextureRegion(),
                lvlBackground.getBackgroundRectangle().x,
                lvlBackground.getBackgroundRectangle().y,
                lvlBackground.getBackgroundRectangle().width,
                lvlBackground.getBackgroundRectangle().height
        );
    }

    private void drawFinishLine() {
        game.getBatch().draw(
            lvlBackground.getFinishLine(),
            lvlBackground.getFinishRectangle().x - 200,
            lvlBackground.getFinishRectangle().y,
            lvlBackground.getFinishRectangle().width + 200,
            lvlBackground.getFinishRectangle().height
        );
    }

    private void drawCharacter(TextureRegion currentFrame) {
        if(player.getCharacterRec().getX() != player.getxBuffer() || player.getCharacterRec().getY() != player.getyBuffer())  {
            game.getBatch().draw(
                currentFrame,
                player.getCharacterRec().getX(),
                player.getCharacterRec().getY(),
                player.getCharacterRec().getWidth(),
                player.getCharacterRec().getHeight()
            );

            frameBuffer = currentFrame; // dans le cas ou le personnage bouge on actualise le frameBuffer
            // pour pouvoir le figer quand il s'arrete.
        } else {
            game.getBatch().draw(
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
            game.getBatch().draw(
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
            game.getBatch().draw(
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
            game.getBatch().draw(
                    watchingFrame,
                    reaper.getRectangle().x,
                    reaper.getRectangle().y,
                    reaper.getRectangle().width,
                    reaper.getRectangle().height
            );
        }
    }// Eo drawReaper()

    private void drawSpeedBoost() {

        game.getBatch().draw(
                speedBoost.getBoostTexture(),
                speedBoost.getBoostRec().x,
                speedBoost.getBoostRec().y,
                speedBoost.getBoostRec().width,
                speedBoost.getBoostRec().height
        );

    }// Eo drawSpeedBoost()

    // MESSAGE DISPLAY

    private void setGlyphLayouts() {

        this.glyphLayout = new GlyphLayout[3];

        this.glyphLayout[0]=new GlyphLayout(fontWinLooseTitle, "VICTOIRE !");
        this.glyphLayout[1]=new GlyphLayout(fontWinLooseTitle, "DEFAITE...");
        this.glyphLayout[2]=new GlyphLayout(fontUnderTextLoose, "Appuyez sur espace [___] pour recommencer");
    }

    private void displayLooseMsg() {

        fontWinLooseTitle.draw(game.getBatch(), glyphLayout[1], this.viewportWidthCenter() - (glyphLayout[1].width / 2), this.viewportHeightCenter() + (glyphLayout[1].height / 2));

        fontUnderTextLoose.draw(game.getBatch(), glyphLayout[2], this.viewportWidthCenter() - (glyphLayout[2].width / 2), this.viewportHeightCenter() + (glyphLayout[2].height / 2) - 150);

        this.hasLost = true;
    }// Eo displayLooseMsg()

    private void displayVictoryMsg() {

        fontWinLooseTitle.draw(game.getBatch(), this.glyphLayout[0], this.viewportWidthCenter() - (glyphLayout[0].width / 2), this.viewportHeightCenter() + (glyphLayout[0].height / 2));

        this.hasWon = true;

    }// Eo displayVictoryMsg()

    // RESET

    private void restartLVL() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            System.out.println("Pressing space-bar");

            this.hasLost = false;
            this.hasWon = false;

            timer.setRandomTimer();

            resetPlayer();

            reloadBoosts();
        }
    }// Eo restartLVL()

    private void resetPlayer() {

        player.getCharacterRec().setX(PLAYER_START_X);

        // to fix the player looking on wrong direction on respawn. (associate to PLayer.flip() conditions)
        player.setxBuffer(player.getCharacterRec().x - 1);

        player.getCharacterRec().setY(Math.round(this.viewportHeightCenter()));

        player.setPlayerSpeed(300);

    }// Eo resetPlayer()

    private void reloadBoosts() {

        this.speedBoost.setConsume(false);

        this.speedBoost.setBoostPos();

    }// Eo reloadBoosts()

    // UTILS METHODS

    private float viewportWidthCenter() {

        return this.camera.viewportWidth / 2 ;

    }// Eo viewportWidthCenter()

    private float viewportHeightCenter() {

        return this.camera.viewportHeight / 2 ;

    }// Eo viewportHeightCenter()



}// Eo GameScreen class
