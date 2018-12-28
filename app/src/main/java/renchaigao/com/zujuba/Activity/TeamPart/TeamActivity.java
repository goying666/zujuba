package renchaigao.com.zujuba.Activity.TeamPart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.mongoDB.info.team.TeamInfo;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import renchaigao.com.zujuba.Activity.MessageInfoActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.OkhttpFunc;
import renchaigao.com.zujuba.util.PropertiesConfig;

import static renchaigao.com.zujuba.util.PropertiesConfig.ACTIVITY_MESSAGE;
import static renchaigao.com.zujuba.util.PropertiesConfig.ACTIVITY_TEAM;
import static renchaigao.com.zujuba.util.PropertiesConfig.FRAGMENT_TEAM;

public class TeamActivity extends AppCompatActivity {

    private TextView ti_team_number, ti_user_position, ti_user_state, ti_text_null1, ti_text_distance,
            ti_store_score, ti_text_spend, ti_text_rank, ti_start_data, ti_start_time, ti_spend_way,
            ti_game_class, ti_filter_all_number, ti_filter_my_sum, ti_IntegrityScore, ti_IntegrityScore_result_info,
            ti_sexRatio, ti_sexRatio_result_info, ti_ageScreening, ti_ageScreening_result_info, ti_evaluationScreening,
            ti_evaluationScreening_result_info, ti_careerScreening, ti_careerScreening_result_info, ti_marriage,
            ti_marriage_result_info, ti_note_info;
    private TextView ti_player_number_all;
    private Button ti_join_button;
    private UserInfo userInfo;
    private String userId, teamId;
    private TeamInfo teamInfo;
    private Timer timer = new Timer();
    private TimerTask task;
    private String reloadFlag = "RELOAD";
    private Integer reloadNUM = 0;
    private int COME_FROM;

    private void initView() {
        ti_team_number = findViewById(R.id.ti_team_number);
        ti_user_position = findViewById(R.id.ti_user_position);
        ti_user_state = findViewById(R.id.ti_user_state);
        ti_text_null1 = findViewById(R.id.ti_text_null1);
        ti_text_distance = findViewById(R.id.ti_text_distance);
        ti_store_score = findViewById(R.id.ti_store_score);
        ti_text_spend = findViewById(R.id.ti_text_spend);
        ti_text_rank = findViewById(R.id.ti_text_rank);
        ti_start_data = findViewById(R.id.ti_start_data);
        ti_start_time = findViewById(R.id.ti_start_time);
        ti_spend_way = findViewById(R.id.ti_spend_way);
        ti_game_class = findViewById(R.id.ti_game_class);
        ti_filter_all_number = findViewById(R.id.ti_filter_all_number);
        ti_filter_my_sum = findViewById(R.id.ti_filter_my_sum);
        ti_IntegrityScore = findViewById(R.id.ti_IntegrityScore);
        ti_IntegrityScore_result_info = findViewById(R.id.ti_IntegrityScore_result_info);
        ti_sexRatio = findViewById(R.id.ti_sexRatio);
        ti_sexRatio_result_info = findViewById(R.id.ti_sexRatio_result_info);
        ti_ageScreening = findViewById(R.id.ti_ageScreening);
        ti_ageScreening_result_info = findViewById(R.id.ti_ageScreening_result_info);
        ti_evaluationScreening = findViewById(R.id.ti_evaluationScreening);
        ti_evaluationScreening_result_info = findViewById(R.id.ti_evaluationScreening_result_info);
        ti_careerScreening = findViewById(R.id.ti_careerScreening);
        ti_careerScreening_result_info = findViewById(R.id.ti_careerScreening_result_info);
        ti_marriage = findViewById(R.id.ti_marriage);
        ti_marriage_result_info = findViewById(R.id.ti_marriage_result_info);
        ti_note_info = findViewById(R.id.ti_note_info);
        ti_player_number_all = findViewById(R.id.ti_player_number_all);
    }

    private void setViewDate() {
        ti_player_number_all.setText(teamInfo.getPlayerMin().toString() + "~" + teamInfo.getPlayerMax().toString());
    }

