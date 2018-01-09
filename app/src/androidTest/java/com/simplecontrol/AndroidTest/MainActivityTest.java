package com.simplecontrol.AndroidTest;

import android.os.SystemClock;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.simplecontrol.src.MainActivity;
import com.simplecontrol.src.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.simplecontrol.src.R.id.addDeviceClicked;
import static com.simplecontrol.src.R.id.removeDevice;
import static junit.framework.Assert.assertNotNull;

/**
 * This class is used for testing the app using espresso
 * this app was tested on Samsung Galaxy S7 edge
 * Created by Mrinmoy Mondal on 9/2/2017.
 *
 */
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mActivity;

    private String roomName001 = "My Room";
    private String roomName002 = "My Room 002";
    private String roomName003 = "My Room 003";
    private String roomName004 = "My Room 004";
    private String roomName005 = "My Room 005";
    private String roomName006 = "My Room 006";


    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }
    @Test
    public void TestLaunch001(){
        View view = mActivity.findViewById(R.id.mainLayout);
        assertNotNull(view);
    }

    @Test
    public void TestNewRoom001(){

        Espresso.onView(withId(R.id.addRoom)).perform(click());
        Espresso.onView(withId(R.id.roomName)).perform(typeText(roomName001));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.addButtonClicked)).perform(click());

       Espresso.onView(withText(roomName001)).check(matches(isDisplayed()));
    }

    @Test
    public void TestNewRoom002(){
        //adds the room
        Espresso.onView(withId(R.id.addRoom)).perform(click());
        Espresso.onView(withId(R.id.roomName)).perform(typeText(roomName002));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.addButtonClicked)).perform(click());




    }

    @Test
    public void TestAddDevice001(){
        Espresso.onView(withId(R.id.addRoom)).perform(click());
        Espresso.onView(withId(R.id.roomName)).perform(typeText(roomName001));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.addButtonClicked)).perform(click());

        Espresso.onView(withText(roomName001)).check(matches(isDisplayed()));

        //adds a button
        Espresso.onView(withId(R.id.addDevice)).perform(click());

        Espresso.onView(withId(R.id.deviceName)).perform(typeText("Lights"));
        Espresso.onView(withId(R.id.deviceIP)).perform(typeText("192.168.29.2"));
        Espresso.onView(withId(R.id.devicePort)).perform(typeText("2222"));
        Espresso.onView(withId(R.id.onCommand)).perform(typeText("O"));
        Espresso.onView(withId(R.id.offCommand)).perform(typeText("F"));
        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(addDeviceClicked)).perform(longClick());
        //Espresso.onView(withText("Fan")).perform(longClick());

        SystemClock.sleep(1000);

    }


    @Test
    public void TestAddDevice005(){
        Espresso.onView(withId(R.id.addRoom)).perform(click());
        Espresso.onView(withId(R.id.roomName)).perform(typeText(roomName001));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.addButtonClicked)).perform(click());

        Espresso.onView(withText(roomName001)).check(matches(isDisplayed()));

        //adds a button
        Espresso.onView(withId(R.id.addDevice)).perform(click());

        Espresso.onView(withId(R.id.deviceName)).perform(typeText("Fan"));
        Espresso.onView(withId(R.id.deviceIP)).perform(typeText("192.168.29.11"));
        Espresso.onView(withId(R.id.devicePort)).perform(typeText("2222"));
        Espresso.onView(withId(R.id.onCommand)).perform(typeText("O"));
        Espresso.onView(withId(R.id.offCommand)).perform(typeText("F"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(addDeviceClicked)).perform(click());



        //adds a button
        Espresso.onView(withId(R.id.addDevice)).perform(click());

        Espresso.onView(withId(R.id.deviceName)).perform(typeText("OutSide Lights"));
        Espresso.onView(withId(R.id.deviceIP)).perform(typeText("192.168.29.11"));
        Espresso.onView(withId(R.id.devicePort)).perform(typeText("2222"));
        Espresso.onView(withId(R.id.onCommand)).perform(typeText("t"));
        Espresso.onView(withId(R.id.offCommand)).perform(typeText("c"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(addDeviceClicked)).perform(click());
        //adds a button
        Espresso.onView(withId(R.id.addDevice)).perform(click());

        Espresso.onView(withId(R.id.deviceName)).perform(typeText("OutLet 1 "));
        Espresso.onView(withId(R.id.deviceIP)).perform(typeText("192.168.29.11"));
        Espresso.onView(withId(R.id.devicePort)).perform(typeText("2222"));
        Espresso.onView(withId(R.id.onCommand)).perform(typeText("t"));
        Espresso.onView(withId(R.id.offCommand)).perform(typeText("c"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(addDeviceClicked)).perform(click());


        Espresso.onView(withId(addDeviceClicked)).perform(click());



    }

    @Test
    public void TestRemoveDevice001(){
        Espresso.onView(withId(R.id.addRoom)).perform(click());
        Espresso.onView(withId(R.id.roomName)).perform(typeText(roomName001));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.addButtonClicked)).perform(click());

        Espresso.onView(withText(roomName001)).check(matches(isDisplayed()));

        //adds a button
        Espresso.onView(withId(R.id.addDevice)).perform(click());

        Espresso.onView(withId(R.id.deviceName)).perform(typeText("Fan"));
        Espresso.onView(withId(R.id.deviceIP)).perform(typeText("192.168.29.11"));
        Espresso.onView(withId(R.id.devicePort)).perform(typeText("2222"));
        Espresso.onView(withId(R.id.onCommand)).perform(typeText("O"));
        Espresso.onView(withId(R.id.offCommand)).perform(typeText("F"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(addDeviceClicked)).perform(click());


        //remove the button
        Espresso.onView(withText("Fan")).perform(longClick());
        Espresso.onView(withId(removeDevice)).perform(longClick());

        SystemClock.sleep(1000);

    }

    @Test
    public void addDevice(){
        //adds a button
        Espresso.onView(withId(R.id.addDevice)).perform(click());

        Espresso.onView(withId(R.id.deviceName)).perform(typeText("Fan"));
        Espresso.onView(withId(R.id.deviceIP)).perform(typeText("192.168.29.11"));
        Espresso.onView(withId(R.id.devicePort)).perform(typeText("2222"));
        Espresso.onView(withId(R.id.onCommand)).perform(typeText("O"));
        Espresso.onView(withId(R.id.offCommand)).perform(typeText("F"));
        Espresso.closeSoftKeyboard();



    }

    @After
    public void tearDown() throws Exception {
        //mActivity = null;
    }

}