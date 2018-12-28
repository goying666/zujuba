package renchaigao.com.zujuba.Activity.TeamPart;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.renchaigao.zujuba.mongoDB.info.AddressInfo;
import com.renchaigao.zujuba.mongoDB.info.team.TeamInfo;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import renchaigao.com.zujuba.Activity.MessageInfoActivity;
import renchaigao.com.zujuba.Activity.PlaceListActivity;
import renchaigao.com.zujuba.Bean.FilterInfo;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.CalendarUtil;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.FinalDefine;
import renchaigao.com.zujuba.util.OkhttpFunc;
import renchaigao.com.zujuba.util.PatternUtil;
import renchaigao.com.zujuba.util.PropertiesConfig;
import renchaigao.com.zujuba.util.dateUse;
import renchaigao.com.zujuba.widgets.WidgetDateAndWeekSelect;


public class TeamCreateActivity extends AppCompatActivity {
    private NestedScrollView nestedScrollView;
    private Integer pageNum;
    private boolean click_id_activity_create_team_datetime_image_more,
            click_id_activity_create_team_time_image_more,
            click_create_team_address_more;
    private String TAG = "createTeam";
    private ImageView id_activity_create_team_datetime_image_more,
            id_activity_create_team_time_image_more,
            create_team_address_more_image,
            create_team_place_pic1, create_team_place_pic2, create_team_place_pic3;
    private TextView
            create_team_date_title,
            textview_creat_team_date,
            id_activity_create_team_time_value,
            create_team_address_name,
            id_activity_create_team_basic_information_textview_precent,
            id_activity_create_team_other_information_textview_precent,
            people_min_textview, people_max_textview, ct_people_selectinfo_textview_value_min, ct_people_selectinfo_textview_value_max;
    private LinearLayout
            id_activity_create_team_datetime,
            id_activity_create_team_people_number,
            id_activity_create_team_datetime_selectinfo,
            id_activity_create_team_time_selectinfo,
            id_activity_create_team_place,
            team_create_address_info_layout,
            ct_addres_info_layout;
    private Button create_team_cancle,
            create_team_next,
            create_team_open_place_button,
            create_team_store_button,
            button_create_team_cancle,
            button_create_team_next;
    private ConstraintLayout
            id_activity_create_team_game,
            id_activity_create_team_basic_information,
            id_activity_create_team_pwd,
            id_activity_create_team_join,
            id_activity_create_team_spend_style,
            id_activity_create_team_other_information,
            id_activity_create_team_datetime_con,
            id_activity_create_team_time_con,
            id_activity_create_team_time_selectinfo_morning,
            id_activity_create_team_time_selectinfo_afternoon,
            id_activity_create_team_time_selectinfo_evening,
            id_activity_create_team_time_selectinfo_night,
            create_team_address_constraintlayout,
            create_team_address_button_cons1,
            create_team_address_button_cons2;
    private WidgetDateAndWeekSelect
            today_widgetDateAndWeekSelect,
            tomorrow_widgetDateAndWeekSelect,
            aftertom_widgetDateAndWeekSelect,
            friday_widgetDateAndWeekSelect,
            saturday_widgetDateAndWeekSelect,
            sunday_widgetDateAndWeekSelect;
    private SeekBar people_min_select, people_max_select;
    private AppCompatSpinner create_team_game_class;

    private ConstraintLayout create_team_game_lrs, create_team_game_thqby;
    private TextView create_team_game_number_lrs, create_team_game_number_thqby, create_team_game_title;
    private AppCompatCheckBox create_team_game_CheckBox_lrs, create_team_game_CheckBox_thqby;

