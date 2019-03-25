package renchaigao.com.zujuba.ActivityAndFragment.Store.Create;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.dao.Address;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.store.BusinessPart.BusinessTimeInfo;
import com.renchaigao.zujuba.mongoDB.info.store.BusinessPart.StoreBusinessInfo;
import com.renchaigao.zujuba.mongoDB.info.store.StoreInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import normal.UUIDUtil;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import renchaigao.com.zujuba.ActivityAndFragment.BaseActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.StoreApiService;
import renchaigao.com.zujuba.util.FinalDefine;
import renchaigao.com.zujuba.util.ImgUtil;
import renchaigao.com.zujuba.util.PatternUtil;
import renchaigao.com.zujuba.util.PictureRAR;
import renchaigao.com.zujuba.util.dateUse;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;

import static com.renchaigao.zujuba.PropertiesConfig.ConstantManagement.ADDRESS_CLASS_STORE;

public class CreateStorePartTwoActivity extends BaseActivity {

    private String TAG = "CreateStorePartTwoActivity";
    private TextView TextView_rulesTextView;
    private TextInputLayout TextInputLayout_hardwareTextInputLayout;
    private TextInputLayout TextInputLayout_maxNumberTextInputLayout;
    private TextInputLayout TextInputLayout_deskTextInputLayout;
    private TextInputLayout TextInputLayout_storeNoteTextInputLayout;
    private TextInputEditText TextInputEditText_hardwareTextInputEditText;
    private TextInputEditText TextInputEditText_maxNumberTextInputEditText;
    private TextInputEditText TextInputEditText_deskTextInputEditText;
    private TextInputEditText TextInputEditText_storeNoteTextInputLayout;
    private Switch Switch_schoolSwitch;
    private AppCompatSpinner Spinner_schoolSpinner;
    private ConstraintLayout ConstraintLayout_toolbar;
    private ConstraintLayout ConstraintLayout_schoolPartConstraintLayout;
    private ConstraintLayout ConstraintLayout_homeConstraintLayout;
    private AppCompatCheckBox CheckBox_wifiCheckBox;
    private AppCompatCheckBox CheckBox_chargeCheckBox;
    private AppCompatCheckBox CheckBox_hotCheckBox;
    private AppCompatCheckBox CheckBox_coldCheckBox;
    private AppCompatCheckBox CheckBox_wcCheckBox;

