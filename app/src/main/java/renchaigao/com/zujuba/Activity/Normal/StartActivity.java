package renchaigao.com.zujuba.Activity.Normal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import org.litepal.LitePal;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.Activity.BaseActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.PropertiesConfig;
import renchaigao.com.zujuba.util.http.ApiService;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;

/**
 * Created by Administrator on 2018/7/27/027.
 */


public class StartActivity extends BaseActivity {

    private final String TAG = "StartActivity";

    // 要申请的权限
    private String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
    };
    private ArrayList<String> waitPermissions = new ArrayList<>();
    private AlertDialog dialog;
    private String path = null;
    private String dataJsonString = null;
    // Handler内部类，它的引用在子线程中被使用，发送mesage，被handlerMesage方法接收
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String str = (String) msg.obj;
            Toast.makeText(StartActivity.this, str, Toast.LENGTH_SHORT).show();
        }
    };

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_start);
//        Intent startHttpService = new Intent(this, HttpService.class);
////        startService(startHttpService);
////        ActionBar actionBar = getSupportActionBar();
////        if (actionBar != null) {
////            actionBar.hide();
////        }
//
//
//    }

    @Override
    protected void InitView() {
    }

    @Override
    protected void InitData() {

    }

    @Override
    protected void InitOther() {
        LitePal.initialize(this);
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


    @Override
    protected int getLayoutId() {
        return R.layout.activity_start;
    }

    UserInfo userInfo = new UserInfo();
    String userId = null;
    String userToken = null;
//
//    private HttpService.ClientBinder clientBinder;
//
//    private ServiceConnection connection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
////            clientBinder = (HttpService.ClientBinder) service;
////            clientBinder.SetActivity(StartActivity.this);
////            clientBinder.starClient(path,dataJsonString);
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//        }
//    };

    private void afterPermissonSteps() {
        if (checkAllPermisson()) {
            dataJsonString = DataUtil.GetUserInfoStringData(this);
            if (null != dataJsonString) {
                userInfo = JSONObject.parseObject(dataJsonString, UserInfo.class);
                userId = userInfo.getId();
                userToken = userInfo.getToken();
//                path = PropertiesConfig.userServerUrl + "login/auto/" + userInfo.getTelephone() + "/" + userId;
//                dataJsonString = jsonObject.toJSONString();
//                Intent bindIntent = new Intent(this, HttpService.class);
//                bindService(bindIntent, connection, BIND_ABOVE_CLIENT);


//                for (int i=0;i<10;i++){
//
//                    RetrofitServiceManager.getInstance().SetRetrofit(PropertiesConfig.userServerUrl);
//                }


                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userToken", userToken);

                RetrofitServiceManager.getInstance().SetRetrofit(PropertiesConfig.userServerUrl);
                addSubscribe(RetrofitServiceManager.getInstance().creat(ApiService.class)
                        .FourParameterJsonPost(
                                "login",
                                "auto",
                                userInfo.getTelephone(),
                                userId,
                                jsonObject)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new BaseObserver<ResponseEntity>(this) {
                            @Override
                            public void onNext(ResponseEntity value) {
                                try {
                                    JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                                    int code = Integer.valueOf(responseJson.get("code").toString());
                                    String token;
                                    Intent intent;
                                    Message msg = new Message();
                                    switch (code) {
                                        case RespCodeNumber.LOGIN_AUTO_SUCCESS:
                                            //用户是存在的，更新数据成功；
                                            //将token信息保存至本地
                                            String responseJsonDataString = responseJson.getJSONObject("data").toJSONString();
                                            userInfo = JSONObject.parseObject(responseJsonDataString, UserInfo.class);
                                            token = userInfo.getToken();
                                            DataUtil.SaveUserInfoData(StartActivity.this, responseJsonDataString);
                                            DataUtil.saveToken(StartActivity.this, token);
                                            intent = new Intent(StartActivity.this, AdvertisingActivity.class);
                                            startActivity(intent);
                                            msg.obj = "登录成功";
                                            // 把消息发送到主线程，在主线程里现实Toast
                                            handler.sendMessage(msg);
                                            finish();
                                            break;
                                        case RespCodeNumber.LOGIN_AUTO_FAIL:
                                            msg.obj = "LOGIN_AUTO_FAIL";
                                            break;
                                        case RespCodeNumber.LOGIN_AUTO_EXCEPTION:
                                            msg.obj = "LOGIN_AUTO_EXCEPTION";
                                            break;
                                        case RespCodeNumber.LOGIN_AUTO_WRONG:
                                            msg.obj = "LOGIN_AUTO_WRONG";
                                            break;
                                    }
                                    handler.sendMessage(msg);
                                    intent = new Intent(StartActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } catch (Exception e) {
                                    Log.e(TAG, e.toString());
                                }
                            }

                            @Override
                            protected void onSuccess(ResponseEntity responseEntity) {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onComplete() {

                                Toast.makeText(StartActivity.this, "onComplete", Toast.LENGTH_SHORT);
                                Log.e(TAG, "onComplete:");

                            }
                        }));

//
//                RetrofitServiceManager.getInstance().create(ApiService.class)
//                        .FourParameterJsonPost("login",
//                                "auto",
//                                userInfo.getTelephone(),
//                                userId,
//                                jsonObject)
////                        .compose(RxjavaHelper.<BaseResponse<List<RecommendEntity>>>observeOnMainThread())
//                OkHttpUtil okHttpUtil = new OkHttpUtil();
//                Retrofit retrofit = new Retrofit.Builder()
////                        .client(OkHttpUtil.getOkHttpUtil().build())
//                        .client(okHttpUtil.getBuilder().build())
//                        .baseUrl(PropertiesConfig.userServerUrl)
//                        //设置数据解析器
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                        .build();
//                ApiService apiService = retrofit.create(ApiService.class);
//                apiService.FourParameterJsonPost("login",
//                        "auto",
//                        userInfo.getTelephone(),
//                        userId,
//                        jsonObject)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Observer<ResponseEntity>() {
//                            @Override
//                            public void onSubscribe(Disposable d) {
//                                Toast.makeText(StartActivity.this, "onSubscribe", Toast.LENGTH_SHORT);
//                                Log.e(TAG, "onSubscribe:");
//                            }
//
//                            @Override
//                            public void onNext(ResponseEntity value) {
//                                try {
//                                    JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
//                                    int code = Integer.valueOf(responseJson.get("code").toString());
//                                    String token;
//                                    Intent intent;
//                                    Message msg = new Message();
//                                    switch (code) {
//                                        case RespCodeNumber.LOGIN_AUTO_SUCCESS:
//                                            //用户是存在的，更新数据成功；
//                                            //将token信息保存至本地
//                                            String responseJsonDataString = responseJson.getJSONObject("data").toJSONString();
//                                            userInfo = JSONObject.parseObject(responseJsonDataString, UserInfo.class);
//                                            token = userInfo.getToken();
//                                            DataUtil.SaveUserInfoData(StartActivity.this, responseJsonDataString);
//                                            DataUtil.saveToken(StartActivity.this, token);
//                                            intent = new Intent(StartActivity.this, AdvertisingActivity.class);
//                                            startActivity(intent);
//                                            msg.obj = "登录成功";
//                                            // 把消息发送到主线程，在主线程里现实Toast
//                                            handler.sendMessage(msg);
//                                            finish();
//                                            break;
//                                        case RespCodeNumber.LOGIN_AUTO_FAIL:
//                                            msg.obj = "LOGIN_AUTO_FAIL";
//                                            break;
//                                        case RespCodeNumber.LOGIN_AUTO_EXCEPTION:
//                                            msg.obj = "LOGIN_AUTO_EXCEPTION";
//                                            break;
//                                        case RespCodeNumber.LOGIN_AUTO_WRONG:
//                                            msg.obj = "LOGIN_AUTO_WRONG";
//                                            break;
//                                    }
//                                    handler.sendMessage(msg);
//                                    intent = new Intent(StartActivity.this, LoginActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                } catch (Exception e) {
//                                    Log.e(TAG, e.toString());
//                                }
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }
//
//                            @Override
//                            public void onComplete() {
//
//                                Toast.makeText(StartActivity.this, "onComplete", Toast.LENGTH_SHORT);
//                                Log.e(TAG, "onComplete:");
//
//                            }
//                        });

            } else {
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        } else

            finish();

    }

    private Boolean checkAllPermisson() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        handler.;
    }

    //    @SuppressLint("StaticFieldLeak")
//    private void sendAutoLogin() {
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected void onCancelled() {
//                super.onCancelled();
//            }
//
//            @Override
//            protected void onCancelled(Void aVoid) {
//                super.onCancelled(aVoid);
//            }
//
//            @Override
//            protected void onProgressUpdate(Void... values) {
//                super.onProgressUpdate(values);
//            }
//
//            @Override
//            protected Void doInBackground(Void... params) {
//
//
//                String path = PropertiesConfig.userServerUrl + "login/auto/" + userInfo.getTelephone() + "/" + userId;
//                OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                        .connectTimeout(3, TimeUnit.SECONDS)
//                        .readTimeout(15, TimeUnit.SECONDS)
//                        .writeTimeout(15, TimeUnit.SECONDS)
//                        .retryOnConnectionFailure(true);
//                builder.sslSocketFactory(OkhttpFunc.createSSLSocketFactory());
//                builder.hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                });
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("userToken", userToken);
//                final RequestBody body = RequestBody.create(FinalDefine.MEDIA_TYPE_JSON, jsonObject.toString());
//                final Request request = new Request.Builder()
//                        .url(path)
//                        .header("Content-Type", "application/json")
//                        .post(body)
//                        .build();
//                builder.build().newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        Log.e(TAG, call.request().body().toString());
//                        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        try {
//                            JSONObject responseJson = JSONObject.parseObject(response.body().string());
//                            int code = Integer.valueOf(responseJson.get("code").toString());
//                            String token;
//                            Intent intent;
//                            Message msg = new Message();
//                            switch (code) {
//                                case RespCodeNumber.LOGIN_AUTO_SUCCESS:
//                                    //用户是存在的，更新数据成功；
//                                    //将token信息保存至本地
//                                    String responseJsonDataString = responseJson.getJSONObject("data").toJSONString();
//                                    userInfo = JSONObject.parseObject(responseJsonDataString, UserInfo.class);
//                                    token = userInfo.getToken();
//                                    DataUtil.SaveUserInfoData(StartActivity.this, responseJsonDataString);
//                                    DataUtil.saveToken(StartActivity.this, token);
//                                    intent = new Intent(StartActivity.this, AdvertisingActivity.class);
//                                    startActivity(intent);
//                                    msg.obj = "登录成功";
//                                    // 把消息发送到主线程，在主线程里现实Toast
//                                    handler.sendMessage(msg);
//                                    finish();
//                                    break;
//                                case RespCodeNumber.LOGIN_AUTO_FAIL:
//                                    msg.obj = "LOGIN_AUTO_FAIL";
//                                    break;
//                                case RespCodeNumber.LOGIN_AUTO_EXCEPTION:
//                                    msg.obj = "LOGIN_AUTO_EXCEPTION";
//                                    break;
//                                case RespCodeNumber.LOGIN_AUTO_WRONG:
//                                    msg.obj = "LOGIN_AUTO_WRONG";
//                                    break;
//                            }
//                            handler.sendMessage(msg);
//                            intent = new Intent(StartActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                            finish();
//                        } catch (Exception e) {
//                            Log.e(TAG, e.toString());
//                        }
//                    }
//
//                });
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                Log.e(TAG, "onPostExecute");
//            }
//        }.execute();
//    }

//
//    @SuppressLint("StaticFieldLeak")
//    private void sendAutoLogin() {
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected void onCancelled() {
//                super.onCancelled();
//            }
//
//            @Override
//            protected void onCancelled(Void aVoid) {
//                super.onCancelled(aVoid);
//            }
//
//            @Override
//            protected void onProgressUpdate(Void... values) {
//                super.onProgressUpdate(values);
//            }
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                String path = PropertiesConfig.userServerUrl + "login/auto/" + userInfo.getTelephone() + "/" + userId;
//                OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                        .connectTimeout(3, TimeUnit.SECONDS)
//                        .readTimeout(15, TimeUnit.SECONDS)
//                        .writeTimeout(15, TimeUnit.SECONDS)
//                        .retryOnConnectionFailure(true);
//                builder.sslSocketFactory(OkhttpFunc.createSSLSocketFactory());
//                builder.hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                });
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("userToken", userToken);
//                final RequestBody body = RequestBody.create(FinalDefine.MEDIA_TYPE_JSON, jsonObject.toString());
//                final Request request = new Request.Builder()
//                        .url(path)
//                        .header("Content-Type", "application/json")
//                        .post(body)
//                        .build();
//                builder.build().newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        Log.e(TAG, call.request().body().toString());
//                        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        try {
//                            JSONObject responseJson = JSONObject.parseObject(response.body().string());
//                            int code = Integer.valueOf(responseJson.get("code").toString());
//                            String token;
//                            Intent intent;
//                            Message msg = new Message();
//                            switch (code) {
//                                case RespCodeNumber.LOGIN_AUTO_SUCCESS:
//                                    //用户是存在的，更新数据成功；
//                                    //将token信息保存至本地
//                                    String responseJsonDataString = responseJson.getJSONObject("data").toJSONString();
//                                    userInfo = JSONObject.parseObject(responseJsonDataString, UserInfo.class);
//                                    token = userInfo.getToken();
//                                    DataUtil.SaveUserInfoData(StartActivity.this, responseJsonDataString);
//                                    DataUtil.saveToken(StartActivity.this, token);
//                                    intent = new Intent(StartActivity.this, AdvertisingActivity.class);
//                                    startActivity(intent);
//                                    msg.obj = "登录成功";
//                                    // 把消息发送到主线程，在主线程里现实Toast
//                                    handler.sendMessage(msg);
//                                    finish();
//                                    break;
//                                case RespCodeNumber.LOGIN_AUTO_FAIL:
//                                    msg.obj = "LOGIN_AUTO_FAIL";
//                                    break;
//                                case RespCodeNumber.LOGIN_AUTO_EXCEPTION:
//                                    msg.obj = "LOGIN_AUTO_EXCEPTION";
//                                    break;
//                                case RespCodeNumber.LOGIN_AUTO_WRONG:
//                                    msg.obj = "LOGIN_AUTO_WRONG";
//                                    break;
//                            }
//                            handler.sendMessage(msg);
//                            intent = new Intent(StartActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                            finish();
//                        } catch (Exception e) {
//                            Log.e(TAG, e.toString());
//                        }
//                    }
//
//                });
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                Log.e(TAG, "onPostExecute");
//            }
//        }.execute();
//    }
}