package renchaigao.com.zujuba.Activity.User;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.dao.User;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.Activity.BaseActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.PropertiesConfig;
import renchaigao.com.zujuba.util.http.ApiService;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;

public class UserSettingActivity extends BaseActivity {

    private final String TAG = "UserSettingActivity";
    private ConstraintLayout acivity_user_setting_ConstraintLayout_nickNamePart,
            acivity_user_setting_ConstraintLayout_imagePart,
            acivity_user_setting_ConstraintLayout_genderPart,
            acivity_user_setting_ConstraintLayout_agePart,
            acivity_user_setting_ConstraintLayout_telephoneNumberPart,
            acivity_user_setting_ConstraintLayout_realNamePart,
            acivity_user_setting_ConstraintLayout_cardNumberPart,
            acivity_user_setting_ConstraintLayout_systemPart1,
            acivity_user_setting_ConstraintLayout_systemPart2,
            acivity_user_setting_ConstraintLayout_systemPart3;
    private TextView acivity_user_setting_ConstraintLayout_nickName,
            activity_user_setting_gender_textView,
            acivity_user_setting_ConstraintLayout_age,
            activity_user_setting_phoneNumber_textView,
            activity_user_setting_realName_textView,
            activity_user_setting_cardNumber_textView,
            activity_user_setting_softwareNumber_textView,
            activity_user_setting_save;
    private ImageView activity_user_setting_toolbar_back;
    private CircleImageView acivity_user_setting_ConstraintLayout_image;

    private AlertDialog.Builder alertDialog;
    private EditText editText;
    private User user;
    private UserInfo userInfo;
    final String[] ageItems = {"95后", "90后", "00后", "90前"},
            genderItems = {"男", "女"};

    // Handler内部类，它的引用在子线程中被使用，发送mesage，被handlerMesage方法接收
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String str = (String) msg.obj;
            Toast.makeText(UserSettingActivity.this, str, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void InitView() {

        InitViewAndData();
    }

    @Override
    protected void InitData() {

    }

    @Override
    protected void InitOther() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_setting;
    }

    ApiService apiService;

//    private void InitRxJavaAndRetrofit() {
////        OkHttpUtil okHttpUtil = new OkHttpUtil();
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


    private void InitViewAndData() {
        acivity_user_setting_ConstraintLayout_nickNamePart = findViewById(R.id.acivity_user_setting_ConstraintLayout_nickNamePart);
        acivity_user_setting_ConstraintLayout_imagePart = findViewById(R.id.acivity_user_setting_ConstraintLayout_imagePart);
        acivity_user_setting_ConstraintLayout_genderPart = findViewById(R.id.acivity_user_setting_ConstraintLayout_genderPart);
        acivity_user_setting_ConstraintLayout_agePart = findViewById(R.id.acivity_user_setting_ConstraintLayout_agePart);
        acivity_user_setting_ConstraintLayout_telephoneNumberPart = findViewById(R.id.acivity_user_setting_ConstraintLayout_telephoneNumberPart);
        acivity_user_setting_ConstraintLayout_realNamePart = findViewById(R.id.acivity_user_setting_ConstraintLayout_realNamePart);
        acivity_user_setting_ConstraintLayout_cardNumberPart = findViewById(R.id.acivity_user_setting_ConstraintLayout_cardNumberPart);
        acivity_user_setting_ConstraintLayout_systemPart1 = findViewById(R.id.acivity_user_setting_ConstraintLayout_systemPart1);
        acivity_user_setting_ConstraintLayout_systemPart2 = findViewById(R.id.acivity_user_setting_ConstraintLayout_systemPart2);
        acivity_user_setting_ConstraintLayout_systemPart3 = findViewById(R.id.acivity_user_setting_ConstraintLayout_systemPart3);
        acivity_user_setting_ConstraintLayout_nickName = findViewById(R.id.acivity_user_setting_ConstraintLayout_nickName);
        activity_user_setting_gender_textView = findViewById(R.id.activity_user_setting_gender_textView);
        acivity_user_setting_ConstraintLayout_age = findViewById(R.id.acivity_user_setting_ConstraintLayout_age);
        activity_user_setting_phoneNumber_textView = findViewById(R.id.activity_user_setting_phoneNumber_textView);
        activity_user_setting_realName_textView = findViewById(R.id.activity_user_setting_realName_textView);
        activity_user_setting_cardNumber_textView = findViewById(R.id.activity_user_setting_cardNumber_textView);
        activity_user_setting_softwareNumber_textView = findViewById(R.id.activity_user_setting_softwareNumber_textView);
        acivity_user_setting_ConstraintLayout_image = findViewById(R.id.acivity_user_setting_ConstraintLayout_image);
        activity_user_setting_save = findViewById(R.id.activity_user_setting_save);
        activity_user_setting_toolbar_back = findViewById(R.id.activity_user_setting_toolbar_back);
        activity_user_setting_save.setVisibility(View.GONE);
        InitViewData();
        SetClick();
    }

