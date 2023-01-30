package com.lollipops.memmatch;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import eu.davidea.flipview.FlipView;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    List<Sweets> list;
    Context activity;
    ClickInterface clickInterface;
    int mode;
    String type;
    SoundPool sound_Level;
    boolean sound;
    int sound_items;
    final int MAX_STREAMS = 5;

    public Adapter(Context activity, List<Sweets> list, ClickInterface clickInterface, int mode, String type, boolean sound) {
        this.activity = activity;
        this.list = list;
        this.clickInterface = clickInterface;
        this.mode = mode;
        this.type = type;
        this.sound = sound;

        sound_Level = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        sound_items = sound_Level.load(activity,R.raw.click1,1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layout = R.layout.item_grid;
        if (type.equals("small")) { layout = R.layout.item_grid_small; }

        ViewHolder holder = new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        if (list.get(i).getChecked()==0) {
            ConstraintLayout view = (ConstraintLayout) viewHolder.image.getRearLayout();
            ImageView im = (ImageView) view.getChildAt(0);
            im.setImageResource(list.get(i).getImage());
            viewHolder.image.setEnabled(true);

            viewHolder.image.setOnFlippingListener(new FlipView.OnFlippingListener() {
                @Override
                public void onFlipped(FlipView flipView, boolean checked) {
                    int time = 0;
                    boolean startIsSleep = false;
                    if (mode==2){ time = PairsGameActivity.time;
                        startIsSleep = PairsGameActivity.startIsSleep;}
                    else if (mode==3){ time = TripleGameActivity.time;
                        startIsSleep = TripleGameActivity.startIsSleep;}
                    if (checked&&time>0&&startIsSleep) {
                        if (sound)
                            sound_Level.play(sound_items, 0.8f, 0.8f, 0, 0, 1);
                        flipView.setClickable(false);
                        clickInterface.ImageClick(i, list.get(i));
                    }
                }
            });
        }
        else{
            ConstraintLayout view = (ConstraintLayout) viewHolder.image.getFrontLayout();
            ImageView im = (ImageView) view.getChildAt(0);
            im.setImageResource(R.drawable.none_style);
            ConstraintLayout viewRear = (ConstraintLayout) viewHolder.image.getRearLayout();
            ImageView imRear = (ImageView) viewRear.getChildAt(0);
            imRear.setImageResource(R.drawable.none_style);
            viewHolder.image.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        List<Sweets> arrayList = this.list;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        FlipView image;
        ConstraintLayout constraint;

        public ViewHolder(View view) {
            super(view);
            this.image = view.findViewById(R.id.mFlipView);
            this.constraint = view.findViewById(R.id.constraint);
        }
    }
}

