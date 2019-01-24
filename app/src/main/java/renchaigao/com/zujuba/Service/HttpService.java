package renchaigao.com.zujuba.Service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.idst.nls.internal.protocol.Content;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import renchaigao.com.zujuba.Activity.Normal.AdvertisingActivity;
import renchaigao.com.zujuba.Activity.Normal.LoginActivity;
import renchaigao.com.zujuba.Activity.Normal.StartActivity;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.FinalDefine;
import renchaigao.com.zujuba.util.HttpClientTask;
import renchaigao.com.zujuba.util.OkhttpFunc;
import renchaigao.com.zujuba.util.PropertiesConfig;

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
