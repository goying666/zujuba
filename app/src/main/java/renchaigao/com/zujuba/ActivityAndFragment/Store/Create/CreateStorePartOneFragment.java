package renchaigao.com.zujuba.ActivityAndFragment.Store.Create;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.PageBean.CardStoreTeamFragmentBean;
import com.renchaigao.zujuba.dao.Address;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.store.BusinessPart.BusinessTimeInfo;
import com.renchaigao.zujuba.mongoDB.info.store.StoreInfo;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.BaseFragment;
import renchaigao.com.zujuba.ActivityAndFragment.Function.GaoDeMapActivity;
import renchaigao.com.zujuba.ActivityAndFragment.Store.Adapter.StoreTeamFragmentAdapter;
import renchaigao.com.zujuba.ActivityAndFragment.TeamPart.TeamActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.StoreApiService;
import renchaigao.com.zujuba.util.DataFunctions.DataSort;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;

import static com.renchaigao.zujuba.PropertiesConfig.ConstantManagement.ADDRESS_CLASS_STORE;


public class CreateStorePartOneFragment extends BaseFragment  {

    public static final int ADD_ADDRESS = 3;

    private TextView TextView_createPlaceBasicPartRulesTextView;
    private TextView TextView_placeNameTextView;
    private TextView TextView_flagAddressTextView;
    private TextView TextView_locationTextView;
    private TextView TextView_addressInfoTextView;
    private TextView TextView_workTimeFlagTextView;
    private TextView TextView_sumOfTimeTextView;
    private TextView TextView_nameFlag;
    private TextInputEditText TextInputEditText_placeNameTextInputEditText;
    private TextInputEditText TextInputEditText_addressNoteTextInputEditText;
    private TextInputEditText TextInputEditText_ownerNickNameTextInputEditText;
    private TextInputEditText TextInputEditText_ownerTelephoneTextInputEditText;
    private TextInputEditText TextInputEditText_ownerTelephoneTwoTextInputEditText;
    private AppCompatSpinner Spinner_placeClassSpinner;
    private LinearLayout LinearLayout_createNotePartLinearLayout;
    private LinearLayout LinearLayout_createBasicPartLinearLayout;
    private ImageView ImageView_locationImageView;
    private CheckBox CheckBox_agreeCheckBox;
//    private Button Button_createPlaceBasicPartNextButton;
    private AppCompatCheckBox AppCompatCheckBox_timeOneAppCompatCheckBox;
    private AppCompatCheckBox AppCompatCheckBox_timeTwoAppCompatCheckBox;
    private AppCompatCheckBox AppCompatCheckBox_timeThreeAppCompatCheckBox;
    private AppCompatCheckBox AppCompatCheckBox_timeFourAppCompatCheckBox;


    private TextView TextView_limitNoteTextView;
    private TextView TextView_limitEndTextView;
    private TextView TextView_limitDateTextView;
    private TextView TextView_limitBeginTextView;
    private AppCompatSpinner Spinner_limitSpinner;
    private ImageView ImageView_cancleImageView;
    private ConstraintLayout ConstraintLayout_timeLimitConstraintLayout;
    private Button Button_limitCancle;
    private Button Button_limitAddButton;


    private Integer step = 1;

