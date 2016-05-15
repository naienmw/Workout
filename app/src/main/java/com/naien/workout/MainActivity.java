package com.naien.workout;

        import android.animation.Animator;
        import android.animation.AnimatorListenerAdapter;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.res.ColorStateList;
        import android.database.sqlite.SQLiteDatabase;
        import android.graphics.Color;
        import android.graphics.PorterDuff;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.Handler;
        import android.support.annotation.DrawableRes;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.ViewAnimationUtils;
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
        import android.widget.PopupWindow;
        import android.widget.RelativeLayout;
        import android.widget.RelativeLayout.LayoutParams;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.w3c.dom.Text;

        import java.util.ArrayList;
        import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Animation fab_anim;
    Animation_Backup fab_anim_bu;
    Animation_Restore fab_anim_re;

    private ImageView image;
    private PopupWindow pw;
    public FastBlur blur = new FastBlur();

    copydbhelper createdbex;

    DBHelper mydb;

    boolean really_restore;

    DBHelper_Ex dbex;
    Boolean openfabs = true;
    String date_db;

    FloatingActionButton myFAB;
    FloatingActionButton FABRestore;
    FloatingActionButton FABBackup;

    TextView currentWorkout;
    TextView showAll;
    TextView newWo;
    RelativeLayout rellayout;

    Boolean toolbarisshown = false;
    ListView setsinmain;

    Boolean pretty_Animation;

    XML_Saver_Class save_db_user;
    XML_Saver_Class_Exercises save_db_ex;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        pretty_Animation = Build.VERSION.SDK_INT >= 21;

        image = (ImageView) findViewById(R.id.picture);

        setsinmain = (ListView) findViewById(R.id.ListviewcurrentSetsinMain);

        mydb = new DBHelper(this);

        myFAB = (FloatingActionButton) findViewById(R.id.fabAddWorkout);

        FABRestore = (FloatingActionButton) findViewById(R.id.FABRestore);
        FABRestore.setBackgroundTintList(getResources().getColorStateList(R.color.colorWhite));

        FABBackup = (FloatingActionButton) findViewById(R.id.FABBackup);
        FABBackup.setBackgroundTintList(getResources().getColorStateList(R.color.colorWhite));

        currentWorkout = (TextView) findViewById(R.id.CurrentWorkoutMain);
        showAll = (TextView) findViewById(R.id.ButtonShowAll);
        newWo = (TextView) findViewById(R.id.ButtonNewWo);

        fab_anim = new Animation(this, myFAB);
        fab_anim_re = new Animation_Restore(this, FABRestore);
        fab_anim_bu = new Animation_Backup(this, FABBackup);

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);
        date_db = "d" + Integer.toString(day) + "_" + Integer.toString(month) + "_" + Integer.toString(year);

        FABRestore.setOnClickListener(new View.OnClickListener() {

            AlertDialog restore = new AlertDialog.Builder(MainActivity.this)
                    .setMessage(R.string.REALLYsuretorestore)
                    .setCancelable(false)
                    .setPositiveButton("JA", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        save_db_user.restore();
                                        Toast.makeText(getApplicationContext(), R.string.re_set_suc, Toast.LENGTH_SHORT).show();
                                    } catch (IllegalAccessException a) {
                                        Toast.makeText(getApplicationContext(), R.string.file_not_found_sets, Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), R.string.re_sets_fail, Toast.LENGTH_SHORT).show();
                                    }

                                    try {
                                        save_db_ex.restore();
                                        Toast.makeText(getApplicationContext(), R.string.re_ex_suc, Toast.LENGTH_SHORT).show();
                                    } catch (IllegalAccessException a) {
                                        Toast.makeText(getApplicationContext(), R.string.file_not_found_ex, Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), R.string.re_ex_fail, Toast.LENGTH_SHORT).show();
                                    }

                                    finish();
                                }
                            }, 500);

                            Toast.makeText(getApplicationContext(), R.string.toast_restoring, Toast.LENGTH_SHORT).show();
                            restore.cancel();
                        }
                    })
                    .setNegativeButton("Lieber nicht", null)
                    .create();

            @Override
            public void onClick(View v) {
                really_restore = false;
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage(R.string.suretorestore)
                        .setCancelable(false)
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                restore.show();
                            }
                        })
                        .setNegativeButton("Nein", null)
                        .show();
            }
        });

        FABBackup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    save_db_user.backup();
                    Toast.makeText(getApplicationContext(), R.string.sets_bu_suc, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.sets_bu_wrong, Toast.LENGTH_SHORT).show();
                }
                try {
                    save_db_ex.backup();
                    Toast.makeText(getApplicationContext(), R.string.ex_bu_suc, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.ex_bu_wrong, Toast.LENGTH_SHORT).show();
                }
            }
        });

        myFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!toolbarisshown) {
                    if (openfabs) {

                        FABRestore.setVisibility(View.VISIBLE);
                        FABBackup.setVisibility(View.VISIBLE);
                        fab_anim.startAnimationopen();
                        fab_anim_bu.startAnimationopen();
                        fab_anim_re.startAnimationopen();
                        if (pretty_Animation) {
                            enterReveal(R.id.myToolbar);
                        } else {
                            findViewById(R.id.myToolbar).setVisibility(View.VISIBLE);
                        }
                        toolbarisshown = true;
                        myFAB.setBackgroundTintList(getResources().getColorStateList(R.color.colorWhite));
                        myFAB.setImageResource(R.drawable.barbell_accent);
                    }


                } else {
                    if (pretty_Animation) {
                        exitReveal(R.id.myToolbar);
                    } else {
                        findViewById(R.id.myToolbar).setVisibility(View.INVISIBLE);
                    }
                    toolbarisshown = false;
                    myFAB.setBackgroundTintList(getResources().getColorStateList(R.color.accent));
                    fab_anim.startAnimationclose();
                    myFAB.setImageResource(R.drawable.barbell);
                    fab_anim_bu.startAnimationclose();
                    fab_anim_re.startAnimationclose();
                }

            }
        });


        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AllWorkoutsActivity.class);
                startActivity(i);
            }
        });

        rellayout = (RelativeLayout) findViewById(R.id.framemain);

        rellayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBlurFragment fragment = (MyBlurFragment) getFragmentManager().findFragmentById(R.id.fragmentid);
                if (fragment != null && toolbarisshown) {
                    if (pretty_Animation) {
                        fragment.exitReveal(R.id.myToolbar);
                    } else {
                        findViewById(R.id.myToolbar).setVisibility(View.INVISIBLE);
                    }
                    toolbarisshown = false;
                    //fragment.enterReveal(R.id.fabAddWorkout);
                    myFAB.setBackgroundTintList(getResources().getColorStateList(R.color.accent));
                    fab_anim.startAnimationclose();
                    myFAB.setImageResource(R.drawable.barbell);
                    fab_anim_bu.startAnimationclose();
                    fab_anim_re.startAnimationclose();
                    FABRestore.setVisibility(View.INVISIBLE);
                    FABBackup.setVisibility(View.INVISIBLE);

                }
            }
        });

    }


    public View getFAB() {
        return myFAB;
    }

    public FloatingActionButton getFAB_bu() {
        return FABBackup;
    }

    public FloatingActionButton getFAB_re() {
        return FABRestore;
    }

    public Boolean getisToolbarShown() {
        return toolbarisshown;
    }

    public void setisToolbarShown(Boolean shown) {

        toolbarisshown = shown;
    }

    public void onResume() {
        super.onResume();

        createdbex = new copydbhelper(this);
        createdbex.createDatabase();

        dbex = new DBHelper_Ex(this);

        save_db_user = new XML_Saver_Class(mydb.getdb(), this);
        save_db_ex = new XML_Saver_Class_Exercises(dbex.getdb(), this);

        if (mydb.doesTableExist(mydb.getdb(), date_db)) {
            mydb.getdb().close();
            newWo.setText("Edit Current");
            newWo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent workout_main = new Intent(getApplicationContext(), WorkoutMainActivity.class);
                    workout_main.putExtra("workout_name", mydb.getWoName(date_db));
                    workout_main.putExtra("date", date_db);
                    startActivity(workout_main);

                }
            });

            ArrayList<String> theExs;

            theExs = mydb.getAllExercises_Arraylist(date_db);

            my_adapter_sets_arraylist theAdapter = new my_adapter_sets_arraylist(this, theExs);

            setsinmain.setAdapter(theAdapter);


            String[] parts = date_db.substring(1).split("_");
            String date = parts[0] + "." + parts[1] + "." + parts[2];

            currentWorkout.setText(mydb.getWoName(date_db) + "   -   " + date);

            currentWorkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent normalWO = new Intent(getApplicationContext(), WorkoutMainActivity.class);

                    normalWO.putExtra("date", date_db);
                    normalWO.putExtra("workout_name", mydb.getWoName(date_db));


                    startActivity(normalWO);

                }
            });
        } else {
            newWo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent test = new Intent(getApplicationContext(), NewWorkoutActivity.class);
                    test.putExtra("date", date_db);
                    startActivity(test);

                }
            });
        }
    }

    void enterReveal(int id) {
        // previously invisible view
        final View myView = findViewById(id);

        // get the center for the clipping circle
        int cx;
        int cy;

        cx = Math.round(myFAB.getX());
        cy = myView.getHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight()) / 2;
        finalRadius = (int) Math.hypot(myView.getWidth(), myView.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        myView.setVisibility(View.VISIBLE);

        anim.start();
        openfabs = false;
    }


    void exitReveal(int id) {
        // previously visible view
        final View myView = findViewById(id);

        // get the center for the clipping circle
        int cx;
        int cy;

        cx = Math.round(myFAB.getX());
        cy = myView.getHeight() / 2;

        // get the initial radius for the clipping circle
        int initialRadius;
        initialRadius = (int) Math.hypot(myView.getWidth(), myView.getHeight());

        // create the animation (the final radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                myView.setVisibility(View.INVISIBLE);
                openfabs = true;
            }
        });
        // start the animation
        anim.start();
    }


    public void onBackPressed() {
        //MyBlurFragment fragment = (MyBlurFragment)getFragmentManager().findFragmentById(R.id.fragmentid);
        //myFAB = (FloatingActionButton)fragment.getFAB();
        //FABBackup = fragment.getFAB_bu();
        //FABRestore = fragment.getFAB_re();
        //fab_anim = new Animation(this,myFAB);
        //fab_anim_bu = new Animation_Backup(this,FABBackup);
        //fab_anim_re = new Animation_Restore(this,FABRestore);
        if (toolbarisshown) {
            exitReveal(R.id.myToolbar);
            toolbarisshown = false;
            myFAB.setBackgroundTintList(getResources().getColorStateList(R.color.accent));
            myFAB.setImageResource(R.drawable.barbell);
            fab_anim.startAnimationclose();
            fab_anim_bu.startAnimationclose();
            fab_anim_re.startAnimationclose();

        }else {
            super.onBackPressed();
        }
    }

}