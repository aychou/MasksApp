package com.example.masks;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.masks.GameView.screenRatioX;
import static com.example.masks.GameView.screenRatioY;

public class Bullet {
	public int x, y, width, height;
	public Bitmap bullet;

	public Bullet(Resources res) {
		bullet = BitmapFactory.decodeResource(res, R.drawable.bullet);

		width = (int) (bullet.getWidth() / 4 * screenRatioX);
		height = (int) (bullet.getHeight() / 4 * screenRatioY);

		bullet = Bitmap.createScaledBitmap(bullet, width, height, false);
	}

	public Rect getCollisionShape() {
		return new Rect(x, y, x + width, y + height);
	}
}
