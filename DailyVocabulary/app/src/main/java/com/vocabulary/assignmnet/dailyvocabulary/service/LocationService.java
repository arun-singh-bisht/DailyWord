package com.vocabulary.assignmnet.dailyvocabulary.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.BatteryManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vocabulary.assignmnet.dailyvocabulary.Utils.SPUtils;

import java.util.Calendar;


/**
 * It's the main location service that runs in background and send location update to the server in a little duration.
 * Duration has been stored in shared preferences that can be changed anytime. To apply changes, you need to restart this
 * service again because Location provider has been initialized in {@code onCreate()} of this service. To restart the service,
 * just stop it using {@code stopService()} because in {@code onDestroy()} of this service, this service is started again.
 * It will be started automatically. In case of <u>logout</u> only, this service will be destroyed completely.
 * <br/><br/>
 * This services uses Google's fused location api to get current location of device that is more powerful approach
 * than traditional GPS and NETWORK providers.
 * <br/><br/>
 * To keep our location service alive in less resource condition, this service is structured as a foreground service. It mean,
 * A notification will always be in Notification Panel of the device.
 *
 */

public class LocationService extends Service implements LocationListener {
    /**
     * To access app preferences
     */
    private SPUtils spUtils;

    /**
     * For a quick access to the last known / most recent location
     */
    public static Location recent;

    /**
     * To get continues location updates
     */
    private FusedLocationProvider provider;

    /**
     * Because it is a foreground service so it is needed to have a notification on notification drawer always.
     * Following instance will build a notification on every location update with default style and settings.
     */
    private NotificationCompat.Builder builder;

    /**
     * It is device specific id to record data in iot hub. It can be retrieved from shared preferences.
     */
    private static String DEVICE_ID;

    /**
     * To keep track on the battery status. Every time we receive battery level,
     * this static variable's value will be changed
     */
    private static int BATTERY_LEVEL;

    /**
     * It's nothing more than a simple broad cast to catch battery level whenever it changes
     */
    private BroadcastReceiver batteryReceiver = new BatteryBroadcast();

    /**
     * Following broadcast will be received whenever state of the network changes
     */
    private BroadcastReceiver networkReceiver = new BatteryBroadcast();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.spUtils = new SPUtils(this);
        DEVICE_ID = spUtils.getString(SPUtils.DEVICE_ID);
        int interval = spUtils.getInterval();

        // Confirming that no other instance is active of provider
        if (provider != null) {
            provider = null;
            Log.e("LocationService", "Setting null to fused location provider and current interval is " + interval);
        }

        // now initiating fused location provider and notification builder
        AppCompatActivity activity = ((FlintDriver) getApplication()).getActivity();
        provider = new FusedLocationProvider(activity != null ? activity : this)
                .setInterval(1000 * interval)
                .setFastestInterval(1000 * interval)
                .setPriority(interval < 10 ? FusedLocationProvider.PRIORITY_HIGH_ACCURACY : FusedLocationProvider.PRIORITY_BALANCED_POWER_ACCURACY)
                .setLocationListener(this);

        builder = new NotificationCompat.Builder(this)
                .setContentTitle("Location Service")
                .setContentText("Fetching current location...")
                .setSmallIcon(R.mipmap.ic_service_notification)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setShowWhen(true)
                .setOngoing(true);

        // registering battery receiver
        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        // registering network receiver
        registerReceiver(networkReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        switch (intent.getAction()) {
            // if we have to start service
            case START_FOREGROUND_SERVICE:
                startForeground(REQUEST_FOREGROUND, builder.build());
                provider.start();
                break;
            // if we are going to stop everything
            case STOP_FOREGROUND_SERVICE:
                stopForeground(true);
                provider.stop();
                stopSelf();
                break;
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            // Preparing data to be sent in IOT HUB
            final JsonArray array = new JsonArray();

            JsonObject hostStatus = new JsonObject();
            hostStatus.addProperty("data_source", "host_status");
            hostStatus.addProperty("current_value", "ON");
            hostStatus.addProperty("service_name", "host_status");
            hostStatus.addProperty("check_timestamp", location.getTime() / 1000);
            hostStatus.addProperty("sys_timestamp", Calendar.getInstance().getTimeInMillis() / 1000);
            hostStatus.addProperty("device_id", DEVICE_ID);
            array.add(hostStatus);

            // formatting data according to iot hub
            for (int i = 1; i < 5; i++) {
                JsonObject object = new JsonObject();
                object.addProperty("data_source",
                                i == 1 ? "Latitude" :
                                        i == 2 ? "Longitude" :
                                                i == 3 ? "Accuracy" :
                                                        i == 4 ? "Rotation" : "Battery");
                object.addProperty("current_value",
                                i == 1 ? location.getLatitude() :
                                        i == 2 ? location.getLongitude() :
                                                i == 3 ? location.getAccuracy() :
                                                        i == 4 ? (recent != null ? recent.bearingTo(location) : 0) : BATTERY_LEVEL);

                object.addProperty("service_name", "mobileLocationService");
                object.addProperty("check_timestamp", location.getTime() / 1000);
                object.addProperty("sys_timestamp", Calendar.getInstance().getTimeInMillis() / 1000);
                object.addProperty("device_id", DEVICE_ID);
                array.add(object);
            }

            // Sending data to server
            RESTClient.updateData(this, array);

            // updating notification details
            startForeground(REQUEST_FOREGROUND, builder.setContentText("Current Location : " + location.getLatitude() + "," + location.getLongitude()).build());

            // Toasting new location (to be remove in release)
            //Toast.makeText(this, location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT).show();
            Log.e("LocationService", spUtils.getInterval() + " Seconds interval | " + location.getLatitude() + "," + location.getLongitude());

            // sending broadcast to ui
            sendBroadcast(new Intent(BROADCAST_ON_CURRENT_LOCATION).putExtra("location", location));

            // holding last known location in "recent" object
            recent = location;
        } else {
            Toast.makeText(this, "Getting Null Locations", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            // stopping location provider
            if (provider != null)
                provider.stop();

            // un-registering network receiver
            if (networkReceiver != null)
                unregisterReceiver(networkReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // un-registering battery receiver
            if (batteryReceiver != null)
                unregisterReceiver(batteryReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // if user us logged in then restarting the service again
        if (!spUtils.getString(SPUtils.USER_ID).trim().equals("")) {
            startService(new Intent(this, LocationService.class).setAction(START_FOREGROUND_SERVICE));
        }
    }

    /**
     * It's nothing more than a simple broad cast to catch battery level whenever it changes
     *
     * @author Mohsin Khan
     * @date 26 November 2016
     */
    class BatteryBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            BATTERY_LEVEL = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        }
    }

    /**
     * This class will received broadcast if user connects/disconnects to the internet
     *
     * Note :
     * Please ensure all the records will be updated to the server within connection/write timeout
     *
     * @author Mohsin Khan
     * @date 30 November 2016
     */
    class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("LocationService", "NetworkReceiver.onReceive");
            if (Utils.isConnected(LocationService.this)) {
                RESTClient.updateData(LocationService.this, new DBHandler(LocationService.this).getTracking());
            }
        }
    }
}