package com.lollipops.memmatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int windowwidth,windowheight;
    int small, normal;
    int height_count;
    String type;
    int width_count;
    boolean sound = true;

    SharedPreferences.Editor editor;

    SoundPool sound_Level;
    int sound_btn;
    final int MAX_STREAMS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        sound = sharedPreferences.getBoolean("sound",true);

        sound_Level = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sound_btn = sound_Level.load(this,R.raw.click3,1);

        getMaxSizeGrid();

    }

    private void getMaxSizeGrid() {

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        small = getResources().getDimensionPixelSize(R.dimen._35sdp);
        normal = getResources().getDimensionPixelSize(R.dimen._40sdp);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        windowwidth = displaymetrics.widthPixels;
        windowheight = displaymetrics.heightPixels;

        if (windowwidth/6>normal){
            height_count = (windowheight-getResources().getDimensionPixelSize(R.dimen._200sdp))/normal-1;
            width_count=6;
            type = "normal";
            if (height_count>=10) {
                height_count=10;
            }
            else{
                if (windowwidth/7>small){
                    height_count = (windowheight-getResources().getDimensionPixelSize(R.dimen._200sdp))/small-1;
                    if (height_count>=9) {
                        height_count=9;
                        width_count=7;
                        type = "small";
                    }
                }
            }
        }
        else if (windowwidth/6>small){
            height_count = (windowheight-getResources().getDimensionPixelSize(R.dimen._200sdp))/small-1;
            width_count=6;
            type = "small";
        }

        editor.putInt("height_count",height_count).apply();
        editor.putInt("width_count",width_count).apply();
        editor.putString("type",type).apply();
    }

    public void toPairsMode(View view) {
        if (sound)
            sound_Level.play(sound_btn, 0.8f, 0.8f, 0, 0, 1);
        Intent intent = new Intent(MainActivity.this, PairsGameActivity.class);
        startActivity(intent); finish();
    }

    public void toTripleMode(View view) {
        if (sound)
            sound_Level.play(sound_btn, 0.8f, 0.8f, 0, 0, 1);
        Intent intent = new Intent(MainActivity.this, TripleGameActivity.class);
        startActivity(intent); finish();
    }

    public void toSettings(View view) {
        if (sound)
            sound_Level.play(sound_btn, 0.8f, 0.8f, 0, 0, 1);
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent); finish();
    }

    public void toInfo(View view) {
        if (sound)
            sound_Level.play(sound_btn, 0.8f, 0.8f, 0, 0, 1);
        Intent intent = new Intent(MainActivity.this, InfoActivity.class);
        startActivity(intent); finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Window w = getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}