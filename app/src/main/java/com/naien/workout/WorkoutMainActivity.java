package com.naien.workout;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;


public class WorkoutMainActivity extends Activity{

    DBHelper mydb;

    String the_date;
    String the_workout;
    ListAdapter theAdapter;
    String[] theExercise = new String[100];
    Integer count;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.workout_main);

        Intent i = getIntent();

        the_date = i.getStringExtra("date");
        the_workout = i.getStringExtra("workout_name");

        TextView my_workout = (TextView) findViewById(R.id.workout_name);
        my_workout.setText(the_workout);

        count = 0;
    }


    public void AddNewExercise(View view) {

        EditText newExercise = (EditText) findViewById(R.id.user_Exercise_Input);

        theExercise[count] = newExercise.getText().toString();

        theAdapter = new my_adapter(this, theExercise);
        ListView theListView = (ListView) findViewById(R.id.listview_exercises);
        theListView.setAdapter(theAdapter);

        count = count + 1;
    }
}
