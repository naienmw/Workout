package com.naien.workout;

        import android.content.Intent;
        import android.content.res.ColorStateList;
        import android.graphics.Color;
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
        import android.widget.AdapterView;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.ListAdapter;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.w3c.dom.Text;

        import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText user_Workout_input;
    DBHelper mydb;
    String date_db;
    String allWorkouts[][];
    String allWorkoutsListView[][];
    ListAdapter multiRowAdapter;
    FloatingActionButton myFAB;

    protected void onCreate(Bundle savedInstanceState) {

        allWorkouts = new String[1000][2];
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydb = new DBHelper(this);

        //user_Workout_input = (EditText) findViewById(R.id.user_Workout_input);
        myFAB = (FloatingActionButton) findViewById(R.id.fabAddWorkout);

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        date_db = "d"+Integer.toString(day) +"_" + Integer.toString(month) +"_"+Integer.toString(year);

        if(mydb.doesTableExist(mydb.getdb(),date_db)) {
            myFAB.setBackgroundTintList(getResources().getColorStateList(R.color.colorRed));
            myFAB.setImageResource(R.drawable.addnewexisting);
        }

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
        //}
    }

    public void onResume(){
        super.onResume();

        allWorkouts = mydb.getAllWorkouts();

        Integer rows = allWorkouts.length;
        allWorkoutsListView = new String[rows][2];

        myFAB = (FloatingActionButton) findViewById(R.id.fabAddWorkout);
        if(mydb.doesTableExist(mydb.getdb(),date_db)) {
            myFAB.setBackgroundTintList(getResources().getColorStateList(R.color.colorRed));
            myFAB.setImageResource(R.drawable.addnewexisting);
        }


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

                TextView v = (TextView) findViewById(R.id.textViewDate);
                TextView v1 = (TextView) findViewById(R.id.textViewWO);

                String tempdate = multiRowAdapter.getItem(i).toString();
                String tempdatedb = DateToDB(tempdate);

                Toast.makeText(MainActivity.this,tempdatedb,Toast.LENGTH_SHORT).show();

                if(!tempdatedb.equals(date_db)) {
                    Intent staticWO = new Intent(MainActivity.this, WorkoutMainStaticActivity.class);

                    staticWO.putExtra("date_static", tempdatedb);
                    staticWO.putExtra("workout_name_static", v1.getText().toString());

                    startActivity(staticWO);
                }else{
                    Intent normalWO = new Intent(MainActivity.this, WorkoutMainActivity.class);

                    normalWO.putExtra("date", tempdatedb);
                    normalWO.putExtra("workout_name", v1.getText().toString());

                    startActivity(normalWO);
                }
                //Toast.makeText(MainActivity.this, tempdatedb, Toast.LENGTH_SHORT).show();
            }

        });

    }

    public void makeNewWorkout(View view){

        if(!mydb.doesTableExist(mydb.getdb(),date_db)) {
            Intent test = new Intent(this,NewWorkoutActivity.class);
            test.putExtra("date",date_db);
            startActivity(test);
        }else{
            Intent workout_main = new Intent(this, WorkoutMainActivity.class);
            workout_main.putExtra("workout_name", mydb.getWoName(date_db));
            workout_main.putExtra("date", date_db);
            startActivity(workout_main);
        }



       /* String user_Workout =  user_Workout_input.getText().toString();
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
        }*/
    }

    public String DateToDB(String date){

        String[] parts = date.split("\\.");
        date = "d" + parts[0] + "_" + parts[1] + "_"+parts[2];
        return date;

    }



}