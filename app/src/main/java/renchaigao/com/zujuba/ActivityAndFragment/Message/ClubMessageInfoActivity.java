package renchaigao.com.zujuba.ActivityAndFragment.Message;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.BaseActivity;
import renchaigao.com.zujuba.ActivityAndFragment.Club.ClubInfoActivity;
import renchaigao.com.zujuba.ActivityAndFragment.Message.Adapter.ClubMessageInfoAdapter;
import renchaigao.com.zujuba.Bean.AndroidCardMessageFragmentTipBean;
import renchaigao.com.zujuba.Bean.AndroidMessageContent;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.MessageApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;

import static com.renchaigao.zujuba.PropertiesConfig.ConstantManagement.CLUB_SEND_MESSAGE;

public class ClubMessageInfoActivity extends BaseActivity implements CommonViewHolder.onItemCommonClickListener {

    private ClubMessageInfoAdapter clubMessageInfoAdapter;
    private RecyclerView recyclerView;
    final static String TAG = "ClubMessageInfoActivity";
    private String userId, token, ownerId, clubId, messageClass, inputString = "";
    private UserInfo userInfo;
    private Button message_info_sendButton;
    private TextInputEditText message_info_inputEdit;
    private SwipeRefreshLayout contentSwipeRefreshLayout;
    ArrayList<AndroidMessageContent> allMessages = new ArrayList<AndroidMessageContent>();
    ArrayList<AndroidMessageContent> newMessages = new ArrayList<AndroidMessageContent>();
    private Timer timer = new Timer();
    private final static String RELOAD_FLAGE_VALUE_RELOAD = "RELOAD";
    private final static String RELOAD_FLAGE_VALUE_STOP = "STOP";
    private String reloadFlag = RELOAD_FLAGE_VALUE_RELOAD;
    final private static int RELOAD_TEAM_MESSAGE_INFO = 1000;
    final private static int INPUTSTRING_IS_NULL = 1001;
    private long messageListEnd = 0L;

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