    private Integer gameNumber = 0;
    private TeamInfo teamInfo;
    private FilterInfo filterInfo;
    final static public int CREATE_TEAM_ADDRESS_STORE = 1;
    final static public int CREATE_TEAM_ADDRESS_OPEN_PLACE = 2;
    private UserInfo userInfo;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        initData();
        setToolBar();
        initDate();
        initTime();
        initPeople();
        setAllClickListener();
        initAddress();
        setGame();
        initExtraInfoPart();
        setButton();
//        Glide.with(TeamCreateActivity.this).load("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg").into(create_team_place_pic1);
    }

    //    初始化一些准备数据；
    private void initData() {
        userInfo = DataUtil.getUserInfoData(this);
        userId = userInfo.getId();
        teamInfo = new TeamInfo();
        teamInfo.setCreaterId(userId);
        teamInfo.setState("WAITING");
        teamInfo.setCreaterStyle("STORE");

        filterInfo = new FilterInfo();
//        date选项部分，点击后是否打开
        click_id_activity_create_team_datetime_image_more = true;
//        time选项部分，点击后是否打开
        click_id_activity_create_team_time_image_more = true;

        nestedScrollView = findViewById(R.id.ct_nestedScrollView);
    }

    //    初始化日期相关默认选项
    private void initDate() {

        create_team_date_title = findViewById(R.id.create_team_date_title);
        id_activity_create_team_datetime_con = findViewById(R.id.id_activity_create_team_datetime_con);
        id_activity_create_team_datetime_image_more = findViewById(R.id.id_activity_create_team_datetime_image_more);
        id_activity_create_team_datetime_selectinfo = findViewById(R.id.id_activity_create_team_datetime_selectinfo);
        id_activity_create_team_datetime_selectinfo.setVisibility(View.GONE);
//      默认设置今日日期
        textview_creat_team_date = findViewById(R.id.textview_creat_team_date);

//        实例化各选项
        today_widgetDateAndWeekSelect = new WidgetDateAndWeekSelect();
        tomorrow_widgetDateAndWeekSelect = new WidgetDateAndWeekSelect();
        aftertom_widgetDateAndWeekSelect = new WidgetDateAndWeekSelect();
        friday_widgetDateAndWeekSelect = new WidgetDateAndWeekSelect();
        saturday_widgetDateAndWeekSelect = new WidgetDateAndWeekSelect();
        sunday_widgetDateAndWeekSelect = new WidgetDateAndWeekSelect();
//        将后三天的总布局定位
        today_widgetDateAndWeekSelect.setConstraint((ConstraintLayout) findViewById(R.id.id_activity_create_team_date_selectinfo_today));
        tomorrow_widgetDateAndWeekSelect.setConstraint((ConstraintLayout) findViewById(R.id.id_activity_create_team_date_selectinfo_tomorrow));
        aftertom_widgetDateAndWeekSelect.setConstraint((ConstraintLayout) findViewById(R.id.id_activity_create_team_date_selectinfo_aftowmor));
        friday_widgetDateAndWeekSelect.setConstraint((ConstraintLayout) findViewById(R.id.id_activity_create_team_date_selectinfo_friday));
        saturday_widgetDateAndWeekSelect.setConstraint((ConstraintLayout) findViewById(R.id.id_activity_create_team_date_selectinfo_saturday));
        sunday_widgetDateAndWeekSelect.setConstraint((ConstraintLayout) findViewById(R.id.id_activity_create_team_date_selectinfo_sunday));
//        锁定各选项的date信息
        today_widgetDateAndWeekSelect.setDate_textView((TextView) findViewById(R.id.id_activity_create_team_date_selectinfo_today_date));
        tomorrow_widgetDateAndWeekSelect.setDate_textView((TextView) findViewById(R.id.id_activity_create_team_date_selectinfo_tomorrow_date));
        aftertom_widgetDateAndWeekSelect.setDate_textView((TextView) findViewById(R.id.id_activity_create_team_date_selectinfo_aftowmor_date));
        friday_widgetDateAndWeekSelect.setDate_textView((TextView) findViewById(R.id.id_activity_create_team_date_selectinfo_friday_date));
        saturday_widgetDateAndWeekSelect.setDate_textView((TextView) findViewById(R.id.id_activity_create_team_date_selectinfo_saturday_date));
        sunday_widgetDateAndWeekSelect.setDate_textView((TextView) findViewById(R.id.id_activity_create_team_date_selectinfo_sunday_date));
//        锁定各选项的week信息
        today_widgetDateAndWeekSelect.setWeek_textView((TextView) findViewById(R.id.id_activity_create_team_date_selectinfo_today_week));
        tomorrow_widgetDateAndWeekSelect.setWeek_textView((TextView) findViewById(R.id.id_activity_create_team_date_selectinfo_tomorrow_week));
        aftertom_widgetDateAndWeekSelect.setWeek_textView((TextView) findViewById(R.id.id_activity_create_team_date_selectinfo_aftowmor_week));
        friday_widgetDateAndWeekSelect.setWeek_textView((TextView) findViewById(R.id.id_activity_create_team_date_selectinfo_friday_week));
        saturday_widgetDateAndWeekSelect.setWeek_textView((TextView) findViewById(R.id.id_activity_create_team_date_selectinfo_saturday_week));
        sunday_widgetDateAndWeekSelect.setWeek_textView((TextView) findViewById(R.id.id_activity_create_team_date_selectinfo_sunday_week));

//        switch ("周六") {
        switch (CalendarUtil.getWeekOfDate(new Date())) {
            case "周一":
                today_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getCurrentDate());
                today_widgetDateAndWeekSelect.getWeek_textView().setText("周一");
                tomorrow_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getStringDateAfter(new Date(), 1));
                tomorrow_widgetDateAndWeekSelect.getWeek_textView().setText("周二");
                aftertom_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getStringDateAfter(new Date(), 2));
                aftertom_widgetDateAndWeekSelect.getWeek_textView().setText("周三");
                friday_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getStringDateAfter(new Date(), 4));
                friday_widgetDateAndWeekSelect.getWeek_textView().setText("周五");
                saturday_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getStringDateAfter(new Date(), 5));
                saturday_widgetDateAndWeekSelect.getWeek_textView().setText("周六");
                sunday_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getStringDateAfter(new Date(), 6));
                sunday_widgetDateAndWeekSelect.getWeek_textView().setText("周日");
                break;
            case "周二":
                today_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getCurrentDate());
                today_widgetDateAndWeekSelect.getWeek_textView().setText("周二");
                tomorrow_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getStringDateAfter(new Date(), 1));
                tomorrow_widgetDateAndWeekSelect.getWeek_textView().setText("周三");
                aftertom_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getStringDateAfter(new Date(), 2));
                aftertom_widgetDateAndWeekSelect.getWeek_textView().setText("周四");
                friday_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getStringDateAfter(new Date(), 3));
                friday_widgetDateAndWeekSelect.getWeek_textView().setText("周五");
                saturday_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getStringDateAfter(new Date(), 4));
                saturday_widgetDateAndWeekSelect.getWeek_textView().setText("周六");
                sunday_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getStringDateAfter(new Date(), 5));
                sunday_widgetDateAndWeekSelect.getWeek_textView().setText("周日");

                break;
            case "周三":
                today_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getCurrentDate());
                today_widgetDateAndWeekSelect.getWeek_textView().setText("周三");
                tomorrow_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getStringDateAfter(new Date(), 1));
                tomorrow_widgetDateAndWeekSelect.getWeek_textView().setText("周四");
                aftertom_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getStringDateAfter(new Date(), 2));
                aftertom_widgetDateAndWeekSelect.getWeek_textView().setText("周五");
                friday_widgetDateAndWeekSelect.getConstraint().setVisibility(View.GONE);
                saturday_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getStringDateAfter(new Date(), 3));
                saturday_widgetDateAndWeekSelect.getWeek_textView().setText("周六");
                sunday_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getStringDateAfter(new Date(), 4));
                sunday_widgetDateAndWeekSelect.getWeek_textView().setText("周日");
                break;
            case "周四":
                today_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getCurrentDate());
                today_widgetDateAndWeekSelect.getWeek_textView().setText("周四");
                tomorrow_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getStringDateAfter(new Date(), 1));
                tomorrow_widgetDateAndWeekSelect.getWeek_textView().setText("周五");
                aftertom_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getStringDateAfter(new Date(), 2));
                aftertom_widgetDateAndWeekSelect.getWeek_textView().setText("周六");
                friday_widgetDateAndWeekSelect.getConstraint().setVisibility(View.GONE);
                saturday_widgetDateAndWeekSelect.getConstraint().setVisibility(View.GONE);
                sunday_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getStringDateAfter(new Date(), 3));
                sunday_widgetDateAndWeekSelect.getWeek_textView().setText("周日");
                break;
            case "周五":
                today_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getCurrentDate());
                today_widgetDateAndWeekSelect.getWeek_textView().setText("周五");
                tomorrow_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getStringDateAfter(new Date(), 1));
                tomorrow_widgetDateAndWeekSelect.getWeek_textView().setText("周六");
                aftertom_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getStringDateAfter(new Date(), 2));
                aftertom_widgetDateAndWeekSelect.getWeek_textView().setText("周日");
                friday_widgetDateAndWeekSelect.getConstraint().setVisibility(View.GONE);
                saturday_widgetDateAndWeekSelect.getConstraint().setVisibility(View.GONE);
                sunday_widgetDateAndWeekSelect.getConstraint().setVisibility(View.GONE);
                break;
            case "周六":
                today_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getCurrentDate());
                today_widgetDateAndWeekSelect.getWeek_textView().setText("周六");
                tomorrow_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getStringDateAfter(new Date(), 1));
                tomorrow_widgetDateAndWeekSelect.getWeek_textView().setText("周日");
                aftertom_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getStringDateAfter(new Date(), 2));
                aftertom_widgetDateAndWeekSelect.getWeek_textView().setText("周一");
                friday_widgetDateAndWeekSelect.getConstraint().setVisibility(View.GONE);
                saturday_widgetDateAndWeekSelect.getConstraint().setVisibility(View.GONE);
                sunday_widgetDateAndWeekSelect.getConstraint().setVisibility(View.GONE);
                break;
            case "周日":
                today_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getCurrentDate());
                today_widgetDateAndWeekSelect.getWeek_textView().setText("周日");
                tomorrow_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getStringDateAfter(new Date(), 1));
                tomorrow_widgetDateAndWeekSelect.getWeek_textView().setText("周一");
                aftertom_widgetDateAndWeekSelect.getDate_textView().setText(CalendarUtil.getStringDateAfter(new Date(), 2));
                aftertom_widgetDateAndWeekSelect.getWeek_textView().setText("周二");
                friday_widgetDateAndWeekSelect.getConstraint().setVisibility(View.GONE);
                saturday_widgetDateAndWeekSelect.getConstraint().setVisibility(View.GONE);
                sunday_widgetDateAndWeekSelect.getConstraint().setVisibility(View.GONE);
                break;
        }
    }

    //    初始化时间段
    private void initTime() {
        id_activity_create_team_time_con = findViewById(R.id.id_activity_create_team_time_con);
        id_activity_create_team_time_selectinfo_morning = findViewById(R.id.id_activity_create_team_time_selectinfo_morning);
        id_activity_create_team_time_selectinfo_afternoon = findViewById(R.id.id_activity_create_team_time_selectinfo_afternoon);
        id_activity_create_team_time_selectinfo_evening = findViewById(R.id.id_activity_create_team_time_selectinfo_evening);
        id_activity_create_team_time_selectinfo_night = findViewById(R.id.id_activity_create_team_time_selectinfo_night);
        id_activity_create_team_time_selectinfo = findViewById(R.id.id_activity_create_team_time_selectinfo);
        id_activity_create_team_time_image_more = findViewById(R.id.id_activity_create_team_time_image_more);
        id_activity_create_team_time_value = findViewById(R.id.id_activity_create_team_time_value);
        id_activity_create_team_time_selectinfo.setVisibility(View.GONE);
        id_activity_create_team_time_value.setText("点击选择");
        id_activity_create_team_time_image_more.setImageResource(R.drawable.more_down);
    }

    //    初始化地点选项
    private void initAddress() {
        create_team_address_constraintlayout = findViewById(R.id.create_team_address_constraintlayout);
        team_create_address_info_layout = findViewById(R.id.team_create_address_info_layout);
        create_team_address_more_image = findViewById(R.id.create_team_address_more_image);
        create_team_open_place_button = findViewById(R.id.create_team_open_place_button);
        create_team_store_button = findViewById(R.id.create_team_store_button);
        create_team_address_name = findViewById(R.id.create_team_address_name);
        create_team_place_pic1 = findViewById(R.id.create_team_place_pic1);
        ct_addres_info_layout = findViewById(R.id.ct_addres_info_layout);
//        create_team_place_pic2 = findViewById(R.id.create_team_place_pic2);
//        create_team_place_pic3 = findViewById(R.id.create_team_place_pic3);
        create_team_address_button_cons1 = findViewById(R.id.create_team_address_button_cons1);
        create_team_address_button_cons2 = findViewById(R.id.create_team_address_button_cons2);
        create_team_address_constraintlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当该flag为true时，展示按键
                if (click_create_team_address_more) { //当该flag为true时，点击展示按键
                    create_team_address_button_cons1.setVisibility(View.VISIBLE);
                    create_team_address_button_cons2.setVisibility(View.VISIBLE);
                    ct_addres_info_layout.setVisibility(View.VISIBLE);
                    create_team_address_more_image.setImageResource(R.drawable.more_up);
//                    click_create_team_address_more = false;
                } else {
                    create_team_address_button_cons1.setVisibility(View.GONE);
                    create_team_address_button_cons2.setVisibility(View.GONE);
                    ct_addres_info_layout.setVisibility(View.GONE);
                    create_team_address_more_image.setImageResource(R.drawable.more_down);
//                    click_create_team_address_more = true;
                }
                click_create_team_address_more = !click_create_team_address_more;
            }
        });
        create_team_open_place_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(TeamCreateActivity.this, PlaceListActivity.class);
                startActivityForResult(intent, CREATE_TEAM_ADDRESS_OPEN_PLACE);
            }
        });
        create_team_store_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(TeamCreateActivity.this, PlaceListActivity.class);
                startActivityForResult(intent, CREATE_TEAM_ADDRESS_STORE);
            }
        });
    }

