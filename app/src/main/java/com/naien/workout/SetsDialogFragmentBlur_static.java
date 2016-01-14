package com.naien.workout;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fr.tvbarthel.lib.blurdialogfragment.BlurDialogFragment;


public class SetsDialogFragmentBlur_static extends BlurDialogFragment {

    String exercise_name;
    String the_date;
    DBHelper mydb;
    ListAdapter theAdapter;
    TextView ex ;
    ListView sets ;
    FloatingActionButton exit ;
    Integer ExNum;

    Dialog dialog;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }



    public void setStuff(String Exname,String date,Integer index){
        exercise_name = Exname;
        the_date = date;
        ExNum = index;

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exercise_main_static, container, false);
        ex = (TextView) view.findViewById(R.id.workout_name_in_ex_static);
        sets = (ListView) view.findViewById(R.id.listview_sets_static);
        exit = (FloatingActionButton) view.findViewById(R.id.sets_exit_button_static);
        return view;
    }


    /*public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.exercise_main_static, null);

        builder.setView(view);



        final Drawable d = new ColorDrawable(Color.BLACK);
        d.setAlpha(0);


        ex = (TextView) view.findViewById(R.id.workout_name_in_ex_static);
        sets = (ListView) view.findViewById(R.id.listview_sets_static);
        exit = (FloatingActionButton) view.findViewById(R.id.sets_exit_button_static);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(d);

        return dialog;
    }*/

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        ex.setText(exercise_name);

        final ArrayList<String> allSets;

        mydb = new DBHelper(getActivity());
        allSets = mydb.getAllSets_index(the_date,ExNum);

        ArrayList<String> theSets_fine = new ArrayList<>();

        for (String temp : allSets) {
            String[] temp_div = temp.split(",");
            theSets_fine.add(temp_div[0] + " x " + temp_div[1]);
        }

        theAdapter = new my_adapter_sets_arraylist_static(getActivity(),theSets_fine);
        sets.setAdapter(theAdapter);
        //exit.setBackgroundTintList(getResources().getColorStateList(R.color.colorGreen));

        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public void onStart(){
        super.onStart();
        Dialog d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    public void onResume(){
        super.onResume();
        //Window window = getDialog().getWindow();
        //window.setLayout(650, AbsListView.LayoutParams.WRAP_CONTENT);
        //dialog.getWindow().setGravity(Gravity.CENTER);
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
