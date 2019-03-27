package renchaigao.com.zujuba.ActivityAndFragment.Store.Manager;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.PageBean.CardStoreManagerBasicHardwareBean;
import com.renchaigao.zujuba.PageBean.CardStoreManagerBasicWorkLimitBean;
import com.renchaigao.zujuba.PageBean.StoreManagerBasicFragmentBean;
import com.renchaigao.zujuba.dao.Address;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.BaseFragment;
import renchaigao.com.zujuba.ActivityAndFragment.Function.GaoDeMapActivity;
import renchaigao.com.zujuba.ActivityAndFragment.Store.Create.CreateStoreActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.StoreApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;

import static com.renchaigao.zujuba.PropertiesConfig.ConstantManagement.ADDRESS_CLASS_STORE;
import static renchaigao.com.zujuba.ActivityAndFragment.Function.InputActivity.UPDATE_STORE_NAME;

public class StoreManagerBasicFragment extends BaseFragment implements CommonViewHolder.onItemCommonClickListener {

    private static final int MSG_UPDATE_VIEW = 0X1101;
    private static final int MSG_UPDATE_NAME_SUCCESS = 0X1102;
    private static final int MSG_UPDATE_NAME_FAIL = 0X1103;
    private static final int MSG_UPDATE_ADDRESS_SUCCESS = 0X1104;
    private static final int MSG_UPDATE_ADDRESS_FAIL = 0X1105;
    private static final int MSG_UPDATE_NOTES_SUCCESS = 0X1106;
    private static final int MSG_UPDATE_NOTES_FAIL = 0X1107;
    private static final int MSG_UPDATE_MAX_PEOPLE_SUCCESS = 0X1108;
    private static final int MSG_UPDATE_MAX_PEOPLE_FAIL = 0X1109;
    private static final int MSG_UPDATE_MAX_DESK_SUCCESS = 0X110A;
    private static final int MSG_UPDATE_MAX_DESK_FAIL = 0X110B;
    private static final int MSG_UPDATE_HARDWARE_NOTE_SUCCESS = 0X110C;
    private static final int MSG_UPDATE_HARDWARE_NOTE_FAIL = 0X110D;
    private static final int MSG_UPDATE_NONE_CHANGE = 0X110E;
    private static final int MSG_UPDATE_SUBMIT = 0X110F;
    private static final int MSG_UPDATE_SUCCESS = 0X1110;
    private static final int MSG_UPDATE_FAIL = 0X1111;

    private StoreManagerBasicHardwareAdapter basicHardwareAdapter;
    private StoreManagerBasicWorkLimitAdapter workLimitAdapter;
    private StoreManagerBasicFragmentBean storeManagerBasicFragmentBean = new StoreManagerBasicFragmentBean();
    private StoreManagerBasicFragmentBean oldStoreManagerBasicFragmentBean = new StoreManagerBasicFragmentBean();
    private ArrayList<CardStoreManagerBasicHardwareBean> hardwareBeanArrayList = new ArrayList<>();
    private ArrayList<CardStoreManagerBasicWorkLimitBean> workLimitBeanArrayList = new ArrayList<>();
    private String userId, token, storeId;

    private TextView TextView_state;
    private TextView TextView_notes;
    private TextView TextView_name;
    private TextView TextView_maxPeople;
    private TextView TextView_maxDesk;
    private TextView TextView_hardwareNote;
    private TextView TextView_editName;
    private TextView TextView_editNotes;
    private TextView TextView_editMaxPeople;
    private TextView TextView_editHardware;
    private TextView TextView_editeAddressNote;
    private TextView TextView_editeAddressInfo;
    private TextView TextView_editDeskMax;
    private TextView TextView_editBusinessTime;
    private TextView TextView_addressNote;
    private TextView TextView_addressInfo;
    private TextView TextView_noteState;
    private Switch Switch_stateSwitch;
    private AppCompatSpinner AppCompatSpinner_outsideMaxPeople;
    private AppCompatSpinner AppCompatSpinner_placeEnvSwitch;
    private RecyclerView workRecyclerView;
    private RecyclerView hardwareRecyclerView;
    private NestedScrollView NestedScrollView_editPart;
    private ConstraintLayout ConstraintLayout_state;
    private ConstraintLayout ConstraintLayout_schoolPartConstraintLayout;
    private ConstraintLayout ConstraintLayout_homeConstraintLayout;
    private ConstraintLayout ConstraintLayout_timeLimitConstraintLayout;