//    private SharedPreferences pref;
//    private String dataJsonString;
//    private JSONObject jsonObject;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CREATE_TEAM_ADDRESS_STORE:
//                pref = getSharedPreferences("createTeamAddressInfo", MODE_PRIVATE);
//                dataJsonString = pref.getString("responseJsonDataString", null);
//                jsonObject = JSONObject.parseObject(dataJsonString);

//                隐藏按键选项
                create_team_address_button_cons1.setVisibility(View.GONE);
                create_team_address_button_cons2.setVisibility(View.GONE);
                create_team_address_more_image.setImageResource(R.drawable.more_down);
                click_create_team_address_more = true;
//                获取json内对应的store信息

                create_team_address_name.setText(data.getStringExtra("name"));
                AddressInfo addressInfo = JSONObject.parseObject(data.getStringExtra("address"), AddressInfo.class);
                teamInfo.setAddressInfo(addressInfo);

                Glide
                        .with(TeamCreateActivity.this)
                        .load("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg")
                        .placeholder(R.drawable.image_loading)
                        .error(R.drawable.image_load_fail)
                        .into(create_team_place_pic1);
//                更新图片信息

                break;
            case CREATE_TEAM_ADDRESS_OPEN_PLACE:
                break;
        }
    }


    private void setToolBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    //    游戏部分设置
    private void setGame() {
        create_team_game_title = findViewById(R.id.create_team_game_title);
        create_team_game_lrs = findViewById(R.id.create_team_game_lrs);
        create_team_game_thqby = findViewById(R.id.create_team_game_thqby);
        create_team_game_number_lrs = findViewById(R.id.create_team_game_number_lrs);
        create_team_game_number_thqby = findViewById(R.id.create_team_game_number_thqby);
        id_activity_create_team_game = findViewById(R.id.id_activity_create_team_game);
        create_team_game_CheckBox_lrs = findViewById(R.id.create_team_game_CheckBox_lrs);
        create_team_game_CheckBox_thqby = findViewById(R.id.create_team_game_CheckBox_thqby);

        create_team_game_CheckBox_lrs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    gameNumber++;
                } else {
                    gameNumber--;
                }
                create_team_game_title.setText("已选：" + gameNumber.toString() + "款");
            }
        });
        create_team_game_CheckBox_thqby.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    gameNumber++;
                } else {
                    gameNumber--;
                }
                create_team_game_title.setText("已选：" + gameNumber.toString() + "款");
            }
        });
        create_team_game_lrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        create_team_game_thqby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//        create_team_game_class = findViewById(R.id.create_team_game_class);
