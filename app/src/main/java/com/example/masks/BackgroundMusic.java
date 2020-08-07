package com.example.masks;

import android.content.Context;
import android.media.MediaPlayer;

public class BackgroundMusic {
	private static BackgroundMusic bgInstance;
	private static MediaPlayer music;
	private Context mContext;

	private BackgroundMusic(Context context) {
		mContext = context.getApplicationContext();
		music = MediaPlayer.create(context, R.raw.backmusic);
		music.setLooping(true);
	}

	public static BackgroundMusic getMedia(Context context) {
		if(null == bgInstance) {
			synchronized(BackgroundMusic.class) {
				bgInstance = new BackgroundMusic(context);
			}
		}
		return bgInstance;
	}

	public void play() {
		if(!music.isPlaying()) {
			System.out.println("Play");
			music.start();
		}
	}

	public void pause() {
		if(music.isPlaying()) {
			System.out.println("Pause");
			music.pause();
		}
	}
}