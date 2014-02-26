package com.brianco.digdoge;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class DogeDig implements Screen, GestureListener {
	static final int SCREEN_WIDTH = 800;
	static final int SCREEN_HEIGHT = 480;
	private static final int GAME_TIME = 60;//seconds
	private Array<DirtTile> dirt;
	private int lastX;
	private int lastY;
	private int dogeX;
	private int dogeY;
	private Texture dogeImage;
	private DirtTile tempDogePosDirt;
	private DirtTile dogeHere;
	private boolean lastRight;
	private Sound fireSound;
	private OrthographicCamera camera;
	private SpriteBatch batch;
//	private Array<City> cities;
	private int coinsCollected;
	private MyGame game;
	private ShapeRenderer shapeRenderer;
	private boolean touch0;
	private boolean touch1;
	private boolean touch2;
	private int time;
	private int lastSillyDrawn;
	private RandomText rt;
	
	/*private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;*/
	
	public DogeDig(MyGame gam) {
		Gdx.input.setInputProcessor(new GestureDetector(this));
		game = gam;
		coinsCollected = 0;
		create();
		
		//reloading stuff
		time = GAME_TIME;
		Timer.schedule(new Task(){
		    @Override
		    public void run() {
		    	time--;
		    	if (time == 0) {
					game.setScreen(new Lose(game, coinsCollected));
			    	dispose();
		    	}
		        Timer.schedule(this, 1);
		    }
		}, 1);
	}
	
	
	private void create() {
		if (Gdx.input.isTouched()){
			touch0 = true;
			touch1 = true;
			touch2 = true;
		} else {
			touch0 = false;
			touch1 = false;
			touch2 = false;
		}
		shapeRenderer = game.shapeRenderer;
		// load the images for the droplet and the bucket, 64x64 pixels each
		//cityImage = new Texture(Gdx.files.internal("bucket.png"));
		
		dirt = new Array<DirtTile>();
		for (int i = 0; i < SCREEN_WIDTH + 64; i += 64) {
			for (int j = SCREEN_HEIGHT; j > -64; j -= 64) {
				dirt.add(new DirtTile(i, j));
				lastX = i;
				lastY = j;
			}
		}

		int numXTilesOnScreen = MathUtils.ceil(SCREEN_WIDTH / 64f);
		int numYTilesOnScreen = MathUtils.ceil(SCREEN_HEIGHT / 64f);
		dogeX = (numXTilesOnScreen / 2 - 1) * 64;
		dogeY = (numYTilesOnScreen / 4) * 64;//remember to SUBTRACT this from the top
		dogeImage = new Texture(Gdx.files.internal("doge.png"));
		lastRight = false;

		lastSillyDrawn = 0;

		game.font.setScale(1);

		// load the drop sound effect and the rain background "music"
		fireSound = Gdx.audio.newSound(Gdx.files.internal("coincollect.wav"));
		//rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
		
		// start the playback of the background music immediately
		//rainMusic.setLooping(true);
		//rainMusic.play();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		//batch = new SpriteBatch();
		batch = game.batch;
		
		
		
		
	}

	@Override
	public void dispose() {
		game.isInGame = false;
		for (DirtTile dirtyTile : dirt) {
			dirtyTile.image.dispose();
		}
//		for (City city : cities) {
//		    city.image.dispose();
//		}
	    //cityImage.dispose();
	    fireSound.dispose();
	    //rainMusic.dispose();
	    //batch.dispose();
		
		/*batch.dispose();
		texture.dispose();*/
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		/*if(Gdx.input.isTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			Iterator<Line> iter = lines.iterator();
			while(iter.hasNext()) {
			   Line line = iter.next();
			   if (Math.abs(touchPos.x - line.x2) <=5 || Math.abs(touchPos.y - line.y2) <= 5) {
					iter.remove();
			   }
			}
		}*/
		
		
		
		
		if (Gdx.input.isTouched(0)){
			if (!touch0) {
				float x = Gdx.input.getX(0);
	            float y = Gdx.input.getY(0);
	    		x = x / Gdx.graphics.getWidth() * SCREEN_WIDTH;
	    		y = y / Gdx.graphics.getHeight() * SCREEN_HEIGHT;
	    		y = SCREEN_HEIGHT - y;//stupid, stupid inconsistency
	            if (y <= SCREEN_HEIGHT * 0.27f) {
	            	moveDown();
	            } else if (x <= SCREEN_WIDTH / 3f) {
	            	moveLeft();
	            } else if (x >= SCREEN_WIDTH * 2f / 3f) {
	            	moveRight();
	            }
			}
			touch0 = true;
		} else if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT)) {
			System.out.println("left");
			if (!touch0) {
				moveLeft();
			}
			touch0 = true;
		} else if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT)) {
			System.out.println("right");
			if (!touch0) {
				moveRight();
			}
			touch0 = true;
		} else if (Gdx.input.isKeyPressed(Keys.DPAD_DOWN)) {
			System.out.println("down");
			if (!touch0) {
				moveDown();
			}
			touch0 = true;
		} else {
			touch0 = false;
		}

		if (Gdx.input.isTouched(1)){
			if (!touch1) {
				float x = Gdx.input.getX(1);
	            float y = Gdx.input.getY(1);
//	            fireMissile(x, y);
			}
			touch1 = true;
		} else {
			touch1 = false;
		}
		
		if (Gdx.input.isTouched(2)){
			if (!touch2) {
				float x = Gdx.input.getX(2);
	            float y = Gdx.input.getY(2);
//	            fireMissile(x, y);
			}
			touch2 = true;
		} else {
			touch2 = false;
		}
		
		
		
		//your game logic goes here, Eric
		
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		//game.font.draw(game.batch, "" + game.location, SCREEN_WIDTH / 2 - 50, 200);
//		for (City city : cities) {
//			game.font.draw(game.batch, "" + city.numMissiles, city.x + 16, 100);
//			batch.draw(city.image, city.x, city.y);
//		}
		int bottomLeftY = (int) camera.position.y - SCREEN_HEIGHT / 2;
		int bottomLeftX = (int) camera.position.x - SCREEN_WIDTH / 2;
		//drawing doge
		int currentAbsoluteDogeX = bottomLeftX + dogeX;
		int currentAbsoluteDogeY = bottomLeftY + SCREEN_HEIGHT - dogeY;
		tempDogePosDirt = new DirtTile(currentAbsoluteDogeX, currentAbsoluteDogeY);
		int index = dirt.indexOf(tempDogePosDirt, false);
		tempDogePosDirt.image.dispose();
		dogeHere = dirt.get(index);
		if (dogeHere.isCoin && dogeHere.shouldDraw) {
			coinsCollected++;
			fireSound.play();
		}
		dogeHere.shouldDraw = false;
		batch.draw(dogeImage, currentAbsoluteDogeX, currentAbsoluteDogeY);
		//drawing tiles
		for (DirtTile dirtyTile : dirt) {
			if (dirtyTile.x - bottomLeftX <= SCREEN_WIDTH
					&& dirtyTile.y - bottomLeftY <= SCREEN_HEIGHT
					&& dirtyTile.shouldDraw) {
				batch.draw(dirtyTile.image, dirtyTile.x, dirtyTile.y);
			}
		}
		//draw silly text
		if (lastSillyDrawn >= 100) {
			rt = new RandomText(SCREEN_WIDTH, SCREEN_HEIGHT);
			lastSillyDrawn = 0;
		}
		if (rt != null && rt.text.length() > 0) {
			game.font.scale(rt.scaleSize);
			game.font.setColor(rt.r, rt.g, rt.b, rt.alpha);
			game.font.draw(game.batch, rt.text,
					bottomLeftX + rt.x, bottomLeftY + rt.y);
			game.font.scale(-rt.scaleSize);
		}
		lastSillyDrawn++;
		//drawing text
		game.font.setColor(1f, 1f, 1f, 1f);
		game.font.draw(game.batch, "Much Coin: " + coinsCollected,
				bottomLeftX, bottomLeftY + SCREEN_HEIGHT);
		game.font.draw(game.batch, "So End in " + time,
				bottomLeftX, bottomLeftY + SCREEN_HEIGHT - 20);
		batch.end();
		
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(1, 1, 0, 1);
//		for(Line line: lines) {
//			shapeRenderer.line(line.x1, 480, line.x2, line.y2);
//		}
		shapeRenderer.setColor(1, 1, 1, 1);
