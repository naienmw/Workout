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

public class MainActivity extends AppCompatActivity {

    EditText input_to_second;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_to_second = (EditText) findViewById(R.id.user_input_to_second_activity);
    }

    public void switch_activity(View view){
        String to_second =  input_to_second.getText().toString();
        Intent the_second_screen = new Intent(this, Second_Activity.class);
        the_second_screen.putExtra("data",to_second);
        startActivity(the_second_screen);
    }
}