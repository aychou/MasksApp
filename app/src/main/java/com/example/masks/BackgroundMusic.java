package com.example.masks;

import android.content.Context;
import android.media.MediaPlayer;

public class BackgroundMusic {
	private static BackgroundMusic bgInstance;
	private static MediaPlayer music;
	private static Context mContext;
	private static boolean playing = false;

	private BackgroundMusic(Context context) {
		mContext = context.getApplicationContext();
		if(null == music) {
			music = MediaPlayer.create(context, R.raw.backmusic);
			music.setLooping(true);
		}
	}

	public static BackgroundMusic getMedia(){
		if(null == bgInstance) {
			System.out.println("CRAP CRAP CRAP FORGOT TO INIT THE BG MUSIC ABORT ABORT ABORT");
//			throw new Throwable("Drat, forgot to init");
//			synchronized(BackgroundMusic.class) {
//				bgInstance = new BackgroundMusic(context);
//			}
		}
		return bgInstance;
	}

	public static void init(Context cont) {
		bgInstance = new BackgroundMusic(cont);
	}

	public static void play() {
		if(!music.isPlaying()) {
			System.out.println("Play");
			music.start();
			playing = true;
		}
	}

	public static void pause() {
		if(music.isPlaying()) {
			System.out.println("Pause");
			music.pause();
			playing = false;
		}
	}

	public static void pause(boolean noPlayingUpdate) {
		if(music.isPlaying()) {
			System.out.println("Soft Pause");
			music.pause();
		}
	}

	public static void reinstate() {
		if(!music.isPlaying() && playing) {
			play();
			System.out.println("Play Reinstated");
		}
		else {
			System.out.println("No Play Reinstated");
		}
	}
}