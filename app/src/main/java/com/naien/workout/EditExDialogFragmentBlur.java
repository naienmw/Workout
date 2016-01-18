package com.naien.workout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fr.tvbarthel.lib.blurdialogfragment.BlurDialogFragment;


public class EditExDialogFragmentBlur extends BlurDialogFragment {

    String exercise_name;
    String exercise_head;

    DBHelper_Ex mydbex;


    TextView ex ;

    FloatingActionButton exit ;
    Button applyEx ;
    EditText user_newexname;
    EditText user_newexhead;
    ExpListAdapter adapter;


    ImageView eximage;
    Dialog dialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }

    public void setStuff(String Exhead, String Exname){
        exercise_name = Exname;
        exercise_head = Exhead;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exercise_edit, container, false);

        ex = (TextView) view.findViewById(R.id.edit_name_in_ex);

        eximage = (ImageView) view.findViewById(R.id.eximage_edit);

        user_newexname = (EditText) view.findViewById(R.id.user_new_ex_name);
        user_newexhead = (EditText) view.findViewById(R.id.user_new_ex_head);

        exit = (FloatingActionButton)view.findViewById(R.id.edit_ex_exit_button);

        applyEx = (Button)view.findViewById(R.id.apply_ex_button);

        return view;
    }

   /* public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.exercise_edit, null);

        final Drawable d = new ColorDrawable(Color.BLACK);
        d.setAlpha(0);

        ex = (TextView) view.findViewById(R.id.edit_name_in_ex);

        eximage = (ImageView) view.findViewById(R.id.eximage_edit);

        user_newexname = (EditText) view.findViewById(R.id.user_new_ex_name);
        user_newexhead = (EditText) view.findViewById(R.id.user_new_ex_head);

        exit = (FloatingActionButton)view.findViewById(R.id.edit_ex_exit_button);

        applyEx = (Button)view.findViewById(R.id.apply_ex_button);

        builder.setView(view);

        dialog = builder.create();

        dialog.getWindow().setBackgroundDrawable(d);

        return dialog;
    }*/
    public void onResume(){
        super.onResume();

        //dialog.getWindow().setGravity(Gravity.CENTER);
    }
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        ex.setText(exercise_name);
        user_newexname.setText(exercise_name);
        user_newexhead.setText(exercise_head);
        user_newexhead.setKeyListener(null);
        user_newexhead.setEnabled(false);


        mydbex = new DBHelper_Ex(getActivity());

        //Bitmap bmp = mydbex.getExerciseImage(exercise_head, exercise_name);
        //eximage.setImageBitmap(bmp);

        //eximage.setClipToOutline(true);

        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });

        applyEx.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String newex=user_newexname.getText().toString();
                if(!newex.equals("")){
                    if(!newex.equals(exercise_name)) {
                        switch (exercise_head) {
                            case "Brust":
                                mydbex.updateExerciseName("Brust", newex, exercise_name);
                                Intent intent = new Intent(getActivity(), WorkoutMainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                dismiss();

                                break;
                            case "Beine":
                                mydbex.updateExerciseName("Beine", newex, exercise_name);

                                intent = new Intent(getActivity(), WorkoutMainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                dismiss();
                                break;
                            case "Rücken":
                                mydbex.updateExerciseName("Rücken", newex, exercise_name);

                                intent = new Intent(getActivity(), WorkoutMainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                dismiss();
                                break;
                            case "Arme":
                                mydbex.updateExerciseName("Arme", newex, exercise_name);

                                intent = new Intent(getActivity(), WorkoutMainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                dismiss();
                                break;
                            case "Schultern":
                                mydbex.updateExerciseName("Schultern", newex, exercise_name);

                                intent = new Intent(getActivity(), WorkoutMainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                dismiss();
                                break;
                            default:
                                Toast.makeText(getActivity(), "Wrong Exercise Type", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }else{
                        Toast.makeText(getActivity(), "Nothing Changed", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
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
