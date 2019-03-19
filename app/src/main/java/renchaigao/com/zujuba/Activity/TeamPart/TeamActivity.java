package renchaigao.com.zujuba.Activity.TeamPart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.PageBean.CardTeamNotesBean;
import com.renchaigao.zujuba.PageBean.TeamActivityBean;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import renchaigao.com.zujuba.Activity.BaseActivity;
import renchaigao.com.zujuba.Activity.Message.TeamMessageInfoActivity;
import renchaigao.com.zujuba.Activity.Place.StoreActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.TeamApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;

import static com.renchaigao.zujuba.PropertiesConfig.ConstantManagement.ADDRESS_CLASS_OPEN;
import static com.renchaigao.zujuba.PropertiesConfig.ConstantManagement.ADDRESS_CLASS_STORE;
import static com.renchaigao.zujuba.PropertiesConfig.ConstantManagement.ADDRESS_CLASS_USER;
import static com.renchaigao.zujuba.PropertiesConfig.ConstantManagement.TEAM_SEND_MESSAGE;
import static renchaigao.com.zujuba.util.PropertiesConfig.ACTIVITY_MESSAGE_PAGE;
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
            timeLeft,
            ti_player_number_now,
            boyNum,
            girlNum, ti_spend_way,
            ti_game_class, ti_filter_all_number, ti_filter_my_sum, ti_IntegrityScore, ti_IntegrityScore_result_info,
            ti_sexRatio, ti_sexRatio_result_info, ti_ageScreening, ti_ageScreening_result_info, ti_evaluationScreening,
            ti_evaluationScreening_result_info, ti_careerScreening, ti_careerScreening_result_info, ti_marriage,
            ti_marriage_result_info, ti_note_info;
    private TextView ti_player_number_all, playerMore;
    private Button ti_join_button, ti_edit_button;
    private UserInfo userInfo = new UserInfo();
    private String userId, teamId, token;
    private TeamActivityBean teamActivityBean = new TeamActivityBean();
    private final static String RELOAD_FLAGE_VALUE_RELOAD = "RELOAD";
    private final static String RELOAD_FLAGE_VALUE_STOP = "STOP";
    private String reloadFlag = RELOAD_FLAGE_VALUE_RELOAD;
    private Integer reloadNUM = 0, reloadViewNUM = 0;
    private String whereCome;
    private int COME_FROM;
    private ImageView ti_store_image, playerMoreImage;
    private CardPlayerAdapter cardPlayerAdapter;
    private CardTeamNoteAdapter cardTeamNoteAdapter;
    private RecyclerView recyclerView, notesRecyclerView;
    private Timer timer = new Timer();
    private TimerTask task;
    private NestedScrollView nesteScrollViewPeople;
    private ConstraintLayout addressCon;
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
//        图片更具主要游戏选择一个图片
        switch (teamActivityBean.getMainGame()) {
            case "LRS":
                ti_store_image.setImageResource(R.drawable.lrs);
                break;
            case "THQBY":
                ti_store_image.setImageResource(R.drawable.thqby);
                break;
            case "MXTSJ":
                ti_store_image.setImageResource(R.drawable.hasl);
                break;
        }
//        Glide.with(this)
//                .load(PropertiesConfig.photoUrl + teamActivityBean.getTeamPhotoUrlList().get(0))
//                .dontAnimate()
//                .skipMemoryCache(false)
//                .placeholder(R.drawable.image_loading)
//                .error(R.drawable.image_load_fail)
//                .into(ti_store_image);
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
        timeLeft.setText(teamActivityBean.getTimeLeft());

//        已入局的玩家人数
        ti_player_number_now.setText(teamActivityBean.getAllPlayerNum());

//        已入局的男玩家人数
        boyNum.setText(teamActivityBean.getBoyPlayerNum());

//        已入局的女玩家人数
        girlNum.setText(teamActivityBean.getGirlPlayerNum());

//        组局最小人数
        ti_player_number_all.setText("/" + teamActivityBean.getMinPlayer() + "~" + teamActivityBean.getMaxPlayer());

//        加入按键
        if (teamActivityBean.getUserClass().equals("游客")) {
            ti_join_button.setText("加入组局");
            ti_edit_button.setVisibility(View.GONE);
        } else {
            ti_join_button.setText("进入聊天室");
            ti_edit_button.setVisibility(View.VISIBLE);
        }
