package renchaigao.com.zujuba.Activity.User;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.mongoDB.info.team.TeamInfo;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import renchaigao.com.zujuba.Fragment.Adapter.TeamFragmentAdapter;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.OkhttpFunc;
import renchaigao.com.zujuba.util.PropertiesConfig;
import renchaigao.com.zujuba.widgets.DividerItemDecoration;

public class UserTeamActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private TeamFragmentAdapter teamFragmentAdapter;

    private String userId;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_create_team);
        userInfo = DataUtil.GetUserInfoData(this);
        userId= userInfo.getId();
        setRecyclerView();
        reloadAdapter();
    }


    private void setRecyclerView() {
        recyclerView = findViewById(R.id.a_u_createTeam_RecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        teamFragmentAdapter = new TeamFragmentAdapter(this);
//        teamFragmentAdapter.setOnItemClickListener(new PlaceListActivity.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                final Intent intent = new Intent(getActivity(), TeamCreateActivity.class);
//                intent.putExtra("address",JSONObject.toJSONString(mStoreInfo.get(position).getAddressInfo()));
//                intent.putExtra("storeInfo",JSONObject.toJSONString(mStoreInfo.get(position)));
//                intent.putExtra("name", mStoreInfo.get(position).getName());
//                setResult(CREATE_TEAM_ADDRESS_STORE, intent);
//            }
//        });
        recyclerView.setAdapter(teamFragmentAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }
    @SuppressLint("StaticFieldLeak")
    public void reloadAdapter() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onCancelled() {
                super.onCancelled();
            }

            @Override
            protected void onCancelled(Void aVoid) {
                super.onCancelled(aVoid);
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected Void doInBackground(Void... params) {
                String path = PropertiesConfig.teamServerUrl + "get/" + userId;
                OkHttpClient.Builder builder = new OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .writeTimeout(15, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true);
                builder.sslSocketFactory(OkhttpFunc.createSSLSocketFactory());
                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
                final Request request = new Request.Builder()
                        .url(path)
                        .header("Content-Type", "application/json")
                        .get()
                        .build();
                builder.build().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("onFailure", e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(response.body().string());
                            String responseJsoStr = responseJson.toJSONString();
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            JSONArray responseJsonData = responseJson.getJSONArray("data");


//                            ArrayList<StoreInfo> mStores = new ArrayList<>();
                            switch (code) {
                                case 0: //在数据库中更新用户数据出错；
                                    ArrayList<TeamInfo> mTeam = new ArrayList();
                                    for (Object m : responseJsonData) {
                                        mTeam.add(JSONObject.parseObject(JSONObject.toJSONString(m), TeamInfo.class));
                                    }
//                                    Log.e("responseJsonData",responseJsonData.toJSONString());
                                    if (teamFragmentAdapter == null) {
                                        teamFragmentAdapter = new TeamFragmentAdapter(UserTeamActivity.this);
                                    }
                                    teamFragmentAdapter.updateResults(mTeam);

                                    break;
                            }
//                            swipeRefreshLayout.setRefreshing(false);
                        } catch (Exception e) {
                        }
                    }

                });
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                teamFragmentAdapter.notifyDataSetChanged();
            }
        }.execute();
    }


}
