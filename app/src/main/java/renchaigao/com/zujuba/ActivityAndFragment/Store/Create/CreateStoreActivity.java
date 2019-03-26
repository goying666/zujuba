package renchaigao.com.zujuba.ActivityAndFragment.Store.Create;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.renchaigao.zujuba.mongoDB.info.store.BusinessPart.BusinessTimeInfo;
import com.renchaigao.zujuba.mongoDB.info.store.HardwarePart.Hardware;
import com.renchaigao.zujuba.mongoDB.info.store.StoreInfo;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.util.ArrayList;

import renchaigao.com.zujuba.ActivityAndFragment.BaseActivity;
import renchaigao.com.zujuba.ActivityAndFragment.Function.GaoDeMapActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.DataPart.DataUtil;

import static com.renchaigao.zujuba.PropertiesConfig.ConstantManagement.ADDRESS_CLASS_STORE;
import static com.renchaigao.zujuba.mongoDB.info.store.HardwarePart.Hardware.HARDWARE_CHARGE;
import static com.renchaigao.zujuba.mongoDB.info.store.HardwarePart.Hardware.HARDWARE_COLD;
import static com.renchaigao.zujuba.mongoDB.info.store.HardwarePart.Hardware.HARDWARE_HOT;
import static com.renchaigao.zujuba.mongoDB.info.store.HardwarePart.Hardware.HARDWARE_WC;
import static com.renchaigao.zujuba.mongoDB.info.store.HardwarePart.Hardware.HARDWARE_WIFI;

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

    private ConstraintLayout partOneView;

    private StoreInfo storeInfo = new StoreInfo();

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
        Spinner_limitSpinner = (Spinner) findViewById(R.id.Spinner_limitSpinner);
        Spinner_schoolSpinner = (Spinner) findViewById(R.id.Spinner_schoolSpinner);
        partZeroView = (LinearLayout) findViewById(R.id.LinearLayout_createNotePartLinearLayout);
        LinearLayout_createBasicPartLinearLayout = (LinearLayout) findViewById(R.id.LinearLayout_createBasicPartLinearLayout);
        partTwoView = (LinearLayout) findViewById(R.id.LinearLayout_partTwoLinearLayout);
        partFourView = (LinearLayout) findViewById(R.id.LinearLayout_partFourLinearLayout);
        LinearLayout_buttonLinearLayout = (LinearLayout) findViewById(R.id.LinearLayout_buttonLinearLayout);
        ImageView_locationImageView = (ImageView) findViewById(R.id.ImageView_locationImageView);
        ImageView_cancleImageView = (ImageView) findViewById(R.id.ImageView_cancleImageView);
        ConstraintLayout_toolbar = (ConstraintLayout) findViewById(R.id.ConstraintLayout_toolbar);
        partOneView = (ConstraintLayout) findViewById(R.id.ConstraintLayout_partOneConstraintLayout);
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

        showPartView(0);
    }

    private String userId, token;

    @Override
    protected void InitData() {

        UserInfo userInfo = DataUtil.GetUserInfoData(this);
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
    }

    private TextView titleTextView, secondTitleTextView;
    private ConstraintLayout toolbar;
    private Integer step = 1;
    private AlertDialog.Builder builder;

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

    public static final int ADD_ADDRESS = 3;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        Spinner_limitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                storeInfo.setStoreclass(String.valueOf(position));
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

    private void SetEnv() {
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
        if (CheckBox_hotCheckBox.isChecked()) {
            Hardware hardware = new Hardware();
            hardware.setHardwareClass(HARDWARE_WC);
            hardware.setName("WC");
            hardware.setState(true);
            hardwares.add(hardware);
        }
        if (CheckBox_hotCheckBox.isChecked()) {
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
                    businessTimeInfo_1.setEndTime("12:00");
                    businessTimeInfo_1.setStartHour(9);
                    businessTimeInfo_1.setStartMinute(0);
                    businessTimeInfo_1.setEndHour(12);
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
                    businessTimeInfo_2.setEndTime("17:00");
                    businessTimeInfo_2.setStartHour(13);
                    businessTimeInfo_2.setStartMinute(0);
                    businessTimeInfo_2.setEndHour(16);
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
                    businessTimeInfo_4.setEndHour(24);
                    businessTimeInfo_4.setEndMinute(0);
                } else {
                    businessTimeInfo_4 = null;
                    checkBoxNum--;
                }
                TextView_sumOfTimeTextView.setText(checkBoxNum.toString());
            }
        });
    }

}
