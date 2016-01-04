package com.naien.workout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import fr.tvbarthel.lib.blurdialogfragment.BlurDialogFragment;


public class WorkoutMainActivity extends Activity{

    DBHelper mydb;
    DBHelper_Ex mydb_ex;

    String the_date;
    String the_workout;
    ArrayAdapter theAdapter;
    ArrayList<String> theExercise;
    FloatingActionButton NewEx;
    FloatingActionButton FABTest;
    ArrayList<Integer> ExIndex;
    String PrimaryWorkout;
    ImageView ExPic;
    RelativeLayout RelLayoutChoice;
    ExpandableListView ListViewChoice;
    Animation faboben;
    Boolean toolbarisshown = false;

    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ExpListAdapter explistadapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

     @Override
    public void onResume(){

        super.onResume();
        this.setContentView(R.layout.workout_main);
        ListView theListView = (ListView) findViewById(R.id.listview_exercises);
        theListView.setClickable(false);
        mydb = new DBHelper(this);
        mydb_ex = new DBHelper_Ex(this);
        Intent i = getIntent();

        the_date = i.getStringExtra("date");
        the_workout = i.getStringExtra("workout_name");



        PrimaryWorkout = getPrimaryWorkout(the_workout);
        ExPic = (ImageView)findViewById(R.id.picture_exercise);

         listDataHeader = new ArrayList<String>();
         listDataChild = new HashMap<String, List<String>>();

        ListViewChoice = (ExpandableListView)findViewById(R.id.listview_exercises_choice);
        temp_list_data(); //fill the list from database
        explistadapter = new ExpListAdapter(this,listDataHeader,listDataChild);

        ListViewChoice.setAdapter(explistadapter);

        ListViewChoice.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

             public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                 onchildclick(groupPosition,childPosition);

                 return false;
             }
        });

         /*
         FABTest = (FloatingActionButton) findViewById(R.id.FABTEST);

         FABTest.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ListViewChoice.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                     public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                         Toast.makeText(getApplicationContext(),listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition),Toast.LENGTH_SHORT).show();

                         return false;
                     }
                 });
             }
         }); */



        RelLayoutChoice = (RelativeLayout) findViewById(R.id.rellayout_exchoice);

        switch(PrimaryWorkout) {
            case "Brust":   ExPic.setImageResource(R.drawable.chest);
                break;
            case "Beine":   ExPic.setImageResource(R.drawable.legs);
                break;
            case "Arme":   ExPic.setImageResource(R.drawable.arms);
                break;
            case "Schultern":   ExPic.setImageResource(R.drawable.shoulders);
                break;
            case "Rücken":   ExPic.setImageResource(R.drawable.back);
                break;
        }
        TextView my_workout = (TextView) findViewById(R.id.workout_name);
        my_workout.setText(the_workout);

        theExercise = mydb.getAllExercises_Arraylist(the_date);
        ExIndex = mydb.allExIndex(the_date);

        NewEx = (FloatingActionButton)findViewById(R.id.ExNewButton);

        faboben = new Animation(this,NewEx);



        theAdapter = new my_adapter(this,theExercise);

        theListView.setAdapter(theAdapter);

        theListView.setClickable(true);

        theListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
             @Override
             public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                 String Ex = theAdapter.getItem(position).toString();
                 mydb.deleteExinWO_index(the_date, ExIndex.get(position));

                 theAdapter.remove(Ex);
                 theAdapter.setNotifyOnChange(true);
                 exitReveal(R.id.rellayout_exchoice);
                 //onResume();
                 return true;
             }
         });


        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                final String exercise_name = theExercise.get(position);

                //////BLUR SEEMS TO WORK JUST FINE --> KEEP IT THIS WAY/////////
                SetsDialogFragmentBlur setsDialog = new SetsDialogFragmentBlur();

                setsDialog.setStuff(exercise_name, the_date, ExIndex.get(position));
                setsDialog.show(getFragmentManager(),"Diag");

                ///////////////////////////

            }
        }
        );
    }

    private void temp_list_data(){


        // Adding head data
        listDataHeader.add("Brust");
        listDataHeader.add("Schultern");
        listDataHeader.add("Rücken");
        listDataHeader.add("Arme");
        listDataHeader.add("Beine");

        listDataChild = mydb_ex.getExHM();

        /*// Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);*/
    }


    public void AddNewExercise(View view) {

        //EditText newExercise = (EditText) findViewById(R.id.user_Exercise_Input);
        //String newEx = newExercise.getText().toString();

        if (!toolbarisshown){
            faboben.startAnimationopen();
            enterReveal(R.id.rellayout_exchoice);
            toolbarisshown = true;
            NewEx.setBackgroundTintList(getResources().getColorStateList(R.color.colorWhite));
            NewEx.setImageResource(R.drawable.add_new_cross_accent);

        }else{
            exitReveal(R.id.rellayout_exchoice);
            toolbarisshown = false;
            NewEx.setBackgroundTintList(getResources().getColorStateList(R.color.accent));
            faboben.startAnimationclose();
            NewEx.setImageResource(R.drawable.addnewcross);
        }




        /*if (!newEx.matches("")) {

            mydb.saveExerciseName(the_date, newEx);
            //theExercise.add(newEx);


            SetsDialogFragmentBlur setsDialog = new SetsDialogFragmentBlur();
            //Toast.makeText(this,ExIndex.size(),Toast.LENGTH_SHORT).show();
            Integer index = 2;
            if(!(ExIndex.size() == 0)){
                 index = ExIndex.get(ExIndex.size() - 1)+1;
            }


            setsDialog.setStuff(newEx, the_date, index);
            setsDialog.show(getFragmentManager(), "Diag");


            if(!(ExIndex.size() == 0)) {
                ExIndex.add(ExIndex.get(ExIndex.size() - 1) + 1);
            }else{
                ExIndex.add(2);
            }


            newExercise.setText("");
            theAdapter.setNotifyOnChange(true);
            theAdapter.add(newEx);


        }else{
            Toast.makeText(WorkoutMainActivity.this, "Please Enter a Exercise", Toast.LENGTH_SHORT).show();
        }*/
    }

    public String getPrimaryWorkout(String longworkout){
        String primary;

        if(!longworkout.contains("/")){
            primary = longworkout;
        }else{
            String temp[] = longworkout.split("/");
            primary = temp[0];
        }

        return primary;
    }


    void enterReveal(int id) {
        // previously invisible view
        final View myView = findViewById(id);

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
        final View myView = findViewById(id);

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

    public void onchildclick(int groupposition, int childposition){

        String exname = listDataChild.get(listDataHeader.get(groupposition)).get(childposition);

        mydb.saveExerciseName(the_date, exname);
        //theExercise.add(newEx);


        SetsDialogFragmentBlur setsDialog = new SetsDialogFragmentBlur();
        //Toast.makeText(this,ExIndex.size(),Toast.LENGTH_SHORT).show();
        Integer index = 2;
        if(!(ExIndex.size() == 0)){
            index = ExIndex.get(ExIndex.size() - 1)+1;
        }


        setsDialog.setStuff(exname, the_date, index);
        setsDialog.show(getFragmentManager(), "Diag");


        if(!(ExIndex.size() == 0)) {
            ExIndex.add(ExIndex.get(ExIndex.size() - 1) + 1);
        }else{
            ExIndex.add(2);
        }


        //newExercise.setText("");
        theAdapter.setNotifyOnChange(true);
        theAdapter.add(exname);
    }

}
