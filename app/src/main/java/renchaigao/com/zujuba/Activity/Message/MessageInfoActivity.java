package renchaigao.com.zujuba.Activity.Message;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.Activity.Adapter.CommonViewHolder;
import renchaigao.com.zujuba.Activity.Adapter.MessageInfoAdapter;
import renchaigao.com.zujuba.Activity.BaseActivity;
import renchaigao.com.zujuba.Activity.TeamPart.TeamActivity;
import renchaigao.com.zujuba.Bean.AndroidMessageContent;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.MessageApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.PropertiesConfig;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;
import renchaigao.com.zujuba.widgets.DividerItemDecoration;

import static com.renchaigao.zujuba.PropertiesConfig.ConstantManagement.FRIEND_SEND_MESSAGE;
import static com.renchaigao.zujuba.PropertiesConfig.ConstantManagement.TEAM_SEND_MESSAGE;
import static com.renchaigao.zujuba.PropertiesConfig.ConstantManagement.USER_SEND_MESSAGE;


public class MessageInfoActivity extends BaseActivity implements CommonViewHolder.onItemCommonClickListener {
    final static String TAG = "MessageInfoActivity";
    private MessageInfoAdapter messageInfoAdapter;
    private String userId, ownerId, teamId, friendId, otherUserId, messageClass, inputString;
    private UserInfo userInfo;
    private Button message_info_sendButton;
    private TextInputEditText message_info_inputEdit;
    private ImageView message_info_more;
    private TextView messageName;
    private SwipeRefreshLayout contentSwipeRefreshLayout;
    ArrayList<AndroidMessageContent> allMessages = new ArrayList<AndroidMessageContent>();
    ArrayList<AndroidMessageContent> newMessages = new ArrayList<AndroidMessageContent>();
    private Timer timer = new Timer();

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case RELOAD_TEAM_MESSAGE_INFO:
                    UpdateMessageDate();
                    break;
                case INPUTSTRING_IS_NULL:
                    break;
            }
        }
    };

    private void UpdateMessageDate() {
        //  查询本地消息，从本地数据库读取当前群组的消息记录，并更新至界面；
        for (AndroidMessageContent o : newMessages) {
            o.setIsReceived(true);
            o.save();
            allMessages.add(o);
        }
        //  排序
        Collections.sort(allMessages, new Comparator<AndroidMessageContent>() {
            @Override
            public int compare(AndroidMessageContent o1, AndroidMessageContent o2) {
                return (int) (o1.getSendTime() - o2.getSendTime());
                //  return (int) (o2.getSendTime() - o1.getSendTime());
            }
        });
        messageListEnd = allMessages.get(allMessages.size() - 1).getSendTime();
        messageInfoAdapter.updateResults(allMessages);
        messageInfoAdapter.notifyDataSetChanged();
    }


    @Override
    protected void InitView() {
        contentSwipeRefreshLayout = findViewById(R.id.message_info_SwipeRefreshLayout);
        messageName = findViewById(R.id.message_info_title);
        message_info_inputEdit = findViewById(R.id.message_info_inputEdit);
        message_info_sendButton = findViewById(R.id.message_info_sendButton);
        message_info_more = findViewById(R.id.message_info_more);

        InitRecyclerView();
    }

    @Override
    protected void InitData() {
        userInfo = DataUtil.GetUserInfoData(this);
        userId = userInfo.getId();
        messageClass = getIntent().getStringExtra("messageClass");
        ownerId = getIntent().getStringExtra("ownerId");
        switch (messageClass) {
            case TEAM_SEND_MESSAGE:
                teamId = getIntent().getStringExtra("ownerId");
                messageName.setText(getIntent().getStringExtra("teamName"));
                break;
            case FRIEND_SEND_MESSAGE:
                friendId = getIntent().getStringExtra("ownerId");
                messageName.setText(getIntent().getStringExtra("friendName"));
                break;
            case USER_SEND_MESSAGE:
                otherUserId = getIntent().getStringExtra("ownerId");
                messageName.setText(getIntent().getStringExtra("otherUserName"));
                break;
        }
        GetLastMessageTimes();
        if (allMessages.size() > 0) {
            String text = JSONObject.toJSONString(allMessages);
            messageInfoAdapter.updateResults(allMessages);
            messageInfoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void InitOther() {
        InitSwipeLayout();
        message_info_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageInfoActivity.this, TeamActivity.class);
                intent.putExtra("teamId", JSONObject.toJSONString(teamId));
                startActivityForResult(intent, PropertiesConfig.ACTIVITY_MESSAGE_PAGE);
//                finish();
            }
        });
        message_info_inputEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                inputString = s.toString();
            }
        });
        message_info_sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                判断是否为空 且 少于500字
