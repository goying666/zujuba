//package renchaigao.com.zujuba.ActivityAndFragment.Store.Create;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Handler;
//import android.os.Message;
//import android.support.constraint.ConstraintLayout;
//import android.support.design.widget.TabLayout;
//import android.support.design.widget.TextInputEditText;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.widget.AppCompatCheckBox;
//import android.support.v7.widget.AppCompatSpinner;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.renchaigao.zujuba.PageBean.CardStoreTeamFragmentBean;
//import com.renchaigao.zujuba.dao.Address;
//import com.renchaigao.zujuba.domain.response.RespCodeNumber;
//import com.renchaigao.zujuba.domain.response.ResponseEntity;
//import com.renchaigao.zujuba.mongoDB.info.store.BusinessPart.BusinessTimeInfo;
//import com.renchaigao.zujuba.mongoDB.info.store.StoreInfo;
//import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;
//
//import java.util.ArrayList;
//
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.schedulers.Schedulers;
//import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
//import renchaigao.com.zujuba.ActivityAndFragment.BaseFragment;
//import renchaigao.com.zujuba.ActivityAndFragment.Function.GaoDeMapActivity;
//import renchaigao.com.zujuba.ActivityAndFragment.Store.Adapter.StoreTeamFragmentAdapter;
//import renchaigao.com.zujuba.ActivityAndFragment.TeamPart.TeamActivity;
//import renchaigao.com.zujuba.R;
//import renchaigao.com.zujuba.util.Api.StoreApiService;
//import renchaigao.com.zujuba.util.DataFunctions.DataSort;
//import renchaigao.com.zujuba.util.DataPart.DataUtil;
//import renchaigao.com.zujuba.util.http.BaseObserver;
//import renchaigao.com.zujuba.util.http.RetrofitServiceManager;
//
//import static com.renchaigao.zujuba.PropertiesConfig.ConstantManagement.ADDRESS_CLASS_STORE;
//
//
//public class CreateStorePartOneFragment extends BaseFragment  {
//
//    public static final int ADD_ADDRESS = 3;
//    private TextView TextView_timeFlag;
//    private TextView TextView_sumOfTimeTextView;
//    private TextView TextView_rulesTextView;
//    private TextView TextView_ownerFlag;
//    private TextView TextView_nameFlag;
//    private TextView TextView_locationTextView;
//    private TextView TextView_addressInfoTextView;
//    private TextView TextView_addressFlag;
//    private TextInputEditText TextInputEditText_storeNamTextInputEditText;
//    private TextInputEditText TextInputEditText_ownerTelephoneTwoTextInputEditText;
//    private TextInputEditText TextInputEditText_ownerTelephoneTextInputEditText;
//    private TextInputEditText TextInputEditText_ownerNickNameTextInputEditText;
//    private TextInputEditText TextInputEditText_addressNoteTextInputEditText;
//    private CheckBox CheckBox_agreeUs;
//    private Button Button_readFinish;
//    private Button Button_basicNextButton;
//    private AppCompatSpinner AppCompatSpinner_storeClassAppCompatSpinner;
//    private AppCompatCheckBox AppCompatCheckBox_timeTwoAppCompatCheckBox;
//    private AppCompatCheckBox AppCompatCheckBox_timeThreeAppCompatCheckBox;
//    private AppCompatCheckBox AppCompatCheckBox_timeOneAppCompatCheckBox;
//    private AppCompatCheckBox AppCompatCheckBox_timeFourAppCompatCheckBox;
//    private LinearLayout LinearLayout_basicPartLinearLayout;
//    private LinearLayout LinearLayout_agreePartLinearLayout;
//    private ImageView locationImageView;
//
//    private TextView TextView_limitNoteTextView;
//    private TextView TextView_limitEndTextView;
//    private TextView TextView_limitDateTextView;
//    private TextView TextView_limitBeginTextView;
//    private AppCompatSpinner Spinner_limitSpinner;
//    private ImageView ImageView_cancleImageView;
//    private ConstraintLayout ConstraintLayout_timeLimitConstraintLayout;
//    private Button Button_limitCancle;
//    private Button Button_limitAddButton;
//
//
//    private Integer step = 1;
//
//    private AlertDialog.Builder builder;
//    private StoreInfo storeInfo = new StoreInfo();
//    private String userId, token, storeId;
//    @Override
//    protected void InitView(View rootView) {
//        initToolbar();
//        TextView_timeFlag = (TextView) rootView.findViewById(R.id.TextView_timeFlag);
//        TextView_sumOfTimeTextView = (TextView) rootView.findViewById(R.id.TextView_sumOfTimeTextView);
//        TextView_rulesTextView = (TextView) rootView.findViewById(R.id.TextView_rulesTextView);
//        TextView_ownerFlag = (TextView) rootView.findViewById(R.id.TextView_ownerFlag);
//        TextView_nameFlag = (TextView) rootView.findViewById(R.id.TextView_nameFlag);
//        TextView_locationTextView = (TextView) rootView.findViewById(R.id.TextView_locationTextView);
//        TextView_addressInfoTextView = (TextView) rootView.findViewById(R.id.TextView_addressInfoTextView);
//        TextView_addressFlag = (TextView) rootView.findViewById(R.id.TextView_addressFlag);
//        TextInputEditText_storeNamTextInputEditText = (TextInputEditText) rootView.findViewById(R.id.TextInputEditText_storeNamTextInputEditText);
//        TextInputEditText_ownerTelephoneTwoTextInputEditText = (TextInputEditText) rootView.findViewById(R.id.TextInputEditText_ownerTelephoneTwoTextInputEditText);
//        TextInputEditText_ownerTelephoneTextInputEditText = (TextInputEditText) rootView.findViewById(R.id.TextInputEditText_ownerTelephoneTextInputEditText);
//        TextInputEditText_ownerNickNameTextInputEditText = (TextInputEditText) rootView.findViewById(R.id.TextInputEditText_ownerNickNameTextInputEditText);
//        TextInputEditText_addressNoteTextInputEditText = (TextInputEditText) rootView.findViewById(R.id.TextInputEditText_addressNoteTextInputEditText);
//        CheckBox_agreeUs = (CheckBox) rootView.findViewById(R.id.CheckBox_agreeUs);
//        Button_readFinish = (Button) rootView.findViewById(R.id.Button_readFinish);
//        Button_basicNextButton = (Button) rootView.findViewById(R.id.Button_basicNextButton);
//        AppCompatSpinner_storeClassAppCompatSpinner = (AppCompatSpinner) rootView.findViewById(R.id.AppCompatSpinner_storeClassAppCompatSpinner);
//        AppCompatCheckBox_timeTwoAppCompatCheckBox = (AppCompatCheckBox) rootView.findViewById(R.id.AppCompatCheckBox_timeTwoAppCompatCheckBox);
//        AppCompatCheckBox_timeThreeAppCompatCheckBox = (AppCompatCheckBox) rootView.findViewById(R.id.AppCompatCheckBox_timeThreeAppCompatCheckBox);
//        AppCompatCheckBox_timeOneAppCompatCheckBox = (AppCompatCheckBox) rootView.findViewById(R.id.AppCompatCheckBox_timeOneAppCompatCheckBox);
//        AppCompatCheckBox_timeFourAppCompatCheckBox = (AppCompatCheckBox) rootView.findViewById(R.id.AppCompatCheckBox_timeFourAppCompatCheckBox);
//        LinearLayout_basicPartLinearLayout = (LinearLayout) rootView.findViewById(R.id.LinearLayout_basicPartLinearLayout);
//        LinearLayout_agreePartLinearLayout = (LinearLayout) rootView.findViewById(R.id.LinearLayout_agreePartLinearLayout);
//        locationImageView = (ImageView) rootView.findViewById(R.id.imageView33);
//        builder = new AlertDialog.Builder(this);
//
//        TextView_limitNoteTextView = (TextView) rootView.findViewById(R.id.TextView_limitNoteTextView);
//        TextView_limitEndTextView = (TextView) rootView.findViewById(R.id.TextView_limitEndTextView);
//        TextView_limitDateTextView = (TextView) rootView.findViewById(R.id.TextView_limitDateTextView);
//        TextView_limitBeginTextView = (TextView) rootView.findViewById(R.id.TextView_limitBeginTextView);
//        Spinner_limitSpinner = (AppCompatSpinner) rootView.findViewById(R.id.Spinner_limitSpinner);
//        ImageView_cancleImageView = (ImageView) rootView.findViewById(R.id.ImageView_cancleImageView);
//        ConstraintLayout_timeLimitConstraintLayout = (ConstraintLayout) rootView.findViewById(R.id.ConstraintLayout_timeLimitConstraintLayout);
//        Button_limitCancle = (Button) rootView.findViewById(R.id.Button_limitCancle);
//        Button_limitAddButton = (Button) rootView.findViewById(R.id.Button_limitAddButton);
//
//        ConstraintLayout_timeLimitConstraintLayout.setVisibility(View.GONE);
//    }
//
//    @Override
//    protected void InitData(View rootView) {
//
//    }
//
//    @Override
//    protected void InitOther(View rootView) {
//
//    }
//
//
//    private TextView titleTextView, secondTitleTextView;
//    private ConstraintLayout toolbar;
//    private void initToolbar() {
//        toolbar = (ConstraintLayout) findViewById(R.id.ConstraintLayout_toolbar);
//        titleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitle);
//        titleTextView.setText("创建新场地");
//        secondTitleTextView = (TextView) toolbar.findViewById(R.id.toolbarSecondTitle);
//        secondTitleTextView.setText("步骤：1/4");
//        ImageView goback = (ImageView) toolbar.findViewById(R.id.toolbarBack);
//        goback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }
//
//    @Override
//    protected void InitView() {
//
//
//    }
//
//    @Override
//    protected void InitData() {
//        UserInfo userInfo = DataUtil.GetUserInfoData(this);
//        userId = userInfo.getId();
//        token = userInfo.getToken();
//        storeId = getIntent().getStringExtra("placeId");
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case ADD_ADDRESS:
//                Address addressUse = JSONObject.parseObject(data.getStringExtra("addressStoreJsonStr"), Address.class);
//                storeInfo.getAddressInfo().setId(addressUse.getId());
//                storeInfo.getAddressInfo().setCity(addressUse.getCity());
//                storeInfo.getAddressInfo().setCitycode(addressUse.getCitycode());
//                storeInfo.getAddressInfo().setDistrict(addressUse.getDistrict());
//                storeInfo.getAddressInfo().setFormatAddress(addressUse.getFormatAddress());
//                storeInfo.getAddressInfo().setNeighborhood(addressUse.getNeighborhood());
//                storeInfo.getAddressInfo().setProvince(addressUse.getProvince());
//                storeInfo.getAddressInfo().setTowncode(addressUse.getTowncode());
//                storeInfo.getAddressInfo().setTownship(addressUse.getTownship());
//                storeInfo.getAddressInfo().setLatitude(addressUse.getLatitude());
//                storeInfo.getAddressInfo().setLongitude(addressUse.getLongitude());
//                storeInfo.getAddressInfo().setAddressClass(ADDRESS_CLASS_STORE);
//                storeInfo.getAddressInfo().setOwnerId(userId);
//                TextView_addressInfoTextView.setText(storeInfo.getAddressInfo().getFormatAddress());
//                if (TextInputEditText_addressNoteTextInputEditText.getTextSize() > 0) {
//                    TextView_addressFlag.setVisibility(View.GONE);
//                }
//                break;
//        }
//    }
//
//    @Override
//    protected void InitOther() {
//        SetName();
//        SetClass();
//        SetAddress();
//        SetWorkTime();
//        SetContact();
//    }
//
//    private void SetName() {
//        TextInputEditText_storeNamTextInputEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() > 0) {
//                    storeInfo.setName(s.toString());
//                    TextView_nameFlag.setVisibility(View.GONE);
//                } else {
//                    TextView_nameFlag.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//    }
//
//    private void SetClass() {
//        AppCompatSpinner_storeClassAppCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                storeInfo.setStoreclass(String.valueOf(position));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }
//
//    private void SetAddress() {
//        TextView_locationTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CreateStoreActivity.this, GaoDeMapActivity.class);
//                intent.putExtra("whereCome", "CreateStoreActivity");
//                startActivityForResult(intent, ADD_ADDRESS);
//            }
//        });
//        locationImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CreateStoreActivity.this, GaoDeMapActivity.class);
//                intent.putExtra("whereCome", "CreateStoreActivity");
//                startActivityForResult(intent, ADD_ADDRESS);
//            }
//        });
//        TextInputEditText_addressNoteTextInputEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() > 0) {
//                    storeInfo.setAddressNote(s.toString());
//                    if (TextView_addressInfoTextView.getText().length() > 0) {
//                        TextView_addressFlag.setVisibility(View.GONE);
//                    }
//                } else {
//                    TextView_addressFlag.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//    }
//
//    private void SetContact() {
//        TextInputEditText_ownerNickNameTextInputEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() > 0) {
//                    storeInfo.setContact(s.toString());
//                    if (TextInputEditText_ownerTelephoneTextInputEditText.getText().length() > 0)
//                        TextView_ownerFlag.setVisibility(View.GONE);
//                } else {
//                    TextView_ownerFlag.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//        TextInputEditText_ownerTelephoneTextInputEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() > 0) {
//                    storeInfo.setTelephonenum(s.toString());
//                    if (TextInputEditText_ownerNickNameTextInputEditText.getText().length() > 0)
//                        TextView_ownerFlag.setVisibility(View.GONE);
//                } else {
//                    TextView_ownerFlag.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//        TextInputEditText_ownerTelephoneTwoTextInputEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() > 0) {
//                    storeInfo.setTelephonetwo(s.toString());
//                } else {
//                }
//            }
//        });
//    }
//
//    private Integer checkBoxNum = 0;
//    private BusinessTimeInfo businessTimeInfo_1, businessTimeInfo_2, businessTimeInfo_3, businessTimeInfo_4;
//
//    private void SetWorkTime() {
//        AppCompatCheckBox_timeOneAppCompatCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                if (isChecked) {
//                    checkBoxNum++;
//                    businessTimeInfo_1 = new BusinessTimeInfo();
//                    businessTimeInfo_1.setTimeFrame("MOR");
//                    businessTimeInfo_1.setStartTime("9:00");
//                    businessTimeInfo_1.setEndTime("12:00");
//                    businessTimeInfo_1.setStartHour(9);
//                    businessTimeInfo_1.setStartMinute(0);
//                    businessTimeInfo_1.setEndHour(12);
//                    businessTimeInfo_1.setEndMinute(0);
//                } else {
//                    businessTimeInfo_1 = null;
//                    checkBoxNum--;
//                }
//                TextView_sumOfTimeTextView.setText(checkBoxNum.toString());
//            }
//        });
//        AppCompatCheckBox_timeTwoAppCompatCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                if (isChecked) {
//                    checkBoxNum++;
//                    businessTimeInfo_2 = new BusinessTimeInfo();
//                    businessTimeInfo_2.setTimeFrame("AFT");
//                    businessTimeInfo_2.setStartTime("13:00");
//                    businessTimeInfo_2.setEndTime("17:00");
//                    businessTimeInfo_2.setStartHour(13);
//                    businessTimeInfo_2.setStartMinute(0);
//                    businessTimeInfo_2.setEndHour(16);
//                    businessTimeInfo_2.setEndMinute(0);
//                } else {
//                    businessTimeInfo_2 = null;
//                    checkBoxNum--;
//                }
//                TextView_sumOfTimeTextView.setText(checkBoxNum.toString());
//            }
//        });
//        AppCompatCheckBox_timeThreeAppCompatCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                if (isChecked) {
//                    checkBoxNum++;
//                    businessTimeInfo_3 = new BusinessTimeInfo();
//                    businessTimeInfo_3.setTimeFrame("NON");
//                    businessTimeInfo_3.setStartTime("18:00");
//                    businessTimeInfo_3.setEndTime("21:00");
//                    businessTimeInfo_3.setStartHour(18);
//                    businessTimeInfo_3.setStartMinute(0);
//                    businessTimeInfo_3.setEndHour(21);
//                    businessTimeInfo_3.setEndMinute(0);
//                } else {
//                    businessTimeInfo_3 = null;
//                    checkBoxNum--;
//                }
//                TextView_sumOfTimeTextView.setText(checkBoxNum.toString());
//            }
//        });
//        AppCompatCheckBox_timeFourAppCompatCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                if (isChecked) {
//                    checkBoxNum++;
//                    businessTimeInfo_4 = new BusinessTimeInfo();
//                    businessTimeInfo_4.setTimeFrame("NIG");
//                    businessTimeInfo_4.setStartTime("21:00");
//                    businessTimeInfo_4.setEndTime("23:00");
//                    businessTimeInfo_4.setStartHour(21);
//                    businessTimeInfo_4.setStartMinute(0);
//                    businessTimeInfo_4.setEndHour(24);
//                    businessTimeInfo_4.setEndMinute(0);
//                } else {
//                    businessTimeInfo_4 = null;
//                    checkBoxNum--;
//                }
//                TextView_sumOfTimeTextView.setText(checkBoxNum.toString());
//            }
//        });
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.fragment_create_store_part_one;
//    }
//}
