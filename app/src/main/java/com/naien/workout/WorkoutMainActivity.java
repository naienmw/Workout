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
    String[] theExercise = new String[100];
    Integer count = 1;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.workout_main);

        Intent i = getIntent();

        the_date = i.getStringExtra("date");
        the_workout = i.getStringExtra("workout_name");

        TextView my_workout = (TextView) findViewById(R.id.workout_name);
        my_workout.setText(the_workout);


        mydb = new DBHelper(this);
    }


    public void AddNewExercise(View view) {

        EditText newExercise = (EditText) findViewById(R.id.user_Exercise_Input);

        theExercise[count] = newExercise.getText().toString();

        if (!theExercise[count].matches("")) {

            Intent workout_main = new Intent(this, ExerciseMainActivity.class);

            theAdapter = new my_adapter(this, theExercise);
            ListView theListView = (ListView) findViewById(R.id.listview_exercises);
            theListView.setAdapter(theAdapter);

            newExercise.setText("");

            mydb.saveExerciseName(the_date, theExercise[count]);

            workout_main.putExtra("exercise",theExercise[count]);
            workout_main.putExtra("ex",count);
            workout_main.putExtra("date",the_date);

            count = count + 1;



            startActivity(workout_main);

        }else{
            Toast.makeText(WorkoutMainActivity.this, "Please Enter a Exercise", Toast.LENGTH_SHORT).show();
        }
    }
}
