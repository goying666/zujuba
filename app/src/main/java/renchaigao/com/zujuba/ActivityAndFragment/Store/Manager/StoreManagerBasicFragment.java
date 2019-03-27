package renchaigao.com.zujuba.ActivityAndFragment.Store.Manager;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.PageBean.CardStoreManagerBasicHardwareBean;
import com.renchaigao.zujuba.PageBean.CardStoreManagerBasicWorkLimitBean;
import com.renchaigao.zujuba.PageBean.StoreManagerBasicFragmentBean;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.BaseFragment;
import renchaigao.com.zujuba.ActivityAndFragment.Function.InputActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.StoreApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;

import static renchaigao.com.zujuba.ActivityAndFragment.Function.InputActivity.UPDATE_STORE_NAME;

public class StoreManagerBasicFragment extends BaseFragment implements CommonViewHolder.onItemCommonClickListener {

    private TextView storeName, storeNameIntroduce, updateStoreName, storeState,
            workTimes, restTimes, addNewLimit, addressInfo, updateAddressNote,
            updateAddress, sumOfHardware, hardwareNotes, updateHardwareNote,
            storeNotes, updateStoreNote;
    private TextView addressNotes;
    private Switch updateStoreState;
    private RecyclerView workRecyclerView, hardwareRecyclerView;
    private Button store_manager_submit;
    private StoreManagerBasicHardwareAdapter basicHardwareAdapter;
    private StoreManagerBasicWorkLimitAdapter workLimitAdapter;
    private StoreManagerBasicFragmentBean storeManagerBasicFragmentBean = new StoreManagerBasicFragmentBean();
    private StoreManagerBasicFragmentBean oldStoreManagerBasicFragmentBean = new StoreManagerBasicFragmentBean();
    private ArrayList<CardStoreManagerBasicHardwareBean> hardwareBeanArrayList = new ArrayList<>();
    private ArrayList<CardStoreManagerBasicWorkLimitBean> workLimitBeanArrayList = new ArrayList<>();
    private String userId, token, storeId;

    

    @Override
    protected void InitView(View rootView) {
        storeName = (TextView) rootView.findViewById(R.id.TextView_name);
        storeNameIntroduce = (TextView) rootView.findViewById(R.id.storeNameIntroduce);
        String str = "修改名称需要通过<font color='#FF0000'>工作人员审批</font>";
        storeNameIntroduce.setText(Html.fromHtml(str));
        updateStoreName = (TextView) rootView.findViewById(R.id.TextView_nameEditTextView);
        storeState = (TextView) rootView.findViewById(R.id.TextView_stateTextView);
        workTimes = (TextView) rootView.findViewById(R.id.workTimes);
        restTimes = (TextView) rootView.findViewById(R.id.restTimes);
        addNewLimit = (TextView) rootView.findViewById(R.id.TextView_editBusinessTime);
        addressInfo = (TextView) rootView.findViewById(R.id.TextView_addressInfo);
        updateAddressNote = (TextView) rootView.findViewById(R.id.TextView_editeAddressNote);
        updateAddress = (TextView) rootView.findViewById(R.id.TextView_editeAddressInfo);
        sumOfHardware = (TextView) rootView.findViewById(R.id.sumOfHardware);
        hardwareNotes = (TextView) rootView.findViewById(R.id.TextView_hardwareNote);
        updateHardwareNote = (TextView) rootView.findViewById(R.id.TextView_editHardware);
        storeNotes = (TextView) rootView.findViewById(R.id.TextView_notes);
        updateStoreNote = (TextView) rootView.findViewById(R.id.TextView_editNotes);
        updateStoreState = (Switch) rootView.findViewById(R.id.Switch_stateSwitch);
        workRecyclerView = (RecyclerView) rootView.findViewById(R.id.RecyclerView_businessTimeRecyclerView);
        hardwareRecyclerView = (RecyclerView) rootView.findViewById(R.id.RecyclerView_hardware);
        addressNotes = (TextView) rootView.findViewById(R.id.TextView_addressNote);
        store_manager_submit = (Button) rootView.findViewById(R.id.store_manager_submit);


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
            case UPDATE_STORE_NAME:
                if (data.getBooleanExtra("contentIsChange", false)) {
                    storeName.setText(data.getStringExtra("newContent"));
                    storeManagerBasicFragmentBean.setStoreName(data.getStringExtra("newContent"));
                }
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            UpdateView();
        }
    };

    private void UpdateView() {
        UpdateBasicView(storeManagerBasicFragmentBean);
        UpdateRecyclerView();
    }

    private void UpdateBasicView(Object o) {
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(o));

        sumOfHardware.setText(json.getString("sumOfHardware"));
        storeState.setText(json.getString("storeState"));
        storeNotes.setText(json.getString("storeNotes"));
        storeName.setText(json.getString("storeName"));
        restTimes.setText(json.getString("restTimes"));
        hardwareNotes.setText(json.getString("hardwareNotes"));
        addressNotes.setText(json.getString("addressNotes"));
        addressInfo.setText(json.getString("addressInfo"));
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
        updateStoreName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InputActivity.class);
                intent.putExtra("oldContent", storeManagerBasicFragmentBean.getStoreName());
                intent.putExtra("updateWhat", UPDATE_STORE_NAME);
                startActivityForResult(intent, UPDATE_STORE_NAME);
            }
        });
        updateStoreState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        updateStoreNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        updateHardwareNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        updateAddressNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        updateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        store_manager_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                                    oldStoreManagerBasicFragmentBean = storeManagerBasicFragmentBean;
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
                        Log.e(TAG, "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete");
                    }
                }));
    }

    @Override
    public void onItemClickListener(int position) {

    }

    @Override
    public void onItemLongClickListener(int position) {

    }
}
