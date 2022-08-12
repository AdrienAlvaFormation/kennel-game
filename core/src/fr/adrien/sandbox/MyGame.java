package fr.adrien.sandbox;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class MyGame extends Game {

	private SpriteBatch batch;
	private FreeTypeFontGenerator generator;
	private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;

	public void create() {
		batch = new SpriteBatch();

		generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Oswald-Regular.ttf"));
		fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

		this.setScreen(new fr.adrien.sandbox.screens.GameScreen(this));
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		batch.dispose();
		generator.dispose();
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public FreeTypeFontGenerator getGenerator() {
		return generator;
	}

	public FreeTypeFontGenerator.FreeTypeFontParameter getFontParameter() {
		return fontParameter;
	}


}// Eo Sandbox
