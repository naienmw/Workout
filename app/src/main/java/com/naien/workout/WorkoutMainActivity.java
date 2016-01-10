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
import java.util.Map;
import java.util.Objects;

import fr.tvbarthel.lib.blurdialogfragment.BlurDialogFragment;


public class WorkoutMainActivity extends Activity{

    DBHelper mydb;
    DBHelper_Ex mydb_ex;

    String the_date;
    String the_workout;
    ArrayAdapter theAdapter;
    ArrayList<String> theExercise;
    FloatingActionButton NewEx;
    FloatingActionButton FABEditEx;
    FloatingActionButton FABNewEx;
    ArrayList<Integer> ExIndex;
    String PrimaryWorkout;
    ImageView ExPic;
    RelativeLayout RelLayoutChoice;
    ExpandableListView ListViewChoice;

    Animation faboben;
    Animation_EditEx faboben_edit_ex;
    Animation_NewEx faboben_new_ex;

    Boolean toolbarisshown;

    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ExpListAdapter explistadapter;

    Boolean editex = false;

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

        editex = false;
        toolbarisshown = false;


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

                onchildclick(groupPosition, childPosition);

                return false;
            }
        });




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

        FABEditEx = (FloatingActionButton)findViewById(R.id.FABEditEx);
        FABEditEx.setBackgroundTintList(getResources().getColorStateList(R.color.FABEditEx));

        FABNewEx = (FloatingActionButton)findViewById(R.id.FABNewEx);
        FABNewEx.setBackgroundTintList(getResources().getColorStateList(R.color.FABNewEx));

        faboben = new Animation(this,NewEx);
        faboben_edit_ex = new Animation_EditEx(this,FABEditEx);
        faboben_new_ex = new Animation_NewEx(this,FABNewEx);

         FABEditEx.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 if(!editex) {
                     editex = true;
                 }else{
                     editex = false;
                 }
             }
         });

         FABNewEx.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 NewExDialogFragmentBlur exnew = new NewExDialogFragmentBlur();
                 exnew.show(getFragmentManager(),"newEx");

             }
         });



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
                 if(toolbarisshown) {
                     exitReveal(R.id.rellayout_exchoice,R.id.ExNewButton);

                     faboben.startAnimationclose();
                     NewEx.setImageResource(R.drawable.addnewcross);

                     faboben_edit_ex.startAnimationclose();
                     faboben_new_ex.startAnimationclose();
                     toolbarisshown = false;
                 }

                 return true;
             }
         });


        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String exercise_name = theExercise.get(position);
                String exercise_head = getHeadfromHMvalue(mydb_ex.getExHM(), exercise_name);

                //////BLUR SEEMS TO WORK JUST FINE --> KEEP IT THIS WAY/////////
                SetsDialogFragmentBlur setsDialog = new SetsDialogFragmentBlur();
                setsDialog.setStuff(exercise_head,exercise_name, the_date, ExIndex.get(position));
                setsDialog.show(getFragmentManager(),"Diag");
                ///////////////////////////

            }
        }
        );
    }

    private void temp_list_data(){



        listDataHeader.add("Brust");
        listDataHeader.add("Schultern");
        listDataHeader.add("Rücken");
        listDataHeader.add("Arme");
        listDataHeader.add("Beine");

        listDataChild = mydb_ex.getExHM();


    }


    public void AddNewExercise(View view) {


        if (!toolbarisshown){
            faboben.startAnimationopen();
            NewEx.setImageResource(R.drawable.barbell);

            enterReveal(R.id.rellayout_exchoice,R.id.ExNewButton);
            toolbarisshown = true;

            FABNewEx.setVisibility(View.VISIBLE);
            faboben_new_ex.startAnimationopen();

            FABEditEx.setVisibility(View.VISIBLE);
            faboben_edit_ex.startAnimationopen();



        }else{
            exitReveal(R.id.rellayout_exchoice,R.id.ExNewButton);
            toolbarisshown = false;
            //NewEx.setBackgroundTintList(getResources().getColorStateList(R.color.accent));
            faboben.startAnimationclose();
            NewEx.setImageResource(R.drawable.addnewcross);

            faboben_edit_ex.startAnimationclose();
            FABEditEx.setVisibility(View.INVISIBLE);

            faboben_new_ex.startAnimationclose();
            FABNewEx.setVisibility(View.INVISIBLE);
        }


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


    void enterReveal(int id,int startid) {
        // previously invisible view
        final View myView = findViewById(id);
        final View start = findViewById(startid);

        // get the center for the clipping circle
        int cx;
        int cy;

        cx = start.getLeft() + start.getWidth()/2;
        cy = start.getTop() - start.getHeight()/2;


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


    void exitReveal(int id,int startid) {
        // previously visible view
        final View myView = findViewById(id);
        final View start = findViewById(startid);

        // get the center for the clipping circle
        int cx ;
        int cy ;

        cx = start.getLeft() + start.getWidth()/2;
        cy = start.getTop() - start.getHeight()/2;
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

    public void onchildclick(int groupposition, int childposition) {
        String exname = listDataChild.get(listDataHeader.get(groupposition)).get(childposition);
        String exhead = listDataHeader.get(groupposition);

        if (!editex) {

            mydb.saveExerciseName(the_date, exname);
            //theExercise.add(newEx);


            SetsDialogFragmentBlur setsDialog = new SetsDialogFragmentBlur();

            Integer index = 2;
            if (!(ExIndex.size() == 0)) {
                index = ExIndex.get(ExIndex.size() - 1) + 1;
            }


            setsDialog.setStuff(exhead,exname, the_date, index);
            setsDialog.show(getFragmentManager(), "Diag");


            if (!(ExIndex.size() == 0)) {
                ExIndex.add(ExIndex.get(ExIndex.size() - 1) + 1);
            } else {
                ExIndex.add(2);
            }

            theAdapter.setNotifyOnChange(true);
            theAdapter.add(exname);
        }else{
            Toast.makeText(getApplicationContext(), listDataChild.get(listDataHeader.get(groupposition)).get(childposition), Toast.LENGTH_SHORT).show();

            EditExDialogFragmentBlur setsDialog = new EditExDialogFragmentBlur();

            setsDialog.setStuff(exhead,exname);
            setsDialog.show(getFragmentManager(), "Diag");
            editex = false;
            toolbarisshown = false;
            NewEx.setImageResource(R.drawable.addnewcross);
        }
    }

    public String getHeadfromHMvalue(HashMap<String, List<String>> hm, String value){
        String head;

        for (Map.Entry<String, List<String>> entry : hm.entrySet()) {
            for (String temp : entry.getValue()){
                if (Objects.equals(value, temp)) {
                    return entry.getKey();
                }
            }

        }
        return "Rücken";

    }

}
