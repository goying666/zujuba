package renchaigao.com.zujuba.Activity.User;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import org.litepal.LitePal;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.Activity.BaseActivity;
import renchaigao.com.zujuba.Activity.Normal.LoginActivity;
import renchaigao.com.zujuba.Bean.AndroidMessageContent;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.UserApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.PropertiesConfig;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;

import static com.renchaigao.zujuba.PropertiesConfig.UserConstant.GENDER_GIRL;
import static com.renchaigao.zujuba.PropertiesConfig.UserConstant.USER_UPDATE_INFO_CLASS_BASIC;

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
    //    private User user;
    private String updateStyle, userId, token;
    private UserInfo userInfo;
    final String[]
            ageItems = {"95后", "90后", "00后", "90前"},
            genderItems = {"男", "女"};
    private JSONObject updateUserInfo = new JSONObject();

    final private int MSG_UPDATE_SUCCESS = 1001;
    final private int MSG_UPDATE_FAIL = -1001;
    final private int MSG_INPUT_NAME_IS_NONE = -1002;
    final private int MSG_INPUT_IDCARD_IS_NONE = -1003;
    // Handler内部类，它的引用在子线程中被使用，发送mesage，被handlerMesage方法接收
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case MSG_UPDATE_SUCCESS:
                    SetViewData();
                    Toast.makeText(UserSettingActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_INPUT_NAME_IS_NONE:
                    Toast.makeText(UserSettingActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_INPUT_IDCARD_IS_NONE:
                    Toast.makeText(UserSettingActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    @Override
    protected void InitView() {
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
    }

    @Override
    protected void InitData() {
        userInfo = DataUtil.GetUserInfoData(UserSettingActivity.this);
        userId = userInfo.getId();
        token = userInfo.getToken();
        SetViewData();
    }

    @Override
    protected void InitOther() {
        SetClick();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_setting;
    }


    private void SetViewData() {
        acivity_user_setting_ConstraintLayout_nickName.setText(userInfo.getNickName());
        activity_user_setting_gender_textView.setText(userInfo.getGender().equals(GENDER_GIRL) ? "女" : "男");
        acivity_user_setting_ConstraintLayout_age.setText(userInfo.getAgeLevel());
        activity_user_setting_phoneNumber_textView.setText(userInfo.getTelephone());
        activity_user_setting_realName_textView.setText(userInfo.getRealName());
        activity_user_setting_cardNumber_textView.setText(userInfo.getIdCard());
        activity_user_setting_softwareNumber_textView.setText(PropertiesConfig.SOFTWARE_CODE);
    }

    private Boolean updateFlag = false;

    private void SetClick() {
        activity_user_setting_toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateFlag) {
//                    说明有修改操作，提示退出确认
                    alertDialog = new AlertDialog.Builder(UserSettingActivity.this);
                    alertDialog.setTitle("取消修改吗？");
                    alertDialog.setNegativeButton("留下修改", null);
                    alertDialog.setPositiveButton("离开此页", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(UserSettingActivity.this, "离开此页咯", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    alertDialog.show();
                } else {
                    finish();
                }
            }
        });
        acivity_user_setting_ConstraintLayout_nickNamePart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(UserSettingActivity.this);
                editText = new EditText(UserSettingActivity.this);
                alertDialog.setView(editText);
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.setTitle("请输入昵称");
                alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editText.getText().toString().length() > 0) {
                            updateFlag = true;
                            updateStyle = USER_UPDATE_INFO_CLASS_BASIC;
                            updateUserInfo.put("nickName", editText.getText().toString());
                            acivity_user_setting_ConstraintLayout_nickName.setText(editText.getText().toString());
                            activity_user_setting_save.setVisibility(View.VISIBLE);
                        } else {
                            Message msg = new Message();
                            msg.arg1 = MSG_INPUT_NAME_IS_NONE;
                            msg.obj = "昵称不能为空";
                            handler.sendMessage(msg);
                        }
                    }
                });
                alertDialog.show();
            }
        });
        acivity_user_setting_ConstraintLayout_imagePart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        acivity_user_setting_ConstraintLayout_genderPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(UserSettingActivity.this);
                editText = new EditText(UserSettingActivity.this);
                alertDialog.setTitle("请选择您的性别");
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.setItems(genderItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateFlag = true;
                        updateStyle = USER_UPDATE_INFO_CLASS_BASIC;
                        updateUserInfo.put("gender", genderItems[which]);
                        activity_user_setting_gender_textView.setText(genderItems[which]);
                        activity_user_setting_save.setVisibility(View.VISIBLE);
                    }
                });
                alertDialog.show();
            }
        });
        acivity_user_setting_ConstraintLayout_agePart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(UserSettingActivity.this);
                editText = new EditText(UserSettingActivity.this);
                alertDialog.setTitle("请选择您所属年龄段");
                alertDialog.setItems(ageItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateStyle = USER_UPDATE_INFO_CLASS_BASIC;
                        updateFlag = true;
                        updateUserInfo.put("ageLevel", ageItems[which]);
                        acivity_user_setting_ConstraintLayout_age.setText(ageItems[which]);
                        activity_user_setting_save.setVisibility(View.VISIBLE);
                    }
                });
                alertDialog.show();
            }
        });
