package renchaigao.com.zujuba.ActivityAndFragment.Game.lrs;

import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.PageBean.StoreActivityBean;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.ActivityAndFragment.BaseActivity;
import renchaigao.com.zujuba.ActivityAndFragment.Store.StoreActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.GameApiService;
import renchaigao.com.zujuba.util.Api.StoreApiService;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;

public class GameLRSEditActivity extends BaseActivity {
    private TextView TextView_sumOfPlayer;
    private TextView TextView_whiteWolf;
    private TextView TextView_allWolfNumber;
    private TextView TextView_sumOfWolfman;
    private TextView TextView_lierenState;
    private TextView TextView_nvwuState;
    private TextView TextView_sumOfGoodRole;
    private TextView TextView_yuyanjiaState;
    private TextView TextView_huweiState;
    private TextView TextView_baichiState;
    private TextView TextView_cunmingState;
    private Switch Switch_whiteWlof;
    private Switch Switch_yuyanjia;
    private Switch Switch_nvwu;
    private Switch Switch_lieren;
    private Switch Switch_huwei;
    private Switch Switch_baichi;
    private ImageView ImageView_wolfJian;
    private ImageView ImageView_wolfAdd;
    private ImageView ImageView_cunmingJian;
    private ImageView ImageView_cunmingAdd;
    private ConstraintLayout toolbar;
    private Button Button_editSum;
    private Button Button_quicklyEdit;
    private Button Button_left;
    private Button Button_right;
    private TextView titleTextView, secondTitleTextView;


    @Override
    protected void InitView() {
        TextView_sumOfPlayer = (TextView) findViewById(R.id.TextView_sumOfPlayer);
        TextView_whiteWolf = (TextView) findViewById(R.id.TextView_whiteWolf);
        TextView_allWolfNumber = (TextView) findViewById(R.id.TextView_allWolfNumber);
        TextView_sumOfWolfman = (TextView) findViewById(R.id.TextView_sumOfWolfman);
        TextView_lierenState = (TextView) findViewById(R.id.TextView_lierenState);
        TextView_nvwuState = (TextView) findViewById(R.id.TextView_nvwuState);
        TextView_sumOfGoodRole = (TextView) findViewById(R.id.TextView_sumOfGoodRole);
        TextView_yuyanjiaState = (TextView) findViewById(R.id.TextView_yuyanjiaState);
        TextView_huweiState = (TextView) findViewById(R.id.TextView_huweiState);
        TextView_baichiState = (TextView) findViewById(R.id.TextView_baichiState);
        TextView_cunmingState = (TextView) findViewById(R.id.TextView_cunmingState);
        Switch_whiteWlof = (Switch) findViewById(R.id.Switch_whiteWlof);
        Switch_yuyanjia = (Switch) findViewById(R.id.Switch_yuyanjia);
        Switch_nvwu = (Switch) findViewById(R.id.Switch_nvwu);
        Switch_lieren = (Switch) findViewById(R.id.Switch_lieren);
        Switch_huwei = (Switch) findViewById(R.id.Switch_huwei);
        Switch_baichi = (Switch) findViewById(R.id.Switch_baichi);
        ImageView_wolfJian = (ImageView) findViewById(R.id.ImageView_wolfJian);
        ImageView_wolfAdd = (ImageView) findViewById(R.id.ImageView_wolfAdd);
        ImageView_cunmingJian = (ImageView) findViewById(R.id.ImageView_cunmingJian);
        ImageView_cunmingAdd = (ImageView) findViewById(R.id.ImageView_cunmingAdd);
        toolbar = (ConstraintLayout) findViewById(R.id.ConstraintLayout_toolbar);
        Button_editSum = (Button) findViewById(R.id.Button_editSum);
        Button_quicklyEdit = (Button) findViewById(R.id.Button_quicklyEdit);
        Button_left = (Button) findViewById(R.id.Button_left);
        Button_right = (Button) findViewById(R.id.Button_right);
        initToolbar();
    }

    private void initToolbar() {
        toolbar = (ConstraintLayout) findViewById(R.id.store_info_toolbar);
        titleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        secondTitleTextView = (TextView) toolbar.findViewById(R.id.toolbarSecondTitle);
        titleTextView.setText("游戏配置");
        secondTitleTextView.setVisibility(View.GONE);
        ImageView goback = (ImageView) toolbar.findViewById(R.id.toolbarBack);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void InitData() {

    }

    @Override
    protected void InitOther() {

    }

    @Override
    protected void UpdateView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game_lrsedit;
    }

}
