package renchaigao.com.zujuba.Activity.Normal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

import java.io.IOException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.renchaigao.zujuba.dao.User;

import renchaigao.com.zujuba.Activity.AdvertisingActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.FinalDefine;
import renchaigao.com.zujuba.util.OkhttpFunc;
import renchaigao.com.zujuba.util.PropertiesConfig;
import renchaigao.com.zujuba.util.SecurityFunc;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private final String TAG = "LoginActivity";

    private TextInputEditText login_telephone_TextInputEditText, login_pwd_TextInputEditText;
    private TextInputLayout login_telephone_TextInputLayout, login_pwd_TextInputLayout;
    private Button login_enter_button;
    private boolean telephoneChangeFlag, pwdChangeFlag;
    private User userApp;
    private TextView login_join_app, login_yzm_text, login_repwd_text, login_recode_text;
    private Integer sendCode = 0;
    private ImageView login_delete_telephone;
    private ProgressBar join_progressBar;
    private TimeCount time;
    private String reCode;//验证码；
    private String pageClass;

    // Handler内部类，它的引用在子线程中被使用，发送mesage，被handlerMesage方法接收
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            String str = (String) msg.obj;
            Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
        }

        ;
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setToolBar();
        startPage();
        initData();
        initView();
        initClick();
        setClickPart();
    }

    private void startPage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);       //此界面沉睡5秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void initData() {
        userApp = new User();
        pageClass = "S";// “S”：密码登录；“Y”：验证码登录；“Z”：注册界面；
        time = new TimeCount(60000, 1000);


    }
    private void setToolBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
    private void initView() {
        login_join_app = findViewById(R.id.login_join_app);
        login_yzm_text = findViewById(R.id.login_yzm_text);
        login_repwd_text = findViewById(R.id.login_repwd_text);
        login_recode_text = findViewById(R.id.login_recode_text);
        login_enter_button = findViewById(R.id.login_enter_button);
        login_telephone_TextInputLayout = findViewById(R.id.login_telephone_TextInputLayout);
        login_pwd_TextInputLayout = findViewById(R.id.login_pwd_TextInputLayout);
//        login_pwd_TextInputLayout.setPasswordVisibilityToggleEnabled(true);
        login_telephone_TextInputEditText = findViewById(R.id.login_telephone_TextInputEditText);
        login_pwd_TextInputEditText = findViewById(R.id.login_pwd_TextInputEditText);
        login_delete_telephone = findViewById(R.id.login_delete_telephone);
//        join_progressBar = findViewById(R.id.join_progressBar);
//        join_progressBar.setMax(100);//设置最大进度
//        join_progressBar.setProgress(30);//设置当前进度
//        join_progressBar.setSecondaryProgress(40);//设置第二进度
//        join_progressBar.setVisibility(View.GONE);
    }
    private void initClick() {
        login_delete_telephone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                login_telephone_TextInputEditText.setText("");
            }
        });
        login_recode_text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                time.start();
            }
        });
        login_enter_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.requestFocus();
                switch (pageClass) {
                    case "S"://密码登录
//                        join_progressBar.setVisibility(View.VISIBLE);
                        addUser(userApp, "S");
                        break;
                    case "Y"://验证码登录
//                        判断是否发送过
//                        if (sendCode == 0) {
                            //发送过验证码，改写各种状态，等待接收
                            sendCode++;
//                        }
//                        判断是否在等待时间内

//                        判断是否超过数值

                        break;
                    case "Z"://注册场景
//                        if (sendCode == 0) {
                            //发送过验证码，改写各种状态，等待接收
                            addUser(userApp, "Z");
                            time.start();
                            sendCode++;
//                        }
                        break;
                }
            }
        });

        login_pwd_TextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*  待补充：密码和验证码的识别*/
                switch (pageClass) {
                    case "S"://密码登录
                        if (s.length() == 0) {
                            login_pwd_TextInputLayout.setError("密码格式：6至20位字符,区分大小写");
                        } else if (s.length() < 6 || s.length() > 20) {
                            pwdChangeFlag = false;
                        } else {
                            pwdChangeFlag = true;
                        }
                        break;
                    case "Y"://验证码登录
                        break;
                    case "Z"://注册场景
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                switch (pageClass) {
                    case "S"://密码登录
                        if (SecurityFunc.checkPWDisOk(s.toString()) && pwdChangeFlag) {
                            userApp.setUserPWD(s.toString());
                            login_pwd_TextInputLayout.setError("格式正确");
                        } else
                            login_pwd_TextInputLayout.setError("密码格式：6至20位字符,区分大小写");
                        break;
                    case "Y"://验证码登录
                        reCode = s.toString();
                        break;
                    case "Z"://注册场景
                        reCode = s.toString();
                        break;
                }
            }
        });