    private TextView titleTextView, secondTitleTextView;
    private ConstraintLayout toolbar;
    private void initToolbar() {
        toolbar = (ConstraintLayout) findViewById(R.id.ConstraintLayout_toolbar);
        titleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        titleTextView.setText("创建新场地");
        secondTitleTextView = (TextView) toolbar.findViewById(R.id.toolbarSecondTitle);
        secondTitleTextView.setText("步骤：2/4");
        ImageView goback = (ImageView) toolbar.findViewById(R.id.toolbarBack);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private AlertDialog.Builder builder;
    private ProgressDialog progDialog;
    private StoreInfo storeInfo = new StoreInfo();

    @Override
    protected void InitView() {
        initToolbar();
        TextView_rulesTextView = (TextView) findViewById(R.id.TextView_rulesTextView);
        TextInputLayout_hardwareTextInputLayout = (TextInputLayout) findViewById(R.id.TextInputLayout_hardwareTextInputLayout);
        TextInputLayout_maxNumberTextInputLayout = (TextInputLayout) findViewById(R.id.TextInputLayout_maxNumberTextInputLayout);
        TextInputLayout_deskTextInputLayout = (TextInputLayout) findViewById(R.id.TextInputLayout_deskTextInputLayout);
        TextInputLayout_storeNoteTextInputLayout = (TextInputLayout) findViewById(R.id.TextInputLayout_storeNoteTextInputLayout);
        TextInputEditText_hardwareTextInputEditText = (TextInputEditText) findViewById(R.id.TextInputEditText_hardwareTextInputEditText);
        TextInputEditText_maxNumberTextInputEditText = (TextInputEditText) findViewById(R.id.TextInputEditText_maxNumberTextInputEditText);
        TextInputEditText_deskTextInputEditText = (TextInputEditText) findViewById(R.id.TextInputEditText_deskTextInputEditText);
        TextInputEditText_storeNoteTextInputLayout = (TextInputEditText) findViewById(R.id.TextInputEditText_storeNoteTextInputLayout);
        Switch_schoolSwitch = (Switch) findViewById(R.id.Switch_schoolSwitch);
        Spinner_schoolSpinner = (AppCompatSpinner) findViewById(R.id.Spinner_schoolSpinner);
        ConstraintLayout_toolbar = (ConstraintLayout) findViewById(R.id.ConstraintLayout_toolbar);
        ConstraintLayout_schoolPartConstraintLayout = (ConstraintLayout) findViewById(R.id.ConstraintLayout_schoolPartConstraintLayout);
        ConstraintLayout_homeConstraintLayout = (ConstraintLayout) findViewById(R.id.ConstraintLayout_homeConstraintLayout);
        CheckBox_wifiCheckBox = (AppCompatCheckBox) findViewById(R.id.CheckBox_wifiCheckBox);
        CheckBox_chargeCheckBox = (AppCompatCheckBox) findViewById(R.id.CheckBox_chargeCheckBox);
        CheckBox_hotCheckBox = (AppCompatCheckBox) findViewById(R.id.CheckBox_hotCheckBox);
        CheckBox_coldCheckBox = (AppCompatCheckBox) findViewById(R.id.CheckBox_coldCheckBox);
        CheckBox_wcCheckBox = (AppCompatCheckBox) findViewById(R.id.CheckBox_wcCheckBox);

    }

    @Override
    protected void InitData() {

    }

    @Override
    protected void InitOther() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_store_part_two;
    }


