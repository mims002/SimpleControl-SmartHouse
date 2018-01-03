package com.simplecontrol.src;

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
 * This class hold the room name and all the devices in each room.
 * This class will be used to add devices and remove in each room.
 * This class will also save all the data to the Shared Preferences.
 *
 * Each room has device with the tag "<Room Name>###<Device Name>###<function>"
 *  function can be anything the devica can perform such as on, off, or timer
 *
 */

public class RoomData {

    private TextView roomNameTextView;
    private String roomName;

    //holds all the devices in the room
    private ArrayList<DeviceData> devices;
    //this will be the unique separator that helps identity each switch name from room name
    //user cannot type this in for a name
    private String separator;

    //hold the layout for each room
    //used to dinamycally add buttons and change image size
    private RelativeLayout room;
    private AppLayout al;


    //grid layout
    private GridLayout gridLayout;
    private int row;
    private int col;
    private int maxRow, maxCol;

    //initializes a new room layout which will be added to the mainView
    //initializes room name and creates device arraylist
    public RoomData(String roomName, AppLayout al, boolean createView) {
        devices = new ArrayList<DeviceData>();
        this.al = al;
        this.roomName = roomName;
        this.separator = "###";

        //if its not the main ac/heat system then a room needs to be added
        if (createView) {
            createView();
        }

        //restores all the buttons to the view
        onCreate();

    }

    private void createView() {
        this.room = (RelativeLayout) LayoutInflater.from(al.context).inflate(R.layout.room_view, null);
        this.roomNameTextView = (TextView) room.findViewById(R.id.TitleBar);
        this.setRoomName(roomName);
        //gives each room a unique tag
        this.room.setTag(roomName);
        //adds the room to the main view
        this.al.addLayout(this.room);
        this.gridLayout = (GridLayout) room.findViewById(R.id.gridLayout);
        this.row = 0;
        this.col = 0;
        this.maxCol = 1;
        //adds the add device button as the first device
        ImageButton addDeviceButton = (ImageButton) room.findViewById(R.id.addDevice);
        addDeviceButton.setTag(roomName + separator + "addDevice");
        addDeviceButton.setOnClickListener(al.onClickListener);
        this.addDevice(new DeviceData("addDevice", addDeviceButton, DeviceData.Type.BUTTON));

        if (MainActivity.DEBUG) Log.d("DEBUG-RoomData", "Created Room - " + roomName);
    }

    private void onCreate() {
        //have a database to string array method in devices and restore from there
    }


    //adds a new device and tags the name of the room
    //adds the device to the device array
    protected void addDevice(DeviceData deviceData) {
        //removes the device if it already exists
        //this.removeDevice(deviceData.getName());
        devices.add(deviceData);
        //adds the room name to the device
        DeviceData dt = devices.get(devices.size() - 1);
        dt.setNameTag(roomName + separator + dt.getName() + separator + "parent");

        //saves the device only if its a user created device
        //if(dt.isUserCreated()) saveDeviceData.saveData(dt);


    }

    //returns a device with the tag
    //only returns the parent view of the device
    protected DeviceData getDevice(String tag) {
        DeviceData deviceData = null;

        String[] tags = tag.split("###");
        tag = tags[0] + separator + tags[1] + separator + "parent";

        for (int i = 0; i < devices.size(); i++) {
            //debug
            if (MainActivity.DEBUG)
                Log.d("DEBUG-RoomData", "Searching at " + devices.get(i).getNameTag() + " for " + tag + " : " + devices.get(i).getNameTag().equals(tag));


            if (devices.get(i).getNameTag().equals(tag)) {
                deviceData = devices.get(i);
                Log.d("DEBUG-RoomData", "Found " + devices.get(i).getNameTag() + " at " + tag);
                break;
            }
        }

        return deviceData;
    }

    //removes the device from the arraylist and the view
    protected void removeDevice(String name) {
        for (int i = 0; i < devices.size(); i++) {
            if (devices.get(i).getName().equals(name)) {

                Log.d("DEBUG-RoomData", "Removing Device from room: " + roomName + "| DeviceName: " + devices.get(i).getView());
                devices.get(i).getView().setVisibility(View.GONE);

                devices.remove(i);
                Log.d("DEBUG-RoomData", "Removed Device from room: " + roomName + "| DeviceName: " + name);
                return;
            }
        }
    }

    protected void addNewDevice(DeviceData deviceData) {

        //adds the view (device) to the gridlayout in each room view

        //increases the view backgound size for each roomView
        if ((this.devices.size() - 1) % maxCol == 0) {
            row++;
            col = 0;
        }
        //adds the new button
        deviceData.getView().setLayoutParams(new GridLayout.LayoutParams(GridLayout.spec(row), GridLayout.spec(col++)));
        gridLayout.addView(deviceData.getView());

        Log.d("DEBUG-RoomData", "New Device was added to room: " + roomName + "| DeviceName: " + deviceData.getName());

        //adds the device to the device array
        this.addDevice(deviceData);

    }

    /*
    public void saveAll(){
        saveDeviceData = new SaveDeviceData(true);
        for(int i =0; i<devices.size();i++){
            if(devices.get(i).isUserCreated())  saveDeviceData.saveData(devices.get(i));
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

    @Override
    public String toString() {
        return "RoomData{" +
                "roomName='" + roomName + '\'' +
                ", devices=" + devices +
                '}';
    }
}