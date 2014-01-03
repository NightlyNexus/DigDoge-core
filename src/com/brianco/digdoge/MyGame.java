package com.brianco.digdoge;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


public class MyGame extends Game {

	boolean isInGame;
	SpriteBatch batch;
	BitmapFont font;
	ShapeRenderer shapeRenderer;
	/*float explosionSize;
	private static final float DEFAULT_EXPLOSION_SIZE = 12;

	public MyGame() {
		this(DEFAULT_EXPLOSION_SIZE);
	}

	public MyGame(float exSize) {
		explosionSize = exSize;
	}*/

	/*public MyGame() {
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
		rainMusic.setLooping(true);
	}*/

	public MyGame() {
		
	}

	public void create() {
		isInGame = false;
		batch = new SpriteBatch();
		//Use LibGDX's default Arial font.
		font = new BitmapFont();
		shapeRenderer = new ShapeRenderer();
		this.setScreen(new MainMenuScreen(this));
	}

	public void render() {
		super.render(); //important!
	}
	
	public void dispose() {
		batch.dispose();
		font.dispose();
		shapeRenderer.dispose();
	}

	public boolean isInGame() {
		return isInGame;
	}
}
