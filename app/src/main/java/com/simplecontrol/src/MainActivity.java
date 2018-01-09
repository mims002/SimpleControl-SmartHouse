package com.simplecontrol.src;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import static com.simplecontrol.src.House.HOUSE_VERSION;


/**
 * This class holds the main method (onCreate). It tell android what to perform when its first run, paused and closed.
*  All objects that will be shared across other cases are created here.
 * v0.5
 * 
 **/
public class MainActivity extends AppCompatActivity {
    public static boolean DEBUG= true;


    //hold the appLayout layout object will be shared across classes
    private AppLayout appLayout;
    //this object will execute all the commands
    ExecuteAction executeAction;
    //Holds all the House Object
    House house;
    //main layout
    private LinearLayout mainView;
    //thermostat layout
    ThermostatRoom thermostatRoom;
    //saved data will be stored in an shared preference
    SavedData savedData;
    DBHandler db;
   ///////////////////////////////////////////////////


    //initializes everything
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainView = (LinearLayout)findViewById(R.id.mainLayout) ;
        //adds the Thermostat view
        appLayout = new AppLayout(this,mainView,this);

        house = new House(appLayout);
        savedData = new SavedData(house, appLayout);

        executeAction = new ExecuteAction(appLayout,house,savedData);
        thermostatRoom = new ThermostatRoom("MainRoom",appLayout);

        this.init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.addRoom:
                executeAction.execute("addRoom");

        }

        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.custom_menu,menu);
        return true;
    }

    //initializes everything on main thermostat
    public void init(){
        // /adds the top view to mainActivity
        appLayout.addLayout(thermostatRoom.getView());
        //adds the mainRoom even if it already exits
        house.addRoom("MainRoom", null);
        //restores all the other rooms
        house.onCreate();



        /**
        house.getRoom("MainRoom").addDevice(new DeviceData("addTemp",thermostatRoom._addTemp,DeviceData.Type.BUTTON));
        house.getRoom("MainRoom").addDevice(new DeviceData("subTemp",thermostatRoom._subTemp, DeviceData.Type.BUTTON));
        house.getRoom("MainRoom").addDevice(new DeviceData("acHeatOFF",thermostatRoom._acHeatOFF, DeviceData.Type.BUTTON));
        house.getRoom("MainRoom").addDevice(new DeviceData("addRoom",thermostatRoom._addRoom, DeviceData.Type.BUTTON));

        if(MainActivity.DEBUG) Log.d(DEBUG_TAG," After main room added-->"+ house.getRoom("MainRoom").toString());
        databaseTester();
         **/
    }



    private void databaseTester() {
        db = new DBHandler(appLayout.context,null,null, HOUSE_VERSION);
        //db.reset();


        Log.d("DEBUG-Main: ", db.dataBaseToString());

        ContentValues values = new ContentValues();
        values.put("test1" , "test11");
        values.put("test2" , "test22");
        values.put("test3" , "test33");


       printDebugMsg("\n\n"+db.dataBaseToString("house")+"\n\n");

        printDebugMsg(values.toString());
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    //prints a debug message
    void printDebugMsg(String msg){
        if(MainActivity.DEBUG)
            Log.d("DEBUG-"+this.getClass().getSimpleName(),msg);
    }


}