    private void InitViewData() {
        user = DataUtil.GetUserData(this);
        assert user != null;
        acivity_user_setting_ConstraintLayout_nickName.setText(user.getNickName());
        activity_user_setting_gender_textView.setText(user.getGender());
        acivity_user_setting_ConstraintLayout_age.setText(user.getAgeLevel());
        activity_user_setting_phoneNumber_textView.setText(user.getTelephone());
        activity_user_setting_realName_textView.setText(user.getRealName());
        activity_user_setting_cardNumber_textView.setText(user.getIdCard());
        activity_user_setting_softwareNumber_textView.setText(PropertiesConfig.SOFTWARE_CODE);
    }

    private void SetClick() {
        activity_user_setting_toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity_user_setting_save.getVisibility() == View.VISIBLE) {
//                    说明有修改操作，提示退出确认
                    alertDialog = new AlertDialog.Builder(UserSettingActivity.this);
                    alertDialog.setTitle("确认取消修改吗？");
                    alertDialog.setNegativeButton("留下修改", null);
                    alertDialog.setPositiveButton("离开此页", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(UserSettingActivity.this, "离开此页咯", Toast.LENGTH_SHORT).show();
                        }
                    });
                    alertDialog.show();
                }
            }
        });
        acivity_user_setting_ConstraintLayout_nickNamePart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(UserSettingActivity.this);
                editText = new EditText(UserSettingActivity.this);
                alertDialog.setView(editText);
                alertDialog.setNegativeButton("取消", null);
                alertDialog.setTitle("请输入昵称");
                alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user.setNickName(editText.getText().toString());
                        acivity_user_setting_ConstraintLayout_nickName.setText(editText.getText().toString());
                        activity_user_setting_save.setVisibility(View.VISIBLE);
                    }
                });
                alertDialog.show();
            }
        });
