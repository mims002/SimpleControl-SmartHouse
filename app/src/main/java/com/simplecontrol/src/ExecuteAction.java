package com.simplecontrol.src;

import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import static com.simplecontrol.src.R.id.deviceName;


/**
 * Created by Mrinmoy Mondal on 8/17/2017.
 * ExecuteAction holds all the commands for when a button is pressed
 * UserInteraction with the appLayout is processed here
 *
 * LOG - DEBUG-ExecuteAction
 */

public class ExecuteAction implements View.OnClickListener, View.OnLongClickListener{
    private final static String DEBUG_TAG = "DEBUG-ExecuteAction";
    //shared appLayout layout to perform actions
    private AppLayout al;
    //Holds house object
    private House house;
   //holds the alert dialog
    private AlertDialog alertDialog;
    private AlertDialog alertDialog2;
    
    //edit texts
    EditText[]editTexts;

    SavedData savedData;
    public ExecuteAction(AppLayout al, House house, SavedData savedData) {
        this.al = al;
        this.house = house;
        this.savedData = savedData;
        //creates enough editTexts for all device info
        editTexts = new EditText[20];

        al.onClickListener = this;
        al.onLongClickListener = this;
    }

    //performs all the actions
    //add all the actions here relation to a tag
    //must have a instance of device data\
    //Each device has the tag "<Room Name>###<Device Name>###<function>"
    //function can be anything the devica can perform such as on, off, or timer
    protected  void execute (String s){


        //performs special cases that do not deal with rooms or simple_switch_view
        switch (s){
            case "addRoom"   :
                //creates the popup dialog
                View popup = LayoutInflater.from(al.context).inflate(R.layout.get_room_info,null);

                AlertDialog.Builder abl = new AlertDialog.Builder(al.context);

                abl.setView(popup);
                alertDialog= abl.create();
                alertDialog.show();

                //sets the edit text
                this.editTexts = new EditText[1];
                this.editTexts[0]= (EditText) popup.findViewById(R.id.roomName);
                //sets the onClick listener so room can be added after pressed
                Button btn = (Button) popup.findViewById(R.id.addButtonClicked);
                btn.setTag("addRoomPressed");
                btn.setOnClickListener(al.onClickListener);
                return;
            //adds the new room after the user types it in and hits add
            case "addRoomPressed":

                String name = this.editTexts[0].getText().toString();
                //adds the room from the alert dialog box
                alertDialog.cancel();
                if(name.equals("")){
                    if (MainActivity.DEBUG ) Log.e("DEBUG-ExecuteAction", "Received call for adding room with"+ this.editTexts[0].getText().toString());
                    Toast.makeText(al.context,"Please Enter a valid Room name",Toast.LENGTH_LONG).show();

                }else {
                    //adds the room through house
                    house.addRoom(name);
                    printDebugMsg("Received call for adding room with name: " + name);
                }
               return;
        }

        String roomName   = s.split("###")[0];
        String deviceName = s.split("###")[1];
        RoomData room     = house.getRoom(roomName);


        printDebugMsg(al.db.dataBaseToString(roomName));


        if(room==null) {
            printDebugMsg("Searched for device "+ s + "->" + "Failed");
            return;
        }
        switch (deviceName){
           /* //ac heat off button
            case "acHeatOFF" :
                if(al.thermostatRoom.getAcStatus()==0) {
                    al.thermostatRoom.setAcHeatOffText("HEAT");
                    al.thermostatRoom.setAcStatus(1);
                    break;
                }if(al.thermostatRoom.getAcStatus()==1) {
                    al.thermostatRoom.setAcHeatOffText("AC");
                    al.thermostatRoom.setAcStatus(2);
                    break;
                }if(al.thermostatRoom.getAcStatus()==2) {
                    al.thermostatRoom.setAcHeatOffText("OFF");
                    al.thermostatRoom.setAcStatus(0);

                    break;
                 }

            case "addTemp"   : al.thermostatRoom.setTemperature(al.thermostatRoom.getSetTemperature()+1);
                break;
            case "subTemp"   : al.thermostatRoom.setTemperature(al.thermostatRoom.getSetTemperature()-1);
                break;
            case "fanSwitch" :
                break;*/
            //prompt the user for device information
            case "addDevice":
                //creates the view for the device info
                View popup2 = LayoutInflater.from(al.context).inflate(R.layout.simple_switch_info,null);
                AlertDialog.Builder abl2 = new AlertDialog.Builder(al.context);

                abl2.setView(popup2);
                alertDialog = abl2.create();
                alertDialog.show();

                //adds the EditText to the ap layout so the data can be retrieved later
                this.editTexts = new EditText[5];
                this.editTexts[0]= (EditText) popup2.findViewById(R.id.deviceName);
                this.editTexts[1]= (EditText) popup2.findViewById(R.id.deviceIP);
                this.editTexts[2]= (EditText) popup2.findViewById(R.id.devicePort);
                this.editTexts[3]= (EditText) popup2.findViewById(R.id.onCommand);
                this.editTexts[4]= (EditText) popup2.findViewById(R.id.offCommand);
                //sets the on action listener
                Button btn2 = (Button) popup2.findViewById(R.id.addDeviceClicked);
                btn2.setTag(roomName+"###addDevicePressed");
                btn2.setOnClickListener(al.onClickListener);


                break;
            //adds a new device to a room
            case "addDevicePressed":

                printDebugMsg("addDevicePressed");
                room.addDevice(createDevice(DeviceData.Type.SIMPLE_SWITCH,roomName));
                break;
            //toggles the device on, off, timer, update, delete,
            default:
                printDebugMsg("performing funtion - "+ s.split("###")[2] + " on " +s);
                String command = s.split("###")[2];
                DeviceData device = house.getDevice(s);

                switch (command){
                    case "function1":
                        device.function1();
                        Toast.makeText(al.context,"Performing action "+device.getFunction(1)+" "+device.getName()+" in "+ roomName,Toast.LENGTH_LONG).show();
                        break;
                    case "function2":
                        device.function2();
                        Toast.makeText(al.context,"Performing action "+device.getFunction(2)+" "+device.getName()+" in "+ roomName,Toast.LENGTH_LONG).show();
                        break;
                    case "function3":
                        device.function3();
                        Toast.makeText(al.context,"Performing action "+device.getFunction(3)+" "+device.getName()+" in "+ roomName,Toast.LENGTH_LONG).show();
                        break;
                    case "function4":
                        device.function4();
                        Toast.makeText(al.context,"Performing action "+device.getFunction(4)+" "+device.getName()+" in "+ roomName,Toast.LENGTH_LONG).show();
                        break;
                    case "deletePrompt":
                        View deleteConfirmation = LayoutInflater.from(al.context).inflate(R.layout.remove,null);
                        Button confirm = (Button) deleteConfirmation.findViewById(R.id.btn_yes);
                        Button cancel = (Button) deleteConfirmation.findViewById(R.id.btn_no);

                        confirm.setTag(device.getRootTag()+"deleteConfirmed");
                        cancel.setTag(device.getRootTag()+"cancel");

                        confirm.setOnClickListener(this);
                        cancel.setOnClickListener(this);

                        AlertDialog.Builder adl = new AlertDialog.Builder(al.context);
                        adl.setView(deleteConfirmation);
                        alertDialog2 = adl.create();
                        alertDialog2.show();
                        break;
                    case "deleteConfirmed":

                        room.removeDevice(deviceName);
                        printDebugMsg("Removing Device from Room: "+ room.getRoomName() +" -> Device: "+ deviceName);

                        if(alertDialog2!=null)alertDialog2.cancel();
                        if(alertDialog!=null) alertDialog.cancel();

                        break;

                    case "update":
                        room.updateDevice(deviceName,createDevice(device.getType(),roomName));
                        alertDialog.cancel();
                        break;
                    case "settings":
                        onLong(s);
                        break;
                    case "cancel":
                        if(alertDialog2!=null)alertDialog2.cancel();




                }



        }

    }

