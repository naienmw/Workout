package com.naien.workout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fr.tvbarthel.lib.blurdialogfragment.BlurDialogFragment;

/**
 * Created by naien on 23.12.2015.
 */
public class SetsDialogFragmentBlur_static extends BlurDialogFragment {

    String exercise_name;
    String the_date;
    DBHelper mydb;
    ListAdapter theAdapter;
    TextView ex ;
    ListView sets ;
    FloatingActionButton exit ;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setStuff(String Exname,String date){
        exercise_name = Exname;
        the_date = date;
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.exercise_main_static, null);

        builder.setView(view);


        ex = (TextView) view.findViewById(R.id.workout_name_in_ex_static);
        sets = (ListView) view.findViewById(R.id.listview_sets_static);
        exit = (FloatingActionButton) view.findViewById(R.id.sets_exit_button_static);

        return builder.create();
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        ex.setText(exercise_name);

        final ArrayList<String> allSets;

        mydb = new DBHelper(getActivity());
        allSets = mydb.getAllSets(the_date,exercise_name);

        ArrayList<String> theSets_fine = new ArrayList<String>();

        for (String temp : allSets) {
            String[] temp_div = temp.split(",");
            theSets_fine.add(temp_div[0] + " x " + temp_div[1]);
        }

        theAdapter = new my_adapter_sets_arraylist(getActivity(),theSets_fine);
        sets.setAdapter(theAdapter);
        exit.setBackgroundTintList(getResources().getColorStateList(R.color.colorGreen));

        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });

    }


    protected int getBlurRadius() {
        // Allow to customize the blur radius factor.
        return 8;
    }

    protected boolean isActionBarBlurred() {
        // Enable or disable the blur effect on the action bar.
        // Disabled by default.
        return true;
    }

    @Override
    protected boolean isDimmingEnable() {
        // Enable or disable the dimming effect.
        // Disabled by default.
        return true;
    }

    @Override
    protected boolean isRenderScriptEnable() {
        // Enable or disable the use of RenderScript for blurring effect
        // Disabled by default.
        return false;
    }

    @Override
    protected boolean isDebugEnable() {
        // Enable or disable debug mode.
        // False by default.
        return false;
    }

}