//        电话号码监听部分
        login_telephone_TextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                login_telephone_TextInputLayout.setError("请输入正确的11位号码");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0)
                    login_delete_telephone.setVisibility(View.VISIBLE);
                else
                    login_delete_telephone.setVisibility(View.GONE);
                /*  待补充：电话号码的识别*/
                if (s.length() < 11) {
                    login_telephone_TextInputLayout.setError("请输入正确的11位号码");
                    telephoneChangeFlag = false;
                } else if (s.length() > 11) {
                    login_telephone_TextInputLayout.setError("号码已超过11位，请修改");
                    telephoneChangeFlag = false;
                } else {
                    telephoneChangeFlag = true;
                    login_telephone_TextInputLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (telephoneChangeFlag)
                    userApp.setTelephone(s.toString());
            }
        });
    }
    private void setClickPart() {
        login_join_app.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (pageClass) {
                    case "S":
                        pageClass = "Z";
                        login_join_app.setText("手机登录");
                        login_enter_button.setText("手机注册");
                        login_pwd_TextInputLayout.setHint("请输入获取到的验证码");
                        login_yzm_text.setVisibility(View.GONE);
                        login_repwd_text.setVisibility(View.GONE);
                        login_recode_text.setVisibility(View.VISIBLE);
                        break;
                    case "Z":
                        pageClass = "S";
                        login_join_app.setText("手机注册");
                        login_enter_button.setText("密码登录");
                        login_pwd_TextInputLayout.setHint("请输入密码");
                        login_yzm_text.setVisibility(View.VISIBLE);
                        login_repwd_text.setVisibility(View.VISIBLE);
                        login_recode_text.setVisibility(View.GONE);
                        break;
                }
                login_pwd_TextInputEditText.setText("");
                login_pwd_TextInputLayout.setError("");
            }
        });
        login_yzm_text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (pageClass) {
                    case "S":
                        pageClass = "Y";
                        login_yzm_text.setText("密码登录");
                        login_enter_button.setText("验证码登录");
                        login_pwd_TextInputLayout.setHint("请输入获取到的验证码");
                        login_join_app.setVisibility(View.GONE);
                        login_repwd_text.setVisibility(View.GONE);
                        login_recode_text.setVisibility(View.VISIBLE);
                        break;
                    case "Y":
                        pageClass = "S";
                        login_yzm_text.setText("验证码登录");
                        login_enter_button.setText("密码登录");
                        login_pwd_TextInputLayout.setHint("请输入密码");
                        login_join_app.setVisibility(View.VISIBLE);
                        login_repwd_text.setVisibility(View.VISIBLE);
                        login_recode_text.setVisibility(View.GONE);
                        break;
                }
                login_pwd_TextInputEditText.setText("");
                login_pwd_TextInputLayout.setError("");
            }
        });
        login_repwd_text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                修改密码，去修改密码的活动内；
            }
        });
    }

//    private boolean checkUserSendInfo() {
//        String userTelephone = login_telephone_TextInputEditText.getText().toString();
//        String userPwd = login_pwd_TextInputEditText.getText().toString();
//        if (null != userTelephone && null != userPwd) {
//            userLogin.setUserpwd(userPwd);
//            userLogin.setUserpwd(userTelephone);
//            return true;
//        } else {
//            Log.e(TAG, "checkUserSendInfo : wrong telephone or password");
//            return false;
//        }
//    }

    @SuppressLint("StaticFieldLeak")
    private void addUser(final User userApp, final String mode) {
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
                String path = PropertiesConfig.userServerUrl + "login/";
                switch (mode){
                    case "S":
                        path += "secret/0";
                        break;
                    case "Y":
                        path = path + "vercode/"+reCode;
                        break;
                    case "Z":
                        path = path + "add/"+reCode;
                        break;
                }
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                OkhttpFunc okhttpFunc = new OkhttpFunc();
                builder.sslSocketFactory(okhttpFunc.createSSLSocketFactory());
                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
                String jsonStr = JSONObject.toJSONString(userApp);
                final RequestBody body = RequestBody.create(FinalDefine.MEDIA_TYPE_JSON, jsonStr);
                final Request request = new Request.Builder()
                        .url(path)
                        .header("Content-Type", "application/json")
                        .post(body)
                        .build();
                builder.build().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, call.request().body().toString());