    /**
     * Creates a Device based on edit texts
     * @param type Device Type
     * @return DeviceData
     */
    public DeviceData createDevice(DeviceData.Type type, String roomName){
        switch (type){
            case SIMPLE_SWITCH:
                int port ;

                try{
                    port = Integer.parseInt(this.editTexts[2].getText().toString());
                    if(this.editTexts[0].getText().toString().length()>20)
                        throw new Exception("Exceeded max number of Characters");

                    int y=  Integer.parseInt(this.editTexts[1].getText().toString().split("\\.")[0])+
                            Integer.parseInt(this.editTexts[1].getText().toString().split("\\.")[1])+
                            Integer.parseInt(this.editTexts[1].getText().toString().split("\\.")[2]);
                }catch(Exception e){
                    Toast.makeText(al.context,"Invalid Info-"+e.getMessage(),Toast.LENGTH_LONG).show();
                    if(MainActivity.DEBUG) Log.e("DEBUG-ExcecuteDevice", "addDevicePressed - In valid room data| "+e.getMessage());
                    break;
                }

                alertDialog.cancel();
                String newdname=  this.editTexts[0].getText().toString();
                String IP = this.editTexts[1].getText().toString();
                String ON = this.editTexts[3].getText().toString();
                String OFF = this.editTexts[4].getText().toString();

                printDebugMsg("Received call for adding device to room " + roomName);

                return new SimpleSwitch(newdname,roomName, IP,ON, OFF, port, this, this);
        }
        return null;
    }

