package com.naien.workout;

        import android.animation.Animator;
        import android.animation.AnimatorListenerAdapter;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.res.ColorStateList;
        import android.database.sqlite.SQLiteDatabase;
        import android.graphics.Color;
        import android.graphics.PorterDuff;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.DropBoxManager;
        import android.os.Environment;
        import android.os.Handler;
        import android.support.annotation.DrawableRes;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.util.AsyncListUtil;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.ViewAnimationUtils;
        import android.view.ViewGroup;
        import android.view.animation.AccelerateInterpolator;
        import android.view.animation.AnticipateOvershootInterpolator;
        import android.view.animation.LinearInterpolator;
        import android.view.animation.OvershootInterpolator;
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

        import com.dropbox.client2.DropboxAPI;
        import com.dropbox.client2.android.AndroidAuthSession;
        import com.dropbox.client2.exception.DropboxException;
        import com.dropbox.client2.exception.DropboxServerException;
        import com.dropbox.client2.session.AppKeyPair;

        import org.w3c.dom.Text;
        import org.xml.sax.XMLReader;

        import java.io.File;
        import java.io.FileInputStream;
        import java.util.ArrayList;
        import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    Animation fab_anim;
    Animation_Backup fab_anim_bu;
    Animation_Restore fab_anim_re;

    String WoName;

    private ImageView image;
    private PopupWindow pw;
    public FastBlur blur = new FastBlur();

    private DropboxAPI<AndroidAuthSession> mDBApi;
    final static private String APP_KEY = "0rhy6rcvdog72sn";
    final static private String APP_SECRET = "asmu3dnc8qlg9m8";

    SharedPreferences sharedpreferences_token;
    SharedPreferences workout_sp;

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

    Boolean today_ex_sp;
    Boolean today_ex;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);

        sharedpreferences_token = getSharedPreferences("MYPREFS", Context.MODE_PRIVATE);
        workout_sp = getSharedPreferences("MYWORKOUT", Context.MODE_PRIVATE);
        today_ex_sp = false;
        if (sharedpreferences_token.getBoolean("auth",false)){
            mDBApi.getSession().setOAuth2AccessToken(sharedpreferences_token.getString("token",""));
        }


        pretty_Animation = Build.VERSION.SDK_INT >= 21;

        image = (ImageView) findViewById(R.id.picture);

        setsinmain = (ListView) findViewById(R.id.ListviewcurrentSetsinMain);

        mydb = new DBHelper(this);

        myFAB = (FloatingActionButton) findViewById(R.id.fabAddWorkout);

        FABRestore = (FloatingActionButton) findViewById(R.id.FABRestore);
        FABRestore.setBackgroundTintList(getResources().getColorStateList(R.color.colorWhite));
        FABRestore.setClickable(false);
        FABBackup = (FloatingActionButton) findViewById(R.id.FABBackup);
        FABBackup.setBackgroundTintList(getResources().getColorStateList(R.color.colorWhite));
        FABBackup.setClickable(false);
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

        String blub;
        blub = workout_sp.getString("today_date","01.01.2015") + workout_sp.getString("today_name","blub");

        if ( blub  .equals(date_db + workout_sp.getString("today_name","blahh"))){
            today_ex_sp = true;
        }

        today_ex = false;

        if(mydb.doesTableExist(mydb.getdb(), date_db)){
            today_ex = true;
        }

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
            boolean backup_suc = false;
            boolean backup_suc_1 =false;
            @Override
            public void onClick(View v) {
                try {
                    save_db_user.backup();
                    Toast.makeText(getApplicationContext(), R.string.sets_bu_suc, Toast.LENGTH_SHORT).show();
                    backup_suc_1 =true;
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.sets_bu_wrong, Toast.LENGTH_SHORT).show();
                }
                try {
                    save_db_ex.backup();
                    Toast.makeText(getApplicationContext(), R.string.ex_bu_suc, Toast.LENGTH_SHORT).show();
                    backup_suc = backup_suc_1;
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.ex_bu_wrong, Toast.LENGTH_SHORT).show();
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(backup_suc)
                        {
                            new UploadToDB().execute();
                            //upload.doInBackground();
                        }
                    }
                },1500);


            }
        });

        myFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!toolbarisshown) {
                    if (openfabs) {

                        //FABRestore.setVisibility(View.VISIBLE);
                        FABRestore.animate().y(290).scaleX(1).scaleY(1).setInterpolator(new OvershootInterpolator()).setDuration(500).start();
                        FABRestore.setClickable(true);

                        //FABBackup.setVisibility(View.VISIBLE);
                        FABBackup.animate().y(290).x(600).scaleX(1).scaleY(1).setInterpolator(new OvershootInterpolator()).setDuration(500).start();
                        FABBackup.setClickable(true);

                        fab_anim.startAnimationopen();

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

                    FABBackup.animate().translationYBy(230).translationXBy(230).scaleX(0).scaleY(0).setInterpolator(new LinearInterpolator()).setDuration(300).start();
                    FABRestore.animate().translationYBy(230).scaleX(0).scaleY(0).setInterpolator(new LinearInterpolator()).setDuration(300).start();

                    FABBackup.setClickable(false);
                    //FABBackup.setVisibility(View.INVISIBLE);
                    FABRestore.setClickable(false);
                    //FABRestore.setVisibility(View.INVISIBLE);
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

                if (toolbarisshown) {
                    if (pretty_Animation) {
                        exitReveal(R.id.myToolbar);
                    } else {
                        findViewById(R.id.myToolbar).setVisibility(View.INVISIBLE);
                    }
                    toolbarisshown = false;

                    FABBackup.animate().translationYBy(230).translationXBy(230).scaleX(0).scaleY(0).setInterpolator(new LinearInterpolator()).setDuration(300).start();
                    FABRestore.animate().translationYBy(230).scaleX(0).scaleY(0).setInterpolator(new LinearInterpolator()).setDuration(300).start();

                    myFAB.setBackgroundTintList(getResources().getColorStateList(R.color.accent));
                    fab_anim.startAnimationclose();
                    myFAB.setImageResource(R.drawable.barbell);

                    FABRestore.setClickable(false);
                    FABBackup.setClickable(false);

                }
            }
        });

    }

    public void onResume() {
        super.onResume();

        SharedPreferences.Editor workout_sp_editor = workout_sp.edit();

        createdbex = new copydbhelper(this);
        createdbex.createDatabase();

        dbex = new DBHelper_Ex(this);



        save_db_user = new XML_Saver_Class(mydb.getdb(), this);
        save_db_ex = new XML_Saver_Class_Exercises(dbex.getdb(), this);

        if (today_ex) {

            if (today_ex_sp) {
                WoName = workout_sp.getString("today_name","heute");
            }else {
                WoName = mydb.getWoName(date_db);
                workout_sp_editor.putString("today_date", date_db);
                workout_sp_editor.putString("today_name", WoName);
                workout_sp_editor.apply();
            }
            mydb.getdb().close();
            newWo.setText("Edit Current");
            newWo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent workout_main = new Intent(getApplicationContext(), WorkoutMainActivity.class);
                    workout_main.putExtra("workout_name", WoName);
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

            currentWorkout.setText(WoName + "   -   " + date);

            currentWorkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent normalWO = new Intent(getApplicationContext(), WorkoutMainActivity.class);

                    normalWO.putExtra("date", date_db);
                    normalWO.putExtra("workout_name", WoName);


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

        SharedPreferences.Editor editor = sharedpreferences_token.edit();

        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                mDBApi.getSession().finishAuthentication();

                String accessToken = mDBApi.getSession().getOAuth2AccessToken();
                editor.putBoolean("auth",true);
                editor.putString("token",accessToken);
                editor.apply();

            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
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

        if (toolbarisshown) {
            exitReveal(R.id.myToolbar);
            toolbarisshown = false;
            myFAB.setBackgroundTintList(getResources().getColorStateList(R.color.accent));
            myFAB.setImageResource(R.drawable.barbell);
            FABBackup.animate().translationYBy(230).translationXBy(230).scaleX(0).scaleY(0).setInterpolator(new LinearInterpolator()).setDuration(300).start();
            FABRestore.animate().translationYBy(230).scaleX(0).scaleY(0).setInterpolator(new LinearInterpolator()).setDuration(300).start();
            fab_anim.startAnimationclose();


        }else {
            super.onBackPressed();
        }
    }

    class UploadToDB extends AsyncTask<String, Void , String> {
        Boolean ex;
        protected String doInBackground(String... args) {

            FileInputStream inputStream1;
            FileInputStream inputStream2;
            File file1;
            File file2;


            if(!sharedpreferences_token.getBoolean("auth",false)) {
                mDBApi.getSession().startOAuth2Authentication(MainActivity.this);
            }else{
                try {
                    file1 = new File(Environment.getExternalStorageDirectory() + "/Download/your_Sets.xml");
                    file2 = new File(Environment.getExternalStorageDirectory() + "/Download/your_Exercises.xml");
                    inputStream1 = new FileInputStream(file1);
                    inputStream2 = new FileInputStream(file2);


                        DropboxAPI.Entry response1 = mDBApi.putFileOverwrite("/your_Sets.xml", inputStream1,
                                file1.length(), null);
                        Log.i("DbExampleLog", "The uploaded file's rev is: " + response1.rev);

                    DropboxAPI.Entry response2 = mDBApi.putFileOverwrite("/your_Exercises.xml", inputStream2,
                            file2.length(), null);
                    Log.i("DbExampleLog", "The uploaded file's rev is: " + response2.rev);


                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            return "jo";


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(),"Upload zur Dropbox erfolgreich",Toast.LENGTH_SHORT).show();
        }

        private boolean exists(String path) {
            try {
                DropboxAPI.Entry existingEntry = mDBApi.metadata(path, 1, null, false, null);
                return true;
            } catch (DropboxServerException e) {
                if(e.error == DropboxServerException._404_NOT_FOUND)
                    return false;
            } catch (DropboxException e){

            }
            return false;
        };
    }

}