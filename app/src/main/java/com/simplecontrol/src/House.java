package com.simplecontrol.src;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Mrinmoy Mondal on 8/18/2017.
 * House Class stores all the rooms
 * Interact with house class to addRoom, remove and get Room
 */

class House {
    //will hold all the rooms in memory
    private ArrayList<RoomData> allRooms;
    //appLayout
    private AppLayout al;
    final static int HOUSE_VERSION = 1;

    //database
    DBHandler db;

    House(AppLayout al){
        this.al=al;
        allRooms = new ArrayList<RoomData>();
        db = new DBHandler(al.context,null,null, HOUSE_VERSION);

    }

    //restores all the rooms
    void onCreate(){

       for(String roomName[]: db.getAllRooms()){
           if(MainActivity.DEBUG) Log.d("DEBUG-House", "Restored room "+ roomName[0]+ "| Create View "+ roomName[1]);

           if(roomName[1].equals(1+""))
               allRooms.add(new RoomData(roomName[0],al,true));
           else
               allRooms.add(new RoomData(roomName[0],null,false));
       }
    }

    //adds new room only. same rooms are not added twice
    //do not call on an empty house
    //looks into database and adds a room if necessary
    //add to view
    void addRoom(String name){

        if(!db.hasRoom(name)){
            allRooms.add(new RoomData(name,al,true));
            db.createRoomTable(name,1);
        }


    }

    //adds new room only. same rooms are not added twice
    //does not add to view
    protected void addRoom(String name,AppLayout al){
        db.createRoomTable(name,0);
        allRooms.add(new RoomData(name,this.al,false));
    }

    //returns the reference to a device
    //do not call if allRoom or device empty
    public DeviceData getDevice(String deviceName){

        String [] name = deviceName.split(this.allRooms.get(0).getSeparator());

        if(MainActivity.DEBUG) Log.d("DEBUG-House", "Recieved get device call"+ Arrays.toString(name));

        RoomData room = this.getRoom(name[0]);

        DeviceData deviceData = room.getDevice(deviceName);

        //debug
        if(deviceData == null)
            if(MainActivity.DEBUG) Log.e("DEBUG-House", "Searched for device "+ deviceName + "->" + "Failed");
        else if(MainActivity.DEBUG) Log.d("DEBUG-House", "Searched for device "+ deviceName + "->" + "found");

        return deviceData;


    }

    //returns the reference to a room
    public RoomData getRoom(String roomName){

        //debug
        if(MainActivity.DEBUG) Log.d("DEBUG-House", "Searched for room "+ roomName);

        RoomData room = null;
        for(int i=0; i<allRooms.size(); i++){
            if(allRooms.get(i).getRoomName().equals(roomName))
                room = allRooms.get(i);
        }

        //debug
        if(db.hasRoom(roomName))
            if(MainActivity.DEBUG) Log.d("DEBUG-House", "Searched for room "+ roomName + "->" + "Found");
        else
            if(MainActivity.DEBUG) Log.e("DEBUG-House", "Searched for room "+ roomName + "->" + "Failed");

        return room;
    }
    /*
    public void saveAll(){
        for(int i=0; i<allRooms.size();i++){
            allRooms.get(i).saveAll();
        }
    }*/
}
