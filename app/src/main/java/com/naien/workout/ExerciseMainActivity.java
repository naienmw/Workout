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

    }





    public void AddNewSet(View view) {

        newSetReps = (EditText) findViewById(R.id.user_reps_input);

        newSetWeight = (EditText) findViewById(R.id.user_weight_input);
        Intent i = getIntent();

        count_ex = mydb.getProfilesCount(the_date);

        if (!newSetReps.getText().toString().matches("")){
            if (!newSetWeight.getText().toString().matches("")){
                theSets[count_sets-1] = newSetReps.getText().toString() + " x " + newSetWeight.getText().toString();

                theAdapter = new my_adapter_sets(this, theSets);
                ListView theListView = (ListView) findViewById(R.id.listview_sets);
                theListView.setAdapter(theAdapter);

                String dbString = newSetReps.getText().toString()+","+newSetWeight.getText().toString();
                mydb.put_set(the_date,count_ex,count_sets,dbString);
                count_sets = count_sets + 1;
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
