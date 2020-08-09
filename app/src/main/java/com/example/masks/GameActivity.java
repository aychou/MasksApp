package com.example.masks;

import android.graphics.Point;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

public class GameActivity extends AppCompatActivity {
	private GameView gameView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Point point = new Point();
		getWindowManager().getDefaultDisplay().getSize(point);

		gameView = new GameView(this, point.x + 10, point.y);

		setContentView(gameView);
	}

	@Override
	protected void onPause() {
		super.onPause();
		BackgroundMusic.pause(true);
		gameView.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		BackgroundMusic.reinstate();
		gameView.resume();
	}
}