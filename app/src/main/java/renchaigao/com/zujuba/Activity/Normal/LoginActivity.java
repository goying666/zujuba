package renchaigao.com.zujuba.Activity.Normal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.dao.User;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.Activity.BaseActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.PropertiesConfig;
import renchaigao.com.zujuba.util.SecurityFunc;
import renchaigao.com.zujuba.util.http.ApiService;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {
    private final String TAG = "LoginActivity";

    private TextInputEditText login_telephone_TextInputEditText, login_pwd_TextInputEditText, login_yzm_TextInputEditText;
    private TextInputLayout login_telephone_TextInputLayout, login_pwd_TextInputLayout, login_yzm_TextInputLayout;
    private Button login_enter_button;
    private boolean telephoneChangeFlag, pwdChangeFlag;
    private User userApp;
    private TextView login_join_app, login_yzm_text, login_repwd_text, login_recode_text;
    private Integer sendCode = 0;
    private ImageView login_delete_telephone;
    private ProgressBar join_progressBar;
    private TimeCount time;
    private String verCode = "NULL";//验证码；
    private String pageClass;
    private String unionID;
    private String telephoneNumber = "NULL", userPWD = "NULL";

    // Handler内部类，它的引用在子线程中被使用，发送mesage，被handlerMesage方法接收
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            String str = (String) msg.obj;
            Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
        }

        ;
    };

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setClickPart();
//    }

    @Override
    protected void InitView() {
        login_join_app = findViewById(R.id.login_join_app);
        login_yzm_text = findViewById(R.id.login_yzm_text);
        login_repwd_text = findViewById(R.id.login_repwd_text);
        login_recode_text = findViewById(R.id.login_recode_text);
        login_enter_button = findViewById(R.id.login_enter_button);
        login_telephone_TextInputLayout = findViewById(R.id.login_telephone_TextInputLayout);
        login_pwd_TextInputLayout = findViewById(R.id.login_pwd_TextInputLayout);
        login_telephone_TextInputEditText = findViewById(R.id.login_telephone_TextInputEditText);
        login_pwd_TextInputEditText = findViewById(R.id.login_pwd_TextInputEditText);
        login_yzm_TextInputLayout = findViewById(R.id.login_yzm_TextInputLayout);
        login_yzm_TextInputEditText = findViewById(R.id.login_yzm_TextInputEditText);
        login_yzm_TextInputLayout.setVisibility(View.GONE);
        login_recode_text.setVisibility(View.GONE);
        login_delete_telephone = findViewById(R.id.login_delete_telephone);
    }

    @SuppressLint({"HardwareIds", "MissingPermission"})
    @Override
    protected void InitData() {
        userApp = new User();
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        unionID = tm.getDeviceId().toString();
        userApp.setUniqueId(unionID);
        pageClass = "S";// “S”：密码登录；“Y”：验证码登录；“Z”：注册界面；
        time = new TimeCount(30000, 1000);
    }

    @Override
    protected void InitOther() {
        login_delete_telephone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                login_telephone_TextInputEditText.setText("");
            }
        });

//获取验证码请求
        login_recode_text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//检查手机号 和 密码 的基本正误；
                if (CheckBasicInfoIsRight(telephoneNumber, userPWD, null)) {
                    time.start();
                    if (sendRequestReCodeFlag) {
//发送验证码请求
                        addUser(userApp, "Y");
                    } else {
                        Toast.makeText(LoginActivity.this, "两次验证码请求至少间隔30秒哦~~", Toast.LENGTH_LONG).show();
                    }
                } else {

                }
            }
        });

//登录/注册 按键点击事件设置
        login_enter_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.requestFocus();
                switch (pageClass) {
                    case "S"://密码登录
//检查手机号和 密码的基本正误
                        if (CheckBasicInfoIsRight(telephoneNumber, userPWD, null)) {
                            addUser(userApp, "S");
                        }
                        break;
                    case "Z"://注册场景
//检查手机号 、 密码 和 验证码的基本正误；
                        if (CheckBasicInfoIsRight(telephoneNumber, userPWD, verCode)) {
                            addUser(userApp, "Z");
                        }
                        break;
                }
            }
        });

