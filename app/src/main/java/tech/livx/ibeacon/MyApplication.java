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
import android.util.Log;


import org.altbeacon.beacon.AltBeacon;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;


import java.util.Collection;

import tech.livx.ibeacon.activities.MainActivity;
import tech.livx.ibeacon.services.ApiService;

/**
 * Created by damionunderworld on 2015/11/23.
 *
 * Application Both Starts the BEacon Detection Service as well as the ApiService
 */
public class MyApplication extends Application implements MonitorNotifier, RangeNotifier, BeaconConsumer {
    private static final String TAG = "MyApplication";

    private static final int NOTIFICATION_ID = 0;
    private BeaconManager manager;
    private static final Region region =  new Region("LivXBeacons", null,null,null);
    private boolean isBound = false;
    private ApiService beaconService;
    private MyApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        manager = BeaconManager.getInstanceForApplication(this);

        manager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        manager.bind(this);
        startService(new Intent(this, ApiService.class));
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

    @Override
    public void didEnterRegion(Region region) {
        Log.i(TAG,"Did enter a region");
        if(region != null){
            showNotification("Title","Message message");
        }
    }

    @Override
    public void didExitRegion(Region region) {
        Log.i(TAG,"Exited a region");
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {

    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
        Log.i(TAG,"Ranging was fired");
    }

    @Override
    public void onBeaconServiceConnect() {
        manager.setMonitorNotifier(this);
        manager.setRangeNotifier(this);
    }
}
