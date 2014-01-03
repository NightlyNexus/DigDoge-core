package com.brianco.digdoge;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class DirtTile{
	/**
	 * 
	 */
	Texture image;
	boolean shouldDraw;
	boolean isCoin;
	int x;
	int y;

	public DirtTile(int x, int y) {
		shouldDraw = true;
		this.x = x;
		this.y = y;
		String fileName;
		if (1 == MathUtils.random(1, 10)){
			fileName = "coin.png";
			isCoin = true;
		} else {
			switch (MathUtils.random(1, 4)) {
				case 1: fileName = "tile1.png"; break;
				case 2: fileName = "tile2.png"; break;
				case 3: fileName = "tile3.png"; break;
				default: fileName = "tile4.png"; break;
			}
			isCoin = false;
		}
		image = new Texture(Gdx.files.internal(fileName));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (!(o instanceof DirtTile))
			return false;
		DirtTile other = (DirtTile) o;
		return this.x == other.x && this.y == other.y;
	}
}