//                        join_progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(response.body().string());
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            JSONObject responseJsonData ;
                            String token;
                            SharedPreferences.Editor editor;
                            Intent intent;
                            Message msg = new Message();
                            switch (code) {
                                //                                    用户首次登陆系统进行创建账号，
                                case 500:
                                    Toast.makeText(LoginActivity.this, "错误500", Toast.LENGTH_LONG).show();
                                    break;
                                case 0://创建新用户账号，成功
                                    //                                    将token信息保存至本地
                                    responseJsonData = (JSONObject) responseJson.getJSONObject("data");
                                    token = responseJsonData.get("token").toString();
                                    editor = getSharedPreferences("userData", MODE_PRIVATE).edit();
                                    editor.putString("token", token);
                                    editor.putString("userId", responseJsonData.get("id").toString());
                                    editor.putString("user", responseJsonData.toJSONString());
                                    editor.apply();
                                    intent = new Intent(LoginActivity.this, AdvertisingActivity.class);
                                    intent.putExtra("token", token);
                                    intent.putExtra("userId", responseJsonData.get("id").toString());
                                    intent.putExtra("user", responseJsonData.toJSONString());
                                    startActivity(intent);
                                    finish();
                                    msg.obj = "登录成功0";
                                    // 把消息发送到主线程，在主线程里现实Toast
                                    handler.sendMessage(msg);
                                    finish();
                                    break;
                                case 1: //在数据库中更新用户数据出错；
                                    Toast.makeText(LoginActivity.this, "在数据库中更新用户数据出错", Toast.LENGTH_LONG).show();
                                    msg.obj = "更新数据失败";
                                    // 把消息发送到主线程，在主线程里现实Toast
                                    handler.sendMessage(msg);
                                    break;
                                case 1001: //用户是存在的，更新数据成功；
                                    //将token信息保存至本地
                                    responseJsonData = (JSONObject) responseJson.getJSONObject("data");
                                    token = responseJsonData.get("token").toString();
                                    editor = getSharedPreferences("userData", MODE_PRIVATE).edit();
                                    editor.putString("token", token);
                                    editor.putString("userId", responseJsonData.get("id").toString());
                                    editor.putString("user", responseJsonData.toJSONString());
                                    editor.apply();
                                    intent = new Intent(LoginActivity.this, AdvertisingActivity.class);
                                    intent.putExtra("token", token);
                                    intent.putExtra("userId", responseJsonData.get("id").toString());
                                    intent.putExtra("user", responseJsonData.toJSONString());
                                    startActivity(intent);
                                    msg.obj = "登录成功1001";
                                    // 把消息发送到主线程，在主线程里现实Toast
                                    handler.sendMessage(msg);
                                    finish();
                                    break;
                                case -1001://用户登录，密码错误；
                                    msg.obj = "密码错误";
                                    // 把消息发送到主线程，在主线程里现实Toast
                                    handler.sendMessage(msg);
                                    break;
                                case -1002: //创建新用户，电话号码错误
                                    msg.obj = "创建新用户，号码错误";
                                    // 把消息发送到主线程，在主线程里现实Toast
                                    handler.sendMessage(msg);
//                                    Toast.makeText(LoginActivity.this, "创建新用户，电话号码错误", Toast.LENGTH_LONG).show();
                                    break;
                                case -1003: //用户是存在的，本地的TOKEN超时，需要重新登录；
                                    msg.obj = "Token超时，需重新登录";
                                    // 把消息发送到主线程，在主线程里现实Toast
                                    handler.sendMessage(msg);
                                    Toast.makeText(LoginActivity.this, "本地的TOKEN超时，需要重新登录", Toast.LENGTH_LONG).show();
                                    break;
                                case -1004: //用户是存在的，本地的TOKEN错误；
                                    msg.obj = "Token错误，需重新登录";
                                    // 把消息发送到主线程，在主线程里现实Toast
                                    handler.sendMessage(msg);
                                    Toast.makeText(LoginActivity.this, "本地的TOKEN错误", Toast.LENGTH_LONG).show();
                                    break;
                                case -1009: //用户是存在的，本地的TOKEN错误；
                                    msg.obj = "未注册用户（手机号）";
                                    // 把消息发送到主线程，在主线程里现实Toast
                                    handler.sendMessage(msg);
                                    Toast.makeText(LoginActivity.this, "该手机号还未注册", Toast.LENGTH_LONG).show();
                                    break;
                            }
                            //                        switch (jsonObject1)
                            Log.e(TAG, response.body().string());

                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }

//                        join_progressBar.setVisibility(View.GONE);
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

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            login_recode_text.setBackgroundColor(Color.parseColor("#B6B6D8"));
            login_recode_text.setClickable(false);
            login_recode_text.setText("(" + millisUntilFinished / 1000 + ") 秒后可重新发送");
        }

        @Override
        public void onFinish() {
            login_recode_text.setText("重新获取验证码");
            login_recode_text.setClickable(true);
            login_recode_text.setBackgroundColor(Color.parseColor("#4EB84A"));

        }
    }
}

