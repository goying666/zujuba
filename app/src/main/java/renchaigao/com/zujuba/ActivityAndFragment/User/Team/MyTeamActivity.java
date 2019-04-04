package renchaigao.com.zujuba.ActivityAndFragment.User.Team;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.PageBean.CardUserTeamBean;
import com.renchaigao.zujuba.PageBean.UserTeamActivityBean;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.BaseActivity;
import renchaigao.com.zujuba.ActivityAndFragment.TeamPart.TeamCreateActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.UserApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;

public class MyTeamActivity extends BaseActivity implements CommonViewHolder.onItemCommonClickListener {

    final private String TAG = "MyTeamActivity";

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private MyTeamActivityAdapter myTeamActivityAdapter;
    private UserTeamActivityBean userTeamActivityBean = new UserTeamActivityBean();
    private TextView sumOfCreate, sumOfJoin, sumOfAdmin;
    private ArrayList<CardUserTeamBean> cardUserTeamBeanArrayList = new ArrayList();
    private LinearLayout createPart, joinPart, adminPart;
    private UserInfo userInfo;
    private TabLayout tab;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            UpdateView();
        }
    };

    private ArrayList<CardUserTeamBean> setCardBeanList(int flag) {
        cardUserTeamBeanArrayList.clear();
        switch (flag) {
            case 1:
                for (CardUserTeamBean b : userTeamActivityBean.getCardList()) {
                    if (b.getIsMyCreate()) {
                        cardUserTeamBeanArrayList.add(b);
                    }
                }
                break;
            case 2:
                for (CardUserTeamBean b : userTeamActivityBean.getCardList()) {
                    if (b.getIsMyJoin()) {
                        cardUserTeamBeanArrayList.add(b);
                    }
                }
                break;
            case 3:
                for (CardUserTeamBean b : userTeamActivityBean.getCardList()) {
                    if (b.getIsMyAdmin()) {
                        cardUserTeamBeanArrayList.add(b);
                    }
                }
                break;
            case 4:
                for (CardUserTeamBean b : userTeamActivityBean.getCardList()) {
                    if (b.getIsToday()) {
                        b.setStartDay("今天");
                        cardUserTeamBeanArrayList.add(b);
                    }
                }
                break;
            case 5:
                for (CardUserTeamBean b : userTeamActivityBean.getCardList()) {
                    if (b.getIsTomorrow()) {
                        b.setStartDay("明天");
                        cardUserTeamBeanArrayList.add(b);
                    }
                }
                break;
            case 6:
                for (CardUserTeamBean b : userTeamActivityBean.getCardList()) {
                    if (b.getIsWeekend()) {
                        b.setStartDay("周末");
                        cardUserTeamBeanArrayList.add(b);
                    }
                }
                break;
            case 7:
                for (CardUserTeamBean b : userTeamActivityBean.getCardList()) {
                    if (b.getIsFinish()) {
                        b.setStartDay("结束");
                        cardUserTeamBeanArrayList.add(b);
                    }
                }
                break;
        }
        return cardUserTeamBeanArrayList;
    }

    @Override
    protected void UpdateView() {
        sumOfCreate.setText(userTeamActivityBean.getSumOfCreate().toString());
        sumOfJoin.setText(userTeamActivityBean.getSumOfJoin().toString());
        sumOfAdmin.setText(userTeamActivityBean.getSumOfAdmin().toString());
        updateRecyclerView(cardUserTeamBeanArrayList);
    }

    private TextView secondTitleTextView;
    private ConstraintLayout toolbar;

    private void initToolbar() {
        toolbar = (ConstraintLayout) findViewById(R.id.user_team_toolbar);
        ((TextView) toolbar.findViewById(R.id.toolbarTitle)).setText("我的组局");
        secondTitleTextView = (TextView) toolbar.findViewById(R.id.toolbarSecondTitle);
        ImageView goback = (ImageView) toolbar.findViewById(R.id.toolbarBack);
        secondTitleTextView.setText("添加新局");
        secondTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyTeamActivity.this, TeamCreateActivity.class);
                startActivity(intent);
            }
        });
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void InitView() {
        initToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.user_team_RecyclerView);
        sumOfCreate = (TextView) findViewById(R.id.user_team_createNum);
        sumOfJoin = (TextView) findViewById(R.id.user_team_joinNum);
        sumOfAdmin = (TextView) findViewById(R.id.user_team_adminNum);
        createPart = (LinearLayout) findViewById(R.id.createPart);
        joinPart = (LinearLayout) findViewById(R.id.joinPart);
        adminPart = (LinearLayout) findViewById(R.id.adminPart);
        tab = (TabLayout) findViewById(R.id.user_team_tab);

    }

    @Override
    protected void InitData() {
        userInfo = DataUtil.GetUserInfoData(this);
    }

    @Override
    protected void InitOther() {
        initRecyclerView();
        reloadAdapter();
        setClick();
        setTab();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_create_team;
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadAdapter();
    }

    @Override
    public void onItemLongClickListener(int position) {

    }

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        myTeamActivityAdapter = new MyTeamActivityAdapter(MyTeamActivity.this
                , userTeamActivityBean.getCardList(), this);
        recyclerView.setAdapter(myTeamActivityAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void updateRecyclerView(ArrayList<CardUserTeamBean> cardUserTeamBeans) {
        myTeamActivityAdapter.updateResults(cardUserTeamBeans);
        myTeamActivityAdapter.notifyDataSetChanged();
    }

    private Boolean selectCreate = false;
    private Boolean selectJoin = false;
    private Boolean selectAdmin = false;

    private void setClick() {
        createPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectJoin = false;
                selectAdmin = false;
                selectCreate = !selectCreate;
                if (selectCreate) {
                    createPart.setBackgroundColor(Color.rgb(0xff, 0xf7, 0x6a));
                    joinPart.setBackgroundColor(Color.rgb(0xed, 0xed, 0xed));
                    adminPart.setBackgroundColor(Color.rgb(0xed, 0xed, 0xed));
                    tab.setVisibility(View.GONE);
                    updateRecyclerView(setCardBeanList(1));
                } else {
                    createPart.setBackgroundColor(Color.rgb(0xed, 0xed, 0xed));
                    tab.setVisibility(View.VISIBLE);
                    updateRecyclerView(setCardBeanList(positon));
                }
                handler.sendMessage(new Message());
            }
        });
        joinPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAdmin = false;
                selectCreate = false;
                selectJoin = !selectJoin;
                if (selectJoin) {
                    createPart.setBackgroundColor(Color.rgb(0xed, 0xed, 0xed));
                    joinPart.setBackgroundColor(Color.rgb(0xff, 0xf7, 0x6a));
                    adminPart.setBackgroundColor(Color.rgb(0xed, 0xed, 0xed));
                    tab.setVisibility(View.GONE);
                    updateRecyclerView(setCardBeanList(2));
                } else {
                    joinPart.setBackgroundColor(Color.rgb(0xed, 0xed, 0xed));
                    tab.setVisibility(View.VISIBLE);
                    updateRecyclerView(setCardBeanList(positon));
                }
                handler.sendMessage(new Message());
            }
        });
        adminPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectJoin = false;
                selectAdmin = !selectAdmin;
                selectCreate = false;
                if (selectAdmin) {
                    createPart.setBackgroundColor(Color.rgb(0xed, 0xed, 0xed));
                    joinPart.setBackgroundColor(Color.rgb(0xed, 0xed, 0xed));
                    adminPart.setBackgroundColor(Color.rgb(0xff, 0xf7, 0x6a));
                    tab.setVisibility(View.GONE);
                    updateRecyclerView(setCardBeanList(3));
                } else {
                    adminPart.setBackgroundColor(Color.rgb(0xed, 0xed, 0xed));
                    updateRecyclerView(setCardBeanList(positon));
                    tab.setVisibility(View.VISIBLE);
                }
                handler.sendMessage(new Message());
            }
        });
    }

    private int positon = 4;

    private void setTab() {
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        updateRecyclerView(setCardBeanList(4));
                        positon = 4;
                        break;
                    case 1:
                        updateRecyclerView(setCardBeanList(5));
                        positon = 5;
                        break;
                    case 2:
                        updateRecyclerView(setCardBeanList(6));
                        positon = 6;
                        break;
                    case 3:
                        updateRecyclerView(setCardBeanList(7));
                        positon = 7;
                        break;
                }
                handler.sendMessage(new Message());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onItemClickListener(int position) {

    }


    public void reloadAdapter() {
        addSubscribe(RetrofitServiceManager.getInstance().creat(UserApiService.class)
                .GetMyTeamInfo(userInfo.getId(), userInfo.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(MyTeamActivity.this) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            String responseJsonData = responseJson.getString("data");
                            Message msg = new Message();
                            switch (code) {
                                case RespCodeNumber.SUCCESS:
                                    userTeamActivityBean = JSONObject.parseObject(responseJsonData, UserTeamActivityBean.class);
                                    updateRecyclerView(setCardBeanList(positon));
                                    msg.arg1 = 1;
                                    handler.sendMessage(msg);
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

}
