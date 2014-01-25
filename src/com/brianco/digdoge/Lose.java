package com.brianco.digdoge;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Lose implements Screen {
	static final int SCREEN_WIDTH = 800;
	static final int SCREEN_HEIGHT = 480;
	private MyGame game;
	private int score;
	private int highScore;
	private boolean winner;
	private OrthographicCamera camera;
	private Texture background;
	private boolean displayTap;
	private RandomText rtLastScore;
	private RandomText rtTap;
	private RandomText rtHighScore;
	private RandomText rtWinner;
	private float scale;
	
	public Lose(MyGame gam, int coinsCollected) {
		game = gam;
		score = coinsCollected;
		displayTap = false;

		//high score
		Preferences prefs = Gdx.app.getPreferences("highscore");
		highScore = prefs.getInteger("coins", 0);
		if (score > highScore) {
			prefs.putInteger("coins", score);
			prefs.flush();
			highScore = score;
			winner = true;
		} else {
			winner = false;
		}
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

		background = new Texture(Gdx.files.internal("muchcoin.png"));

		//assigning and making sure no same colors
		rtLastScore = new RandomText(250, 100);
		rtTap = new RandomText(250, 100);
		while (rtTap.equals(rtLastScore)) {
			rtTap = new RandomText(250, 100);
		}
		rtHighScore = new RandomText(250, 100);
		while (rtHighScore.equals(rtLastScore) || rtHighScore.equals(rtTap)) {
			rtHighScore = new RandomText(250, 100);
		}
		rtWinner = new RandomText(250, 100);
		while (rtWinner.equals(rtLastScore) || rtWinner.equals(rtTap)
				|| rtWinner.equals(rtHighScore)) {
			rtWinner = new RandomText(250, 100);
		}

		//until retry
		Timer.schedule(new Task(){
			@Override
			public void run() {
				displayTap = true;
			}
		}, 3);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		//backgound
		game.batch.draw(background, 0, 0);
		//last score
		scale = rtLastScore.scaleSize;
		game.font.scale(scale);
		game.font.setColor(rtLastScore.r, rtLastScore.g, rtLastScore.b, 1f);
		game.font.draw(game.batch, "Much Collect: " + score,
				SCREEN_WIDTH / 5f,
				SCREEN_HEIGHT * 2f / 3f);
		game.font.scale(-scale);
		//retry
		if (displayTap) {
			scale = rtTap.scaleSize;
			game.font.scale(scale);
			game.font.setColor(rtTap.r, rtTap.g, rtTap.b, 1f);
			game.font.draw(game.batch, "So Tap; Must Retry!",
				SCREEN_WIDTH / 2f - game.font.getBounds("So Tap; Must Retry!").width / 2f,
				SCREEN_HEIGHT / 4f);
			game.font.scale(-scale);
		}
		//high score
		scale = rtHighScore.scaleSize;
		game.font.scale(scale);
		game.font.setColor(rtHighScore.r, rtHighScore.g, rtHighScore.b, 1f);
		game.font.draw(game.batch, "Most High: " + highScore,
				SCREEN_WIDTH * 3f / 5f,
				SCREEN_HEIGHT * 2f / 5f);
		game.font.scale(-scale);
		//winner
		if (winner) {
			scale = rtWinner.scaleSize;
			game.font.scale(scale);
			game.font.setColor(rtWinner.r, rtWinner.g, rtWinner.b, 1f);
			game.font.draw(game.batch, "WINNER -- New High Score!", 75, 50);
			game.font.scale(-scale);
		}
		game.batch.end();

		if ((Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.DPAD_DOWN)) && displayTap) {
			game.setScreen(new DogeDig(game));
			dispose();
		}
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		background.dispose();
	}
}
