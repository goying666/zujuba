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
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.store.StoreInfo;
import com.renchaigao.zujuba.mongoDB.info.team.TeamInfo;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import renchaigao.com.zujuba.Activity.TeamPart.TeamCreateActivity;
import renchaigao.com.zujuba.Activity.MyTeamActivity;
import renchaigao.com.zujuba.Fragment.Adapter.HallFragmentAdapter;
import renchaigao.com.zujuba.Fragment.Adapter.TeamFragmentAdapter;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.OkhttpFunc;
import renchaigao.com.zujuba.util.PropertiesConfig;
import renchaigao.com.zujuba.util.http.ApiService;
import renchaigao.com.zujuba.util.http.OkHttpUtil;
import renchaigao.com.zujuba.widgets.DividerItemDecoration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class TeamFragment extends Fragment {

    public Activity mContext;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private TeamFragmentAdapter teamFragmentAdapter;

    private FloatingActionButton floatingActionButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button button_creatTeam, button_myTeam, button_joinTeam;

    final private String TAG = "TeamFragment";
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
//                reloadAdapter();
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
        userInfo = DataUtil.GetUserInfoData(mContext);
        userId = userInfo.getId();
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
//        InitRxJavaAndRetrofit();
//        reloadAdapter();
        return rootView;
    }

    ApiService apiService;

    private void InitRxJavaAndRetrofit() {
//        OkHttpUtil okHttpUtil = new OkHttpUtil();
        Retrofit retrofit = new Retrofit.Builder()
                .client(OkHttpUtil.builder.build())
//                .client(okHttpUtil.getBuilder().build())
                .baseUrl(PropertiesConfig.teamServerUrl)
                //设置数据解析器
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    private String reloadFlag;

    @SuppressLint("StaticFieldLeak")
    public void reloadAdapter() {
        apiService.UserServicePost("get",  userInfo.getId(), "null","null",
                JSONObject.parseObject(JSONObject.toJSONString(userInfo), JSONObject.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "onSubscribe:");
                    }

                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            JSONArray responseJsonData = responseJson.getJSONArray("data");

                            Log.e(TAG, "onResponse CODE OUT");
                            Log.e(TAG, "onResponse CODE is" + code);

                            switch (code) {
                                case 0: //在数据库中更新用户数据出错；
                                    ArrayList<TeamInfo> mTeam = new ArrayList();
                                    for (Object m : responseJsonData) {
                                        mTeam.add(JSONObject.parseObject(JSONObject.toJSONString(m), TeamInfo.class));
                                    }
                                    if (teamFragmentAdapter == null) {
                                        teamFragmentAdapter = new TeamFragmentAdapter(mContext);
                                    }
                                    teamFragmentAdapter.updateResults(mTeam);
                                    teamFragmentAdapter.notifyDataSetChanged();
                                    Log.e(TAG, "onResponse");
                                    break;
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError:");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete:");
                    }
                });

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
