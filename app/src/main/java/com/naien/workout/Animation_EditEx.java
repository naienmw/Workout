package com.naien.workout;


import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.animation.AnimationUtils;

public class Animation_EditEx {

    private android.view.animation.Animation fab_open_edit_ex;
    private android.view.animation.Animation fab_close_edit_ex;
    FloatingActionButton fab;

    public Animation_EditEx(Context context, FloatingActionButton fab){

        this.fab = fab ;// (FloatingActionButton)view.findViewById(fabresourceid);

        fab_open_edit_ex = AnimationUtils.loadAnimation(context,R.anim.fab_open_edit_ex);
        fab_close_edit_ex = AnimationUtils.loadAnimation(context,R.anim.fab_close_edit_ex);

    }

    public void startAnimationopen(){

        fab.startAnimation(fab_open_edit_ex);
    }

    public void startAnimationclose(){
        fab.startAnimation(fab_close_edit_ex);
    }
}
