package com.simplecontrol.src;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
    //saves the devices
    SaveDeviceData saveDeviceData;
    //if this is the main view
    boolean createView;
    //grid layout
    private GridLayout gridLayout;
    private int row;
    private int col;
    private int maxRow,maxCol;

    //initializes a new room layout which will be added to the mainView
    //initializes room name and creates device arraylist
    public RoomData(String roomName, AppLayout al, boolean createView) {
        devices = new ArrayList<DeviceData>();
        this.al = al;
        this.roomName = roomName;
        this.separator = "###";
        this.createView =createView;
       // this.saveDeviceData = new SaveDeviceData(false);
        //if its not the main ac/heat system then a room needs to be added
        if (createView) {
            //
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
            this.maxCol=1;
            //adds the add device button as the first device
            ImageButton addDeviceButton = (ImageButton) room.findViewById(R.id.addDevice);
            addDeviceButton.setTag(roomName + separator + "addDevice");
            addDeviceButton.setOnClickListener(al.onClickListener);
            this.addDevice(new DeviceData("addDevice", addDeviceButton, DeviceData.Type.BUTTON));

            if (MainActivity.DEBUG) Log.d("DEBUG-RoomData", "Created Room - " + roomName);


            //saveDeviceData.restore();
        }

    }


    //adds a new device and tags the name of the room
    //adds the device to the device array
    protected void addDevice(DeviceData deviceData) {
        //removes the device if it already exists
        //this.removeDevice(deviceData.getName());
        devices.add(deviceData);
        //adds the room name to the device
        DeviceData dt = devices.get(devices.size() - 1);
        dt.setNameTag(roomName + separator + dt.getName()+ separator + "parent");

        //saves the device only if its a user created device
        //if(dt.isUserCreated()) saveDeviceData.saveData(dt);


    }

    //returns a device with the tag
    //only returns the parent view of the device
    protected DeviceData getDevice(String tag){
        DeviceData deviceData = null;

        String [] tags = tag.split("###");


        tag = tags[0]+separator+tags[1]+separator+"parent";




        for(int i=0; i<devices.size(); i++){
            //debug
            if(MainActivity.DEBUG)
                Log.d("DEBUG-RoomData", "Searching at "+ devices.get(i).getNameTag() +" for "+ tag + " : "+ devices.get(i).getNameTag().equals(tag) );




            if(devices.get(i).getNameTag().equals(tag)) {
                deviceData = devices.get(i);
                Log.d("DEBUG-RoomData", "Found "+ devices.get(i).getNameTag() +" at "+ tag );
                break;
            }
        }

        return deviceData;
    }
    //removes the device from the arraylist and the view
    protected void removeDevice(String name){
        for(int i=0; i<devices.size();i++){
            if(devices.get(i).getName().equals(name)){

                Log.d("DEBUG-RoomData", "Removing Device from room: " + roomName + "| DeviceName: " + devices.get(i).getView());
                devices.get(i).getView().setVisibility(View.GONE);
                saveDeviceData.removeData(devices.get(i));
                devices.remove(i);
                Log.d("DEBUG-RoomData", "Removed Device from room: " + roomName + "| DeviceName: " + name);
                return;
            }
        }
    }

    protected void addNewDevice(DeviceData deviceData) {

        //adds the view (device) to the gridlayout in each room view

        //increases the view backgound size for each roomView
        if ((this.devices.size()-1) % maxCol == 0 ) {
            row++;
            col=0;
        }
        //adds the new button
        deviceData.getView().setLayoutParams(new GridLayout.LayoutParams(GridLayout.spec(row),GridLayout.spec(col++)));
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
        if(roomNameTextView!=null)this.roomNameTextView.setText(roomName);
        this.roomName = roomName;
    }

    @Override
    public String toString() {
        return "RoomData{" +
                "roomName='" + roomName + '\'' +
                ", devices=" + devices +
                '}';
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    private class SaveDeviceData{
        private SharedPreferences sharedPreferences;
        private SharedPreferences.Editor editor;
        private int size;
        private boolean restoreInProgress;
        private int members;

    private SaveDeviceData(boolean init){
            sharedPreferences = al.mainActivity.getSharedPreferences(roomName, Context.MODE_PRIVATE);

            size = sharedPreferences.getInt("size",0);
            if(init) size=0;
            restoreInProgress = false;
            members =9;



        }
    //removes the device from the shared preference
    private void removeData(DeviceData deviceData){
        int index =-1;
        //find the index of the device
        for(int i=0; i<size;i++){
            index = (i) * members;
            if(sharedPreferences.getString(index+"","").equals(deviceData.getName())) break;
        }

        //removes the device
        for(int i=0; i<members && index>=0;i++ ) {
            editor.remove((i + index) + "");
        }
        editor.apply();

        if(MainActivity.DEBUG) Log.d("DEBUG-SaveDeviceData", "Removed Device | "+ deviceData);
    }
    private void saveData(DeviceData deviceData){
        if(!restoreInProgress) {
            editor = sharedPreferences.edit();
            int index = (size) * members;
            editor.putString(index + "", deviceData.getName());
            editor.putString(++index + "", deviceData.getIP());
            editor.putInt(++index + "", deviceData.getPort());
            editor.putString(++index + "", deviceData.getOn());
            editor.putString(++index + "", deviceData.getOff());
            editor.putString(++index + "", deviceData.status()+"");
            editor.putString(++index + "", deviceData.getNameTag());
            editor.putString(++index + "", deviceData.getType() + "");
            editor.putString(++index + "", deviceData.getView().getTag().toString());
            editor.putInt("size", ++size);

            editor.commit();

            if (MainActivity.DEBUG)
                Log.d("DEBUG-SaveDeviceData", "Device saved | Room Name: "+ roomName +" "+ deviceData+ "| number of devices: " + size);
            if(MainActivity.DEBUG) Log.d("DEBUG-SaveDeviceData", "Device saving vtag : "+ deviceData.getView().getTag().toString());
        }

    }
    private void restore(){
        restoreInProgress = true;
        for(int i=0; i<size;i++){
            if(MainActivity.DEBUG) Log.d("DEBUG-SaveDeviceData", "Device restoring #: "+ i);

            String name,IP,on,off,type,tag,vTag,status;
            int port,index;

            index = (i)*members;

            name = sharedPreferences.getString(index+"",null);
            IP   = sharedPreferences.getString(++index+"",null);
            port = sharedPreferences.getInt(++index+"",-1);
            on   = sharedPreferences.getString(++index+"",null);
            off  = sharedPreferences.getString(++index+"",null);
            status  = sharedPreferences.getString(++index+"",null);
            tag  = sharedPreferences.getString(++index+"",null);
            type = sharedPreferences.getString(++index+"",null);
            vTag = sharedPreferences.getString(++index+"",null);

            //sample delete code
            if(name==null) continue;

            boolean statusB = false;
            if(status.equals("true")) statusB=true;

            //creates the view button

            View deviceV =  LayoutInflater.from(al.context).inflate(R.layout.devices,null);

            Button bt2n = (Button) deviceV.findViewById(R.id.deviceButtonMain);
            Button bt2On= (Button) deviceV.findViewById(R.id.deviceButtonOn);
            Button bt2Off=(Button) deviceV.findViewById(R.id.deviceButtonOff);

            bt2n.setText(name);

            if(MainActivity.DEBUG) Log.d("DEBUG-SaveDeviceData", "Device restoring vtag : "+vTag);

            bt2n.setTag(vTag.split("###")[0]+separator+vTag.split("###")[1]+separator+"Main");
            bt2On.setTag(vTag.split("###")[0]+separator+vTag.split("###")[1]+separator+"On");
            bt2Off.setTag(vTag.split("###")[0]+separator+vTag.split("###")[1]+separator+"Off");

            bt2n.setOnLongClickListener(al.onLongClickListener);
            bt2n.setOnClickListener(al.onClickListener);
            bt2On.setOnClickListener(al.onClickListener);
            bt2Off.setOnClickListener(al.onClickListener);


            DeviceData deviceData = new DeviceData(name,IP,port,on,off,deviceV, DeviceData.Type.BUTTON);
            deviceData.setStatus(statusB);
            //adds the device to the room view
            addNewDevice(deviceData);

            if(MainActivity.DEBUG) Log.d("DEBUG-SaveDeviceData", "Device restored | "+ deviceData);
        }
        restoreInProgress = false;
    }
    }

}
