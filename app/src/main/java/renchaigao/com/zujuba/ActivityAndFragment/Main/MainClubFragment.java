package renchaigao.com.zujuba.ActivityAndFragment.Main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.PageBean.CardClubFragmentTipBean;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.Club.ClubInfoActivity;
import renchaigao.com.zujuba.ActivityAndFragment.Club.CreateClubActivity;
import renchaigao.com.zujuba.ActivityAndFragment.Main.Adapter.MainClubFragmentAdapter;
import renchaigao.com.zujuba.ActivityAndFragment.BaseFragment;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.ClubApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;


public class MainClubFragment extends BaseFragment implements CommonViewHolder.onItemCommonClickListener {

    private static String TAG = "MainClubFragment";
    private UserInfo userInfo;
    private MainClubFragmentAdapter mainClubFragmentAdapter;
    private ArrayList<CardClubFragmentTipBean> displayTipCardBeanList = new ArrayList<>();
    private final static String RELOAD_FLAGE_VALUE_RELOAD = "RELOAD";
    private final static String RELOAD_FLAGE_VALUE_STOP = "STOP";
    private String reloadFlag = RELOAD_FLAGE_VALUE_RELOAD;
    private Timer timer = new Timer();
    private Button createNewClub, joinClub;

    @Override
    protected void InitView(View rootView) {
        this.rootView = rootView;
        createNewClub = (Button) rootView.findViewById(R.id.fragement_message_create_club_club);
        joinClub = (Button) rootView.findViewById(R.id.fragement_message_jion_club_club);
        setRecyclerView(rootView);
    }

    @Override
    protected void InitData(View rootView) {
        userInfo = DataUtil.GetUserInfoData(mContext);
    }

    @Override
    protected void InitOther(View rootView) {
        setButton();
        reloadAdapter();
    }

    private void setButton() {
        createNewClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CreateClubActivity.class);
                getActivity().startActivity(intent);
            }
        });
        joinClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case RespCodeNumber.SUCCESS:
                    UpdateViewData();
                    break;
            }
        }
    };

    public void UpdateViewData() {
        mainClubFragmentAdapter.updateResults(displayTipCardBeanList);
        mainClubFragmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        //相当于Fragment的onResume
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (reloadFlag.equals(RELOAD_FLAGE_VALUE_RELOAD)) {
                    reloadAdapter();
                    reloadFlag = RELOAD_FLAGE_VALUE_STOP;
                }
            }
        }, 0, 10000);
    }

    private View rootView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message_club;
    }

    private void setRecyclerView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragement_message_club_RecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        mainClubFragmentAdapter = new MainClubFragmentAdapter(mContext, displayTipCardBeanList, this);
        recyclerView.setAdapter(mainClubFragmentAdapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onItemClickListener(int position) {
        Intent intent = new Intent(mContext, ClubInfoActivity.class);
        intent.putExtra("clubId", displayTipCardBeanList.get(position).getClubId());
        startActivity(intent);
    }

    public void showPopMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        popupMenu.inflate(R.menu.item_select_menu);
        popupMenu.show();

//        popupWindow.showAsDropDown(view);

//        PopupMenu popupMenu = new PopupMenu(mContext,view, Gravity.LEFT,0x111,0);
//        Gravity gravity = new Gravity();
//        PopupMenu popupMenu = new PopupMenu(mContext,view, Gravity.LEFT,0,0);
//        popupMenu.setGravity(Gravity.CENTER);
//        popupMenu.getMenuInflater().inflate(R.menu.item_select_menu, popupMenu.getMenu());
//
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            public boolean onMenuItemClick(MenuItem item) {
//                new AlertDialog.Builder(mContext)
//                        .setMessage("确定删除这条消息吗？")
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        }).setCancelable(false).show();
//                return false;
//            }
//        });
//        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
//            @Override
//            public void onDismiss(PopupMenu menu) {
//                menu = null;
//            }
//        });
//        popupMenu.show();
    }

    @Override
    public void onItemLongClickListener(int position) {
        showPopMenu(rootView);
    }

    private void reloadAdapter() {
        addSubscribe(RetrofitServiceManager.getInstance().creat(ClubApiService.class)
                .GetMyAllClub(userInfo.getId(), userInfo.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(mContext) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            Message msg = new Message();
                            JSONArray responseJsonData = responseJson.getJSONArray("data");
                            switch (code) {
                                case RespCodeNumber.CLUB_UPDATE_SUCCESS: //在数据库中更新用户数据出错；
                                    msg.arg1 = RespCodeNumber.SUCCESS;
                                    displayTipCardBeanList.clear();
                                    for (Object m : responseJsonData) {
                                        displayTipCardBeanList.add(JSONObject.parseObject(JSONObject.toJSONString(m), CardClubFragmentTipBean.class));
                                    }
                                    if (displayTipCardBeanList.size() > 0)
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
                        Log.e(TAG, "onError");
                        reloadFlag = RELOAD_FLAGE_VALUE_RELOAD;
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete");
                        reloadFlag = RELOAD_FLAGE_VALUE_RELOAD;
                    }
                }));
    }

}