    private TextView TextView_limitDateTextView;
    private TextView TextView_limitBeginTextView;
    private TextView TextView_limitEndTextView;
    private TextView TextView_limitNoteTextView;
    private TextView TextView_limitHowLongTextView;
    private AppCompatSpinner AppCompatSpinner_limitSpinner;
    private ImageView ImageView_cancleImageView;
    private Button Button_limitAddButton;
    private Button Button_limitCancle;

    private AlertDialog.Builder alertDialog, builder;
    private EditText editText;
    private Button Button_submitStoreManager;
    private ProgressDialog mProgressDialog;
    public static final int ADD_ADDRESS = 3;

    @Override
    protected void InitView(View rootView) {

//        storeNameIntroduce = (TextView) rootView.findViewById(R.id.storeNameIntroduce);
//        String str = "修改名称需要通过<font color='#FF0000'>工作人员审批</font>";
//        storeNameIntroduce.setText(Html.fromHtml(str));

        builder = new AlertDialog.Builder(mContext);
        TextView_state = (TextView) rootView.findViewById(R.id.TextView_state);
        TextView_notes = (TextView) rootView.findViewById(R.id.TextView_notes);
        TextView_editName = (TextView) rootView.findViewById(R.id.TextView_editName);
        TextView_name = (TextView) rootView.findViewById(R.id.TextView_name);
        TextView_maxPeople = (TextView) rootView.findViewById(R.id.TextView_maxPeople);
        TextView_maxDesk = (TextView) rootView.findViewById(R.id.TextView_maxDesk);
        TextView_hardwareNote = (TextView) rootView.findViewById(R.id.TextView_hardwareNote);
        TextView_editNotes = (TextView) rootView.findViewById(R.id.TextView_editNotes);
        TextView_editMaxPeople = (TextView) rootView.findViewById(R.id.TextView_editMaxPeople);
        TextView_editHardware = (TextView) rootView.findViewById(R.id.TextView_editHardware);
        TextView_editeAddressNote = (TextView) rootView.findViewById(R.id.TextView_editeAddressNote);
        TextView_editeAddressInfo = (TextView) rootView.findViewById(R.id.TextView_editeAddressInfo);
        TextView_editDeskMax = (TextView) rootView.findViewById(R.id.TextView_editDeskMax);
        TextView_editBusinessTime = (TextView) rootView.findViewById(R.id.TextView_editBusinessTime);
        TextView_addressNote = (TextView) rootView.findViewById(R.id.TextView_addressNote);
        TextView_addressInfo = (TextView) rootView.findViewById(R.id.TextView_addressInfo);
        TextView_noteState = (TextView) rootView.findViewById(R.id.TextView_noteState);
        Switch_stateSwitch = (Switch) rootView.findViewById(R.id.Switch_stateSwitch);
        AppCompatSpinner_outsideMaxPeople = (AppCompatSpinner) rootView.findViewById(R.id.AppCompatSpinner_outsideMaxPeople);
        AppCompatSpinner_placeEnvSwitch = (AppCompatSpinner) rootView.findViewById(R.id.AppCompatSpinner_placeEnvSwitch);
        hardwareRecyclerView = (RecyclerView) rootView.findViewById(R.id.RecyclerView_hardware);
        workRecyclerView = (RecyclerView) rootView.findViewById(R.id.RecyclerView_businessTimeRecyclerView);
        NestedScrollView_editPart = (NestedScrollView) rootView.findViewById(R.id.NestedScrollView_editPart);
        ConstraintLayout_state = (ConstraintLayout) rootView.findViewById(R.id.ConstraintLayout_state);
        ConstraintLayout_schoolPartConstraintLayout = (ConstraintLayout) rootView.findViewById(R.id.ConstraintLayout_schoolPartConstraintLayout);
        ConstraintLayout_homeConstraintLayout = (ConstraintLayout) rootView.findViewById(R.id.ConstraintLayout_homeConstraintLayout);
        ConstraintLayout_timeLimitConstraintLayout = (ConstraintLayout) rootView.findViewById(R.id.ConstraintLayout_timeLimitConstraintLayout);
        Button_submitStoreManager = (Button) rootView.findViewById(R.id.Button_submitStoreManager);

        TextView_limitDateTextView = (TextView) rootView.findViewById(R.id.TextView_limitDateTextView);
        TextView_limitBeginTextView = (TextView) rootView.findViewById(R.id.TextView_limitBeginTextView);
        TextView_limitEndTextView = (TextView) rootView.findViewById(R.id.TextView_limitEndTextView);
        TextView_limitNoteTextView = (TextView) rootView.findViewById(R.id.TextView_limitNoteTextView);
        TextView_limitHowLongTextView = (TextView) rootView.findViewById(R.id.TextView_limitHowLongTextView);
        AppCompatSpinner_limitSpinner = (AppCompatSpinner) rootView.findViewById(R.id.AppCompatSpinner_limitSpinner);
        ImageView_cancleImageView = (ImageView) rootView.findViewById(R.id.ImageView_cancleImageView);
        Button_limitAddButton = (Button) rootView.findViewById(R.id.Button_limitAddButton);
        Button_limitCancle = (Button) rootView.findViewById(R.id.Button_limitCancle);

        mProgressDialog = new ProgressDialog(mContext);
        NestedScrollView_editPart.setVisibility(View.VISIBLE);
        ConstraintLayout_timeLimitConstraintLayout.setVisibility(View.GONE);

    }

