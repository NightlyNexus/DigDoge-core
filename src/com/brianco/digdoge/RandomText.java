package com.brianco.digdoge;

import com.badlogic.gdx.math.MathUtils;

public class RandomText {

	String text;
	int x;
	int y;
	int scaleSize;
	float r;
	float g;
	float b;
	float alpha;

	public RandomText(int width, int height) {
		int num = MathUtils.random(1, 25);
		switch (num) {
			case 1: text = "So Fun"; break;
			case 2: text = "Wow"; break;
			case 3: text = "Much Dirt"; break;
			case 4: text = "So Dig"; break;
			case 5: text = "Very Brown"; break;
			case 6: text = "Fast Mine"; break;
			case 7: text = "Much Tap"; break;
			case 8: text = "Many Finger"; break;
			case 9: text = "So Fast"; break;
			case 10: text = "Go Quick"; break;
			case 11: text = "Such Find"; break;
			case 12: text = "No Jump"; break;
			case 13: text = "Such Doge"; break;
			case 14: text = "Such Speed"; break;
			case 15: text = "Much Coin"; break;
			case 16: text = "To Da Moon!"; break;
			default: text = ""; break;
		}
		num = MathUtils.random(1, 6);
		switch (num) {
			case 1: r = 1f; g = 1f; b = 0f; break;
			case 2: r = 1f; g = 0f; b = 1f; break;
			case 3: r = 1f; g = 0f; b = 0f; break;
			case 4: r = 0f; g = 1f; b = 1f; break;
			case 5: r = 0f; g = 1f; b = 0f; break;
			default: r = 0f; g = 0f; b = 1f; break;
		}
		alpha = 0.75f;
		scaleSize = MathUtils.random(1, 2);
		x = MathUtils.random(0, width - 250);
		y = MathUtils.random(100, height);
	}

	//equal if the same rgb
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (!(o instanceof RandomText))
			return false;
		RandomText other = (RandomText) o;
		float epsilon = 0.00000001f;
		return Math.abs(this.r - other.r) < epsilon
				&& Math.abs(this.g - other.g) < epsilon
				&& Math.abs(this.b - other.b) < epsilon;
	}
}
