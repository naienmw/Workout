package com.naien.workout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;


public class MyBlurFragment extends Fragment {

    private ImageView image;
    public FastBlur blur = new FastBlur();

    DBHelper mydb;
    String date_db;

    //ListAdapter multiRowAdapter;
    FloatingActionButton myFAB;
    //TextView infotext;
    //ImageView arrow;
    TextView currentWorkout;
    Button showAll;
    Button newWo;
    RelativeLayout rellayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        image = (ImageView) view.findViewById(R.id.picture);
        currentWorkout = (TextView) view.findViewById(R.id.CurrentWorkoutMain);



        mydb = new DBHelper(getActivity());
        myFAB = (FloatingActionButton) view.findViewById(R.id.fabAddWorkout);
        currentWorkout = (TextView) view.findViewById(R.id.CurrentWorkoutMain);
        showAll = (Button) view.findViewById(R.id.ButtonShowAll);
        newWo = (Button) view.findViewById(R.id.ButtonNewWo);


        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH) +1;
        int year = c.get(Calendar.YEAR);
        date_db = "d"+Integer.toString(day) +"_" + Integer.toString(month) +"_"+Integer.toString(year);


        myFAB.setBackgroundTintList(getResources().getColorStateList(R.color.colorRed));
        myFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterReveal(R.id.myToolbar);
                exitReveal(R.id.fabAddWorkout);
            }
        });


        if(mydb.doesTableExist(mydb.getdb(), date_db)) {
            //myFAB.setBackgroundTintList(getResources().getColorStateList(R.color.colorPurple));
            //myFAB.setImageResource(R.drawable.addnewexisting);
            newWo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent workout_main = new Intent(getActivity(), WorkoutMainActivity.class);
                    workout_main.putExtra("workout_name", mydb.getWoName(date_db));
                    workout_main.putExtra("date", date_db);
                    startActivity(workout_main);

                }
            });


            String[] parts = date_db.substring(1).split("_");
            String date = parts[0] + "." + parts[1] + "." + parts[2];

            currentWorkout.setText(mydb.getWoName(date_db) + "   -   " + date);

            currentWorkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent normalWO = new Intent(getActivity(), WorkoutMainActivity.class);

                    normalWO.putExtra("date", date_db);
                    normalWO.putExtra("workout_name", mydb.getWoName(date_db));

                    startActivity(normalWO);

                }
            });
        }else{
            newWo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent test = new Intent(getActivity(), NewWorkoutActivity.class);
                    test.putExtra("date", date_db);
                    startActivity(test);

                }
            });
        }

        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AllWorkoutsActivity.class);
                startActivity(i);
            }
        });

        rellayout = (RelativeLayout)view.findViewById(R.id.framemain);

        rellayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBlurFragment fragment = (MyBlurFragment)getFragmentManager().findFragmentById(R.id.fragmentid);
                if (fragment != null && !myFAB.isShown()) {
                    fragment.exitReveal(R.id.myToolbar);
                    fragment.enterReveal(R.id.fabAddWorkout);

                }else {
                }
            }
        });

        applyBlur();
        return view;
    }


    public void onResume() {
        super.onResume();
        if(mydb.doesTableExist(mydb.getdb(), date_db)) {
            //myFAB.setBackgroundTintList(getResources().getColorStateList(R.color.colorPurple));
            //myFAB.setImageResource(R.drawable.addnewexisting);
            newWo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent workout_main = new Intent(getActivity(), WorkoutMainActivity.class);
                    workout_main.putExtra("workout_name", mydb.getWoName(date_db));
                    workout_main.putExtra("date", date_db);
                    startActivity(workout_main);

                }
            });
            //infotext.setText("Edit today's Workout");
            //arrow.setImageResource(R.drawable.arrow_edit);

            String[] parts = date_db.substring(1).split("_");
            String date = parts[0] + "." + parts[1] + "." + parts[2];

            currentWorkout.setText(mydb.getWoName(date_db) + "   -   " + date);

            currentWorkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent normalWO = new Intent(getActivity(), WorkoutMainActivity.class);

                    normalWO.putExtra("date", date_db);
                    normalWO.putExtra("workout_name", mydb.getWoName(date_db));

                    startActivity(normalWO);

                }
            });
        }
    };

    /*public void makeNewWorkout(View view){

        if(!mydb.doesTableExist(mydb.getdb(),date_db)) {
            Intent test = new Intent(getActivity(),NewWorkoutActivity.class);
            test.putExtra("date",date_db);
            startActivity(test);
        }else{
            Intent workout_main = new Intent(getActivity(), WorkoutMainActivity.class);
            workout_main.putExtra("workout_name", mydb.getWoName(date_db));
            workout_main.putExtra("date", date_db);
            startActivity(workout_main);
        }


    }*/

    void enterReveal(int id) {
        // previously invisible view
        final View myView = getView().findViewById(id);

        // get the center for the clipping circle
        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight() / 2;


        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight()) / 2;

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);



        // make the view visible and start the animation
        myView.setVisibility(View.VISIBLE);
        /*anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                getActivity().getWindow().getEnterTransition().removeListener(mEnterTransitionListener);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });*/
        anim.start();
    }


    void exitReveal(int id) {
        // previously visible view
        final View myView = getView().findViewById(id);

        // get the center for the clipping circle
        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight() / 2;

        // get the initial radius for the clipping circle
        int initialRadius = myView.getWidth() / 2;

        // create the animation (the final radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                myView.setVisibility(View.INVISIBLE);
                //myfab.setVisibility(View.VISIBLE);

            }
        });

        // start the animation

        anim.start();
    }





    private void applyBlur() {
        image.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                image.getViewTreeObserver().removeOnPreDrawListener(this);
                image.buildDrawingCache();

                Bitmap bmp = image.getDrawingCache();
                blur(bmp, currentWorkout);
                return true;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void blur(Bitmap bkg, View view) {

        //long startMs = System.currentTimeMillis();
        float scaleFactor = 1;
        float radius = 20;

            scaleFactor = 8;
            radius = 2;


        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth() / scaleFactor),
                (int) (view.getMeasuredHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft()/scaleFactor, -view.getTop()/scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = blur.doBlur(overlay,(float)1, (int)radius);
        view.setBackground(new BitmapDrawable(getResources(), overlay));
    }


    public String toString() {
        return "RenderScript";
    }



}
