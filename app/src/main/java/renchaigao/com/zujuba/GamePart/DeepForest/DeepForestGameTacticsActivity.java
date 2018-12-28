package renchaigao.com.zujuba.GamePart.DeepForest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.dao.User;
import com.renchaigao.zujuba.mongoDB.info.team.TeamInfo;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.FinalDefine;
import renchaigao.com.zujuba.util.OkhttpFunc;
import renchaigao.com.zujuba.util.PropertiesConfig;

public class DeepForestGameTacticsActivity extends AppCompatActivity {

    private Button dfg_tactics_attact_button;
    private Button dfg_tactics_defence_button;
    private Button dfg_tactics_explore_button;
    private Button dfg_tactics_move_button;
    private Button dfg_tactics_technology_button;
    private TextView dfg_tactics_time;

    private void initView() {
        dfg_tactics_time = findViewById(R.id.dfg_tactics_time);
        dfg_tactics_attact_button = findViewById(R.id.dfg_tactics_attact_button);
        dfg_tactics_defence_button = findViewById(R.id.dfg_tactics_defence_button);
        dfg_tactics_explore_button = findViewById(R.id.dfg_tactics_attact_button);
        dfg_tactics_move_button = findViewById(R.id.dfg_tactics_move_button);
        dfg_tactics_technology_button = findViewById(R.id.dfg_tactics_technology_button);
    }

    private void setButton() {
        dfg_tactics_attact_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeepForestGameTacticsActivity.this, DeepForestGameTacticsAttactActivity.class);
                startActivity(intent);
            }
        });
        dfg_tactics_defence_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeepForestGameTacticsActivity.this, DeepForestGameTacticsDefenceActivity.class);
                startActivity(intent);
            }
        });
        dfg_tactics_explore_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeepForestGameTacticsActivity.this, DeepForestGameTacticsExploreActivity.class);
                startActivity(intent);
            }
        });
        dfg_tactics_move_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeepForestGameTacticsActivity.this, DeepForestGameTacticsMoveActivity.class);
                startActivity(intent);
            }
        });
        dfg_tactics_technology_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                向后台服务器发送 发展科技的请求；

//                等待发送成功，
                sendMessageToServer();
                Intent intent = new Intent(DeepForestGameTacticsActivity.this, DeepForestGameTacticsSettleActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_forest_game_tactics);
        initView();
        setButton();
    }

    @SuppressLint("StaticFieldLeak")
    public void sendMessageToServer() {
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
                User user = DataUtil.getUserData(DeepForestGameTacticsActivity.this);
                String userId = user.getId();
                String userToken = DataUtil.getUserData(DeepForestGameTacticsActivity.this).getToken();
                TeamInfo teamInfo = DataUtil.getTeamInfo(DeepForestGameTacticsActivity.this);
                String path = PropertiesConfig.deepForestGameUrl + "dfg/do/" + teamInfo.getId() + "/" + userId + "/" + userToken;
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
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("key", "KJ");
                String stringBody = JSONObject.toJSONString(jsonObject);
                RequestBody jsonBody = RequestBody.create(FinalDefine.MEDIA_TYPE_JSON, stringBody);
                final Request request = new Request.Builder()
                        .url(path)
                        .header("Content-Type", "application/json")
                        .post(jsonBody)
                        .build();
                builder.build().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(response.body().string());
                            String responseJsoStr = responseJson.toJSONString();
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            JSONArray responseJsonData = responseJson.getJSONArray("data");
                            switch (code) {
                                case 0: //在数据库中更新用户数据出错；
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
            }
        }.execute();
    }
}
