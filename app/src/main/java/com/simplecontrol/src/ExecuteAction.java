package com.simplecontrol.src;

import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by Mrinmoy Mondal on 8/17/2017.
 * ExecuteAction holds all the commands for when a button is pressed
 * UserInteraction with the app is processed here
 *
 * LOG - DEBUG-ExecuteAction
 */

public class ExecuteAction implements View.OnClickListener, View.OnLongClickListener{
    //shared app layout to perform actions
    private AppLayout al;
    //Holds house object
    private House house;
   // final static String tempText;

    SavedData savedData;
    public ExecuteAction(AppLayout al, House house, SavedData savedData) {
        this.al = al;
        this.house = house;
        this.savedData = savedData;

        al.onClickListener = this;
        al.onLongClickListener = this;
    }

    //performs all the actions
    //add all the actions here relation to a tag
    //must have a instance of device data\
    //Each device has the tag "<Room Name>###<Device Name>###<function>"
    //function can be anything the devica can perform such as on, off, or timer
    protected  void execute (String s){
        //gets the device from house
        DeviceData device = house.getDevice(s);
        String roomName   = s.split("###")[0];

        if(device==null) {
            if(MainActivity.DEBUG) Log.e("DEBUG-ExecuteAction", "Searched for device "+ s + "->" + "Failed");
            return;
        }
        switch (device.getName()){
            //ac heat off button
            case "acHeatOFF" :
                if(al.topView.getAcStatus()==0) {
                    al.topView.setAcHeatOffText("HEAT");
                    al.topView.setAcStatus(1);
                    break;
                }if(al.topView.getAcStatus()==1) {
                    al.topView.setAcHeatOffText("AC");
                    al.topView.setAcStatus(2);
                    break;
                }if(al.topView.getAcStatus()==2) {
                    al.topView.setAcHeatOffText("OFF");
                    al.topView.setAcStatus(0);

                    break;
                 }

            case "addTemp"   : al.topView.setTemperature(al.topView.getSetTemperature()+1);
                break;
            case "subTemp"   : al.topView.setTemperature(al.topView.getSetTemperature()-1);
                break;
            case "fanSwitch" :
                if(device.status())
                    device.Off();
                else device.On();
                device.toggle();
                break;

            case "addRoom"   :
                //creates the popup dialog
                View popup = LayoutInflater.from(al.context).inflate(R.layout.get_room_info,null);

                AlertDialog.Builder abl = new AlertDialog.Builder(al.context);

                abl.setView(popup);
                al.alertDialog= abl.create();
                al.alertDialog.show();

                //sets the edit text
                al.editText = new EditText[1];
                al.editText[0]= (EditText) popup.findViewById(R.id.roomName);
                //sets the onClick listener so room can be added after pressed
                Button btn = (Button) popup.findViewById(R.id.addButtonClicked);
                btn.setTag("MainRoom###addRoomPressed");
                btn.setOnClickListener(al.onClickListener);
                //creates a new device so it can be pressed and its action redirected to this class
                DeviceData db = new DeviceData("addRoomPressed",btn, DeviceData.Type.BUTTON);
                house.getRoom("MainRoom").addDevice(db);

                break;

            //adds the new room after the user types it in and hits add
            case "addRoomPressed":

                String name = al.editText[0].getText().toString();
                //adds the room from the alert dialog box
                al.alertDialog.cancel();
                if(name.equals("")){
                    if (MainActivity.DEBUG ) Log.e("DEBUG-ExecuteAction", "Received call for adding room with"+ al.editText[0].getText().toString());
                    Toast.makeText(al.context,"Please Enter a valid Room name",Toast.LENGTH_LONG).show();

                }else {
                    //adds the room through house
                    house.addRoom(name);


                    if (MainActivity.DEBUG) Log.d("DEBUG-ExecuteAction", "Received call for adding room with name: " + name);
                }

                break;
            //prompt the user for device information
            case "addDevice":
                //creates the view for the device info
                View popup2 = LayoutInflater.from(al.context).inflate(R.layout.device_data_info,null);
                AlertDialog.Builder abl2 = new AlertDialog.Builder(al.context);

                abl2.setView(popup2);
                al.alertDialog = abl2.create();
                al.alertDialog.show();

                //adds the EditText to the ap layout so the data can be retrieved later
                al.editText = new EditText[5];
                al.editText[0]= (EditText) popup2.findViewById(R.id.deviceName);
                al.editText[1]= (EditText) popup2.findViewById(R.id.deviceIP);
                al.editText[2]= (EditText) popup2.findViewById(R.id.devicePort);
                al.editText[3]= (EditText) popup2.findViewById(R.id.onCommand);
                al.editText[4]= (EditText) popup2.findViewById(R.id.offCommand);
                //sets the on action listener
                Button btn2 = (Button) popup2.findViewById(R.id.addDeviceClicked);
                btn2.setTag(s.split("###")[0]+"###addDevicePressed");
                btn2.setOnClickListener(al.onClickListener);
                //adds the device to the room but does not add it to the view
                DeviceData db2 = new DeviceData("addDevicePressed",btn2, DeviceData.Type.BUTTON);
                house.getRoom(s.split("###")[0]).addDevice(db2);

                break;
            //adds a new device to a room
            case "addDevicePressed":
                if(MainActivity.DEBUG) Log.d("DEBUG-ExcecuteDevice", "addDevicePressed");
                int port ;

                try{
                    port = Integer.parseInt(al.editText[2].getText().toString());
                    if(al.editText[0].getText().toString().length()>20)
                        throw new Exception("Exceeded max number of Characters");

                    int y=  Integer.parseInt(al.editText[1].getText().toString().split("\\.")[0])+
                            Integer.parseInt(al.editText[1].getText().toString().split("\\.")[1])+
                            Integer.parseInt(al.editText[1].getText().toString().split("\\.")[2]);
                }catch(Exception e){
                    Toast.makeText(al.context,"Invalid Info-"+e.getMessage(),Toast.LENGTH_LONG).show();
                    if(MainActivity.DEBUG) Log.e("DEBUG-ExcecuteDevice", "addDevicePressed - In valid room data| "+e.getMessage());
                    break;
                }

                al.alertDialog.cancel();
                String newdname=  al.editText[0].getText().toString();
                String IP = al.editText[1].getText().toString();
                String ON = al.editText[3].getText().toString();
                String OFF = al.editText[4].getText().toString();


                RoomData room = house.getRoom(s.split("###")[0]);
                View deviceV =  LayoutInflater.from(al.context).inflate(R.layout.devices,null);

                Button bt2n = (Button) deviceV.findViewById(R.id.deviceButtonMain);
                Button bt2On= (Button) deviceV.findViewById(R.id.deviceButtonOn);
                Button bt2Off=(Button) deviceV.findViewById(R.id.deviceButtonOff);

                bt2n.setText(newdname);
                bt2n.setTag(roomName+"###"+newdname+"###main");
                bt2On.setTag(roomName+"###"+newdname+"###On");
                bt2Off.setTag(roomName+"###"+newdname+"###Off");

                bt2n.setOnLongClickListener(al.onLongClickListener);
                bt2n.setOnClickListener(al.onClickListener);
                bt2On.setOnClickListener(al.onClickListener);
                bt2Off.setOnClickListener(al.onClickListener);

                room.addNewDevice(new DeviceData(newdname, IP,port, ON,OFF, deviceV, DeviceData.Type.BUTTON));
                if (MainActivity.DEBUG) Log.d("DEBUG-ExecuteAction", "Received call for adding device to room " + room.getRoomName());

                break;
            //toggles the device on, off, timer
            default:
                if(MainActivity.DEBUG) Log.d("DEBUG-ExcecuteDevice", "performing funtion - "+ s.split("###")[2] );
                String command = s.split("###")[2];

                switch (command){
                    case "On":
                        device.On();
                        Toast.makeText(al.context,"Tuning on "+device.getName()+" in "+ roomName,Toast.LENGTH_LONG).show();
                        break;
                    case "Off":
                        device.Off();
                        Toast.makeText(al.context,"Tuning off "+device.getName()+" in "+ roomName,Toast.LENGTH_LONG).show();
                        break;
                    case "timer":


                }

        }

    }

