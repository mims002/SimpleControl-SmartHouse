package com.simplecontrol.src;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mrinmoy Mondal on 8/17/2017.
 * This class hold the room name and all the simple_switch_view in each room.
 * This class will be used to add simple_switch_view and remove in each room.
 * This class will also save all the data to the Shared Preferences.
 *
 * Each room has device with the tag "<Room Name>###<Device Name>###<function>"
 *  function can be anything the devica can perform such as on, off, or timer
 *
 */

public class RoomData {


    private TextView roomNameTextView;
    private String roomName;

    //holds all the simple_switch_view in the room
    private ArrayList<DeviceData> devices;
    //this will be the unique separator that helps identity each switch name from room name
    //user cannot type this in for a name
    private String separator;

    private boolean restore, hasView;


    //Main layout of the room
    //used to dinamycally add buttons and change image size
    private RelativeLayout room;
    private AppLayout appLayout;


    //grid layout holds the simple_switch_view
    private GridLayout gridLayout;
    private int row;
    private int col;
    private int maxRow, maxCol;


    /**
     * Initializes room name and creates device arraylist
     * @param roomName The roomName
     * @param appLayout AppLayout that hold context and action listeners
     */
    public RoomData(String roomName, AppLayout appLayout) {
        this.devices = new ArrayList<DeviceData>();
        this.appLayout = appLayout;
        this.roomName = roomName;
        this.separator = "###";
        this.hasView = false;
        this.restore = false;

        //create view
        if(appLayout!=null) createView();
        //restores all the simple_switch_view to the view
        onCreate();

    }

    View createView() {

        this.hasView = true;

        this.room = (RelativeLayout) LayoutInflater.from(appLayout.context).inflate(R.layout.room_view, null);
        this.roomNameTextView = (TextView) room.findViewById(R.id.TitleBar);
        this.setRoomName(roomName);

        //gives each room a unique tag
        this.room.setTag(roomName);


        this.gridLayout = (GridLayout) room.findViewById(R.id.gridLayout);
        this.row = 0;
        this.col = 0;
        this.maxCol = 1;

        //adds the add device button as the first device
        ImageButton addDeviceButton = (ImageButton) room.findViewById(R.id.addDevice);
        addDeviceButton.setTag(roomName + separator + "addDevice");
        addDeviceButton.setOnClickListener(appLayout.onClickListener);

        printDebugMsg( "Created Room - " + roomName);

        return this.room;
    }

    View getView(){
        return this.room;
    }

    boolean hasView(){
        return hasView;
    }

    //accesses the database and adds all the simple_switch_view
    private void onCreate() {

        //have a database to string array method in simple_switch_view and restore from there
        if(appLayout ==null) return;
        ArrayList<ContentValues> devices= appLayout.db.getAllDevices(roomName);

        printDebugMsg(appLayout.db.dataBaseToString(roomName));

        restore = true;
        Object[] actionListeners = {appLayout.onClickListener, appLayout.onLongClickListener};
        for(int i =0; devices!=null && i<devices.size();i++){
            addDevice(DeviceDB.createDeviceFromDatabaseString(devices.get(i), appLayout.context, actionListeners));
        }
        restore = false;
    }



    //adds a new device and tags the name of the room
    //adds the device to the device array
    void addDevice(DeviceData deviceData) {

        //same simple_switch_view are not added twice
        if(deviceData == null || this.getDevice(deviceData.getTag())!= null )
            return;



        //saves the device only if its not in restore mode
        if(appLayout != null && !restore ) {
            appLayout.db.addDevice(this.roomName, deviceData.createDataBaseString());

            //prints the database
            printDebugMsg(String.format("Adding Device: %s from Room: %s to database",deviceData.getName(),getRoomName()));

            printDebugMsg(deviceData.createDataBaseString().toString());
        }


        //adds the view (device) to the gridlayout in each room view
        View view;
        if((view = deviceData.createView(appLayout.context))!=null) {
            //increases the view background size for each roomView
            if (this.devices.size() != 0 && (this.devices.size() - 1) % maxCol == 0) {
                row++;
                col = 0;
            }

            //adds the new button
            view.setLayoutParams(new GridLayout.LayoutParams(GridLayout.spec(row), GridLayout.spec(col++)));
            gridLayout.addView(view);

            printDebugMsg("New Device was added to room: " + roomName + "| DeviceName: " + deviceData.getName());

        }else
            printDebugMsg("New Device was added to room: " + roomName + "| DeviceName: " + deviceData.getName() + "without view");

        //adds device to the arraylist
        devices.add(deviceData);
    }

    Context getContext(){
        return appLayout.context;
    }



    void updateDevice(String deviceName, DeviceData newDeviceData){
        DeviceData deviceData = getDevice(deviceName);
        if(!deviceData.getType().equals(newDeviceData.getType())) return;

        deviceData.updateDeviceData(newDeviceData);
        appLayout.db.updatedateDevice(roomName, deviceName, newDeviceData.createDataBaseString() );

    }

    //returns a device with the tag
    //only returns the parent view of the device
     DeviceData getDevice(String deviceName) {
        DeviceData deviceData = null;

        for (int i = 0; i < devices.size(); i++) {
            //debug
            printDebugMsg( "Searching at " + devices.get(i).getTag() + " for " + deviceName + " : " + devices.get(i).getName());


            if (devices.get(i).getName().equals(deviceName)) {
                deviceData = devices.get(i);
                printDebugMsg( "Found " + devices.get(i).getTag());
                break;
            }
        }

        return deviceData;
    }

    //removes the device from the arraylist and the view
    protected void removeDevice(String name) {
        for (int i = 0; i < devices.size(); i++) {
            if (devices.get(i).getName().equals(name)) {

                printDebugMsg("Removing Device from room: " + roomName + "| DeviceName: " + devices.get(i).getName());
                devices.get(i).getView().setVisibility(View.GONE);

                devices.remove(i);
                appLayout.db.removeDevice(roomName,name);
                printDebugMsg( "Removed Device from room: " + roomName + "| DeviceName: " + name);
                return;
            }
        }
    }


    /*
    public void saveAll(){
        saveDeviceData = new SaveDeviceData(true);
        for(int i =0; i<simple_switch_view.size();i++){
            if(simple_switch_view.get(i).isUserCreated())  saveDeviceData.saveData(simple_switch_view.get(i));
        }

    }*/
    //returns the character separator
    public String getSeparator() {
        return separator;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        if (roomNameTextView != null) this.roomNameTextView.setText(roomName);
        this.roomName = roomName;
    }

    //prints a debug message
    void printDebugMsg(String msg){
        if(MainActivity.DEBUG)
            Log.d("DEBUG-"+this.getClass().getSimpleName(),msg);
    }

    @Override
    public String toString() {
        return "RoomData{" +
                "roomName='" + roomName + '\'' +
                ", simple_switch_view=" + devices +
                '}';
    }
}