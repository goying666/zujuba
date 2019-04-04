package renchaigao.com.zujuba.ActivityAndFragment.Store.Create;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
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
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.dao.Address;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.store.BusinessPart.BusinessTimeInfo;
import com.renchaigao.zujuba.mongoDB.info.store.BusinessPart.OffWorkLimit;
import com.renchaigao.zujuba.mongoDB.info.store.BusinessPart.StoreBusinessInfo;
import com.renchaigao.zujuba.mongoDB.info.store.HardwarePart.Hardware;
import com.renchaigao.zujuba.mongoDB.info.store.StoreInfo;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import normal.UUIDUtil;
import normal.dateUse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import renchaigao.com.zujuba.ActivityAndFragment.BaseActivity;
import renchaigao.com.zujuba.ActivityAndFragment.Function.GaoDeMapActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.StoreApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.FinalDefine;
import renchaigao.com.zujuba.util.ImgUtil;
import renchaigao.com.zujuba.util.PictureRAR;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;

import static com.renchaigao.zujuba.PropertiesConfig.ConstantManagement.ADDRESS_CLASS_STORE;
import static com.renchaigao.zujuba.mongoDB.info.store.HardwarePart.Hardware.HARDWARE_CHARGE;
import static com.renchaigao.zujuba.mongoDB.info.store.HardwarePart.Hardware.HARDWARE_COLD;
import static com.renchaigao.zujuba.mongoDB.info.store.HardwarePart.Hardware.HARDWARE_HOT;
import static com.renchaigao.zujuba.mongoDB.info.store.HardwarePart.Hardware.HARDWARE_WC;
import static com.renchaigao.zujuba.mongoDB.info.store.HardwarePart.Hardware.HARDWARE_WIFI;
import static com.renchaigao.zujuba.mongoDB.info.store.StoreInfo.PLACE_CLASS_COMMUNITY;
import static com.renchaigao.zujuba.mongoDB.info.store.StoreInfo.PLACE_CLASS_HOMESTAY;
import static com.renchaigao.zujuba.mongoDB.info.store.StoreInfo.PLACE_CLASS_RESTAURANT;
import static com.renchaigao.zujuba.mongoDB.info.store.StoreInfo.PLACE_CLASS_SCHOOL;
import static com.renchaigao.zujuba.mongoDB.info.store.StoreInfo.PLACE_CLASS_SQUARE;
import static com.renchaigao.zujuba.mongoDB.info.store.StoreInfo.PLACE_CLASS_ZYB;

public class CreateStoreActivity extends BaseActivity {

    private String TAG = "This is CreateStoreActivity ";
    private TextView TextView_createPlaceBasicPartRulesTextView;
    private TextView TextView_placeNameTextView;
    private TextView TextView_flagAddressTextView;
    private TextView TextView_locationTextView;
    private TextView TextView_addressInfoTextView;
    private TextView TextView_workTimeFlagTextView;
    private TextView TextView_sumOfTimeTextView;
    private TextView TextView_nameFlag;
    private TextView TextView_homeFlag;
    private TextView TextView_envFlag;
    private TextView TextView_limitDateTextView;
    private TextView TextView_limitBeginTextView;
    private TextView TextView_limitEndTextView;
    private TextView TextView_limitNoteTextView;
    private TextView TextView_rulesTextView;
    private TextInputLayout TextInputLayout_hardwareTextInputLayout;
    private TextInputLayout TextInputLayout_maxNumberTextInputLayout;
    private TextInputLayout TextInputLayout_deskTextInputLayout;
    private TextInputLayout TextInputLayout_storeNoteTextInputLayout;
    private TextInputEditText TextInputEditText_placeNameTextInputEditText;
    private TextInputEditText TextInputEditText_addressNoteTextInputEditText;
    private TextInputEditText TextInputEditText_ownerNickNameTextInputEditText;
    private TextInputEditText TextInputEditText_ownerTelephoneTextInputEditText;
    private TextInputEditText TextInputEditText_ownerTelephoneTwoTextInputEditText;
    private TextInputEditText TextInputEditText_hardwareTextInputEditText;
    private TextInputEditText TextInputEditText_maxNumberTextInputEditText;
    private TextInputEditText TextInputEditText_deskTextInputEditText;
    private TextInputEditText TextInputEditText_storeNoteTextInputLayout;
    private Switch Switch_placeEnvSwitch;
    private Spinner Spinner_placeClassSpinner;
    private Spinner Spinner_limitSpinner;
    private Spinner Spinner_schoolSpinner;
    private LinearLayout partZeroView;
    private LinearLayout LinearLayout_createBasicPartLinearLayout;
    private LinearLayout partOneView;
    private LinearLayout partTwoView;
    private LinearLayout partFourView;
    private LinearLayout LinearLayout_buttonLinearLayout;
    private ImageView ImageView_locationImageView;
    private ImageView ImageView_cancleImageView;
    private ConstraintLayout ConstraintLayout_toolbar;
    private ConstraintLayout ConstraintLayout_timeLimitConstraintLayout;
    private ConstraintLayout ConstraintLayout_schoolPartConstraintLayout;
    private ConstraintLayout ConstraintLayout_homeConstraintLayout;
    private CheckBox CheckBox_agreeCheckBox;
    private CheckBox CheckBox_wifiCheckBox;
    private CheckBox CheckBox_chargeCheckBox;
    private CheckBox CheckBox_hotCheckBox;
    private CheckBox CheckBox_coldCheckBox;
    private CheckBox CheckBox_wcCheckBox;
    private Button Button_limitAddButton;
    private Button Button_limitCancle;
    private Button Button_backButton;
    private Button Button_nextButton;
    private Button Button_createPlaceCancleButton;
    private Button Button_createPlaceNextButton;
    private AppCompatCheckBox AppCompatCheckBox_timeOneAppCompatCheckBox;
    private AppCompatCheckBox AppCompatCheckBox_timeTwoAppCompatCheckBox;
    private AppCompatCheckBox AppCompatCheckBox_timeThreeAppCompatCheckBox;
    private AppCompatCheckBox AppCompatCheckBox_timeFourAppCompatCheckBox;
    private NestedScrollView nestedScrollView;

