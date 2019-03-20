package renchaigao.com.zujuba.ActivityAndFragment.Store;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.PageBean.CardStoreTeamFragmentBean;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.TeamPart.TeamActivity;
import renchaigao.com.zujuba.ActivityAndFragment.BaseFragment;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.StoreApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;


public class StoreTeamsFragment extends BaseFragment implements CommonViewHolder.onItemCommonClickListener {

    private String userId, token, storeId;
    private int lastTime = 0;

    private TabLayout store_team_tablayout;
    private RecyclerView recyclerView;
    private StoreTeamFragmentAdapter storeTeamFragmentAdapter;
    ArrayList<CardStoreTeamFragmentBean> cardStoreTeamFragmentBeanArrayList = new ArrayList();

    @Override
    protected void InitView(View rootView) {
        store_team_tablayout = (TabLayout) rootView.findViewById(R.id.store_team_tablayout);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            storeTeamFragmentAdapter.updateResults(cardStoreTeamFragmentBeanArrayList);
            storeTeamFragmentAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void InitOther(View rootView) {
        setRecyclerView(rootView);
        reloadAdapter();
    }

    @Override
    public void onItemClickListener(int position) {
        Intent intent  = new Intent(mContext,TeamActivity.class);
        intent.putExtra("teamId", cardStoreTeamFragmentBeanArrayList.get(position).getTeamId());
        intent.putExtra("whereCome", "StoreActivity");
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadAdapter();
    }

    @Override
    public void onItemLongClickListener(int position) {

    }

    @Override
    protected void InitData(View rootView) {
        UserInfo userInfo = DataUtil.GetUserInfoData(mContext);
        userId = userInfo.getId();
        token = userInfo.getToken();
        storeId = getActivity().getIntent().getStringExtra("placeId");
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_store_team;
    }


    private void setRecyclerView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.store_team_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        storeTeamFragmentAdapter = new StoreTeamFragmentAdapter(mContext, cardStoreTeamFragmentBeanArrayList, this);
        recyclerView.setAdapter(storeTeamFragmentAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void reloadAdapter() {
        addSubscribe(RetrofitServiceManager.getInstance().creat(StoreApiService.class)
                .GetOneStoreInfo("team", userId, storeId, token, lastTime)
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
                                    cardStoreTeamFragmentBeanArrayList.clear();
                                    for (Object m : responseJsonData) {
                                        cardStoreTeamFragmentBeanArrayList.add(
                                                JSONObject.parseObject(JSONObject.toJSONString(m), CardStoreTeamFragmentBean.class));
                                    }
                                    if (cardStoreTeamFragmentBeanArrayList.size() > 0)
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
