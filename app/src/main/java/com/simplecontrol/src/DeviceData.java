package com.simplecontrol.src;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.view.View;


/**
 * Created by Mrinmoy Mondal on 8/17/2017.
 * This class holds the on off command for each device and uses an AsyncTask to execute it.
 * It has parameters IP address, port and the String for on or off.
 *
 * Device tag "<Room Name>###<Device Name>###<function>"
 */


abstract class DeviceData{
    //holds the type of view it is
    public enum Type{
        SIMPLE_SWITCH, BUTTON, TEXTFIELD, THERMOSTAT,

    }

    private String name,roomName, tag;

    private int port ;
    private boolean status;

    private boolean hasView;

    private View view;
    private Type type;

    String seperator = "###";

    DeviceData(String name, String roomName, Type type){
        this.roomName = roomName;
        this.name = name;
        this.type=type;
        this.tag = roomName + seperator + name + seperator + "parent";

        hasView = false;
    }





    static  String   DATABASE_COLUMN_NAME = ("(id INTEGER PRIMARY KEY, roomName TEXT, NAME TEXT, TYPE TEXT, data4 TEXT, data5 TEXT,data6 TEXT, data7 TEXT, data8 TEXT)");
    static  String[] DATABASE_COLUMN_NAME_ARRAY = {"id", "roomName", "NAME", "TYPE", "data4", "data5","data6", "data7", "data8"};



/////////////////////////////////////////////////////////////
    boolean hasView(){
        return view != null;
    }

    View getView() {
        return view;
    }

    abstract View createView(Context context);

    public void setTag(String n) {
        tag = n;
    }
    public String getTag() {
        return tag;
    }

    abstract void function1();
    abstract void function2();
    abstract void function3();
    abstract void function4();

    //if the device is on or off
    public boolean status() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }
    public String getRoomName() {
        return roomName;
    }

    public void setName(String name) {
        this.name = name;
    }


    //prints a debug message
    void printDebugMsg(String msg){

        if(MainActivity.DEBUG)Log.d("DEBUG-"+this.getClass().toString(),msg);
    }

    /**
     * Creates the contentValues needed for database
     * @return Database ContentValues
     */
    abstract ContentValues createDataBaseString();

    abstract String getFunction(int f);

    String getRootTag(){
        return roomName+seperator+name+seperator;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof DeviceData) {
            DeviceData y = ((DeviceData) o);

            return (y.getRootTag().equals(this.getRootTag()));
        }
        return false;
    }

    abstract void updateDeviceData(DeviceData deviceData);


    ////////////////////////////////////////////////////////////////////////////
    @Override
    public String toString(){
        return ("DeviceName: "+ name + "| Room Name: "+ roomName+ "| Type: "+type);
    }

}