//验证码监听部分
        login_yzm_TextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!SecurityFunc.CheckVerCodeIsRight(s.toString())) {
                    login_yzm_TextInputLayout.setError("验证码格式错误：0000~9999");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!SecurityFunc.CheckVerCodeIsRight(s.toString())) {
                    login_yzm_TextInputLayout.setError("验证码格式错误：0000~9999");
                } else {
                    verCode = s.toString();
                    login_yzm_TextInputLayout.setError("");
                }
            }
        });

//密码监听部分
        login_pwd_TextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (SecurityFunc.checkPWDisOk(s.toString())) {
                    pwdChangeFlag = true;
                } else {
                    pwdChangeFlag = false;
                    login_pwd_TextInputLayout.setError("密码格式：6至8位数字");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (SecurityFunc.checkPWDisOk(s.toString()) && pwdChangeFlag) {
                    userApp.setUserPWD(s.toString());
                    userPWD = s.toString();
                    login_pwd_TextInputLayout.setError("");
                } else {
                    pwdChangeFlag = false;
                    login_pwd_TextInputLayout.setError("密码格式：6至8位数字");
                }
            }
        });

//电话号码监听部分
        login_telephone_TextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                login_telephone_TextInputLayout.setError("请输入正确的11位手机号码");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0)
                    login_delete_telephone.setVisibility(View.VISIBLE);
                else
                    login_delete_telephone.setVisibility(View.GONE);
/*待补充：电话号码的识别*/
                if (SecurityFunc.CheckTelephoneNumberIsRight(s.toString())) {
                    telephoneChangeFlag = true;
//login_telephone_TextInputLayout.setError("格式正确");
                } else {
                    login_telephone_TextInputLayout.setError("请输入正确的11位号码");
                    telephoneChangeFlag = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (SecurityFunc.CheckTelephoneNumberIsRight(s.toString()) && telephoneChangeFlag) {
                    telephoneNumber = s.toString();
                    userApp.setTelephone(s.toString());
                    login_telephone_TextInputLayout.setError("");
                } else {
                    login_telephone_TextInputLayout.setError("请输入正确的11位号码");
                    telephoneChangeFlag = false;
                }
            }
        });

        login_join_app.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (pageClass) {
                    case "S":
                        pageClass = "Z";
                        login_join_app.setText("手机登录");
                        login_enter_button.setText("注册");
                        login_recode_text.setVisibility(View.VISIBLE);
                        login_yzm_TextInputLayout.setVisibility(View.VISIBLE);
                        login_repwd_text.setVisibility(View.GONE);
                        break;
                    case "Z":
                        pageClass = "S";
                        login_join_app.setText("手机注册");
                        login_enter_button.setText("登录");
                        login_recode_text.setVisibility(View.GONE);
                        login_yzm_TextInputLayout.setVisibility(View.GONE);
                        login_repwd_text.setVisibility(View.VISIBLE);
                        break;
                }
                login_pwd_TextInputEditText.setText("");
                login_pwd_TextInputLayout.setError("");
            }
        });
//login_yzm_text.setOnClickListener(new OnClickListener() {
//@Override
//public void onClick(View v) {
//switch (pageClass) {
//case "Z":
//pageClass = "Y";
//login_yzm_text.setText("密码登录");
//login_enter_button.setText("验证码登录");
//login_pwd_TextInputLayout.setHint("请输入获取到的验证码");
//login_join_app.setVisibility(View.GONE);
//login_repwd_text.setVisibility(View.GONE);
//login_recode_text.setVisibility(View.VISIBLE);
//break;
////case "Y":
////pageClass = "S";
////login_yzm_text.setText("验证码登录");
////login_enter_button.setText("密码登录");
////login_pwd_TextInputLayout.setHint("请输入密码");
////login_join_app.setVisibility(View.VISIBLE);
////login_repwd_text.setVisibility(View.VISIBLE);
////login_recode_text.setVisibility(View.GONE);
////break;
//}
//login_pwd_TextInputEditText.setText("");
//login_pwd_TextInputLayout.setError("");
//}
//});
        login_repwd_text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//修改密码，去修改密码的活动内；
            }
        });

//        InitRxJavaAndRetrofit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    ApiService apiService;

