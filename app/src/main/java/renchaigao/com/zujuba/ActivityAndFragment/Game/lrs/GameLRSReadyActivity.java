package renchaigao.com.zujuba.ActivityAndFragment.Game.lrs;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.PageBean.CardStoreClubFragmentBean;

import java.util.ArrayList;

import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonRecycleAdapter;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.BaseActivity;
import renchaigao.com.zujuba.ActivityAndFragment.Function.CallUsActivity;
import renchaigao.com.zujuba.R;

import static com.renchaigao.zujuba.PropertiesConfig.PlayerConstant.PLAYER_STATE_READY;
import static com.renchaigao.zujuba.PropertiesConfig.PlayerConstant.PLAYER_STATE_WAITING;

public class GameLRSReadyActivity extends BaseActivity {

    private TextView TextView_sumOfPeople;
    private TextView TextView_langren;
    private TextView TextView_bailangwang;
    private TextView TextView_yuyanjia;
    private TextView TextView_lieren;
    private TextView TextView_nvwu;
    private TextView TextView_pingmin;
    private TextView TextView_shousha;
    private TextView TextView_nvwuzijiu;
    private TextView TextView_huwei;
    private TextView TextView_lierenkaiqiang;
    private TextView TextView_ready;
    private TextView TextView_wait;
    private RecyclerView RecyclerView_recycler;
    private ConstraintLayout ConstraintLayout_lrstoolbal;
    private Button Button_left;
    private Button Button_right;

    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private PlayerAdapter playerAdapter;
    private ArrayList<JSONObject> jsonObjects = new ArrayList<>();

    private void initRecyclerView() {
        recyclerView = new RecyclerView(GameLRSReadyActivity.this);
        playerAdapter = new PlayerAdapter(GameLRSReadyActivity.this, jsonObjects);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(playerAdapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void InitView() {
        TextView_sumOfPeople = (TextView) findViewById(R.id.TextView_sumOfPeople);
        TextView_langren = (TextView) findViewById(R.id.TextView_langren);
        TextView_bailangwang = (TextView) findViewById(R.id.TextView_bailangwang);
        TextView_yuyanjia = (TextView) findViewById(R.id.TextView_yuyanjia);
        TextView_lieren = (TextView) findViewById(R.id.TextView_lieren);
        TextView_nvwu = (TextView) findViewById(R.id.TextView_nvwu);
        TextView_pingmin = (TextView) findViewById(R.id.TextView_pingmin);
        TextView_shousha = (TextView) findViewById(R.id.TextView_shousha);
        TextView_nvwuzijiu = (TextView) findViewById(R.id.TextView_nvwuzijiu);
        TextView_huwei = (TextView) findViewById(R.id.TextView_huwei);
        TextView_lierenkaiqiang = (TextView) findViewById(R.id.TextView_lierenkaiqiang);
        TextView_ready = (TextView) findViewById(R.id.TextView_ready);
        TextView_wait = (TextView) findViewById(R.id.TextView_wait);
        RecyclerView_recycler = (RecyclerView) findViewById(R.id.RecyclerView_recycler);
        ConstraintLayout_lrstoolbal = (ConstraintLayout) findViewById(R.id.ConstraintLayout_lrstoolbal);
        Button_left = (Button) findViewById(R.id.Button_left);
        Button_right = (Button) findViewById(R.id.Button_right);
        initRecyclerView();
    }

    @Override
    protected void InitData() {


    }

    @Override
    protected void InitOther() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game_lrsready;
    }

    private class PlayerAdapter extends CommonRecycleAdapter<JSONObject> {

        public PlayerAdapter(Context context, ArrayList<JSONObject> dataList) {
            super(context, dataList, R.layout.card_game_ready_player);
        }

        @Override
        public void bindData(CommonViewHolder holder, JSONObject data) {
            holder.setText(R.id.TextView_name, data.getString("name"));
            switch (data.getString("state")) {
                case PLAYER_STATE_READY:
                    holder.setImageResource(R.id.CircleImageView_state, Color.rgb(0x00, 0x66, 0x00));
                    break;
                case PLAYER_STATE_WAITING:
                    holder.setImageResource(R.id.CircleImageView_state, Color.rgb(0x66, 0x00, 0x00));
                    break;
            }
        }
    }
}
