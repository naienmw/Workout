package com.naien.workout;


import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.AnimationUtils;

public class Animation {

    private android.view.animation.Animation fab_oben,rotate_forward,fab_close;
    FloatingActionButton fab;

    public Animation(Context context,FloatingActionButton fab){

        this.fab = fab ;// (FloatingActionButton)view.findViewById(fabresourceid);

        fab_oben = AnimationUtils.loadAnimation(context,R.anim.fab_open);
        rotate_forward = AnimationUtils.loadAnimation(context,R.anim.rotate_forward);
        fab_close = AnimationUtils.loadAnimation(context,R.anim.fab_close);

    }

    public void startAnimationopen(){
        fab.startAnimation(fab_oben);
    }

    public void startAnimationclose(){
        fab.startAnimation(fab_close);
    }
}
