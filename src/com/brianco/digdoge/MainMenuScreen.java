package com.brianco.digdoge;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class MainMenuScreen implements Screen {

	static final int SCREEN_WIDTH = 800;
	static final int SCREEN_HEIGHT = 480;

	private final MyGame game;

	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;
	private float downLineY;
	private float leftLineX;
	private float rightLineX;
	private Texture background;

	public MainMenuScreen(final MyGame gam) {
		game = gam;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

		shapeRenderer = game.shapeRenderer;
		downLineY = SCREEN_HEIGHT * 0.27f;
		leftLineX = SCREEN_WIDTH / 3f;
		rightLineX = SCREEN_WIDTH * 2f / 3f;

		background = new Texture(Gdx.files.internal("muchdoge.png"));

		game.font.setScale(3);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		//background
		game.batch.begin();
		game.batch.draw(background, 0, 0);
		game.batch.end();

		shapeRenderer.setProjectionMatrix(camera.combined);

		//lines
		Gdx.gl10.glLineWidth(10);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(92f / 255f, 150f / 255f, 1f, 1);
		shapeRenderer.line(0, downLineY, SCREEN_WIDTH, downLineY);
		shapeRenderer.line(leftLineX, downLineY, leftLineX, SCREEN_HEIGHT);
		shapeRenderer.line(rightLineX, downLineY, rightLineX, SCREEN_HEIGHT);
		shapeRenderer.end();
		//arrows
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.triangle(SCREEN_WIDTH / 6f, SCREEN_HEIGHT / 2f - 50,
				SCREEN_WIDTH / 6f, SCREEN_HEIGHT / 2f + 50,
				SCREEN_WIDTH / 6f - 100, SCREEN_HEIGHT / 2f);
		shapeRenderer.triangle(SCREEN_WIDTH * 5f / 6f, SCREEN_HEIGHT / 2f - 50,
				SCREEN_WIDTH * 5f / 6f, SCREEN_HEIGHT / 2f + 50,
				SCREEN_WIDTH * 5f / 6f + 100, SCREEN_HEIGHT / 2f);
		shapeRenderer.triangle(SCREEN_WIDTH / 2f - 50, SCREEN_HEIGHT / 4f,
				SCREEN_WIDTH / 2f + 50, SCREEN_HEIGHT / 4f,
				SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 4f - 100);
		shapeRenderer.end();

		//words
		game.batch.begin();
		game.font.setColor(1f, 222f / 255, 67f / 255f, 1f);
		game.font.draw(game.batch, "Dig Doge",
				SCREEN_WIDTH / 2f - game.font.getBounds("Dig Doge").width / 2f,
				SCREEN_HEIGHT * 4f / 5f);
		game.font.scale(1);
		game.font.setColor(1f, 37f / 255f, 167f / 255f, 1f);
		game.font.draw(game.batch, "Such Controls",
				SCREEN_WIDTH / 2f - 100, SCREEN_HEIGHT * 2f / 3f - 60);
		game.font.scale(-1);
		game.font.setColor(47f / 255f, 1f, 0f, 1f);
		game.font.draw(game.batch, "Wow Tap",
				SCREEN_WIDTH / 2f - 150, SCREEN_HEIGHT * 2f / 3f - 150);
		game.batch.end();

		if (Gdx.input.isTouched()) {
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
