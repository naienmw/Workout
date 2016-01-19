package com.naien.workout;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;


public class MyBlurFragment extends Fragment {


    private ImageView image;
    public FastBlur blur = new FastBlur();

    DBHelper mydb;
    DBHelper_Ex dbex;

    String date_db;

    //ListAdapter multiRowAdapter;
    FloatingActionButton myFAB;
    FloatingActionButton FABBackup;

    //TextView infotext;
    //ImageView arrow;
    TextView currentWorkout;
    TextView showAll;
    TextView newWo;
    RelativeLayout rellayout;

    Boolean toolbarisshown = false;
    ListView setsinmain;

    Animation faboben;
    Animation_Backup faboben_backup;

    Boolean pretty_Animation;

    XML_Saver_Class save_db_user;
    XML_Saver_Class_Exercises save_db_ex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);

        if (Build.VERSION.SDK_INT < 21){
            pretty_Animation = false;
        }else{
            pretty_Animation = true;
        }

        image = (ImageView) view.findViewById(R.id.picture);
        //currentWorkout = (TextView) view.findViewById(R.id.CurrentWorkoutMain);

        setsinmain = (ListView) view.findViewById(R.id.ListviewcurrentSetsinMain);

        mydb = new DBHelper(getActivity());
        dbex = new DBHelper_Ex(getActivity());

        save_db_user = new XML_Saver_Class(mydb.getdb(),getActivity());
        save_db_ex = new XML_Saver_Class_Exercises(dbex.getdb(),getActivity());

        //dbex.create_all();
        //////////////////EXERCISES TO DATABASE//////////////////////////////
        //saveExercises();
        /////////////////////////////////////////////////////////////////////

        myFAB = (FloatingActionButton) view.findViewById(R.id.fabAddWorkout);

        FABBackup = (FloatingActionButton) view.findViewById(R.id.FABBackup);
        FABBackup.setBackgroundTintList(getResources().getColorStateList(R.color.FABBackup));

        currentWorkout = (TextView) view.findViewById(R.id.CurrentWorkoutMain);
        showAll = (TextView) view.findViewById(R.id.ButtonShowAll);
        newWo = (TextView) view.findViewById(R.id.ButtonNewWo);

        faboben = new Animation(getActivity(),myFAB);
        faboben_backup = new Animation_Backup(getActivity(),FABBackup);

        //Bitmap temp = dbex.getExerciseImage("Brust","Bankdrücken");
        //myFAB.setImageBitmap(temp);

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH) +1;
        int year = c.get(Calendar.YEAR);
        date_db = "d"+Integer.toString(day) +"_" + Integer.toString(month) +"_"+Integer.toString(year);

        FABBackup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try{
                    save_db_user.backup();
                    //save_db_user.restore();
                    Toast.makeText(getActivity(),"Backup successful (User Sets)",Toast.LENGTH_SHORT).show();}



                catch (Exception e){
                    Toast.makeText(getActivity(), "Something went wrong when trying to backup your Sets)", Toast.LENGTH_SHORT).show();
                }

                try{
                    save_db_ex.backup();
                    //save_db_ex.restore();
                    Toast.makeText(getActivity(),"Backup successful (Exercises)",Toast.LENGTH_SHORT).show();}
                catch (Exception e){
                    Toast.makeText(getActivity(), "Something went wrong when trying to backup Exercises", Toast.LENGTH_SHORT).show();
                }
            }
        });


        myFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!toolbarisshown){
                    faboben.startAnimationopen();
                    FABBackup.setVisibility(View.VISIBLE);
                    faboben_backup.startAnimationopen();
                    if(pretty_Animation) {
                        enterReveal(R.id.myToolbar);
                    }else{
                        getView().findViewById(R.id.myToolbar).setVisibility(View.VISIBLE);
                    }
                toolbarisshown = true;
                    myFAB.setBackgroundTintList(getResources().getColorStateList(R.color.colorWhite));
                    myFAB.setImageResource(R.drawable.barbell_accent);

                }else{
                    if(pretty_Animation) {
                        exitReveal(R.id.myToolbar);
                    }else{
                        getView().findViewById(R.id.myToolbar).setVisibility(View.INVISIBLE);
                    }
                    toolbarisshown = false;
                    myFAB.setBackgroundTintList(getResources().getColorStateList(R.color.accent));
                    faboben.startAnimationclose();
                    myFAB.setImageResource(R.drawable.barbell);
                    faboben_backup.startAnimationclose();
                }

                //exitReveal(R.id.fabAddWorkout);
            }
        });


        /*if(mydb.doesTableExist(mydb.getdb(), date_db)) {
            //myFAB.setBackgroundTintList(getResources().getColorStateList(R.color.colorPurple));
            //myFAB.setImageResource(R.drawable.addnewexisting);
            newWo.setText("Edit Current");
            newWo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent workout_main = new Intent(getActivity(), WorkoutMainActivity.class);
                    workout_main.putExtra("workout_name", mydb.getWoName(date_db));
                    workout_main.putExtra("date", date_db);
                    startActivity(workout_main);

                }
            });

            ArrayList<String> theExs;

            theExs = mydb.getAllExercises_Arraylist(date_db);

            my_adapter_sets_arraylist theAdapter = new my_adapter_sets_arraylist(getActivity(),theExs);

            setsinmain.setAdapter(theAdapter);


            String[] parts = date_db.substring(1).split("_");
            String date = parts[0] + "." + parts[1] + "." + parts[2];

            currentWorkout.setText(mydb.getWoName(date_db) + "   -   " + date);

            currentWorkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    XML_Saver_Class save_db_user = new XML_Saver_Class(mydb.getdb());
                    //XML_Saver_Class save_db_ex = new XML_Saver_Class(dbex.getdb());

                    Intent normalWO = new Intent(getActivity(), WorkoutMainActivity.class);

                    normalWO.putExtra("date", date_db);
                    normalWO.putExtra("workout_name", mydb.getWoName(date_db));

                    //save_db_user.backup();

                    //startActivity(normalWO);

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
        }*/

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
                MyBlurFragment fragment = (MyBlurFragment) getFragmentManager().findFragmentById(R.id.fragmentid);
                if (fragment != null && toolbarisshown) {
                    if(pretty_Animation) {
                        fragment.exitReveal(R.id.myToolbar);
                    }else{
                        getView().findViewById(R.id.myToolbar).setVisibility(View.INVISIBLE);
                    }
                    toolbarisshown = false;
                    //fragment.enterReveal(R.id.fabAddWorkout);
                    myFAB.setBackgroundTintList(getResources().getColorStateList(R.color.accent));
                    faboben.startAnimationclose();
                    myFAB.setImageResource(R.drawable.barbell);

                    faboben_backup.startAnimationclose();
                    FABBackup.setVisibility(View.INVISIBLE);

                } else {
                }
            }
        });



        //applyBlur();
        return view;
    }

    public View getFAB(){
        return myFAB;
    }

    public Boolean getisToolbarShown(){
        return toolbarisshown;
    }

    public void setisToolbarShown(Boolean shown){

        toolbarisshown = shown;
    }

    public void onResume() {
        super.onResume();
        if (mydb.doesTableExist(mydb.getdb(), date_db)) {
            //myFAB.setBackgroundTintList(getResources().getColorStateList(R.color.colorPurple));
            //myFAB.setImageResource(R.drawable.addnewexisting);
            newWo.setText("Edit Current");
            newWo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent workout_main = new Intent(getActivity(), WorkoutMainActivity.class);
                    workout_main.putExtra("workout_name", mydb.getWoName(date_db));
                    workout_main.putExtra("date", date_db);
                    startActivity(workout_main);

                }
            });

            ArrayList<String> theExs;

            theExs = mydb.getAllExercises_Arraylist(date_db);

            my_adapter_sets_arraylist theAdapter = new my_adapter_sets_arraylist(getActivity(), theExs);

            setsinmain.setAdapter(theAdapter);


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
        } else {
            newWo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent test = new Intent(getActivity(), NewWorkoutActivity.class);
                    test.putExtra("date", date_db);
                    startActivity(test);

                }
            });
        }
    }




    void enterReveal(int id) {
        // previously invisible view
        final View myView = getView().findViewById(id);

        // get the center for the clipping circle
        int cx;
        int cy;

        cx = myView.getRight();
        cy = myView.getBottom();


        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight()) / 2;
        finalRadius = (int)Math.hypot(myView.getWidth(),myView.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);



        // make the view visible and start the animation
        myView.setVisibility(View.VISIBLE);



        anim.start();
    }


    void exitReveal(int id) {
        // previously visible view
        final View myView = getView().findViewById(id);

        // get the center for the clipping circle
        int cx ;
        int cy ;

        cx = myView.getRight();
        cy = myView.getBottom();

        // get the initial radius for the clipping circle
        int initialRadius = myView.getWidth() / 2;
        initialRadius = (int)Math.hypot(myView.getWidth(),myView.getHeight());

        // create the animation (the final radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                myView.setVisibility(View.INVISIBLE);


            }
        });

        // start the animation

        anim.start();
    }





    /*private void applyBlur() {
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
    }*/
