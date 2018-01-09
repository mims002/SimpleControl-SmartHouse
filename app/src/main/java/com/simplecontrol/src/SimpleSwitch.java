package com.simplecontrol.src;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mrinmoy Mondal on 1/6/2018.
 */

public class SimpleSwitch extends DeviceData {

    View.OnLongClickListener onLongClickListener;
    View.OnClickListener onClickListener;
    //static  String DATABASE_COLUMN_NAME = ("(id int PRIMARY KEY NOT NULL, data1 TEXT, NAME TEXT, TYPE TEXT, data2 TEXT, data3 TEXT,data4 TEXT, data5 TEXT, data6 TEXT)");
    //static  String[] DATABASE_COLUMN_NAME_ARRAY = {"id", "data1", "NAME", "TYPE", "data2", "data3","data4", "data5", "data6"};
    private String ip, on, off;
    private int port;

    private View view;
    private boolean deviceStatus;

    static String functions[] = {"on","off","timer"};
    static String input[] = {"Device Name", "Device"};

    private Button mainButton, onButton,offButton,timerButton;

    SimpleSwitch(String name, String roomName, String ip, String on, String off, int port, View.OnClickListener onClickListener, View.OnLongClickListener onLongClickListener) {
        super(name, roomName, Type.SIMPLE_SWITCH);
        this.onClickListener = onClickListener;
        this.onLongClickListener = onLongClickListener;

        this.ip = ip;
        this.on = on;
        this.off = off;
        this.port = port;
        this.deviceStatus = true;
    }



    @Override
    View createView(Context context) {
        View deviceV = LayoutInflater.from(context).inflate(R.layout.simple_switch_view, null);

        Button mainButton = (Button) deviceV.findViewById(R.id.deviceButtonMain);
        Button onButton = (Button) deviceV.findViewById(R.id.deviceButtonOn);
        Button offButton = (Button) deviceV.findViewById(R.id.deviceButtonOff);
        ImageButton timerBtton = (ImageButton) deviceV.findViewById(R.id.deviceButtonTimer);
        ImageButton settings = (ImageButton) deviceV.findViewById(R.id.deviceButtonSettings);





        mainButton.setText(getName());
        mainButton.setTag(getRoomName() + seperator + getName() + seperator + "main");
        onButton.setTag(getRoomName() + seperator + getName() + seperator + "function1");
        offButton.setTag(getRoomName() + seperator + getName() + seperator + "function2");
        timerBtton.setTag(getRoomName() + seperator + getName() + seperator + "function3");
        settings.setTag(getRoomName() + seperator + getName() + seperator + "settings");

        mainButton.setOnLongClickListener(onLongClickListener);
        offButton.setOnClickListener(onClickListener);
        onButton.setOnClickListener(onClickListener);
        timerBtton.setOnClickListener(onClickListener);
        settings.setOnClickListener(onClickListener);

        this.view = deviceV;

        return deviceV;
    }

    @Override
    View getView(){
        return this.view;
    }
    @Override
    boolean hasView(){
        return this.view != null;
    }

    @Override
    ContentValues createDataBaseString() {

        ContentValues cv = new ContentValues();

        cv.put(DATABASE_COLUMN_NAME_ARRAY[1], getRoomName());
        cv.put(DATABASE_COLUMN_NAME_ARRAY[2], getName());
        cv.put(DATABASE_COLUMN_NAME_ARRAY[3], getType().toString());
        cv.put(DATABASE_COLUMN_NAME_ARRAY[4], getIP());
        cv.put(DATABASE_COLUMN_NAME_ARRAY[5], getPort()+"");
        cv.put(DATABASE_COLUMN_NAME_ARRAY[6], getOn());
        cv.put(DATABASE_COLUMN_NAME_ARRAY[7], getOff());

        printDebugMsg("Created Device Data for database: " + cv.toString());
        return cv;
    }

    @Override
    String getFunction(int f){
        if(f<=0 || f>functions.length) return "error";

        return SimpleSwitch.functions[f-1];
    }

    /**
     * Turns the device on
     */
    @Override
    void function1() {
        new AsyncThread().execute(this.on);
    }

    /**
     * Turns the device off
     */
    @Override
    void function2() {
        new AsyncThread().execute(this.off);
    }

    @Override
    void function3() {

    }
    @Override
    void function4() {

    }
    @Override
    void updateDeviceData(DeviceData deviceData){
        SimpleSwitch s = (SimpleSwitch)deviceData;
        setName(s.getName());
        setIP(s.getIP());
        setOff(s.getOff());
        setOn(s.getOn());
        setPort(s.getPort());

        if(!hasView()) return;

       //replaces the view
        ViewGroup parent = (ViewGroup)getView().getParent();

        int index = parent.indexOfChild(getView());
        parent.removeView(getView());
        parent.addView(createView(getView().getContext()));


    }

    //turns the state to off or on
    void toggle() {
        deviceStatus = !deviceStatus;
    }

    //setters and getters for all the fields

    public String getOn() {
        return on;
    }

    public void setOn(String on) {
        this.on = on;
    }

    public String getOff() {
        return off;
    }

    public void setOff(String off) {
        this.off = off;
    }

    public void setIP(String IP) {
        this.ip = IP;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIP() {
        return ip;
    }

    public int getPort() {
        return port;
    }



    @Override
    public String toString() {
        return (super.toString() + " | IP: " + ip + " | port: " + port + " | command: " + on + "/" + off);
    }

    //prints a debug message
    void printDebugMsg(String msg) {

        if (MainActivity.DEBUG) Log.d("DEBUG-" + this.getClass().getSimpleName(), msg);
    }

    ///////////////////////////////////////////////////////////////////////
    //send the command to a selected device
    class AsyncThread extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... params) {
            Socket skt = null;
            DataOutputStream dOut = null;
            DataInputStream dIn = null;
            try {
                printDebugMsg("DeviceName: " + getName() + " | IP: " + getIP() + " | port: " + getPort() + " | command sent: \"" + params[0] + "\"");

                skt = new Socket(getIP(), getPort());
                skt.setTcpNoDelay(true);

                dOut = new DataOutputStream(skt.getOutputStream());
                dIn = new DataInputStream(skt.getInputStream());
                TimeUnit.SECONDS.sleep(1);
                dOut.writeBytes(params[0] + "\n");
                // Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                skt.close();
            } catch (IOException | InterruptedException e) {
                if (MainActivity.DEBUG) {
                    if (MainActivity.DEBUG) Log.e("DEBUG", "Controller failed: " + e.getMessage());
                }
            }
            return null;
        }

    }

}
