package com.naien.workout;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper_Ex extends SQLiteOpenHelper {

    public static final String DATABASE_NAME_EX = "exercises.db";
    public static final String EXERCISE_CHEST = "Brust";
    public static final String EXERCISE_LEGS= "Beine";
    public static final String EXERCISE_ARMS = "Arme";
    public static final String EXERCISE_BACK = "Rücken";
    public static final String EXERCISE_SHOULDERS = "Schultern";
    public static final String EXERCISE_SPECIFIC = "name";

    //public static final String[] ALL_SETS = new String[]{"set1","set2","set3","set4","set5","set6","set7","set8","set9","set10","set11","set12","set13"};
    SQLiteDatabase db;
    //String[][] allWorkouts;

    private HashMap hp;
    Context context;

    public DBHelper_Ex(Context context)
    {
        super(context, DATABASE_NAME_EX, null, 1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


    }
    public void create_all(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(
                "create table if not exists " + EXERCISE_CHEST + " " +
                        "(id integer primary key, name text, image BLOB)"
        );
        db.execSQL(
                "create table if not exists " + EXERCISE_BACK + " " +
                        "(id integer primary key, name text, image BLOB)"
        );
        db.execSQL(
                "create table if not exists " + EXERCISE_SHOULDERS + " " +
                        "(id integer primary key, name text,image BLOB)"
        );
        db.execSQL(
                "create table if not exists " + EXERCISE_LEGS + " " +
                        "(id integer primary key, name text,image BLOB)"
        );
        db.execSQL(
                "create table if not exists " + EXERCISE_ARMS + " " +
                        "(id integer primary key, name text,image BLOB)"
        );
        db.close();
    }

    /*protected long saveBitmap(String tablename, String exname , Bitmap bmp)
    {
        int size = bmp.getRowBytes() * bmp.getHeight();
        ByteBuffer b = ByteBuffer.allocate(size); bmp.copyPixelsToBuffer(b);
        byte[] bytes = new byte[size];
        b.get(bytes, 0, bytes.length);
        ContentValues cv=new ContentValues();
        cv.put(CHUNK, bytes);
        this.id= database.insert(TABLE, null, cv);
    }*/

   /* public boolean put_ex(String tablename,Integer count_ex, Integer count_set, String reps_weight) {

        String[] line = {count_ex.toString()};
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String temp = "name"
        contentValues.put(temp, reps_weight);
        db.update(tablename, contentValues, "id = ? ", line);
        db.close();
        return true;
    }*/

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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
        //String[] columns = new String[]{"id","name"};
        Cursor c =  db.rawQuery("SELECT * FROM " + tablename + " WHERE name=?", new String[]{Ex + ""});
        if (c.moveToFirst()) {
            temp = c.getInt(0);
        }
        c.close();
        return temp;
    }

    /*public void deleteSetinEx(String tablename,String Ex,Integer count_set){
        Integer temp = getExIndex(tablename, Ex);
        put_set(tablename, temp, count_set, "0");
        ArrayList<String> allsetsupdate = getAllSets(tablename, Ex);
        putnewSetArray(tablename, Ex, allsetsupdate);
    }

    public void deleteSetinEx_index(String tablename,Integer Exindex,Integer count_set){
        //Integer temp = getExIndex(tablename, Ex);
        put_set(tablename, Exindex, count_set, "0");
        ArrayList<String> allsetsupdate = getAllSets_index(tablename, Exindex);
        putnewSetArray_index(tablename, Exindex, allsetsupdate);
    }*/

    public boolean deleteExinWO(String tablename,String Ex){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(tablename, "name=?", new String[]{Ex});
        db.close();
        return true;
    }

    public boolean deleteExinWO_index(String tablename,Integer index){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(tablename, "ROWID=?", new String[]{index + ""});
        db.execSQL("VACUUM");
        db.close();
        return true;
    }


    /*public void putnewSetArray(String tablename,String Ex,ArrayList<String> newSets){

        for (int k = 1;k<14;k++) {
            put_set(tablename,getExIndex(tablename,Ex),k,"0");
        }

        Integer i = 1;
        for (String temp:newSets){
            put_set(tablename,getExIndex(tablename,Ex),i,temp);
            i = i+1;
        }

    }

    public void putnewSetArray_index(String tablename,Integer ExCount,ArrayList<String> newSets){

        for (int k = 1;k<14;k++) {
            put_set(tablename,ExCount,k,"0");
        }

        Integer i = 1;
        for (String temp:newSets){
            put_set(tablename,ExCount,i,temp);
            i = i+1;
        }

    }*/

    public boolean saveExerciseName(String tablename, String name) {

        if(!isExThere(tablename,name)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            contentValues.put("image", "0");

            db.insert(tablename, null, contentValues);
            db.close();
            return true;
        }

        return false;
    }

    public boolean saveExerciseImage(String tablename, String exname, Bitmap bmp) {

        SQLiteDatabase db = this.getWritableDatabase();
        String[] temp =  new String[]{Integer.toString(getExIndex(tablename, exname))};

        byte[] data = getBitmapAsByteArray(bmp); // this is a function


        ContentValues cv = new ContentValues();
        cv.put("image", data);

        db.update(tablename, cv, "id = ? ", temp);

        return true;
    }

    public Bitmap getExerciseImage(String tablename, String exname){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cur = null;
        cur =  db.rawQuery("SELECT * FROM " + tablename + " WHERE name=?", new String[]{exname + ""});
        if (isExThere(tablename,exname)) {
            if (cur.moveToFirst()) {
                byte[] imgByte = cur.getBlob(cur.getColumnIndex("image"));
                cur.close();
                return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
            }
        }else {
            return BitmapFactory.decodeResource(context.getResources(),R.drawable.arms);
        }

        if (cur != null && !cur.isClosed()) {
            cur.close();
        }

        return null ;
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public boolean isExThere(String tablename, String exname){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = null;
        cur =  db.rawQuery("SELECT * FROM " + tablename + " WHERE name=?", new String[]{exname + ""});
        if (!(cur==null)) {
            cur.moveToFirst();

            while (!cur.isAfterLast()) {
                if (cur.getCount() > 0) {

                    return true;
                }
                cur.moveToNext();
            }
            cur.close();
        }

        if (cur != null && !cur.isClosed()) {
            cur.close();
        }


        return false;
    }




    public String getWoName(String date){

        String WoName = "";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from " + date + " where id=1", null);

        if (res.moveToFirst()){
             WoName= res.getString(res.getColumnIndex("name"));
        }
        res.close();
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
        c.close();
        return ExName;
    }

    public ArrayList<String> getAllExercises_Arraylist(String date){

        ArrayList<String> ExName = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Integer temp;

        Cursor c = db.rawQuery("select * from " + date + "",null);
        c.moveToFirst();
        Integer i = 0;
        while(c.moveToNext()) {
            ExName.add(c.getString(1));
            i = i+1;
        }
        c.close();

        return ExName;
    }

    public ArrayList<Integer> allExIndex(String date){

        ArrayList<Integer> ExIndex = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Integer temp;

        Cursor c = db.rawQuery("select * from " + date + "",null);
        c.moveToFirst();
        Integer i = 0;
        while(c.moveToNext()) {
            ExIndex.add(c.getInt(c.getColumnIndex("id")));
            i = i+1;
        }
        c.close();

        return ExIndex;
    }



    public ArrayList<ArrayList<String>> getAllWorkouts_Arraylist(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name!='android_metadata'", null);
        ArrayList<ArrayList<String>> allWorkouts = new ArrayList<ArrayList<String>>();


        c.moveToFirst();
        while (!c.isAfterLast() ) {
            Integer index = c.getColumnIndex("name");
            String date = c.getString(index);
            ArrayList<String> oneWorkout = new ArrayList<>();

            oneWorkout.add(getWoName(date));
            oneWorkout.add(date);
            allWorkouts.add(oneWorkout);
            c.moveToNext();
        }
        c.close();

        return allWorkouts;
    }


    public SQLiteDatabase getdb(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db;
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
        db.close();
    }

    public ArrayList<String> getAllSets(String tablename , String ex) {

        ArrayList<String> allSetsinEx;
        allSetsinEx = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

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
        cursor.close();
        return allSetsinEx;
    }

    public ArrayList<String> getAllSets_index(String tablename , Integer index) {

        ArrayList<String> allSetsinEx;
        allSetsinEx = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+ tablename +" WHERE ID=?",new String[] {index + ""} );

        while(cursor.moveToNext())
        {
            for(int i=1;i<14;i++) {
                String temp = cursor.getString(cursor.getColumnIndex("set" + Integer.toString(i)));
                if (!temp.equals("0")) {
                    allSetsinEx.add(temp);
                }
            }
        }
        cursor.close();
        return allSetsinEx;
    }

    public ArrayList<String> getAllDatesExeptcurrent(){ //in order from current to last
        ArrayList<String> dates = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name!='android_metadata'", null);


        for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {
            dates.add(cursor.getString(cursor.getColumnIndex("name")));
        }

        /*while(!c.isAfterLast()){
            dates.add(c.getString(c.getColumnIndex("name")));
            c.moveToNext();
        }*/
        cursor.close();

        dates.remove(0);

        return dates;
    }


    private String FineSets(String setsfromdb){
        String finesets;

        String[] temp = setsfromdb.split(",");

        finesets = temp[0] + " x " + temp[1];

        return finesets;
    }
}
