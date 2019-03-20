package renchaigao.com.zujuba.ActivityAndFragment.Store;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.PageBean.CardStoreClubFragmentBean;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.Club.ClubInfoActivity;
import renchaigao.com.zujuba.ActivityAndFragment.BaseFragment;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.StoreApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;


public class StoreClubFragment extends BaseFragment implements CommonViewHolder.onItemCommonClickListener {


    private static String TAG = "StoreClubFragment";
    @Override
    public void onItemClickListener(int position) {

        Intent intent = new Intent(mContext, ClubInfoActivity.class);
        intent.putExtra("clubId", cardClubFragmentTipBeanArrayList.get(position).getClubId());
        startActivity(intent);
    }

    @Override
    public void onItemLongClickListener(int position) {

    }


    @Override
    protected void InitView(View rootView) {
        setRecyclerView(rootView);
    }

    private RecyclerView recyclerView;
    StoreClubFragmentAdapter storeClubFragmentAdapter;
    private ArrayList<CardStoreClubFragmentBean> cardClubFragmentTipBeanArrayList = new ArrayList<>();

    private void setRecyclerView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.store_club_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        storeClubFragmentAdapter = new StoreClubFragmentAdapter(mContext, cardClubFragmentTipBeanArrayList, this);
        recyclerView.setAdapter(storeClubFragmentAdapter);
        recyclerView.setHasFixedSize(true);
    }

    public void UpdateViewData() {
        storeClubFragmentAdapter.updateResults(cardClubFragmentTipBeanArrayList);
        storeClubFragmentAdapter.notifyDataSetChanged();
    }

    @Override
    protected void InitData(View rootView) {

        UserInfo userInfo = DataUtil.GetUserInfoData(mContext);
        userId = userInfo.getId();
        token = userInfo.getToken();
        storeId = getActivity().getIntent().getStringExtra("placeId");
    }

    private String userId, token, storeId;
    private int lastTime = 0;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            UpdateViewData();
        }
    };

    @Override
    protected void InitOther(View rootView) {
        reloadAdapter();
    }
    @Override
    public void onResume() {
        super.onResume();
        reloadAdapter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_store_club;
    }


    private void reloadAdapter() {
        addSubscribe(RetrofitServiceManager.getInstance().creat(StoreApiService.class)
                .GetOneStoreInfo("club", userId, storeId, token, lastTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(mContext) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            Message msg = new Message();
                            JSONArray responseJsonData = responseJson.getJSONArray("data");
                            switch (code) {
                                case RespCodeNumber.STORE_INFO_GET_SUCCESS:
                                    msg.arg1 = RespCodeNumber.SUCCESS;
                                    cardClubFragmentTipBeanArrayList.clear();
                                    for (Object m : responseJsonData) {
                                        cardClubFragmentTipBeanArrayList.add(
                                                JSONObject.parseObject(JSONObject.toJSONString(m), CardStoreClubFragmentBean.class));
                                    }
                                    if (cardClubFragmentTipBeanArrayList.size() > 0)
                                        handler.sendMessage(msg);
                                    break;
                                case RespCodeNumber.STORE_INFO_GET_FAIL:
                                    break;
                                case RespCodeNumber.STORE_NOT_FOUND:
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
}
