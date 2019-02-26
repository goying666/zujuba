package renchaigao.com.zujuba.Fragment;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import renchaigao.com.zujuba.Activity.Adapter.CommonViewHolder;
import renchaigao.com.zujuba.Activity.TeamPart.TeamActivity;
import renchaigao.com.zujuba.Activity.TeamPart.TeamCreateActivity;
import renchaigao.com.zujuba.Fragment.Adapter.TeamFragmentAdapter;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.PropertiesConfig;
import renchaigao.com.zujuba.util.http.ApiService;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;
import renchaigao.com.zujuba.widgets.DividerItemDecoration;

import static renchaigao.com.zujuba.util.PropertiesConfig.FRAGMENT_TEAM_PAGE;

public class TeamFragment extends BaseFragment implements CommonViewHolder.onItemCommonClickListener{


    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private TeamFragmentAdapter teamFragmentAdapter;

    private FloatingActionButton floatingActionButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button button_creatTeam, button_myTeam, button_joinTeam;

    final private String TAG = "TeamFragment";
    private UserInfo userInfo;

    ArrayList<JSONObject> teamList = new ArrayList();

    @Override
    public void onItemLongClickListener(int position) {
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
        teamFragmentAdapter = new TeamFragmentAdapter(mContext,teamList,this);
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


    private void setButton(View view) {
        button_creatTeam = view.findViewById(R.id.button_creatTeam);

        button_creatTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getActivity(), TeamCreateActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void InitView(View rootView) {
        setSwipeRefresh(rootView);
        setRecyclerView(rootView);
//        setFloatingActionButton(rootView);
        setButton(rootView);
    }

    @Override
    protected void InitData(View rootView) {
        userInfo = DataUtil.GetUserInfoData(mContext);
    }

    @Override
    protected void InitOther(View rootView) {
        reloadAdapter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_team;
    }

    public void reloadAdapter() {
        RetrofitServiceManager.getInstance().SetRetrofit(PropertiesConfig.teamServerUrl);
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody multiBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("json", "")
                .build();
//        MultipartBody.Part.createFormData("json", "aaa");
//        map.put("plateNo", multiBody);

        addSubscribe(RetrofitServiceManager.getInstance().creat(ApiService.class)
                .FourParameterBodyPost("getnear",
                        userInfo.getId(),
                        "null",
                        "null",
                        multiBody)
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
                                    teamList = new ArrayList<>(responseJsonData.toJavaList(JSONObject.class));
                                    teamFragmentAdapter.updateResults(teamList);
                                    teamFragmentAdapter.notifyDataSetChanged();
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
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClickListener(int position) {
        JSONObject teamJSON = teamList.get(position);
        Intent intent = new Intent(mContext, TeamActivity.class);
        intent.putExtra("teamId", teamJSON.getString("teamId"));
        intent.putExtra("teamFragmentData",JSONObject.toJSONString(teamJSON));
        intent.putExtra("whereCome","TeamFragment");
        getActivity().startActivityForResult(intent,FRAGMENT_TEAM_PAGE);
    }
}
//    public void reloadAdapter() {
//        RetrofitServiceManager.getInstance().SetRetrofit(PropertiesConfig.placeServerUrl);
//        Map<String, RequestBody> map = new HashMap<>();
//        RequestBody multiBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("json", JSONObject.toJSONString(userInfo))
//                .build();
//        map.put("multiBody", multiBody);
//        addSubscribe(RetrofitServiceManager.getInstance().creat(ApiService.class)
//                .FourParameterBodyPost("store",
//                        "getnear",
//                        "",
//                        "",
//                        multiBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseObserver<ResponseEntity>(mContext) {
//
//                    @Override
//                    public void onNext(ResponseEntity value) {
//                        try {
//                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
//                            int code = Integer.valueOf(responseJson.get("code").toString());
//                            JSONArray responseJsonData = responseJson.getJSONArray("data");
//
//                            Log.e(TAG, "onResponse CODE OUT");
//                            Log.e(TAG, "onResponse CODE is" + code);
//
//                            switch (code) {
//                                case 0: //在数据库中更新用户数据出错；
//                                    ArrayList<TeamInfo> mTeam = new ArrayList();
//                                    for (Object m : responseJsonData) {
//                                        mTeam.add(JSONObject.parseObject(JSONObject.toJSONString(m), TeamInfo.class));
//                                    }
//                                    if (teamFragmentAdapter == null) {
//                                        teamFragmentAdapter = new TeamFragmentAdapter(mContext);
//                                    }
//                                    teamFragmentAdapter.updateResults(mTeam);
//                                    teamFragmentAdapter.notifyDataSetChanged();
//                                    Log.e(TAG, "onResponse");
//                                    break;
//                            }
//                        } catch (Exception e) {
//                        }
//                    }
//
//                    @Override
//                    protected void onSuccess(ResponseEntity responseEntity) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        super.onError(e);
//                        Log.e(TAG, "onError");
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                }));
//
////        apiService.FourParameterJsonPost("get",  userInfo.getId(), "null","null",
////                JSONObject.parseObject(JSONObject.toJSONString(userInfo), JSONObject.class))
////                .subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .subscribe(new Observer<ResponseEntity>() {
////                    @Override
////                    public void onSubscribe(Disposable d) {
////                        Log.e(TAG, "onSubscribe:");
////                    }
////
////                    @Override
////                    public void onNext(ResponseEntity value) {
////                        try {
////                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
////                            int code = Integer.valueOf(responseJson.get("code").toString());
////                            JSONArray responseJsonData = responseJson.getJSONArray("data");
////
////                            Log.e(TAG, "onResponse CODE OUT");
////                            Log.e(TAG, "onResponse CODE is" + code);
////
////                            switch (code) {
////                                case 0: //在数据库中更新用户数据出错；
////                                    ArrayList<TeamInfo> mTeam = new ArrayList();
////                                    for (Object m : responseJsonData) {
////                                        mTeam.add(JSONObject.parseObject(JSONObject.toJSONString(m), TeamInfo.class));
////                                    }
////                                    if (teamFragmentAdapter == null) {
////                                        teamFragmentAdapter = new TeamFragmentAdapter(mContext);
////                                    }
////                                    teamFragmentAdapter.updateResults(mTeam);
////                                    teamFragmentAdapter.notifyDataSetChanged();
////                                    Log.e(TAG, "onResponse");
////                                    break;
////                            }
////                        } catch (Exception e) {
////                        }
////                    }
////
////                    @Override
////                    public void onError(Throwable e) {
////                        Log.e(TAG, "onError:");
////                    }
////
////                    @Override
////                    public void onComplete() {
////                        Log.e(TAG, "onComplete:");
////                    }
////                });
//
//    }


