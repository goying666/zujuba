package renchaigao.com.zujuba.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import renchaigao.com.zujuba.Activity.Adapter.MessageInfoAdapter;
import renchaigao.com.zujuba.Activity.TeamPart.TeamActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.Bean.MessageContentInfo;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.FinalDefine;
import renchaigao.com.zujuba.util.OkhttpFunc;
import renchaigao.com.zujuba.util.PropertiesConfig;
import renchaigao.com.zujuba.widgets.DividerItemDecoration;

import static renchaigao.com.zujuba.util.PropertiesConfig.ACTIVITY_TEAM;
import static renchaigao.com.zujuba.util.PropertiesConfig.FRAGMENT_MESSAGE;


public class MessageInfoActivity extends AppCompatActivity {

    final static String TAG = "MessageInfoActivity";

    private MessageInfoAdapter messageInfoAdapter;
    private String userId;
    private String teamId, teamInfoString;
    private TeamInfo teamInfo;

    private Button message_info_sendButton;
    private TextInputEditText message_info_inputEdit;
    private SwipeRefreshLayout message_info_swip;
    private String inputString;
    private ImageView message_info_more;
    ArrayList<MessageContentInfo> allMessages = new ArrayList();
    private int COME_FROM;
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_info);
        IniteInputText();
        InitButton();
        InitRecyclerView();
        InitSwipeLayout();
        InitView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVITY_TEAM:
                COME_FROM = ACTIVITY_TEAM;
                break;
        }
    }


    private void InitNormalDate() {
        Intent intent = getIntent();
        UserInfo userInfo = DataUtil.getUserInfoData(this);
        teamInfo = JSONObject.parseObject(intent.getStringExtra("teamInfo"), TeamInfo.class);
        userId = userInfo.getId();
        teamId = teamInfo.getId();
        try{
            if (intent.getStringExtra("COME_FROM").equals("FRAGMENT_MESSAGE")){
                COME_FROM = FRAGMENT_MESSAGE;
            }
        }catch (Exception e){
            Log.e("e",e.toString());
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        InitNormalDate();
        InitMessageDate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        InitTimer();
        timer = new Timer();
        timer.schedule(task, 0, 3000);
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

    private void InitView() {
        message_info_more = findViewById(R.id.message_info_more);
        message_info_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageInfoActivity.this, TeamActivity.class);
                intent.putExtra("teamInfo", JSONObject.toJSONString(teamInfo));
                startActivityForResult(intent, PropertiesConfig.ACTIVITY_MESSAGE);
                finish();
            }
        });
    }

    private void InitSwipeLayout() {
//        message_info_swip = findViewById(R.id.message_info_swip);
    }

    private void IniteInputText() {
        message_info_inputEdit = findViewById(R.id.message_info_inputEdit);
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
    }

    private void InitButton() {
        message_info_sendButton = findViewById(R.id.message_info_sendButton);
        message_info_sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                判断是否为空 且 少于500字
//                满足条件下发送，
//                获取最后一条数据的times
                MessageContentInfo messageContentInfo = new MessageContentInfo();
                messageContentInfo.setId(Calendar.getInstance().getTimeInMillis());
                messageContentInfo.setSenderId(userId);
                messageContentInfo.setTeamId(teamId);
                messageContentInfo.setContent(inputString);
                messageContentInfo.setSendTime(Calendar.getInstance().getTimeInMillis());
                SendMessage(messageContentInfo);
                message_info_inputEdit.clearFocus();
                message_info_inputEdit.setText("");
                message_info_inputEdit.setHint("");
            }
        });
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


    private Long messageListEnd;

    private Long GetLastMessageTimes() {
        try {
            List<MessageContentInfo> messageContentList = LitePal.where("teamId = ?", teamId).
                    find(MessageContentInfo.class);
            Collections.sort(messageContentList, new Comparator<MessageContentInfo>() {
                @Override
                public int compare(MessageContentInfo o1, MessageContentInfo o2) {
                    return (int) (o2.getSendTime() - o1.getSendTime());
                }
            });
            messageListEnd = messageContentList.get(0).getSendTime();
        } catch (Exception e) {
            messageListEnd = 0L;
        }
        return messageListEnd;
    }

    private List<MessageContentInfo> GetLiteDBData() {
        return LitePal.where("teamId = ?", teamId).find(MessageContentInfo.class);
    }

    private void InitMessageDate() {
        //查询本地消息，从本地数据库读取当前群组的消息记录，并更新至界面；
        try {
            List<MessageContentInfo> messageContentList = GetLiteDBData();
            if (messageContentList.size() > 0) {
                messageListEnd = messageContentList.get(messageContentList.size() - 1).getSendTime();
                allMessages.addAll(messageContentList);
            } else {
                messageListEnd = 0L;
            }
            messageInfoAdapter.updateResults(allMessages);
        } catch (Exception e) {
            messageListEnd = 0L;
        }
        reloadAdapter();
    }

    //    本地存储消息
    private void SaveMessageDate(List<MessageContentInfo> messageContentList) {
        for (MessageContentInfo messageContentInfo : messageContentList) {
            if (messageContentInfo.save())
                System.out.println("save ok");
            else System.out.println("save fail");
        }
    }

    //    删除消息
    private void deleteMessagesFromLiteSQL(String sendTime) {
        DataSupport.deleteAll(MessageContentInfo.class, "sendTime = ?", sendTime);
    }

    private String reloadFlag = "RELOAD";

    private TimerTask task;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            // 要做的事情
            super.handleMessage(msg);
        }
    };

    private void InitTimer() {
        task = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
                if (reloadFlag.equals("RELOAD")) {
                    reloadAdapter();
                    reloadFlag = "STOP";
                }
            }
        };
    }

    @SuppressLint("StaticFieldLeak")
    public void reloadAdapter() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }

            @Override
            protected void onCancelled(Void aVoid) {
                super.onCancelled(aVoid);
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected Void doInBackground(Void... params) {
                Log.e(TAG, "doInBackground");

                String path = PropertiesConfig.messageUrl + "/message/get/" + userId + "/" + teamId + "/" + GetLastMessageTimes();
//                String path = PropertiesConfig.serverUrl + "store/get/storeinfo/" + JSONObject.parseObject(getActivity().getSharedPreferences("userData",getActivity().MODE_PRIVATE).getString("responseJsonDataString",null)).get("id").toString();
                OkHttpClient.Builder builder = new OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .writeTimeout(15, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true);
                builder.sslSocketFactory(OkhttpFunc.createSSLSocketFactory());
                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
                final Request request = new Request.Builder()
                        .url(path)
                        .header("Content-Type", "application/json")
                        .get()
                        .build();
                builder.build().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("onFailure", e.toString());
                        reloadFlag = "RELOAD";
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            reloadFlag = "RELOAD";
                            JSONObject responseJson = JSONObject.parseObject(response.body().string());
                            String responseJsoStr = responseJson.toJSONString();
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            JSONArray responseJsonData = responseJson.getJSONArray("data");

                            Log.e(TAG, "onResponse CODE OUT");
                            Log.e(TAG, "onResponse CODE is" + code);

                            switch (code) {
                                case 0: //在数据库中更新用户数据出错；
                                    ArrayList<MessageContentInfo> messageContents = new ArrayList();
                                    for (Object m : responseJsonData) {
                                        messageContents.add(JSONObject.parseObject(JSONObject.toJSONString(m), MessageContentInfo.class));
                                    }
//                                    Log.e("responseJsonData",responseJsonData.toJSONString());
                                    if (messageInfoAdapter == null) {
                                        messageInfoAdapter = new MessageInfoAdapter(MessageInfoActivity.this);
                                    }
                                    allMessages = new ArrayList<>(GetLiteDBData());
                                    allMessages.addAll(messageContents);
                                    messageInfoAdapter.updateResults(allMessages);
                                    messageInfoAdapter.notifyDataSetChanged();
                                    SaveMessageDate(messageContents);
                                    Log.e(TAG, "onResponse");
                                    break;
                            }
//                            swipeRefreshLayout.setRefreshing(false);
                        } catch (Exception e) {
                        }
                    }

                });
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (this == null)
                    return;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void SendMessage(final MessageContentInfo messageContentInfo) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }

            @Override
            protected void onCancelled(Void aVoid) {
                super.onCancelled(aVoid);
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected Void doInBackground(Void... params) {
                Log.e(TAG, "doInBackground");

                String path = PropertiesConfig.messageUrl + "/message/add/" + userId + "/" + teamId + "/" + GetLastMessageTimes();
//                String path = PropertiesConfig.serverUrl + "store/get/storeinfo/" + JSONObject.parseObject(getActivity().getSharedPreferences("userData",getActivity().MODE_PRIVATE).getString("responseJsonDataString",null)).get("id").toString();
                OkHttpClient.Builder builder = new OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .writeTimeout(15, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true);
                builder.sslSocketFactory(OkhttpFunc.createSSLSocketFactory());
                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });

                String storeInfoString = JSONObject.toJSONString(messageContentInfo);
                RequestBody jsonBody = RequestBody.create(FinalDefine.MEDIA_TYPE_JSON, storeInfoString);
                final Request request = new Request.Builder()
                        .url(path)
                        .header("Content-Type", "application/json")
                        .post(jsonBody)
                        .build();
                builder.build().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("onFailure", e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(response.body().string());
                            String responseJsoStr = responseJson.toJSONString();
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            JSONArray responseJsonData = responseJson.getJSONArray("data");

                            Log.e(TAG, "onResponse CODE OUT");
                            Log.e(TAG, "onResponse CODE is" + code);

                            switch (code) {
                                case 0: //在数据库中更新用户数据出错；
                                    ArrayList<MessageContentInfo> messageContents = new ArrayList();
                                    for (Object m : responseJsonData) {
                                        messageContents.add(JSONObject.parseObject(JSONObject.toJSONString(m), MessageContentInfo.class));
                                    }
//                                    Log.e("responseJsonData",responseJsonData.toJSONString());
                                    if (messageInfoAdapter == null) {
                                        messageInfoAdapter = new MessageInfoAdapter(MessageInfoActivity.this);
                                    }
                                    allMessages = new ArrayList<>(GetLiteDBData());
                                    allMessages.addAll(messageContents);
                                    messageInfoAdapter.updateResults(allMessages);
                                    messageInfoAdapter.notifyDataSetChanged();
                                    SaveMessageDate(messageContents);
                                    Log.e(TAG, "onResponse");
                                    break;
                            }
//                            swipeRefreshLayout.setRefreshing(false);
                        } catch (Exception e) {
                        }
                    }

                });
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (this == null)
                    return;
            }
        }.execute();
    }


}