//        create_team_game_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                team.setg(getResources().getStringArray(R.array.business_class_array)[position]);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }


    private void initPeople() {
        ct_people_selectinfo_textview_value_min = findViewById(R.id.ct_people_selectinfo_textview_value_min);
        ct_people_selectinfo_textview_value_max = findViewById(R.id.ct_people_selectinfo_textview_value_max);
        people_min_textview = findViewById(R.id.ct_people_selectinfo_textview_min_value);
        people_max_textview = findViewById(R.id.ct_people_selectinfo_textview_max_value);
        people_min_select = findViewById(R.id.ct_people_selectinfo_seekbar_min);
        people_max_select = findViewById(R.id.ct_people_selectinfo_seekbar_max);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            people_min_select.setMin(4);
            people_max_select.setMin(4);
        }
        people_min_select.setMax(9);
        people_max_select.setMax(19);
        people_min_select.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                people_min_textview.setText(Integer.toString(progress));
                if (Integer.valueOf(people_max_textview.getText().toString()) < progress) {
                    people_max_textview.setText(Integer.toString(progress));
                    ct_people_selectinfo_textview_value_max.setText(Integer.toString(progress));
                    people_max_select.setProgress(progress);
                }
                ct_people_selectinfo_textview_value_min.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("------------", "开始滑动！");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("------------", "停止滑动！");
            }
        });
        people_max_select.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                people_max_textview.setText(Integer.toString(progress));
                if (Integer.valueOf(people_min_textview.getText().toString()) > progress) {
                    people_min_textview.setText(Integer.toString(progress));
                    ct_people_selectinfo_textview_value_min.setText(Integer.toString(progress));
                    people_min_select.setProgress(progress);
                }
                ct_people_selectinfo_textview_value_max.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("------------", "开始滑动！");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("------------", "停止滑动！");
            }
        });
    }

    private void setAllClickListener() {
//        设置date选项区域折叠部分
        id_activity_create_team_datetime_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否要折叠选项部分
                if (click_id_activity_create_team_datetime_image_more) {
                    id_activity_create_team_datetime_image_more.setImageResource(R.drawable.more_up);
                    id_activity_create_team_datetime_selectinfo.setVisibility(View.VISIBLE);
                } else {
                    id_activity_create_team_datetime_image_more.setImageResource(R.drawable.more_down);
                    id_activity_create_team_datetime_selectinfo.setVisibility(View.GONE);
                }
                click_id_activity_create_team_datetime_image_more = !click_id_activity_create_team_datetime_image_more;
            }
        });

        id_activity_create_team_time_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click_id_activity_create_team_time_image_more) {
                    id_activity_create_team_time_image_more.setImageResource(R.drawable.more_up);
                    id_activity_create_team_time_selectinfo.setVisibility(View.VISIBLE);
                    Log.i(TAG, "click_id_activity_create_team_time_image_more  is true");
                } else {
                    id_activity_create_team_time_image_more.setImageResource(R.drawable.more_down);
                    id_activity_create_team_time_selectinfo.setVisibility(View.GONE);
                    Log.i(TAG, "click_id_activity_create_team_time_image_more  is false");
                }
                click_id_activity_create_team_time_image_more = !click_id_activity_create_team_time_image_more;
            }
        });
        id_activity_create_team_time_selectinfo_morning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_activity_create_team_time_value.setText(R.string.create_team_time_morning);
                id_activity_create_team_time_image_more.setImageResource(R.drawable.more_down);
                id_activity_create_team_time_selectinfo.setVisibility(View.GONE);
                click_id_activity_create_team_time_image_more = true;
                Toast.makeText(TeamCreateActivity.this, "时间设置成功：" + R.string.create_team_time_morning, Toast.LENGTH_SHORT).show();
            }
        });
        id_activity_create_team_time_selectinfo_afternoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_activity_create_team_time_value.setText(R.string.create_team_time_afternoon);
                id_activity_create_team_time_image_more.setImageResource(R.drawable.more_down);
                id_activity_create_team_time_selectinfo.setVisibility(View.GONE);
                click_id_activity_create_team_time_image_more = true;
                Toast.makeText(TeamCreateActivity.this, "时间设置成功：" + R.string.create_team_time_afternoon, Toast.LENGTH_SHORT).show();
            }
        });
        id_activity_create_team_time_selectinfo_evening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_activity_create_team_time_value.setText(R.string.create_team_time_evening);
                id_activity_create_team_time_image_more.setImageResource(R.drawable.more_down);
                id_activity_create_team_time_selectinfo.setVisibility(View.GONE);
                click_id_activity_create_team_time_image_more = true;
                Toast.makeText(TeamCreateActivity.this, "时间设置成功：" + R.string.create_team_time_evening, Toast.LENGTH_SHORT).show();
            }
        });
        id_activity_create_team_time_selectinfo_night.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_activity_create_team_time_value.setText(R.string.create_team_time_night);
                id_activity_create_team_time_image_more.setImageResource(R.drawable.more_down);
                id_activity_create_team_time_selectinfo.setVisibility(View.GONE);
                click_id_activity_create_team_time_image_more = true;
                Toast.makeText(TeamCreateActivity.this, "时间设置成功：" + R.string.create_team_time_night, Toast.LENGTH_SHORT).show();
            }
        });

