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
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.PageBean.CardStoreTeamFragmentBean;
import com.renchaigao.zujuba.PageBean.CardUserTeamBean;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.Store.Adapter.StoreTeamFragmentAdapter;
import renchaigao.com.zujuba.ActivityAndFragment.TeamPart.TeamActivity;
import renchaigao.com.zujuba.ActivityAndFragment.BaseFragment;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.StoreApiService;
import renchaigao.com.zujuba.util.DataFunctions.DataSort;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;
import renchaigao.com.zujuba.widgets.CustomViewPager;


public class StoreTeamsFragment extends BaseFragment implements CommonViewHolder.onItemCommonClickListener {

    private String userId, token, storeId;
    private int lastTime = 0;
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private StoreTeamFragmentAdapter storeTeamFragmentAdapter;
    private ArrayList<CardStoreTeamFragmentBean> cardStoreTeamFragmentBeanArrayList = new ArrayList();
    private ArrayList<CardStoreTeamFragmentBean> displayList = new ArrayList();
    //    private CustomViewPager store_team_fragment_view;
    private int tabPosition = 0;
    private TextView teamNote;

    @Override
    protected void InitView(View rootView) {
        tabLayout = (TabLayout) rootView.findViewById(R.id.store_team_tablayout);
        teamNote = (TextView) rootView.findViewById(R.id.note_of_this_page);
//        store_team_fragment_view = (CustomViewPager) rootView.findViewById(R.id.store_team_fragment_view);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            UpdateRecyclerView();
        }
    };

    private void UpdateRecyclerView() {
        UpdateDateForView();
        switch (tabPosition) {
            case 0:
                if (displayList.size() == 0){
                    teamNote.setText("今日暂无组局信息");
                    teamNote.setVisibility(View.VISIBLE);
                }else{
                    teamNote.setVisibility(View.GONE);
                }
                break;
            case 1:
                if (displayList.size() == 0){
                    teamNote.setText("明日暂无组局信息");
                    teamNote.setVisibility(View.VISIBLE);
                }else{
                    teamNote.setVisibility(View.GONE);
                }
                break;
            case 2:
                if (displayList.size() == 0){
                    teamNote.setText("本周末暂无组局信息");
                    teamNote.setVisibility(View.VISIBLE);
                }else{
                    teamNote.setVisibility(View.GONE);
                }
                break;
        }
        storeTeamFragmentAdapter.updateResults(displayList);
        storeTeamFragmentAdapter.notifyDataSetChanged();
    }

    @Override
    protected void InitOther(View rootView) {
        setRecyclerView(rootView);
        reloadAdapter();
        initTablayout();
    }


    private void initTablayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
                UpdateDateForView();
                handler.sendMessage(new Message());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void UpdateDateForView() {
        displayList.clear();
        for (CardStoreTeamFragmentBean c : cardStoreTeamFragmentBeanArrayList) {
            switch (tabPosition) {
                case 0:
                    if (c.getIsToday())
                        displayList.add(c);
                    break;
                case 2:
                    if (c.getIsTomorrow())
                        displayList.add(c);
                    break;
                case 1:
                    if (c.getIsWeekend())
                        displayList.add(c);
                    break;
            }
        }
        displayList = new DataSort<CardStoreTeamFragmentBean>().InvertedSort(displayList, "startTimeLong");
    }

    @Override
    public void onItemClickListener(int position) {
        Intent intent = new Intent(mContext, TeamActivity.class);
        intent.putExtra("teamId", cardStoreTeamFragmentBeanArrayList.get(position).getTeamId());
        intent.putExtra("whereCome", "StoreActivity");
        startActivity(intent);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && mIsViewCreated) {
            reloadAdapter();
        }
        super.setUserVisibleHint(isVisibleToUser);
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
                                    if (cardStoreTeamFragmentBeanArrayList.size() > 0) {
                                        handler.sendMessage(msg);
                                    }
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

//    @SuppressLint("ValidFragment")
//    private class storeOwnerTeamFragment extends BaseFragment implements CommonViewHolder.onItemCommonClickListener{
//
//        @Override
//        public void onItemClickListener(int position) {
//
//        }
//
//        @Override
//        public void onItemLongClickListener(int position) {
//
//        }
//
//        @Override
//        protected void InitView(View rootView) {
//
//        }
//
//        @Override
//        protected void InitData(View rootView) {
//
//        }
//
//        @Override
//        protected void InitOther(View rootView) {
//
//        }
//
//        @Override
//        protected int getLayoutId() {
//            return 0;
//        }
//    }
}
