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

public class Second_Activity extends Activity {

    EditText second_input;
    DBHelper mydb;
    String first_user_input;
    String second_user_input;
    TextView user_restored_text;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_content);

        Intent i = getIntent();
        first_user_input = i.getStringExtra("data");
        second_input = (EditText) findViewById(R.id.user_text_to_save);

        user_restored_text = (TextView) findViewById(R.id.restored);

        mydb = new DBHelper(this);
    }

    public void save_to_database(View view){

        second_user_input = second_input.getText().toString();
        mydb.saveData(first_user_input,second_user_input);

         Toast.makeText(this, first_user_input + second_user_input,Toast.LENGTH_SHORT).show();

    }

    public void get_data(View view){
        Cursor cur = mydb.getData(1);
        cur.moveToFirst();
        String input1 = cur.getString(cur.getColumnIndex("name"));
        String input2 = cur.getString(cur.getColumnIndex("email"));

        Toast.makeText(this, input1+input2,Toast.LENGTH_SHORT).show();

        //user_restored_text.setText(input1 + " " + input2);

    }



}
