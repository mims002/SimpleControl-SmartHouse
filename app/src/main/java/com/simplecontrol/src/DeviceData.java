package com.simplecontrol.src;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mrinmoy Mondal on 8/17/2017.
 * This class holds the on off command for each device and uses an AsyncTask to execute it.
 * It has parameters IP address, port and the String for on or off.
 */


public class DeviceData{
    //holds the type of view it is
    public enum Type{
        SWITCH, BUTTON, TEXTFIELD
    }

    private String IP, on, off,name;

    private int port;
    private boolean status;

    private boolean notUICreated;

    private View view;
    private Type type;

    DeviceData(String name, View view, Type type){
        this.name = name;
        this.type=type;
        this.view = view;
        this.notUICreated = false;
    }
    DeviceData(String name, String IP, int port, String on, String off, View view,Type type) {
        this(name,view,type);

        this.IP = IP;
        this.on = on;
        this.off = off;
        this.port = port;
        this.status= false;
        this.notUICreated = true;
        if(MainActivity.DEBUG) Log.d("DEBUG", "Created - "+toString());

    }
    //deivice on
    void On() {
        new AsyncThread().execute(this.on);
    }
    //device off
    void Off() {
        new AsyncThread().execute(this.off);
    }
    //turns the state to off or on
    void toggle (){
        status = !status;
    }
/////////////////////////////////////////////////////////////
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

    public void setView(View view) {
        this.view = view;
    }
    public boolean isUserCreated(){
        return notUICreated;
    }
    View getView() {
        return view;
    }

    public void setNameTag(String n) {
        view.setTag(n);
    }
    public String getNameTag() {
       return view.getTag().toString();
    }

    //if the device is on or off
    public boolean status() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void setPort(int port) {
        this.port = port;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getIP() {

        return IP;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString(){
        return ("DeviceName: "+ name+" | IP: " + IP +  " | port: " + port + " | command: " + on+"/"+off+"| usercreated: "+this.isUserCreated());
    }

    //send the command to a selected device
    class AsyncThread extends AsyncTask<String,Void,Void> {


        @Override
        protected Void doInBackground(String... params) {
            Socket skt = null;
            DataOutputStream dOut = null;
            DataInputStream dIn = null;
            try {
                if (MainActivity.DEBUG) {
                    Log.d("DEBUG", "DeviceName: " + name + " | IP: " + IP + " | port: " + port + " | command sent: \"" + params[0] + "\"");
                }
                skt = new Socket(IP, port);
                skt.setTcpNoDelay(true);

                dOut = new DataOutputStream(skt.getOutputStream());
                dIn = new DataInputStream(skt.getInputStream());
                TimeUnit.SECONDS.sleep(1);
                dOut.writeBytes(params[0] + "\n");
                // Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                skt.close();
            } catch (IOException | InterruptedException e) {
                if (MainActivity.DEBUG) {
                    if(MainActivity.DEBUG) Log.e("DEBUG", "Controller failed: " + e.getMessage());
                }
            }
            return null;
        }
    }
}

