package com.naien.workout;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;

public class AllWorkoutsActivity extends AppCompatActivity {

    EditText user_Workout_input;
    DBHelper mydb;
    String date_db;
    String allWorkouts[][];
    String allWorkoutsListView[][];
    ListAdapter multiRowAdapter;
    FloatingActionButton myFAB;
    TextView infotext;
    ImageView arrow;

    protected void onCreate(Bundle savedInstanceState) {



        allWorkouts = new String[1000][2];
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_workouts);

        infotext = (TextView) findViewById(R.id.InfoNewWorkout);
        arrow = (ImageView) findViewById(R.id.infoarrow);
        mydb = new DBHelper(this);
        myFAB = (FloatingActionButton) findViewById(R.id.fabAddWorkout);



        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH) +1;
        int year = c.get(Calendar.YEAR);
        date_db = "d"+Integer.toString(day) +"_" + Integer.toString(month) +"_"+Integer.toString(year);


       /* if(mydb.doesTableExist(mydb.getdb(), date_db)) {
            myFAB.setBackgroundTintList(getResources().getColorStateList(R.color.colorPurple));
            myFAB.setImageResource(R.drawable.addnewexisting);
            infotext.setText("Edit today's Workout");
            arrow.setImageResource(R.drawable.arrow_edit);

        }*/

            allWorkouts = mydb.getAllWorkouts();

            Integer rows = allWorkouts.length;
            allWorkoutsListView = new String[rows][2];

        allWorkouts = invertArray(allWorkouts);

        for (int i=0;i<rows;i++){
            String[] parts = allWorkouts[i][0].substring(1).split("_");
            String date = parts[0] + "." + parts[1] + "." + parts[2];
            allWorkoutsListView[i][0] = allWorkouts[i][1];
            allWorkoutsListView[i][1] = date;
        }

            multiRowAdapter = new my_adapter_multiCol(this, allWorkoutsListView);
            ListView theListView = (ListView) findViewById(R.id.ListViewWorkouts);
            theListView.setAdapter(multiRowAdapter);
        //}

    }

    public void onResume(){
        super.onResume();

        allWorkouts = mydb.getAllWorkouts();

        Integer rows = allWorkouts.length;
        allWorkoutsListView = new String[rows][2];

        allWorkouts = invertArray(allWorkouts);

        /*myFAB = (FloatingActionButton) findViewById(R.id.fabAddWorkout);
        if(mydb.doesTableExist(mydb.getdb(),date_db)) {
            myFAB.setBackgroundTintList(getResources().getColorStateList(R.color.colorGreen));
            myFAB.setImageResource(R.drawable.addnewexisting);
            infotext.setText("Edit today's Workout");
            arrow.setImageResource(R.drawable.arrow_edit);

        }*/


        for (int i=0;i<rows;i++){
            String[] parts = allWorkouts[i][0].substring(1).split("_");
            String date = parts[0] + "." + parts[1] + "." + parts[2];
            allWorkoutsListView[i][0] = allWorkouts[i][1];
            allWorkoutsListView[i][1] = date;
        }



        multiRowAdapter = new my_adapter_multiCol(this, allWorkoutsListView);
        ListView theListView = (ListView) findViewById(R.id.ListViewWorkouts);
        theListView.setAdapter(multiRowAdapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //FIRST ITEM IN LISTVIEW HAS i = 0!!!

                TextView v1 = (TextView) findViewById(R.id.textViewWO_multi);

                String tempdate = allWorkoutsListView[i][1];
                String tempdatedb = DateToDB(tempdate);
                String workoutstatic = allWorkoutsListView[i][0];


                if (!tempdatedb.equals(date_db)) {
                    Intent staticWO = new Intent(AllWorkoutsActivity.this, WorkoutMainStaticActivity.class);

                    staticWO.putExtra("date_static", tempdatedb);
                    staticWO.putExtra("workout_name_static", workoutstatic);

                    startActivity(staticWO);
                } else {
                    Intent normalWO = new Intent(AllWorkoutsActivity.this, WorkoutMainActivity.class);

                    normalWO.putExtra("date", tempdatedb);
                    normalWO.putExtra("workout_name", v1.getText().toString());

                    startActivity(normalWO);
                }
            }

        });

    }


    public String DateToDB(String date){

        String[] parts = date.split("\\.");
        date = "d" + parts[0] + "_" + parts[1] + "_"+parts[2];
        return date;

    }

    public String FineSets(String setsfromdb){
        String finesets;

        String[] temp = setsfromdb.split(",");

        finesets = temp[0] + " x " + temp[1];

        return finesets;
    }

    public String[][] invertArray(String[][] theArray){

        String[][] blub = new String[theArray.length][2];

        for (int i = 0;i<theArray.length;i++){
            blub[i][0] = theArray[theArray.length-i-1][0];
            blub[i][1] = theArray[theArray.length-i-1][1];
        }

        return blub;
    }



}