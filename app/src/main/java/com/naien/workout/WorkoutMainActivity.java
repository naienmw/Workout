package com.naien.workout;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class WorkoutMainActivity extends Activity{

    DBHelper mydb;

    String the_date;
    String the_workout;
    ListAdapter theAdapter;
    String[] theExercise;


    protected void onCreate(Bundle savedInstanceState) {

        theExercise = new String[100];

        super.onCreate(savedInstanceState);

    }

     @Override
    public void onResume(){
        super.onResume();
        this.setContentView(R.layout.workout_main);
        mydb = new DBHelper(this);
        Intent i = getIntent();

        the_date = i.getStringExtra("date");
        the_workout = i.getStringExtra("workout_name");

        TextView my_workout = (TextView) findViewById(R.id.workout_name);
        my_workout.setText(the_workout);

        theExercise = mydb.getAllExercises(the_date);


        theAdapter = new my_adapter(this,theExercise);
        ListView theListView = (ListView) findViewById(R.id.listview_exercises);
        theListView.setAdapter(theAdapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //NEW
                ////////////////

                final String exercise_name = theExercise[position];
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

                dialog.show();
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


        if (!newExercise.getText().toString().matches("")) {

            Intent workout_main = new Intent(this, ExerciseMainActivity.class);

            mydb.saveExerciseName(the_date, newExercise.getText().toString());

            workout_main.putExtra("exercise",newExercise.getText().toString());

            workout_main.putExtra("date", the_date);
            newExercise.setText("");


            startActivity(workout_main);

        }else{
            Toast.makeText(WorkoutMainActivity.this, "Please Enter a Exercise", Toast.LENGTH_SHORT).show();
        }
    }
}
