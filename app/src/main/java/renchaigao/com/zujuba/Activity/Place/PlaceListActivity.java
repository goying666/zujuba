package renchaigao.com.zujuba.Activity.Place;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import renchaigao.com.zujuba.Activity.Adapter.CommonViewHolder;
import renchaigao.com.zujuba.Activity.BaseActivity;
import renchaigao.com.zujuba.Activity.TeamPart.TeamCreateActivity;
import renchaigao.com.zujuba.Fragment.Adapter.PlaceCardAdapter;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.PlaceApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;
import renchaigao.com.zujuba.widgets.DividerItemDecoration;

import static renchaigao.com.zujuba.Activity.TeamPart.TeamCreateActivity.CREATE_TEAM_ADDRESS_STORE;

/******     该活动用于创建Team时选择地点，展示地点list使用。    ******/
public class PlaceListActivity extends BaseActivity implements CommonViewHolder.onItemCommonClickListener {

    final static public int PLACE_LIST_ACTIVITY_CODE = 103;
    final private String TAG = "PlaceListActivity";
    //    private ArrayList<StoreInfo> mStoreInfo;
    private ArrayList<JSONObject> mStoreInfo;
    private TextView place_list_user_place_text;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    //    private HallFragmentAdapter hallFragmentAdapter;
    private LinearLayoutManager layoutManager;
    String userId;
    private String reloadFlag;

    @Override
    public void onItemClickListener(int position) {
        Intent intent = new Intent(PlaceListActivity.this, TeamCreateActivity.class);
        intent.putExtra("place", JSONObject.toJSONString(mStoreInfo.get(position)));
//        intent.putExtra("name", JSONObject.toJSONString(mStoreInfo.get(position).get("placeName")));
        setResult(PLACE_LIST_ACTIVITY_CODE, intent);
        Toast.makeText(this, "onItemLongClickListener :" + position, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onItemLongClickListener(int position) {
    }

    //    public interface OnItemClickListener {
//        void onItemClick(View view, int position);
//    }
    private void setSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.place_list_SwipeRefreshLayout); //设置没有item动画
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadAdapter();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadAdapter();
    }

    //    private void setToolBar() {
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.hide();
//        }
//    }
//    private void refresh() {
////        super.onResume();
//        hallFragmentAdapter.updateResults(mStoreInfo);
//        hallFragmentAdapter.notifyDataSetChanged();
//    }
    private PlaceCardAdapter placeListAdapter;

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.place_list_recyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        hallFragmentAdapter = new HallFragmentAdapter(this);


        placeListAdapter = new PlaceCardAdapter(this, mStoreInfo, this);
        recyclerView.setAdapter(placeListAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    @Override
    protected void InitView() {
        mStoreInfo = new ArrayList();
        initeData();
        setContentView(R.layout.activity_place_list);
//        setToolBar();
        setRecyclerView();

        setSwipeRefresh();
    }

    @Override
    protected void InitData() {

    }

    @Override
    protected void InitOther() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_place_list;
    }

    private UserInfo userInfo;

    private void initeData() {
        userInfo = DataUtil.GetUserInfoData(this);
        userId = userInfo.getId();
    }

    public void reloadAdapter() {
        RequestBody multiBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("json", "")
                .build();
//        MultipartBody.Part.createFormData("json", "aaa");
        addSubscribe(RetrofitServiceManager.getInstance().creat(PlaceApiService.class)
                .FourParameterBodyPost("store",
                        "getnear",
                        userInfo.getId(),
                        "null",
                        (MultipartBody) multiBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(PlaceListActivity.this) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            JSONArray responseJsonData = responseJson.getJSONArray("data");
                            switch (code) {
                                case RespCodeNumber.SUCCESS: //在数据库中更新用户数据出错；
                                    for (Object m : responseJsonData) {
                                        mStoreInfo.add(JSONObject.parseObject(JSONObject.toJSONString(m), JSONObject.class));
                                    }
                                    placeListAdapter.updateResults(mStoreInfo);
                                    placeListAdapter.notifyDataSetChanged();
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
}
