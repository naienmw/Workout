package com.naien.workout;


import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.animation.AnimationUtils;

public class Animation_Restore {

    private android.view.animation.Animation fab_open_new_ex;
    private android.view.animation.Animation fab_close_new_ex;
    FloatingActionButton fab;

    public Animation_Restore(Context context, FloatingActionButton fab){

        this.fab = fab ;// (FloatingActionButton)view.findViewById(fabresourceid);

        fab_open_new_ex = AnimationUtils.loadAnimation(context,R.anim.fab_open_restore);
        fab_close_new_ex = AnimationUtils.loadAnimation(context,R.anim.fab_close_restore);

    }

    public void startAnimationopen(){

        fab.startAnimation(fab_open_new_ex);
    }

    public void startAnimationclose(){
        fab.startAnimation(fab_close_new_ex);
    }
}
