package com.naien.workout;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "blub.db";
    public static final String CONTACTS_TABLE_NAME = "Workout";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String WORKOUT_EXERCISE_NAME = "name";

    private HashMap hp;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table Workout " +
                        "(id integer primary key, name text,set1 text,set2 text, set3 text,set4 text,set5 text,set6 text,set7 text)"
        );
    }
    public void create_new_table(SQLiteDatabase db, String name){
        db.execSQL(
                "create table if not exists " + name + " "+
                        "(id integer primary key, name text,set1 text,set2 text, set3 text,set4 text,set5 text,set6 text,set7 text)"
        );
        db.close();
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS Workout");
        onCreate(db);
    }


    public boolean saveExerciseName(String tablename, String name) {

        String[] position = {"1"};
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("set1", "0");
        contentValues.put("set2", "0");
        contentValues.put("set3", "0");
        contentValues.put("set4", "0");
        contentValues.put("set5", "0");
        contentValues.put("set6", "0");
        contentValues.put("set7", "0");
        db.insert(tablename, null, contentValues);
        db.close();

        return true;
    }

    public boolean updateData (String first_input, String second_input) {
        String[] position = {"1"};
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", first_input);
        contentValues.put("email", second_input);
        db.update(CONTACTS_TABLE_NAME, contentValues, "id = ? ", position);
        return true;
    }

    public boolean put_set (String table_name,Integer count_ex, Integer count_set, String reps_weight) {

        String[] line = {count_ex.toString()};
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String temp = "set"+count_set.toString();
        contentValues.put(temp, reps_weight);
        db.update(table_name, contentValues, "id = ? ", line);
        db.close();
        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from Workout where id=" + id + "", null);
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String name, String phone, String email, String street,String place)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("Workout", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Workout",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public SQLiteDatabase getdb(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db;
    }


    public ArrayList<String> getAllCotacts()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Workout", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(WORKOUT_EXERCISE_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public void FillLineWithZeros(String tablename) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("set1", "0");
        contentValues.put("set2", "0");
        contentValues.put("set3", "0");
        contentValues.put("set4", "0");
        contentValues.put("set5", "0");
        contentValues.put("set6", "0");
        contentValues.put("set7", "0");
        db.insert(tablename, null, contentValues);
    }
}
