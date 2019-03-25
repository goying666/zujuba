package renchaigao.com.zujuba.ActivityAndFragment.User.Place;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.store.BusinessPart.BusinessTimeInfo;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.BaseFragment;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.UserApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.dateUse;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;
import renchaigao.com.zujuba.widgets.DividerItemDecoration;

public class UserPlaceManagerBasicPartFragment extends BaseFragment implements CommonViewHolder.onItemCommonClickListener {

    private static String TAG = "UserPlaceManagerBasicPartFragment";
    public Activity mContext;
    private TextView place_state, time1, time2, time3, time4, mapDetail,  placeInfoMore;
    private Button addDesk, cancleEdit, submitEdit, reloadMapInfo;
    private ImageView time1Cancle, time2Cancle, time3Cancle, time4Cancle, time1Add, time2Add, time3Add, placeInfoMoreCancle;
    private Switch placeStateSwitch;
    private Boolean isEditStyle = false;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private UserPlaceManagerPageActivityAdapter userPlaceManagerPageActivityAdapter;
    private ArrayList<BusinessTimeInfo> businessTimeInfoArrayList = new ArrayList<>();
    private ArrayList<BusinessTimeInfo> businessTimeInfoArrayListEdit = new ArrayList<>();
    private TimePickerDialog timePickerDialog;
    private AlertDialog.Builder builder;
    private NumberPicker numberPicker;

    UserInfo userInfo = new UserInfo();
    private JSONObject jsonObject = new JSONObject();

    private Boolean placeIsOpen;

    private TextInputEditText allPeopleNum, allDeskNum, addressNote;
    private JSONArray placeChangeInfoJSONArray = new JSONArray();


