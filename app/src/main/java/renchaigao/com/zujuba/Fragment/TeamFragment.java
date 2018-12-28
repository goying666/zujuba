package renchaigao.com.zujuba.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import renchaigao.com.zujuba.Activity.TeamPart.TeamCreateActivity;
import renchaigao.com.zujuba.Activity.MyTeamActivity;
import renchaigao.com.zujuba.Fragment.Adapter.TeamFragmentAdapter;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.OkhttpFunc;
import renchaigao.com.zujuba.util.PropertiesConfig;
import renchaigao.com.zujuba.widgets.DividerItemDecoration;

public class TeamFragment extends Fragment {

    public Activity mContext;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private TeamFragmentAdapter teamFragmentAdapter;

    private FloatingActionButton floatingActionButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button button_creatTeam, button_myTeam, button_joinTeam;

    final private String TAG = "TeamFragment";
    private SharedPreferences pref;
    private String dataJsonString;
    private JSONObject jsonObject;
    private String userId;
    private UserInfo userInfo;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    public TeamFragment() {
        // Required empty public constructor
    }

    private void setSwipeRefresh(View view) {
        swipeRefreshLayout = view.findViewById(R.id.team_swipe_refresh); //设置没有item动画
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadAdapter();
            }
        });
    }

    private void setRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.team_page_recycler_view);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        teamFragmentAdapter = new TeamFragmentAdapter(mContext);
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
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

    }

    private void setFloatingActionButton(View view) {
        floatingActionButton = view.findViewById(R.id.team_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(1);
            }
        });
    }

    final static private int MAIN_TEAM_CREAT_TEAM = 1;
    final static private int MAIN_TEAM_MY_TEAM = 2;
    final static private int MAIN_TEAM_JOIN_TEAM = 3;

    private void setButton(View view) {
        button_creatTeam = view.findViewById(R.id.button_creatTeam);
        button_myTeam = view.findViewById(R.id.button_myTeam);
        button_joinTeam = view.findViewById(R.id.button_joinTeam);

        button_creatTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getActivity(), TeamCreateActivity.class);
                getActivity().startActivityForResult(intent, MAIN_TEAM_CREAT_TEAM);
            }
        });
        button_myTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getActivity(), MyTeamActivity.class);
                getActivity().startActivityForResult(intent, MAIN_TEAM_MY_TEAM);
            }
        });
        button_joinTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final Intent intent = new Intent(getActivity(), TeamCreateActivity.class);
//                getActivity().startActivityForResult(intent,MAIN_TEAM_CREAT_TEAM);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MAIN_TEAM_CREAT_TEAM:
                Log.e(TAG, "MAIN_TEAM_CREAT_TEAM");
                break;

            case MAIN_TEAM_MY_TEAM:
                Log.e(TAG, "MAIN_TEAM_MY_TEAM");
                break;


        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = DataUtil.getUserInfoData(mContext);
       userId= userInfo.getId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(
                R.layout.fragment_team, container, false);
        setSwipeRefresh(rootView);
        setRecyclerView(rootView);
        setFloatingActionButton(rootView);
        setButton(rootView);
        reloadAdapter();
        return rootView;
    }

    private String reloadFlag;

    @SuppressLint("StaticFieldLeak")
    public void reloadAdapter() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                reloadFlag = "onPreExecute";
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
                Log.e(TAG, "doInBackground");

                String path = PropertiesConfig.teamServerUrl + "get/" + userId;
//                String path = PropertiesConfig.serverUrl + "store/get/storeinfo/" + JSONObject.parseObject(getActivity().getSharedPreferences("userData",getActivity().MODE_PRIVATE).getString("responseJsonDataString",null)).get("id").toString();
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
                        reloadFlag = "doInBackground";
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(response.body().string());
                            String responseJsoStr = responseJson.toJSONString();
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            JSONArray responseJsonData = responseJson.getJSONArray("data");

                            Log.e(TAG, "onResponse CODE OUT");
                            Log.e(TAG, "onResponse CODE is" + code);

//                            ArrayList<StoreInfo> mStores = new ArrayList<>();
                            switch (code) {
                                case 0: //在数据库中更新用户数据出错；
                                    ArrayList<TeamInfo> mTeam = new ArrayList();
                                    for (Object m : responseJsonData) {
                                        mTeam.add(JSONObject.parseObject(JSONObject.toJSONString(m), TeamInfo.class));
                                    }
//                                    Log.e("responseJsonData",responseJsonData.toJSONString());
                                    if (teamFragmentAdapter == null) {
                                        teamFragmentAdapter = new TeamFragmentAdapter(mContext);
                                    }
                                    teamFragmentAdapter.updateResults(mTeam);

                                    Log.e(TAG, "onResponse");
                                    break;
                            }
//                            swipeRefreshLayout.setRefreshing(false);
                        } catch (Exception e) {
                        }
                        reloadFlag = "doInBackground";
                    }

                });
                while (!reloadFlag.equals("doInBackground")) ;
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.e(TAG, "onPostExecute");
                if (mContext == null)
                    return;
                teamFragmentAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        }.execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