    //          附加信息部分
    private void initExtraPart() {

//        绑定监听
        business_join_desk_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (PatternUtil.strMatcher(s.toString(), PatternUtil.FUNC_NUMBER_1_99)) {
                    business_join_desk_layout.setError("");
                } else business_join_desk_layout.setError("请输入1到99的数字");
            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (PatternUtil.strMatcher(s.toString(), PatternUtil.FUNC_NUMBER_1_99)) {
//                    storeInfo.setMaxDeskSum(Integer.valueOf(s.toString()));
//                    business_join_desk_layout.setError("");
//                } else business_join_desk_layout.setError("请输入1到99的数字");
            }
        });
        business_join_maxpeople_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (PatternUtil.strMatcher(s.toString(), PatternUtil.FUNC_NUMBER_1_99)) {
                    business_join_maxpeople_layout.setError("");
                } else business_join_maxpeople_layout.setError("请输入1到99的数字");
            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (PatternUtil.strMatcher(s.toString(), PatternUtil.FUNC_NUMBER_1_99)) {
//                    storeInfo.setMaxPeopleSum(Integer.valueOf(s.toString()));
//                    business_join_maxpeople_layout.setError("");
//                } else business_join_maxpeople_layout.setError("请输入1到99的数字");
            }
        });
        business_join_extra_storeinfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null)
                    storeInfo.setPlaceinfo(s.toString());
                else {
                }
            }
        });
        business_join_other_equipment_air.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    storeInfo.getStoreHardwareInfo().setExistAir(true);
                } else {
                    storeInfo.getStoreHardwareInfo().setExistAir(false);
                }
            }
        });
        business_join_other_equipment_wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    storeInfo.getStoreHardwareInfo().setExistWifi(true);
                } else {
                    storeInfo.getStoreHardwareInfo().setExistWifi(false);
                }
            }
        });
        business_join_other_equipment_hot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    storeInfo.getStoreHardwareInfo().setExistHeat(true);
                } else {
                    storeInfo.getStoreHardwareInfo().setExistHeat(false);
                }
            }
        });
        business_join_other_equipment_wc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    storeInfo.getStoreHardwareInfo().setExistToilet(true);
                } else {
                    storeInfo.getStoreHardwareInfo().setExistToilet(false);
                }
            }
        });
        business_join_detail_button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinearLayoutVisibile(2);
            }
        });
        business_join_detail_button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkExtraPart())
                    setLinearLayoutVisibile(4);
            }
        });
    }

    //          图片信息部分
    private void initPhotoPart() {
        business_join_map_end_back = (Button) findViewById(R.id.business_join_map_end_back);
        business_join_map_end_next = (Button) findViewById(R.id.business_join_map_end_next);
        linearLayout_part4 = (LinearLayout) findViewById(R.id.business_join_introduce_part4);
        business_join_map_image_store_1 = (ImageView) findViewById(R.id.business_join_map_image_store_1);
        business_join_map_image_store_2 = (ImageView) findViewById(R.id.business_join_map_image_store_2);
        business_join_map_image_store_3 = (ImageView) findViewById(R.id.business_join_map_image_store_3);
        business_join_map_image_store_4 = (ImageView) findViewById(R.id.business_join_map_image_store_4);
        business_join_map_image_license_1 = (ImageView) findViewById(R.id.business_join_map_image_license_1);
        business_join_map_image_license_2 = (ImageView) findViewById(R.id.business_join_map_image_license_2);
        business_join_map_image_license_3 = (ImageView) findViewById(R.id.business_join_map_image_license_3);
        business_join_map_image_back = (Button) findViewById(R.id.business_join_map_image_back);
        business_join_map_image_next = (Button) findViewById(R.id.business_join_map_image_next);
        imageFinish1 = false;
        imageFinish2 = false;
        imageFinish3 = false;
        imageFinish4 = false;
        imageFinish5 = false;
        imageFinish6 = false;
        imageFinish7 = false;
        builderPhoto = new AlertDialog.Builder(this);
        builderPhoto.setTitle("图片来源");
        builderPhoto.setMessage("选择图片获取方式");
        builderPhoto.setNegativeButton("本地相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openAlbum();
            }
        });
        business_join_map_image_store_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFlag = PICK_PHOTO_1;
                choosePhoto(PHOTO_1);
            }
        });
        business_join_map_image_store_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFlag = PICK_PHOTO_2;
                choosePhoto(PHOTO_2);
            }
        });
        business_join_map_image_store_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFlag = PICK_PHOTO_3;
                choosePhoto(PHOTO_3);
            }
        });
        business_join_map_image_store_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFlag = PICK_PHOTO_4;
                choosePhoto(PHOTO_4);
            }
        });
        business_join_map_image_license_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFlag = PICK_PHOTO_5;
                choosePhoto(PHOTO_5);
            }
        });
        business_join_map_image_license_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFlag = PICK_PHOTO_6;
                choosePhoto(PHOTO_6);
            }
        });
        business_join_map_image_license_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFlag = PICK_PHOTO_7;
                choosePhoto(PHOTO_7);
            }
        });
        business_join_map_image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinearLayoutVisibile(3);
            }
        });
        business_join_map_image_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkMapPart())
                    setLinearLayoutVisibile(5);
                else
                    Toast.makeText(CreateStorePartTwoActivity.this, "请完善所有图片", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void choosePhoto(final Integer j) {
        builderPhoto.setPositiveButton("相机", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                takePhoto("photo" + imageFlag.toString(), j);
            }
        });
        builderPhoto.show();
    }

    //          所有信息展示部分
    private void initFinishPart() {
        progDialog = new ProgressDialog(CreateStorePartTwoActivity.this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在加载...");
        builder.setTitle("确认");
        builder.setMessage("确定提交吗？");
        builder.setNegativeButton("否", null);
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reloadAdapter();
                Toast.makeText(CreateStorePartTwoActivity.this, "你点击了确定发送", Toast.LENGTH_LONG).show();
            }
        });
        business_join_introduce_part5 = (LinearLayout) findViewById(R.id.business_join_introduce_part5);
        business_join_map_end_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinearLayoutVisibile(4);
            }
        });
        business_join_map_end_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreBusinessInfo storeBusinessInfo = new StoreBusinessInfo();
                ArrayList<BusinessTimeInfo> businessTimeInfos = new ArrayList<>();
                if (businessTimeInfo_1 != null)
                    businessTimeInfos.add(businessTimeInfo_1);
                if (businessTimeInfo_2 != null)
                    businessTimeInfos.add(businessTimeInfo_2);
                if (businessTimeInfo_3 != null)
                    businessTimeInfos.add(businessTimeInfo_3);
                if (businessTimeInfo_4 != null)
                    businessTimeInfos.add(businessTimeInfo_4);
                storeBusinessInfo.setBusinessTimeInfos(businessTimeInfos);
                storeBusinessInfo.setAllFrameSum(checkBoxNum);
                storeBusinessInfo.setUpTime(dateUse.DateToString(new Date()));
                storeInfo.setStoreBusinessInfo(storeBusinessInfo);
                builder.show();
            }
        });
    }

    private void timeNoteCheckView() {
        if (checkBoxNum > 0)
            business_join_introduce_time_textView_title_note.setVisibility(View.GONE);
        else
            business_join_introduce_time_textView_title_note.setVisibility(View.VISIBLE);
    }

    private boolean checkBasicPart() {
        return true;
//        if (store.getName() != null) {
//            if (store.getFormataddress() != null) {
//                if (store.getContact() != null) {
//                    if (store.getTelephonenum() != null) {
//                        if (store.getWorkingtimeid() > 0) {
//                            business_join_introduce_time_textView_title_note.setVisibility(View.GONE);
//                            return true;
//                        } else {
//                            business_join_introduce_time_textView_title_note.setVisibility(View.VISIBLE);
//                            business_join_introduce_time_textView_title_note.setError("请至少选择一个营业时间段");
//                            Toast.makeText(this, "请至少选择一个营业时间段", Toast.LENGTH_LONG).show();
//                            return false;
//                        }
//                    } else {
//                        business_join_introduce_content_TextInputLayout_person.setError("请完善店铺联系人电话信息");
//                        Toast.makeText(this, "请完善店铺联系人电话信息", Toast.LENGTH_LONG).show();
//                        return false;
//                    }
//                } else {
//                    business_telephone_name.setError("请完善店铺联系人信息");
//                    Toast.makeText(this, "请完善店铺联系人信息", Toast.LENGTH_LONG).show();
//                    return false;
//                }
//            } else {
//                business_join_name_layout.setError("请完善店铺地址信息");
//                Toast.makeText(this, "请完善店铺地址信息", Toast.LENGTH_LONG).show();
//                return false;
//            }
//        } else {
//            business_join_name_layout.setError("请完善店铺名称");
//            Toast.makeText(this, "请完善店铺名称", Toast.LENGTH_LONG).show();
//            return false;
//        }
    }

    private boolean checkExtraPart() {
        return true;
    }

    private boolean checkMapPart() {
        if (imageFinish1 & imageFinish2 & imageFinish3 & imageFinish4 & imageFinish5 & imageFinish6 & imageFinish7)
            return true;
        else return false;
    }


    // Handler内部类，它的引用在子线程中被使用，发送mesage，被handlerMesage方法接收
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            String str = (String) msg.obj;
            Toast.makeText(CreateStorePartTwoActivity.this, str, Toast.LENGTH_SHORT).show();
        }

        ;
    };