    private void setToolBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void InitNormalDate() {
        Intent intent = getIntent();
        String teamString = intent.getStringExtra("teamInfo");
        teamInfo = JSONObject.parseObject(teamString, TeamInfo.class);
        userInfo = DataUtil.getUserInfoData(this);
        userId = userInfo.getId();
        teamId = teamInfo.getId();
        try{
            if (intent.getStringExtra("COME_FROM").equals("FRAGMENT_TEAM")){
                COME_FROM = FRAGMENT_TEAM;
            }
        }catch (Exception e){
            Log.e("e",e.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        setToolBar();
        initView();
        InitButton();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVITY_MESSAGE:
                COME_FROM = ACTIVITY_MESSAGE;
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        InitNormalDate();
        setViewDate();
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
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            // 要做的事情
            super.handleMessage(msg);
            setViewDate();
        }
    };


    private void InitTimer() {
        task = new TimerTask() {
            @Override
            public void run() {
//                reloadAdapter();
//                    reloadNUM++;
//                    Log.e("TeamActivity", reloadNUM.toString());
                // TODO Auto-generated method stub
                if (reloadFlag.equals("RELOAD")) {
                    reloadAdapter();
                    reloadNUM++;
                    Log.e("TeamActivity", reloadNUM.toString());
                    reloadFlag = "STOP";
                }
            }
        };
    }

    public int TEAM_JION_PAGE = 901;

    private void InitButton() {

        ti_join_button = findViewById(R.id.ti_join_button);
        ti_join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                判断资格
//                资格不通过，提示错误信息
//                资格通过，进入聊天界面；
//               判断加入用户是否满足team的约束条件，若不满足则提示错误；

//                请求服务器，获取team最新信息；
                SendMessage();
//                修改“加入”按键文字；

//                显示申请加入的提示，直到返回正确的加入信息后，否则提示失败。

            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public void SendMessage() {
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

                String path = PropertiesConfig.teamServerUrl + "join/" + userId + "/" + teamId;//
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
                String userInfoString = JSONObject.toJSONString(userInfo);
                String teamInfoString = JSONObject.toJSONString(teamInfo);
//                RequestBody jsonBody = RequestBody.create(FinalDefine.MEDIA_TYPE_JSON, storeInfoString);
//                final Request request = new Request.Builder()
//                        .url(path)
//                        .header("Content-Type", "application/json")
//                        .post(jsonBody)
//                        .build();

                RequestBody multiBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("userInfo", userInfoString)
                        .addFormDataPart("teamInfo", teamInfoString)
                        .build();
                Request mulRrequest = new Request.Builder()
                        .url(path)
                        .header("Content-Type", "multipart/form-data")
                        .post(multiBody)
                        .build();

                builder.build().newCall(mulRrequest).enqueue(new Callback() {
                    //                builder.build().newCall(request).enqueue(new Callback() {
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
//                根据服务器返回结果提示
                            switch (code) {
                                case 0: //在数据库中更新用户数据出错
                                    Intent intent = new Intent(TeamActivity.this, MessageInfoActivity.class);
                                    intent.putExtra("teamInfo", JSONObject.toJSONString(teamInfo));
                                    startActivityForResult(intent, ACTIVITY_TEAM);
                                    finish();
                                    break;
                            }
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

                String path = PropertiesConfig.teamServerUrl + "/update/" + userId + "/" + teamId;
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
                Request request = new Request.Builder()
                        .url(path)
                        .header("Content-Type", "application/json")
                        .get()
                        .build();
                builder.build().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        reloadFlag = "RELOAD";
                        Log.e("onFailure", e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            reloadFlag = "RELOAD";
                            JSONObject responseJson = JSONObject.parseObject(response.body().string());
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            Message message = new Message();
                            switch (code) {
                                case 1201: //更新teaminfo成功，并且判断出该用户已经加入该team
                                    teamInfo = JSONObject.parseObject(responseJson.get("data").toString(), TeamInfo.class);
                                    if (COME_FROM == FRAGMENT_TEAM) {
                                        Intent intent = new Intent(TeamActivity.this, MessageInfoActivity.class);
                                        intent.putExtra("teamInfo", JSONObject.toJSONString(teamInfo));
                                        startActivityForResult(intent, ACTIVITY_TEAM);
                                        finish();
                                    } else if (COME_FROM == ACTIVITY_MESSAGE) {

                                    }
                                    break;
                                case 1200://更新teaminfo成功，用户未加入该team
                                    teamInfo = JSONObject.parseObject(responseJson.get("data").toString(), TeamInfo.class);
                                    message.what = 2;
                                    handler.sendMessage(message);
                                    break;
                            }
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
