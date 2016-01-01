package com.naien.workout;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.tvbarthel.lib.blurdialogfragment.BlurDialogFragment;


public class WorkoutMainActivity extends Activity{

    DBHelper mydb;

    String the_date;
    String the_workout;
    ArrayAdapter theAdapter;
    ArrayList<String> theExercise;
    FloatingActionButton NewEx;
    ArrayList<Integer> ExIndex;
    String PrimaryWorkout;
    ImageView ExPic;


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
        Intent i = getIntent();

        the_date = i.getStringExtra("date");
        the_workout = i.getStringExtra("workout_name");

        PrimaryWorkout = getPrimaryWorkout(the_workout);
        ExPic = (ImageView)findViewById(R.id.picture_exercise);

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

        NewEx.setBackgroundTintList(getResources().getColorStateList(R.color.colorRed));

        theAdapter = new my_adapter(this,theExercise);
        //ListView theListView = (ListView) findViewById(R.id.listview_exercises);
        theListView.setAdapter(theAdapter);

        theListView.setClickable(true);

        theListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
             @Override
             public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                 String Ex = theAdapter.getItem(position).toString();
                 mydb.deleteExinWO_index(the_date, ExIndex.get(position));

                 theAdapter.remove(Ex);
                 theAdapter.setNotifyOnChange(true);
                 onResume();
                 return true;
             }
         });


        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                final String exercise_name = theExercise.get(position);

                //////BLUR SEEMS TO WORK JUST FINE --> KEEP IT THIS WAY/////////
                SetsDialogFragmentBlur setsDialog = new SetsDialogFragmentBlur();
                setsDialog.setStuff(exercise_name,the_date,ExIndex.get(position));
                setsDialog.show(getFragmentManager(),"Diag");

                ///////////////////////////

/*

                final Dialog dialog = new Dialog(WorkoutMainActivity.this);

                dialog.setContentView(R.layout.exercise_main);
                dialog.setTitle(exercise_name);

                TextView ex = (TextView) dialog.findViewById(R.id.workout_name_in_ex);
                ListView sets = (ListView) dialog.findViewById(R.id.listview_sets);
                Button exit = (Button) dialog.findViewById(R.id.sets_exit_button);
                Button addSet =(Button) dialog.findViewById(R.id.add_set_button);
                final EditText user_input_reps = (EditText) dialog.findViewById(R.id.user_reps_input);
                final EditText user_input_weight = (EditText) dialog.findViewById(R.id.user_weight_input);

                ex.setText(theExercise[position]);

                final ArrayList<String> allSets;

                mydb = new DBHelper(WorkoutMainActivity.this);
                allSets = mydb.getAllSets(the_date,exercise_name);

                ArrayList<String> theSets_fine = new ArrayList<String>();

                for (String temp : allSets) {
                    String[] temp_div = temp.split(",");
                    theSets_fine.add(temp_div[0] + " x " + temp_div[1]);
                }

                theAdapter = new my_adapter_sets_arraylist(WorkoutMainActivity.this,theSets_fine);
                sets.setAdapter(theAdapter);

                exit.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        dialog.dismiss();
                    }

                });

                addSet.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        Integer count_ex = mydb.getExIndex(the_date,exercise_name); //number of Exercise in Workout

                        Integer count_sets = allSets.size();

                        if (!user_input_reps.getText().toString().matches("")){
                            if (!user_input_weight.getText().toString().matches("")){

                                String newEx = user_input_reps.getText().toString() + "," + user_input_weight.getText().toString();
                                allSets.add(newEx);

                                ArrayList<String> theSets_fine = new ArrayList<String>();

                                for (String temp : allSets) {
                                    String[] temp_div = temp.split(",");
                                    theSets_fine.add(temp_div[0] + " x " + temp_div[1]);
                                }

                                theAdapter = new my_adapter_sets_arraylist(WorkoutMainActivity.this, theSets_fine);
                                ListView theListView = (ListView) dialog.findViewById(R.id.listview_sets);
                                theListView.setAdapter(theAdapter);

                                mydb.put_set(the_date,count_ex,count_sets+1,newEx);


                            }else{
                                Toast.makeText(WorkoutMainActivity.this,"Give me some weight",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(WorkoutMainActivity.this,"Give me some Reps",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();*/
                ///////////////////////

                //Just uncomment and use for old view of Sets with new Activitiy (ExMain)
                /*Intent ex = new Intent(WorkoutMainActivity.this,ExerciseMainActivity.class);

                    ex.putExtra("exercise",theExercise[position]);
                    ex.putExtra("date",the_date);
                startActivity(ex);*/

            }
        }
        );
    }



    public void AddNewExercise(View view) {

        EditText newExercise = (EditText) findViewById(R.id.user_Exercise_Input);
        String newEx = newExercise.getText().toString();


        if (!newEx.matches("")) {

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



            //theAdapter.add(newExercise.getText().toString());

            /*Intent workout_main = new Intent(this, ExerciseMainActivity.class);

            workout_main.putExtra("exercise",newExercise.getText().toString());

            workout_main.putExtra("date", the_date);

            startActivity(workout_main);*/

        }else{
            Toast.makeText(WorkoutMainActivity.this, "Please Enter a Exercise", Toast.LENGTH_SHORT).show();
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
}