//    private void InitRxJavaAndRetrofit() {
////        OkHttpUtil okHttpUtil = new OkHttpUtil();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .client(OkHttpUtil.builder.build())
////                .client(okHttpUtil.getBuilder().build())
//                .baseUrl(PropertiesConfig.userServerUrl)
//                //设置数据解析器
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build();
//        apiService = retrofit.create(ApiService.class);
//    }

//    private void startPage() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(3000); //此界面沉睡5秒
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }

    private void initData() {

    }

    //private void setToolBar() {
//ActionBar actionBar = getSupportActionBar();
//if (actionBar != null) {
//actionBar.hide();
//}
//}
    private void initView() {

//join_progressBar = findViewById(R.id.join_progressBar);
//join_progressBar.setMax(100);//设置最大进度
//join_progressBar.setProgress(30);//设置当前进度
//join_progressBar.setSecondaryProgress(40);//设置第二进度
//join_progressBar.setVisibility(View.GONE);
    }

    private Boolean CheckBasicInfoIsRight(String telephone, String userPWD, String reCode) {
        if (telephone != null && !SecurityFunc.CheckTelephoneNumberIsRight(telephone)) {
            Toast.makeText(LoginActivity.this, "手机号码书写有误~~", Toast.LENGTH_LONG).show();
            return false;
        }
        if (userPWD != null && !SecurityFunc.checkPWDisOk(userPWD)) {
//if (userPWD != null && !PatternUtil.strMatcher(userPWD, PatternUtil.FUNC_TELEPHONE_NUMBER)) {
            Toast.makeText(LoginActivity.this, "密码格式有误~~", Toast.LENGTH_LONG).show();
            return false;
        }
        if (reCode != null && !SecurityFunc.CheckVerCodeIsRight(reCode)) {
            Toast.makeText(LoginActivity.this, "验证码格式有误~~", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void setClickPart() {

    }

//private boolean checkUserSendInfo() {
//String userTelephone = login_telephone_TextInputEditText.getText().toString();
//String userPwd = login_pwd_TextInputEditText.getText().toString();
//if (null != userTelephone && null != userPwd) {
//userLogin.setUserpwd(userPwd);
//userLogin.setUserpwd(userTelephone);
//return true;
//} else {
//Log.e(TAG, "checkUserSendInfo : wrong telephone or password");
//return false;
//}
//}

    private void addUser(User users, String mode) {
        String str1 = null, str2 = null, str3 = null, str4 = null;
        switch (mode) {
            case "S"://密码登录
                str1 = "login";
                str2 = "secret";
                str3 = telephoneNumber;
                str4 = "null";
                break;
            case "Y"://获取验证码
                str1 = "signin";
                str2 = telephoneNumber;
                str3 = verCode;
                str4 = "vercode";
                break;
            case "Z"://注册请求
                str1 = "signin";
                str2 = telephoneNumber;
                str3 = verCode;
                str4 = "signin";
                break;
        }
        addSubscribe(RetrofitServiceManager.getInstance(PropertiesConfig.userServerUrl).creat(ApiService.class)
        .UserServicePost(str1, str2, str3, str4, JSONObject.parseObject(JSONObject.toJSONString(users),JSONObject.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(this) {

                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            JSONObject responseJsonData;
                            Intent intent;
                            Message msg = new Message();
                            switch (code) {
//返回分为三个类型：验证码请求，注册结果返回 和 登录返回；
//part1：验证码请求；
                                case RespCodeNumber.VERCODE_SUCCESS:
                                    msg.obj = "VERCODE_SUCCESS";
                                    break;

                                case RespCodeNumber.VERCODE_FAIL:
                                    msg.obj = "VERCODE_FAIL";
                                    break;

                                case RespCodeNumber.VERCODE_REQUEST_TOO_MUCH:
                                    msg.obj = "VERCODE_REQUEST_TOO_MUCH";
                                    break;

                                case RespCodeNumber.VERCODE_BUSY:
                                    msg.obj = "VERCODE_BUSY";
                                    break;

                                case RespCodeNumber.VERCODE_EXCEPTION:
                                    msg.obj = "VERCODE_EXCEPTION";
                                    break;

                                case RespCodeNumber.VERCODE_TIME_OUT:
                                    msg.obj = "VERCODE_TIME_OUT";
                                    break;

//singin part：注册部分
                                case RespCodeNumber.SIGNIN_SUCCESS:
                                    //创建新用户账号，成功
                                    //将token信息保存至本地
                                    responseJsonData = (JSONObject) responseJson.getJSONObject("data");
                                    DataUtil.SaveUserInfoData(LoginActivity.this, responseJson.getString("data"));
                                    DataUtil.saveToken(LoginActivity.this, responseJsonData.getString("token"));
                                    intent = new Intent(LoginActivity.this, AdvertisingActivity.class);
                                    msg.obj = "SIGNIN_SUCCESS";
                                    startActivity(intent);
                                    finish();
                                    break;
                                case RespCodeNumber.SIGNIN_FAIL:
                                    msg.obj = "SIGNIN_FAIL";
                                    break;
                                case RespCodeNumber.SIGNIN_EXCEPTION:
                                    msg.obj = "SIGNIN_EXCEPTION";
                                    break;
                                case RespCodeNumber.SIGNIN_WRONG:
                                    msg.obj = "SIGNIN_WRONG";
                                    break;

//Login Part：登录部分
                                case RespCodeNumber.LOGIN_AUTO_SUCCESS:
                                    //用户是存在的，更新数据成功；
                                    //将token信息保存至本地
                                    responseJsonData = (JSONObject) responseJson.getJSONObject("data");
                                    DataUtil.SaveUserInfoData(LoginActivity.this, responseJson.getString("data"));
                                    DataUtil.saveToken(LoginActivity.this, responseJsonData.getString("token"));
                                    intent = new Intent(LoginActivity.this, AdvertisingActivity.class);
                                    msg.obj = "LOGIN_AUTO_SUCCESS";
                                    startActivity(intent);
                                    finish();
                                    break;
                                case RespCodeNumber.LOGIN_SECRET_FAIL:
                                    msg.obj = "LOGIN_SECRET_FAIL";
                                    break;
                                case RespCodeNumber.LOGIN_AUTO_EXCEPTION:
                                    msg.obj = "LOGIN_AUTO_EXCEPTION";
                                    break;
                                case RespCodeNumber.LOGIN_SECRET_WRONG:
                                    msg.obj = "LOGIN_SECRET_WRONG";
                                    break;
                            }
                            handler.sendMessage(msg);
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }

                    }

                    @Override
                    protected void onSuccess(ResponseEntity responseEntity) {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete:");

                    }
                }));
    }
//    @SuppressLint("StaticFieldLeak")
//    private void addUser(final User userApp, final String mode) {
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
//                String path = PropertiesConfig.userServerUrl;
//                switch (mode) {
//                    case "S"://密码登录
//                        path += "login/secret/" + telephoneNumber + "/null";
//                        break;
//                    case "Y"://获取验证码
//                        path += "signin/" + telephoneNumber + "/" + verCode + "/vercode";
//                        break;
//                    case "Z"://注册请求
//                        path += "signin/" + telephoneNumber + "/" + verCode + "/signin";
//                        break;
//                }
//                OkHttpClient.Builder builder = new OkHttpClient.Builder();
//                OkhttpFunc okhttpFunc = new OkhttpFunc();
//                builder.sslSocketFactory(okhttpFunc.createSSLSocketFactory());
//                builder.hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                });
//                final RequestBody body = RequestBody.create(FinalDefine.MEDIA_TYPE_JSON, JSONObject.toJSONString(userApp));
//                final Request request = new Request.Builder()
//                        .url(path)
//                        .header("Content-Type", "application/json")
//                        .post(body)
//                        .build();
//                builder.build().newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        Log.e(TAG, call.request().body().toString());
////join_progressBar.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        try {
//                            JSONObject responseJson = JSONObject.parseObject(response.body().string());
//                            int code = Integer.valueOf(responseJson.get("code").toString());
//                            JSONObject responseJsonData;
//                            String token;
//                            SharedPreferences.Editor editor;
//                            Intent intent;
//                            Message msg = new Message();
//                            switch (code) {
////返回分为三个类型：验证码请求，注册结果返回 和 登录返回；
//
////part1：验证码请求；
//                                case RespCodeNumber.VERCODE_SUCCESS:
//                                    msg.obj = "VERCODE_SUCCESS";
//                                    break;
//
//                                case RespCodeNumber.VERCODE_FAIL:
//                                    msg.obj = "VERCODE_FAIL";
//                                    break;
//
//                                case RespCodeNumber.VERCODE_REQUEST_TOO_MUCH:
//                                    msg.obj = "VERCODE_REQUEST_TOO_MUCH";
//                                    break;
//
//                                case RespCodeNumber.VERCODE_BUSY:
//                                    msg.obj = "VERCODE_BUSY";
//                                    break;
//
//                                case RespCodeNumber.VERCODE_EXCEPTION:
//                                    msg.obj = "VERCODE_EXCEPTION";
//                                    break;
//
//                                case RespCodeNumber.VERCODE_TIME_OUT:
//                                    msg.obj = "VERCODE_TIME_OUT";
//                                    break;
//
////singin part：注册部分
//                                case RespCodeNumber.SIGNIN_SUCCESS:
//                                    //创建新用户账号，成功
//                                    //将token信息保存至本地
//                                    responseJsonData = (JSONObject) responseJson.getJSONObject("data");
//                                    DataUtil.SaveUserInfoData(LoginActivity.this, responseJson.getString("data"));
//                                    DataUtil.saveToken(LoginActivity.this, responseJsonData.getString("token"));
//                                    intent = new Intent(LoginActivity.this, AdvertisingActivity.class);
//                                    msg.obj = "SIGNIN_SUCCESS";
//                                    startActivity(intent);
//                                    finish();
//                                    break;
//                                case RespCodeNumber.SIGNIN_FAIL:
//                                    msg.obj = "SIGNIN_FAIL";
//                                    break;
//                                case RespCodeNumber.SIGNIN_EXCEPTION:
//                                    msg.obj = "SIGNIN_EXCEPTION";
//                                    break;
//                                case RespCodeNumber.SIGNIN_WRONG:
//                                    msg.obj = "SIGNIN_WRONG";
//                                    break;
//
////Login Part：登录部分
//                                case RespCodeNumber.LOGIN_AUTO_SUCCESS:
//                                    //用户是存在的，更新数据成功；
//                                    //将token信息保存至本地
//                                    responseJsonData = (JSONObject) responseJson.getJSONObject("data");
//                                    DataUtil.SaveUserInfoData(LoginActivity.this, responseJson.getString("data"));
//                                    DataUtil.saveToken(LoginActivity.this, responseJsonData.getString("token"));
//                                    intent = new Intent(LoginActivity.this, AdvertisingActivity.class);
//                                    msg.obj = "LOGIN_AUTO_SUCCESS";
//                                    startActivity(intent);
//                                    finish();
//                                    break;
//                                case RespCodeNumber.LOGIN_SECRET_FAIL:
//                                    msg.obj = "LOGIN_SECRET_FAIL";
//                                    break;
//                                case RespCodeNumber.LOGIN_AUTO_EXCEPTION:
//                                    msg.obj = "LOGIN_AUTO_EXCEPTION";
//                                    break;
//                                case RespCodeNumber.LOGIN_SECRET_WRONG:
//                                    msg.obj = "LOGIN_SECRET_WRONG";
//                                    break;
//                            }
//                            handler.sendMessage(msg);
//                            Log.e(TAG, response.body().string());
//                        } catch (Exception e) {
//                            Log.e(TAG, e.toString());
//                        }
//
//                    }
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

    private Boolean sendRequestReCodeFlag = true;

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            sendRequestReCodeFlag = false;
            login_recode_text.setBackgroundColor(Color.parseColor("#B6B6D8"));
            login_recode_text.setClickable(false);
            login_recode_text.setText("(" + millisUntilFinished / 1000 + ") 秒后可重新发送");
        }

        @Override
        public void onFinish() {
            sendRequestReCodeFlag = true;
            login_recode_text.setText("重新获取验证码");
            login_recode_text.setClickable(true);
            login_recode_text.setBackgroundColor(Color.parseColor("#4EB84A"));

        }
    }
}

