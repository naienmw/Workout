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

import java.util.ArrayList;
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
    ArrayList <String> allSets;




    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.exercise_main_static);

        mydb = new DBHelper(this);

        Intent i = getIntent();
        exercise_name = i.getStringExtra("exercise_static");
        the_date = i.getStringExtra("date_static");  //das ausgewählte Datum

        exercise = (TextView) findViewById(R.id.workout_name_in_ex_static);
        exercise.setText(exercise_name);

        allSets = mydb.getAllSets(the_date,exercise_name);
        ArrayList<String> theSets_fine = new ArrayList<String>();

        for (String temp : allSets) {
            String[] temp_div = temp.split(",");
            theSets_fine.add(temp_div[0] + " x " + temp_div[1]);
        }

        ListView sets = (ListView) findViewById(R.id.listview_sets_static);

        theAdapter = new my_adapter_sets_arraylist(this,theSets_fine);

        sets.setAdapter(theAdapter);

        sets.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //FIRST ITEM IN LISTVIEW HAS i=0, DB set index starts with 1
                //This is useless, old Workouts should not be editable!!
                /*
                mydb.deleteSetinEx(the_date, exercise_name, i + 1);

                ListView sets = (ListView) findViewById(R.id.listview_sets_static);

                allSets = mydb.getAllSets(the_date,exercise_name);
                ArrayList<String> theSets_fine = new ArrayList<String>();


                for (String temp : allSets) {
                    String[] temp_div = temp.split(",");
                    theSets_fine.add(temp_div[0] + " x " + temp_div[1]);
                }

                theAdapter = new my_adapter_sets_arraylist(ExerciseMainStaticActivity.this,theSets_fine);

                sets.setAdapter(theAdapter);
                */

            }

        });

    }


}
