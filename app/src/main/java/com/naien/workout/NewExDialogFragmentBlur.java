package com.naien.workout;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fr.tvbarthel.lib.blurdialogfragment.BlurDialogFragment;


public class NewExDialogFragmentBlur extends BlurDialogFragment {

    String exercise_name;
    String exercise_head;

    DBHelper_Ex mydbex;


    TextView ex ;

    FloatingActionButton exit ;
    Button applyEx ;
    EditText user_newexname;
    TextView user_newexhead;
    ExpListAdapter adapter;

    ListView listview_head;
    ArrayList<String> heads;
    Boolean visible;


    ImageView eximage;
    Dialog dialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exercise_new, container, false);

        ex = (TextView) view.findViewById(R.id.edit_name_in_ex);

        eximage = (ImageView) view.findViewById(R.id.eximage_new);

        user_newexname = (EditText) view.findViewById(R.id.edit_new_ex_name);
        user_newexhead = (TextView) view.findViewById(R.id.edit_new_ex_head);

        listview_head = (ListView) view.findViewById(R.id.listview_head_exercises);
        heads = new ArrayList<>();
        heads.add("Brust");
        heads.add("Schultern");
        heads.add("Rücken");
        heads.add("Arme");
        heads.add("Beine");

        listview_head.setVisibility(View.GONE);
        visible = false;

        my_adapter_sets_arraylist the_Adapter = new my_adapter_sets_arraylist(getActivity(),heads);
        listview_head.setAdapter(the_Adapter);

        exit = (FloatingActionButton)view.findViewById(R.id.edit_ex_exit_button);

        applyEx = (Button)view.findViewById(R.id.apply_new_ex_button);

        return view;
    }


   /* public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.exercise_new, null);


        //final Drawable d = new ColorDrawable(Color.BLACK);
        //d.setAlpha(0);

        ex = (TextView) view.findViewById(R.id.edit_name_in_ex);

        eximage = (ImageView) view.findViewById(R.id.eximage_new);

        user_newexname = (EditText) view.findViewById(R.id.edit_new_ex_name);
        user_newexhead = (TextView) view.findViewById(R.id.edit_new_ex_head);

        listview_head = (ListView) view.findViewById(R.id.listview_head_exercises);
        heads = new ArrayList<>();
        heads.add("Brust");
        heads.add("Schultern");
        heads.add("Rücken");
        heads.add("Arme");
        heads.add("Beine");

        listview_head.setVisibility(View.GONE);
        visible = false;

        my_adapter_sets_arraylist the_Adapter = new my_adapter_sets_arraylist(getActivity(),heads);
        listview_head.setAdapter(the_Adapter);

        exit = (FloatingActionButton)view.findViewById(R.id.edit_ex_exit_button);

        applyEx = (Button)view.findViewById(R.id.apply_new_ex_button);

        builder.setView(view);

        dialog = builder.create();

        //dialog.getWindow().setBackgroundDrawable(d);

        return dialog;
    }*/
    public void onResume(){
        super.onResume();

        //dialog.getWindow().setGravity(Gravity.CENTER);
    }



    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);


        user_newexhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!visible) {
                    listview_head.setVisibility(View.VISIBLE);
                    visible = true;
                }else{
                    listview_head.setVisibility(View.GONE);
                    visible = false;
                }
            }
        });

        listview_head.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                user_newexhead.setText(heads.get(position));

                listview_head.setVisibility(View.GONE);
                visible = false;
            }
        });


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
                    exercise_head = user_newexhead.getText().toString();
                    switch (exercise_head) {
                        case "Brust":
                            mydbex.saveExerciseName("Brust", newex);
                            Intent intent = new Intent(getActivity(), WorkoutMainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(intent);
                            dismiss();

                            break;
                        case "Beine":
                            mydbex.saveExerciseName("Beine", newex);

                            intent = new Intent(getActivity(), WorkoutMainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(intent);
                            dismiss();
                            break;
                        case "Rücken":
                            mydbex.saveExerciseName("Rücken", newex);

                            intent = new Intent(getActivity(), WorkoutMainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(intent);
                            dismiss();
                            break;
                        case "Arme":
                            mydbex.saveExerciseName("Arme", newex);

                            intent = new Intent(getActivity(), WorkoutMainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(intent);
                            dismiss();
                            break;
                        case "Schultern":
                            mydbex.saveExerciseName("Schultern", newex);

                            intent = new Intent(getActivity(), WorkoutMainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(intent);
                            dismiss();
                            break;
                        default:
                            Toast.makeText(getActivity(), "Wrong Exercise Type", Toast.LENGTH_SHORT).show();
                            break;
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