    public void onLong(String s){
        DeviceData device = house.getDevice(s);

        if(device==null) {
            if(MainActivity.DEBUG) Log.e(DEBUG_TAG, "Searched for device "+ s + "->" + "Failed");
            return;
        }


        switch (device.getType()){
            case SIMPLE_SWITCH:
                SimpleSwitch simpleDevice = (SimpleSwitch) device;
                //allows the device data to be modified or removed
                //creates the view for the device info
                View popup2 = LayoutInflater.from(al.context).inflate(R.layout.simple_switch_modify,null);
                AlertDialog.Builder abl2 = new AlertDialog.Builder(al.context);

                abl2.setView(popup2);
                alertDialog = abl2.create();
                alertDialog.show();

                //adds the EditText to the ap layout so the data can be retrieved later
                this.editTexts = new EditText[5];
                this.editTexts[0]= (EditText) popup2.findViewById(deviceName);
                this.editTexts[0].setText(simpleDevice.getName());
                this.editTexts[1]= (EditText) popup2.findViewById(R.id.deviceIP);
                this.editTexts[1].setText(simpleDevice.getIP());
                this.editTexts[2]= (EditText) popup2.findViewById(R.id.devicePort);
                this.editTexts[2].setText(simpleDevice.getPort()+"");
                this.editTexts[3]= (EditText) popup2.findViewById(R.id.onCommand);
                this.editTexts[3].setText(simpleDevice.getOn());
                this.editTexts[4]= (EditText) popup2.findViewById(R.id.offCommand);
                this.editTexts[4].setText(simpleDevice.getOff());
                //sets the on action listener

                //update button
                Button btn2 = (Button) popup2.findViewById(R.id.addDeviceClicked);
                btn2.setTag(device.getRootTag()+"update");
                btn2.setOnClickListener(al.onClickListener);
                //delete button
                ImageButton btn3 = (ImageButton) popup2.findViewById(R.id.removeDevice);
                btn3.setTag(device.getRootTag()+"deletePrompt");
                btn3.setOnClickListener(al.onClickListener);
                break;
        }


    }

    //prints a debug message
    void printDebugMsg(String msg){
        if(MainActivity.DEBUG)
            Log.d("DEBUG-"+this.getClass().getSimpleName(),msg);
    }


    ////////////////////////////////////////////////////
    //calls the actionPerform class after a button click
    @Override
    public void onClick(View v) {
        if(MainActivity.DEBUG) {
            Log.d(DEBUG_TAG,"Short Button Pressed: "+v.getTag());
        }

        // redirects to execute action if its a predefined action
        execute (v.getTag().toString());




    }

    @Override
    public boolean onLongClick(View v) {

        if(MainActivity.DEBUG) {
            Log.d(DEBUG_TAG,"Long Button Pressed: "+v.getTag());
        }


        onLong(v.getTag().toString());
        return true;
    }

}

