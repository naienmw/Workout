package com.naien.workout;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.naien.workout.R;

import java.util.Calendar;
import java.util.Date;

public class Second_Activity extends Activity {

    EditText second_input;
    DBHelper mydb;
    String first_user_input;
    String second_user_input;
    TextView user_restored_text;
    EditText newinput;
    String date;
    String date_db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_content);

        Intent i = getIntent();
        first_user_input = i.getStringExtra("data");
        second_input = (EditText) findViewById(R.id.user_text_to_save);

        user_restored_text = (TextView) findViewById(R.id.restored);

        mydb = new DBHelper(this);

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        newinput = (EditText) findViewById(R.id.newdb);
        date = Integer.toString(day) + "." + Integer.toString(month) +"."+Integer.toString(year);
        newinput.setHint(date);

        date_db = Integer.toString(day) +"_" + Integer.toString(month) +"_"+Integer.toString(year);
    }

    /*public void save_to_database(View view){
        second_user_input = second_input.getText().toString();
        Boolean there = false;

        if (is_there()) {
            mydb.updateData(first_user_input, second_user_input);
            Toast.makeText(this, first_user_input + second_user_input, Toast.LENGTH_SHORT).show();
        } else {mydb.saveData(first_user_input, second_user_input);
            Toast.makeText(this, first_user_input + second_user_input, Toast.LENGTH_SHORT).show();}
    }

    public void get_data(View view) {
        Cursor cur = mydb.getData(1);
        cur.moveToFirst();
        String input1 = cur.getString(cur.getColumnIndex("name"));
        String input2 = cur.getString(cur.getColumnIndex("email"));

        Toast.makeText(this, input1 + input2, Toast.LENGTH_SHORT).show();

        user_restored_text.setText(input1 + " " + input2);
    }

    public boolean is_there(){
        Cursor cur = mydb.getData(1);
        Boolean there;
        if (cur.moveToFirst()){
            there = true;
        } else {there = false;}

        return there;
    }


    public void make_new_db(View view) {

        mydb.create_new_table(mydb.getdb(), date_db);
    }*/
}
