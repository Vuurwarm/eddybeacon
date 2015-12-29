package tech.livx.ibeacon;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;


import java.util.List;
import java.util.UUID;

import tech.livx.ibeacon.activities.MainActivity;
import tech.livx.ibeacon.services.ApiService;

/**
 * Created by damionunderworld on 2015/11/23.
 *
 * Application Both Starts the BEacon Detection Service as well as the ApiService
 */
public class MyApplication extends Application implements BeaconManager.MonitoringListener{
    private static final int NOTIFICATION_ID = 0;
    private BeaconManager manager;
    private static final Region region =  new Region("LivXIBeacons", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),null,null);
    private boolean isBound = false;
    private ApiService beaconService;
    private MyApplication application;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ApiService.LocalBinder binder = (ApiService.LocalBinder)service;
            beaconService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        manager = new BeaconManager(MyApplication.this);
        manager.setMonitoringListener(this);
        manager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                manager.startMonitoring(region);
            }
        });
        startService(new Intent(this, ApiService.class));
    }

    @Override
    public void onEnteredRegion(Region region, List<Beacon> list) {
        String title = "Title";
        String message = "Message Message Message";
        showNotification(title, message);
        manager.startRanging(region);
    }

    @Override
    public void onExitedRegion(Region region) {
        //Remember that it will only fire 30 seconds after you have exited the region, to ensure
        //that you have accurately exited a region.
        manager.stopRanging(region);
    }

    public void showNotification(String title, String message){
        Intent i = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notice = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent).build();

        notice.defaults = Notification.DEFAULT_SOUND;
        NotificationManager noticeManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        noticeManager.notify(NOTIFICATION_ID, notice);
    }


    public BeaconManager getBeaconManager(){
        return manager;
    }

    public Region getRegion(){
        return region;
    }

}
