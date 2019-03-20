package renchaigao.com.zujuba.Service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import renchaigao.com.zujuba.util.HttpClientTask;

public class HttpService extends Service {
    public HttpService() {
    }

    public HttpClientTask httpClientTask = new HttpClientTask();

    private ClientBinder mBinder = new ClientBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class ClientBinder extends Binder {
        private Activity activity;

        public void SetActivity(Activity activity) {
            this.activity = activity;
        }

        public void starClient(String path, String json, String responseId, String sendTime) {
            httpClientTask.SetActivity(activity);
            httpClientTask.SetData(path, json, responseId, sendTime);
            httpClientTask.execute("");
            Log.e("HttpService", "starClient");
        }

        public void cancleClient() {
            Log.e("HttpService", "cancleClient");

        }
    }

}
