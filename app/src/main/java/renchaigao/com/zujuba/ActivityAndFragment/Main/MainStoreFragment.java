package renchaigao.com.zujuba.ActivityAndFragment.Main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.Main.Adapter.MainStoreFragmentAdapter;
import renchaigao.com.zujuba.ActivityAndFragment.BaseFragment;
import renchaigao.com.zujuba.ActivityAndFragment.Store.StoreActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.StoreApiService;
import renchaigao.com.zujuba.util.DataFunctions.DataSort;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;
import renchaigao.com.zujuba.widgets.DividerItemDecoration;

public class MainStoreFragment extends BaseFragment implements CommonViewHolder.onItemCommonClickListener {

    private ArrayList<JSONObject> updateJsonList = new ArrayList<>();
    private UserInfo userInfo;
    final private String TAG = "MainStoreFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private MainStoreFragmentAdapter mainStoreFragmentAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private TabLayout tableLayout;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case TABSELECTED_CHANGE:
                    UpdateViewData();
                    break;
                case RespCodeNumber.SUCCESS:
                    UpdateViewData();
                    break;
            }
        }
    };

    private void UpdateViewData() {
        DataSort<JSONObject> dataSort = new DataSort<>();
        switch (tabSelecte) {
            case 0:
//                距离排序
                updateJsonList = dataSort.SequenceSort(updateJsonList, "realDistance");
                break;
            case 1:
//                星级排序
                updateJsonList = dataSort.SequenceSort(updateJsonList, "realStar");
                break;
            case 2:
//                热度/评价排序
                updateJsonList = dataSort.SequenceSort(updateJsonList, "realScore");
                break;
            case 3:
//                筛选排序
                updateJsonList = dataSort.SequenceSort(updateJsonList, "");
                break;
        }
        mainStoreFragmentAdapter.updateResults(updateJsonList);
        mainStoreFragmentAdapter.notifyDataSetChanged();
    }

    @Override
    protected void InitView(View rootView) {
        tableLayout = (TabLayout) rootView.findViewById(R.id.hall_tablayout);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.hall_SwipeRefreshLayout); //设置没有item动画
        recyclerView = (RecyclerView) rootView.findViewById(R.id.hall_recyclerView);
    }

    @Override
    protected void InitData(View rootView) {
        userInfo = DataUtil.GetUserInfoData(mContext);
    }

    @Override
    protected void InitOther(View rootView) {
        setSwipeRefresh(rootView);
        setRecyclerView(rootView);
        reloadAdapter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hall;
    }

    private void setSwipeRefresh(View view) {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadAdapter();
            }
        });
    }

    private int tabSelecte = 0;
    final static private int TABSELECTED_CHANGE = 1003;
    private void setTableLayoutView(View view) {
        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabSelecte = tab.getPosition();
                Message msg = new Message();
                msg.arg1 = TABSELECTED_CHANGE;
                handler.sendMessage(msg);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setRecyclerView(View view) {
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        mainStoreFragmentAdapter = new MainStoreFragmentAdapter(mContext, updateJsonList, this);
        recyclerView.setAdapter(mainStoreFragmentAdapter);
        recyclerView.setHasFixedSize(true);
    }


    private void reloadAdapter() {
        addSubscribe(RetrofitServiceManager.getInstance().creat(StoreApiService.class)
                .GetNearlyStoreInfo(userInfo.getId()
                        , userInfo.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(mContext) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            JSONArray responseJsonData = responseJson.getJSONArray("data");
                            switch (code) {
                                case RespCodeNumber.SUCCESS: //在数据库中更新用户数据出错；
                                    updateJsonList = new ArrayList<>(responseJsonData.toJavaList(JSONObject.class));
                                    mainStoreFragmentAdapter.updateResults(updateJsonList);
                                    mainStoreFragmentAdapter.notifyDataSetChanged();
                                    Log.e(TAG, "onResponse");
                                    break;
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }

                    }

                    @Override
                    protected void onSuccess(ResponseEntity responseEntity) {

                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        swipeRefreshLayout.setRefreshing(false);
                        Log.e(TAG, "onError");
                    }

                    @Override
                    public void onComplete() {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.e(TAG, "onComplete");
                    }
                }));
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadAdapter();
    }


    @Override
    public void onItemClickListener(int position) {
        Intent intent = new Intent(mContext, StoreActivity.class);
        intent.putExtra("placeId", updateJsonList.get(position).getString("placeid"));
        mContext.startActivity(intent);
    }

    @Override
    public void onItemLongClickListener(int position) {

    }

}
