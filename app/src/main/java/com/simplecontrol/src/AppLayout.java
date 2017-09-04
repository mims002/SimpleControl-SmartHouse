package com.simplecontrol.src;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by Mrinmoy Mondal on 8/17/2017.
 * This class hold the appLayout that mainActivity will share across
 * other classes that need to access and modify the layout.
 */

public class AppLayout {

    protected Context context;
    //House thermostat object
     TopView topView;
    //activity
     Activity mainActivity;
    //on Click Listeners
     View.OnClickListener onClickListener;
    //long on click listener
    View.OnLongClickListener onLongClickListener;
    //alert dialog
     AlertDialog alertDialog;
     EditText [] editText;






    //views are created dinamically to add or remove features
    private LinearLayout mainView;
    //all actions executor object


    public AppLayout(Context context, LinearLayout mainView, Activity mainActivity ) {
        this.context = context;
        this.mainView = mainView;
        this.mainActivity = mainActivity;





    }

    protected void addLayout(View view){
        mainView.addView(view);
    }


}
