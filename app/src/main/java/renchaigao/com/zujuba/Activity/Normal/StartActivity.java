package renchaigao.com.zujuba.Activity.Normal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.FinalDefine;
import renchaigao.com.zujuba.util.OkhttpFunc;
import renchaigao.com.zujuba.util.PropertiesConfig;

/**
 * Created by Administrator on 2018/7/27/027.
 */


public class StartActivity extends AppCompatActivity {

    private final String TAG = "StartActivity";

    // 要申请的权限
    private String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private ArrayList<String> waitPermissions = new ArrayList<>();
    private AlertDialog dialog;

    private String dataJsonString = null;
    // Handler内部类，它的引用在子线程中被使用，发送mesage，被handlerMesage方法接收
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            String str = (String) msg.obj;
            Toast.makeText(StartActivity.this, str, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        LitePal.initialize(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Boolean allCheckResult = true;//当所有授权均通过时，才为true；
            for (int num = 0; num < permissions.length; num++) {
                // 轮训检查所有权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permissions[num]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 没有授予该权限情况下
                    waitPermissions.add(permissions[num]);//增加待授权内容至待授权list内
                    allCheckResult = false;
                }
            }
            if (!allCheckResult) {
                showDialogTipUserRequestPermission();
            } else {
                afterPermissonSteps();
            }
        }
    }

    private void afterPermissonSteps() {
        if(checkAllPermisson()){
            SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
            dataJsonString = pref.getString("user", null);
            if (null != dataJsonString)
                sendAutoLogin();
            else {
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }else finish();
    }
    private Boolean checkAllPermisson(){
        for (int num = 0; num < permissions.length; num++) {
            // 轮训检查所有权限是否已经获取
            int i = ContextCompat.checkSelfPermission(this, permissions[num]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 没有授予该权限情况下
                return false;
            }
        }
        return true;
    }

    // 提示用户该请求权限的弹出框
    private void showDialogTipUserRequestPermission() {
        String titleStr = null, messageStr = null;
        switch (waitPermissions.get(0)) {
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                titleStr = "存储权限不可用";
                messageStr = "由于组局吧需要获取存储空间，为你存储个人信息；\n否则，您将无法正常使用组局吧";
                break;
            case Manifest.permission.ACCESS_COARSE_LOCATION:
                titleStr = "位置权限不可用";
                messageStr = "由于组局吧需要获取位置，为你定位服务；\n否则，您将无法正常使用组局吧";
                break;
        }
        new AlertDialog.Builder(this)
                .setTitle(titleStr)
                .setMessage(messageStr)
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }

    // 开始提交请求权限
    private void startRequestPermission() {
        String[] requestPermission =
                waitPermissions.toString().replace("[", "").replace("]", "").replace(" ", "").split(",");
        ActivityCompat.requestPermissions(this, requestPermission, 321);
    }

    // 用户权限 申请 的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting();
                    } else
                        finish();
                } else {
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                    afterPermissonSteps();
                }
            }
        }
    }

    // 提示用户去应用设置界面手动开启权限

    private void showDialogTipUserGoToAppSettting() {
        String titleStr = null, messageStr = null;
        switch (waitPermissions.get(0)) {
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                titleStr = "存储权限不可用，且被上次操作禁止";
                messageStr = "请在-应用设置-权限-中，允许组局吧使用存储权限来保存用户数据；\n要不，组局吧将无法为您正常服务o(╥﹏╥)o";
                break;
            case Manifest.permission.ACCESS_COARSE_LOCATION:
                titleStr = "位置权限不可用，且被上次操作禁止";
                messageStr = "请在-应用设置-权限-中，允许组局吧使用位置权限来保存用户数据；\n要不，组局吧将无法为您正常服务o(╥﹏╥)o";
                break;
        }
        dialog = new AlertDialog.Builder(this)
                .setTitle(titleStr)
                .setMessage(messageStr)
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, 123);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                if (!checkAllPermisson()) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSettting();
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                    afterPermissonSteps();
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void sendAutoLogin() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }

            @Override
            protected void onCancelled(Void aVoid) {
                super.onCancelled(aVoid);
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected Void doInBackground(Void... params) {
                String path = PropertiesConfig.userServerUrl + "login/auto/0";
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
                String str = dataJsonString;
                final RequestBody body = RequestBody.create(FinalDefine.MEDIA_TYPE_JSON, dataJsonString);
                final Request request = new Request.Builder()
                        .url(path)
                        .header("Content-Type", "application/json")
                        .post(body)
                        .build();
                builder.build().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, call.request().body().toString());
                        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(response.body().string());
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            JSONObject responseJsonData = (JSONObject) responseJson.getJSONObject("data");
                            String token;
                            SharedPreferences.Editor editor;
                            Intent intent;
                            Message msg = new Message();
                            switch (code) {
                                case 0: //用户是存在的，更新数据成功；
                                    //将token信息保存至本地
                                    token = responseJsonData.get("token").toString();
                                    editor = getSharedPreferences("userData", MODE_PRIVATE).edit();
                                    editor.putString("token", token);
                                    editor.putString("user", responseJsonData.toJSONString());
                                    editor.apply();
                                    intent = new Intent(StartActivity.this, AdvertisingActivity.class);
                                    startActivity(intent);
                                    msg.obj = "登录成功";
                                    // 把消息发送到主线程，在主线程里现实Toast
                                    handler.sendMessage(msg);
                                    finish();
                                    break;
                                case 1: //在数据库中更新用户数据出错；
                                    intent = new Intent(StartActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case -1003: //用户是存在的，本地的TOKEN超时，需要重新登录；
                                    intent = new Intent(StartActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    msg.obj = "token过期";
                                    // 把消息发送到主线程，在主线程里现实Toast
                                    handler.sendMessage(msg);
                                    finish();
                                    break;
                                case -1004: //用户是存在的，本地的TOKEN错误；
                                    intent = new Intent(StartActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    msg.obj = "token错误";
                                    // 把消息发送到主线程，在主线程里现实Toast
                                    handler.sendMessage(msg);
                                    finish();
                                    break;
                                case -1005: //生成token错误；
                                    intent = new Intent(StartActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    msg.obj = "生成token错误";
                                    // 把消息发送到主线程，在主线程里现实Toast
                                    handler.sendMessage(msg);
                                    finish();
                                    break;

                                case -1010: //没有找到用户；
                                    intent = new Intent(StartActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    msg.obj = "用户不存在，请确认手机号正确";
                                    // 把消息发送到主线程，在主线程里现实Toast
                                    handler.sendMessage(msg);
                                    finish();
                                    break;

                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }
                });
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.e(TAG, "onPostExecute");
            }
        }.execute();
    }
//    private void addUser(final String dataJsonString) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String path = PropertiesConfig.testServerUrl + "user/login/auto/0";
//                OkHttpClient.Builder builder = new OkHttpClient.Builder();
//                OkhttpFunc okhttpFunc = new OkhttpFunc();
//                builder.sslSocketFactory(okhttpFunc.createSSLSocketFactory());
//                builder.hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                });
//                final RequestBody body = RequestBody.create(FinalDefine.MEDIA_TYPE_JSON, dataJsonString);
//                final Request request = new Request.Builder()
//                        .url(path)
//                        .header("Content-Type", "application/json")
//                        .post(body)
//                        .build();
//                builder.build().newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        Log.e(TAG, call.request().body().toString());
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        try {
//                            JSONObject responseJson = JSONObject.parseObject(response.body().string());
//                            int code = Integer.valueOf(responseJson.get("code").toString());
//                            JSONObject responseJsonData = (JSONObject) responseJson.getJSONObject("data");
//                            String token;
//                            SharedPreferences.Editor editor;
//                            Intent intent;
//                            switch (code) {
//                                case 1: //在数据库中更新用户数据出错；
//                                    Toast.makeText(StartActivity.this, "在数据库中更新用户数据出错", Toast.LENGTH_LONG).show();
//                                    break;
//                                case 1002: //用户是存在的，更新数据成功；
//                                    //将token信息保存至本地
//                                    token = responseJsonData.get("token").toString();
//                                    editor = getSharedPreferences("userData", MODE_PRIVATE).edit();
//                                    editor.putString("token", token);
//                                    editor.putString("responseJsonDataString", responseJsonData.toJSONString());
//                                    editor.apply();
//                                    if (!hasGo) {//程序执行到这一步说明返回的数据已经回来，
//                                        hasGo = true;
//                                        intent = new Intent(StartActivity.this, MainActivity.class);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                    break;
//                                case -1003: //用户是存在的，本地的TOKEN超时，需要重新登录；
//                                    Toast.makeText(StartActivity.this, "本地的TOKEN超时，需要重新登录", Toast.LENGTH_LONG).show();
//                                    break;
//                                case -1004: //用户是存在的，本地的TOKEN错误；
//                                    Toast.makeText(StartActivity.this, "本地的TOKEN错误", Toast.LENGTH_LONG).show();
//                                    break;
//                            }
//                        } catch (Exception e) {
//                            Log.e(TAG, e.toString());
//                        }
//                    }
//                });
//            }
//        }).start();
//    }
}