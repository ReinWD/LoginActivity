package com.jay.loginactivitytest.activity;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.jay.loginactivitytest.R;

public class MakeTroubleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_trouble);

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, 7, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

        final SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_RING, 5);
        final int id = soundPool.load(this, R.raw.music, 1);
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    soundPool.play(id, 10, 10, 0, 0, 1);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }
}
