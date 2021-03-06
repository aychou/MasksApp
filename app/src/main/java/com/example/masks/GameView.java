package com.example.masks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {
	private Thread thread;
	private boolean isPlaying, isGameOver = false;
	private int screenX, screenY, score = 0;
	public static float screenRatioX, screenRatioY;
	private Paint paint;
	private Germ[] germs;
	private SharedPreferences prefs;
	private Random random;
	private SoundPool soundPool;
	private List<Bullet> bullets;
	private int sound;
	private Flight flight;
	private GameActivity activity;
	private Background background1, background2;
	private boolean bgMusicPlaying = false;

	public GameView(GameActivity activity, int screenX, int screenY) {
		super(activity);
		System.out.println(null==activity);
		this.activity = activity;

		prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

			AudioAttributes audioAttributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_GAME).build();
			soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(2).build();

		} else soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

		sound = soundPool.load(activity, R.raw.shoot, 1);


		this.screenX = screenX;
		this.screenY = screenY;
		screenRatioX = 1920f / screenX;
		screenRatioY = (1080f / screenY);
		background1 = new Background(screenX, screenY, getResources());
		background2 = new Background(screenX, screenY, getResources());

		flight = new Flight(this, screenY, screenX, getResources());

		bullets = new ArrayList<>();

		background2.x = screenX;

		paint = new Paint();
		paint.setTextSize(120);
		paint.setColor(Color.WHITE);

		germs = new Germ[4];

		for(int i = 0; i < 4; i++) {
			Germ germ = new Germ(getResources(), screenX, screenY);
			germs[i] = germ;
		}
		random = new Random();
	}

	@Override
	public void run() {
		while(isPlaying) {
			update();
			draw();
			sleep();
		}
	}

	private void update() {

		background1.x -= (int) (10 * screenRatioX);
		background2.x -= (int) (10 * screenRatioX);

		if(background1.x + screenX <= 0) {
			background1.x = screenX - 10;
		}
		if(background2.x + screenX <= 0) {
			background2.x = screenX - 10;
		}
		//        if(flight.isGoingUp)
		//            flight.y -= 30 * screenRatioY;
		//        else
		//            flight.y += 30 * screenRatioY;
		if(flight.y < 0) flight.y = 0;
		if(flight.y >= screenY - flight.height) flight.y = screenY - flight.height;

		List<Bullet> trash = new ArrayList<>();

		for(Bullet bullet : bullets) {
			if(bullet.x > screenX) trash.add(bullet);

			bullet.x += 50 * screenRatioX;

			for(Germ germ : germs) {
				if(Rect.intersects(germ.getCollisionShape(), bullet.getCollisionShape())) {

					score++;
					germ.x = -500;
					bullet.x = screenX + 500;
					germ.wasShot = true;
				}
			}
		}

		for(Bullet bullet : trash)
			bullets.remove(bullet);

		for(Germ germ : germs) {
			//            if(score<20){
			//                germ.x= germ.speed+
			//            }
			//            germ.x -= germ.speed+25*Math.log(Math.max(score, 1));
			germ.x -= germ.speed + 10 * Math.sqrt(score);


			if(germ.x + germ.width < 0) {

				if(!germ.wasShot) {
					isGameOver = true;
					return;
				}

				int bound = (int) (30 * screenRatioX);
				germ.speed = random.nextInt(bound);

				if(germ.speed < 10 * screenRatioX) germ.speed = (int) (10 * screenRatioX);

				germ.x = screenX;
				germ.y = random.nextInt(screenY - germ.height);
			}

			if(Rect.intersects(germ.getCollisionShape(), flight.getCollisionShape())) {
				isGameOver = true;
				return;
			}
		}
	}

	private void draw() {

		if(getHolder().getSurface().isValid()) {
			Canvas canvas = getHolder().lockCanvas();
			canvas.drawColor(Color.BLACK);
			canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
			canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

			for(Germ germ : germs)
				canvas.drawBitmap(germ.getGerm(), germ.x, germ.y, paint);
			canvas.drawText(score + "", screenX / 2f, 164, paint);

			if(isGameOver) {
				isPlaying = false;
				canvas.drawBitmap(flight.getDead(), flight.x, flight.y, paint);
				getHolder().unlockCanvasAndPost(canvas);
				saveIfHighScore();
				waitBeforeExiting();
				return;
			}

			canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint);

			for(Bullet bullet : bullets)
				canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);

			getHolder().unlockCanvasAndPost(canvas);
		}
	}

	private void waitBeforeExiting() {
		try {
			Thread.sleep(3000);
			activity.startActivity(new Intent(activity, MainActivity.class));
			activity.finish();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void saveIfHighScore() {
		if(prefs.getInt("highscore", 0) < score) {
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("highscore", score);
			editor.apply();
		}
	}

	private void sleep() {
		try {
			Thread.sleep(17);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void resume() {
		isPlaying = true;
		thread = new Thread(this);
		thread.start();
	}

	public void pause() {
		try {
			isPlaying = false;
			thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

/*	    @Override
	    public boolean onTouchEvent(MotionEvent event) {
	        // get pointer index from the event object
	        int pointerIndex = event.getActionIndex();

	        // get pointer ID
	        int pointerId = event.getPointerId(pointerIndex);

	        // get masked (not specific to a pointer) action
	        int maskedAction = event.getActionMasked();

	        switch(maskedAction) {
	            case MotionEvent.ACTION_DOWN:
	                    if(event.getX() < screenX / 2){
	                        flight.isGoingUp = true;
	                    }

	                break;
	            case MotionEvent.ACTION_UP:
	                case MotionEvent.ACTION_POINTER_UP:
	                    flight.isGoingUp = false;
	                    if(event.getX() > screenX / 2)
	                        flight.toShoot++;
	                    break;

	        }

	        return true;*/

	public boolean onTouchEvent(MotionEvent event) {
		int amount = event.getPointerCount();
		for(int pointer = 0; pointer < amount; pointer++) {
			float x = event.getX(pointer);
			float y = event.getY(pointer);

			if(x > screenX / 2 && flight.toShoot == 0) {
				flight.toShoot++;
			}

			if(x < screenX / 2 - 500) {
				flight.x = (int) x - 100;
				flight.y = Math.max(Math.min((int) y - 50, screenY - flight.height), 0);
			}
		}
		return true;
	}

	public void newBullet() {
		if(prefs.getBoolean("isMute", false)) {
			soundPool.play(sound, 1, 1, 0, 0, 1);
		}
		Bullet bullet = new Bullet(getResources());
		bullet.x = flight.x + flight.width;
		bullet.y = flight.y + (flight.height / 2);
		bullets.add(bullet);
	}
}