    @Override
    protected void InitData(View rootView) {
        UserInfo userInfo = DataUtil.GetUserInfoData(mContext);
        userId = userInfo.getId();
        token = userInfo.getToken();
        storeId = getActivity().getIntent().getStringExtra("placeId");
    }

    @Override
    protected void InitOther(View rootView) {
        setClick();
        SetTimeLimitPart();
        setRecyclerView();
        reloadAdapter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_store_manager_basic_part;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_ADDRESS:
                Address addressUse = JSONObject.parseObject(data.getStringExtra("addressStoreJsonStr"), Address.class);
                storeManagerBasicFragmentBean.getAddressInfos().setId(addressUse.getId());
                storeManagerBasicFragmentBean.getAddressInfos().setCity(addressUse.getCity());
                storeManagerBasicFragmentBean.getAddressInfos().setCitycode(addressUse.getCitycode());
                storeManagerBasicFragmentBean.getAddressInfos().setDistrict(addressUse.getDistrict());
                storeManagerBasicFragmentBean.getAddressInfos().setFormatAddress(addressUse.getFormatAddress());
                storeManagerBasicFragmentBean.getAddressInfos().setNeighborhood(addressUse.getNeighborhood());
                storeManagerBasicFragmentBean.getAddressInfos().setProvince(addressUse.getProvince());
                storeManagerBasicFragmentBean.getAddressInfos().setTowncode(addressUse.getTowncode());
                storeManagerBasicFragmentBean.getAddressInfos().setTownship(addressUse.getTownship());
                storeManagerBasicFragmentBean.getAddressInfos().setLatitude(addressUse.getLatitude());
                storeManagerBasicFragmentBean.getAddressInfos().setLongitude(addressUse.getLongitude());
                storeManagerBasicFragmentBean.getAddressInfos().setAddressClass(ADDRESS_CLASS_STORE);
                storeManagerBasicFragmentBean.getAddressInfos().setOwnerId(userId);
                storeManagerBasicFragmentBean.setAddressInfo(addressUse.getFormatAddress() + addressUse.getTownship());
                TextView_addressInfo.setText(storeManagerBasicFragmentBean.getAddressInfo());
                break;
        }
    }

    private void handleMsg(Message msg) {
        switch (msg.arg1) {
            case MSG_UPDATE_VIEW:
                UpdateView();
                break;
            case MSG_UPDATE_NAME_SUCCESS:
                builder.setMessage("名称已修改，提交后需等待审核通过后方可使用。");
                TextView_name.setText(storeManagerBasicFragmentBean.getName());
                break;
            case MSG_UPDATE_NAME_FAIL:
                builder.setMessage("名称不能为空。");
                break;
            case MSG_UPDATE_ADDRESS_SUCCESS:
                builder.setMessage("位置备注已修改，提交后即可生效。");
                TextView_addressNote.setText(storeManagerBasicFragmentBean.getAddressNote());
                break;
            case MSG_UPDATE_ADDRESS_FAIL:
                builder.setMessage("已清空位置备注信息");
                TextView_addressNote.setText(storeManagerBasicFragmentBean.getAddressNote());
                break;
            case MSG_UPDATE_NOTES_SUCCESS:
                builder.setMessage("场地备注已修改，提交后即可生效。");
                TextView_notes.setText(storeManagerBasicFragmentBean.getNotes());
                break;
            case MSG_UPDATE_NOTES_FAIL:
                builder.setMessage("已清空位置备注信息");
                TextView_notes.setText(storeManagerBasicFragmentBean.getNotes());
                break;
            case MSG_UPDATE_MAX_PEOPLE_SUCCESS:
                builder.setMessage("场地最大容纳人数已修改，提交后即可生效。");
                TextView_maxPeople.setText(storeManagerBasicFragmentBean.getMaxPeople());
                break;
            case MSG_UPDATE_MAX_PEOPLE_FAIL:
                builder.setMessage("最大容纳人数更新失败，已还原");
                TextView_maxPeople.setText(oldStoreManagerBasicFragmentBean.getMaxPeople());
                break;
            case MSG_UPDATE_MAX_DESK_SUCCESS:
                builder.setMessage("场地最大容纳 组局数已修改，提交后即可生效。");
                TextView_maxDesk.setText(storeManagerBasicFragmentBean.getMaxDesk());
                break;
            case MSG_UPDATE_MAX_DESK_FAIL:
                builder.setMessage("最大容纳 组局数更新失败，已还原");
                TextView_maxDesk.setText(oldStoreManagerBasicFragmentBean.getMaxDesk());
                break;
            case MSG_UPDATE_HARDWARE_NOTE_SUCCESS:
                builder.setMessage("场地设施描述已修改，提交后即可生效。");
                TextView_hardwareNote.setText(storeManagerBasicFragmentBean.getHardwareNote());
                break;
            case MSG_UPDATE_HARDWARE_NOTE_FAIL:
                builder.setMessage("场地设施描述更新失败，已还原");
                TextView_hardwareNote.setText(oldStoreManagerBasicFragmentBean.getHardwareNote());
                break;
            case MSG_UPDATE_NONE_CHANGE:
                builder.setMessage("无变更信息，无法提交修改请求");
                break;
            case MSG_UPDATE_SUCCESS:
                builder.setMessage("修改成功");
                getActivity().finish();
                break;
            case MSG_UPDATE_FAIL:
                builder.setTitle("修改失败");
                builder.setMessage("请尝试重新提交" + msg.obj);
                break;
            case MSG_UPDATE_SUBMIT:
                mProgressDialog.setTitle("修改信息已提交");
                mProgressDialog.setMessage("正在等待服务器处理...");
                mProgressDialog.show();
                break;
        }
        builder.show();
    }

    private void UpdateView() {
        UpdateBasicView(storeManagerBasicFragmentBean);
        UpdateRecyclerView();
    }

    private void UpdateBasicView(StoreManagerBasicFragmentBean o) {
        TextView_state.setText(o.getState());
        if (!o.getState().equals("营业") && !o.getState().equals("不营业")) {
            Switch_stateSwitch.setVisibility(View.GONE);
            TextView_noteState.setText("审核状态无法修改此项");
        }
        TextView_notes.setText(o.getNotes());
        TextView_name.setText(o.getName());
        TextView_maxPeople.setText(o.getMaxPeople());
        TextView_maxDesk.setText(o.getMaxDesk());
        TextView_hardwareNote.setText(o.getHardwareNote());
        TextView_addressNote.setText(o.getAddressNote());
        TextView_addressInfo.setText(o.getAddressInfo());
        if (o.getIsInside()) {
            AppCompatSpinner_placeEnvSwitch.setSelection(1);
            ConstraintLayout_homeConstraintLayout.setVisibility(View.VISIBLE);
        } else {
            switch (o.getMaxPeople()) {
                case "20":
                    AppCompatSpinner_outsideMaxPeople.setSelection(0);
                    break;
                case "40":
                    AppCompatSpinner_outsideMaxPeople.setSelection(1);
                    break;
                case "100":
                    AppCompatSpinner_outsideMaxPeople.setSelection(2);
                    break;
                case "1000":
                    AppCompatSpinner_outsideMaxPeople.setSelection(3);
                    break;
                case "10000":
                    AppCompatSpinner_outsideMaxPeople.setSelection(4);
                    break;
            }
            AppCompatSpinner_placeEnvSwitch.setSelection(0);
            ConstraintLayout_homeConstraintLayout.setVisibility(View.GONE);
        }
    }

    private void UpdateRecyclerView() {
        basicHardwareAdapter.updateResults(hardwareBeanArrayList);
        basicHardwareAdapter.notifyDataSetChanged();
        workLimitAdapter.updateResults(workLimitBeanArrayList);
        workLimitAdapter.notifyDataSetChanged();
    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        workRecyclerView.setLayoutManager(layoutManager);
        workLimitAdapter = new StoreManagerBasicWorkLimitAdapter(mContext, workLimitBeanArrayList, this);
        workRecyclerView.setAdapter(workLimitAdapter);
        workRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(mContext);
        hardwareRecyclerView.setLayoutManager(layoutManager1);
        basicHardwareAdapter = new StoreManagerBasicHardwareAdapter(mContext, hardwareBeanArrayList, this);
        hardwareRecyclerView.setAdapter(basicHardwareAdapter);
        hardwareRecyclerView.setHasFixedSize(true);
    }

    private void setClick() {
        Button_submitStoreManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                if (JSONObject.toJSONString(storeManagerBasicFragmentBean).equals(JSONObject.toJSONString(oldStoreManagerBasicFragmentBean))) {
                    msg.arg1 = MSG_UPDATE_NONE_CHANGE;
                    handler.sendMessage(msg);
                } else {
                    msg.arg1 = MSG_UPDATE_SUBMIT;
                    SendBasicChange();
                }
            }
        });
        AppCompatSpinner_placeEnvSwitch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://       室外
                        storeManagerBasicFragmentBean.setIsInside(false);
                        ConstraintLayout_homeConstraintLayout.setVisibility(View.GONE);
                        break;
                    case 1://       室内
                        storeManagerBasicFragmentBean.setIsInside(true);
                        ConstraintLayout_homeConstraintLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        AppCompatSpinner_outsideMaxPeople.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        storeManagerBasicFragmentBean.setMaxPeople(String.valueOf(20));
                        break;
                    case 1:
                        storeManagerBasicFragmentBean.setMaxPeople(String.valueOf(40));
                        break;
                    case 2:
                        storeManagerBasicFragmentBean.setMaxPeople(String.valueOf(100));
                        break;
                    case 3:
                        storeManagerBasicFragmentBean.setMaxPeople(String.valueOf(1000));
                        break;
                    case 4:
                        storeManagerBasicFragmentBean.setMaxPeople(String.valueOf(10000));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        TextView_editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(mContext);
                editText = new EditText(mContext);
                editText.setText(storeManagerBasicFragmentBean.getName());
                alertDialog.setView(editText);
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.setTitle("请输入新名称");
                alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Message msg = new Message();
                        if (editText.getText().toString().length() > 0) {
                            storeManagerBasicFragmentBean.setName(editText.getText().toString());
                            msg.arg1 = MSG_UPDATE_NAME_SUCCESS;
                        } else {
                            msg.arg1 = MSG_UPDATE_NAME_FAIL;
                            msg.obj = "昵称不能为空";
                        }
                        handler.sendMessage(msg);
                    }
                });
                alertDialog.show();
            }
        });
        Switch_stateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Switch_stateSwitch.setText("营业");
                    TextView_state.setText("营业");
                    storeManagerBasicFragmentBean.setState("营业");
                } else {
                    Switch_stateSwitch.setText("不营业");
                    TextView_state.setText("不营业");
                    storeManagerBasicFragmentBean.setState("不营业");
                }
            }
        });
        TextView_editeAddressNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(mContext);
                editText = new EditText(mContext);
                editText.setText(storeManagerBasicFragmentBean.getAddressNote());
                alertDialog.setView(editText);
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.setTitle("请输入对地址的新备注");
                alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Message msg = new Message();
                        if (editText.getText().toString().length() > 0) {
                            storeManagerBasicFragmentBean.setAddressNote(editText.getText().toString());
                            msg.arg1 = MSG_UPDATE_ADDRESS_SUCCESS;
                        } else {
                            msg.arg1 = MSG_UPDATE_ADDRESS_FAIL;
                        }
                        handler.sendMessage(msg);
                    }
                });
                alertDialog.show();
            }
        });
        TextView_editeAddressInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GaoDeMapActivity.class);
                intent.putExtra("whereCome", "CreateStoreActivity");
                startActivityForResult(intent, ADD_ADDRESS);
            }
        });
        TextView_editNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(mContext);
                editText = new EditText(mContext);
                editText.setText(storeManagerBasicFragmentBean.getNotes());
                alertDialog.setView(editText);
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.setTitle("请输入对场地的新备注");
                alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Message msg = new Message();
                        if (editText.getText().toString().length() > 0) {
                            storeManagerBasicFragmentBean.setNotes(editText.getText().toString());
                            msg.arg1 = MSG_UPDATE_NOTES_SUCCESS;
                        } else {
                            msg.arg1 = MSG_UPDATE_NOTES_FAIL;
                        }
                        handler.sendMessage(msg);
                    }
                });
                alertDialog.show();
            }
        });
        TextView_editMaxPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(mContext);
                editText = new EditText(mContext);
                editText.setText(storeManagerBasicFragmentBean.getMaxPeople());
                alertDialog.setView(editText);
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.setTitle("请输入场地最大容纳人数");
                alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Message msg = new Message();
                        if (editText.getText().toString().length() > 0) {
                            storeManagerBasicFragmentBean.setMaxPeople(editText.getText().toString());
                            msg.arg1 = MSG_UPDATE_MAX_PEOPLE_SUCCESS;
                        } else {
                            msg.arg1 = MSG_UPDATE_MAX_PEOPLE_FAIL;
                        }
                        handler.sendMessage(msg);
                    }
                });
                alertDialog.show();
            }
        });
        TextView_editDeskMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(mContext);
                editText = new EditText(mContext);
                editText.setText(storeManagerBasicFragmentBean.getMaxDesk());
                alertDialog.setView(editText);
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.setTitle("请输入场地最大容纳组局数（一桌一局）");
                alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Message msg = new Message();
                        if (editText.getText().toString().length() > 0) {
                            storeManagerBasicFragmentBean.setMaxDesk(editText.getText().toString());
                            msg.arg1 = MSG_UPDATE_MAX_DESK_SUCCESS;
                        } else {
                            msg.arg1 = MSG_UPDATE_MAX_DESK_FAIL;
                        }
                        handler.sendMessage(msg);
                    }
                });
                alertDialog.show();
            }
        });
        TextView_editHardware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(mContext);
                editText = new EditText(mContext);
                alertDialog.setView(editText);
                editText.setText(storeManagerBasicFragmentBean.getHardwareNote());
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.setTitle("请输入更新的设施描述");
                alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Message msg = new Message();
                        if (editText.getText().toString().length() > 0) {
                            storeManagerBasicFragmentBean.setHardwareNote(editText.getText().toString());
                            msg.arg1 = MSG_UPDATE_HARDWARE_NOTE_SUCCESS;
                        } else {
                            msg.arg1 = MSG_UPDATE_HARDWARE_NOTE_FAIL;
                        }
                        handler.sendMessage(msg);
                    }
                });
                alertDialog.show();
            }
        });
    }

    private void SetTimeLimitPart() {
        TextView_editBusinessTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NestedScrollView_editPart.setVisibility(View.GONE);
                ConstraintLayout_timeLimitConstraintLayout.setVisibility(View.VISIBLE);
            }
        });
        AppCompatSpinner_limitSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        TextView_limitDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        TextView_limitBeginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        TextView_limitHowLongTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        TextView_limitEndTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        TextView_limitNoteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ImageView_cancleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView_limitDateTextView.setText("点击设置");
                TextView_limitBeginTextView.setText("点击设置");
                TextView_limitHowLongTextView.setText("点击设置");
                TextView_limitEndTextView.setText("点击设置");
                TextView_limitNoteTextView.setText("");
                NestedScrollView_editPart.setVisibility(View.GONE);
                ConstraintLayout_timeLimitConstraintLayout.setVisibility(View.VISIBLE);
            }
        });
        Button_limitAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Button_limitCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView_limitDateTextView.setText("点击设置");
                TextView_limitBeginTextView.setText("点击设置");
                TextView_limitHowLongTextView.setText("点击设置");
                TextView_limitEndTextView.setText("点击设置");
                TextView_limitNoteTextView.setText("");
                NestedScrollView_editPart.setVisibility(View.GONE);
                ConstraintLayout_timeLimitConstraintLayout.setVisibility(View.VISIBLE);
            }
        });


    }

    private void reloadAdapter() {
        addSubscribe(RetrofitServiceManager.getInstance().creat(StoreApiService.class)
                .ManagerGetOneStoreInfo("basic", userId, storeId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(mContext) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            switch (code) {
                                case RespCodeNumber.SUCCESS:
                                    storeManagerBasicFragmentBean = JSONObject.parseObject(responseJson.getJSONObject("data").toJSONString()
                                            , StoreManagerBasicFragmentBean.class);
                                    oldStoreManagerBasicFragmentBean = JSONObject.parseObject(JSONObject.toJSONString(storeManagerBasicFragmentBean), StoreManagerBasicFragmentBean.class);
                                    Message msg = new Message();
                                    msg.arg1 = MSG_UPDATE_VIEW;
                                    handler.sendMessage(msg);
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
                        getActivity().finish();
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete");
                    }
                }));
    }

    private void SendBasicChange() {

        RequestBody multiBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("json", JSONObject.toJSONString(storeManagerBasicFragmentBean))
                .build();
        addSubscribe(RetrofitServiceManager.getInstance().creat(StoreApiService.class)
                .ManagerUpdateStoreInfo("basic"
                        , userId
                        , storeId
                        , token
                        , multiBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(mContext) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            switch (code) {
                                case RespCodeNumber.SUCCESS:
                                    Message msg = new Message();
                                    msg.arg1 = MSG_UPDATE_SUCCESS;
                                    handler.sendMessage(msg);
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
                        Message msg = new Message();
                        msg.arg1 = MSG_UPDATE_FAIL;
                        msg.obj = e.toString();
                        handler.sendMessage(msg);
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        mProgressDialog.dismiss();
                    }
                }));
    }

    @Override
    public void onItemClickListener(int position) {

    }

    @Override
    public void onItemLongClickListener(int position) {

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            handleMsg(msg);
        }
    };
}