//                满足条件下发送
                if (inputString.length() > 0 && inputString.length() < 500) {
                    AndroidMessageContent androidMessageContent = new AndroidMessageContent();
                    long nowTimeLong = Calendar.getInstance().getTimeInMillis();
                    androidMessageContent.setMessageClass(messageClass);
                    switch (messageClass) {
                        case TEAM_SEND_MESSAGE:
                            androidMessageContent.setTitle(getIntent().getStringExtra("teamName"));
                            break;
                        case FRIEND_SEND_MESSAGE:
                            androidMessageContent.setTitle(getIntent().getStringExtra("friendName"));
                            androidMessageContent.setFriendId("");
                            break;
                        case USER_SEND_MESSAGE:
                            androidMessageContent.setTitle(getIntent().getStringExtra("userName"));
                            break;
                    }
                    androidMessageContent.setIdLong(nowTimeLong);
                    androidMessageContent.setIsMe(true);
                    androidMessageContent.setIsGroup(false);
                    androidMessageContent.setSenderId(userId);
                    androidMessageContent.setSendTime(nowTimeLong);
                    androidMessageContent.setIsReceived(false);
                    androidMessageContent.setSenderImageUrl(userInfo.getPicPath());
                    androidMessageContent.setMyImageUrl(userInfo.getPicPath());
                    androidMessageContent.setTeamId(teamId);
                    androidMessageContent.setSendTime(Calendar.getInstance().getTimeInMillis());
                    androidMessageContent.setContent(inputString);
                    SendMessage(androidMessageContent);
                    message_info_inputEdit.clearFocus();
                    message_info_inputEdit.setText("");
                    message_info_inputEdit.setHint("");
                    inputString = null;
                } else {

                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_info;
    }

    private void InitSwipeLayout() {
        contentSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetLastMessageTimes();
                reloadMessageInfo();
            }
        });
//        message_info_swip = findViewById(R.id.message_info_swip);
    }

    private void InitRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.message_info_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        messageInfoAdapter = new MessageInfoAdapter(this, allMessages, this);
        recyclerView.setAdapter(messageInfoAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    private long messageListEnd = 0L;

    private long GetLastMessageTimes() {
        try {
            switch (messageClass) {
                case TEAM_SEND_MESSAGE:
                    allMessages = new ArrayList<>(LitePal.where("teamId = ?", teamId).find(AndroidMessageContent.class));
                    break;
                case FRIEND_SEND_MESSAGE:
                    allMessages = new ArrayList<>(LitePal.where("friendId = ?", friendId).find(AndroidMessageContent.class));
                    break;
                case USER_SEND_MESSAGE:
                    allMessages = new ArrayList<>(LitePal.where("userId = ?", otherUserId).find(AndroidMessageContent.class));
                    break;
            }
            Collections.sort(allMessages, new Comparator<AndroidMessageContent>() {
                @Override
                public int compare(AndroidMessageContent o1, AndroidMessageContent o2) {
                    return (int) (o2.getSendTime() - o1.getSendTime());
                }
            });
            if (allMessages.size() > 0)
                messageListEnd = allMessages.get(0).getSendTime();
            else
                messageListEnd = 0L;
        } catch (Exception e) {
            messageListEnd = 0L;
        }

        return messageListEnd;
    }

    final private static int RELOAD_TEAM_MESSAGE_INFO = 1000;
    final private static int INPUTSTRING_IS_NULL = 1001;

    private void SendMessage(AndroidMessageContent androidMessageContent) {
        RetrofitServiceManager.getInstance().SetRetrofit(PropertiesConfig.messageUrl);
        addSubscribe(RetrofitServiceManager.getInstance().creat(MessageApiService.class)
                .AddMessageInfo(
                        userId,
                        messageClass,
                        androidMessageContent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(MessageInfoActivity.this) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            JSONArray responseJsonData = responseJson.getJSONArray("data");
                            Message msg = new Message();
                            switch (code) {
                                case RespCodeNumber.SUCCESS: //在数据库中更新用户数据出错；
                                    newMessages.clear();
                                    for (Object o : responseJsonData) {
                                        newMessages.add(JSONObject.parseObject(JSONObject.toJSONString(o), AndroidMessageContent.class));
                                    }
                                    msg.arg2 = RELOAD_TEAM_MESSAGE_INFO;
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

    public void reloadMessageInfo() {
        RetrofitServiceManager.getInstance().SetRetrofit(PropertiesConfig.messageUrl);
        addSubscribe(RetrofitServiceManager.getInstance().creat(MessageApiService.class)
                .GetMessageInfo(userId, ownerId, messageClass, messageListEnd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(MessageInfoActivity.this) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            JSONArray responseJsonData = responseJson.getJSONArray("data");
                            Message msg = new Message();
                            switch (code) {
                                case RespCodeNumber.SUCCESS: //在数据库中更新用户数据出错；
                                    newMessages.clear();
                                    if (responseJsonData.size() > 0) {
                                        for (Object o : responseJsonData) {
                                            newMessages.add(JSONObject.parseObject(JSONObject.toJSONString(o), AndroidMessageContent.class));
                                        }
                                        msg.arg1 = RELOAD_TEAM_MESSAGE_INFO;
                                        handler.sendMessage(msg);
                                    }
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
                        contentSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        contentSwipeRefreshLayout.setRefreshing(false);
                    }
                }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
//                GetLastMessageTimes();
//                reloadMessageInfo();
            }
        }, 0, 5000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }

    @Override
    public void onItemClickListener(int position) {

    }

    @Override
    public void onItemLongClickListener(int position) {

    }
}
