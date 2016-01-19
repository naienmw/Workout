package com.naien.workout;

        import android.content.Intent;
        import android.content.res.ColorStateList;
        import android.database.sqlite.SQLiteDatabase;
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

    Animation fabclose;
    Animation_Backup fabclose_bu;
    FloatingActionButton myFAB;

    FloatingActionButton myFAB_bu;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onBackPressed() {
        MyBlurFragment fragment = (MyBlurFragment)getFragmentManager().findFragmentById(R.id.fragmentid);
        myFAB = (FloatingActionButton)fragment.getFAB();
        myFAB_bu = fragment.getFAB_bu();
        fabclose = new Animation(this,myFAB);
        fabclose_bu = new Animation_Backup(this,myFAB_bu);
        if (fragment != null && fragment.getisToolbarShown()) {
            fragment.exitReveal(R.id.myToolbar);
            fragment.setisToolbarShown(false);
            fragment.getFAB().setBackgroundTintList(getResources().getColorStateList(R.color.accent));
            myFAB.setImageResource(R.drawable.barbell);
            fabclose.startAnimationclose();
            fabclose_bu.startAnimationclose();

        }else {
            super.onBackPressed();
        }
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