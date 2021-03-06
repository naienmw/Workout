package com.naien.workout;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.DialogFragment;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fr.tvbarthel.lib.blurdialogfragment.BlurDialogFragment;


public class SetsDialogFragmentBlur extends DialogFragment {

    String exercise_name;
    String exercise_head;

    String the_date;
    Integer ExNum;
    DBHelper mydb;
    DBHelper_Ex mydbex;

    ArrayAdapter theAdapter;
    ArrayAdapter theAdapterold_1;
    ArrayAdapter theAdapterold_2;

    TextView ex ;
    ListView sets ;
    FloatingActionButton exit ;
    Button addSet ;
    EditText user_input_reps;
    EditText user_input_weight;
    ListView sets_old_1;
    ListView sets_old_2;

    ImageView eximage;
    Dialog dialog;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
//        exercise_head = getArguments().getString("Exhead");
//        exercise_name = getArguments().getString("Exname");
//        the_date = getArguments().getString("date");
//        ExNum = getArguments().getInt("num");
    }

    public void setStuff(String Exhead,String Exname,String date,Integer num){
        exercise_head = Exhead;
        exercise_name = Exname;
        the_date = date;
        ExNum = num;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exercise_main, container, false);
        ex = (TextView) view.findViewById(R.id.workout_name_in_ex);
        sets = (ListView) view.findViewById(R.id.listview_sets);
        sets_old_1 = (ListView) view.findViewById(R.id.listview_sets_old_1);
        sets_old_2 = (ListView) view.findViewById(R.id.listview_sets_old_2);
        eximage = (ImageView) view.findViewById(R.id.eximage);

        exit = (FloatingActionButton) view.findViewById(R.id.sets_exit_button);

        addSet =(Button) view.findViewById(R.id.add_set_button);
        user_input_reps = (EditText) view.findViewById(R.id.user_reps_input);
        user_input_weight = (EditText) view.findViewById(R.id.user_weight_input);
        return view;
    }


   /* public Dialog onCreateDialog(Bundle savedInstanceState) {
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //View view = getActivity().getLayoutInflater().inflate(R.layout.exercise_main, null);

        //final Drawable d = new ColorDrawable(Color.BLACK);
        //d.setAlpha(0);


        ex = (TextView) view.findViewById(R.id.workout_name_in_ex);
        sets = (ListView) view.findViewById(R.id.listview_sets);
        sets_old_1 = (ListView) view.findViewById(R.id.listview_sets_old_1);
        sets_old_2 = (ListView) view.findViewById(R.id.listview_sets_old_2);
        eximage = (ImageView) view.findViewById(R.id.eximage);

        exit = (FloatingActionButton) view.findViewById(R.id.sets_exit_button);

        addSet =(Button) view.findViewById(R.id.add_set_button);
        user_input_reps = (EditText) view.findViewById(R.id.user_reps_input);
        user_input_weight = (EditText) view.findViewById(R.id.user_weight_input);

        builder.setView(view);

        // creating the fullscreen dialog


        dialog = builder.create();


        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //dialog.getWindow().setGravity(Gravity.CENTER);

        //setStyle(DialogFragment.STYLE_NORMAL, R.style.TransparentDialog);



        //dialog.getWindow().setBackgroundDrawable(d);


        return dialog;
    }*/

    public void onResume(){

        super.onResume();


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
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);


        ex.setText(exercise_name);

        final ArrayList<String> allSets;

        mydb = new DBHelper(getActivity());
        mydbex = new DBHelper_Ex(getActivity());

        //Bitmap bmp = mydbex.getExerciseImage(exercise_head,exercise_name);
        //eximage.setImageBitmap(bmp);


        //eximage.setClipToOutline(true);

        allSets = mydb.getAllSets_index(the_date,ExNum);

        ArrayList<String> theSets_fine = new ArrayList<>();

        for (String temp : allSets) {
            String[] temp_div = temp.split(",");
            theSets_fine.add(temp_div[0] + " x " + temp_div[1]);
        }

        theAdapter = new my_adapter_sets_arraylist(getActivity(),theSets_fine);
        sets.setAdapter(theAdapter);

        datatrunk oldsets1 = mydb.getLastEx(exercise_name,1);

        ArrayList<String> list1 = oldsets1.getList();
        Integer Offset1 = oldsets1.getOffset();

        datatrunk oldsets2 = mydb.getLastEx(exercise_name, Offset1+1);

        ArrayList<String> list2 = oldsets2.getList();


        theAdapterold_1 = new my_adapter_sets_arraylist_old(getActivity(),list1);
        theAdapterold_2 = new my_adapter_sets_arraylist_old(getActivity(),list2);

        sets_old_1.setAdapter(theAdapterold_1);
        sets_old_2.setAdapter(theAdapterold_2);

        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });
        sets.setLongClickable(true);

        //getDialog().getWindow().setLayout(1080, 1920);

        sets.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mydb.deleteSetinEx_index(the_date, ExNum, position + 1);

                allSets.remove(position);

                String delete = theAdapter.getItem(position).toString();

                theAdapter.remove(delete);
                theAdapter.setNotifyOnChange(true);

                return false;
            }
        });


        addSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer count_ex;
                count_ex = ExNum;

                Integer count_sets = allSets.size();

                if (!user_input_reps.getText().toString().matches("")) {
                    if (!user_input_weight.getText().toString().matches("")) {

                        String newEx = user_input_reps.getText().toString() + "," + user_input_weight.getText().toString();
                        allSets.add(newEx);

                        String newEx_fine = user_input_reps.getText().toString() + " x " + user_input_weight.getText().toString();

                        ArrayList<String> theSets_fine = new ArrayList<>();

                        for (String temp : allSets) {
                            String[] temp_div = temp.split(",");
                            theSets_fine.add(temp_div[0] + " x " + temp_div[1]);
                        }


                        theAdapter.add(newEx_fine);
                        theAdapter.setNotifyOnChange(true);

                        mydb.put_set(the_date, count_ex, count_sets + 1, newEx);


                        user_input_weight.setText("");
                        user_input_reps.setText("");

                        Toast.makeText(getActivity(), "SET CREATED", Toast.LENGTH_SHORT).show();

                        user_input_reps.requestFocus();

                    } else {
                        Toast.makeText(getActivity(), "Give me some weight", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Give me some Reps", Toast.LENGTH_SHORT).show();
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

    //@Override
    protected boolean isDimmingEnable() {
        // Enable or disable the dimming effect.
        // Disabled by default.
        return true;
    }

    //@Override
    protected boolean isRenderScriptEnable() {
        // Enable or disable the use of RenderScript for blurring effect
        // Disabled by default.
        return false;
    }

    //@Override
    protected boolean isDebugEnable() {
        // Enable or disable debug mode.
        // False by default.
        return false;
    }



}
