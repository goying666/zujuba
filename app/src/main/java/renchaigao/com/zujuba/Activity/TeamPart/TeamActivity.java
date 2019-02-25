package renchaigao.com.zujuba.Activity.TeamPart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.renchaigao.zujuba.PageBean.TeamActivityBean;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import renchaigao.com.zujuba.Activity.Adapter.CardPlayerAdapter;
import renchaigao.com.zujuba.Activity.BaseActivity;
import renchaigao.com.zujuba.Activity.Message.MessageInfoActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.PropertiesConfig;
import renchaigao.com.zujuba.util.http.ApiService;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;
import renchaigao.com.zujuba.widgets.DividerItemDecoration;

import static renchaigao.com.zujuba.util.PropertiesConfig.ACTIVITY_MESSAGE_PAGE;
import static renchaigao.com.zujuba.util.PropertiesConfig.ACTIVITY_TEAM_PAGE;
import static renchaigao.com.zujuba.util.PropertiesConfig.FRAGMENT_TEAM_PAGE;

public class TeamActivity extends BaseActivity {

    private static String TAG = "TeamActivity";
    private TextView
            ti_user_position,
            ti_user_state,
            ti_text_null1,
            ti_text_distance,
            ti_store_score,
            ti_text_spend,
            ti_text_rank,
            ti_start_data,
            ti_start_time,
            ti_time_left,
            ti_player_number_now,
            boyNum,
            girlNum, ti_spend_way,
            ti_game_class, ti_filter_all_number, ti_filter_my_sum, ti_IntegrityScore, ti_IntegrityScore_result_info,
            ti_sexRatio, ti_sexRatio_result_info, ti_ageScreening, ti_ageScreening_result_info, ti_evaluationScreening,
            ti_evaluationScreening_result_info, ti_careerScreening, ti_careerScreening_result_info, ti_marriage,
            ti_marriage_result_info, ti_note_info;
    private TextView ti_player_number_all;
    private Button ti_join_button,ti_edit_button;
    private UserInfo userInfo = new UserInfo();
    private String userId, teamId;
    private TeamActivityBean teamActivityBean = new TeamActivityBean();
    private Timer timer = new Timer();
    private TimerTask task;
    private final static String RELOAD_FLAGE_VALUE_RELOAD = "RELOAD";
    private final static String RELOAD_FLAGE_VALUE_STOP = "STOP";
    private String reloadFlag = RELOAD_FLAGE_VALUE_RELOAD;
    private Integer reloadNUM = 0,reloadViewNUM = 0;
    private String whereCome;
    private int COME_FROM;
    private ImageView ti_store_image,ti_people_image_more;

    private CardPlayerAdapter cardPlayerAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == RespCodeNumber.SUCCESS && msg.arg2 == TEAMINFO_BASIC_LOAD) {
                UpdateViewAndData(teamActivityBean);
            }
        }
    };
    private void UpdateViewAndData(TeamActivityBean teamActivityBean) {
//        浏览者类型
        ti_user_position.setText(teamActivityBean.getUserClass());
//        队伍状态
        ti_user_state.setText(teamActivityBean.getTeamState());
//        地点图片url + 游戏图片
        Glide.with(this)
                .load(PropertiesConfig.photoUrl + teamActivityBean.getTeamPhotoUrlList().get(0))
                .dontAnimate()
                .skipMemoryCache(false)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_load_fail)
                .into(ti_store_image);
//        地点名称
        ti_text_null1.setText(teamActivityBean.getPlaceName());

//        距离我的距离
        ti_text_distance.setText(teamActivityBean.getDistance());

//        地点评分
        ti_store_score.setText(teamActivityBean.getPlaceScore());

//        地点名次
        ti_text_rank.setText(teamActivityBean.getPlaceRank());

//        组局日期
        ti_start_data.setText(teamActivityBean.getStartDate());

//        组局开始时间
        ti_start_time.setText(teamActivityBean.getStartTime() + "~" + teamActivityBean.getEndTime());

//        结束时间
//        ti_user_state.setText(teamActivityBean.getTeamState());

//        组局倒计时
        ti_time_left.setText(teamActivityBean.getTimeLeft());

//        已入局的玩家人数
        ti_player_number_now.setText(teamActivityBean.getAllPlayerNum());

//        已入局的男玩家人数
        boyNum.setText(teamActivityBean.getBoyPlayerNum());

//        已入局的女玩家人数
        girlNum.setText(teamActivityBean.getGirlPlayerNum());

//        组局最小人数
        ti_player_number_all.setText("/" + teamActivityBean.getMinPlayer() + "~" + teamActivityBean.getMaxPlayer());

//        加入按键
        if(teamActivityBean.getUserClass().equals("游客")){
            ti_join_button.setText("加入组局");
            ti_edit_button.setVisibility(View.GONE);
        }else {
            ti_join_button.setText("进入聊天室");
            ti_edit_button.setVisibility(View.VISIBLE);
        }
