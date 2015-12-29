package com.naien.workout;

        import android.content.Intent;
        import android.content.res.ColorStateList;
        import android.graphics.Color;
        import android.graphics.PorterDuff;
        import android.os.Bundle;
        import android.support.annotation.DrawableRes;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.FrameLayout;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.ListAdapter;
        import android.widget.ListView;
        import android.widget.RelativeLayout;
        import android.widget.RelativeLayout.LayoutParams;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.w3c.dom.Text;

        import java.util.ArrayList;
        import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    //EditText user_Workout_input;
    DBHelper mydb;
    String date_db;

    //ListAdapter multiRowAdapter;
    FloatingActionButton myFAB;
    TextView infotext;
    ImageView arrow;
    TextView currentWorkout;
    Button showAll;


    protected void onCreate(Bundle savedInstanceState) {

        //allWorkouts = new String[1000][2];
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* infotext = (TextView) findViewById(R.id.InfoNewWorkout);
        arrow = (ImageView) findViewById(R.id.infoarrow);
        mydb = new DBHelper(this);
        myFAB = (FloatingActionButton) findViewById(R.id.fabAddWorkout);
        currentWorkout = (TextView) findViewById(R.id.CurrentWorkoutMain);
        showAll = (Button) findViewById(R.id.ButtonShowAll);

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH) +1;
        int year = c.get(Calendar.YEAR);
        date_db = "d"+Integer.toString(day) +"_" + Integer.toString(month) +"_"+Integer.toString(year);

        //WORKS
        //ArrayList<String> lastsets = mydb.getLastEx("Ü3",3);

        myFAB.setBackgroundTintList(getResources().getColorStateList(R.color.colorRed));


        if(mydb.doesTableExist(mydb.getdb(), date_db)) {
            myFAB.setBackgroundTintList(getResources().getColorStateList(R.color.colorPurple));
            myFAB.setImageResource(R.drawable.addnewexisting);
            infotext.setText("Edit today's Workout");
            arrow.setImageResource(R.drawable.arrow_edit);

            String[] parts = date_db.substring(1).split("_");
            String date = parts[0] + "." + parts[1] + "." + parts[2];

            currentWorkout.setText(mydb.getWoName(date_db) + "   -   " + date);

            currentWorkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent normalWO = new Intent(MainActivity.this, WorkoutMainActivity.class);

                    normalWO.putExtra("date", date_db);
                    normalWO.putExtra("workout_name", mydb.getWoName(date_db));

                    startActivity(normalWO);

                }
            });
        }

        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,AllWorkoutsActivity.class);
                startActivity(i);
            }
        });*/
    }

    public void onResume(){
        super.onResume();

    }



    /*public void makeNewWorkout(View view){

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


    }*/

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