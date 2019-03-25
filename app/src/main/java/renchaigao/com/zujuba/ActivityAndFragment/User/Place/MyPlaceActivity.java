package renchaigao.com.zujuba.ActivityAndFragment.User.Place;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.BaseActivity;
import renchaigao.com.zujuba.ActivityAndFragment.Store.Create.CreateStoreActivity;
import renchaigao.com.zujuba.ActivityAndFragment.Store.StoreActivity;
import renchaigao.com.zujuba.ActivityAndFragment.Store.StoreManagerActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.UserApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;
import renchaigao.com.zujuba.widgets.DividerItemDecoration;

public class MyPlaceActivity extends BaseActivity implements CommonViewHolder.onItemCommonClickListener {

    private static String TAG = "MyPlaceActivity";

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private UserPlaceListPageAdapter userPlaceListPageAdapter;

    private TextView state_open, state_create, state_close;
    UserInfo userInfo = new UserInfo();
    ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<>();

    // Handler内部类，它的引用在子线程中被使用，发送mesage，被handlerMesage方法接收
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            JSONObject jsonObject = (JSONObject) msg.obj;
            UpdateView(jsonObject);
        }
    };

    private void UpdateView(JSONObject jsonObject) {

        secondTitleTextView.setText(String.format("%s/5", jsonObjectArrayList.size()));
        userPlaceListPageAdapter.updateResults(jsonObjectArrayList);
        userPlaceListPageAdapter.notifyDataSetChanged();
        state_open.setText(jsonObject.getString("state_open"));
        state_create.setText(String.valueOf(Integer.valueOf(jsonObject.getString("state_create")) + Integer.valueOf(jsonObject.getString("state_wait"))));
        state_close.setText(jsonObject.getString("state_close"));
    }


    @Override
    protected void InitView() {
        initToolbar();
        state_open = (TextView) findViewById(R.id.doing);
        state_create = (TextView) findViewById(R.id.textView55);
        state_close = (TextView) findViewById(R.id.textView56);
        recyclerView = (RecyclerView) findViewById(R.id.a_user_store_list_page_recyclerview);
    }

    @Override
    protected void InitData() {
        userInfo = DataUtil.GetUserInfoData(this);
    }

    @Override
    protected void InitOther() {
        setRecyclerView();
        reloadAdapter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_place_list_page;
    }

    private TextView secondTitleTextView;
    private ConstraintLayout toolbar;

    private void initToolbar() {
        toolbar = (ConstraintLayout) findViewById(R.id.my_place_toolbar);
        ((TextView) toolbar.findViewById(R.id.toolbarTitle)).setText("我的场地");
        secondTitleTextView = (TextView) toolbar.findViewById(R.id.toolbarSecondTitle);
        ImageView goback = (ImageView) toolbar.findViewById(R.id.toolbarBack);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setRecyclerView() {
        layoutManager = new LinearLayoutManager(MyPlaceActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        userPlaceListPageAdapter = new UserPlaceListPageAdapter(this, jsonObjectArrayList,
                this, R.layout.card_place_list_page);

        recyclerView.setAdapter(userPlaceListPageAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(MyPlaceActivity.this, DividerItemDecoration.VERTICAL_LIST));
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    private void reloadAdapter() {
        addSubscribe(RetrofitServiceManager.getInstance().creat(UserApiService.class)
                .GetUserPlaceList(
                        userInfo.getId(), userInfo.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(MyPlaceActivity.this) {
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
                                    jsonObjectArrayList = new ArrayList<>(responseJsonData.getJSONArray("array").toJavaList(JSONObject.class));

                                    handler.sendMessage(msg);
//                                    userPlaceListPageActivityAdapter.updateResults(jsonObjectArrayList);
//                                    userPlaceListPageActivityAdapter.notifyDataSetChanged();
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


    public void AddNewPlace(View view) {
        Intent intent = new Intent(MyPlaceActivity.this, CreateStoreActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClickListener(int position) {
        Intent intent = new Intent();
        switch (jsonObjectArrayList.get(position).getString("state")) {
//            case "创建中":
//                intent = new Intent(MyPlaceActivity.this, UserPlaceManagerActivity.class);
//                intent.putExtra("storeinfo", JSONObject.toJSONString(jsonObjectArrayList.get(position)));
//                break;
            case "审核中":
                intent = new Intent(MyPlaceActivity.this, StoreManagerActivity.class);
                intent.putExtra("placeId", jsonObjectArrayList.get(position).getString("placeid"));
                break;
            case "营业中":
                intent = new Intent(MyPlaceActivity.this, StoreActivity.class);
                intent.putExtra("placeId", jsonObjectArrayList.get(position).getString("placeid"));
                break;
            case "停业中":
//                intent = new Intent(MyPlaceActivity.this, UserPlaceManagerActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onItemLongClickListener(int position) {
    }
}
