package com.lollipops.memmatch;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.davidea.flipview.FlipView;

public class TripleGameActivity extends AppCompatActivity implements ClickInterface {

    RecyclerView recyclerView;
    TextView toMenu;
    List<Sweets> list = new ArrayList<>();
    int cells=30;
    int[] Images = {R.drawable.cake, R.drawable.cake2, R.drawable.cake_slice,
            R.drawable.cakes, R.drawable.candy, R.drawable.candy2,
            R.drawable.candy3, R.drawable.cupcake, R.drawable.cupcake2,
            R.drawable.cherry_pie, R.drawable.ice_cream3, R.drawable.pancakes,
            R.drawable.cupcake3, R.drawable.donut, R.drawable.donut2,
            R.drawable.cake3, R.drawable.ice_cream2, R.drawable.honey,
            R.drawable.donut3, R.drawable.ice_cream, R.drawable.lollipop,
            R.drawable.lollipop2, R.drawable.macarons, R.drawable.pancake,
            R.drawable.cake4, R.drawable.cupcake4, R.drawable.chocolate,
            R.drawable.sweet, R.drawable.truffle, R.drawable.cupcake5};
    String[] nameImage = {"cake","cake2","cake_slice","cakes","candy",
            "candy2","candy3","cupcake","cupcake2","cherry_pie",
            "ice_cream3","pancakes","cupcake3","donut","donut2",
            "cake3","ice_cream2","honey","donut3","ice_cream",
            "lollipop","lollipop2","macarons","pancake","pancake",
            "cupcake4","chocolate","sweet","truffle","cupcake5"};

    String firstName="", secondName="", thirdName="";
    int first=-1, second=-1, third =-1;
    Adapter adapter;

    int moves=0;
    boolean sound;
    int move=150;
    public static int time;
    String type;

    TextView text_moves,text_time,text_mode;
    CountDownTimer timer;

    SharedPreferences.Editor editor;

    boolean isStart=false;
    boolean isStartPause=false;
    boolean isPause=false;
    boolean onPause=false;
    public static boolean startIsSleep=true;

    int width_count;

    SoundPool sound_Level;
    int sound_btn;
    final int MAX_STREAMS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        startIsSleep=false;

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        cells = sharedPreferences.getInt("cells",30);
        sound = sharedPreferences.getBoolean("sound",true);
        time = sharedPreferences.getInt("time",180);
        move = sharedPreferences.getInt("move",150);
        type = sharedPreferences.getString("type","normal");
        width_count = sharedPreferences.getInt("width_count",6);

        toMenu = findViewById(R.id.text_toMenu);
        text_mode = findViewById(R.id.text_mode);
        text_time = findViewById(R.id.text_time);
        text_moves = findViewById(R.id.text_moves);
        text_mode.setText("Triple mode");
        text_moves.setText("Moves: "+moves);
        text_time.setText("Time: "+time+"s");
        recyclerView = findViewById(R.id.recyclerView);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        sound_Level = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sound_btn = sound_Level.load(this,R.raw.click3,1);