//		for(Missile missile : missiles) {
//			shapeRenderer.line(missile.x1, missile.y1, missile.x2, missile.y2);
//		}
		shapeRenderer.end();
		
		/*Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		sprite.draw(batch);
		batch.end();*/
	}

	private void moveRight() {
		camera.position.x += 64;
		int bottomLeftY = (int) camera.position.y - SCREEN_HEIGHT / 2;
		if (!lastRight) {
			dogeImage = new Texture(Gdx.files.internal("doge2.png"));
		}
		lastRight = true;
		for (int j = bottomLeftY + SCREEN_HEIGHT;
				j > bottomLeftY - 64; j -= 64) {
			DirtTile tempDirt = new DirtTile(lastX + 64, j);
			if (!dirt.contains(tempDirt, false)){
				dirt.add(tempDirt);
			} else {
				tempDirt.image.dispose();
			}
		}
		lastX += 64;
	}

	private void moveLeft() {
		camera.position.x -= 64;
		int bottomLeftY = (int) camera.position.y - SCREEN_HEIGHT / 2;
		int bottomLeftX = (int) camera.position.x - SCREEN_WIDTH / 2;
		if (lastRight) {
			dogeImage = new Texture(Gdx.files.internal("doge.png"));
		}
		lastRight = false;
		for (int j = bottomLeftY + SCREEN_HEIGHT;
				j > bottomLeftY - 64; j -= 64) {
			DirtTile tempDirt = new DirtTile(bottomLeftX, j);
			if (!dirt.contains(tempDirt, false)){
				dirt.add(tempDirt);
			} else {
				tempDirt.image.dispose();
			}
		}
		lastX -= 64;
	}

	private void moveDown() {
		camera.position.y -= 64;
		int bottomLeftX = (int) camera.position.x - SCREEN_WIDTH / 2;
		//TODO: move doge down
		for (int i = bottomLeftX;
				i < bottomLeftX + SCREEN_WIDTH + 64; i += 64) {
			DirtTile tempDirt = new DirtTile(i, lastY - 64);
			if (!dirt.contains(tempDirt, false)){
				dirt.add(tempDirt);
			} else {
				tempDirt.image.dispose();
			}
		}
		lastY -= 64;
	}

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
	public void show() {
		// TODO Auto-generated method stub
		//rainMusic.play();
		game.isInGame = true;
		
	}


	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean tap(float x, float y, int count, int button) {
		/*fireMissile(x, y);*/
		/*Iterator<Line> iter = lines.iterator();
		while(iter.hasNext()) {
			Line line = iter.next();
			if (Math.abs(x - line.x2) <= 5 || Math.abs(y - line.y2) <= 5) {
				iter.remove();
			}
		}*/
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}
}

