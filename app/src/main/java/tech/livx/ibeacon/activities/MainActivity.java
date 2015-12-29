package tech.livx.ibeacon.activities;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import tech.livx.ibeacon.MyApplication;
import tech.livx.ibeacon.R;
import tech.livx.ibeacon.interfaces.ApiCallBackInterface;
import tech.livx.ibeacon.models.BeaconContract;
import tech.livx.ibeacon.models.SpecialContract;
import tech.livx.ibeacon.services.ApiService;
import tech.livx.ibeacon.services.ApiService.LocalBinder;

/**
 * Created by Aj van Deventer on 23 November 2015
 *
 * Main Activity does not start any services but gets a pointer to beaconService and
 * binds to the already running ApiService
 *
 * @author AJ van Deventer
 * @version 1.0
 *
 */
public class MainActivity extends AppCompatActivity implements ApiCallBackInterface, BeaconManager.RangingListener{
    private static final String TAG = "MainActivity";
    private boolean isBound = false;
    private ApiService beaconService;
    private MyApplication application;
    private static long num = 0;
    private BeaconManager manager;
    private SimpleCursorAdapter adapter;

    //For exsample views(Delete after use)
    private Button button;
    private TextView textView;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "Binding onServiceConnect");
            LocalBinder binder = (LocalBinder)service;
            beaconService = binder.getService();
            beaconService.addCallBack(MainActivity.this);
            isBound = true;
            startRanging();
            checkForResources();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    private void startRanging() {
        manager = application.getBeaconManager();
        manager.startRanging(application.getRegion());
        manager.setRangingListener(this);
    }

    private void checkForResources() {
        if(beaconService.contains(num)){
            String val = beaconService.getValueSaved(num);
            setResult(val);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState != null){
            num = savedInstanceState.getLong("ApiId");
        }
        application = (MyApplication)getApplication();

        button = (Button)findViewById(R.id.button);
        textView = (TextView)findViewById(R.id.result_text_view);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doApiCall("/beacons");
            }
        });

        String colombs[] = new String[]{
                BeaconContract._ID,BeaconContract.MAJOR,BeaconContract.MINOR
        };

        int values[] = new int[]{
                R.id.major,
                R.id.minor
        };

        adapter = new SimpleCursorAdapter(MainActivity.this,R.layout.activity_main,null,colombs,values,0);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!isBound){
            Intent i = new Intent(MainActivity.this, ApiService.class);
            Bundle bun = new Bundle();
            bindService(i, connection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRanging();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isBound){
            unbindService(connection);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!isBound){
            Intent i = new Intent(MainActivity.this, ApiService.class);
            Bundle bun = new Bundle();
            bindService(i, connection, Context.BIND_AUTO_CREATE);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(beaconService == null)
            return;

    }

    @Override
    public void OnSuccess(final String result, int code) {
        if(code == 200){
            if (result != null){
                try {
                    parseResult(result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setResult(result);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

        beaconService.removeCallBack(this);
        String callId = beaconService.getValueSaved(num);
    }

    @Override
    public void OnFailed(String value, int code) {
        setResult(value);
    }

    public void doApiCall(String url){
        Log.i("AJ","Caling api");
        num = beaconService.getRandomNumber();
        beaconService.doApiCall(url, num);
    }

    public void setResult(final String resultMessage){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(resultMessage);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong("ApiId",num);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBeaconsDiscovered(Region region, List<Beacon> list) {
        Log.i("AJ", "Beacon Discovered");
        if(list.size() < 0){

        }
    }

    public void stopRanging(){
        
    }

    public void parseResult(String result) throws JSONException {
        JSONObject object = new JSONObject(result);
        JSONArray jsonArray = object.getJSONArray("result");
        for(int x = 0; x <jsonArray.length();x++) {
            JSONObject obj = (JSONObject) jsonArray.get(x);
            ContentValues values = new ContentValues();
            values.put(SpecialContract.DESCRIPTION,obj.getString("description"));
            values.put(SpecialContract.ID,obj.getLong("id"));
            values.put(SpecialContract.IMAGE,obj.getString("image"));
            values.put(SpecialContract.LINK,obj.getString("link"));
            values.put(SpecialContract.MAJOR,obj.getInt("major"));
            values.put(SpecialContract.MINOR,obj.getInt("minor"));
            values.put(SpecialContract.SPECIAL_ID,obj.getLong("special_id"));
            getContentResolver().insert(SpecialContract.CONTENT_URI, values);
        }

    }
}