//        acivity_user_setting_ConstraintLayout_nickNamePart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.setTitle("请输入您的手机号");
//               alertDialog.setView(editText);
//               alertDialog.setPositiveButton(new )
//                alertDialog.show();
//            }
//        });

        acivity_user_setting_ConstraintLayout_realNamePart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(UserSettingActivity.this);
                editText = new EditText(UserSettingActivity.this);
                alertDialog.setView(editText);
                alertDialog.setTitle("请输入您的真实姓名");
                alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editText.getText().toString().length() > 0) {
                            updateStyle = USER_UPDATE_INFO_CLASS_BASIC;
                            updateFlag = true;
                            updateUserInfo.put("realName", editText.getText().toString());
                            activity_user_setting_realName_textView.setText(editText.getText().toString());
                            activity_user_setting_save.setVisibility(View.VISIBLE);
                        } else {
                            Message msg = new Message();
                            msg.arg1 = MSG_INPUT_NAME_IS_NONE;
                            msg.obj = "姓名不能为空";
                            handler.sendMessage(msg);
                        }
                    }
                });
                alertDialog.show();
            }
        });
        acivity_user_setting_ConstraintLayout_cardNumberPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(UserSettingActivity.this);
                editText = new EditText(UserSettingActivity.this);
                alertDialog.setView(editText);
                alertDialog.setTitle("请输入您的证件号");
                alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editText.getText().toString().length() > 0) {
                            updateStyle = USER_UPDATE_INFO_CLASS_BASIC;
                            updateFlag = true;
                            updateUserInfo.put("idCard", editText.getText().toString());
                            activity_user_setting_cardNumber_textView.setText(editText.getText().toString());
                            activity_user_setting_save.setVisibility(View.VISIBLE);
                        } else {
                            Message msg = new Message();
                            msg.arg1 = MSG_INPUT_IDCARD_IS_NONE;
                            msg.obj = "修改的身份证号不能为空";
                            handler.sendMessage(msg);
                        }
                    }
                });
                alertDialog.show();
            }
        });
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
    }

    private void UpdateUserInfo() {
        addSubscribe(RetrofitServiceManager.getInstance().creat(UserApiService.class)
                .UpdateUserInfo(updateStyle, userId, token, updateUserInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(this) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            final Message msg = new Message();
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            String responseJsonDataString = responseJson.getJSONObject("data").toJSONString();
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            switch (code) {
                                case RespCodeNumber.USER_UPDATE_INFO_SUCCESS:
                                    //用户是存在的，更新数据成功；
                                    //将token信息保存至本地
                                    userInfo = JSONObject.parseObject(responseJsonDataString, UserInfo.class);
                                    DataUtil.SaveUserInfoData(UserSettingActivity.this, responseJsonDataString);
                                    updateFlag = false;
                                    updateUserInfo.clear();
                                    activity_user_setting_save.setVisibility(View.GONE);
                                    msg.arg1 = MSG_UPDATE_SUCCESS;
                                    msg.obj = "更新成功";
                                    // 把消息发送到主线程，在主线程里现实Toast
                                    break;
                                case RespCodeNumber.CLUB_UPDATE_FAIL:
                                    msg.obj = "更新信息错误/失败";
                                    break;
                                case RespCodeNumber.USER_UPDATE_EXCEPTION:
                                    msg.obj = "USER_UPDATE_EXCEPTION";
                                    break;
                                case RespCodeNumber.USER_UPDATE_WRONG:
                                    msg.obj = "USER_UPDATE_WRONG";
                                    break;
                                case RespCodeNumber.USER_UPDATE_INFO_FAIL:
                                    msg.obj = "USER_UPDATE_INFO_FAIL";
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

                    }
                }));

    }

    public void quitApp(View view) {
        alertDialog = new AlertDialog.Builder(UserSettingActivity.this);
        alertDialog.setTitle("退出后APP所有保存的数据将被情况？");
        alertDialog.setNegativeButton("暂不退出", null);
        alertDialog.setPositiveButton("确认退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataUtil.SaveUserInfoData(UserSettingActivity.this, null);
                DataUtil.saveUserData(UserSettingActivity.this, null);
                Intent intent = new Intent(UserSettingActivity.this, LoginActivity.class);
                LitePal.deleteDatabase("MessageDB");
                startActivity(intent);
                finish();
            }
        });
        alertDialog.show();
    }
}