    public void onLong(String s){
        DeviceData device = house.getDevice(s);

        if(device==null) {
            if(MainActivity.DEBUG) Log.e("DEBUG-ExecuteAction", "Searched for device "+ s + "->" + "Failed");
            return;
        }

        //allows the device data to be modified or removed
        //creates the view for the device info
        View popup2 = LayoutInflater.from(al.context).inflate(R.layout.modify_device,null);
        AlertDialog.Builder abl2 = new AlertDialog.Builder(al.context);

        abl2.setView(popup2);
        al.alertDialog = abl2.create();
        al.alertDialog.show();

        //adds the EditText to the ap layout so the data can be retrieved later
        al.editText = new EditText[5];
        al.editText[0]= (EditText) popup2.findViewById(R.id.deviceName);
        al.editText[0].setText(device.getName());
        al.editText[1]= (EditText) popup2.findViewById(R.id.deviceIP);
        al.editText[1].setText(device.getIP());
        al.editText[2]= (EditText) popup2.findViewById(R.id.devicePort);
        al.editText[2].setText(device.getPort()+"");
        al.editText[3]= (EditText) popup2.findViewById(R.id.onCommand);
        al.editText[3].setText(device.getOn());
        al.editText[4]= (EditText) popup2.findViewById(R.id.offCommand);
        al.editText[4].setText(device.getOff());
        //sets the on action listener
        Button btn2 = (Button) popup2.findViewById(R.id.addDeviceClicked);
        btn2.setTag(s.split("###")[0]+"###addDevicePressed");
        btn2.setOnClickListener(al.onClickListener);
        //adds the device to the room but does not add it to the view
        DeviceData db2 = new DeviceData("addDevicePressed",btn2, DeviceData.Type.BUTTON);
        house.getRoom(s.split("###")[0]).addDevice(db2);


       // house.getRoom(s.split("###")[0]).removeDevice(device.getName());
    }

    ////////////////////////////////////////////////////
    //calls the actionPerform class after a button click
    @Override
    public void onClick(View v) {
        if(MainActivity.DEBUG) {
            Log.d("DEBUG-MAIN","Short Button Pressed: "+v.getTag());
        }

        // redirects to execute action if its a predefined action
        execute (v.getTag().toString());




    }


    @Override
    public boolean onLongClick(View v) {

        if(MainActivity.DEBUG) {
            Log.d("DEBUG","Long Button Pressed: "+v.getTag());
        }


        onLong(v.getTag().toString());
        return true;
    }

}

