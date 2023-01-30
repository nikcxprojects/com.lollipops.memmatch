package com.lollipops.memmatch;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SettingsActivity extends AppCompatActivity {

    TextView cells_count, cells_minus, cells_plus;
    TextView text_time, text_time_plus, text_time_minus;
    TextView text_move, text_move_plus, text_move_minus;
    TextView Sound;

    int maxCells = 60;
    int minCells = 24;
    int cells = 30;
    boolean sound = true;

    int maxTime = 300;
    int minTime = 30;
    int time = 180;
    int maxMove = 200;
    int minMove = 30;
    int move = 150;

    int height_count = 10;
    int width_count = 6;

    SharedPreferences.Editor editor;

    SoundPool sound_Level;
    int sound_btn;
    int sound_btn2;
    final int MAX_STREAMS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        cells = sharedPreferences.getInt("cells",30);
        sound = sharedPreferences.getBoolean("sound",true);
        time = sharedPreferences.getInt("time",180);
        move = sharedPreferences.getInt("move",150);
        height_count = sharedPreferences.getInt("height_count",10);
        width_count = sharedPreferences.getInt("width_count",6);
        maxCells = width_count*height_count;

        cells_count = findViewById(R.id.cells_count);
        cells_minus = findViewById(R.id.cells_minus);
        cells_plus = findViewById(R.id.cells_plus);

        text_time = findViewById(R.id.text_time);
        text_time_minus = findViewById(R.id.text_time_minus);
        text_time_plus = findViewById(R.id.text_time_plus);

        text_move = findViewById(R.id.text_move);
        text_move_minus = findViewById(R.id.text_move_minus);
        text_move_plus = findViewById(R.id.text_move_plus);

        Sound = findViewById(R.id.Sound);

        sound_Level = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sound_btn = sound_Level.load(this, R.raw.click2, 1);
        sound_btn2 = sound_Level.load(this, R.raw.click3, 1);

        cells_count.setText(""+cells);
        text_time.setText(""+time+"s");
        text_move.setText(""+move);
        if (sound) { Sound.setText("Off"); }
        else { Sound.setText("On"); }

        if (cells==minCells){
            cells_minus.setEnabled(false);
            cells_minus.setBackgroundResource(R.drawable.button_enabled);
        }
        else if (cells>minCells){
            cells_minus.setEnabled(true);
            cells_minus.setBackgroundResource(R.drawable.button_style);
        }

        if (cells==maxCells){
            cells_plus.setEnabled(false);
            cells_plus.setBackgroundResource(R.drawable.button_enabled);
        }
        else if (cells<maxCells){
            cells_plus.setEnabled(true);
            cells_plus.setBackgroundResource(R.drawable.button_style);
        }

        if (time==minTime){
            text_time_minus.setEnabled(false);
            text_time_minus.setBackgroundResource(R.drawable.button_enabled);
        }
        else if (time>minTime){
            text_time_minus.setEnabled(true);
            text_time_minus.setBackgroundResource(R.drawable.button_style);
        }

        if (time==maxTime){
            text_time_plus.setEnabled(false);
            text_time_plus.setBackgroundResource(R.drawable.button_enabled);
        }
        else if (time<maxTime){
            text_time_plus.setEnabled(true);
            text_time_plus.setBackgroundResource(R.drawable.button_style);
        }

        if (move==minMove){
            text_move_minus.setEnabled(false);
            text_move_minus.setBackgroundResource(R.drawable.button_enabled);
        }
        else if (move>minMove){
            text_move_minus.setEnabled(true);
            text_move_minus.setBackgroundResource(R.drawable.button_style);
        }

        if (move==maxMove){
            text_move_plus.setEnabled(false);
            text_move_plus.setBackgroundResource(R.drawable.button_enabled);
        }
        else if (move<maxMove){
            text_move_plus.setEnabled(true);
            text_move_plus.setBackgroundResource(R.drawable.button_style);
        }

        Sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sound)
                    sound_Level.play(sound_btn, 0.8f, 0.8f, 0, 0, 1);
                sound=!sound;
                if (sound) { Sound.setText("Off"); }
                else { Sound.setText("On"); }
            }
        });

        cells_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sound)
                    sound_Level.play(sound_btn, 0.8f, 0.8f, 0, 0, 1);
                if (cells>minCells){
                    cells-=6;
                    cells_count.setText(""+cells);
                    cells_plus.setEnabled(true);
                    cells_plus.setBackgroundResource(R.drawable.button_style);
                    if (cells==minCells){
                        cells_minus.setEnabled(false);
                        cells_minus.setBackgroundResource(R.drawable.button_enabled);
                    }
                }
            }
        });

        cells_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sound)
                    sound_Level.play(sound_btn, 0.8f, 0.8f, 0, 0, 1);
                if (cells<maxCells){
                    cells+=6;
                    cells_count.setText(""+cells);
                    cells_minus.setEnabled(true);
                    cells_minus.setBackgroundResource(R.drawable.button_style);
                    if (cells==maxCells){
                        cells_plus.setEnabled(false);
                        cells_plus.setBackgroundResource(R.drawable.button_enabled);
                    }
                }
            }
        });

        text_time_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sound)
                    sound_Level.play(sound_btn, 0.8f, 0.8f, 0, 0, 1);
                if (time>minTime){
                    time-=10;
                    text_time.setText(""+time+"s");
                    text_time_plus.setEnabled(true);
                    text_time_plus.setBackgroundResource(R.drawable.button_style);
                    if (time==minTime){
                        text_time_minus.setEnabled(false);
                        text_time_minus.setBackgroundResource(R.drawable.button_enabled);
                    }
                }
            }
        });

        text_time_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sound)
                    sound_Level.play(sound_btn, 0.8f, 0.8f, 0, 0, 1);
                if (time<maxTime){
                    time+=10;
                    text_time.setText(""+time+"s");
                    text_time_minus.setEnabled(true);
                    text_time_minus.setBackgroundResource(R.drawable.button_style);
                    if (time==maxTime){
                        text_time_plus.setEnabled(false);
                        text_time_plus.setBackgroundResource(R.drawable.button_enabled);
                    }
                }
            }
        });

        text_move_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sound)
                    sound_Level.play(sound_btn, 0.8f, 0.8f, 0, 0, 1);
                if (move>minMove){
                    move-=10;
                    text_move.setText(""+move);
                    text_move_plus.setEnabled(true);
                    text_move_plus.setBackgroundResource(R.drawable.button_style);
                    if (move==minMove){
                        text_move_minus.setEnabled(false);
                        text_move_minus.setBackgroundResource(R.drawable.button_enabled);
                    }
                }
            }
        });

        text_move_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sound)
                    sound_Level.play(sound_btn, 0.8f, 0.8f, 0, 0, 1);
                if (move<maxMove){
                    move+=10;
                    text_move.setText(""+move);
                    text_move_minus.setEnabled(true);
                    text_move_minus.setBackgroundResource(R.drawable.button_style);
                    if (move==maxMove){
                        text_move_plus.setEnabled(false);
                        text_move_plus.setBackgroundResource(R.drawable.button_enabled);
                    }
                }
            }
        });

    }

    public void Save(View view) {
        if (sound)
            sound_Level.play(sound_btn2, 0.8f, 0.8f, 0, 0, 1);
        editor.putInt("cells",cells).apply();
        editor.putBoolean("sound",sound).apply();
        editor.putInt("time",time).apply();
        editor.putInt("move",move).apply();

        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent); finish();
    }

    public void Back(View view) {
        if (sound)
            sound_Level.play(sound_btn2, 0.8f, 0.8f, 0, 0, 1);
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
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