//        acivity_user_setting_ConstraintLayout_imagePart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        acivity_user_setting_ConstraintLayout_genderPart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog = new AlertDialog.Builder(UserSettingActivity.this);
//                editText = new EditText(UserSettingActivity.this);
//                alertDialog.setTitle("请选择您的性别");
//                alertDialog.setItems(genderItems, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        activity_user_setting_gender_textView.setText(genderItems[which]);
//                        user.setAgeLevel(genderItems[which]);
//                        activity_user_setting_save.setVisibility(View.VISIBLE);
//                    }
//                });
//                alertDialog.show();
//            }
//        });
//        acivity_user_setting_ConstraintLayout_agePart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog = new AlertDialog.Builder(UserSettingActivity.this);
//                editText = new EditText(UserSettingActivity.this);
//                alertDialog.setTitle("请选择您所属年龄段");
//                alertDialog.setItems(ageItems, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        acivity_user_setting_ConstraintLayout_age.setText(ageItems[which]);
//                        user.setAgeLevel(ageItems[which]);
//                        activity_user_setting_save.setVisibility(View.VISIBLE);
//                    }
//                });
//                alertDialog.show();
//            }
//        });
////        acivity_user_setting_ConstraintLayout_nickNamePart.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                alertDialog.setTitle("请输入您的手机号");
////               alertDialog.setView(editText);
////               alertDialog.setPositiveButton(new )
////                alertDialog.show();
////            }
////        });
//        acivity_user_setting_ConstraintLayout_realNamePart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog = new AlertDialog.Builder(UserSettingActivity.this);
//                editText = new EditText(UserSettingActivity.this);
//                alertDialog.setView(editText);
//                alertDialog.setTitle("请输入您的真实姓名");
//                alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        user.setRealName(editText.getText().toString());
//                        activity_user_setting_realName_textView.setText(editText.getText().toString());
//                        activity_user_setting_save.setVisibility(View.VISIBLE);
//                    }
//                });
//                alertDialog.show();
//            }
//        });
//        acivity_user_setting_ConstraintLayout_cardNumberPart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog = new AlertDialog.Builder(UserSettingActivity.this);
//                editText = new EditText(UserSettingActivity.this);
//                alertDialog.setView(editText);
//                alertDialog.setTitle("请输入您的证件号");
//                alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        user.setIdCard(editText.getText().toString());
//                        activity_user_setting_cardNumber_textView.setText(editText.getText().toString());
//                        activity_user_setting_save.setVisibility(View.VISIBLE);
//                    }
//                });
//                alertDialog.show();
//            }
//        });
        activity_user_setting_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(UserSettingActivity.this);
                alertDialog.setTitle("修改完毕，是否保存？");
                alertDialog.setNegativeButton("继续修改", null);
                alertDialog.setPositiveButton("确认保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UpdateUserInfo();
                    }
                });
                alertDialog.show();
            }
        });
//        acivity_user_setting_ConstraintLayout_nickNamePart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.setTitle("请输入昵称");
//                alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        Toast.makeText(UserSettingActivity.this,
//                                editText.getText().toString(),
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//                alertDialog.show();
//            }
//        });

    }

    private void UpdateUserInfo() {

        RetrofitServiceManager.getInstance().SetRetrofit(PropertiesConfig.userServerUrl);
        addSubscribe(RetrofitServiceManager.getInstance().creat(ApiService.class)
                .UserServicePost(
                        "update",
                        "basicInfo",
                        user.getId(),
                        "null",
                JSONObject.parseObject(JSONObject.toJSONString(user), JSONObject.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(this) {
                    @Override
                    protected void onSuccess(ResponseEntity responseEntity) {

                    }
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            final Message msg = new Message();
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            String responseJsonDataString = responseJson.getJSONObject("data").toJSONString();
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            switch (code) {
                                case RespCodeNumber.SUSER_UPDATE_SUCCESS:
                                    //用户是存在的，更新数据成功；
                                    //将token信息保存至本地
                                    userInfo = JSONObject.parseObject(responseJsonDataString, UserInfo.class);
                                    DataUtil.SaveUserInfoData(UserSettingActivity.this, responseJsonDataString);
                                    msg.obj = "登录成功";
                                    // 把消息发送到主线程，在主线程里现实Toast
                                    handler.sendMessage(msg);
                                    finish();
                                    break;
                                case RespCodeNumber.USER_UPDATE_FAIL:
                                    msg.obj = "USER_UPDATE_FAIL";
                                    break;
                                case RespCodeNumber.USER_UPDATE_EXCEPTION:
                                    msg.obj = "USER_UPDATE_EXCEPTION";
                                    break;
                                case RespCodeNumber.USER_UPDATE_WRONG:
                                    msg.obj = "USER_UPDATE_WRONG";
                                    break;
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
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
}

