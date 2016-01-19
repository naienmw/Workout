package com.naien.workout;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

public class copydbhelper extends SQLiteOpenHelper {

    boolean dbExist = true;

    private static final String DB_PATH = "/data/data/com.naien.workout/databases/";

    private static final String DB_NAME = "exercises.db";

    private final Context myContext;

    public SQLiteDatabase db;

    public copydbhelper(Context context) {

        super(context, DB_NAME , null, 1);
        getWritableDatabase();
        this.myContext = context;
        close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        dbExist = false;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void createDatabase() {
        createDB();
    }

    private void createDB() {

        if(!dbExist) {
            getWritableDatabase();
            copyDBFromResource();
        }
    }

    private boolean DBExists() {
        SQLiteDatabase db = null;

        //getReadableDatabase();

        try {
            String databasePath = DB_PATH + DB_NAME;
            db = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
            db.setLocale(Locale.getDefault());
            db.setLockingEnabled(true);
            db.setVersion(1);
        } catch (SQLiteException e) {
            Log.e("SqlHelper", "database not found");
        }

        if (db != null) {
            db.close();
        }
        return db != null ? true : false;
    }

    private void copyDBFromResource() {
        //close();
        InputStream inputStream = null;
        OutputStream outStream = null;
        String dbFilePath = DB_PATH + DB_NAME;

        try {
            inputStream = myContext.getAssets().open(DB_NAME);
            outStream = new FileOutputStream(dbFilePath);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }

            outStream.flush();
            outStream.close();
            inputStream.close();

            //getWritableDatabase().close();

        } catch (IOException e) {
            throw new Error("Problem copying database from resource file.");
        }

    }

}