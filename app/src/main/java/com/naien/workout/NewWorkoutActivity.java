package com.naien.workout;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;

public class NewWorkoutActivity extends AppCompatActivity {

    String[] allPossibleWorkouts = {"Brust","Rücken","Beine","Schultern"};
    String[] myChoice;
    ListAdapter theAdapter;
    TextView theCombi;
    ListView theWO;
    String newcombi;
    DBHelper mydb;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_choice_new);

        mydb = new DBHelper(this);
        theWO = (ListView) findViewById(R.id.workout_choice);
        theCombi = (TextView) findViewById(R.id.workout_combi);

        theAdapter = new my_adapter_sets(this,allPossibleWorkouts);
        theWO.setAdapter(theAdapter);

        theWO.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String temp = String.valueOf(adapterView.getItemAtPosition(i));

                Toast.makeText(NewWorkoutActivity.this, temp, Toast.LENGTH_SHORT).show();

                if (!theCombi.getText().toString().contains(temp)){
                    if(theCombi.getText().toString().equals("")) {
                        newcombi = temp;
                        theCombi.setText(newcombi);
                    }else{
                        newcombi = theCombi.getText().toString() + "/" + temp;
                        theCombi.setText(newcombi);
                    }
                }else{
                    newcombi = newcombi.replace(temp,"");
                    newcombi = newcombi.replace("//","/");
                    if(!newcombi.isEmpty()) {
                        if (newcombi.substring(newcombi.length() - 1).equals("/")) {
                            newcombi = newcombi.substring(0, newcombi.length() - 1);
                        }
                    }

                    if(!newcombi.isEmpty()){
                        if(newcombi.startsWith("/")){
                            newcombi = newcombi.substring(1);
                        }
                    }

                    theCombi.setText(newcombi);
                }
            }
        });
    }




    public void composeWorkout(View view) {

        String user_Workout = theCombi.getText().toString();
        Intent i = getIntent();
        String date_db = i.getStringExtra("date");

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