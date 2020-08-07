package com.example.masks;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.masks.GameView.screenRatioX;
import static com.example.masks.GameView.screenRatioY;

public class Flight {
	public int toShoot = 0;
	public boolean isGoingUp = false, flapOne = false;
	public int x, y, width, height, shootCounter = 1;
	public Bitmap dead;
	public Bitmap[] shootBitmaps, flights;
	private GameView gameView;

	public Flight(GameView gameView, int screenY, Resources res) {
		this.gameView = gameView;

		flights = new Bitmap[] {
			BitmapFactory.decodeResource(res, R.drawable.fly1),
			BitmapFactory.decodeResource(res, R.drawable.fly2)
		};

		width = (int) (flights[0].getWidth() / 3.5 * screenRatioX);
		height = (int) (flights[0].getHeight() / 3.5 * screenRatioY);

		flights[0] = Bitmap.createScaledBitmap(flights[0], width, height, false);
		flights[1] = Bitmap.createScaledBitmap(flights[1], width, height, false);

		shootBitmaps = new Bitmap[] {
			BitmapFactory.decodeResource(res, R.drawable.shoot1),
			BitmapFactory.decodeResource(res, R.drawable.shoot2),
			BitmapFactory.decodeResource(res, R.drawable.shoot3),
			BitmapFactory.decodeResource(res, R.drawable.shoot4),
			BitmapFactory.decodeResource(res, R.drawable.shoot5)
		};

		for(int shoot = 0; shoot < shootBitmaps.length; shoot++) {
			shootBitmaps[shoot] = Bitmap.createScaledBitmap(shootBitmaps[shoot], width, height, false);
		}

		dead = BitmapFactory.decodeResource(res, R.drawable.dead);
		dead = Bitmap.createScaledBitmap(dead, width, height, false);

		x = (int) (64 * screenRatioX);
		y = screenY / 2;
	}

	public Bitmap getFlight() {
		if(toShoot != 0) {
			shootCounter++;
			switch(shootCounter) {
				case 1:
				case 2:
				case 3:
				case 4:
					return shootBitmaps[shootCounter - 1];
				default:
					shootCounter = 1;
					toShoot--;
					gameView.newBullet();
					return shootBitmaps[4];
			}
		}
		flapOne = !flapOne;
		if(flapOne) {
			return flights[0];
		} else {
			return flights[1];
		}
	}

	public Rect getCollisionShape() {
		return new Rect(x, y, x + width, y + height);
	}

	public Bitmap getDead() {
		return dead;
	}
}

