package renchaigao.com.zujuba.Activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.store.StoreInfo;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import renchaigao.com.zujuba.Activity.TeamPart.TeamCreateActivity;
import renchaigao.com.zujuba.Fragment.Adapter.HallFragmentAdapter;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.PropertiesConfig;
import renchaigao.com.zujuba.util.http.ApiService;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;
import renchaigao.com.zujuba.widgets.DividerItemDecoration;

import static renchaigao.com.zujuba.Activity.TeamPart.TeamCreateActivity.CREATE_TEAM_ADDRESS_STORE;

/******     该活动用于创建Team时选择地点，展示地点list使用。    ******/
public class PlaceListActivity extends BaseActivity {

    final private String TAG = "PlaceListActivity";
//    private ArrayList<StoreInfo> mStoreInfo;
    private ArrayList<JSONObject> mStoreInfo;
    private TextView place_list_user_place_text;
    private  SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private HallFragmentAdapter hallFragmentAdapter;
    private LinearLayoutManager layoutManager;
    String userId;
    private String reloadFlag;
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
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

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.place_list_recyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        hallFragmentAdapter = new HallFragmentAdapter(this);
        hallFragmentAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final Intent intent = new Intent(PlaceListActivity.this, TeamCreateActivity.class);
                RetrofitServiceManager.getInstance().SetRetrofit(PropertiesConfig.placeServerUrl);
                RequestBody multiBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("json", "")
                        .build();
                addSubscribe(RetrofitServiceManager.getInstance().creat(ApiService.class)
                        .PlaceServicePost("store",
                                "getone",
                                userInfo.getId(),
                                mStoreInfo.get(position).get("placeid").toString(),
                                (MultipartBody) multiBody)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new BaseObserver<ResponseEntity>(PlaceListActivity.this) {
                            @Override
                            public void onNext(ResponseEntity value) {
                                try {
                                    JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                                    int code = Integer.valueOf(responseJson.get("code").toString());
                                    switch (code) {
                                        case RespCodeNumber.SUCCESS: //在数据库中更新用户数据出错；
                                            StoreInfo retStoreInfo  = JSONObject.parseObject(JSONObject.toJSONString(responseJson.get("data")), StoreInfo.class);
                                            intent.putExtra("address",JSONObject.toJSONString(retStoreInfo.getAddressInfo()));
                                            intent.putExtra("storeInfo",JSONObject.toJSONString(retStoreInfo));
                                            intent.putExtra("name", retStoreInfo.getName());
                                            setResult(CREATE_TEAM_ADDRESS_STORE, intent);
                                            finish();
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
                            }

                            @Override
                            public void onComplete() {
                            }
                        }));

            }
        });
        recyclerView.setAdapter(hallFragmentAdapter);
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
    private void initeData(){
        userInfo = DataUtil.GetUserInfoData(this);
        userId = userInfo.getId();
    }

//    @SuppressLint("StaticFieldLeak")
//    public void reloadAdapter1() {
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                reloadFlag = "onPreExecute";
//            }
//
//            @Override
//            protected void onCancelled() {
//                super.onCancelled();
//            }
//
//            @Override
//            protected void onCancelled(Void aVoid) {
//                super.onCancelled(aVoid);
//            }
//
//            @Override
//            protected void onProgressUpdate(Void... values) {
//                super.onProgressUpdate(values);
//            }
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                Log.e(TAG,"doInBackground");
//
//                okhttp();
//                while (!reloadFlag.equals("doInBackground"));
//                return null;
//            }
//            @Override
//            protected void onPostExecute(Void aVoid){
//                super.onPostExecute(aVoid);
//                Log.e(TAG,"onPostExecute");
//                if (PlaceListActivity.this == null)
//                    return;
//                hallFragmentAdapter.updateResults(mStoreInfo);
//                hallFragmentAdapter.notifyDataSetChanged();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        }.execute();
//    }

    public void reloadAdapter() {
        RetrofitServiceManager.getInstance().SetRetrofit(PropertiesConfig.placeServerUrl);
        RequestBody multiBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("json", "")
                .build();
//        MultipartBody.Part.createFormData("json", "aaa");
        addSubscribe(RetrofitServiceManager.getInstance().creat(ApiService.class)
                .PlaceServicePost("store",
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
                                    if (hallFragmentAdapter == null) {
                                        hallFragmentAdapter = new HallFragmentAdapter(PlaceListActivity.this);
                                    }
                                    hallFragmentAdapter.updateResults(mStoreInfo);
                                    hallFragmentAdapter.notifyDataSetChanged();
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
//
//    private void okhttp(){
//        String path = PropertiesConfig.storeServerUrl + "get/storeinfo/" + userId;
////                String path = PropertiesConfig.serverUrl + "store/get/storeinfo/" + JSONObject.parseObject(getActivity().getSharedPreferences("userData",getActivity().MODE_PRIVATE).getString("responseJsonDataString",null)).get("id").toString();
//        OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                .connectTimeout(15, TimeUnit.SECONDS)
//                .readTimeout(15, TimeUnit.SECONDS)
//                .writeTimeout(15, TimeUnit.SECONDS)
//                .retryOnConnectionFailure(true);
//        builder.sslSocketFactory(OkhttpFunc.createSSLSocketFactory());
//        builder.hostnameVerifier(new HostnameVerifier() {
//            @Override
//            public boolean verify(String hostname, SSLSession session) {
//                return true;
//            }
//        });
//        final Request request = new Request.Builder()
//                .url(path)
//                .header("Content-Type", "application/json")
//                .get()
//                .build();
//        builder.build().newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("onFailure", e.toString());
//                reloadFlag = "doInBackground";
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                try {
//                    JSONObject responseJson = JSONObject.parseObject(response.body().string());
//                    String responseJsoStr = responseJson.toJSONString();
//                    int code = Integer.valueOf(responseJson.get("code").toString());
//                        JSONArray responseJsonData = responseJson.getJSONArray("data");
//
//
//                    Log.e(TAG,"onResponse CODE OUT");
//                    Log.e(TAG,"onResponse CODE is" + code);
//
////                            ArrayList<StoreInfo> mStores = new ArrayList<>();
//                    switch (code) {
//                        case 0: //在数据库中更新用户数据出错；
////                            ArrayList<StoreInfo> mStores = new ArrayList();
//                            for (Object m : responseJsonData) {
//                                mStoreInfo.add(JSONObject.parseObject(JSONObject.toJSONString(m), StoreInfo.class));
//                            }
////                                    Log.e("responseJsonData",responseJsonData.toJSONString());
//                            if (hallFragmentAdapter == null) {
//                                hallFragmentAdapter = new HallFragmentAdapter(PlaceListActivity.this);
//                            }
//                            hallFragmentAdapter.updateResults(mStoreInfo);
////                            hallFragmentAdapter.notifyDataSetChanged();
//                            Log.e(TAG,"onResponse");
////                            refresh();
//                            break;
//                    }
//                    reloadFlag = "doInBackground";
////                            swipeRefreshLayout.setRefreshing(false);
//                } catch (Exception e) {
//                }
//            }
//        });
//    }
}
