package renchaigao.com.zujuba.GamePart.DeepForest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.renchaigao.zujuba.mongoDB.info.game.DeepForest.CivilizationInfo;
import com.renchaigao.zujuba.mongoDB.info.game.DeepForest.DeepForestGameInfo;
import com.renchaigao.zujuba.mongoDB.info.game.DeepForest.StarInfo;
import com.renchaigao.zujuba.mongoDB.info.team.TeamInfo;

import java.util.Timer;
import java.util.TimerTask;

import renchaigao.com.zujuba.GamePart.DeepForest.Adapter.DFGNoteAdapter;
import renchaigao.com.zujuba.GamePart.DeepForest.Adapter.DFGPlayerAdapter;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.widgets.DividerItemDecoration;

public class DeepForestGameMainActivity extends AppCompatActivity {

    private Button dfg_main_button;
    private Button dfg_main_mine_informationValue_button;
    private Button dfg_main_mine_star_info_button;
    private TabLayout tabLayout;
    private TabItem dfg_main_note_part_text1;
    private TabItem dfg_main_note_part_text2;
    private TextView dfg_main_gameInfo_current_countDown;
    private TextView dfg_main_gameInfo_currentMoment;
    private TextView dfg_main_gameInfo_currentMoment_info;
    private TextView dfg_main_gameInfo_playerNum;
    private TextView dfg_main_gameInfo_roundNum;
    private TextView dfg_main_gameInfo_starNum;
    private TextView dfg_main_gameInfo_worldTime;
    private TextView dfg_main_mine_active;
    private TextView dfg_main_mine_defenceValue;
    private TextView dfg_main_mine_informationValue;
    private TextView dfg_main_mine_playerNum;
    private TextView dfg_main_mine_star_info;
    private TextView dfg_main_mine_technologyValue;
    private TextView dfg_main_other_activeSum;
    private TextView dfg_main_other_ggSum;

    private DeepForestGameInfo deepForestGameInfo;
    private StarInfo myStarInfo;
    private CivilizationInfo myCivilization;
    private TeamInfo teamInfo;

    private RecyclerView dfg_main_note_part_textRcyclerView, dfg_main_other_info_recyclerView;
    private LinearLayoutManager layoutManager1, layoutManager2;
    private DFGNoteAdapter dfgNoteAdapter;
    private DFGPlayerAdapter dfgPlayerAdapter;

    private void initView() {
        tabLayout = findViewById(R.id.tabLayout);
        dfg_main_button = findViewById(R.id.dfg_main_button);
        dfg_main_mine_informationValue_button = findViewById(R.id.dfg_main_mine_informationValue_button);
        dfg_main_mine_star_info_button = findViewById(R.id.dfg_main_mine_star_info_button);
        dfg_main_note_part_textRcyclerView = findViewById(R.id.dfg_main_note_part_textRcyclerView);
        dfg_main_other_info_recyclerView = findViewById(R.id.dfg_main_other_info_recyclerView);
        dfg_main_note_part_text1 = findViewById(R.id.dfg_main_note_part_text1);
        dfg_main_note_part_text2 = findViewById(R.id.dfg_main_note_part_text2);
        dfg_main_gameInfo_current_countDown = findViewById(R.id.dfg_main_gameInfo_current_countDown);
        dfg_main_gameInfo_currentMoment = findViewById(R.id.dfg_main_gameInfo_currentMoment);
        dfg_main_gameInfo_currentMoment_info = findViewById(R.id.dfg_main_gameInfo_currentMoment_info);
        dfg_main_gameInfo_playerNum = findViewById(R.id.dfg_main_gameInfo_playerNum);
        dfg_main_gameInfo_roundNum = findViewById(R.id.dfg_main_gameInfo_roundNum);
        dfg_main_gameInfo_starNum = findViewById(R.id.dfg_main_gameInfo_starNum);
        dfg_main_gameInfo_worldTime = findViewById(R.id.dfg_main_gameInfo_worldTime);
        dfg_main_mine_active = findViewById(R.id.dfg_main_mine_active);
        dfg_main_mine_defenceValue = findViewById(R.id.dfg_main_mine_defenceValue);
        dfg_main_mine_informationValue = findViewById(R.id.dfg_main_mine_informationValue);
        dfg_main_mine_playerNum = findViewById(R.id.dfg_main_mine_playerNum);
        dfg_main_mine_star_info = findViewById(R.id.dfg_main_mine_star_info);
        dfg_main_mine_technologyValue = findViewById(R.id.dfg_main_mine_technologyValue);
        dfg_main_other_activeSum = findViewById(R.id.dfg_main_other_activeSum);
        dfg_main_other_ggSum = findViewById(R.id.dfg_main_other_ggSum);

    }

    private void initDate() {
        deepForestGameInfo = new DeepForestGameInfo();
        myStarInfo = deepForestGameInfo.getAllStarInfo().get(0);
        myCivilization = myStarInfo.getCivilizationInfo();
        teamInfo = DataUtil.getTeamInfo(DeepForestGameMainActivity.this);
    }