    private AlertDialog.Builder builder;
    private StoreInfo storeInfo = new StoreInfo();
    private String userId, token, storeId;
    @Override
    protected void InitView(View rootView) {
        builder = new AlertDialog.Builder(mContext);

        TextView_createPlaceBasicPartRulesTextView = (TextView) rootView.findViewById(R.id.TextView_createPlaceBasicPartRulesTextView);
        TextView_placeNameTextView = (TextView) rootView.findViewById(R.id.TextView_placeNameTextView);
        TextView_flagAddressTextView = (TextView) rootView.findViewById(R.id.TextView_flagAddressTextView);
        TextView_locationTextView = (TextView) rootView.findViewById(R.id.TextView_locationTextView);
        TextView_addressInfoTextView = (TextView) rootView.findViewById(R.id.TextView_addressInfoTextView);
        TextView_workTimeFlagTextView = (TextView) rootView.findViewById(R.id.TextView_workTimeFlagTextView);
        TextView_sumOfTimeTextView = (TextView) rootView.findViewById(R.id.TextView_sumOfTimeTextView);
        TextView_nameFlag = (TextView) rootView.findViewById(R.id.TextView_nameFlag);
        TextInputEditText_placeNameTextInputEditText = (TextInputEditText) rootView.findViewById(R.id.TextInputEditText_placeNameTextInputEditText);
        TextInputEditText_addressNoteTextInputEditText = (TextInputEditText) rootView.findViewById(R.id.TextInputEditText_addressNoteTextInputEditText);
        TextInputEditText_ownerNickNameTextInputEditText = (TextInputEditText) rootView.findViewById(R.id.TextInputEditText_ownerNickNameTextInputEditText);
        TextInputEditText_ownerTelephoneTextInputEditText = (TextInputEditText) rootView.findViewById(R.id.TextInputEditText_ownerTelephoneTextInputEditText);
        TextInputEditText_ownerTelephoneTwoTextInputEditText = (TextInputEditText) rootView.findViewById(R.id.TextInputEditText_ownerTelephoneTwoTextInputEditText);
        Spinner_placeClassSpinner = (AppCompatSpinner) rootView.findViewById(R.id.Spinner_placeClassSpinner);
        LinearLayout_createNotePartLinearLayout = (LinearLayout) rootView.findViewById(R.id.LinearLayout_createNotePartLinearLayout);
        LinearLayout_createBasicPartLinearLayout = (LinearLayout) rootView.findViewById(R.id.LinearLayout_createBasicPartLinearLayout);
        ImageView_locationImageView = (ImageView) rootView.findViewById(R.id.ImageView_locationImageView);
        CheckBox_agreeCheckBox = (CheckBox) rootView.findViewById(R.id.CheckBox_agreeCheckBox);
//        Button_createPlaceBasicPartNextButton = (Button) rootView.findViewById(R.id.Button_createPlaceBasicPartNextButton);
        AppCompatCheckBox_timeOneAppCompatCheckBox = (AppCompatCheckBox) rootView.findViewById(R.id.AppCompatCheckBox_timeOneAppCompatCheckBox);
        AppCompatCheckBox_timeTwoAppCompatCheckBox = (AppCompatCheckBox) rootView.findViewById(R.id.AppCompatCheckBox_timeTwoAppCompatCheckBox);
        AppCompatCheckBox_timeThreeAppCompatCheckBox = (AppCompatCheckBox) rootView.findViewById(R.id.AppCompatCheckBox_timeThreeAppCompatCheckBox);
        AppCompatCheckBox_timeFourAppCompatCheckBox = (AppCompatCheckBox) rootView.findViewById(R.id.AppCompatCheckBox_timeFourAppCompatCheckBox);


        TextView_limitNoteTextView = (TextView) rootView.findViewById(R.id.TextView_limitNoteTextView);
        TextView_limitEndTextView = (TextView) rootView.findViewById(R.id.TextView_limitEndTextView);
        TextView_limitDateTextView = (TextView) rootView.findViewById(R.id.TextView_limitDateTextView);
        TextView_limitBeginTextView = (TextView) rootView.findViewById(R.id.TextView_limitBeginTextView);
        Spinner_limitSpinner = (AppCompatSpinner) rootView.findViewById(R.id.Spinner_limitSpinner);
        ImageView_cancleImageView = (ImageView) rootView.findViewById(R.id.ImageView_cancleImageView);
        ConstraintLayout_timeLimitConstraintLayout = (ConstraintLayout) rootView.findViewById(R.id.ConstraintLayout_timeLimitConstraintLayout);
        Button_limitCancle = (Button) rootView.findViewById(R.id.Button_limitCancle);
        Button_limitAddButton = (Button) rootView.findViewById(R.id.Button_limitAddButton);
        ConstraintLayout_timeLimitConstraintLayout.setVisibility(View.GONE);

    }

    @Override
    protected void InitData(View rootView) {
//        UserInfo userInfo = DataUtil.GetUserInfoData(mContext);
//        userId = userInfo.getId();
//        token = userInfo.getToken();
    }

    @Override
    protected void InitOther(View rootView) {
        SetName();
        SetClass();
        SetAddress();
        SetWorkTime();
        SetContact();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (!isVisibleToUser && mIsViewCreated) {
            DataUtil.SaveCreatePlaceInfo(getActivity(),"basic",JSONObject.toJSONString(storeInfo));
        }
        super.setUserVisibleHint(isVisibleToUser);
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
                    TextView_nameFlag.setVisibility(View.GONE);
                } else {
                    TextView_nameFlag.setVisibility(View.VISIBLE);
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
                Intent intent = new Intent(mContext, GaoDeMapActivity.class);
                intent.putExtra("whereCome", "CreateStoreActivity");
                startActivityForResult(intent, ADD_ADDRESS);
            }
        });
        ImageView_locationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GaoDeMapActivity.class);
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

    public void readFinish(View view) {
        if (CheckBox_agreeCheckBox.isChecked()) {
            LinearLayout_createNotePartLinearLayout.setVisibility(View.GONE);
            LinearLayout_createBasicPartLinearLayout.setVisibility(View.VISIBLE);
        } else {
//            提示点击同意才能
            builder.setMessage("同意条款后才可以继续操作哦~")
                    .setTitle("Title")
                    .show();
        }

    }

    public void finishBasicPart(View view) {
        Intent intent = new Intent(mContext, CreateStorePartTwoActivity.class);
        intent.putExtra("storeInf", JSONObject.toJSONString(storeInfo));
        startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_create_store_part_one;
    }
}
