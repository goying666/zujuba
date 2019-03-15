//package renchaigao.com.zujuba.Activity.Message;
//
//import android.annotation.SuppressLint;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v7.widget.RecyclerView;
//
//import renchaigao.com.zujuba.Activity.Adapter.CommonViewHolder;
//import renchaigao.com.zujuba.Activity.BaseActivity;
//import renchaigao.com.zujuba.R;
//
//import android.annotation.SuppressLint;
//import android.os.Handler;
//import android.os.Message;
//import android.support.design.widget.TextInputEditText;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.renchaigao.zujuba.domain.response.RespCodeNumber;
//import com.renchaigao.zujuba.domain.response.ResponseEntity;
//import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;
//
//import org.litepal.LitePal;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.schedulers.Schedulers;
//import renchaigao.com.zujuba.Activity.Adapter.CommonViewHolder;
//import renchaigao.com.zujuba.Activity.BaseActivity;
//import renchaigao.com.zujuba.Bean.AndroidCardMessageFragmentTipBean;
//import renchaigao.com.zujuba.Bean.AndroidMessageContent;
//import renchaigao.com.zujuba.R;
//import renchaigao.com.zujuba.util.Api.MessageApiService;
//import renchaigao.com.zujuba.util.DataPart.DataUtil;
//import renchaigao.com.zujuba.util.http.BaseObserver;
//import renchaigao.com.zujuba.util.http.RetrofitServiceManager;
//public class FriendMessageInfoActivity extends BaseActivity implements CommonViewHolder.onItemCommonClickListener{
//
//    private FriendMessageInfoAdapter friendMessageInfoAdapter;
//    private RecyclerView recyclerView;
//
//    @SuppressLint("HandlerLeak")
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.arg1) {
//                case RELOAD_TEAM_MESSAGE_INFO:
//                    UpdateMessageDate();
//                    break;
//                case INPUTSTRING_IS_NULL:
//                    break;
//            }
//        }
//    };
//    @Override
//    protected void InitView() {
//
//    }
//
//    private void InitRecyclerView() {
//        recyclerView = findViewById(R.id.message_info_recycler_view);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setStackFromEnd(true);
//        layoutManager.setReverseLayout(true);
//        recyclerView.setLayoutManager(layoutManager);
//        teamMessageInfoAdapter = new TeamMessageInfoAdapter(this, allMessages, this);
//        recyclerView.setAdapter(teamMessageInfoAdapter);
//        recyclerView.setHasFixedSize(true);
//    }
//    @Override
//    protected void InitData() {
//
//    }
//
//    @Override
//    protected void InitOther() {
//
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_friend_message_info;
//    }
//
//    private void SendMessage(AndroidMessageContent androidMessageContent) {
//        addSubscribe(RetrofitServiceManager.getInstance().creat(MessageApiService.class)
//                .AddMessageInfo(
//                        userId,
//                        token,
//                        androidMessageContent)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseObserver<ResponseEntity>(TeamMessageInfoActivity.this) {
//                    @Override
//                    public void onNext(ResponseEntity value) {
//                        try {
//                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
//                            int code = Integer.valueOf(responseJson.get("code").toString());
//                            JSONArray responseJsonData = responseJson.getJSONArray("data");
//                            Message msg = new Message();
//                            switch (code) {
//                                case RespCodeNumber.MESSAGE_ADD_TEAM_SUCCESS: //在数据库中更新用户数据出错；
//                                    newMessages.clear();
//                                    if (responseJsonData.size() > 0) {
//                                        for (Object o : responseJsonData) {
//                                            newMessages.add(JSONObject.parseObject(JSONObject.toJSONString(o), AndroidMessageContent.class));
//                                        }
//                                        msg.arg1 = RELOAD_TEAM_MESSAGE_INFO;
//                                        handler.sendMessage(msg);
//                                    }
//                                    break;
//                            }
//                        } catch (Exception e) {
//                            Log.e(TAG, e.toString());
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
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        reloadMessageInfo();
//                    }
//                }));
//    }
//
//    public void reloadMessageInfo() {
//        addSubscribe(RetrofitServiceManager.getInstance().creat(MessageApiService.class)
//                .GetMessageInfo(userId, ownerId, messageClass, messageListEnd)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseObserver<ResponseEntity>(TeamMessageInfoActivity.this) {
//                    @Override
//                    public void onNext(ResponseEntity value) {
//                        try {
//                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
//                            int code = Integer.valueOf(responseJson.get("code").toString());
//                            JSONArray responseJsonData = responseJson.getJSONArray("data");
//                            Message msg = new Message();
//                            switch (code) {
//                                case RespCodeNumber.MESSAGE_USER_GET_TEAM_SUCCESS: //在数据库中更新用户数据出错；
//                                    newMessages.clear();
//                                    if (responseJsonData.size() > 0) {
//                                        for (Object o : responseJsonData) {
//                                            newMessages.add(JSONObject.parseObject(JSONObject.toJSONString(o), AndroidMessageContent.class));
//                                        }
//                                        msg.arg1 = RELOAD_TEAM_MESSAGE_INFO;
//                                        handler.sendMessage(msg);
//                                    }
//                                    break;
//                            }
//                        } catch (Exception e) {
//                            Log.e(TAG, e.toString());
//                        }
//                    }
//
//                    @Override
//                    protected void onSuccess(ResponseEntity responseEntity) {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        super.onError(e);
//                        contentSwipeRefreshLayout.setRefreshing(false);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        contentSwipeRefreshLayout.setRefreshing(false);
//                    }
//                }));
//    }
//
//
//    @Override
//    public void onItemClickListener(int position) {
//
//    }
//
//    @Override
//    public void onItemLongClickListener(int position) {
//
//    }
//}
