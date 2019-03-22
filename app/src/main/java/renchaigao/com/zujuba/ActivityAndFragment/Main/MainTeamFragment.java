package renchaigao.com.zujuba.ActivityAndFragment.Main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.Main.Adapter.MainTeamFragmentAdapter;
import renchaigao.com.zujuba.ActivityAndFragment.TeamPart.TeamActivity;
import renchaigao.com.zujuba.ActivityAndFragment.TeamPart.TeamCreateActivity;
import renchaigao.com.zujuba.ActivityAndFragment.BaseFragment;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.TeamApiService;
import renchaigao.com.zujuba.util.DataFunctions.DataSort;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;

import static renchaigao.com.zujuba.util.PropertiesConfig.FRAGMENT_TEAM_PAGE;

public class MainTeamFragment extends BaseFragment implements CommonViewHolder.onItemCommonClickListener {


    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private MainTeamFragmentAdapter mainTeamFragmentAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private Button button_creatTeam;

    private TabLayout tableLayout;
    private TabItem item1, item2, item3, item4;
    final private String TAG = "MainTeamFragment";
    private UserInfo userInfo;
    ArrayList<JSONObject> teamList = new ArrayList();
    private int tabSelecte = 0;
    final static private int TABSELECTED_CHANGE = 1003;

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


    @Override
    public void onResume() {
        super.onResume();
        reloadAdapter();
        if(teamList.size()>0){
            mainTeamFragmentAdapter.updateResults(teamList);
            mainTeamFragmentAdapter.notifyDataSetChanged();
        }
    }

    private void UpdateViewData() {
        DataSort<JSONObject> dataSort = new DataSort<>();
        switch (tabSelecte) {
            case 0:
//                人数排序
                teamList = dataSort.SequenceSort(teamList, "realPlayerNum");
                break;
            case 1:
//                距离排序
                teamList = dataSort.SequenceSort(teamList, "realDistance");
                break;
            case 2:
//                时间排序
                teamList = dataSort.SequenceSort(teamList, "realStartTime");
                break;
            case 3:
//                筛选排序
                teamList = dataSort.SequenceSort(teamList, "");
                break;
        }
        mainTeamFragmentAdapter.updateResults(teamList);
        mainTeamFragmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemLongClickListener(int position) {
    }

    private void setSwipeRefresh(View view) {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadAdapter();
            }
        });
    }

    private void setRecyclerView(View view) {
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        mainTeamFragmentAdapter = new MainTeamFragmentAdapter(mContext, teamList, this);
        recyclerView.setAdapter(mainTeamFragmentAdapter);
        recyclerView.setHasFixedSize(true);
//        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
//        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

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

//    private void setFloatingActionButton(View view) {
//        floatingActionButton = view.findViewById(R.id.team_fab);
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                recyclerView.scrollToPosition(1);
//            }
//        });
//    }


    private void setButton(View view) {
        button_creatTeam = (Button) view.findViewById(R.id.button_creatTeam);

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
        tableLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.team_swipe_refresh); //设置没有item动画
        recyclerView = (RecyclerView) rootView.findViewById(R.id.team_page_recycler_view);
        item1 = (TabItem) rootView.findViewById(R.id.tabItem1);
        item2 = (TabItem) rootView.findViewById(R.id.tabItem2);
        item3 = (TabItem) rootView.findViewById(R.id.tabItem3);
        item4 = (TabItem) rootView.findViewById(R.id.tabItem4);
    }

    @Override
    protected void InitData(View rootView) {
        userInfo = DataUtil.GetUserInfoData(mContext);
    }

    @Override
    protected void InitOther(View rootView) {
        setSwipeRefresh(rootView);
        setRecyclerView(rootView);
        setTableLayoutView(rootView);
        setButton(rootView);
        reloadAdapter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_team;
    }

    public void reloadAdapter() {
        RequestBody multiBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("json", "")
                .build();
        addSubscribe(RetrofitServiceManager.getInstance().creat(TeamApiService.class)
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
                            Message msg = new Message();
                            switch (code) {
                                case RespCodeNumber.SUCCESS: //在数据库中更新用户数据出错；
                                    teamList = new ArrayList<>(responseJsonData.toJavaList(JSONObject.class));
                                    msg.arg1 = RespCodeNumber.SUCCESS;
                                    handler.sendMessage(msg);
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
        intent.putExtra("whereCome", "MainTeamFragment");
        getActivity().startActivityForResult(intent, FRAGMENT_TEAM_PAGE);
    }
}



