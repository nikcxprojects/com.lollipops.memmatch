package com.lollipops.memmatch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    String[] info_texts = {
            "Open 2 random cards and keep them if they are matching (for example, two cookies).If they pair then they stand on the table.",
            "If not - they will be turned face down.",
            "They to open all pairs (or triples) before time runs up (in Time Attack mode).\n" +
                    "Try to make the least amount of moves."
    };

    int[] info_images = {R.drawable.info_image1,R.drawable.info_image2,R.drawable.info_image3};

    String[] info_btns = {"Next","Next","Back to menu"};

    TextView text_Next,text_Info;
    ImageView image_info;
    int pos=0;

    SoundPool sound_Level;
    int sound_btn;
    final int MAX_STREAMS = 5;

    boolean sound = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        sound = sharedPreferences.getBoolean("sound",true);

        image_info = findViewById(R.id.info_image);
        text_Info = findViewById(R.id.text_info);
        text_Next = findViewById(R.id.text_Next);

        image_info.setImageResource(info_images[pos]);
        text_Info.setText(""+info_texts[pos]);
        text_Next.setText(""+info_btns[pos]);

        sound_Level = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sound_btn = sound_Level.load(this,R.raw.click2,1);

        text_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sound)
                    sound_Level.play(sound_btn, 0.8f, 0.8f, 0, 0, 1);
                if (pos<2){
                    pos++;
                    image_info.setImageResource(info_images[pos]);
                    text_Info.setText(""+info_texts[pos]);
                    text_Next.setText(""+info_btns[pos]);
                }
                else{
                    Intent intent = new Intent(InfoActivity.this, MainActivity.class);
                    startActivity(intent); finish();
                }
            }
        });

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
