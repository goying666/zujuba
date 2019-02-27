package renchaigao.com.zujuba.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.PageBean.CardMessageFragmentTipBean;
import com.renchaigao.zujuba.PageBean.MessageFragmentCardBean;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.Activity.Adapter.CommonViewHolder;
import renchaigao.com.zujuba.Activity.Message.MessageInfoActivity;
import renchaigao.com.zujuba.Bean.AndroidCardMessageFragmentTipBean;
import renchaigao.com.zujuba.Fragment.Adapter.MessageFragmentAdapter;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.MessageApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.PropertiesConfig;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;
import renchaigao.com.zujuba.widgets.DividerItemDecoration;


public class MessageFragment extends BaseFragment implements CommonViewHolder.onItemCommonClickListener {

    private static String TAG = "MessageFragment";
    private UserInfo userInfo;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private MessageFragmentAdapter messageFragmentAdapter;
    private MessageFragmentCardBean messageFragmentBean = new MessageFragmentCardBean();
    private ArrayList<CardMessageFragmentTipBean> oldTipCardBeanList = new ArrayList<>();
    private ArrayList<CardMessageFragmentTipBean> newTipCardBeanList = new ArrayList<>();
    private ArrayList<CardMessageFragmentTipBean> displayTipCardBeanList = new ArrayList<>();
    private final static String RELOAD_FLAGE_VALUE_RELOAD = "RELOAD";
    private final static String RELOAD_FLAGE_VALUE_STOP = "STOP";
    private String reloadFlag = RELOAD_FLAGE_VALUE_RELOAD;
    private TextView fragement_message_note;
    private Timer timer = new Timer();

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == RespCodeNumber.SUCCESS) {
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (reloadFlag.equals(RELOAD_FLAGE_VALUE_RELOAD)) {
                    reloadAdapter();
                    reloadFlag = RELOAD_FLAGE_VALUE_STOP;
                }
            }
        }, 0, 5000);
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }


    private void InitMessageNoteInfos() {
//        将所有存在本地的message拿出来
        for (AndroidCardMessageFragmentTipBean p : LitePal.findAll(AndroidCardMessageFragmentTipBean.class)) {
            oldTipCardBeanList.add(JSONObject.parseObject(JSONObject.toJSONString(p), CardMessageFragmentTipBean.class));
        }
        if (oldTipCardBeanList.size() > 0) {
//           本地存储的有数据，排序后显示
            if (oldTipCardBeanList.size() > 1) {
                Collections.sort(oldTipCardBeanList, new Comparator<CardMessageFragmentTipBean>() {
                    @Override
                    public int compare(CardMessageFragmentTipBean o1, CardMessageFragmentTipBean o2) {
                        return (int) (o2.getLastTime() - o1.getLastTime());
                    }
                });
            }
            displayTipCardBeanList.clear();
            for (CardMessageFragmentTipBean o : oldTipCardBeanList) {
                displayTipCardBeanList.add(JSONObject.parseObject(JSONObject.toJSONString(o), CardMessageFragmentTipBean.class));
            }
            messageFragmentAdapter.updateResults(displayTipCardBeanList);
            messageFragmentAdapter.notifyDataSetChanged();
        } else {
            reloadAdapter();
        }
    }

    private ArrayList<CardMessageFragmentTipBean> UpdateNewCardTipBeanDate(ArrayList<CardMessageFragmentTipBean> oldData, ArrayList<CardMessageFragmentTipBean> newData) {
//        找出新旧数据的重复项，并将最新的放入一个数列
        ArrayList<CardMessageFragmentTipBean> retArrayList = new ArrayList<>();
        for (int i = 0; i < oldData.size(); i++) {
            CardMessageFragmentTipBean o = oldData.get(i);
            for (int j = 0; j < newData.size(); j++) {
                CardMessageFragmentTipBean n = newData.get(j);
                if (n.getOwnerId().equals(o.getOwnerId())) {
                    n.setId(o.getId());//服务端没有给回传carBean设置id，这里的id需要同本地通ownerId的消息同id，方便后面保存；
                    retArrayList.add(n);
                    break;
                }
            }
        }
        ArrayList<CardMessageFragmentTipBean> oldDataUse = new ArrayList<CardMessageFragmentTipBean>();
        ArrayList<CardMessageFragmentTipBean> newDataUse = new ArrayList<CardMessageFragmentTipBean>();
//        将旧数据数列中重复部分剔除后放入缓存数列中备用；
        for (CardMessageFragmentTipBean o : oldData) {
            Boolean isHave = false;
            for (CardMessageFragmentTipBean same : retArrayList) {
                if (o.getOwnerId().equals(same.getOwnerId())) {
                    isHave = true;
                    break;
                }
            }
            if (!isHave) {
                oldDataUse.add(o);
            }
        }
//        将 新 数据数列中重复部分剔除后放入缓存数列中备用；
        for (CardMessageFragmentTipBean n : newData) {
            Boolean isHave = false;
            for (CardMessageFragmentTipBean same : retArrayList) {
                if (n.getOwnerId().equals(same.getOwnerId())) {
                    isHave = true;
                    break;
                }
            }
            if (!isHave) {
                newDataUse.add(n);
            }
        }
//        将新数据存储进本地
        for (CardMessageFragmentTipBean n : newDataUse) {
            JSONObject.parseObject(JSONObject.toJSONString(n), AndroidCardMessageFragmentTipBean.class).save();
        }
//        将重复部分的数据更新
        for (CardMessageFragmentTipBean same : retArrayList) {
            AndroidCardMessageFragmentTipBean a = JSONObject.parseObject(JSONObject.toJSONString(same), AndroidCardMessageFragmentTipBean.class);
            a.updateAll("ownerId = ?", a.getOwnerId());
        }
//        拼接三部分数列
        retArrayList.addAll(oldDataUse);
        retArrayList.addAll(newDataUse);
//        排序
        Collections.sort(retArrayList, new Comparator<CardMessageFragmentTipBean>() {
            @Override
            public int compare(CardMessageFragmentTipBean o1, CardMessageFragmentTipBean o2) {
                return (int) (o2.getLastTime() - o1.getLastTime());
            }
        });
        return retArrayList;
    }

    @Override
    protected void InitView(View rootView) {
        fragement_message_note = getActivity().findViewById(R.id.fragement_message_note);
        setRecyclerView(rootView);
//        获取本地存储数据
    }

    @Override
    protected void InitData(View rootView) {
        userInfo = DataUtil.GetUserInfoData(mContext);
        InitMessageNoteInfos();
    }

    @Override
    protected void InitOther(View rootView) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    private void setRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.fragement_message_RecyclerView);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        messageFragmentAdapter = new MessageFragmentAdapter(mContext, displayTipCardBeanList, this);
        recyclerView.setAdapter(messageFragmentAdapter);
        recyclerView.setHasFixedSize(true);