//        组局最大人数
//        ti_player_number_all.setText(teamActivityBean.getMaxPlayer());

//        玩家card的bean信息列表
        cardPlayerAdapter.updateResults(teamActivityBean.getPlayerList());
        cardPlayerAdapter.notifyDataSetChanged();
        cardTeamNoteAdapter.updateResults(teamActivityBean.getCardTeamNotesBeans());
        cardTeamNoteAdapter.notifyDataSetChanged();
        titleTextView.setText(teamActivityBean.getTeamName());
        secondTitleTextView.setText(teamActivityBean.getUserClass());
        reloadFlag = RELOAD_FLAGE_VALUE_RELOAD;
    }

    private ArrayList<CardTeamNotesBean> cardTeamNotesBeanArrayList = new ArrayList<>();

    private void setRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.ti_RecyclerView_People);
        LinearLayoutManager layoutManager = new LinearLayoutManager(TeamActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        cardPlayerAdapter = new CardPlayerAdapter(this, teamActivityBean.getPlayerList());
        recyclerView.setAdapter(cardPlayerAdapter);
        recyclerView.setHasFixedSize(true);

        notesRecyclerView = (RecyclerView) findViewById(R.id.team_info_notes_recyclerView);
        LinearLayoutManager noteslayoutManager = new LinearLayoutManager(TeamActivity.this);
        notesRecyclerView.setLayoutManager(noteslayoutManager);
        cardTeamNoteAdapter = new CardTeamNoteAdapter(this, cardTeamNotesBeanArrayList);
        notesRecyclerView.setAdapter(cardTeamNoteAdapter);
        notesRecyclerView.setHasFixedSize(true);
//        recyclerView.addItemDecoration(new DividerItemDecoration(TeamActivity.this, DividerItemDecoration.VERTICAL_LIST));
//        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    Boolean playerVisibale = true;

    private void setPlayerMorePart() {
        playerMore = (TextView) findViewById(R.id.ti_people_null1);
        playerMoreImage = (ImageView) findViewById(R.id.ti_people_image_more);
        nesteScrollViewPeople = (NestedScrollView) findViewById(R.id.ti_nesteScrollView_People);
        playerMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerVisibale) {
                    playerMoreImage.setImageResource(R.drawable.more_down);
                    nesteScrollViewPeople.setVisibility(View.GONE);
                    playerVisibale = false;
                } else {
                    playerMoreImage.setImageResource(R.drawable.more_up);
                    nesteScrollViewPeople.setVisibility(View.VISIBLE);
                    playerVisibale = true;
                }
            }
        });
    }

    @Override
    protected void InitView() {
        addressCon = (ConstraintLayout) findViewById(R.id.team_info_addressConstraintLayout);
        boyNum = (TextView) findViewById(R.id.textView78);
        girlNum = (TextView) findViewById(R.id.textView79);
        ti_player_number_now = (TextView) findViewById(R.id.ti_player_number_now);
        timeLeft = (TextView) findViewById(R.id.ti_time_left);
        ti_store_image = (ImageView) findViewById(R.id.ti_store_image);
        ti_user_position = (TextView) findViewById(R.id.ti_user_position);
        ti_user_state = (TextView) findViewById(R.id.ti_user_state);
        ti_text_null1 = (TextView) findViewById(R.id.ti_text_null1);
        ti_text_distance = (TextView) findViewById(R.id.ti_text_distance);
        ti_store_score = (TextView) findViewById(R.id.ti_store_score);
        ti_text_spend = (TextView) findViewById(R.id.ti_text_spend);
        ti_text_rank = (TextView) findViewById(R.id.ti_text_rank);
        ti_start_data = (TextView) findViewById(R.id.ti_start_data);
        ti_start_time = (TextView) findViewById(R.id.ti_start_time);
        ti_spend_way = (TextView) findViewById(R.id.ti_spend_way);
        ti_game_class = (TextView) findViewById(R.id.ti_game_class);
        ti_filter_all_number = (TextView) findViewById(R.id.ti_filter_all_number);
        ti_filter_my_sum = (TextView) findViewById(R.id.ti_filter_my_sum);
        ti_IntegrityScore = (TextView) findViewById(R.id.ti_IntegrityScore);
        ti_IntegrityScore_result_info = (TextView) findViewById(R.id.ti_IntegrityScore_result_info);
        ti_sexRatio = (TextView) findViewById(R.id.ti_sexRatio);
        ti_sexRatio_result_info = (TextView) findViewById(R.id.ti_sexRatio_result_info);
        ti_ageScreening = (TextView) findViewById(R.id.ti_ageScreening);
        ti_ageScreening_result_info = (TextView) findViewById(R.id.ti_ageScreening_result_info);
        ti_evaluationScreening = (TextView) findViewById(R.id.ti_evaluationScreening);
        ti_evaluationScreening_result_info = (TextView) findViewById(R.id.ti_evaluationScreening_result_info);
        ti_careerScreening = (TextView) findViewById(R.id.ti_careerScreening);
        ti_careerScreening_result_info = (TextView) findViewById(R.id.ti_careerScreening_result_info);
        ti_marriage = (TextView) findViewById(R.id.ti_marriage);
        ti_marriage_result_info = (TextView) findViewById(R.id.ti_marriage_result_info);
        ti_note_info = (TextView) findViewById(R.id.ti_note_info);
        ti_player_number_all = (TextView) findViewById(R.id.ti_player_number_all);
        ti_join_button = (Button) findViewById(R.id.ti_join_button);
        ti_edit_button = (Button) findViewById(R.id.ti_edit_button);
        setRecyclerView();
    }

    @Override
    protected void InitData() {
        teamActivityBean.setTeamId(getIntent().getStringExtra("teamId"));
        whereCome = getIntent().getStringExtra("whereCome");
        userInfo = DataUtil.GetUserInfoData(this);
        userId = userInfo.getId();
        teamId = getIntent().getStringExtra("teamId");
        token = userInfo.getToken();

    }

    private TextView titleTextView, secondTitleTextView;

    private void initToolbar() {
        ConstraintLayout toolbar = (ConstraintLayout) findViewById(R.id.team_info_toolbar);
        titleTextView = (TextView) toolbar.findViewById(R.id.textView146);
        secondTitleTextView = (TextView) toolbar.findViewById(R.id.textView147);
        ImageView goback = (ImageView) toolbar.findViewById(R.id.imageView33);
        titleTextView.setText(teamActivityBean.getTeamName());
        secondTitleTextView.setText("成员");
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void InitOther() {
        initToolbar();
        setPlayerMorePart();
        addressCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                switch (teamActivityBean.getPlaceClass()) {
                    case ADDRESS_CLASS_STORE:
                        intent = new Intent(TeamActivity.this, StoreActivity.class);
                        break;
                    case ADDRESS_CLASS_OPEN:
                        break;
                    case ADDRESS_CLASS_USER:
                        break;
                }
                intent.putExtra("placeId",teamActivityBean.getPlaceId());
                startActivity(intent);
            }
        });
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

    public void SendJoinMessage() {
        addSubscribe(RetrofitServiceManager.getInstance().creat(TeamApiService.class)
                .JoinTeam(userId, teamId, token)
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
        RequestBody multiBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("json", "")
                .build();
        addSubscribe(RetrofitServiceManager.getInstance().creat(TeamApiService.class)
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

        if (teamActivityBean.getUserClass().equals("游客")) {
//            发送加入申请，等待系统判断资格；
            SendJoinMessage();
//                判断资格
//                资格不通过，提示错误信息
//                资格通过，进入聊天界面；
//               判断加入用户是否满足team的约束条件，若不满足则提示错误；
//                请求服务器，获取team最新信息；
//                修改“加入”按键文字；
//                显示申请加入的提示，直到返回正确的加入信息后，否则提示失败。
        } else {
            Intent intent = new Intent(TeamActivity.this, TeamMessageInfoActivity.class);
            intent.putExtra("messageClass", TEAM_SEND_MESSAGE);
            intent.putExtra("ownerId", teamActivityBean.getTeamId());
            intent.putExtra("teamId", teamActivityBean.getTeamId());
            intent.putExtra("title", teamActivityBean.getTeamName());
            startActivity(intent);
            finish();
        }
    }

    public void GoBack(View view) {
        onBackPressed();
    }

    public void EditTeamButton(View view) {

    }
}
