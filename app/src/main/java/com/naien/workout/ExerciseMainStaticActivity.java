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

import java.util.List;


public class ExerciseMainStaticActivity extends Activity {

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
        this.setContentView(R.layout.exercise_main_static);

        mydb = new DBHelper(this);

        Intent i = getIntent();
        exercise_name = i.getStringExtra("exercise_static");
        the_date = i.getStringExtra("date_static");  //das ausgewählte Datum

        exercise = (TextView) findViewById(R.id.workout_name_in_ex_static);
        exercise.setText(exercise_name);

        String test = mydb.getAllSets(the_date,exercise_name).get(0);
        Toast.makeText(ExerciseMainStaticActivity.this,test,Toast.LENGTH_SHORT).show();

        ListView sets = (ListView) findViewById(R.id.listview_sets_static);

        theAdapter = new my_adapter_sets_arraylist(this,mydb.getAllSets(the_date,exercise_name));

        sets.setAdapter(theAdapter);

    }


}
