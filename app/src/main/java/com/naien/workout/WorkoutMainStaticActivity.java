package com.naien.workout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class WorkoutMainStaticActivity extends Activity{

    DBHelper mydb;

    String the_date;
    String the_workout;
    ListAdapter theAdapter;
    ArrayList<String> theExercise;

    protected void onCreate(Bundle savedInstanceState) {

        //theExercise = new String[100];

        super.onCreate(savedInstanceState);

    }

     @Override
    public void onResume(){
        super.onResume();
        this.setContentView(R.layout.workout_main_static);
        mydb = new DBHelper(this);
        Intent i = getIntent();

        the_date = i.getStringExtra("date_static");
        the_workout = i.getStringExtra("workout_name_static");

        TextView my_workout = (TextView) findViewById(R.id.workout_name_static);
        my_workout.setText(the_workout);

        theExercise = mydb.getAllExercises_Arraylist(the_date);


        theAdapter = new my_adapter(this,theExercise);
        ListView theListView = (ListView) findViewById(R.id.listview_exercises_static);
        theListView.setAdapter(theAdapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                 String theexercise = theExercise.get(i);

                 //final String exercise_name = theExercise[i];

                 //////BLUR SEEMS TO WORK JUST FINE --> KEEP IT THIS WAY/////////
                 SetsDialogFragmentBlur_static setsDialog = new SetsDialogFragmentBlur_static();
                 setsDialog.setStuff(theexercise,the_date);
                 setsDialog.show(getFragmentManager(),"Diag");

                 ///////////////////////////


                 /*Intent ExStatic = new Intent(WorkoutMainStaticActivity.this, ExerciseMainStaticActivity.class);

                 ExStatic.putExtra("date_static",the_date);
                 ExStatic.putExtra("exercise_static",theexercise);

                 startActivity(ExStatic);*/
             }

         });


    }


}