//        组局最大人数
//        ti_player_number_all.setText(teamActivityBean.getMaxPlayer());

//        玩家card的bean信息列表
        cardPlayerAdapter.updateResults(teamActivityBean.getPlayerList());
        cardPlayerAdapter.notifyDataSetChanged();
        reloadFlag = RELOAD_FLAGE_VALUE_RELOAD;
    }

    private void setRecyclerView() {
        layoutManager = new LinearLayoutManager(TeamActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        cardPlayerAdapter = new CardPlayerAdapter(this, teamActivityBean.getPlayerList());
        recyclerView.setAdapter(cardPlayerAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(TeamActivity.this, DividerItemDecoration.VERTICAL_LIST));
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    @Override
    protected void InitView() {
        recyclerView = findViewById(R.id.ti_RecyclerView_People);
        boyNum = findViewById(R.id.textView78);
        girlNum = findViewById(R.id.textView79);
        ti_player_number_now = findViewById(R.id.ti_player_number_now);
        ti_time_left = findViewById(R.id.ti_time_left);
        ti_store_image = findViewById(R.id.ti_store_image);
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
        ti_join_button = findViewById(R.id.ti_join_button);
        ti_edit_button = findViewById(R.id.ti_edit_button);
        setRecyclerView();
    }

    @Override
    protected void InitData() {
        teamActivityBean.setTeamId(getIntent().getStringExtra("teamId"));
        whereCome = getIntent().getStringExtra("whereCome");
        userInfo = DataUtil.GetUserInfoData(this);
    }

    @Override
    protected void InitOther() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_team;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVITY_MESSAGE_PAGE:
                COME_FROM = ACTIVITY_MESSAGE_PAGE;
                break;
            case FRAGMENT_TEAM_PAGE:
                COME_FROM = FRAGMENT_TEAM_PAGE;
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        InitTimer();
        timer = new Timer();
        timer.schedule(task, 0, 5000);
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

    private void InitTimer() {
        task = new TimerTask() {
            @Override
            public void run() {
                if (reloadFlag.equals(RELOAD_FLAGE_VALUE_RELOAD)) {
                    reloadAdapter();
                    reloadFlag = RELOAD_FLAGE_VALUE_STOP;
                }
            }
        };
    }

    public void SendMessage() {
        RetrofitServiceManager.getInstance().SetRetrofit(PropertiesConfig.teamServerUrl);
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody multiBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("json", "")
                .build();
        addSubscribe(RetrofitServiceManager.getInstance().creat(ApiService.class)
                .FourParameterBodyPost("join",
                        userInfo.getId(),
                        teamActivityBean.getTeamId(),
                        "null",
                        multiBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(TeamActivity.this) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            String responseJsonData = responseJson.getString("data");
                            Message msg = new Message();
                            switch (code) {
                                case RespCodeNumber.SUCCESS: //在数据库中更新用户数据出错；
                                    teamActivityBean = JSONObject.parseObject(responseJsonData, TeamActivityBean.class);
                                    msg.arg1 = RespCodeNumber.SUCCESS;
                                    msg.arg2 = TEAMINFO_BASIC_LOAD;
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

    private static final int TEAMINFO_BASIC_LOAD = 0;
    private static final int TEAM_PLAYER_INFO_LOAD = 1;

    public void reloadAdapter() {
        RetrofitServiceManager.getInstance().SetRetrofit(PropertiesConfig.teamServerUrl);
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody multiBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("json", "")
                .build();
        addSubscribe(RetrofitServiceManager.getInstance().creat(ApiService.class)
                .FourParameterBodyPost("getone",
                        userInfo.getId(),
                        teamActivityBean.getTeamId(),
                        "null",
                        multiBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(TeamActivity.this) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            String responseJsonData = responseJson.getString("data");
                            Message msg = new Message();
                            switch (code) {
                                case RespCodeNumber.SUCCESS: //在数据库中更新用户数据出错；
                                    teamActivityBean = JSONObject.parseObject(responseJsonData, TeamActivityBean.class);
                                    msg.arg1 = RespCodeNumber.SUCCESS;
                                    msg.arg2 = TEAMINFO_BASIC_LOAD;
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

    public void JoinTeamButton(View view) {
        if(teamActivityBean.getUserClass().equals("游客")){

        }else {
            Intent intent = new Intent(TeamActivity.this, MessageInfoActivity.class);
            intent.putExtra("teamId",teamActivityBean.getTeamId());
            intent.putExtra("teamName",teamActivityBean.getTeamName());
            startActivity(intent);
            finish();
        }
//                判断资格
//                资格不通过，提示错误信息
//                资格通过，进入聊天界面；
//               判断加入用户是否满足team的约束条件，若不满足则提示错误；
//                请求服务器，获取team最新信息；
        SendMessage();
//                修改“加入”按键文字；
//                显示申请加入的提示，直到返回正确的加入信息后，否则提示失败。
    }

    public void GoBack(View view) {
        onBackPressed();
    }

    public void EditTeamButton(View view) {

    }
}
