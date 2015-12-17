package com.naien.workout;

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.View;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.EditText;
        import android.widget.ListAdapter;
        import android.widget.ListView;
        import android.widget.Toast;

        import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText user_Workout_input;
    DBHelper mydb;
    String date_db;
    String allWorkouts[][];
    String allWorkoutsListView[];
    ListAdapter theAdapter;

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

        if(mydb.doesTableExist(mydb.getdb(),date_db)){

            allWorkouts = mydb.getAllWorkouts();

            Integer rows = allWorkouts.length;
            allWorkoutsListView = new String[rows];


            for (int i=0;i<rows;i++){
                allWorkoutsListView[i] = allWorkouts[i][0] + " - " + allWorkouts[i][1];
            }


            theAdapter = new my_adapter_sets(this, allWorkoutsListView);
            ListView theListView = (ListView) findViewById(R.id.ListViewWorkouts);
            theListView.setAdapter(theAdapter);
        }
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
                Integer temp = NoOfEx+1;
                workout_main.putExtra("sets",temp);

                startActivity(workout_main);

            }
        }else{
            Toast.makeText(this, "Give me a Workout name", Toast.LENGTH_SHORT).show();
        }
    }
}