package com.naien.workout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ExerciseMainActivity extends Activity {

    DBHelper mydb;
    EditText newSetReps;
    EditText newSetWeight;
    Integer count_sets = 1;
    String exercise_name;
    Integer count_ex;
    ListAdapter theAdapter;
    TextView exercise;
    String[] theSets = new String[14];
    String the_date;
    ArrayList<String> allSets;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.exercise_main);

        mydb = new DBHelper(this);


        Intent i = getIntent();
        exercise_name = i.getStringExtra("exercise");
        the_date = i.getStringExtra("date");  //das AKTUELLE Datum
        //count_sets = i.getIntExtra("sets", 1);

        exercise = (TextView) findViewById(R.id.workout_name_in_ex);
        exercise.setText(exercise_name);


        //String test = mydb.getAllSets(the_date,exercise_name).get(0);
        //Toast.makeText(ExerciseMainActivity.this,test,Toast.LENGTH_SHORT).show();

        allSets = mydb.getAllSets(the_date,exercise_name);

        ArrayList<String> theSets_fine = new ArrayList<String>();

        for (String temp : allSets) {
            String[] temp_div = temp.split(",");
            theSets_fine.add(temp_div[0] + " x " + temp_div[1]);
        }

        ListView sets = (ListView) findViewById(R.id.listview_sets);

        theAdapter = new my_adapter_sets_arraylist(this,theSets_fine);

        sets.setAdapter(theAdapter);

    }


    public void AddNewSet(View view) {

        newSetReps = (EditText) findViewById(R.id.user_reps_input);

        newSetWeight = (EditText) findViewById(R.id.user_weight_input);
        Intent i = getIntent();

        count_ex = mydb.getExIndex(the_date,exercise_name); //number of Exercise in Workout

        count_sets = allSets.size();

        if (!newSetReps.getText().toString().matches("")){
            if (!newSetWeight.getText().toString().matches("")){

                //theSets[count_sets] = newSetReps.getText().toString() + " x " + newSetWeight.getText().toString();

                String newEx = newSetReps.getText().toString() + "," + newSetWeight.getText().toString();
                allSets.add(newEx);
                /*String[] theSets_fine = new String[count_sets+1];
                for (int k = 0;k<count_sets;k++){
                    theSets_fine[k] = theSets[k];
                }*/

                ArrayList<String> theSets_fine = new ArrayList<String>();

                for (String temp : allSets) {
                    String[] temp_div = temp.split(",");
                    theSets_fine.add(temp_div[0] + " x " + temp_div[1]);
                }


                theAdapter = new my_adapter_sets_arraylist(this, theSets_fine);
                ListView theListView = (ListView) findViewById(R.id.listview_sets);
                theListView.setAdapter(theAdapter);

                //String dbString = newSetReps.getText().toString()+","+newSetWeight.getText().toString();
                mydb.put_set(the_date,count_ex,count_sets+1,newEx);
                //count_sets = count_sets + 1;

            }else{
                Toast.makeText(this,"Give me some weight",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"Give me some Reps",Toast.LENGTH_SHORT).show();
        }



    }

    public void finish(View view){
        //super.onResume();
        finish();
    }

}