    // Handler内部类，它的引用在子线程中被使用，发送mesage，被handlerMesage方法接收
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            jsonObject = (JSONObject) msg.obj;
            initViewData(jsonObject);
        }
    };


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void InitView(View rootView) {
        addressNote = (TextInputEditText) rootView.findViewById(R.id.TextInputEditText1);
        allPeopleNum = (TextInputEditText) rootView.findViewById(R.id.TextInputEditText2);
        allPeopleNum.setInputType(InputType.TYPE_CLASS_NUMBER);
        allDeskNum = (TextInputEditText) rootView.findViewById(R.id.TextInputEditText3);
        allDeskNum.setInputType(InputType.TYPE_CLASS_NUMBER);
        place_state = (TextView) rootView.findViewById(R.id.storeState);
        time1 = (TextView) rootView.findViewById(R.id.textView66);
        time2 = (TextView) rootView.findViewById(R.id.textView68);
        time3 = (TextView) rootView.findViewById(R.id.textView69);
        time4 = (TextView) rootView.findViewById(R.id.textView70);
        time1Cancle = (ImageView) rootView.findViewById(R.id.imageView14);
        time2Cancle = (ImageView) rootView.findViewById(R.id.imageView21);
        time3Cancle = (ImageView) rootView.findViewById(R.id.imageView19);
        time4Cancle = (ImageView) rootView.findViewById(R.id.imageView16);
        time1Add = (ImageView) rootView.findViewById(R.id.imageView15);
        time2Add = (ImageView) rootView.findViewById(R.id.imageView20);
        time3Add = (ImageView) rootView.findViewById(R.id.imageView17);
        mapDetail = (TextView) rootView.findViewById(R.id.textView73);
        placeInfoMore = (TextView) rootView.findViewById(R.id.textView80);
        addDesk = (Button) rootView.findViewById(R.id.button4);
        submitEdit = (Button) rootView.findViewById(R.id.store_manager_submit);
        reloadMapInfo = (Button) rootView.findViewById(R.id.button3);
        placeInfoMoreCancle = (ImageView) rootView.findViewById(R.id.imageView22);
        placeStateSwitch = (Switch) rootView.findViewById(R.id.updateStoreState);
        editeView(false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.RecyclerView1);
        timePickerDialog = new TimePickerDialog(
                mContext,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    }
                },
                1, 1, true);
        timePickerDialog.setTitle("请选择一个时段的开始时间");
        builder = new AlertDialog.Builder(mContext);
        builder.setTitle("配置信息有误，请修改");
    }

    private void editeView(Boolean isEditStyle) {
        if (isEditStyle) {
//            修改界面
            placeStateSwitch.setVisibility(View.VISIBLE);
            switch (businessTimeInfoArrayListEdit.size()) {
                case 0:
                    time1Cancle.setVisibility(View.GONE);
                    time1Add.setVisibility(View.VISIBLE);
                    time2Cancle.setVisibility(View.GONE);
                    time2Add.setVisibility(View.GONE);
                    time3Cancle.setVisibility(View.GONE);
                    time3Add.setVisibility(View.GONE);
                    time4Cancle.setVisibility(View.GONE);
                case 1:
                    time1Cancle.setVisibility(View.VISIBLE);
                    time1Add.setVisibility(View.VISIBLE);
                    time2Cancle.setVisibility(View.GONE);
                    time2Add.setVisibility(View.GONE);
                    time3Cancle.setVisibility(View.GONE);
                    time3Add.setVisibility(View.GONE);
                    time4Cancle.setVisibility(View.GONE);
                    break;
                case 2:
                    time1Cancle.setVisibility(View.VISIBLE);
                    time1Add.setVisibility(View.GONE);
                    time2Cancle.setVisibility(View.VISIBLE);
                    time2Add.setVisibility(View.VISIBLE);
                    time3Cancle.setVisibility(View.GONE);
                    time3Add.setVisibility(View.GONE);
                    time4Cancle.setVisibility(View.GONE);
                    break;
                case 3:
                    time1Cancle.setVisibility(View.VISIBLE);
                    time1Add.setVisibility(View.GONE);
                    time2Cancle.setVisibility(View.VISIBLE);
                    time2Add.setVisibility(View.GONE);
                    time3Cancle.setVisibility(View.VISIBLE);
                    time3Add.setVisibility(View.VISIBLE);
                    time4Cancle.setVisibility(View.GONE);
                    break;
                case 4:
                    time1Cancle.setVisibility(View.VISIBLE);
                    time1Add.setVisibility(View.GONE);
                    time2Cancle.setVisibility(View.VISIBLE);
                    time2Add.setVisibility(View.GONE);
                    time3Cancle.setVisibility(View.VISIBLE);
                    time3Add.setVisibility(View.GONE);
                    time4Cancle.setVisibility(View.VISIBLE);
                    break;
            }
            reloadMapInfo.setVisibility(View.VISIBLE);
            placeInfoMoreCancle.setVisibility(View.VISIBLE);
//            addDesk.setVisibility(View.VISIBLE);
            addDesk.setVisibility(View.GONE);
            cancleEdit.setVisibility(View.VISIBLE);
            submitEdit.setText("提交修改");
            addressNote.setEnabled(true);
            allPeopleNum.setEnabled(true);
            allDeskNum.setEnabled(true);
        } else {
            placeStateSwitch.setVisibility(View.GONE);
            time1Cancle.setVisibility(View.GONE);
            time2Cancle.setVisibility(View.GONE);
            time3Cancle.setVisibility(View.GONE);
            time4Cancle.setVisibility(View.GONE);
            time1Add.setVisibility(View.GONE);
            time2Add.setVisibility(View.GONE);
            time3Add.setVisibility(View.GONE);
            reloadMapInfo.setVisibility(View.GONE);
            placeInfoMoreCancle.setVisibility(View.GONE);
            addDesk.setVisibility(View.GONE);
            cancleEdit.setVisibility(View.GONE);
            submitEdit.setText("修改信息");
            addressNote.setEnabled(false);
            allPeopleNum.setEnabled(false);
            allDeskNum.setEnabled(false);
        }
    }

    @SuppressLint("SetTextI18n")
    private void initViewData(JSONObject jsonObject) {
//        设置场地状态
        switch (jsonObject.getString("state")) {

            case "S":
                placeIsOpen = true;
                place_state.setText("营业");
                placeStateSwitch.setText("营业");
                placeStateSwitch.setChecked(true);
                break;

            case "Y":
                placeIsOpen = true;
                place_state.setText("营业");
                placeStateSwitch.setText("营业");
                placeStateSwitch.setChecked(true);
                break;
            case "X":
                placeIsOpen = false;
                place_state.setText("不营业");
                placeStateSwitch.setText("不营业");
                placeStateSwitch.setChecked(false);
                break;
        }
//        设置场地时间段
        switch (businessTimeInfoArrayListEdit.size()) {
            case 0:
                time1.setText("请添加至少一个时段");
                time1.setVisibility(View.VISIBLE);
                time2.setVisibility(View.GONE);
                time3.setVisibility(View.GONE);
                time4.setVisibility(View.GONE);
                break;
            case 1:
                time1.setText(businessTimeInfoArrayListEdit.get(0).getStartTime() + "~" + businessTimeInfoArrayListEdit.get(0).getEndTime());
                time1.setVisibility(View.VISIBLE);
                time2.setVisibility(View.GONE);
                time3.setVisibility(View.GONE);
                time4.setVisibility(View.GONE);
                break;
            case 2:
                time1.setText(businessTimeInfoArrayListEdit.get(0).getStartTime() + "~" + businessTimeInfoArrayListEdit.get(0).getEndTime());
                time2.setText(businessTimeInfoArrayListEdit.get(1).getStartTime() + "~" + businessTimeInfoArrayListEdit.get(1).getEndTime());
                time1.setVisibility(View.VISIBLE);
                time2.setVisibility(View.VISIBLE);
                time3.setVisibility(View.GONE);
                time4.setVisibility(View.GONE);
                break;
            case 3:
                time1.setText(businessTimeInfoArrayListEdit.get(0).getStartTime() + "~" + businessTimeInfoArrayListEdit.get(0).getEndTime());
                time2.setText(businessTimeInfoArrayListEdit.get(1).getStartTime() + "~" + businessTimeInfoArrayListEdit.get(1).getEndTime());
                time3.setText(businessTimeInfoArrayListEdit.get(2).getStartTime() + "~" + businessTimeInfoArrayListEdit.get(2).getEndTime());
                time1.setVisibility(View.VISIBLE);
                time2.setVisibility(View.VISIBLE);
                time3.setVisibility(View.VISIBLE);
                time4.setVisibility(View.GONE);
                break;
            case 4:
                time1.setText(businessTimeInfoArrayListEdit.get(0).getStartTime() + "~" + businessTimeInfoArrayListEdit.get(0).getEndTime());
                time2.setText(businessTimeInfoArrayListEdit.get(1).getStartTime() + "~" + businessTimeInfoArrayListEdit.get(1).getEndTime());
                time3.setText(businessTimeInfoArrayListEdit.get(2).getStartTime() + "~" + businessTimeInfoArrayListEdit.get(2).getEndTime());
                time4.setText(businessTimeInfoArrayListEdit.get(3).getStartTime() + "~" + businessTimeInfoArrayListEdit.get(3).getEndTime());

                time1.setVisibility(View.VISIBLE);
                time2.setVisibility(View.VISIBLE);
                time3.setVisibility(View.VISIBLE);
                time4.setVisibility(View.VISIBLE);
                break;
        }
//        设置场地定位信息
        mapDetail.setText(jsonObject.getString("mapDetail"));
//        mapNote.setText(jsonObject.getString("mapNote"));
        addressNote.setText(jsonObject.getString("mapNote"));
//        设置场地信息
        allPeopleNum.setText(jsonObject.getString("allPeopleNum"));
        allDeskNum.setText(jsonObject.getString("allDeskNum"));
    }


    @Override
    protected void InitData(View rootView) {
        userInfo = DataUtil.GetUserInfoData(getActivity());
        jsonObject = ((UserPlaceManagerActivity) mContext).toFragementData();
        reloadAdapter();
    }

    @Override
    protected void InitOther(View rootView) {
        setRecyclerView();
        setButtonClick(rootView);
        setInputEditText();
    }

    private void setButtonClick(View rootView) {
        submitEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditStyle = !isEditStyle;
                editeView(isEditStyle);
                if (isEditStyle) {
//
//                    提交修改情况
//                    检查各项修改内容
//                    1、运营状态
//                    2、营业时间段

//                    3、地理位置定位
//                    4、容纳人数/桌子
//                    5、修改卡片里面的cancle图标

                    for (JSONObject o : jsonObjectList) {
                        o.put("WHICHCARD", "BASIC_DESK_EDIT");
                    }
                } else {
//                    开始修改
                    for (JSONObject o : jsonObjectList) {
                        o.put("WHICHCARD", "BASIC_DESK");
                    }
                    builder.setTitle("修改确认").setMessage("确认后将发送至服务器进行修改")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    RequestBody multiBody = new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("json", placeChangeInfoJSONArray.toJSONString())
                                            .build();
                                    addSubscribe(RetrofitServiceManager.getInstance().creat(UserApiService.class)
                                            .FourParameterBodyPost("user",
                                                    "update",
                                                    userInfo.getId(),
                                                    jsonObject.getString("placeid"),
                                                    multiBody)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeWith(new BaseObserver<ResponseEntity>(mContext) {
                                                @Override
                                                public void onNext(ResponseEntity value) {
                                                    try {
                                                        JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                                                        int code = Integer.valueOf(responseJson.get("code").toString());
                                                        JSONObject responseJsonData = responseJson.getJSONObject("data");
                                                        Message msg = new Message();
                                                        switch (code) {
                                                            case RespCodeNumber.SUCCESS:
//                                                                msg.obj = responseJsonData;
//                                                                handler.sendMessage(msg);
//                                                                jsonObjectList = new ArrayList<>(responseJsonData.getJSONArray("array").toJavaList(JSONObject.class));
//                                                                businessTimeInfoArrayList = new ArrayList<>(responseJsonData.getJSONArray("businessTimeInfos").toJavaList(BusinessTimeInfo.class));
//                                                                businessTimeInfoArrayListEdit = businessTimeInfoArrayList;
//                                                                userPlaceManagerPageActivityAdapter.updateResults(jsonObjectList);
//                                                                userPlaceManagerPageActivityAdapter.notifyDataSetChanged();
                                                                break;
                                                        }
                                                    } catch (Exception e) {

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
                            }).show();

                }
                userPlaceManagerPageActivityAdapter.updateResults(jsonObjectList);
                userPlaceManagerPageActivityAdapter.notifyDataSetChanged();
            }
        });

        cancleEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditStyle = false;
                businessTimeInfoArrayListEdit = businessTimeInfoArrayList;
                editeView(isEditStyle);
                for (JSONObject o : jsonObjectList) {
                    o.put("WHICHCARD", "BASIC_DESK");
                }
                userPlaceManagerPageActivityAdapter.updateResults(jsonObjectList);
                userPlaceManagerPageActivityAdapter.notifyDataSetChanged();
//              用原始数据初始化当前界面
                initViewData(jsonObject);
            }
        });
