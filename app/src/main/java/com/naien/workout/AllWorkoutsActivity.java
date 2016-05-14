package com.naien.workout;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AllWorkoutsActivity extends AppCompatActivity {

    EditText user_Workout_input;
    DBHelper mydb;
    String date_db;
    String allWorkouts[][];
    String allWorkoutsListView[][];
    ListAdapter multiRowAdapter;
    ListAdapter multiRowAdapter_arraylist;
    FloatingActionButton myFAB;
    TextView infotext;
    ImageView arrow;
    ArrayList<ArrayList<String>> allWorkouts_Array = new ArrayList<>();
    ArrayList<ArrayList<String>> allWorkouts_Array_list = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_workouts);


        mydb = new DBHelper(this);
        myFAB = (FloatingActionButton) findViewById(R.id.fabAddWorkout);


        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH) +1;
        int year = c.get(Calendar.YEAR);
        date_db = "d"+Integer.toString(day) +"_" + Integer.toString(month) +"_"+Integer.toString(year);




    }

    public void onResume(){
        super.onResume();

        //allWorkouts = mydb.getAllWorkouts();

        //Integer rows = allWorkouts.length;
        //allWorkoutsListView = new String[rows][2];

        //allWorkouts = invertArray(allWorkouts);

        allWorkouts_Array_list.clear();
        allWorkouts_Array.clear();

        allWorkouts_Array = mydb.getAllWorkouts_Arraylist();
        allWorkouts_Array = invertArraylist(allWorkouts_Array);





        for (int i=0;i<allWorkouts_Array.size();i++){
            ArrayList<String> temp = new ArrayList<>();

            String[] parts = allWorkouts_Array.get(i).get(1).substring(1).split("_");
            String date = parts[0] + "." + parts[1] + "." + parts[2];
            temp.add(allWorkouts_Array.get(i).get(0));
            temp.add(date);
            allWorkouts_Array_list.add(temp);
        }




        final ListAdapter theAdapter = new my_adapter_multiCol_arraylist(this,allWorkouts_Array_list);

        ListView theListView = (ListView) findViewById(R.id.ListViewWorkouts);
        theListView.setAdapter(theAdapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //FIRST ITEM IN LISTVIEW HAS i = 0!!!

                TextView v1 = (TextView) findViewById(R.id.textViewWO_multi);

                String tempdate = allWorkouts_Array_list.get(i).get(1);//allWorkoutsListView[i][1];
                String tempdatedb = DateToDB(tempdate);
                String workoutstatic = allWorkouts_Array_list.get(i).get(0);//allWorkoutsListView[i][0];

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

    public ArrayList<ArrayList<String>> invertArraylist(ArrayList<ArrayList<String>> list){
        ArrayList<ArrayList<String>> returnlist = new ArrayList<ArrayList<String>>();


        for (int i = 0;i<list.size();i++){
            ArrayList<String> workout = new ArrayList<>();
            workout.add(list.get(list.size()-i-1).get(0));
            workout.add(list.get(list.size()-i-1).get(1));
            returnlist.add(workout);
        }

        return returnlist;
    }



}