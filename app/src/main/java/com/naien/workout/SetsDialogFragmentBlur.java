package com.naien.workout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
public class SetsDialogFragmentBlur extends BlurDialogFragment {

    String exercise_name;
    String the_date;
    DBHelper mydb;
    ListAdapter theAdapter;
    TextView ex ;
    ListView sets ;
    Button exit ;
    Button addSet ;
    EditText user_input_reps;
    EditText user_input_weight;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setStuff(String Exname,String date){
        exercise_name = Exname;
        the_date = date;
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.exercise_main, null);

        builder.setView(view);


        ex = (TextView) view.findViewById(R.id.workout_name_in_ex);
        sets = (ListView) view.findViewById(R.id.listview_sets);
        exit = (Button) view.findViewById(R.id.sets_exit_button);
        addSet =(Button) view.findViewById(R.id.add_set_button);
        user_input_reps = (EditText) view.findViewById(R.id.user_reps_input);
        user_input_weight = (EditText) view.findViewById(R.id.user_weight_input);


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

        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });

        addSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer count_ex = mydb.getExIndex(the_date,exercise_name); //number of Exercise in Workout

                Integer count_sets = allSets.size();

                if (!user_input_reps.getText().toString().matches("")){
                    if (!user_input_weight.getText().toString().matches("")){

                        String newEx = user_input_reps.getText().toString() + "," + user_input_weight.getText().toString();
                        allSets.add(newEx);

                        ArrayList<String> theSets_fine = new ArrayList<String>();

                        for (String temp : allSets) {
                            String[] temp_div = temp.split(",");
                            theSets_fine.add(temp_div[0] + " x " + temp_div[1]);
                        }

                        theAdapter = new my_adapter_sets_arraylist(getActivity(), theSets_fine);

                        sets.setAdapter(theAdapter);

                        mydb.put_set(the_date,count_ex,count_sets+1,newEx);


                    }else{
                        Toast.makeText(getActivity(), "Give me some weight", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(),"Give me some Reps",Toast.LENGTH_SHORT).show();
                }
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