    private TextView TextView_photoTitleTextView1;
    private TextView TextView_f1;
    private TextView TextView_photoNoteTextView1;
    private TextView TextView_exp1;
    private TextView TextView_photoTitleTextView2;
    private TextView TextView_f2;
    private TextView TextView_photoNoteTextView2;
    private TextView TextView_exp2;
    private TextView TextView_photoTitleTextView3;
    private TextView TextView_f3;
    private TextView TextView_photoNoteTextView3;
    private TextView TextView_exp3;
    private TextView TextView_f4;
    private TextView TextView_exp4;
    private TextView TextView_f5;
    private TextView TextView_exp5;
    private TextView TextView_f6;
    private TextView TextView_exp6;
    private ImageView ImageView_photo1;
    private ImageView ImageView_photo2;
    private ImageView ImageView_photo3;
    private ImageView ImageView_photo4;
    private ImageView ImageView_photo5;
    private ImageView ImageView_photo6;
    private ConstraintLayout ConstraintLayout_photo_store_part;

    private StoreInfo storeInfo = new StoreInfo();
    private Integer imageFlag = 0;
    private Bitmap bitmapPhoto;

    public static final int PHOTO_1 = 1001;
    public static final int PHOTO_2 = 1002;
    public static final int PHOTO_3 = 1003;
    public static final int PHOTO_4 = 1004;
    public static final int PHOTO_5 = 1005;
    public static final int PHOTO_6 = 1006;
    public static final int PHOTO_7 = 1007;

    public static final int PICK_PHOTO_1 = 1011;
    public static final int PICK_PHOTO_2 = 1012;
    public static final int PICK_PHOTO_3 = 1013;
    public static final int PICK_PHOTO_4 = 1014;
    public static final int PICK_PHOTO_5 = 1015;
    public static final int PICK_PHOTO_6 = 1016;
    public static final int PICK_PHOTO_7 = 1017;

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    public static final int ADD_ADDRESS = 3;

    private boolean imageFinish1 = false, imageFinish2 = false, imageFinish3 = false, imageFinish4 = false, imageFinish5 = false, imageFinish6 = false;
    private Uri imageUri;
    private String userId, token;
    private UserInfo userInfo;
    private ProgressDialog mProgressDialog;