/*public class Guardian implements Screen {
	private Texture dropImage;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Rectangle bucket;
	private Vector3 touchPos = new Vector3();
	private Array<Rectangle> raindrops;
	private long lastDropTime;
	private int dropsGathered;
	private MyGame game;
	
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	
	public Guardian(MyGame gam) {
		game = gam;
		dropsGathered = 0;
		create();
	}
	
	
	private void create() {
		// load the images for the droplet and the bucket, 64x64 pixels each
		dropImage = new Texture(Gdx.files.internal("droplet.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));
		
		// load the drop sound effect and the rain background "music"
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
		
		// start the playback of the background music immediately
		rainMusic.setLooping(true);
		//rainMusic.play();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		
		//batch = new SpriteBatch();
		batch = game.batch;
		
		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2;
		bucket.y = 20;
		bucket.width = 64;
		bucket.height = 64;
		
		raindrops = new Array<Rectangle>();
		spawnRaindrop();
		
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(1, h/w);
		batch = new SpriteBatch();
		
		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);
		
		sprite = new Sprite(region);
		sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
	}

	@Override
	public void dispose() {
		dropImage.dispose();
	    bucketImage.dispose();
	    dropSound.dispose();
	    rainMusic.dispose();
	    //batch.dispose();
		
		batch.dispose();
		texture.dispose();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if(Gdx.input.isTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64 / 2;
		}
		
		if(Gdx.input.isKeyPressed(Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();
		
		if(bucket.x < 0) bucket.x = 0;
		if(bucket.x > 800 - 64) bucket.x = 800 - 64;
		
		if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();
		
		Iterator<Rectangle> iter = raindrops.iterator();
		while(iter.hasNext()) {
		   Rectangle raindrop = iter.next();
		   raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
		   if(raindrop.y + 64 < 0) iter.remove();
		   //if drop hits bucket
		   if(raindrop.overlaps(bucket)) {
			  dropsGathered++;
		      dropSound.play();
		      iter.remove();
		   }
		}
		
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, 480);
		batch.draw(bucketImage, bucket.x, bucket.y);
		for(Rectangle raindrop: raindrops) {
		   batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		batch.end();
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		sprite.draw(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800-64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}


	@Override
	public void show() {
		// TODO Auto-generated method stub
		rainMusic.play();
		
	}


	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
}*/
