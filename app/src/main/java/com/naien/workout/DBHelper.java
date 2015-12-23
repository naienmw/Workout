package com.naien.workout;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.ViewDebug;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "blub.db";
    public static final String CONTACTS_TABLE_NAME = "Workout";
    public static final String WORKOUT_EXERCISE_NAME = "name";
    public static final String[] ALL_SETS = new String[]{"set1","set2","set3","set4","set5","set6","set7","set8","set9","set10","set11","set12","set13"};
    SQLiteDatabase db;
    String[][] allWorkouts;

    private HashMap hp;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
    }
    public void create_new_table(SQLiteDatabase db, String name){
        db.execSQL(
                "create table if not exists " + name + " " +
                        "(id integer primary key, name text,set1 text,set2 text, set3 text,set4 text,set5 text,set6 text,set7 text,set8 text,set9 text,set10 text,set11 text,set12 text,set13 text)"
        );
        db.close();
    }




    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS Workout");
        onCreate(db);
    }

    public boolean doesTableExist(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public int getExIndex(String tablename,String Ex) {

        SQLiteDatabase db = this.getReadableDatabase();
        Integer temp = 0;
        String[] columns = new String[]{"id","name"};
        Cursor c =  db.rawQuery("SELECT * FROM " + tablename + " WHERE name=?", new String[]{Ex + ""});
        if (c.moveToFirst()) {
            temp = c.getInt(0);
        }
        return temp;
    }

    public void deleteSetinEx(String tablename,String Ex,Integer count_set){
        Integer temp = getExIndex(tablename, Ex);
        put_set(tablename,temp,count_set,"0");
        ArrayList<String> allsetsupdate = getAllSets(tablename,Ex);
        putnewSetArray(tablename,Ex,allsetsupdate);
    }

    public void putnewSetArray(String tablename,String Ex,ArrayList<String> newSets){

        for (int k = 1;k<14;k++) {
            put_set(tablename,getExIndex(tablename,Ex),k,"0");
        }

        Integer i = 1;
        for (String temp:newSets){
            put_set(tablename,getExIndex(tablename,Ex),i,temp);
            i = i+1;
        }

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
        contentValues.put("set8", "0");
        contentValues.put("set9", "0");
        contentValues.put("set10", "0");
        contentValues.put("set11", "0");
        contentValues.put("set12", "0");
        contentValues.put("set13", "0");
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

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public String getWoName(String date){

        String WoName = "";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from " + date + " where id=1", null);

        if (res.moveToFirst()){
             WoName= res.getString(res.getColumnIndex("name"));
        }
        return WoName;
    }

    public String[] getAllExercises(String date){

        SQLiteDatabase db = this.getReadableDatabase();

        Integer temp;

        Cursor c =  db.rawQuery("select * from " + date + "", null);
        c.moveToFirst();
        temp = c.getCount();
        String ExName[] = new String[temp-1];


        c = db.rawQuery("select * from " + date + "",null);
        c.moveToFirst();
        Integer i = 0;
            while(c.moveToNext()) {
                ExName[i] = c.getString(1);
                i = i+1;
            }

        return ExName;
    }


    public String[][] getAllWorkouts(){
        SQLiteDatabase db = this.getReadableDatabase();
        Integer countrow = 0;
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name!='android_metadata'", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                countrow = countrow +1;
                c.moveToNext();
            }
        }

        allWorkouts = new String[countrow][2];

        countrow = 0;

        c.moveToFirst();
            while (!c.isAfterLast() ) {
                Integer index = c.getColumnIndex("name");
                allWorkouts[countrow][0] = c.getString(index);
                allWorkouts[countrow][1] = getWoName(allWorkouts[countrow][0]);
                countrow = countrow +1;
                c.moveToNext();
            }
        return allWorkouts;
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
        contentValues.put("set8", "0");
        contentValues.put("set9", "0");
        contentValues.put("set10", "0");
        contentValues.put("set11", "0");
        contentValues.put("set12", "0");
        contentValues.put("set13", "0");
        db.insert(tablename, null, contentValues);
    }

    public ArrayList<String> getAllSets(String tablename , String ex) {

        ArrayList<String> allSetsinEx;
        allSetsinEx = new ArrayList<String>();

        SQLiteDatabase db = getReadableDatabase();

        String WHERE = WORKOUT_EXERCISE_NAME + "=?";
        Cursor cursor = db.rawQuery("SELECT * FROM "+ tablename +" WHERE name=?",new String[] {ex + ""} );

        while(cursor.moveToNext())
        {
            for(int i=1;i<14;i++) {
                String temp = cursor.getString(cursor.getColumnIndex("set" + Integer.toString(i)));
                if (!temp.equals("0")) {
                    allSetsinEx.add(temp);
                }
            }
        }

        return allSetsinEx;
    }
}