//        date
        today_widgetDateAndWeekSelect.getConstraint().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textview_creat_team_date.setText(today_widgetDateAndWeekSelect.getDate_textView().getText());
                id_activity_create_team_datetime_image_more.setImageResource(R.drawable.more_down);
                id_activity_create_team_datetime_selectinfo.setVisibility(View.GONE);
                create_team_date_title.setText(today_widgetDateAndWeekSelect.getWeek_textView().getText());
                click_id_activity_create_team_datetime_image_more = true;
                Toast.makeText(TeamCreateActivity.this, "日期设置成功：" + today_widgetDateAndWeekSelect.getDate_textView().getText(), Toast.LENGTH_SHORT).show();
            }
        });
        tomorrow_widgetDateAndWeekSelect.getConstraint().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textview_creat_team_date.setText(tomorrow_widgetDateAndWeekSelect.getDate_textView().getText());
                id_activity_create_team_datetime_image_more.setImageResource(R.drawable.more_down);
                id_activity_create_team_datetime_selectinfo.setVisibility(View.GONE);
                create_team_date_title.setText(tomorrow_widgetDateAndWeekSelect.getWeek_textView().getText());
                click_id_activity_create_team_datetime_image_more = true;
                Toast.makeText(TeamCreateActivity.this, "日期设置成功：" + tomorrow_widgetDateAndWeekSelect.getDate_textView().getText(), Toast.LENGTH_SHORT).show();
            }
        });
        aftertom_widgetDateAndWeekSelect.getConstraint().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textview_creat_team_date.setText(aftertom_widgetDateAndWeekSelect.getDate_textView().getText());
                id_activity_create_team_datetime_image_more.setImageResource(R.drawable.more_down);
                id_activity_create_team_datetime_selectinfo.setVisibility(View.GONE);
                create_team_date_title.setText(aftertom_widgetDateAndWeekSelect.getWeek_textView().getText());
                click_id_activity_create_team_datetime_image_more = true;
                Toast.makeText(TeamCreateActivity.this, "日期设置成功：" + aftertom_widgetDateAndWeekSelect.getDate_textView().getText(), Toast.LENGTH_SHORT).show();
            }
        });
        friday_widgetDateAndWeekSelect.getConstraint().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textview_creat_team_date.setText(friday_widgetDateAndWeekSelect.getDate_textView().getText());
                id_activity_create_team_datetime_image_more.setImageResource(R.drawable.more_down);
                id_activity_create_team_datetime_selectinfo.setVisibility(View.GONE);
                create_team_date_title.setText(friday_widgetDateAndWeekSelect.getWeek_textView().getText());
                click_id_activity_create_team_datetime_image_more = true;
                Toast.makeText(TeamCreateActivity.this, "日期设置成功：" + friday_widgetDateAndWeekSelect.getDate_textView().getText(), Toast.LENGTH_SHORT).show();
            }
        });
        saturday_widgetDateAndWeekSelect.getConstraint().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textview_creat_team_date.setText(saturday_widgetDateAndWeekSelect.getDate_textView().getText());
                id_activity_create_team_datetime_image_more.setImageResource(R.drawable.more_down);
                id_activity_create_team_datetime_selectinfo.setVisibility(View.GONE);
                create_team_date_title.setText(saturday_widgetDateAndWeekSelect.getWeek_textView().getText());
                click_id_activity_create_team_datetime_image_more = true;
                Toast.makeText(TeamCreateActivity.this, "日期设置成功：" + saturday_widgetDateAndWeekSelect.getDate_textView().getText(), Toast.LENGTH_SHORT).show();
            }
        });
        sunday_widgetDateAndWeekSelect.getConstraint().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textview_creat_team_date.setText(sunday_widgetDateAndWeekSelect.getDate_textView().getText());
                id_activity_create_team_datetime_image_more.setImageResource(R.drawable.more_down);
                id_activity_create_team_datetime_selectinfo.setVisibility(View.GONE);
                create_team_date_title.setText(sunday_widgetDateAndWeekSelect.getWeek_textView().getText());
                click_id_activity_create_team_datetime_image_more = true;
                Toast.makeText(TeamCreateActivity.this, "日期设置成功：" + sunday_widgetDateAndWeekSelect.getDate_textView().getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initExtraInfoPart() {
        setSecretPart();
        setFilterPart();
        setSpendPart();
    }

    private TextView ct_pwd_title;
    private ConstraintLayout create_team_pwd_ConstraintLayout;
    private TextInputLayout create_team_pwd_TextInputLayout;
    private TextInputEditText create_team_pwd_TextInputEditText;
    private Switch create_team_pwd_switch;
    private boolean pwdFlag;

    private void setSecretPart() {
//        加密部分
        pwdFlag = false;
        ct_pwd_title = findViewById(R.id.ct_pwd_title);
        create_team_pwd_switch = findViewById(R.id.create_team_pwd_switch);
        create_team_pwd_ConstraintLayout = findViewById(R.id.create_team_pwd_ConstraintLayout);
        create_team_pwd_TextInputLayout = findViewById(R.id.create_team_pwd_TextInputLayout);
        create_team_pwd_TextInputEditText = findViewById(R.id.create_team_pwd_TextInputEditText);
        create_team_pwd_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    create_team_pwd_switch.setText("是");
                    create_team_pwd_ConstraintLayout.setVisibility(View.VISIBLE);
                } else {
                    create_team_pwd_switch.setText("否");
                    create_team_pwd_ConstraintLayout.setVisibility(View.GONE);
                    ct_pwd_title.setText("");
                }

            }
        });
        create_team_pwd_TextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 4) {
                    create_team_pwd_TextInputLayout.setError("请输入4位的数字密码");
                } else if (s.length() > 4) {
                    create_team_pwd_TextInputLayout.setError("密码已超过4位，请修改");
                } else {
                    create_team_pwd_TextInputLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (create_team_pwd_switch.isChecked() && s.length() == 4) {
                    pwdFlag = true;
                    ct_pwd_title.setText("加密成功");
                } else {
                    pwdFlag = false;
                    ct_pwd_title.setText("");
                }
            }
        });

    }

    private TextView ct_filter_title_text;
    private ImageView ct_filter_image_more;
    private Switch switch_join_team_1, switch_join_team_2, switch_join_team_3, switch_join_team_4, switch_join_team_5, switch_join_team_6;
    private AppCompatSpinner switch_join_team_spinner_1, switch_join_team_spinner_2, switch_join_team_spinner_3, switch_join_team_spinner_4, switch_join_team_spinner_5, switch_join_team_spinner_6;
    private Integer filterNum = 0;
    private LinearLayout filter_all_layout;
    private ConstraintLayout filter_all_ConstrainLayout;
    private boolean filterClickFlag;

    private void setFilterPart() {
        filter1 = null;
        filter2 = null;
        filter3 = null;
        filter4 = null;
        filter5 = null;
        filter6 = null;
        filterClickFlag = true;
        filter_all_ConstrainLayout = findViewById(R.id.filter_all_ConstrainLayout);
        filter_all_layout = findViewById(R.id.filter_all_layout);
        ct_filter_image_more = findViewById(R.id.ct_filter_image_more);
        ct_filter_title_text = findViewById(R.id.ct_filter_title_text);
        switch_join_team_1 = findViewById(R.id.switch_join_team_1);
        switch_join_team_2 = findViewById(R.id.switch_join_team_2);
        switch_join_team_3 = findViewById(R.id.switch_join_team_3);
        switch_join_team_4 = findViewById(R.id.switch_join_team_4);
        switch_join_team_5 = findViewById(R.id.switch_join_team_5);
        switch_join_team_6 = findViewById(R.id.switch_join_team_6);
        switch_join_team_spinner_1 = findViewById(R.id.switch_join_team_spinner_1);
        switch_join_team_spinner_2 = findViewById(R.id.switch_join_team_spinner_2);
        switch_join_team_spinner_3 = findViewById(R.id.switch_join_team_spinner_3);
        switch_join_team_spinner_4 = findViewById(R.id.switch_join_team_spinner_4);
        switch_join_team_spinner_5 = findViewById(R.id.switch_join_team_spinner_5);
        switch_join_team_spinner_6 = findViewById(R.id.switch_join_team_spinner_6);

        filter_all_ConstrainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterClickFlag) {
                    ct_filter_image_more.setImageResource(R.drawable.more_up);
                    filter_all_layout.setVisibility(View.VISIBLE);
                } else {
                    ct_filter_image_more.setImageResource(R.drawable.more_down);
                    filter_all_layout.setVisibility(View.GONE);
                }
                filterClickFlag = !filterClickFlag;
            }
        });
        switch_join_team_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    filterNum++;
                    switch_join_team_spinner_1.setVisibility(View.VISIBLE);
                    teamInfo.getTeamFilterInfo().setIntegrityScore(filter1);
                } else {
                    filterNum--;
                    switch_join_team_spinner_1.setVisibility(View.GONE);
                    teamInfo.getTeamFilterInfo().setIntegrityScore(null);
                }
                ct_filter_title_text.setText("已选" + filterNum.toString() + "项筛选");
            }
        });
        switch_join_team_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    filterNum++;
                    switch_join_team_spinner_2.setVisibility(View.VISIBLE);
                    teamInfo.getTeamFilterInfo().setSexRatio(filter2);
                } else {
                    filterNum--;
                    switch_join_team_spinner_2.setVisibility(View.GONE);
                    teamInfo.getTeamFilterInfo().setSexRatio(null);
                }

                ct_filter_title_text.setText("已选" + filterNum.toString() + "项筛选");
            }
        });
        switch_join_team_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    filterNum++;
                    switch_join_team_spinner_3.setVisibility(View.VISIBLE);
                    teamInfo.getTeamFilterInfo().setSexRatio(filter3);
                } else {
                    filterNum--;
                    switch_join_team_spinner_3.setVisibility(View.GONE);
                    teamInfo.getTeamFilterInfo().setSexRatio(null);
                }

                ct_filter_title_text.setText("已选" + filterNum.toString() + "项筛选");
            }
        });
        switch_join_team_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    filterNum++;
                    switch_join_team_spinner_4.setVisibility(View.VISIBLE);
                    teamInfo.getTeamFilterInfo().setAgeScreening(filter4);
                } else {
                    filterNum--;
                    switch_join_team_spinner_4.setVisibility(View.GONE);
                    teamInfo.getTeamFilterInfo().setAgeScreening(null);
                }

                ct_filter_title_text.setText("已选" + filterNum.toString() + "项筛选");
            }
        });
        switch_join_team_5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    filterNum++;
                    switch_join_team_spinner_5.setVisibility(View.VISIBLE);
                    teamInfo.getTeamFilterInfo().setEvaluationScreening(filter5);
                } else {
                    filterNum--;
                    switch_join_team_spinner_5.setVisibility(View.GONE);
                    teamInfo.getTeamFilterInfo().setEvaluationScreening(null);
                }

                ct_filter_title_text.setText("已选" + filterNum.toString() + "项筛选");
            }
        });
        switch_join_team_6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    filterNum++;
                    switch_join_team_spinner_6.setVisibility(View.VISIBLE);
                    teamInfo.getTeamFilterInfo().setMarriage(filter6);
                } else {
                    filterNum--;
                    switch_join_team_spinner_6.setVisibility(View.GONE);
                    teamInfo.getTeamFilterInfo().setMarriage(null);
                }

                ct_filter_title_text.setText("已选" + filterNum.toString() + "项筛选");
            }
        });
        switch_join_team_spinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter1 = getResources().getStringArray(R.array.compare_number_array)[position];
                teamInfo.getTeamFilterInfo().setIntegrityScore(filter1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filter1 = null;
                teamInfo.getTeamFilterInfo().setIntegrityScore(null);
            }
        });
        switch_join_team_spinner_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter2 = getResources().getStringArray(R.array.filter_gender)[position];
                teamInfo.getTeamFilterInfo().setIntegrityScore(filter2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filter2 = null;
                teamInfo.getTeamFilterInfo().setIntegrityScore(null);
            }
        });
        switch_join_team_spinner_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter3 = getResources().getStringArray(R.array.filter_job)[position];
                teamInfo.getTeamFilterInfo().setIntegrityScore(filter3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filter3 = null;
                teamInfo.getTeamFilterInfo().setIntegrityScore(null);
            }
        });
        switch_join_team_spinner_4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter4 = getResources().getStringArray(R.array.filter_age)[position];
                teamInfo.getTeamFilterInfo().setIntegrityScore(filter4);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filter4 = null;
                teamInfo.getTeamFilterInfo().setIntegrityScore(null);
            }
        });
        switch_join_team_spinner_5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter5 = getResources().getStringArray(R.array.filter_evaluation)[position];
                teamInfo.getTeamFilterInfo().setIntegrityScore(filter5);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filter5 = null;
                teamInfo.getTeamFilterInfo().setIntegrityScore(null);
            }
        });
        switch_join_team_spinner_6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter6 = getResources().getStringArray(R.array.filter_single)[position];
                teamInfo.getTeamFilterInfo().setIntegrityScore(filter6);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filter6 = null;
                teamInfo.getTeamFilterInfo().setIntegrityScore(null);
            }
        });

    }

    private String filter1, filter2, filter3, filter4, filter5, filter6;
    private AlertDialog.Builder builder;
    private ProgressDialog progDialog;

    private void setButton() {
        builder = new AlertDialog.Builder(TeamCreateActivity.this);
        progDialog = new ProgressDialog(TeamCreateActivity.this);
        button_create_team_cancle = findViewById(R.id.button_create_team_cancle);
        button_create_team_next = findViewById(R.id.button_create_team_next);
        button_create_team_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("取消创建");
                builder.setMessage("确定取消吗？");
                builder.setNegativeButton("否", null);
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.show();
            }
        });
        button_create_team_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkTeamInfo()) {
                    builder.setTitle("提交创建");
                    builder.setMessage("确定提交吗？");
                    builder.setNegativeButton("否", null);
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendCreateInfo();
                            progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progDialog.setIndeterminate(false);
                            progDialog.setCancelable(true);
                            progDialog.setMessage("正在加载...");
                        }
                    });
                    builder.show();
                }
            }
        });
    }

    private boolean checkTeamInfo() {
//        1、检查日期；
        if (textview_creat_team_date.getText().equals("点击选择")) {
            new AlertDialog.Builder(this)
                    .setTitle("信息不完整")
                    .setMessage("没有选择组队日期，请选择")
                    .setPositiveButton("确定", null)
                    .show();
            nestedScrollView.scrollTo(1, 1);
            return false;
        } else {
            teamInfo.setCreateTime(dateUse.getTodayDate());
            teamInfo.setStartDate(textview_creat_team_date.getText().toString());
        }
//        2、检查时间；
        if (id_activity_create_team_time_value.getText().equals("点击选择")) {
            new AlertDialog.Builder(this)
                    .setTitle("信息不完整")
                    .setMessage("没有选择组队时间段，请选择")
                    .setPositiveButton("确定", null)
                    .show();
            nestedScrollView.scrollTo(1, 1);
            return false;
        } else {
            teamInfo.setStartTime(id_activity_create_team_time_value.getText().toString());
        }
//        3、检查人数；
        if (Integer.valueOf(ct_people_selectinfo_textview_value_min.getText().toString()) > 2) {
            if (Integer.valueOf(ct_people_selectinfo_textview_value_max.getText().toString()) > 2) {
                teamInfo.setPlayerMin(Integer.valueOf(ct_people_selectinfo_textview_value_min.getText().toString()));
                teamInfo.setPlayerMax(Integer.valueOf(ct_people_selectinfo_textview_value_max.getText().toString()));
                teamInfo.setPlayerNow(1);
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("成员数量设置有误")
                        .setMessage("最大人数应大于2人")
                        .setPositiveButton("确定", null)
                        .show();
                nestedScrollView.scrollTo(1, 1);
                return false;
            }
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("成员数量设置有误")
                    .setMessage("最小人数应大于2人")
                    .setPositiveButton("确定", null)
                    .show();
            nestedScrollView.scrollTo(1, 1);
            return false;
        }
//        4、检查地点；

        if (create_team_address_name.getText().equals("点击选择")) {
            new AlertDialog.Builder(this)
                    .setTitle("信息不完整")
                    .setMessage("没有选择组队地点，请选择")
                    .setPositiveButton("确定", null)
                    .show();
            nestedScrollView.scrollTo(1, 1000);
            return false;
        } else {
        }
//        5、检查游戏；
        if (create_team_game_title.getText().equals("点击选择")) {
            new AlertDialog.Builder(this)
                    .setTitle("信息不完整")
                    .setMessage("没有选择游戏内容，请选择")
                    .setPositiveButton("确定", null)
                    .show();
            nestedScrollView.scrollTo(1, 1000);
            return false;
        } else {

        }
//        6、检查加密；
        if (create_team_pwd_switch.getText().equals("是") &&
                !PatternUtil.strMatcher(ct_pwd_title.getText().toString(), PatternUtil.FUNC_NUMBER_0000_9999)) {
            new AlertDialog.Builder(this)
                    .setTitle("密码设置有误")
                    .setMessage("密码设置有误，请重新设置")
                    .setPositiveButton("确定", null)
                    .show();
            nestedScrollView.scrollTo(1, 1500);
            return false;
        }
//        7、检查筛选信息；
//        8、检查消费方式；
        return true;
    }

    public void dismissDialog() {
        Log.e(TAG, "dismissDialog");
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void sendCreateInfo() {
        progDialog.show();
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
                String path = PropertiesConfig.teamServerUrl + "create";
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
                String stringBody = JSONObject.toJSONString(teamInfo);
                RequestBody jsonBody = RequestBody.create(FinalDefine.MEDIA_TYPE_JSON, stringBody);
                final Request request = new Request.Builder()
                        .url(path)
                        .header("Content-Type", "application/json")
                        .post(jsonBody)
                        .build();
                final Long startTimeMil = System.currentTimeMillis();
                builder.build().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("onFailure", e.toString());
                        Log.i(TAG + " callBack spend time : ", String.valueOf(System.currentTimeMillis() - startTimeMil));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(response.body().string());
//                            String responseJsoStr = responseJson.toJSONString();
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            JSONObject responseJsonData = responseJson.getJSONObject("data");
//                            JSONArray responseJsonData = responseJson.getJSONArray("data");
//                            Log.e(TAG, "onResponse CODE OUT");
//                            Log.e(TAG, "onResponse CODE is" + code);//
////                            ArrayList<StoreInfo> mStores = new ArrayList<>();
//                            Log.i(TAG + " callBack spend time : ", String.valueOf(System.currentTimeMillis() - startTimeMil));
                            switch (code) {
                                case 0: //在数据库中更新用户数据出错；
                                    Log.e(TAG, "onResponse");
                                    Intent intent = new Intent(TeamCreateActivity.this, MessageInfoActivity.class);
                                    intent.putExtra("teamInfo",JSONObject.toJSONString(responseJsonData));
                                    startActivity(intent);
                                    finish();
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
                dismissDialog();
                Log.e(TAG, "onPostExecute");
            }
        }.execute();
    }

    private AppCompatSpinner spinner_ct_spend;

    //
    private void setSpendPart() {
        spinner_ct_spend = findViewById(R.id.spinner_ct_spend);
        spinner_ct_spend.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterInfo.setIntegrityScore(getResources().getStringArray(R.array.compare_number_array)[position]);
//                teamInfo.setFilterInfo(filterInfo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
