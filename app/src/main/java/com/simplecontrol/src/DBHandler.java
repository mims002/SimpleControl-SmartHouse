package com.simplecontrol.src;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mrinmoy Mondal on 11/12/2017.
 */

public class DBHandler extends SQLiteOpenHelper{
    private static final String DEBUG_TAG = "DEBUG-DBHandler: ";
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "house.db";

    public static final String TABLE_HOUSE = "house";
    public static final String HOUSE_COLUMNS = "(" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "room_name TEXT," +
            "create_view INTEGER" +
            ")";
    public static final String [] HOUSE_COLUMNS_ARRAY = {"ID","room_name","create_view"};


    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

    }


    /**
     * Resets the database and deletes everything
     */
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

    /**
     * Adds an _ to spaces
     * @param x String that will be formatted
     * @return A new String without any spaces
     */
    //removes any space with a underscore
    public String StringCheck (String x){
        return  x.replace(" ", "_");

    }

    /**
     * Creates a new room table if it doesn't exists
     * @param name Name of the room
     * @param createView 1 if a button if the room needs to be created
     */
    public void addRoom(String name, Integer createView){
        SQLiteDatabase db = getReadableDatabase();
        //checks is the room exits
        if(hasRoom(name)) return;

        //creates a new table
        String query = "CREATE TABLE IF NOT EXISTS " + StringCheck(name) + DeviceData.DATABASE_COLUMN_NAME;

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

   void removeDevice(String roomName, String deviceName){
        SQLiteDatabase db = getReadableDatabase();

        String query = "DELETE FROM "+ StringCheck(roomName) + " WHERE "+ DeviceData.DATABASE_COLUMN_NAME_ARRAY[2]+" = \""+deviceName+"\"";
        db.execSQL(query);
    }

    /**
     * Will add a device and update an existing on if found
     * @param roomName
     * @param contentValues
     */
    boolean addDevice(String roomName, ContentValues contentValues){
        SQLiteDatabase db = getReadableDatabase();

        printDebugMsg(String.format("Adding Device: %s to Room: %s in database", contentValues.getAsString("NAME"),roomName));
        printDebugMsg(contentValues.toString());

        //if device exists updates is
        return (getId(roomName,contentValues.getAsString(DeviceData.DATABASE_COLUMN_NAME_ARRAY[2])) == null &&
                db.insert(StringCheck(roomName),null, contentValues) != -1);
    }

    boolean updatedateDevice(String roomName, String oldDeviceName, ContentValues newDeviceContentValues){
        Integer id = getId(roomName, oldDeviceName);
        if(!hasRoom(roomName) && id == null) return false;

        String where = DeviceData.DATABASE_COLUMN_NAME_ARRAY[0] + "= ?";
        String [] args = {String.valueOf(id)};

        getReadableDatabase().update(
                StringCheck(roomName),
                newDeviceContentValues,
                where, args);

        printDebugMsg("Replaced device with "+ where + id);
        return  true;

    }

    Integer getId(String table, String uniqueIdentifier){
        if(!hasRoom(table)) return null;

        Cursor c = getReadableDatabase().rawQuery("" +
                " SELECT " + DeviceData.DATABASE_COLUMN_NAME_ARRAY[0]+
                " FROM "+ StringCheck(table) +
                " WHERE " + DeviceData.DATABASE_COLUMN_NAME_ARRAY[2] +
                " = \"" + uniqueIdentifier +"\"",null);

        if(c == null || c.getCount() == 0) return null;

        c.moveToFirst();
        return c.getInt(0);

    }
     ArrayList<ContentValues> getAllDevices(String roomName){
        if(!hasRoom(roomName)) return null;

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + StringCheck(roomName);
        Cursor c = db.rawQuery(query,null);

        ArrayList<ContentValues> cvList = new ArrayList<>();


        if(c.moveToFirst()){
            do {
                ContentValues cv = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(c,cv);
                cvList.add(cv);
            } while(c.moveToNext());
        }
        printDebugMsg(dataBaseToString(roomName));
        printDebugMsg(cvList.toString());
        return cvList;
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

    //checks if a room exits or not
    public Boolean hasRoom(String roomName){
        SQLiteDatabase db = getReadableDatabase();

        String q = "SELECT * FROM "+ TABLE_HOUSE+ " WHERE "+ HOUSE_COLUMNS_ARRAY[1]+ " = '"+ roomName+"'";
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
        return dataBaseToString(TABLE_HOUSE);
    }
    //print the database as a string
    public String dataBaseToString(String databaseName){
        if(!hasRoom(databaseName))
            return "Database" + databaseName + "does not exists";

        String dbString = "Database: " + databaseName+ "\n";

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "+  StringCheck(databaseName);

        //cursor point to a location in your results
        Cursor c = db.rawQuery(query,null);

        String [] columName = c.getColumnNames();

        c.moveToFirst();
        //gets the column names
        for(int i = 0; i<c.getColumnCount(); i++){
            dbString += columName[i];
            dbString += '\t';
        }
            dbString += "\n";

        while(!c.isAfterLast()){

            for(int i = 0; i<c.getColumnCount(); i++){
                if(c.getString(c.getColumnIndex(columName[i])) != null){
                    dbString += c.getString(c.getColumnIndex(columName[i]));
                    dbString += '\t';
                }
            }
            dbString +="\n";
            c.moveToNext();
        }
        return dbString;
    }

    //prints a debug message
    void printDebugMsg(String msg){

        if(MainActivity.DEBUG)Log.d("DEBUG-"+this.getClass().getSimpleName(),msg);
    }

}
