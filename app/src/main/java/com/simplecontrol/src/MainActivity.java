package com.simplecontrol.src;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;


/**
 * This class holds the main method (onCreate). It tell android what to perform when its first run, paused and closed.
*  All objects that will be shared across other cases are created here.
 * v0.5
 * testing
 **/
public class MainActivity extends AppCompatActivity{
    public static boolean DEBUG= true;

    //hold the app layout object will be shared across classes
    AppLayout al;
    //this object will execute all the commands
    ExecuteAction executeAction;
    //Holds all the House Object
    House house;
    //main layout
    private LinearLayout mainView;
    //thermostat layout
    TopView topView;
    //saved data will be stored in an shared preference
    SavedData savedData;
   ///////////////////////////////////////////////////


    //initializes everything
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainView = (LinearLayout)findViewById(R.id.linearLayout) ;
        al = new AppLayout(this,mainView,this);

        house = new House(al);
        savedData = new SavedData(house,al);

        executeAction = new ExecuteAction(al,house,savedData);
        topView = new TopView(al);



        this.init();
    }
    ///////////////////////////////////////////////////

    ///////////////////////////////////////////////////
    //initializes everything on main thermostat
    public void init(){

        // /adds the top view to mainActivity
        al.addLayout(topView.getView());
        //adds the thermostat as the mainRoom
        house.addRoom("MainRoom",null);
        //restores all the other rooms
        savedData.restore();


        house.getRoom("MainRoom").addDevice(new DeviceData("addTemp",topView._addTemp,DeviceData.Type.BUTTON));
        house.getRoom("MainRoom").addDevice(new DeviceData("subTemp",topView._subTemp, DeviceData.Type.BUTTON));
        house.getRoom("MainRoom").addDevice(new DeviceData("acHeatOFF",topView._acHeatOFF, DeviceData.Type.BUTTON));
        house.getRoom("MainRoom").addDevice(new DeviceData("addRoom",topView._addRoom, DeviceData.Type.BUTTON));

        if(MainActivity.DEBUG) Log.d("DEBUG-Main: "," After main room added-->"+ house.getRoom("MainRoom").toString());

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }




}


