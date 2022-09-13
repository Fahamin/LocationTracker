package com.tbl.locationtracker;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    public static final long INTERVAL = 5000;//variable for execute services every 1 minute
    private Handler mHandler = new Handler(); // run on another Thread to avoid crash
    private Timer mTimer = null; // timer handling
    private final IBinder binder = new LocalBinder();
    private ServiceCallbacks serviceCallbacks;

    public Location currentLocation;
    private static final String CHANNEL_ID = "channel_01";

    private MyService myService;
    private boolean bound = false;


    // Class used for the client Binder.
    public class LocalBinder extends Binder {
        public MyService getService() {
            // Return this instance of MyService so clients can call public methods
            return MyService.this;
        }
    }

    public void setCallbacks(ServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {

        // cancel if service is  already existed
        if (mTimer != null)
            mTimer.cancel();
        else
            mTimer = new Timer(); // recreate new timer
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, INTERVAL);// schedule task

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "In Destroy", Toast.LENGTH_SHORT).show();//display toast when method called
        //  mTimer.cancel();//cancel the timer
    }

    //inner class of TimeDisplayTimerTask
    private class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    if(serviceCallbacks != null) {
                        serviceCallbacks.getLocation();
                    }
                }
            });
        }
    }

}

