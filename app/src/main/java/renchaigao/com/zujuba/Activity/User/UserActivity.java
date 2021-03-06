package renchaigao.com.zujuba.Activity.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import renchaigao.com.zujuba.R;

public class UserActivity extends AppCompatActivity {

    private Button activity_user_other_toolbar_edit;
    private TextView activity_user_other_textview_zuju_title, activity_user_other_textview_title_dianzan,
            activity_user_other_textview_youxi_title, activity_user_other_textview_youxi_title_wanguo,
            activity_user_other_changdi_title, activity_user_other_changdi_title_playTimes,
            activity_user_other_textview_zuju_create, activity_user_other_textview_zuju_join,
            activity_user_other_textview_youxi_own, activity_user_other_textview_youxi_play, activity_user_other_textview_youxi_want,
            activity_user_other_changdi_title_own, activity_user_other_changdi_title_play;
    private TextView
            activity_user_other_textview_zuju_create_1, activity_user_other_textview_zuju_join_1,
            activity_user_other_textview_youxi_own_1, activity_user_other_textview_youxi_play_1, activity_user_other_textview_youxi_want_1,
            activity_user_other_changdi_title_own_1, activity_user_other_changdi_title_play_1,
            activity_user_other_textview_zuju_create_2, activity_user_other_textview_zuju_join_2,
            activity_user_other_textview_youxi_own_2, activity_user_other_textview_youxi_play_2, activity_user_other_textview_youxi_want_2,
            activity_user_other_changdi_title_own_2, activity_user_other_changdi_title_play_2;

    private void initButton() {
        activity_user_other_toolbar_edit = findViewById(R.id.activity_user_other_toolbar_edit);
        activity_user_other_toolbar_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, UserSettingActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("CutPasteId")
    private void InitView() {

        activity_user_other_textview_zuju_title = findViewById(R.id.activity_user_other_textview_zuju_title);
        activity_user_other_textview_title_dianzan = findViewById(R.id.activity_user_other_textview_title_dianzan);
        activity_user_other_textview_youxi_title = findViewById(R.id.activity_user_other_textview_youxi_title);
        activity_user_other_textview_youxi_title_wanguo = findViewById(R.id.activity_user_other_textview_youxi_title_wanguo);
        activity_user_other_changdi_title = findViewById(R.id.activity_user_other_changdi_title);
        activity_user_other_changdi_title_playTimes = findViewById(R.id.activity_user_other_changdi_title_playTimes);
        activity_user_other_textview_zuju_title.setText("组局（1）");
        activity_user_other_textview_title_dianzan.setText("点赞：5");
        activity_user_other_textview_youxi_title.setText("游戏（5）");
        activity_user_other_textview_youxi_title_wanguo.setText("玩过：15盘");
        activity_user_other_changdi_title.setText("场地（1）");
        activity_user_other_changdi_title_playTimes.setText("去过：5次");

        activity_user_other_textview_zuju_create_1 = findViewById(R.id.activity_user_other_textview_zuju_create).findViewById(R.id.widget_user_content_TextView_name);
        activity_user_other_textview_zuju_join_1 = findViewById(R.id.activity_user_other_textview_zuju_join).findViewById(R.id.widget_user_content_TextView_name);
        activity_user_other_textview_youxi_own_1 = findViewById(R.id.activity_user_other_textview_youxi_own).findViewById(R.id.widget_user_content_TextView_name);
        activity_user_other_textview_youxi_play_1 = findViewById(R.id.activity_user_other_textview_youxi_play).findViewById(R.id.widget_user_content_TextView_name);
        activity_user_other_textview_youxi_want_1 = findViewById(R.id.activity_user_other_textview_youxi_want).findViewById(R.id.widget_user_content_TextView_name);
        activity_user_other_changdi_title_own_1 = findViewById(R.id.activity_user_other_textview_changdi_own).findViewById(R.id.widget_user_content_TextView_name);
        activity_user_other_changdi_title_play_1 = findViewById(R.id.activity_user_other_textview_changdi_play).findViewById(R.id.widget_user_content_TextView_name);
        activity_user_other_textview_zuju_create_2 = findViewById(R.id.activity_user_other_textview_zuju_create).findViewById(R.id.widget_user_content_TextView_info);
        activity_user_other_textview_zuju_join_2 = findViewById(R.id.activity_user_other_textview_zuju_join).findViewById(R.id.widget_user_content_TextView_info);
        activity_user_other_textview_youxi_own_2 = findViewById(R.id.activity_user_other_textview_youxi_own).findViewById(R.id.widget_user_content_TextView_info);
        activity_user_other_textview_youxi_play_2 = findViewById(R.id.activity_user_other_textview_youxi_play).findViewById(R.id.widget_user_content_TextView_info);
        activity_user_other_textview_youxi_want_2 = findViewById(R.id.activity_user_other_textview_youxi_want).findViewById(R.id.widget_user_content_TextView_info);
        activity_user_other_changdi_title_own_2 = findViewById(R.id.activity_user_other_textview_changdi_own).findViewById(R.id.widget_user_content_TextView_info);
        activity_user_other_changdi_title_play_2 = findViewById(R.id.activity_user_other_textview_changdi_play).findViewById(R.id.widget_user_content_TextView_info);

        activity_user_other_textview_zuju_create_1.setText("创建的组局");
        activity_user_other_textview_zuju_join_1.setText("加入的组局");
        activity_user_other_textview_youxi_own_1.setText("拥有的游戏");
        activity_user_other_textview_youxi_play_1.setText("玩过的游戏");
        activity_user_other_textview_youxi_want_1.setText("想要的游戏");
        activity_user_other_changdi_title_own_1.setText("拥有的场地");
        activity_user_other_changdi_title_play_1.setText("玩过的场地");
        activity_user_other_textview_zuju_create_2.setText("0次，累计0人参与");
        activity_user_other_textview_zuju_join_2.setText("1次，5人点赞");
        activity_user_other_textview_youxi_own_2.setText("1款，和12位朋友玩过");
        activity_user_other_textview_youxi_play_2.setText("2款，和19位朋友玩过");
        activity_user_other_textview_youxi_want_2.setText("2款，和11位朋友玩过");
        activity_user_other_changdi_title_own_2.setText("0个，0人玩过");
        activity_user_other_changdi_title_play_2.setText("1个，1900人玩过");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        initButton();
        InitView();
    }
}