//
//        addDesk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        time1Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(
                        mContext,
                        new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                BusinessTimeInfo beforeTime = new BusinessTimeInfo();
                                if (businessTimeInfoArrayListEdit.size() > 0) {
                                    beforeTime = businessTimeInfoArrayListEdit.get(0);
                                    if (hourOfDay < beforeTime.getEndHour() || (hourOfDay == beforeTime.getEndHour() && minute <= beforeTime.getEndMinute())) {
                                        builder.setMessage("您所选时间有误，请保证修改后的时间段起始时间小于前一时间段的截止时间");
                                        builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                        builder.show();
                                    } else {
                                        setNewTime(time2, hourOfDay, minute);
                                    }
                                } else {
                                    setNewTime(time2, hourOfDay, minute);
                                }
                            }
                        }, 1, 1, true);
                timePickerDialog.show();
            }
        });

        time2Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeAdd(1, time3);
            }
        });
        time3Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeAdd(2, time4);
            }
        });
        time1Cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeCancle(1);
            }
        });
        time2Cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeCancle(1);
            }
        });
        time3Cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeCancle(2);
            }
        });
        time4Cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeCancle(3);
            }
        });
        placeStateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (placeIsOpen) {
                    placeStateSwitch.setText("营业");
                    jsonObject.put("state", "Y");
                } else {
                    placeStateSwitch.setText("不营业");
                    jsonObject.put("state", "X");
                }
                initViewData(jsonObject);
                placeIsOpen = !placeIsOpen;
            }
        });
    }

    private void setInputEditText() {
        addressNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                JSONObject json = new JSONObject();
                json.put("addressNote", s.toString());
                placeChangeInfoJSONArray.add(json);
            }
        });
        allPeopleNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                JSONObject json = new JSONObject();
                json.put("allPeopleNum", s.toString());
                placeChangeInfoJSONArray.add(json);
            }
        });
        allDeskNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                JSONObject json = new JSONObject();
                json.put("allDeskNum", s.toString());
                placeChangeInfoJSONArray.add(json);
            }
        });
        placeInfoMoreCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressNote.setText("");
            }
        });
        reloadMapInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ;
            }
        });
    }

    private void timeAdd(final int index, final TextView imageView) {
        timePickerDialog = new TimePickerDialog(
                mContext,
                new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        BusinessTimeInfo beforeTime = businessTimeInfoArrayListEdit.get(index);
                        if (hourOfDay < beforeTime.getEndHour() ||
                                (hourOfDay == beforeTime.getEndHour() &&
                                        minute <= beforeTime.getEndMinute())) {
                            builder.setMessage("您所选时间有误，请保证修改后的时间段起始时间小于前一时间段的截止时间");
                            builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.show();
                        } else
                            setNewTime(imageView, hourOfDay, minute);
                    }
                }, 1, 1, true);
        timePickerDialog.show();
    }

    private void timeCancle(final int index) {
        builder.setTitle("提示信息");
        builder.setMessage("您确定要删除该时段吗？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                businessTimeInfoArrayListEdit.remove(index);
                editeView(true);
                initViewData(jsonObject);
            }
        });
        builder.show();
    }

    @SuppressLint("SetTextI18n")
    private void setNewTime(TextView timeView, int hourOfDay, int minute) {
        timeView.setText(dateUse.IntTimeToStringTime(hourOfDay, minute) + "~" + dateUse.IntTimeToStringTime(hourOfDay + 3, minute));
        BusinessTimeInfo businessTimeInfo = new BusinessTimeInfo();
        businessTimeInfo.setStartTime(dateUse.IntTimeToStringTime(hourOfDay, minute));
        businessTimeInfo.setEndTime(dateUse.IntTimeToStringTime(hourOfDay + 3, minute));
        businessTimeInfo.setStartHour(hourOfDay);
        businessTimeInfo.setStartMinute(minute);
        if (hourOfDay >= 21) {
            businessTimeInfo.setEndHour(hourOfDay - 21);
        } else
            businessTimeInfo.setEndHour(hourOfDay + 3);
        businessTimeInfo.setEndMinute(minute);
        businessTimeInfoArrayListEdit.add(businessTimeInfo);
        editeView(true);
        initViewData(jsonObject);
    }


    ArrayList<JSONObject> jsonObjectList = new ArrayList<>();

    private void setRecyclerView() {

        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);

        userPlaceManagerPageActivityAdapter = new UserPlaceManagerPageActivityAdapter(mContext, jsonObjectList, this, R.layout.card_place_manager_desk);
        recyclerView.setAdapter(userPlaceManagerPageActivityAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    private void reloadAdapter() {
        RequestBody multiBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("json", "")
                .build();
        addSubscribe(RetrofitServiceManager.getInstance().creat(UserApiService.class)
                .FourParameterBodyPost("user",
                        "one",
                        userInfo.getId(),
                        jsonObject.getString("placeid"),
                        multiBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(mContext) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            JSONObject responseJsonData = responseJson.getJSONObject("data");
                            Message msg = new Message();
                            switch (code) {
                                case RespCodeNumber.SUCCESS:
                                    msg.obj = responseJsonData;
                                    handler.sendMessage(msg);
                                    jsonObjectList = new ArrayList<>(responseJsonData.getJSONArray("array").toJavaList(JSONObject.class));
                                    businessTimeInfoArrayList = new ArrayList<>(responseJsonData.getJSONArray("businessTimeInfos").toJavaList(BusinessTimeInfo.class));
                                    businessTimeInfoArrayListEdit = businessTimeInfoArrayList;
                                    userPlaceManagerPageActivityAdapter.updateResults(jsonObjectList);
                                    userPlaceManagerPageActivityAdapter.notifyDataSetChanged();
                                    break;
                            }
                        } catch (Exception e) {

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

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_place_manager_basic_part;
    }

    @Override
    public void onItemClickListener(int position) {
    }

    @Override
    public void onItemLongClickListener(int position) {
    }
}