    private void setDate() {
        dfg_main_gameInfo_current_countDown.setText(deepForestGameInfo.getCountDown());//倒计时
        dfg_main_gameInfo_currentMoment.setText(deepForestGameInfo.getRoundIntroduce());
        dfg_main_gameInfo_currentMoment_info.setText(deepForestGameInfo.getRoundIntroduceInfo());
        dfg_main_gameInfo_playerNum.setText(deepForestGameInfo.getAllCivilizationInfo().size());
        dfg_main_gameInfo_roundNum.setText(deepForestGameInfo.getRound());
        dfg_main_gameInfo_starNum.setText(deepForestGameInfo.getAllStarInfo().size());
        dfg_main_gameInfo_worldTime.setText(deepForestGameInfo.getEra());

        dfg_main_mine_active.setText(myCivilization.getActive());
        dfg_main_mine_defenceValue.setText(myCivilization.getDefenceValue());
        dfg_main_mine_informationValue.setText(myCivilization.getOwnerMessage().size());
        dfg_main_mine_playerNum.setText(myCivilization.getNumber());
        dfg_main_mine_star_info.setText(myCivilization.getStarName());
        dfg_main_mine_technologyValue.setText(myCivilization.getTechnologyValue());

        dfg_main_other_activeSum.setText(deepForestGameInfo.getActiveSum());
        dfg_main_other_ggSum.setText(deepForestGameInfo.getDestroySum());
    }

    private void setToolBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void setRecyclerView() {
        dfg_main_note_part_textRcyclerView.findViewById(R.id.dfg_main_note_part_textRcyclerView);
        layoutManager1 = new LinearLayoutManager(DeepForestGameMainActivity.this);
        dfg_main_note_part_textRcyclerView.setLayoutManager(layoutManager1);
        dfgNoteAdapter = new DFGNoteAdapter(DeepForestGameMainActivity.this);
        dfg_main_note_part_textRcyclerView.setAdapter(dfgNoteAdapter);
        dfg_main_note_part_textRcyclerView.setHasFixedSize(true);
        dfg_main_note_part_textRcyclerView.addItemDecoration(new DividerItemDecoration(DeepForestGameMainActivity.this,
                DividerItemDecoration.VERTICAL_LIST));
        ((SimpleItemAnimator) dfg_main_note_part_textRcyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        dfg_main_other_info_recyclerView.findViewById(R.id.dfg_main_other_info_recyclerView);
        layoutManager2 = new LinearLayoutManager(DeepForestGameMainActivity.this);
        dfg_main_other_info_recyclerView.setLayoutManager(layoutManager2);
        dfgPlayerAdapter = new DFGPlayerAdapter(DeepForestGameMainActivity.this);
        dfg_main_other_info_recyclerView.setAdapter(dfgPlayerAdapter);
        dfg_main_other_info_recyclerView.setHasFixedSize(true);
        dfg_main_other_info_recyclerView.addItemDecoration(new DividerItemDecoration(DeepForestGameMainActivity.this,
                DividerItemDecoration.VERTICAL_LIST));
        ((SimpleItemAnimator) dfg_main_other_info_recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

    }

    private void setButton(){
        dfg_main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DeepForestGameMainActivity.this,DeepForestGameTacticsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_forest_game_main);
        setToolBar();
        initView();
//        initDate();
//        setDate();
        setRecyclerView();
//        reloadAdapter();
        timer.schedule(timerTask,1000,500);//延时1s，每隔500毫秒执行一次run方法
        setButton();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //    定时执行，每隔2S请求一次数据向服务器；
    private Integer num = 0;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                //do something
            }
            super.handleMessage(msg);
        }
    };
    Timer timer = new Timer();
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
            System.out.println(num++);
        }
    };

//    @SuppressLint("StaticFieldLeak")
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
//
//                String path = PropertiesConfig.teamServerUrl + "get/" + userId;
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
//                        reloadFlag = "doInBackground";
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
////                            ArrayList<StoreInfo> mStores = new ArrayList<>();
//                            switch (code) {
//                                case 0: //在数据库中更新用户数据出错；
//                                    ArrayList<TeamInfo> mTeam = new ArrayList();
//                                    for (Object m : responseJsonData) {
//                                        mTeam.add(JSONObject.parseObject(JSONObject.toJSONString(m), TeamInfo.class));
//                                    }
////                                    Log.e("responseJsonData",responseJsonData.toJSONString());
//                                    if (teamFragmentAdapter == null) {
//                                        teamFragmentAdapter = new TeamFragmentAdapter(mContext);
//                                    }
//                                    teamFragmentAdapter.updateResults(mTeam);
//
//                                    Log.e(TAG, "onResponse");
//                                    break;
//                            }
////                            swipeRefreshLayout.setRefreshing(false);
//                        } catch (Exception e) {
//                        }
//                        reloadFlag = "doInBackground";
//                    }
//
//                });
//                while (!reloadFlag.equals("doInBackground")) ;
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                Log.e(TAG, "onPostExecute");
//                if (mContext == null)
//                    return;
//                teamFragmentAdapter.notifyDataSetChanged();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        }.execute();
//    }


}