    /*
     * 界面更新 、 数据刷新
     * */
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
                return (int) (o2.getSendTime() - o1.getSendTime());
            }
        });
        clubMessageInfoAdapter.updateResults(allMessages);
        clubMessageInfoAdapter.notifyDataSetChanged();
        reloadFlag = RELOAD_FLAGE_VALUE_RELOAD;
    }

    private ConstraintLayout toolbar;
    private TextView titleTextView, secondTitleTextView;
    private ImageView goback;

    @Override
    protected void InitView() {
        toolbar = (ConstraintLayout) findViewById(R.id.message_info_toolbar);
        titleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        secondTitleTextView = (TextView) toolbar.findViewById(R.id.toolbarSecondTitle);

        goback = (ImageView) toolbar.findViewById(R.id.toolbarBack);
        contentSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.message_info_SwipeRefreshLayout);
        message_info_inputEdit = (TextInputEditText) findViewById(R.id.message_info_inputEdit);
        message_info_sendButton = (Button) findViewById(R.id.message_info_sendButton);
        InitRecyclerView();
    }

    private void InitRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.message_info_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        clubMessageInfoAdapter = new ClubMessageInfoAdapter(this, allMessages, this);
        recyclerView.setAdapter(clubMessageInfoAdapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void InitData() {
        messageClass = CLUB_SEND_MESSAGE;
        userInfo = DataUtil.GetUserInfoData(this);
        userId = userInfo.getId();
        token = userInfo.getToken();
        ownerId = getIntent().getStringExtra("clubId");
        clubId = getIntent().getStringExtra("clubId");
        titleTextView.setText(getIntent().getStringExtra("title"));
        secondTitleTextView.setText("俱乐部");
        allMessages = new ArrayList<>(LitePal.where("clubId = ?", clubId).find(AndroidMessageContent.class));
        Collections.sort(allMessages, new Comparator<AndroidMessageContent>() {
            @Override
            public int compare(AndroidMessageContent o1, AndroidMessageContent o2) {
                return (int) (o2.getSendTime() - o1.getSendTime());
            }
        });
        if (allMessages.size() > 0) {
            clubMessageInfoAdapter.updateResults(allMessages);
            clubMessageInfoAdapter.notifyDataSetChanged();
        }
    }

    private void InitSwipeLayout() {
        contentSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                reloadMessageInfo();
            }
        });
    }

    @Override
    protected void InitOther() {
        InitSwipeLayout();
        message_info_inputEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    // 获得焦点
//                    recyclerView.scrollToPosition(0);
//                } else {
//                    // 失去焦点
//                    recyclerView.scrollToPosition(0);
//                }
                recyclerView.scrollToPosition(0);
            }
        });
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        secondTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ClubMessageInfoActivity.this, ClubInfoActivity.class);
                intent.putExtra("clubId", clubId);
                startActivity(intent);
                finish();
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
                    androidMessageContent.setMessageClass(CLUB_SEND_MESSAGE);
                    androidMessageContent.setTitle(getIntent().getStringExtra("title"));
                    androidMessageContent.setIdLong(nowTimeLong);
                    androidMessageContent.setIsMe(true);
                    androidMessageContent.setIsGroup(false);
                    androidMessageContent.setSenderId(userId);
                    androidMessageContent.setSendTime(nowTimeLong);
                    androidMessageContent.setIsReceived(false);
                    androidMessageContent.setSenderImageUrl(userInfo.getPicPath());
                    androidMessageContent.setMyImageUrl(userInfo.getPicPath());
                    androidMessageContent.setClubId(clubId);
                    androidMessageContent.setSendTime(Calendar.getInstance().getTimeInMillis());
                    androidMessageContent.setContent(inputString);
                    SendMessage(androidMessageContent);
                    message_info_inputEdit.clearFocus();
                    message_info_inputEdit.setText("");
                    message_info_inputEdit.setHint("");
                    inputString = "";
                }
                message_info_inputEdit.requestFocus();
            }
        });
    }

    @Override
    protected void UpdateView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_info;
    }

    private void SendMessage(AndroidMessageContent androidMessageContent) {
        addSubscribe(RetrofitServiceManager.getInstance().creat(MessageApiService.class)
                .AddMessageInfo(
                        userId,
                        token,
                        androidMessageContent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(ClubMessageInfoActivity.this) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            JSONArray responseJsonData = responseJson.getJSONArray("data");
                            Message msg = new Message();
                            switch (code) {
                                case RespCodeNumber.MESSAGE_ADD_CLUB_SUCCESS: //在数据库中更新用户数据出错；
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
                    }

                    @Override
                    public void onComplete() {
                        reloadMessageInfo();
                    }
                }));
    }

    public void reloadMessageInfo() {
        addSubscribe(RetrofitServiceManager.getInstance().creat(MessageApiService.class)
                .GetMessageInfo(userId, ownerId, messageClass, messageListEnd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(ClubMessageInfoActivity.this) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            JSONArray responseJsonData = responseJson.getJSONArray("data");
                            Message msg = new Message();
                            switch (code) {
                                case RespCodeNumber.MESSAGE_USER_GET_CLUB_SUCCESS: //在数据库中更新用户数据出错；
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
                        reloadFlag = RELOAD_FLAGE_VALUE_RELOAD;
                    }

                    @Override
                    public void onComplete() {
                        contentSwipeRefreshLayout.setRefreshing(false);
                        reloadFlag = RELOAD_FLAGE_VALUE_RELOAD;
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
                if (reloadFlag.equals(RELOAD_FLAGE_VALUE_RELOAD)) {
//                    GetLastMessageTimes();
                    reloadMessageInfo();
                    reloadFlag = RELOAD_FLAGE_VALUE_STOP;
                }
            }
        }, 0, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        if (allMessages.size() > 0) {
//        每次页面停止时，将该页的message数据更新至数据库的messageFragmentBeam内
            AndroidCardMessageFragmentTipBean androidCardMessageFragmentTipBean = new AndroidCardMessageFragmentTipBean();
            AndroidMessageContent androidMessageContent = allMessages.get(0);
            androidCardMessageFragmentTipBean.setImageUrl(androidMessageContent.getSenderImageUrl());
            androidCardMessageFragmentTipBean.setName(androidMessageContent.getTitle());
            androidCardMessageFragmentTipBean.setContent(androidMessageContent.getContent());
            androidCardMessageFragmentTipBean.setTime(androidMessageContent.getSendTime().toString());
            androidCardMessageFragmentTipBean.setNoRead(0);
            androidCardMessageFragmentTipBean.setMClass(androidMessageContent.getMessageClass());
            androidCardMessageFragmentTipBean.setOwnerId(clubId);
            androidCardMessageFragmentTipBean.setLastTime(androidMessageContent.getSendTime());
            androidCardMessageFragmentTipBean.saveOrUpdate("ownerId = ?", clubId);
        }
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