        CreateTriples();
    }

    public void CreateTriples(){

        for (int j=0;j<cells;j+=3){
            list.add(new Sweets(j,Images[j/3],nameImage[j/3],0));
            list.add(new Sweets(j+1,Images[j/3],nameImage[j/3],0));
            list.add(new Sweets(j+2,Images[j/3],nameImage[j/3],0));
        }

        Collections.shuffle(list);

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),width_count));
        adapter = new Adapter(getApplicationContext(),list, TripleGameActivity.this, 3, type,sound);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        DialogStart();
    }

    private void StartTimer() {
       timer = new CountDownTimer(time*1000,1000) {
           @Override
           public void onTick(long millisUntilFinished) {
               time--;
               text_time.setText("Time: "+time+"s");
           }

           @Override
           public void onFinish() {
               time = 0;
               text_time.setText("Time: "+time+"s");
               for (int i2=0; i2<recyclerView.getChildCount();i2++){
                   ConstraintLayout view = (ConstraintLayout) recyclerView.getChildAt(i2);
                   FlipView im = (FlipView) view.getChildAt(0);
                   im.flip(true);
                   im.setClickable(false);
               }
               DialogEnd("Lose");
           }
       }.start();
    }

    @Override
    public void ImageClick(int i, Sweets sweets) {
        Log.d("click", ""+sweets.getNameImage());
        if (firstName.equals("")){
            firstName = sweets.getNameImage();
            first = i;}
        else if (secondName.equals("")){
            secondName = sweets.getNameImage();
            second = i;}
        else{
            for (int i2=0; i2<recyclerView.getChildCount();i2++){
                ConstraintLayout view = (ConstraintLayout) recyclerView.getChildAt(i2);
                FlipView im = (FlipView) view.getChildAt(0);
                im.setClickable(false);
            }
            thirdName = sweets.getNameImage();
            third = i;
            if (firstName.equals(secondName)&&firstName.equals(thirdName)){
                Log.d("click", "Right "+first+" "+second+" "+third);
                list.get(first).setChecked(1);
                list.get(second).setChecked(1);
                list.get(third).setChecked(1);
                firstName="";
                secondName="";
                thirdName="";
                int f = first;
                int s = second;
                int t = third;
                first=-1;
                second=-1;
                third=-1;
                for (int i2=0; i2<recyclerView.getChildCount();i2++){
                    ConstraintLayout view3 = (ConstraintLayout) recyclerView.getChildAt(i2);
                    FlipView im3 = (FlipView) view3.getChildAt(0);
                    im3.setClickable(true);
                }
                Handler handler = new
                        Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyItemChanged(f);
                        adapter.notifyItemChanged(s);
                        adapter.notifyItemChanged(t);
                        moves++;
                        text_moves.setText("Moves: "+moves);
                        if (moves<move) {
                            cells -= 3;
                            if (cells == 0 && time > 0) {
                                timer.cancel();
                                DialogEnd("Won");
                            }
                        }
                        else{
                            DialogEnd("Moves");
                        }
                    }
                }, 500);

            }
            else{
                Log.d("click", "Right "+first+" "+second+" "+third);
                firstName="";
                secondName="";
                thirdName="";
                int f = first;
                int s = second;
                int t = third;
                first=-1;
                second=-1;
                third=-1;
                for (int i2=0; i2<recyclerView.getChildCount();i2++){
                    ConstraintLayout view3 = (ConstraintLayout) recyclerView.getChildAt(i2);
                    FlipView im3 = (FlipView) view3.getChildAt(0);
                    im3.setClickable(true);
                }
                Handler handler = new
                        Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ConstraintLayout view = (ConstraintLayout) recyclerView.getChildAt(f);
                        FlipView im = (FlipView) view.getChildAt(0);
                        im.showPrevious();
                        ConstraintLayout view2 = (ConstraintLayout) recyclerView.getChildAt(s);
                        FlipView im2 = (FlipView) view2.getChildAt(0);
                        im2.showPrevious();
                        ConstraintLayout view3 = (ConstraintLayout) recyclerView.getChildAt(t);
                        FlipView im3 = (FlipView) view3.getChildAt(0);
                        im3.showPrevious();
                        moves++;
                        text_moves.setText("Moves: "+moves);
                    }
                }, 500);
            }
        }
    }

    public void toMenu(View view) {
        if (sound)
        sound_Level.play(sound_btn, 0.8f, 0.8f, 0, 0, 1);
        timer.cancel();
        Intent intent = new Intent(TripleGameActivity.this, MainActivity.class);
        startActivity(intent); finish();
    }

    private void ShowGrid() {
        for (int i=0; i<recyclerView.getChildCount();i++){
            ConstraintLayout view = (ConstraintLayout) recyclerView.getChildAt(i);
            FlipView im = (FlipView) view.getChildAt(0);
            im.flip(true);
            im.setClickable(false);
        }
        toMenu.setEnabled(false);
        Handler handler = new
                Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<recyclerView.getChildCount();i++){
                    ConstraintLayout view = (ConstraintLayout) recyclerView.getChildAt(i);
                    FlipView im = (FlipView) view.getChildAt(0);
                    im.flip(false);
                    im.setClickable(true);
                }
                toMenu.setEnabled(true);
                startIsSleep=true;
                StartTimer();
            }
        }, 2000);
    }

    private void DialogStart() {
        Dialog startDialog = new Dialog(TripleGameActivity.this,R.style.FullScreenDialog);
        startDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        startDialog.setContentView(R.layout.dialog_start);
        startDialog.setCanceledOnTouchOutside(false);
        startDialog.setCancelable(false);
        startDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        startDialog.getWindow().setDimAmount(0.5f);
        startDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        startDialog.show();

        LinearLayout linear_dialog = startDialog.findViewById(R.id.linear_dialog);
        linear_dialog.setAlpha(0.6f);
        linear_dialog.setScaleX(0.8f);
        linear_dialog.setScaleY(0.8f);
        linear_dialog.animate().alpha(1.0f)
                .setDuration(500)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(300)
                .start();
        isStartPause = true;

        TextView text_level = startDialog.findViewById(R.id.text_level);
        text_level.setText("Triple mode");

        TextView text_desc = startDialog.findViewById(R.id.text_desc);
        text_desc.setText("They to open all pairs (or triples) before time runs up (in Time Attack mode).\n" +
                "Try to make the least amount of moves.");

        TextView start = startDialog.findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStartPause=false;
                ShowGrid();
                startDialog.dismiss();
                if (sound)
                    sound_Level.play(sound_btn, 0.8f, 0.8f, 0, 0, 1);
            }
        });
    }

    private void DialogPause() {
        Dialog pauseDialog = new Dialog(TripleGameActivity.this,R.style.FullScreenDialog);
        pauseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pauseDialog.setContentView(R.layout.dialog_pause);
        pauseDialog.setCanceledOnTouchOutside(false);
        pauseDialog.setCancelable(false);
        pauseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pauseDialog.getWindow().setDimAmount(0.5f);
        pauseDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        pauseDialog.show();
        isPause = true;

        LinearLayout linear_dialog = pauseDialog.findViewById(R.id.linear_dialog);
        linear_dialog.setAlpha(0.6f);
        linear_dialog.setScaleX(0.8f);
        linear_dialog.setScaleY(0.8f);
        linear_dialog.animate().alpha(1.0f)
                .setDuration(500)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(300)
                .start();

        TextView time_left = pauseDialog.findViewById(R.id.text_left);
        TextView text_move = pauseDialog.findViewById(R.id.text_progress);

        time_left.setText("Time : "+time+"s");
        text_move.setText("Move : "+moves);

        TextView resume = pauseDialog.findViewById(R.id.resume);
        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cells>0&&timer!=null) {
                    timer.start(); }
                isPause = false;
                pauseDialog.dismiss();
                if (sound)
                    sound_Level.play(sound_btn, 0.8f, 0.8f, 0, 0, 1);
            }
        });

        TextView restart = pauseDialog.findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                isPause = false;
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
                overridePendingTransition(0, 0);
                pauseDialog.dismiss();
                if (sound_btn!=0)
                    sound_Level.play(sound_btn, 0.8f, 0.8f, 0, 0, 1);
            }
        });

        TextView quit = pauseDialog.findViewById(R.id.quit);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sound)
                    sound_Level.play(sound_btn, 0.8f, 0.8f, 0, 0, 1);
                Intent intent = new Intent(TripleGameActivity.this,MainActivity.class);
                startActivity(intent); finish();
            }
        });
    }

    private void DialogEnd(String status) {
        Dialog endDialog = new Dialog(TripleGameActivity.this,R.style.FullScreenDialog);
        endDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        endDialog.setContentView(R.layout.dialog_end);
        endDialog.setCanceledOnTouchOutside(false);
        endDialog.setCancelable(false);
        endDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        endDialog.getWindow().setDimAmount(0.5f);
        endDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        endDialog.show();

        LinearLayout linear_dialog = endDialog.findViewById(R.id.linear_dialog);
        linear_dialog.setAlpha(0.6f);
        linear_dialog.setScaleX(0.8f);
        linear_dialog.setScaleY(0.8f);
        linear_dialog.animate().alpha(1.0f)
                .setDuration(500)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(300)
                .start();

        TextView text_status = endDialog.findViewById(R.id.text_status);
        TextView text_desc = endDialog.findViewById(R.id.text_desc);
        if (status.equals("Won")) {
            text_desc.setText("You Won in " + moves + " moves");
            text_status.setText("You have Won");
        }
        else if (status.equals("Lose")){
            text_desc.setText("Time's up, you've lost");
            text_status.setText("You have Lost");
        }
        else if (status.equals("Moves")){
            text_desc.setText("You've spent all the moves");
            text_status.setText("You have Lost");
        }

        TextView quit = endDialog.findViewById(R.id.quit);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sound)
                    sound_Level.play(sound_btn, 0.8f, 0.8f, 0, 0, 1);
                Intent intent = new Intent(TripleGameActivity.this,MainActivity.class);
                startActivity(intent); finish();
                endDialog.dismiss();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer!=null) {
            timer.cancel();
        }
        onPause=true;
    }


    @Override
    protected void onResume() {
        super.onResume();

        onPause=false;
        if (isStart) {
            if (!isPause){ DialogPause(); }
        }
        else{
            if (startIsSleep){ DialogStart(); }
        }

        final Window w = getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onBackPressed() {
        if (timer!=null) {
            timer.cancel();
        }
        if (isStart) {
            if (!isPause){ DialogPause(); }
        }
        else{
            Intent intent = new Intent(TripleGameActivity.this,MainActivity.class);
            startActivity(intent); finish();
        }
    }
}