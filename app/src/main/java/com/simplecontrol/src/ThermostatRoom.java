package com.simplecontrol.src;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;



/**
 * Created by Mrinmoy Mondal on 8/16/2017.
 * The thermostatRoom is the houses main AC/HEAT system.
 * This class holds the thermostatRoom layout and the main should interact with it
 * to access the top view. It set the buttons pressed to the ActionListener class.
 * There should only be one top view it. There area setters in plcae to change the temperature
 */

public class ThermostatRoom extends RoomData{
    //creates the a instance of the layout
    private View thermostatView;
    //activity from main
    private Activity _activity;

    //variables to control the thermostatRoom
    private TextView _temperature;
    private TextView _setTemperature;


    Switch _fanSwitch;

    Button _acHeatOFF;
    Button _addTemp;
    Button _subTemp;

    ImageButton _addRoom;

    /// state of the current settings
    private int acStatus;

    ThermostatDevice thermostatDevice;

    //initializes everything from main activity and all the widgets
     ThermostatRoom(String roomName, AppLayout appLayout )  {
         super(roomName,appLayout);



     }

    @Override
    View createView() {

        thermostatView = LayoutInflater.from(getContext()).inflate(R.layout.top_layout, null);
        thermostatView.setTag(getRoomName());



        ImageView image = (ImageView)getView().findViewById(R.id.bg);
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        Bitmap newbitmap = ImageHelper.getRoundedCornerBitmap(bitmap,30);

        image.setImageBitmap(newbitmap);



        return thermostatView;

    }


    @Override
    void addDevice(DeviceData deviceData) {
         this.thermostatDevice = (ThermostatDevice) deviceData;
         thermostatDevice.createDataBaseString();

    }

    @Override
     DeviceData getDevice(String deviceName){
         return this.thermostatDevice;
     }

    @Override
    View getView() {
        return this.thermostatView;
    }

    @Override
    void updateDevice(String deviceName, DeviceData newDeviceData) {
        thermostatDevice = (ThermostatDevice) newDeviceData;
    }
}

/*

         Context context =appLayout.context;
         View.OnClickListener onClickListener= appLayout.onClickListener;
         View.OnLongClickListener onLongClickListener = appLayout.onLongClickListener;
         appLayout.thermostatRoom = this;

        thermostatView = LayoutInflater.from(context).inflate(R.layout.top_layout,null);

        _acHeatOFF = (Button)thermostatView.findViewById(R.id.toggleHeatAC);
        _addTemp   = (Button)thermostatView.findViewById(R.id.temp_increment);
        _subTemp   = (Button)thermostatView.findViewById(R.id.temp_decrement);

        _addRoom   = (ImageButton)thermostatView.findViewById(R.id.addRoom);

        _fanSwitch = (Switch)thermostatView.findViewById(R.id.fan);

        _setTemperature     = (TextView)thermostatView.findViewById(R.id.temp);
        _temperature  = (TextView)thermostatView.findViewById(R.id.current_temperature);


        //sets the buttons to on listeners
        _acHeatOFF.setOnClickListener(onClickListener);
        _addTemp.setOnClickListener(onClickListener);
        _subTemp.setOnClickListener(onClickListener);

        _addRoom.setOnClickListener(onClickListener);
        _fanSwitch.setOnClickListener(onClickListener);

        _addTemp.setOnLongClickListener(onLongClickListener);
        _subTemp.setOnLongClickListener(onLongClickListener);


    }

    //creates the setters and getters for textfield
     void setTemperature (int temp){
        _setTemperature.setText(temp+"");

    }
    protected void setCurrentTemperature(int temp){
        _temperature.setText(temp+"");
    }

    public int getTemperature() {
        return Integer.parseInt(_temperature.getText().toString());
    }

    int getSetTemperature() {
        return Integer.parseInt(_setTemperature.getText().toString());
    }

     void setAcHeatOffText (String x){
        if(x.length()>4){
            Log.e("setString", "acHeatToggle Excceds max char");
            x="NA";
        }
        _acHeatOFF.setText(x);
    }
     int getAcStatus(){
        return acStatus;
    }

     void setAcStatus(int acStatus) {
        if(acStatus>2 || acStatus<0) {
            if( MainActivity.DEBUG)
               Log.e("DEBUG", "ac status error");
            return;
        }
        this.acStatus = acStatus;
    }

    //returns the view object
    protected View getView(){
        return thermostatView;
    }


}
*/