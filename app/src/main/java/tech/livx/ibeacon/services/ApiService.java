package tech.livx.ibeacon.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.estimote.sdk.BeaconManager;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import tech.livx.ibeacon.MyApplication;
import tech.livx.ibeacon.interfaces.ApiCallBackInterface;

/**
 * Created by AJ van Deventer on 2015/11/23.
 *
 */
public class ApiService extends Service {
    private static final String TAG = "BeaconDetectionServices";

    private ArrayList<ApiCallBackInterface> array = new ArrayList<>();
    private HashMap<Long,String> hashMap;
    private final IBinder mBinder = new LocalBinder();
    private BeaconManager manager;
    private MyApplication application;
    private final Random randomNumber = new Random();
    private OkHttpClient okHttpClient;

    public void addCallBack(ApiCallBackInterface pointer) {
        array.add(pointer);
    }

    public void removeCallBack(ApiCallBackInterface pointer){
    }

    public class LocalBinder extends Binder{
        public ApiService getService(){
            return ApiService.this;
        }
    }

    public void runApiCommand(final long number,String url){
        String tempUrl = "http://private-ff5bd2-ajvandeventer.apiary-mock.com" + url;
        Request request = new Request.Builder().url(tempUrl).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                hashMap.put(number,"Somthing went wrong");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                if(response != null) {
                    hashMap.put(number, response.message());
                    if (response.code() != 404) {
                        for (int x = 0; x < array.size(); x++) {
                            String temp = response.body().string();
                            array.get(x).OnSuccess(temp, response.code());

                        }


                        return;
                    }

                    for (int x = 0; x < array.size(); x++) {
                        array.get(x).OnFailed(response.body().toString(), response.code());
                    }

                }
            }
        });


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"OnStartCommand Called");
        application = (MyApplication)getApplication();
        manager = application.getBeaconManager();
        okHttpClient = new OkHttpClient();
        hashMap = new HashMap<>();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "OnBind");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public long getRandomNumber(){
        return randomNumber.nextInt(1000);
    }

    public void doApiCall(String value, long num){
        hashMap.put(num, "");
        runApiCommand(num, value);

    }

    public String getValueSaved(long num){
        String value = "";
        value = new String(hashMap.get(num));
        return value;
    }

    public boolean contains(long num){
        return hashMap.containsKey(num);
    }

}
