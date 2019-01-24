package renchaigao.com.zujuba.util;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

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

/**
 * Created by Administrator on 2019/1/22/022.
 */

public class HttpClientTask extends AsyncTask<String, String, String> {

    public String path, jsonString,responseId,sendTime;
    private Activity activity;
    public void SetActivity (Activity activity){
        this.activity = activity;
    }
    public void SetData(String path,String jsonString,String responseId,String sendTime){
        this.path = path;
        this.jsonString = jsonString;
        this.responseId =responseId;
        this.sendTime = sendTime;
    }

    @Override
    protected String doInBackground(String... voids) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        builder.sslSocketFactory(OkhttpFunc.createSSLSocketFactory());
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        final RequestBody body = RequestBody.create(FinalDefine.MEDIA_TYPE_JSON, jsonString);
        final Request request = new Request.Builder()
                .url(path)
                .header("Content-Type", "application/json")
                .post(body)
                .build();
        builder.build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                DataUtil.ResponseData responseData = new DataUtil.ResponseData();
                responseData.setRet(false);
                responseData.setId(responseId);
                responseData.setResponseStr(JSONObject.toJSONString(e));
                responseData.setSendTime(sendTime);
                DataUtil.SaveResponse(activity,responseData);
                System.out.println("HttpClientTask  onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                DataUtil.ResponseData responseData = new DataUtil.ResponseData();
                responseData.setId(responseId);
                responseData.setResponseStr(JSONObject.toJSONString(response));
                responseData.setSendTime(sendTime);
                responseData.setRet(true);
                DataUtil.SaveResponse(activity,responseData);
                System.out.println("HttpClientTask  onResponse");
            }
        });
        return null;
    }
}

