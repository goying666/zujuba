package renchaigao.com.zujuba.Activity.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.Activity.Adapter.CommonViewHolder;
import renchaigao.com.zujuba.Activity.Adapter.UserPlaceListPageAdapter;
import renchaigao.com.zujuba.Activity.BaseActivity;
import renchaigao.com.zujuba.Activity.Place.CreateStoreActivity;
import renchaigao.com.zujuba.Activity.Place.UserPlaceManagerActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.UserApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;
import renchaigao.com.zujuba.widgets.DividerItemDecoration;

public class UserPlaceListPageActivity extends BaseActivity implements CommonViewHolder.onItemCommonClickListener{

    private static String TAG = "UserPlaceListPageActivity";

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
//    private UserPlaceListPageActivityAdapter userPlaceListPageActivityAdapter;
    private UserPlaceListPageAdapter userPlaceListPageAdapter;

    private TextView state_open, state_create, state_close;
    private Button button;
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
        state_open.setText(jsonObject.getString("state_open"));
        state_create.setText(String.valueOf(Integer.valueOf(jsonObject.getString("state_create")) + Integer.valueOf(jsonObject.getString("state_wait"))));
        state_close.setText(jsonObject.getString("state_close"));
    }


    @Override
    protected void InitView() {
        state_open = (TextView) findViewById(R.id.textView54);
        state_create = (TextView) findViewById(R.id.textView55);
        state_close = (TextView) findViewById(R.id.textView56);

        button = (Button) findViewById(R.id.button);
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


    private void setRecyclerView() {
        layoutManager = new LinearLayoutManager(UserPlaceListPageActivity.this);
        recyclerView.setLayoutManager(layoutManager);
//        userPlaceListPageActivityAdapter = new UserPlaceListPageActivityAdapter(UserPlaceListPageActivity.this);
        userPlaceListPageAdapter = new UserPlaceListPageAdapter(this,jsonObjectArrayList,this,R.layout.card_place_list_page);

        recyclerView.setAdapter(userPlaceListPageAdapter);
//        recyclerView.setAdapter(userPlaceListPageActivityAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(UserPlaceListPageActivity.this, DividerItemDecoration.VERTICAL_LIST));
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }
    private void reloadAdapter() {
//        RequestBody multiBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("json", "")
//                .build();
        addSubscribe(RetrofitServiceManager.getInstance().creat(UserApiService.class)
                .PlaceServiceGet("user",
                        "allcreate",
                        userInfo.getId(),
                        "null")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(UserPlaceListPageActivity.this) {
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
                                    jsonObjectArrayList = new ArrayList<>(responseJsonData.getJSONArray("array").toJavaList(JSONObject.class));
                                    userPlaceListPageAdapter.updateResults(jsonObjectArrayList);
                                    userPlaceListPageAdapter.notifyDataSetChanged();
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
        Intent intent = new Intent(UserPlaceListPageActivity.this, CreateStoreActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClickListener(int position) {
        Intent intent = new Intent(UserPlaceListPageActivity.this, UserPlaceManagerActivity.class);
        intent.putExtra("storeinfo", JSONObject.toJSONString(jsonObjectArrayList.get(position)));
        startActivity(intent);
    }

    @Override
    public void onItemLongClickListener(int position) {
    }
}
