package renchaigao.com.zujuba.Activity.Message;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.renchaigao.zujuba.mongoDB.info.team.TeamInfo;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import renchaigao.com.zujuba.Activity.Adapter.MessageInfoAdapter;
import renchaigao.com.zujuba.Activity.BaseActivity;
import renchaigao.com.zujuba.Activity.TeamPart.TeamActivity;
import renchaigao.com.zujuba.Bean.MessageContent;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.FinalDefine;
import renchaigao.com.zujuba.util.OkhttpFunc;
import renchaigao.com.zujuba.util.PropertiesConfig;
import renchaigao.com.zujuba.util.http.ApiService;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;
import renchaigao.com.zujuba.widgets.DividerItemDecoration;

import static renchaigao.com.zujuba.util.PropertiesConfig.ACTIVITY_MESSAGE_PAGE;
import static renchaigao.com.zujuba.util.PropertiesConfig.ACTIVITY_TEAM_PAGE;
import static renchaigao.com.zujuba.util.PropertiesConfig.FRAGMENT_MESSAGE_PAGE;


public class MessageInfoActivity extends BaseActivity {

    final static String TAG = "MessageInfoActivity";

    private MessageInfoAdapter messageInfoAdapter;
    private String userId;
    private String teamId, teamNameString;
    private TeamInfo teamInfo;
    private UserInfo userInfo;

