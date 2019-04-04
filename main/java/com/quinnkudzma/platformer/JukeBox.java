package com.quinnkudzma.platformer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

/**
 * sounds from https://gamesounds.xyz/?dir=Sonniss.com%20-%20GDC%202015%20-%20Game%20Audio%20Bundle
 * https://gamesounds.xyz/?dir=The%20Frontier%20-%20RPG%20Set
 * https://gamesounds.xyz/?dir=Kenney%27s%20Sound%20Pack/RPG%20Sounds
 * http://bruitages.free.fr/bruits/sifflet_de_carnaval.wav
 */

public class JukeBox {
    Context mContext = null;
    SoundPool mSoundPool = null;
    MediaPlayer mBgPlayer = null;
    HashMap mSoundsMap = null;

    private static final float DEFAULT_MUSIC_VOLUME = 0.6f;
    private static final int MAX_STREAMS = 4;
    private static final float DEFAULT_SFX_VOLUME = 0.6f;
    private static final String SOUNDS_PREF_KEY = "sounds_pref_key";
    private static final String MUSIC_PREF_KEY = "music_pref_key";
    public boolean mSoundEnabled = true;
    public boolean mMusicEnabled = true; //TODO: make pref
    private static final String TAG = "JukeBox: ";

    public JukeBox(Context context) {
        mContext = context;
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        mSoundEnabled = prefs.getBoolean(SOUNDS_PREF_KEY, true);
        mMusicEnabled = prefs.getBoolean(MUSIC_PREF_KEY, true);
        loadIfNeeded();
    }

    private void loadIfNeeded(){
        if(mSoundEnabled){
            loadSounds();
        }
        if(mMusicEnabled){
            loadMusic();
        }
    }

    public void toggleSoundStatus(){
        mSoundEnabled = !mSoundEnabled;
        if(mSoundEnabled){
            loadSounds();
        }else{
            unloadSounds();
        }
        PreferenceManager
                .getDefaultSharedPreferences(mContext)
                .edit()
                .putBoolean(SOUNDS_PREF_KEY, mSoundEnabled)
                .commit();
    }

    public void toggleMusicStatus(){
        mMusicEnabled = !mMusicEnabled;
        if(mMusicEnabled){
            loadMusic();
        }else{
            unloadMusic();
        }
        PreferenceManager
                .getDefaultSharedPreferences(mContext)
                .edit()
                .putBoolean(MUSIC_PREF_KEY, mSoundEnabled)
                .commit();
    }

    private void loadSounds(){
        createSoundPool();
        mSoundsMap = new HashMap<Game.GameEvent, Integer>();
        loadEventSound(Game.GameEvent.Jump, "sfx/jump.wav");
        loadEventSound(Game.GameEvent.CoinPickup, "sfx/coin.wav");
        loadEventSound(Game.GameEvent.LevelStart, "sfx/start.wav");
        loadEventSound(Game.GameEvent.Damage, "sfx/sammy.wav");
        loadEventSound(Game.GameEvent.Movement, "sfx/cloth2.ogg");
        loadEventSound(Game.GameEvent.Death, "sfx/death.WAV");
        loadEventSound(Game.GameEvent.LevelGoal, "sfx/win.wav");
    }

    private void unloadSounds(){
        if(mSoundPool != null) {
            mSoundPool.release();
            mSoundPool = null;
            mSoundsMap.clear();
        }
    }

    private void loadEventSound(final Game.GameEvent event, final String fileName){
        try {
            AssetFileDescriptor afd = mContext.getAssets().openFd(fileName);
            int soundId = mSoundPool.load(afd, 1);
            mSoundsMap.put(event, soundId);
        }catch(IOException e){
            Log.e(TAG, "loadEventSound: error loading sound " + e.toString());
        }
    }

    public void playSoundForGameEvent(Game.GameEvent event) {
        if (!mSoundEnabled) {
            return;
        }
        final float leftVolume = DEFAULT_SFX_VOLUME;
        final float rightVolume = DEFAULT_SFX_VOLUME;
        final int priority = 1;
        final int loop = 0;
        final float rate = 1.0f;
        final Integer soundID = (Integer) mSoundsMap.get(event);
        if (soundID != null) {
            mSoundPool.play(soundID, leftVolume, rightVolume, priority, loop, rate);
        }
    }

    @SuppressWarnings("deprecation")
    private void createSoundPool() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }
        else {
            AudioAttributes attr = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            mSoundPool = new SoundPool.Builder()
                    .setAudioAttributes(attr)
                    .setMaxStreams(MAX_STREAMS)
                    .build();
        }
    }

    // Music
    private void loadMusic(){
        try{
            mBgPlayer = new MediaPlayer();
            AssetFileDescriptor afd = mContext
                    .getAssets().openFd("sfx/backround_music.mp3");
            mBgPlayer.setDataSource(
                    afd.getFileDescriptor(),
                    afd.getStartOffset(),
                    afd.getLength());
            mBgPlayer.setLooping(true);
            mBgPlayer.setVolume(DEFAULT_MUSIC_VOLUME, DEFAULT_MUSIC_VOLUME);
            mBgPlayer.prepare();
        }catch(IOException e){
            mBgPlayer = null;
            mMusicEnabled = false;
            //Log.e(e);
        }
    }

    private void unloadMusic(){
        if(mBgPlayer != null) {
            mBgPlayer.stop();
            mBgPlayer.release();
        }
    }

    public void pauseBgMusic(){
        if(!mMusicEnabled){ return; }
        mBgPlayer.pause();
    }

    public void resumeBgMusic(){
        if(!mMusicEnabled){ return; }
        mBgPlayer.start();
    }

}
