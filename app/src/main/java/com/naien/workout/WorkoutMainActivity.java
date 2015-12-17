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
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;


public class WorkoutMainActivity extends Activity{

    DBHelper mydb;

    String the_date;
    String the_workout;
    ListAdapter theAdapter;
    String[] theExercise;

    Integer count;

    protected void onCreate(Bundle savedInstanceState) {

        theExercise = new String[100];

        super.onCreate(savedInstanceState);
        /*
        this.setContentView(R.layout.workout_main);
        mydb = new DBHelper(this);
        Intent i = getIntent();

        the_date = i.getStringExtra("date");
        the_workout = i.getStringExtra("workout_name");
        count = i.getIntExtra("sets", 2);
        TextView my_workout = (TextView) findViewById(R.id.workout_name);
        my_workout.setText(the_workout);

        theExercise = mydb.getAllExercises(the_date);
        theAdapter = new my_adapter_sets(this,theExercise);
        ListView theListView = (ListView) findViewById(R.id.listview_exercises);
        theListView.setAdapter(theAdapter);*/

    }

     @Override
    public void onResume(){
        super.onResume();
        this.setContentView(R.layout.workout_main);
        mydb = new DBHelper(this);
        Intent i = getIntent();

        the_date = i.getStringExtra("date");
        the_workout = i.getStringExtra("workout_name");
        count = i.getIntExtra("sets", 2);
        TextView my_workout = (TextView) findViewById(R.id.workout_name);
        my_workout.setText(the_workout);

        theExercise = mydb.getAllExercises(the_date);
        theAdapter = new my_adapter_sets(this,theExercise);
        ListView theListView = (ListView) findViewById(R.id.listview_exercises);
        theListView.setAdapter(theAdapter);
    }


    public void AddNewExercise(View view) {

        EditText newExercise = (EditText) findViewById(R.id.user_Exercise_Input);

        //Integer temp = count-1;

        //theExercise[temp] = ;

        if (!newExercise.getText().toString().matches("")) {

            Intent workout_main = new Intent(this, ExerciseMainActivity.class);

            /*theAdapter = new my_adapter(this, theExercise);
            ListView theListView = (ListView) findViewById(R.id.listview_exercises);
            theListView.setAdapter(theAdapter);*/

            mydb.saveExerciseName(the_date, newExercise.getText().toString());

            workout_main.putExtra("exercise",newExercise.getText().toString());
            workout_main.putExtra("ex",count);
            workout_main.putExtra("date",the_date);
            newExercise.setText("");

            count = count + 1;


            startActivity(workout_main);

        }else{
            Toast.makeText(WorkoutMainActivity.this, "Please Enter a Exercise", Toast.LENGTH_SHORT).show();
        }
    }
}
