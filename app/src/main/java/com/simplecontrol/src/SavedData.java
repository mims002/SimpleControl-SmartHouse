package com.simplecontrol.src;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Mrinmoy Mondal on 8/30/2017.
 * SaVE will restore and save all the devices and rooms.
 * It uses a sharedPreference to save everything.
 * The constructor needs access to the app layout and the house.
 * LOG tag - DEBUG-SavedData
 */


public class SavedData {

    private House house;
    private AppLayout appLayout;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int roomSize;


    public SavedData(House house, AppLayout appLayout){
        this.house = house;
        this.appLayout = appLayout;

        //creates and reads the files from SharedPreferences
        sharedPreferences = appLayout.mainActivity.getSharedPreferences("RoomNames", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        this.roomSize = sharedPreferences.getInt("roomSize",0);
    }

    //this will save rooms to the shared preference
    //do not call with duplicate names
    //remove old names or it will not be restored
    public void insertRoomName(String name){
        editor.putString(""+roomSize, name);
        editor.putInt("roomSize",++roomSize);

        editor.commit();


        if(MainActivity.DEBUG) Log.d("DEBUG-SavedData", "Room saved| current room: "+roomSize+" |Room Name: "+name );
    }

    //restores all the room that were saved in the shared SharedPreferences
    public void restore(){

        if(MainActivity.DEBUG) Log.d("DEBUG-SavedData", "Restore started| number of rooms saved: "+ roomSize);

        if(roomSize <= 0) return;

        for(int i=0; i<roomSize; i++){

            String roomName = sharedPreferences.getString(""+i,null);
            house.addRoom(roomName);

            if(MainActivity.DEBUG) Log.d("DEBUG-SavedData", "Room restored| room number: "+i+" |Room Name: "+ roomName );
        }
    }

}

