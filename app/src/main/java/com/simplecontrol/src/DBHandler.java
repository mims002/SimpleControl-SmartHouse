package com.simplecontrol.src;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mrinmoy Mondal on 11/12/2017.
 */

public class DBHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "house.db";

    public static final String TABLE_HOUSE = "house";
    public static final String HOUSE_COLUMNS = "(" +
            "id INT PRIMARY KEY," +
            "room_name TEXT," +
            "create_view INTEGER" +
            ")";
    public static final String [] HOUSE_COLUMNS_ARRAY = {"id","room_name","create_view"};

    public static final String ROOM_COLUMNS = "(" +
            "id INT PRIMARY KEY," +
            "device_name TEXT," +
            "device_ip varchar(10)" +
            ")";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

    }

    public void reset(){
        SQLiteDatabase db = getReadableDatabase();

        List<String> tables = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table';", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String tableName = cursor.getString(1);
            if (!tableName.equals("android_metadata") &&
                    !tableName.equals("sqlite_sequence")
                    &&!tableName.equals("house"))
                tables.add(tableName);
            cursor.moveToNext();
        }
        cursor.close();
        for(String tableName:tables) {
            if(MainActivity.DEBUG) Log.d("DEBUG-DBHandler", "Table dropped: " + tableName);
            db.execSQL("DROP TABLE IF EXISTS " + tableName);
        }

        //checks is the room exits
        Cursor cursor2 = db.rawQuery("SELECT name FROM sqlite_master WHERE name='house'",null);
        if(cursor2 != null && cursor2.getCount()> 0){
            db.execSQL("DELETE FROM house");
        }else{
            String query = "CREATE TABLE " + TABLE_HOUSE + HOUSE_COLUMNS;

            db.execSQL(query);

        }
        cursor2.close();
    }

    //removes any space with a underscore
    public String StringCheck (String x){
        return  x.replace(" ", "_");

    }
    //creates a new room table if it doesn't exists
    public void createRoomTable(String name, Integer createView){
        SQLiteDatabase db = getReadableDatabase();

        //checks is the room exits
        if(hasRoom(name)) return;

        //creates a new table
        String query = "CREATE TABLE IF NOT EXISTS " + StringCheck(name) + ROOM_COLUMNS;

        db.execSQL(query);

        //adds a table name in house table
        ContentValues values = new ContentValues();
        values.put(HOUSE_COLUMNS_ARRAY[1], name);
        values.put(HOUSE_COLUMNS_ARRAY[2], createView);
        db.insert(TABLE_HOUSE, null,values);



        //print the database when debugging
        if(MainActivity.DEBUG) Log.d("DEBUG-DBHandler: ", "Created a new Room Table: " + StringCheck(name));
        if(MainActivity.DEBUG) Log.d("DEBUG-DBHandler: ", "printed house: "+dataBaseToString());
    }

    public void insertDevice(String roomName, ContentValues contentValues){
        SQLiteDatabase db = getReadableDatabase();


        db.insert(roomName,null, contentValues);

        Log.d("DEBUG-DBHandler",dataBaseToString(roomName));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_HOUSE + HOUSE_COLUMNS;

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOUSE);
        onCreate(db);
    }

    //adds a new device to the database
    public void addNewDevice(DeviceData deviceData){

    }

    //removes a device from the database
    public void removeDevice(String deviceName){

    }
    //checks if a room exits or not
    public Boolean hasRoom(String roomName){
        SQLiteDatabase db = getReadableDatabase();

        String q = "SELECT * FROM "+ TABLE_HOUSE+ " WHERE "+ HOUSE_COLUMNS_ARRAY[1]+ " = '"+ StringCheck(roomName)+"'";
        Cursor c = db.rawQuery(q,null);

        if(c.getCount()<=0) return false;
        return true;
    }
    //returns the every room stored in house
    public String[][] getAllRooms(){
        String rooms[][];

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "+  TABLE_HOUSE;
        //cursor point to a location in your results
        Cursor c = db.rawQuery(query,null);

        if(c.getCount()<0){
            return null;
        }
        rooms = new String[c.getCount()][2];
        if(MainActivity.DEBUG) Log.d("DEBUG-DBHandler: ", " here "+ dataBaseToString());
        c.moveToFirst();
        int i =0;
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(HOUSE_COLUMNS_ARRAY[1])) != null){

                rooms[i][0] = c.getString((c.getColumnIndex(HOUSE_COLUMNS_ARRAY[1])));
                rooms[i++][1] = c.getInt(c.getColumnIndex(HOUSE_COLUMNS_ARRAY[2]))+"";

            }
            c.moveToNext();
        }
        c.close();
        return rooms;

    }
    //print the database as a string
    public String dataBaseToString(){
        String dbString = "";

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "+  TABLE_HOUSE;

        //cursor point to a location in your results
        Cursor c = db.rawQuery(query,null);

        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(HOUSE_COLUMNS_ARRAY[1])) != null){
                dbString += c.getString(c.getColumnIndex(HOUSE_COLUMNS_ARRAY[1]));
                dbString += ',';
            }

            c.moveToNext();
        }
        return dbString;
    }
    //print the database as a string
    public String dataBaseToString(String databaseName){
        String dbString = "";

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "+  StringCheck(databaseName);

        //cursor point to a location in your results
        Cursor c = db.rawQuery(query,null);

        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(HOUSE_COLUMNS_ARRAY[1])) != null){
                dbString += c.getString(c.getColumnIndex(HOUSE_COLUMNS_ARRAY[1]));
                dbString += ',';
            }
            c.moveToNext();
        }
        return dbString;
    }
}
