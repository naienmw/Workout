package com.naien.workout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

public class XML_Saver_Class
{

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //Sql Verbindung
    private SQLiteDatabase connection;
    private Activity myactivity;
    private DBHelper mydb;
    private DBHelper_Ex mydb_ex;

    //Backup Pfad - entsprechend anpassen
    static final String BACKUP_PATH = Environment.getExternalStorageDirectory() + "/Download/";

    /**
     * Konstruktor
     * @param paramSQLiteDatabase
     */
    public XML_Saver_Class(SQLiteDatabase paramSQLiteDatabase, Activity activity)
    {
        this.connection = paramSQLiteDatabase;
        this.myactivity = activity;
        mydb = new DBHelper(activity);
        mydb_ex =  new DBHelper_Ex(activity);
    }

    /**
     * Sichert die Datenbank.
     * Im Script werden als String[] die Tabellen angebenen.
     * Spalten holt sich die Funktion selber.
     *
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @throws TransformerFactoryConfigurationError
     */
    public boolean backup() throws ParserConfigurationException, IOException, TransformerConfigurationException, TransformerException, TransformerFactoryConfigurationError
    {

        //Check directory
        File path = new File(BACKUP_PATH);
        if (path.exists() == false)
        {
            path.mkdirs();

        }

        verifyStoragePermissions(myactivity);


        //Hier alle Tabellen auflisten die gesichert werden sollen
        String[] tables = getWorkouts();

        Document localDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element localElement1 = localDocument.createElement("backup");
        localDocument.appendChild(localElement1);

        for (int a = 0; a < tables.length; a++)
        {

            Cursor localCursor = this.connection.rawQuery("select * from "+tables[a], null);
            Element localElement55 = localDocument.createElement("table");
            localElement55.setAttribute("name", tables[a]);
            localElement1.appendChild(localElement55);
            while (localCursor.moveToNext())
            {
                String[] arrayOfString = localCursor.getColumnNames();
                Element localElement2 = localDocument.createElement("item");
                localElement55.appendChild(localElement2);
                for (int i = 0; i < arrayOfString.length; i++)
                {
                    Element localElement3 = localDocument.createElement(arrayOfString[i]);

                    Log.d("col "+i+"/"+(arrayOfString.length-1), arrayOfString[i]);
                    String content = "";
                    if (localCursor.getString(i) == null)
                    {
                        content = "";
                    }
                    else
                    {
                        content = localCursor.getString(i);
                    }
                    localElement3.appendChild(localDocument.createTextNode(content));
                    localElement2.appendChild(localElement3);

                }
            } // ende while
        } //ende for tables

        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(localDocument), new StreamResult(new File(BACKUP_PATH + "/your_Sets.xml")));
        Log.d("BACKUP", "FINISHED to " + BACKUP_PATH);
        return false;
    }

    /**
     * Nimmt die database.xml Datei und stellt die Datenbank wieder her
     */
    public void restore() throws Exception
    {
        verifyStoragePermissions(myactivity);
        connection = mydb.getdb();
        File localFile = new File(BACKUP_PATH + "/your_Sets.xml");
        if(localFile.canRead()) {
            mydb.clear();
            Document localDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(localFile);
            localDocument.getDocumentElement().normalize();
            NodeList localNodeList = localDocument.getElementsByTagName("table");

            for (int i = 0; i < localNodeList.getLength(); i++) {

                Node localNode = localNodeList.item(i);
                if (localNode.getNodeType() == 1) {

                    Element localElement = (Element) localNode;
                    Log.d("TABLE", localElement.getAttribute("name"));

                    String deleteifnotex = "drop table if exists " + localElement.getAttribute("name") + " ";

                    this.connection.execSQL(deleteifnotex);

                    String createifnotex = "create table if not exists " + localElement.getAttribute("name") + " " +
                            "(id integer primary key, name text,set1 text,set2 text, set3 text,set4 text,set5 text,set6 text,set7 text,set8 text,set9 text,set10 text,set11 text,set12 text,set13 text)";

                    this.connection.execSQL(createifnotex);

                    this.connection.execSQL("DELETE FROM " + localElement.getAttribute("name"));

                    NodeList nl = localElement.getElementsByTagName("item");
                    for (int a = 0; a < nl.getLength(); a++) {

                        ArrayList<String> aSpalten = new ArrayList<String>();
                        ArrayList<String> aWerte = new ArrayList<String>();

                        Node el = nl.item(a);

                        NodeList spalten = el.getChildNodes();

                        for (int s = 0; s < spalten.getLength(); s++) {
                            Node node = spalten.item(s);

                            aSpalten.add(node.getNodeName());
                            aWerte.add(node.getTextContent());
                        }

                        String sql = "INSERT INTO " + localElement.getAttribute("name") + "(";

                        for (String s : aSpalten) {
                            sql = sql + "`" + s + "`";
                            if (aSpalten.get(aSpalten.size() - 1) != s) {
                                sql = sql + ",";
                            }
                        }

                        sql = sql + ") VALUES (";

                        for (String s : aWerte) {
                            sql = sql + "'" + s + "'";
                            if (aWerte.get(aWerte.size() - 1) != s) {
                                sql = sql + ",";
                            }
                        }
                        sql = sql + ");";
                        Log.d("SQL", sql);
                        this.connection.execSQL(sql);
                        aSpalten.clear();
                        aWerte.clear();
                    }
                }
            }
        }
        else{
            throw new Exception();
        }
        mydb.closedb();

    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public String[] getWorkouts(){

        ArrayList<ArrayList<String>> wo = mydb.getAllWorkouts_Arraylist();

        String[] workouts = new String[wo.size()];
        for(int i = 0 ; i < workouts.length;i++){
            workouts[i] = wo.get(i).get(1);
        }

        return workouts;

    }

}