//    private void sendStoresAddInfo() {
//        /*进度条后续优化*/
//        progDialog.show();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
////                String path = "https://47.106.149.105/zujuba/join/addstores";
//                String path = PropertiesConfig.storeServerUrl + "join";
////                OkHttpClient client = new OkHttpClient();
//                OkHttpClient.Builder builder = new OkHttpClient.Builder();
//                builder.sslSocketFactory(OkhttpFunc.createSSLSocketFactory());
////                builder.sslSocketFactory(createSSLSocketFactory());
//                builder.hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                });
//                File photo1 = new File(getExternalCacheDir() + "/photo1.jpg");
//                File photo2 = new File(getExternalCacheDir() + "/photo2.jpg");
//                File photo3 = new File(getExternalCacheDir() + "/photo3.jpg");
//                File photo4 = new File(getExternalCacheDir() + "/photo4.jpg");
//                File photo5 = new File(getExternalCacheDir() + "/photo5.jpg");
//                File photo6 = new File(getExternalCacheDir() + "/photo6.jpg");
//                File photo7 = new File(getExternalCacheDir() + "/photo7.jpg");
//                PictureRAR.qualityCompress(getExternalCacheDir() + "/photo1.jpg", photo1);
//                PictureRAR.qualityCompress(getExternalCacheDir() + "/photo2.jpg", photo2);
//                PictureRAR.qualityCompress(getExternalCacheDir() + "/photo3.jpg", photo3);
//                PictureRAR.qualityCompress(getExternalCacheDir() + "/photo4.jpg", photo4);
//                PictureRAR.qualityCompress(getExternalCacheDir() + "/photo5.jpg", photo5);
//                PictureRAR.qualityCompress(getExternalCacheDir() + "/photo6.jpg", photo6);
//                PictureRAR.qualityCompress(getExternalCacheDir() + "/photo7.jpg", photo7);
//
//                RequestBody fileBodyPhoto1 = RequestBody.create(FinalDefine.MEDIA_TYPE_JPG, photo1);
//                RequestBody fileBodyPhoto2 = RequestBody.create(FinalDefine.MEDIA_TYPE_JPG, photo2);
//                RequestBody fileBodyPhoto3 = RequestBody.create(FinalDefine.MEDIA_TYPE_JPG, photo3);
//                RequestBody fileBodyPhoto4 = RequestBody.create(FinalDefine.MEDIA_TYPE_JPG, photo4);
//                RequestBody fileBodyPhoto5 = RequestBody.create(FinalDefine.MEDIA_TYPE_JPG, photo5);
//                RequestBody fileBodyPhoto6 = RequestBody.create(FinalDefine.MEDIA_TYPE_JPG, photo6);
//                RequestBody fileBodyPhoto7 = RequestBody.create(FinalDefine.MEDIA_TYPE_JPG, photo7);
//
//                storeInfo.setOwnerId(userInfo.getId());
//                storeInfo.setId(UUIDUtil.getUUID());
//                storeInfo.getStoreRankInfo().setId(UUIDUtil.getUUID());
//
//                String storeInfoString = JSONObject.toJSONString(storeInfo);
//                RequestBody jsonBody = RequestBody.create(FinalDefine.MEDIA_TYPE_JSON, storeInfoString);
//
//                RequestBody multiBody = new MultipartBody.Builder()
//                        .setType(MultipartBody.FORM)
//                        .addFormDataPart("json", storeInfoString)
//                        .addFormDataPart("photo", photo1.getName(), fileBodyPhoto1)
//                        .addFormDataPart("photo", photo2.getName(), fileBodyPhoto2)
//                        .addFormDataPart("photo", photo3.getName(), fileBodyPhoto3)
//                        .addFormDataPart("photo", photo4.getName(), fileBodyPhoto4)
//                        .addFormDataPart("photo", photo5.getName(), fileBodyPhoto5)
//                        .addFormDataPart("photo", photo6.getName(), fileBodyPhoto6)
//                        .addFormDataPart("photo", photo7.getName(), fileBodyPhoto7)
//                        .build();
//                Request mulRrequest = new Request.Builder()
//                        .url(path)
//                        .header("Content-Type", "multipart/form-data")
//                        .post(multiBody)
//                        .build();
//
//                builder.build().newCall(mulRrequest).enqueue(new Callback() {
//                    //                builder.build().newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        dismissDialog();
//                        Message msg = new Message();
//                        msg.obj = "发送失败，e";
//                        // 把消息发送到主线程，在主线程里现实Toast
//                        handler.sendMessage(msg);
//                        Log.e(TAG, call.request().body().toString());
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
////                        确认上传成功，结束当前活动；
//                        JSONObject responseJson = JSONObject.parseObject(response.body().string());
//                        int code = Integer.valueOf(responseJson.get("code").toString());
//                        JSONObject responseJsonData = (JSONObject) responseJson.getJSONObject("data");
//                        Message msg = new Message();
//                        switch (code) {
//                            case 0:
//                                dismissDialog();
//                                msg.obj = "成功发送";
//                                // 把消息发送到主线程，在主线程里现实Toast
//                                handler.sendMessage(msg);
//                                finish();
//                                break;
//                            case 1:
//                                msg.obj = "Throw an exception";
//                                // 把消息发送到主线程，在主线程里现实Toast
//                                handler.sendMessage(msg);
//                                dismissDialog();
//                                break;
//                        }
//                    }
//                });
//            }
//        }).start();
//    }

    private void reloadAdapter() {
        Map<String, RequestBody> map = new HashMap<>();

        File photo1 = new File(getExternalCacheDir() + "/photo1.jpg");
        File photo2 = new File(getExternalCacheDir() + "/photo2.jpg");
        File photo3 = new File(getExternalCacheDir() + "/photo3.jpg");
        File photo4 = new File(getExternalCacheDir() + "/photo4.jpg");
        File photo5 = new File(getExternalCacheDir() + "/photo5.jpg");
        File photo6 = new File(getExternalCacheDir() + "/photo6.jpg");
        File photo7 = new File(getExternalCacheDir() + "/photo7.jpg");
        PictureRAR.qualityCompress(getExternalCacheDir() + "/photo1.jpg", photo1);
        PictureRAR.qualityCompress(getExternalCacheDir() + "/photo2.jpg", photo2);
        PictureRAR.qualityCompress(getExternalCacheDir() + "/photo3.jpg", photo3);
        PictureRAR.qualityCompress(getExternalCacheDir() + "/photo4.jpg", photo4);
        PictureRAR.qualityCompress(getExternalCacheDir() + "/photo5.jpg", photo5);
        PictureRAR.qualityCompress(getExternalCacheDir() + "/photo6.jpg", photo6);
        PictureRAR.qualityCompress(getExternalCacheDir() + "/photo7.jpg", photo7);

        RequestBody fileBodyPhoto1 = RequestBody.create(FinalDefine.MEDIA_TYPE_JPG, photo1);
        RequestBody fileBodyPhoto2 = RequestBody.create(FinalDefine.MEDIA_TYPE_JPG, photo2);
        RequestBody fileBodyPhoto3 = RequestBody.create(FinalDefine.MEDIA_TYPE_JPG, photo3);
        RequestBody fileBodyPhoto4 = RequestBody.create(FinalDefine.MEDIA_TYPE_JPG, photo4);
        RequestBody fileBodyPhoto5 = RequestBody.create(FinalDefine.MEDIA_TYPE_JPG, photo5);
        RequestBody fileBodyPhoto6 = RequestBody.create(FinalDefine.MEDIA_TYPE_JPG, photo6);
        RequestBody fileBodyPhoto7 = RequestBody.create(FinalDefine.MEDIA_TYPE_JPG, photo7);

        storeInfo.setOwnerId(userInfo.getId());
        storeInfo.setId(UUIDUtil.getUUID());
        storeInfo.getStoreRankInfo().setId(UUIDUtil.getUUID());
//        storeInfo.setMaxDeskSum(1);
        String storeInfoString = JSONObject.toJSONString(storeInfo);
//
        RequestBody multiBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("json", storeInfoString)
                .addFormDataPart("photo", photo1.getName(), fileBodyPhoto1)
                .addFormDataPart("photo", photo2.getName(), fileBodyPhoto2)
                .addFormDataPart("photo", photo3.getName(), fileBodyPhoto3)
                .addFormDataPart("photo", photo4.getName(), fileBodyPhoto4)
                .addFormDataPart("photo", photo5.getName(), fileBodyPhoto5)
                .addFormDataPart("photo", photo6.getName(), fileBodyPhoto6)
                .addFormDataPart("photo", photo7.getName(), fileBodyPhoto7)
                .build();
        map.put("plateNo", multiBody);
//        MultipartBody.Part.createFormData("json","aaa");
//        addSubscribe(RetrofitServiceManager.getInstance().creat(PlaceApiService.class)
//                .FourParameterBodyPost("store",
//                        "join",
//                        user.getId(),
//                        storeInfo.getId(),
//                        multiBody)
        addSubscribe(RetrofitServiceManager.getInstance().creat(StoreApiService.class)
                .AddStore(user.getId(),
                        storeInfo.getId(),
                        user.getToken(),
                        multiBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(CreateStorePartTwoActivity.this) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            switch (code) {
                                case RespCodeNumber.SUCCESS: //在数据库中更新用户数据出错；
                                    finish();
                                    break;
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }

                    }

                    @Override
                    protected void onSuccess(ResponseEntity responseEntity) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.e(TAG, "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete");
                    }
                }));
    }


    public void dismissDialog() {
        Log.e(TAG, "dismissDialog");
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    private void takePhoto(String photoName, int requestCode) {
        File outputImage = new File(getExternalCacheDir(), photoName + ".jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 24) {
            imageUri = Uri.fromFile(outputImage);
        } else {
            imageUri = FileProvider.getUriForFile(CreateStorePartTwoActivity.this, "com.example.cameraalbumtest.fileprovider", outputImage);
        }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ADD_ADDRESS:
                Address addressUse = JSONObject.parseObject(data.getStringExtra("addressStoreJsonStr").toString(), Address.class);
                storeInfo.getAddressInfo().setId(addressUse.getId());
                storeInfo.getAddressInfo().setCity(addressUse.getCity());
                storeInfo.getAddressInfo().setCitycode(addressUse.getCitycode());
                storeInfo.getAddressInfo().setDistrict(addressUse.getDistrict());
                storeInfo.getAddressInfo().setFormatAddress(addressUse.getFormatAddress());
                storeInfo.getAddressInfo().setNeighborhood(addressUse.getNeighborhood());
                storeInfo.getAddressInfo().setProvince(addressUse.getProvince());
                storeInfo.getAddressInfo().setTowncode(addressUse.getTowncode());
                storeInfo.getAddressInfo().setTownship(addressUse.getTownship());
                storeInfo.getAddressInfo().setLatitude(addressUse.getLatitude());
                storeInfo.getAddressInfo().setLongitude(addressUse.getLongitude());
                storeInfo.getAddressInfo().setAddressClass(ADDRESS_CLASS_STORE);
                storeInfo.getAddressInfo().setOwnerId(userInfo.getId());
                business_join_introduce_addres_name.setText(storeInfo.getAddressInfo().getFormatAddress());
                break;
            case PHOTO_1:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        bitmapPhoto = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        business_join_map_image_store_1.setAdjustViewBounds(true);
                        business_join_map_image_store_1.setImageBitmap(bitmapPhoto);
                        imageFinish1 = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case PHOTO_2:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        bitmapPhoto = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        business_join_map_image_store_2.setImageBitmap(bitmapPhoto);
                        imageFinish2 = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case PHOTO_3:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        bitmapPhoto = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        business_join_map_image_store_3.setImageBitmap(bitmapPhoto);
                        imageFinish3 = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case PHOTO_4:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        bitmapPhoto = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        business_join_map_image_store_4.setImageBitmap(bitmapPhoto);
                        imageFinish4 = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case PHOTO_5:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        bitmapPhoto = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        business_join_map_image_license_1.setImageBitmap(bitmapPhoto);
                        imageFinish5 = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case PHOTO_6:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        bitmapPhoto = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        business_join_map_image_license_2.setImageBitmap(bitmapPhoto);
                        imageFinish6 = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case PHOTO_7:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        bitmapPhoto = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        business_join_map_image_license_3.setImageBitmap(bitmapPhoto);
                        imageFinish7 = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

//                通过相册选择的图片和对应的显示设置；
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmapPhoto;
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        bitmapPhoto = ImgUtil.handleImageOnKitKat(this, data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        bitmapPhoto = ImgUtil.handleImageBeforeKitKat(this, data);
                    }
                    switch (imageFlag) {
                        case PICK_PHOTO_1:
                            imageFinish1 = true;
//                            String test = getExternalCacheDir() + "/photo1.jpg";
                            ImgUtil.bitmapToFile(bitmapPhoto, getExternalCacheDir() + "/photo1.jpg");
                            business_join_map_image_store_1.setImageBitmap(bitmapPhoto);
                            break;
                        case PICK_PHOTO_2:
                            imageFinish2 = true;
                            ImgUtil.bitmapToFile(bitmapPhoto, getExternalCacheDir() + "/photo2.jpg");
                            business_join_map_image_store_2.setImageBitmap(bitmapPhoto);
                            break;
                        case PICK_PHOTO_3:
                            imageFinish3 = true;
                            ImgUtil.bitmapToFile(bitmapPhoto, getExternalCacheDir() + "/photo3.jpg");
                            business_join_map_image_store_3.setImageBitmap(bitmapPhoto);
                            break;
                        case PICK_PHOTO_4:
                            imageFinish4 = true;
                            ImgUtil.bitmapToFile(bitmapPhoto, getExternalCacheDir() + "/photo4.jpg");
                            business_join_map_image_store_4.setImageBitmap(bitmapPhoto);
                            break;
                        case PICK_PHOTO_5:
                            imageFinish5 = true;
                            ImgUtil.bitmapToFile(bitmapPhoto, getExternalCacheDir() + "/photo5.jpg");
                            business_join_map_image_license_1.setImageBitmap(bitmapPhoto);
                            break;
                        case PICK_PHOTO_6:
                            imageFinish6 = true;
                            ImgUtil.bitmapToFile(bitmapPhoto, getExternalCacheDir() + "/photo6.jpg");
                            business_join_map_image_license_2.setImageBitmap(bitmapPhoto);
                            break;
                        case PICK_PHOTO_7:
                            imageFinish7 = true;
                            ImgUtil.bitmapToFile(bitmapPhoto, getExternalCacheDir() + "/photo7.jpg");
                            business_join_map_image_license_3.setImageBitmap(bitmapPhoto);
                            break;
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    private void setLinearLayoutVisibile(int whichOne) {
        linearLayout_part1.setVisibility(View.GONE);
        linearLayout_part2.setVisibility(View.GONE);
        linearLayout_part3.setVisibility(View.GONE);
        linearLayout_part4.setVisibility(View.GONE);
        business_join_introduce_part5.setVisibility(View.GONE);
        switch (whichOne) {
            case 1:
                linearLayout_part1.setVisibility(View.VISIBLE);
                business_join_NestedScrollView.fullScroll(View.FOCUS_UP);
                break;
            case 2:
                linearLayout_part2.setVisibility(View.VISIBLE);
                business_join_NestedScrollView.fullScroll(View.FOCUS_UP);
                break;
            case 3:
                linearLayout_part3.setVisibility(View.VISIBLE);
                business_join_NestedScrollView.fullScroll(View.FOCUS_UP);
                break;
            case 4:
                linearLayout_part4.setVisibility(View.VISIBLE);
                business_join_NestedScrollView.fullScroll(View.FOCUS_UP);
                break;
            case 5:
                business_join_introduce_part5.setVisibility(View.VISIBLE);
                business_join_NestedScrollView.fullScroll(View.FOCUS_UP);
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    long time = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - time > 1000)) {
                Toast.makeText(this, "再按一次取消入驻", Toast.LENGTH_SHORT).show();
                time = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    public void goBack(View view) {
    }
    public void nextButton(View view) {
    }
}