    // Handler内部类，它的引用在子线程中被使用，发送mesage，被handlerMesage方法接收
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            finish();
            builder.setMessage("创建申请已成功提交，请在“我的”里面查看进度。");
        }
    };

    @Override
    protected void InitView() {
        initToolbar();
        builder = new AlertDialog.Builder(this);
        TextView_createPlaceBasicPartRulesTextView = (TextView) findViewById(R.id.TextView_createPlaceBasicPartRulesTextView);
        TextView_placeNameTextView = (TextView) findViewById(R.id.TextView_placeNameTextView);
        TextView_flagAddressTextView = (TextView) findViewById(R.id.TextView_flagAddressTextView);
        TextView_locationTextView = (TextView) findViewById(R.id.TextView_locationTextView);
        TextView_addressInfoTextView = (TextView) findViewById(R.id.TextView_addressInfoTextView);
        TextView_workTimeFlagTextView = (TextView) findViewById(R.id.TextView_workTimeFlagTextView);
        TextView_sumOfTimeTextView = (TextView) findViewById(R.id.TextView_sumOfTimeTextView);
        TextView_nameFlag = (TextView) findViewById(R.id.TextView_nameFlag);
        TextView_homeFlag = (TextView) findViewById(R.id.TextView_homeFlag);
        TextView_envFlag = (TextView) findViewById(R.id.TextView_envFlag);
        TextView_limitDateTextView = (TextView) findViewById(R.id.TextView_limitDateTextView);
        TextView_limitBeginTextView = (TextView) findViewById(R.id.TextView_limitBeginTextView);
        TextView_limitEndTextView = (TextView) findViewById(R.id.TextView_limitEndTextView);
        TextView_limitNoteTextView = (TextView) findViewById(R.id.TextView_limitNoteTextView);
        TextView_rulesTextView = (TextView) findViewById(R.id.TextView_rulesTextView);
        TextInputLayout_hardwareTextInputLayout = (TextInputLayout) findViewById(R.id.TextInputLayout_hardwareTextInputLayout);
        TextInputLayout_maxNumberTextInputLayout = (TextInputLayout) findViewById(R.id.TextInputLayout_maxNumberTextInputLayout);
        TextInputLayout_deskTextInputLayout = (TextInputLayout) findViewById(R.id.TextInputLayout_deskTextInputLayout);
        TextInputLayout_storeNoteTextInputLayout = (TextInputLayout) findViewById(R.id.TextInputLayout_storeNoteTextInputLayout);
        TextInputEditText_placeNameTextInputEditText = (TextInputEditText) findViewById(R.id.TextInputEditText_placeNameTextInputEditText);
        TextInputEditText_addressNoteTextInputEditText = (TextInputEditText) findViewById(R.id.TextInputEditText_addressNoteTextInputEditText);
        TextInputEditText_ownerNickNameTextInputEditText = (TextInputEditText) findViewById(R.id.TextInputEditText_ownerNickNameTextInputEditText);
        TextInputEditText_ownerTelephoneTextInputEditText = (TextInputEditText) findViewById(R.id.TextInputEditText_ownerTelephoneTextInputEditText);
        TextInputEditText_ownerTelephoneTwoTextInputEditText = (TextInputEditText) findViewById(R.id.TextInputEditText_ownerTelephoneTwoTextInputEditText);
        TextInputEditText_hardwareTextInputEditText = (TextInputEditText) findViewById(R.id.TextInputEditText_hardwareTextInputEditText);
        TextInputEditText_maxNumberTextInputEditText = (TextInputEditText) findViewById(R.id.TextInputEditText_maxNumberTextInputEditText);
        TextInputEditText_deskTextInputEditText = (TextInputEditText) findViewById(R.id.TextInputEditText_deskTextInputEditText);
        TextInputEditText_storeNoteTextInputLayout = (TextInputEditText) findViewById(R.id.TextInputEditText_storeNoteTextInputLayout);
        Switch_placeEnvSwitch = (Switch) findViewById(R.id.Switch_placeEnvSwitch);
        Spinner_placeClassSpinner = (Spinner) findViewById(R.id.Spinner_placeClassSpinner);
        Spinner_limitSpinner = (Spinner) findViewById(R.id.AppCompatSpinner_limitSpinner);
        Spinner_schoolSpinner = (Spinner) findViewById(R.id.Spinner_outsideMaxPeople);
        partZeroView = (LinearLayout) findViewById(R.id.LinearLayout_createNotePartLinearLayout);
        LinearLayout_createBasicPartLinearLayout = (LinearLayout) findViewById(R.id.LinearLayout_createBasicPartLinearLayout);
        partTwoView = (LinearLayout) findViewById(R.id.LinearLayout_partTwoLinearLayout);
        partFourView = (LinearLayout) findViewById(R.id.LinearLayout_partFourLinearLayout);
        LinearLayout_buttonLinearLayout = (LinearLayout) findViewById(R.id.LinearLayout_buttonLinearLayout);
        ImageView_locationImageView = (ImageView) findViewById(R.id.ImageView_locationImageView);
        ImageView_cancleImageView = (ImageView) findViewById(R.id.ImageView_cancleImageView);
        ConstraintLayout_toolbar = (ConstraintLayout) findViewById(R.id.ConstraintLayout_toolbar);
        partOneView = (LinearLayout) findViewById(R.id.LinearLayout_createBasicPartLinearLayout);
        ConstraintLayout_timeLimitConstraintLayout = (ConstraintLayout) findViewById(R.id.ConstraintLayout_timeLimitConstraintLayout);
        ConstraintLayout_schoolPartConstraintLayout = (ConstraintLayout) findViewById(R.id.ConstraintLayout_schoolPartConstraintLayout);
        ConstraintLayout_homeConstraintLayout = (ConstraintLayout) findViewById(R.id.ConstraintLayout_homeConstraintLayout);
        CheckBox_agreeCheckBox = (CheckBox) findViewById(R.id.CheckBox_agreeCheckBox);
        CheckBox_wifiCheckBox = (CheckBox) findViewById(R.id.CheckBox_wifiCheckBox);
        CheckBox_chargeCheckBox = (CheckBox) findViewById(R.id.CheckBox_chargeCheckBox);
        CheckBox_hotCheckBox = (CheckBox) findViewById(R.id.CheckBox_hotCheckBox);
        CheckBox_coldCheckBox = (CheckBox) findViewById(R.id.CheckBox_coldCheckBox);
        CheckBox_wcCheckBox = (CheckBox) findViewById(R.id.CheckBox_wcCheckBox);
        Button_limitAddButton = (Button) findViewById(R.id.Button_limitAddButton);
        Button_limitCancle = (Button) findViewById(R.id.Button_limitCancle);
        Button_createPlaceCancleButton = (Button) findViewById(R.id.Button_createPlaceCancleButton);
        Button_createPlaceNextButton = (Button) findViewById(R.id.Button_createPlaceNextButton);
        AppCompatCheckBox_timeOneAppCompatCheckBox = (AppCompatCheckBox) findViewById(R.id.AppCompatCheckBox_timeOneAppCompatCheckBox);
        AppCompatCheckBox_timeTwoAppCompatCheckBox = (AppCompatCheckBox) findViewById(R.id.AppCompatCheckBox_timeTwoAppCompatCheckBox);
        AppCompatCheckBox_timeThreeAppCompatCheckBox = (AppCompatCheckBox) findViewById(R.id.AppCompatCheckBox_timeThreeAppCompatCheckBox);
        AppCompatCheckBox_timeFourAppCompatCheckBox = (AppCompatCheckBox) findViewById(R.id.AppCompatCheckBox_timeFourAppCompatCheckBox);
        Button_createPlaceCancleButton = (Button) findViewById(R.id.Button_createPlaceCancleButton);
        Button_createPlaceNextButton = (Button) findViewById(R.id.Button_createPlaceNextButton);
        nestedScrollView = (NestedScrollView) findViewById(R.id.NestedScrollView);

        TextView_photoTitleTextView1 = (TextView) findViewById(R.id.TextView_photoTitleTextView1);
        TextView_f1 = (TextView) findViewById(R.id.TextView_f1);
        TextView_photoNoteTextView1 = (TextView) findViewById(R.id.TextView_photoNoteTextView1);
        TextView_exp1 = (TextView) findViewById(R.id.TextView_exp1);
        TextView_photoTitleTextView2 = (TextView) findViewById(R.id.TextView_photoTitleTextView2);
        TextView_f2 = (TextView) findViewById(R.id.TextView_f2);
        TextView_photoNoteTextView2 = (TextView) findViewById(R.id.TextView_photoNoteTextView2);
        TextView_exp2 = (TextView) findViewById(R.id.TextView_exp2);
        TextView_photoTitleTextView3 = (TextView) findViewById(R.id.TextView_photoTitleTextView3);
        TextView_f3 = (TextView) findViewById(R.id.TextView_f3);
        TextView_photoNoteTextView3 = (TextView) findViewById(R.id.TextView_photoNoteTextView3);
        TextView_exp3 = (TextView) findViewById(R.id.TextView_exp3);
        TextView_f4 = (TextView) findViewById(R.id.TextView_f4);
        TextView_exp4 = (TextView) findViewById(R.id.TextView_exp4);
        TextView_f5 = (TextView) findViewById(R.id.TextView_f5);
        TextView_exp5 = (TextView) findViewById(R.id.TextView_exp5);
        TextView_f6 = (TextView) findViewById(R.id.TextView_f6);
        TextView_exp6 = (TextView) findViewById(R.id.TextView_exp6);
        ImageView_photo1 = (ImageView) findViewById(R.id.ImageView_photo1);
        ImageView_photo2 = (ImageView) findViewById(R.id.ImageView_photo2);
        ImageView_photo3 = (ImageView) findViewById(R.id.ImageView_photo3);
        ImageView_photo4 = (ImageView) findViewById(R.id.ImageView_photo4);
        ImageView_photo5 = (ImageView) findViewById(R.id.ImageView_photo5);
        ImageView_photo6 = (ImageView) findViewById(R.id.ImageView_photo6);
        ConstraintLayout_photo_store_part = (ConstraintLayout) findViewById(R.id.ConstraintLayout_photo_store_part);

        showPartView(0);
    }

    @Override
    protected void InitData() {
        userInfo = DataUtil.GetUserInfoData(this);
        userId = userInfo.getId();
        token = userInfo.getToken();
    }

    @Override
    protected void InitOther() {

        SetName();
        SetClass();
        SetAddress();
        SetWorkTime();
        SetContact();
        setButtonClick();
        SetEnv();
        SetHardware();
        SetPlaceNotes();
    }

    @Override
    protected void UpdateView() {

    }

    @Override

    protected int getLayoutId() {
        return R.layout.activity_create_store;
    }


    private Integer pageNum = 0;

    private void setButtonClick() {
        Button_createPlaceCancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (pageNum) {
                    case 0:
                        break;
                }
                if (pageNum > 0)
                    pageNum--;
                showPartView(pageNum);
            }
        });
        Button_createPlaceNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (pageNum) {
                    case 0:
                        if (!CheckBox_agreeCheckBox.isChecked()) {
                            pageNum = -1;
                            builder.setTitle("注意：")
                                    .setMessage("请您仔细阅读《入驻须知》后选择同意该条款协议后方可继续完成入驻。")
                                    .show();
                        }
                        break;
                    case 1:
                        if (CheckName() && CheckAddress() && CheckContact() && CheckWorkTime()) {
                        } else {
                            pageNum = 0;
                        }
                        break;
                    case 2:
                        if (CheckEnv()) {
                            storeInfo.setHardwareList(GetHardwareInfo());
                            storeInfo.setOffworkLimitList(GetWorkTime());
                        } else {
                            pageNum = 1;
                        }
                        break;
                    case 3:
                        if (imageFinish1 & imageFinish2 & imageFinish3 & imageFinish4 & imageFinish5 & imageFinish6) {
                            mProgressDialog = new ProgressDialog(CreateStoreActivity.this);
                            mProgressDialog.setTitle("入驻申请已提交");
                            mProgressDialog.setMessage("正在等待服务器处理...");
                            mProgressDialog.show();
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
                            reloadAdapter();
                        }
                        break;
                }
                if (pageNum < 3)
                    pageNum++;
                showPartView(pageNum);
            }
        });
    }

    private void showPartView(Integer page) {
        switch (page) {
            case 0:
                //readme
                partZeroView.setVisibility(View.VISIBLE);
                //第一大部分
                partOneView.setVisibility(View.GONE);
                //第二大部分
                partTwoView.setVisibility(View.GONE);
                //第三大部分
                partFourView.setVisibility(View.GONE);
                Button_createPlaceCancleButton.setVisibility(View.GONE);
                Button_createPlaceNextButton.setVisibility(View.VISIBLE);
                Button_createPlaceNextButton.setText("已阅读，下一步");
                secondTitleTextView.setText("步骤：1/4");
                break;
            case 1:
                //readme
                partZeroView.setVisibility(View.GONE);
                //第一大部分
                partOneView.setVisibility(View.VISIBLE);
                //第二大部分
                partTwoView.setVisibility(View.GONE);
                //第三大部分
                partFourView.setVisibility(View.GONE);
                Button_createPlaceCancleButton.setVisibility(View.VISIBLE);
                Button_createPlaceCancleButton.setText("上一步");
                Button_createPlaceNextButton.setVisibility(View.VISIBLE);
                Button_createPlaceNextButton.setText("下一步");
                secondTitleTextView.setText("步骤：2/4");
                break;
            case 2:
                //readme
                partZeroView.setVisibility(View.GONE);
                //第一大部分
                partOneView.setVisibility(View.GONE);
                //第二大部分
                partTwoView.setVisibility(View.VISIBLE);
                //第三大部分
                partFourView.setVisibility(View.GONE);
                Button_createPlaceCancleButton.setVisibility(View.VISIBLE);
                Button_createPlaceCancleButton.setText("上一步");
                Button_createPlaceNextButton.setVisibility(View.VISIBLE);
                Button_createPlaceNextButton.setText("下一步");
                secondTitleTextView.setText("步骤：3/4");
                break;
            case 3:
                //readme
                partZeroView.setVisibility(View.GONE);
                //第一大部分
                partOneView.setVisibility(View.GONE);
                //第二大部分
                partTwoView.setVisibility(View.GONE);
                //第三大部分
                partFourView.setVisibility(View.VISIBLE);
                Button_createPlaceCancleButton.setVisibility(View.VISIBLE);
                Button_createPlaceCancleButton.setText("上一步");
                Button_createPlaceNextButton.setVisibility(View.VISIBLE);
                Button_createPlaceNextButton.setText("提交");
                secondTitleTextView.setText("步骤：4/4");
                break;
        }
        nestedScrollView.fling(0);
        nestedScrollView.smoothScrollTo(0, 0);
        SetPhotoPart();
    }

    private TextView titleTextView, secondTitleTextView;
    private ConstraintLayout toolbar;
    private AlertDialog.Builder builder, builderPhoto;

    @SuppressLint("SetTextI18n")
    private void initToolbar() {
        toolbar = (ConstraintLayout) findViewById(R.id.ConstraintLayout_toolbar);
        titleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        titleTextView.setText("创建新场地");
        secondTitleTextView = (TextView) toolbar.findViewById(R.id.toolbarSecondTitle);
        secondTitleTextView.setText("步骤：" + (pageNum + 1) + "/4");
        ImageView goback = (ImageView) toolbar.findViewById(R.id.toolbarBack);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    private Boolean CheckName() {
        if (TextInputEditText_placeNameTextInputEditText.getText().length() > 0) {
            return true;
        } else {
            builder.setMessage("请检查场地名称设置的正误！")
                    .show();
            return false;
        }
    }

    private void SetName() {
        TextInputEditText_placeNameTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    storeInfo.setName(s.toString());
                    TextView_placeNameTextView.setVisibility(View.GONE);
                } else {
                    TextView_placeNameTextView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void SetClass() {
        Resources res = getResources();
        final String[] reminder_methods = res.getStringArray(R.array.business_class_array);
        storeInfo.setStoreclass(reminder_methods[0]);
        Spinner_placeClassSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        storeInfo.setStoreClassInt(PLACE_CLASS_ZYB);
                        break;
                    case 1:
                        storeInfo.setStoreClassInt(PLACE_CLASS_RESTAURANT);
                        break;
                    case 2:
                        storeInfo.setStoreClassInt(PLACE_CLASS_HOMESTAY);
                        break;
                    case 3:
                        storeInfo.setStoreClassInt(PLACE_CLASS_SCHOOL);
                        break;
                    case 4:
                        storeInfo.setStoreClassInt(PLACE_CLASS_SQUARE);
                        break;
                    case 5:
                        storeInfo.setStoreClassInt(PLACE_CLASS_COMMUNITY);
                        break;
                }
                storeInfo.setStoreclass(reminder_methods[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void SetAddress() {
        TextView_locationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateStoreActivity.this, GaoDeMapActivity.class);
                intent.putExtra("whereCome", "CreateStoreActivity");
                startActivityForResult(intent, ADD_ADDRESS);
            }
        });
        ImageView_locationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateStoreActivity.this, GaoDeMapActivity.class);
                intent.putExtra("whereCome", "CreateStoreActivity");
                startActivityForResult(intent, ADD_ADDRESS);
            }
        });
        TextInputEditText_addressNoteTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    storeInfo.setAddressNote(s.toString());
                    if (TextView_addressInfoTextView.getText().length() > 0) {
                        TextView_flagAddressTextView.setVisibility(View.GONE);
                    }
                } else {
                    TextView_flagAddressTextView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private Boolean CheckAddress() {
        if (TextView_flagAddressTextView.getVisibility() == View.GONE) {
            return true;
        } else {
            builder.setMessage("请检查场地 地址信息 设置的正误！")
                    .show();
            return false;
        }
    }

    private void SetContact() {
        TextInputEditText_ownerNickNameTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    storeInfo.setContact(s.toString());
                    if (TextInputEditText_ownerTelephoneTextInputEditText.getText().length() > 0)
                        TextView_nameFlag.setVisibility(View.GONE);
                } else {
                    TextView_nameFlag.setVisibility(View.VISIBLE);
                }
            }
        });
        TextInputEditText_ownerTelephoneTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    storeInfo.setTelephonenum(s.toString());
                    if (TextInputEditText_ownerNickNameTextInputEditText.getText().length() > 0)
                        TextView_nameFlag.setVisibility(View.GONE);
                } else {
                    TextView_nameFlag.setVisibility(View.VISIBLE);
                }
            }
        });
        TextInputEditText_ownerTelephoneTwoTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    storeInfo.setTelephonetwo(s.toString());
                } else {
                }
            }
        });
    }

    private Boolean CheckContact() {
        if (TextInputEditText_ownerNickNameTextInputEditText.getText().length() > 0
                && TextInputEditText_ownerTelephoneTextInputEditText.getText().length() > 0) {
            return true;
        } else {
            builder.setMessage("请检查 联系信息 设置的正误！")
                    .show();
            return false;
        }
    }

    private void SetEnv() {
        storeInfo.setIsInside(true);
        storeInfo.setMaxPeople(20);
        Switch_placeEnvSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Switch_placeEnvSwitch.setText("室外");
                    ConstraintLayout_homeConstraintLayout.setVisibility(View.GONE);
                    storeInfo.setIsInside(false);
                } else {
                    Switch_placeEnvSwitch.setText("室内");
                    ConstraintLayout_homeConstraintLayout.setVisibility(View.VISIBLE);
                    storeInfo.setIsInside(true);
                }
            }
        });
        Spinner_schoolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        storeInfo.setMaxPeople(20);
                        break;
                    case 1:
                        storeInfo.setMaxPeople(40);
                        break;
                    case 2:
                        storeInfo.setMaxPeople(100);
                        break;
                    case 3:
                        storeInfo.setMaxPeople(1000);
                        break;
                    case 4:
                        storeInfo.setMaxPeople(10000);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        TextInputEditText_deskTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    storeInfo.setMaxTeams(Integer.valueOf(s.toString()));
                    if (TextInputEditText_maxNumberTextInputEditText.getText().length() > 0) {
                        TextView_homeFlag.setVisibility(View.GONE);
                    }
                } else {
                    TextView_homeFlag.setVisibility(View.VISIBLE);
                }
            }
        });
        TextInputEditText_maxNumberTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    storeInfo.setMaxPeople(Integer.valueOf(s.toString()));
                    if (TextInputEditText_deskTextInputEditText.getText().length() > 0) {
                        TextView_homeFlag.setVisibility(View.GONE);
                    }
                } else {
                    TextView_homeFlag.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private Boolean CheckEnv() {
        if (!Switch_placeEnvSwitch.isChecked()) {
            if (TextInputEditText_deskTextInputEditText.getText().length() > 0
                    && TextInputEditText_maxNumberTextInputEditText.getText().length() > 0) {
                return true;
            } else {
                builder.setMessage("请检查 室内环境信息 设置的正误！")
                        .show();
                return false;
            }
        } else return true;
    }

    private void SetHardware() {
        TextInputEditText_hardwareTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    storeInfo.setHardwareNote(s.toString());
                }
            }
        });
    }

    private ArrayList<Hardware> GetHardwareInfo() {
        ArrayList<Hardware> hardwares = new ArrayList<>();
        if (CheckBox_wifiCheckBox.isChecked()) {
            Hardware hardware = new Hardware();
            hardware.setHardwareClass(HARDWARE_WIFI);
            hardware.setName("WiFi");
            hardware.setState(true);
            hardwares.add(hardware);
        }
        if (CheckBox_chargeCheckBox.isChecked()) {
            Hardware hardware = new Hardware();
            hardware.setHardwareClass(HARDWARE_CHARGE);
            hardware.setName("充电器");
            hardware.setState(true);
            hardwares.add(hardware);
        }
        if (CheckBox_hotCheckBox.isChecked()) {
            Hardware hardware = new Hardware();
            hardware.setHardwareClass(HARDWARE_HOT);
            hardware.setName("暖气");
            hardware.setState(true);
            hardwares.add(hardware);
        }
        if (CheckBox_wcCheckBox.isChecked()) {
            Hardware hardware = new Hardware();
            hardware.setHardwareClass(HARDWARE_WC);
            hardware.setName("WC");
            hardware.setState(true);
            hardwares.add(hardware);
        }
        if (CheckBox_coldCheckBox.isChecked()) {
            Hardware hardware = new Hardware();
            hardware.setHardwareClass(HARDWARE_COLD);
            hardware.setName("冷气");
            hardware.setState(true);
            hardwares.add(hardware);
        }

        return hardwares;
    }

    private void SetPlaceNotes() {
        TextInputEditText_storeNoteTextInputLayout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    storeInfo.setStoreNote(s.toString());
                }
            }
        });
    }

    private void SetPhotoPart() {
        int c = storeInfo.getStoreClassInt();
        if (c == PLACE_CLASS_ZYB || c == PLACE_CLASS_RESTAURANT || c == PLACE_CLASS_HOMESTAY) {
            TextView_photoTitleTextView1.setText("上传正门照片");
            TextView_photoNoteTextView1.setText("必须能看见完整的牌匾和门框");
            TextView_photoTitleTextView2.setText("上传场地照片");
            TextView_photoNoteTextView2.setText("室内/室外整体环境。");
            TextView_photoTitleTextView3.setText("上传桌子照片");
            TextView_photoNoteTextView3.setText("展示提供给客人组局的整个桌面。");
            ConstraintLayout_photo_store_part.setVisibility(View.VISIBLE);
        } else {
            TextView_photoTitleTextView1.setText("上传整体照片");
            TextView_photoNoteTextView1.setText("展示场地整体环境，图片中最好有标志性建筑");
            TextView_photoTitleTextView2.setText("上传组局场地照片");
            TextView_photoNoteTextView2.setText("提供给用户组局的场地图片。");
            TextView_photoTitleTextView3.setText("上传环境周边照片");
            TextView_photoNoteTextView3.setText("场地附近环境照片");
            ConstraintLayout_photo_store_part.setVisibility(View.GONE);
            imageFinish4 = true;
            imageFinish5 = true;
            imageFinish6 = true;
        }
        builderPhoto = new AlertDialog.Builder(this);
        builderPhoto.setTitle("图片来源");
        builderPhoto.setMessage("选择图片获取方式");
        builderPhoto.setNegativeButton("本地相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openAlbum();
            }
        });
        ImageView_photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFlag = PICK_PHOTO_1;
                choosePhoto(PHOTO_1);
            }
        });
        ImageView_photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFlag = PICK_PHOTO_2;
                choosePhoto(PHOTO_2);
            }
        });
        ImageView_photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFlag = PICK_PHOTO_3;
                choosePhoto(PHOTO_3);
            }
        });
        ImageView_photo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFlag = PICK_PHOTO_4;
                choosePhoto(PHOTO_4);
            }
        });
        ImageView_photo5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFlag = PICK_PHOTO_5;
                choosePhoto(PHOTO_5);
            }
        });
        ImageView_photo6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFlag = PICK_PHOTO_6;
                choosePhoto(PHOTO_6);
            }
        });
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
            imageUri = FileProvider.getUriForFile(CreateStoreActivity.this, "com.example.cameraalbumtest.fileprovider", outputImage);
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
                Address addressUse = JSONObject.parseObject(data.getStringExtra("addressStoreJsonStr"), Address.class);
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
                storeInfo.getAddressInfo().setOwnerId(userId);
                TextView_addressInfoTextView.setText(storeInfo.getAddressInfo().getFormatAddress());
                if (TextInputEditText_addressNoteTextInputEditText.getTextSize() > 0) {
                    TextView_flagAddressTextView.setVisibility(View.GONE);
                }
                break;
            case PHOTO_1:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        bitmapPhoto = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        ImageView_photo1.setAdjustViewBounds(true);
                        ImageView_photo1.setImageBitmap(bitmapPhoto);
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
                        ImageView_photo2.setImageBitmap(bitmapPhoto);
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
                        ImageView_photo3.setImageBitmap(bitmapPhoto);
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
                        ImageView_photo4.setImageBitmap(bitmapPhoto);
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
                        ImageView_photo5.setImageBitmap(bitmapPhoto);
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
                        ImageView_photo6.setImageBitmap(bitmapPhoto);
                        imageFinish6 = true;
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
                            ImageView_photo1.setImageBitmap(bitmapPhoto);
                            break;
                        case PICK_PHOTO_2:
                            imageFinish2 = true;
                            ImgUtil.bitmapToFile(bitmapPhoto, getExternalCacheDir() + "/photo2.jpg");
                            ImageView_photo2.setImageBitmap(bitmapPhoto);
                            break;
                        case PICK_PHOTO_3:
                            imageFinish3 = true;
                            ImgUtil.bitmapToFile(bitmapPhoto, getExternalCacheDir() + "/photo3.jpg");
                            ImageView_photo3.setImageBitmap(bitmapPhoto);
                            break;
                        case PICK_PHOTO_4:
                            imageFinish4 = true;
                            ImgUtil.bitmapToFile(bitmapPhoto, getExternalCacheDir() + "/photo4.jpg");
                            ImageView_photo4.setImageBitmap(bitmapPhoto);
                            break;
                        case PICK_PHOTO_5:
                            imageFinish5 = true;
                            ImgUtil.bitmapToFile(bitmapPhoto, getExternalCacheDir() + "/photo5.jpg");
                            ImageView_photo5.setImageBitmap(bitmapPhoto);
                            break;
                        case PICK_PHOTO_6:
                            imageFinish6 = true;
                            ImgUtil.bitmapToFile(bitmapPhoto, getExternalCacheDir() + "/photo6.jpg");
                            ImageView_photo6.setImageBitmap(bitmapPhoto);
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

    private void choosePhoto(final Integer j) {
        builderPhoto.setPositiveButton("相机", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                takePhoto("photo" + imageFlag.toString(), j);
            }
        });
        builderPhoto.show();
    }

    private Integer checkBoxNum = 0;
    private BusinessTimeInfo businessTimeInfo_1, businessTimeInfo_2, businessTimeInfo_3, businessTimeInfo_4;
    private void SetWorkTime() {
        AppCompatCheckBox_timeOneAppCompatCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    checkBoxNum++;
                    businessTimeInfo_1 = new BusinessTimeInfo();
                    businessTimeInfo_1.setTimeFrame("MOR");
                    businessTimeInfo_1.setStartTime("9:00");
                    businessTimeInfo_1.setEndTime("13:00");
                    businessTimeInfo_1.setStartHour(9);
                    businessTimeInfo_1.setStartMinute(0);
                    businessTimeInfo_1.setEndHour(13);
                    businessTimeInfo_1.setEndMinute(0);
                } else {
                    businessTimeInfo_1 = null;
                    checkBoxNum--;
                }
                TextView_sumOfTimeTextView.setText(checkBoxNum.toString());
            }
        });
        AppCompatCheckBox_timeTwoAppCompatCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    checkBoxNum++;
                    businessTimeInfo_2 = new BusinessTimeInfo();
                    businessTimeInfo_2.setTimeFrame("AFT");
                    businessTimeInfo_2.setStartTime("13:00");
                    businessTimeInfo_2.setEndTime("18:00");
                    businessTimeInfo_2.setStartHour(13);
                    businessTimeInfo_2.setStartMinute(0);
                    businessTimeInfo_2.setEndHour(18);
                    businessTimeInfo_2.setEndMinute(0);
                } else {
                    businessTimeInfo_2 = null;
                    checkBoxNum--;
                }
                TextView_sumOfTimeTextView.setText(checkBoxNum.toString());
            }
        });
        AppCompatCheckBox_timeThreeAppCompatCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    checkBoxNum++;
                    businessTimeInfo_3 = new BusinessTimeInfo();
                    businessTimeInfo_3.setTimeFrame("NON");
                    businessTimeInfo_3.setStartTime("18:00");
                    businessTimeInfo_3.setEndTime("21:00");
                    businessTimeInfo_3.setStartHour(18);
                    businessTimeInfo_3.setStartMinute(0);
                    businessTimeInfo_3.setEndHour(21);
                    businessTimeInfo_3.setEndMinute(0);
                } else {
                    businessTimeInfo_3 = null;
                    checkBoxNum--;
                }
                TextView_sumOfTimeTextView.setText(checkBoxNum.toString());
            }
        });
        AppCompatCheckBox_timeFourAppCompatCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    checkBoxNum++;
                    businessTimeInfo_4 = new BusinessTimeInfo();
                    businessTimeInfo_4.setTimeFrame("NIG");
                    businessTimeInfo_4.setStartTime("21:00");
                    businessTimeInfo_4.setEndTime("23:00");
                    businessTimeInfo_4.setStartHour(21);
                    businessTimeInfo_4.setStartMinute(0);
                    businessTimeInfo_4.setEndHour(23);
                    businessTimeInfo_4.setEndMinute(0);
                } else {
                    businessTimeInfo_4 = null;
                    checkBoxNum--;
                }
                TextView_sumOfTimeTextView.setText(checkBoxNum.toString());
            }
        });
    }

    private ArrayList<OffWorkLimit> GetWorkTime() {
        ArrayList<OffWorkLimit> offWorkLimits = new ArrayList<>();
        OffWorkLimit off1 = new OffWorkLimit();
        off1.setBeginTime("23:00");
        off1.setEndTime("09:00");
        off1.setAfterDayNumber(1);
        off1.setSample("每日 23:00 至 次日 09:00 不营业");
        offWorkLimits.add(off1);

        if (!AppCompatCheckBox_timeOneAppCompatCheckBox.isChecked()) {
            OffWorkLimit offWorkLimit = new OffWorkLimit();
            offWorkLimit.setBeginTime("09:00");
            offWorkLimit.setEndTime("13:00");
            offWorkLimit.setAfterDayNumber(0);
            offWorkLimit.setSample("每日 09:00 至 12:00 不营业");
            offWorkLimits.add(offWorkLimit);
        }
        if (!AppCompatCheckBox_timeTwoAppCompatCheckBox.isChecked()) {
            OffWorkLimit offWorkLimit = new OffWorkLimit();
            offWorkLimit.setBeginTime("13:00");
            offWorkLimit.setEndTime("18:00");
            offWorkLimit.setAfterDayNumber(0);
            offWorkLimit.setSample("每日 13:00 至 17:00 不营业");
            offWorkLimits.add(offWorkLimit);
        }
        if (!AppCompatCheckBox_timeThreeAppCompatCheckBox.isChecked()) {

            OffWorkLimit offWorkLimit = new OffWorkLimit();
            offWorkLimit.setBeginTime("18:00");
            offWorkLimit.setEndTime("21:00");
            offWorkLimit.setAfterDayNumber(0);
            offWorkLimit.setSample("每日 18:00 至 21:00 不营业");
            offWorkLimits.add(offWorkLimit);
        }
        if (!AppCompatCheckBox_timeFourAppCompatCheckBox.isChecked()) {

            OffWorkLimit offWorkLimit = new OffWorkLimit();
            offWorkLimit.setBeginTime("21:00");
            offWorkLimit.setEndTime("23:00");
            offWorkLimit.setAfterDayNumber(0);
            offWorkLimit.setSample("每日 21:00 至 23:00 不营业");
            offWorkLimits.add(offWorkLimit);
        }
        return offWorkLimits;
    }

    private Boolean CheckWorkTime() {
        if (checkBoxNum > 0) {
            return true;
        } else {
            builder.setMessage("请为场地选择至少一个营业时段（审核通过后可进行更新配置）！")
                    .show();
            return false;
        }
    }

    private void reloadAdapter() {

        File photo1 = new File(getExternalCacheDir() + "/photo1.jpg");
        File photo2 = new File(getExternalCacheDir() + "/photo2.jpg");
        File photo3 = new File(getExternalCacheDir() + "/photo3.jpg");
        File photo4 = new File(getExternalCacheDir() + "/photo4.jpg");
        File photo5 = new File(getExternalCacheDir() + "/photo5.jpg");
        File photo6 = new File(getExternalCacheDir() + "/photo6.jpg");
        PictureRAR.qualityCompress(getExternalCacheDir() + "/photo1.jpg", photo1);
        PictureRAR.qualityCompress(getExternalCacheDir() + "/photo2.jpg", photo2);
        PictureRAR.qualityCompress(getExternalCacheDir() + "/photo3.jpg", photo3);
        PictureRAR.qualityCompress(getExternalCacheDir() + "/photo4.jpg", photo4);
        PictureRAR.qualityCompress(getExternalCacheDir() + "/photo5.jpg", photo5);
        PictureRAR.qualityCompress(getExternalCacheDir() + "/photo6.jpg", photo6);

        RequestBody fileBodyPhoto1 = RequestBody.create(FinalDefine.MEDIA_TYPE_JPG, photo1);
        RequestBody fileBodyPhoto2 = RequestBody.create(FinalDefine.MEDIA_TYPE_JPG, photo2);
        RequestBody fileBodyPhoto3 = RequestBody.create(FinalDefine.MEDIA_TYPE_JPG, photo3);
        RequestBody fileBodyPhoto4 = RequestBody.create(FinalDefine.MEDIA_TYPE_JPG, photo4);
        RequestBody fileBodyPhoto5 = RequestBody.create(FinalDefine.MEDIA_TYPE_JPG, photo5);
        RequestBody fileBodyPhoto6 = RequestBody.create(FinalDefine.MEDIA_TYPE_JPG, photo6);

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
                .build();

        addSubscribe(RetrofitServiceManager.getInstance().creat(StoreApiService.class)
                .AddStore(userId, storeInfo.getId(), token, multiBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(CreateStoreActivity.this) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            switch (code) {
                                case RespCodeNumber.SUCCESS: //在数据库中更新用户数据出错；
                                    handler.sendMessage(new Message());
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
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        mProgressDialog.dismiss();
                    }
                }));
    }

}
