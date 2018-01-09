package com.simplecontrol.src;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.view.View;

/**
 * Created by Mrinmoy Mondal on 1/6/2018.
 */

class DeviceDB {

    DeviceDB(DBHandler db){

    }


    //DATABASE_COLUMN_NAME_ARRAY = {"id", "data1", "NAME", "roomName:, "TYPE", "data2", "data3","data4", "data5"}
    //                                0     1       2           3         4         5       6       7       8


    static DeviceData createDeviceFromDatabaseString(ContentValues cv, Context context, Object[] ActoionListeners) {

        String type = cv.getAsString(DeviceData.DATABASE_COLUMN_NAME_ARRAY[3]);


        if(DeviceData.Type.SIMPLE_SWITCH.toString().equals(type)) return createSimpleSwitch(context, cv, (View.OnClickListener)ActoionListeners[0], (View.OnLongClickListener)ActoionListeners[1]);


        printDebugMsg("Failed to Restored "+type);
        return null;
    }


    private static SimpleSwitch createSimpleSwitch(Context context, ContentValues cv, View.OnClickListener onClickListener, View.OnLongClickListener onLongClickListener ){

        String roomName = cv.getAsString(DeviceData.DATABASE_COLUMN_NAME_ARRAY[1]);
        String name = cv.getAsString(DeviceData.DATABASE_COLUMN_NAME_ARRAY[2]);
        String ip = cv.getAsString(DeviceData.DATABASE_COLUMN_NAME_ARRAY[4]);
        int port = cv.getAsInteger(DeviceData.DATABASE_COLUMN_NAME_ARRAY[5]);
        String on = cv.getAsString(DeviceData.DATABASE_COLUMN_NAME_ARRAY[6]);
        String off = cv.getAsString(DeviceData.DATABASE_COLUMN_NAME_ARRAY[7]);

        SimpleSwitch simpleSwitch = new SimpleSwitch(name,roomName,ip,on,off,port, onClickListener, onLongClickListener);

        printDebugMsg("Restored SimpleSwitch" + simpleSwitch.toString());
        return simpleSwitch;
    }

    //prints a debug message
    static void printDebugMsg(String msg){
        if(MainActivity.DEBUG)
            Log.d("DEBUG-DeviceDB",msg);
    }
}
