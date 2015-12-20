package com.naien.workout;

        import android.content.Intent;
        import android.graphics.PorterDuff;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.ListAdapter;
        import android.widget.ListView;
        import android.widget.Toast;

        import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText user_Workout_input;
    DBHelper mydb;
    String date_db;
    String allWorkouts[][];
    String allWorkoutsListView[][];
    ListAdapter multiRowAdapter;

    protected void onCreate(Bundle savedInstanceState) {

        allWorkouts = new String[1000][2];
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydb = new DBHelper(this);

        user_Workout_input = (EditText) findViewById(R.id.user_Workout_input);

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        date_db = "d"+Integer.toString(day) +"_" + Integer.toString(month) +"_"+Integer.toString(year);

        //if(mydb.doesTableExist(mydb.getdb(),date_db)){

            allWorkouts = mydb.getAllWorkouts();

            Integer rows = allWorkouts.length;
            allWorkoutsListView = new String[rows][2];

        for (int i=0;i<rows;i++){
            String[] parts = allWorkouts[i][0].substring(1).split("_");
            String date = parts[0] + "." + parts[1] + "." + parts[2];
            allWorkoutsListView[i][0] = allWorkouts[i][1];
            allWorkoutsListView[i][1] = date;
        }

            /*for (int i=0;i<rows;i++){
                allWorkoutsListView[i] = allWorkouts[i][0].substring(1) + " - " + allWorkouts[i][1];
            }*/


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


        for (int i=0;i<rows;i++){
            String[] parts = allWorkouts[i][0].substring(1).split("_");
            String date = parts[0] + "." + parts[1] + "." + parts[2];
            allWorkoutsListView[i][0] = allWorkouts[i][1];
            allWorkoutsListView[i][1] = date;
        }

        multiRowAdapter = new my_adapter_multiCol(this, allWorkoutsListView);
        ListView theListView = (ListView) findViewById(R.id.ListViewWorkouts);
        theListView.setAdapter(multiRowAdapter);

    }


    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                ImageButton view = (ImageButton ) v;
                view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                v.invalidate();
                break;
            }
            case MotionEvent.ACTION_UP:

                // Your action here on button click

            case MotionEvent.ACTION_CANCEL: {
                ImageButton view = (ImageButton) v;
                view.getBackground().clearColorFilter();
                view.invalidate();
                break;
            }
        }
        return true;
    }

    public void makeNewWorkout(View view){

        String user_Workout =  user_Workout_input.getText().toString();
        if (!user_Workout.matches("")) {
            Boolean blub;
            blub = mydb.doesTableExist(mydb.getdb(),date_db);
            if (!blub) {
                Intent workout_main = new Intent(this, WorkoutMainActivity.class);
                workout_main.putExtra("workout_name", user_Workout);
                workout_main.putExtra("date", date_db);
                startActivity(workout_main);
                mydb.create_new_table(mydb.getdb(), date_db);
                mydb.saveExerciseName(date_db, user_Workout);
            }else{

                Integer NoOfEx = mydb.getProfilesCount(date_db);

                Toast.makeText(this, "Date existing, ExCount is " + NoOfEx, Toast.LENGTH_SHORT).show();

                Intent workout_main = new Intent(this, WorkoutMainActivity.class);
                workout_main.putExtra("workout_name",mydb.getWoName(date_db));
                workout_main.putExtra("date", date_db);
                //Integer temp = NoOfEx+1;
                //workout_main.putExtra("sets",temp);

                startActivity(workout_main);

            }
        }else{
            Toast.makeText(this, "Give me a Workout name", Toast.LENGTH_SHORT).show();
        }
    }
}