package com.simplecontrol.src;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;



/**
 * Created by Mrinmoy Mondal on 8/16/2017.
 * The topView is the houses main AC/HEAT system.
 * This class holds the topView layout and the main should interact with it
 * to access the top view. It set the buttons pressed to the ActionListener class.
 * There should only be one top view it. There area setters in plcae to change the temperature
 */

public class TopView {
    //creates the a instance of the layout
    private View _TopView;
    //activity from main
    private Activity _activity;

    //variables to control the topView
    private TextView _temperature;
    private TextView _setTemperature;


    Switch _fanSwitch;

    Button _acHeatOFF;
    Button _addTemp;
    Button _subTemp;

    ImageButton _addRoom;

    /// state of the current settings
    private int acStatus;


    //initializes everything from main activity and all the widgets
     TopView(AppLayout appLayout ) {
         Context context =appLayout.context;
         View.OnClickListener onClickListener= appLayout.onClickListener;
         View.OnLongClickListener onLongClickListener = appLayout.onLongClickListener;
         appLayout.topView = this;

        _TopView = LayoutInflater.from(context).inflate(R.layout.top_layout,null);

        _acHeatOFF = (Button)_TopView.findViewById(R.id.toggleHeatAC);
        _addTemp   = (Button)_TopView.findViewById(R.id.temp_increment);
        _subTemp   = (Button)_TopView.findViewById(R.id.temp_decrement);

        _addRoom   = (ImageButton)_TopView.findViewById(R.id.addRoom);

        _fanSwitch = (Switch)_TopView.findViewById(R.id.fan);

        _setTemperature     = (TextView)_TopView.findViewById(R.id.temp);
        _temperature  = (TextView)_TopView.findViewById(R.id.current_temperature);


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
        return _TopView;
    }


}
