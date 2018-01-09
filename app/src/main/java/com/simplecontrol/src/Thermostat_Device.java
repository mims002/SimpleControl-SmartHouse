package com.simplecontrol.src;

import android.content.ContentValues;
import android.content.Context;
import android.view.View;

/**
 * Created by Mrinmoy Mondal on 1/8/2018.
 */

class ThermostatDevice extends DeviceData {

    ThermostatDevice(String name, String roomName, String ip, int port, String up, String down, String fanOn, String fanOff, String heat, String ac, String off){
        super(name,roomName, Type.THERMOSTAT);
    }

    @Override
    boolean hasView() {
        return super.hasView();
    }

    @Override
    View getView() {
        return super.getView();
    }

    @Override
    View createView(Context context) {
        return null;
    }

    @Override
    public void setTag(String n) {
        super.setTag(n);
    }

    @Override
    public String getTag() {
        return super.getTag();
    }

    @Override
    void function1() {

    }

    @Override
    void function2() {

    }

    @Override
    void function3() {

    }

    @Override
    void function4() {

    }

    @Override
    public boolean status() {
        return super.status();
    }

    @Override
    public void setStatus(boolean status) {
        super.setStatus(status);
    }

    @Override
    public Type getType() {
        return super.getType();
    }

    @Override
    public void setType(Type type) {
        super.setType(type);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getRoomName() {
        return super.getRoomName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    void printDebugMsg(String msg) {
        super.printDebugMsg(msg);
    }

    @Override
    ContentValues createDataBaseString() {
        return null;
    }

    @Override
    String getFunction(int f) {
        return null;
    }

    @Override
    String getRootTag() {
        return super.getRootTag();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    void updateDeviceData(DeviceData deviceData) {

    }

    @Override
    public String toString() {
        return super.toString();
    }
}