/*
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void blur(Bitmap bkg, View view) {


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
    }*/



    public void saveExercises(){

        Bitmap icon;

        dbex.saveExerciseName("Brust", "Bankdrücken");
         icon = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.eximage_benchpress);
        dbex.saveExerciseImage("Brust","Bankdrücken",icon);

        dbex.saveExerciseName("Brust", "Bankdrücken-schräg");
        icon = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.eximage_benchpress_incline);
        dbex.saveExerciseImage("Brust","Bankdrücken-schräg",icon);

        dbex.saveExerciseName("Brust", "Bankdrücken-schräg-HS");
        icon = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.eximage_benchpress_incline_hs);
        dbex.saveExerciseImage("Brust","Bankdrücken-schräg-HS",icon);

        dbex.saveExerciseName("Brust", "Bankdrücken-KH");
        icon = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.eximage_benchpress_dumbbell);
        dbex.saveExerciseImage("Brust","Bankdrücken-KH",icon);

        dbex.saveExerciseName("Brust", "Bankdrücken-schräg-KH");
        icon = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.eximage_benchpress_incline_dumbbell);
        dbex.saveExerciseImage("Brust","Bankdrücken-schräg-KH",icon);

        dbex.saveExerciseName("Brust", "Bankdrücken-HS");
        icon = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.eximage_benchpress_hs);
        dbex.saveExerciseImage("Brust","Bankdrücken-HS",icon);

        dbex.saveExerciseName("Brust", "Bankdrücken-schräg2-HS");
        icon = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.eximage_benchpress_incline2_hs);
        dbex.saveExerciseImage("Brust","Bankdrücken-schräg2-HS",icon);

        dbex.saveExerciseName("Brust", "Butterfly-M");
        icon = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.eximage_butterfly_m);
        dbex.saveExerciseImage("Brust","Butterfly-M",icon);

        dbex.saveExerciseName("Brust", "Butterfly-incline-Kabel");
        icon = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.eximage_butterfly_incline_cable);
        dbex.saveExerciseImage("Brust","Butterfly-incline-Kabel",icon);

        dbex.saveExerciseName("Schultern", "Seitheben-KH");
        icon = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.eximage_butterfly_m);
        dbex.saveExerciseImage("Schultern","Seitheben-KH",icon);

        dbex.saveExerciseName("Rücken", "Kreuzheben");
        icon = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.eximage_butterfly_m);
        dbex.saveExerciseImage("Rücken","Kreuzheben",icon);

        dbex.saveExerciseName("Arme", "Bi-Curls-KH");
        icon = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.eximage_butterfly_m);
        dbex.saveExerciseImage("Arme","Bi-Curls-KH",icon);

        dbex.saveExerciseName("Beine", "Beinstrecker");
        icon = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.eximage_butterfly_m);
        dbex.saveExerciseImage("Beine","Beinstrecker",icon);

    }



}
