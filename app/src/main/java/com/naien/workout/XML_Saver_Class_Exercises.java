package com.naien.workout;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XML_Saver_Class_Exercises
{

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //Sql Verbindung
    private SQLiteDatabase connection;
    private Activity myactivity;
    private DBHelper_Ex mydb_ex;

    //Backup Pfad - entsprechend anpassen
    static final String BACKUP_PATH = Environment.getExternalStorageDirectory() + "/Download/";

    /**
     * Konstruktor
     * @param paramSQLiteDatabase
     */
    public XML_Saver_Class_Exercises(SQLiteDatabase paramSQLiteDatabase, Activity activity)
    {
        this.connection = paramSQLiteDatabase;
        this.myactivity = activity;
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
        String[] tables = {"Arme","Beine","Brust","Rücken","Schultern"};

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
                for (int i = 0; i < 2;i++)//arrayOfString.length; i++)
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

        TransformerFactory.newInstance().newTransformer().transform(new DOMSource(localDocument), new StreamResult(new File(BACKUP_PATH+ "/your_Exercises.xml")));
        Log.d("BACKUP", "FINISHED to " + BACKUP_PATH);

        return false;
    }

    /**
     * Nimmt die database.xml Datei und stellt die Datenbank wieder her
     */
    public void restore() throws Exception
    {

        verifyStoragePermissions(myactivity);

        File localFile = new File(BACKUP_PATH + "/your_Exercises.xml");
        if(localFile.canRead()){
            Document localDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(localFile);
            localDocument.getDocumentElement().normalize();
            NodeList localNodeList = localDocument.getElementsByTagName("table");

            for (int i = 0; i<localNodeList.getLength() ; i++)
            {

                Node localNode = localNodeList.item(i);
                if (localNode.getNodeType() == 1)
                {

                    Element localElement = (Element)localNode;
                    Log.d("TABLE",  localElement.getAttribute("name"));


                    String deleteifnotex = "drop table if exists " + localElement.getAttribute("name") + " ";

                    this.connection.execSQL(deleteifnotex);

                    String createifnotex = "create table if not exists " + localElement.getAttribute("name") + " " + "(id integer primary key, name text) ";

                    this.connection.execSQL(createifnotex);

                    this.connection.execSQL("DELETE FROM " + localElement.getAttribute("name"));

                    NodeList nl = localElement.getElementsByTagName("item");
                    for (int a = 0; a < nl.getLength(); a++)
                    {

                        ArrayList<String> aSpalten = new ArrayList<String>();
                        ArrayList<String> aWerte = new ArrayList<String>();

                        Node el = nl.item(a);

                        NodeList spalten = el.getChildNodes();

                        for (int s = 0; s < spalten.getLength(); s++)
                        {
                            Node node = spalten.item(s);

                            aSpalten.add(node.getNodeName());
                            aWerte.add(node.getTextContent());
                        }

                        String sql ="INSERT INTO " +localElement.getAttribute("name") + "(";

                        for  (String s : aSpalten)
                        {
                            sql = sql + "`"+s+"`";
                            if (aSpalten.get(aSpalten.size()-1)!=s)
                            {
                                sql = sql + ",";
                            }
                        }

                        sql = sql + ") VALUES (";

                        for  (String s : aWerte)
                        {
                            sql = sql + "'"+s+"'";
                            if (aWerte.get(aWerte.size()-1)!=s)
                            {
                                sql = sql + ",";
                            }
                        }
                        sql = sql + ");";
                        Log.d("SQL", sql);
                        this.connection.execSQL(sql);
                        aSpalten.clear(); aWerte.clear();
                    }
                }
            }
        }else{
            throw new Exception();
        }

        mydb_ex.closedb();

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


}