//        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
//        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    @Override
    public void onItemClickListener(int position) {
        Intent intent = new Intent(mContext, MessageInfoActivity.class);
        intent.putExtra("messageClass", displayTipCardBeanList.get(position).getMClass());
        intent.putExtra("ownerId", displayTipCardBeanList.get(position).getOwnerId());
        intent.putExtra("title", displayTipCardBeanList.get(position).getName());
        startActivity(intent);
    }

    @Override
    public void onItemLongClickListener(int position) {

    }

    private void reloadAdapter() {
        RetrofitServiceManager.getInstance().SetRetrofit(PropertiesConfig.messageUrl);
        addSubscribe(RetrofitServiceManager.getInstance().creat(MessageApiService.class)
                .GetMessageFragmentBean(userInfo.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(mContext) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            Message msg = new Message();
                            switch (code) {
                                case RespCodeNumber.SUCCESS: //在数据库中更新用户数据出错；
                                    msg.arg1 = RespCodeNumber.SUCCESS;
                                    handler.sendMessage(msg);
                                    messageFragmentBean = JSONObject.parseObject(JSONObject.toJSONString(responseJson.get("data")), MessageFragmentCardBean.class);
                                    newTipCardBeanList = messageFragmentBean.getAllMessageTips();
                                    displayTipCardBeanList = UpdateNewCardTipBeanDate(oldTipCardBeanList, newTipCardBeanList);
                                    messageFragmentAdapter.updateResults(displayTipCardBeanList);
                                    messageFragmentAdapter.notifyDataSetChanged();
                                    Log.e(TAG, "onResponse");
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