    private Button message_info_sendButton;
    private TextInputEditText message_info_inputEdit;
    private SwipeRefreshLayout message_info_swip;
    private String inputString;
    private ImageView message_info_more;
    private TextView messageName;
    ArrayList<MessageContent> allMessages = new ArrayList();
    ArrayList<MessageContent> newMessages = new ArrayList();
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
        //查询本地消息，从本地数据库读取当前群组的消息记录，并更新至界面；
        if (newMessages.size() > 0) {
            for (MessageContent o : newMessages) {
                o.setIsReceived(true);
                o.save();
                allMessages.add(o);
                messageListEnd = o.getSendTime();
            }
            messageInfoAdapter.updateResults(allMessages);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVITY_TEAM_PAGE:
                teamId = data.getStringExtra("teamId");
                teamNameString=data.getStringExtra("teamName");
                break;
            case FRAGMENT_MESSAGE_PAGE:
                break;
        }
    }

    @Override
    protected void InitView() {
        messageName = findViewById(R.id.message_info_title);
        messageName.setText(teamNameString);
        message_info_inputEdit = findViewById(R.id.message_info_inputEdit);
        message_info_sendButton = findViewById(R.id.message_info_sendButton);
        message_info_more = findViewById(R.id.message_info_more);
    }

    @Override
    protected void InitData() {
        userInfo = DataUtil.GetUserInfoData(this);
        userId = userInfo.getId();
    }

    @Override
    protected void InitOther() {
        InitRecyclerView();
        InitSwipeLayout();
        message_info_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageInfoActivity.this, TeamActivity.class);
                intent.putExtra("teamInfo", JSONObject.toJSONString(teamInfo));
                startActivityForResult(intent, PropertiesConfig.ACTIVITY_MESSAGE_PAGE);
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
                    MessageContent messageContent = new MessageContent();
                    long nowTimeLong = Calendar.getInstance().getTimeInMillis();
                    messageContent.setId(nowTimeLong);
                    messageContent.setIsMe(true);
                    messageContent.setIsGroup(false);
                    messageContent.setSenderId(userId);
                    messageContent.setTitle("");
                    messageContent.setSendTime(nowTimeLong);
                    messageContent.setIsReceived(false);
                    messageContent.setSenderImageUrl(userInfo.getPicPath());
                    messageContent.setMyImageUrl(userInfo.getPicPath());
                    messageContent.setTeamId(teamId);
                    messageContent.setFriendId("");
                    messageContent.setSendTime(Calendar.getInstance().getTimeInMillis());
                    messageContent.setContent(inputString);
                    SendMessage(messageContent);
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
//        message_info_swip = findViewById(R.id.message_info_swip);
    }

    private void InitRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.message_info_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        messageInfoAdapter = new MessageInfoAdapter(this);
        recyclerView.setAdapter(messageInfoAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    private Long messageListEnd = 0L;

    private Long GetLastMessageTimes() {
        try {
            List<MessageContent> messageContentList = LitePal.where("teamId = ?", teamId).
                    find(MessageContent.class);
            Collections.sort(messageContentList, new Comparator<MessageContent>() {
                @Override
                public int compare(MessageContent o1, MessageContent o2) {
                    return (int) (o2.getSendTime() - o1.getSendTime());
                }
            });
            if (messageContentList.size() > 0)
                messageListEnd = messageContentList.get(0).getSendTime();
            else
                messageListEnd = 0L;
        } catch (Exception e) {
            messageListEnd = 0L;
        }
        return messageListEnd;
    }

    //    本地存储消息
    private void SaveMessageDate(List<MessageContent> messageContentList) {
        for (MessageContent messageContent : messageContentList) {
            if (messageContent.save())
                System.out.println("save ok");
            else System.out.println("save fail");
        }
    }

    //    删除消息
    private void deleteMessagesFromLiteSQL(String sendTime) {
        DataSupport.deleteAll(MessageContent.class, "sendTime = ?", sendTime);
    }


    final private static int RELOAD_TEAM_MESSAGE_INFO = 1000;
    final private static int INPUTSTRING_IS_NULL = 1001;

    private void SendMessage(MessageContent messageContent) {
        RetrofitServiceManager.getInstance().SetRetrofit(PropertiesConfig.messageUrl);
        addSubscribe(RetrofitServiceManager.getInstance().creat(ApiService.class)
                .FourParameterJsonPost("add",
                        userId,
                        teamId,
                        GetLastMessageTimes().toString(),
                        JSONObject.parseObject(JSONObject.toJSONString(messageContent), JSONObject.class))
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
                                        newMessages.add(JSONObject.parseObject(JSONObject.toJSONString(o), MessageContent.class));
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

    public void reloadTeamMessageInfo() {
        RetrofitServiceManager.getInstance().SetRetrofit(PropertiesConfig.messageUrl);
        addSubscribe(RetrofitServiceManager.getInstance().creat(ApiService.class)
                .FourParameterGet("getteam",
                        userId,
                        teamId,
                        GetLastMessageTimes().toString())
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
                                        newMessages.add(JSONObject.parseObject(JSONObject.toJSONString(o), MessageContent.class));
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
//
//    public void reloadAdapter() {
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected void onCancelled() {
//                super.onCancelled();
//            }
//
//            @Override
//            protected void onCancelled(Void aVoid) {
//                super.onCancelled(aVoid);
//            }
//
//            @Override
//            protected void onProgressUpdate(Void... values) {
//                super.onProgressUpdate(values);
//            }
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                Log.e(TAG, "doInBackground");
//
//                String path = PropertiesConfig.messageUrl + "/message/get/" + userId + "/" + teamId + "/" + GetLastMessageTimes();
////                String path = PropertiesConfig.serverUrl + "store/get/storeinfo/" + JSONObject.parseObject(getActivity().getSharedPreferences("userData",getActivity().MODE_PRIVATE).getString("responseJsonDataString",null)).get("id").toString();
//                OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                        .connectTimeout(15, TimeUnit.SECONDS)
//                        .readTimeout(15, TimeUnit.SECONDS)
//                        .writeTimeout(15, TimeUnit.SECONDS)
//                        .retryOnConnectionFailure(true);
//                builder.sslSocketFactory(OkhttpFunc.createSSLSocketFactory());
//                builder.hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                });
//                final Request request = new Request.Builder()
//                        .url(path)
//                        .header("Content-Type", "application/json")
//                        .get()
//                        .build();
//                builder.build().newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        Log.e("onFailure", e.toString());
//                        reloadFlag = "RELOAD";
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        try {
//                            reloadFlag = "RELOAD";
//                            JSONObject responseJson = JSONObject.parseObject(response.body().string());
//                            String responseJsoStr = responseJson.toJSONString();
//                            int code = Integer.valueOf(responseJson.get("code").toString());
//                            JSONArray responseJsonData = responseJson.getJSONArray("data");
//
//                            Log.e(TAG, "onResponse CODE OUT");
//                            Log.e(TAG, "onResponse CODE is" + code);
//
//                            switch (code) {
//                                case 0: //在数据库中更新用户数据出错；
//                                    ArrayList<MessageContent> messageContents = new ArrayList();
//                                    for (Object m : responseJsonData) {
//                                        messageContents.add(JSONObject.parseObject(JSONObject.toJSONString(m), MessageContent.class));
//                                    }
////                                    Log.e("responseJsonData",responseJsonData.toJSONString());
//                                    if (messageInfoAdapter == null) {
//                                        messageInfoAdapter = new MessageInfoAdapter(MessageInfoActivity.this);
//                                    }
//                                    allMessages = new ArrayList<>(GetLiteDBData());
//                                    allMessages.addAll(messageContents);
//                                    messageInfoAdapter.updateResults(allMessages);
//                                    messageInfoAdapter.notifyDataSetChanged();
//                                    SaveMessageDate(messageContents);
//                                    Log.e(TAG, "onResponse");
//                                    break;
//                            }
////                            swipeRefreshLayout.setRefreshing(false);
//                        } catch (Exception e) {
//                        }
//                    }
//
//                });
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                if (this == null)
//                    return;
//            }
//        }.execute();
//    }

//    @SuppressLint("StaticFieldLeak")
//    public void SendMessage(final MessageContent messageContent) {
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected void onCancelled() {
//                super.onCancelled();
//            }
//
//            @Override
//            protected void onCancelled(Void aVoid) {
//                super.onCancelled(aVoid);
//            }
//
//            @Override
//            protected void onProgressUpdate(Void... values) {
//                super.onProgressUpdate(values);
//            }
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                Log.e(TAG, "doInBackground");
//
//                String path = PropertiesConfig.messageUrl + "/message/add/" + userId + "/" + teamId + "/" + GetLastMessageTimes();
////                String path = PropertiesConfig.serverUrl + "store/get/storeinfo/" + JSONObject.parseObject(getActivity().getSharedPreferences("userData",getActivity().MODE_PRIVATE).getString("responseJsonDataString",null)).get("id").toString();
//                OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                        .connectTimeout(15, TimeUnit.SECONDS)
//                        .readTimeout(15, TimeUnit.SECONDS)
//                        .writeTimeout(15, TimeUnit.SECONDS)
//                        .retryOnConnectionFailure(true);
//                builder.sslSocketFactory(OkhttpFunc.createSSLSocketFactory());
//                builder.hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                });
//
//                String storeInfoString = JSONObject.toJSONString(messageContent);
//                RequestBody jsonBody = RequestBody.create(FinalDefine.MEDIA_TYPE_JSON, storeInfoString);
//                final Request request = new Request.Builder()
//                        .url(path)
//                        .header("Content-Type", "application/json")
//                        .post(jsonBody)
//                        .build();
//                builder.build().newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        Log.e("onFailure", e.toString());
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        try {
//                            JSONObject responseJson = JSONObject.parseObject(response.body().string());
//                            String responseJsoStr = responseJson.toJSONString();
//                            int code = Integer.valueOf(responseJson.get("code").toString());
//                            JSONArray responseJsonData = responseJson.getJSONArray("data");
//
//                            Log.e(TAG, "onResponse CODE OUT");
//                            Log.e(TAG, "onResponse CODE is" + code);
//
//                            switch (code) {
//                                case 0: //在数据库中更新用户数据出错；
//                                    ArrayList<MessageContent> messageContents = new ArrayList();
//                                    for (Object m : responseJsonData) {
//                                        messageContents.add(JSONObject.parseObject(JSONObject.toJSONString(m), MessageContent.class));
//                                    }
////                                    Log.e("responseJsonData",responseJsonData.toJSONString());
//                                    if (messageInfoAdapter == null) {
//                                        messageInfoAdapter = new MessageInfoAdapter(MessageInfoActivity.this);
//                                    }
//                                    allMessages = new ArrayList<>(GetLiteDBData());
//                                    allMessages.addAll(messageContents);
//                                    messageInfoAdapter.updateResults(allMessages);
//                                    messageInfoAdapter.notifyDataSetChanged();
//                                    SaveMessageDate(messageContents);
//                                    Log.e(TAG, "onResponse");
//                                    break;
//                            }
////                            swipeRefreshLayout.setRefreshing(false);
//                        } catch (Exception e) {
//                        }
//                    }
//
//                });
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                if (this == null)
//                    return;
//            }
//        }.execute();
//    }


    @Override
    protected void onResume() {
        super.onResume();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                reloadTeamMessageInfo();
            }
        }, 0, 3000);
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

}
