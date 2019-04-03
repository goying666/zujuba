package renchaigao.com.zujuba.ActivityAndFragment.Game.lrs;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import renchaigao.com.zujuba.ActivityAndFragment.BaseActivity;
import renchaigao.com.zujuba.R;

public class GameLRSMainActivity extends BaseActivity {
    LinearLayout LinearLayout_playerStatePart;
    @Override
    protected void InitView() {
        LayoutInflater inflater = LayoutInflater.from(GameLRSMainActivity.this);
        LinearLayout_playerStatePart = (LinearLayout) findViewById(R.id.LinearLayout_playerStatePart);
        ConstraintLayout user8 = (ConstraintLayout) inflater.inflate(R.layout.widget_user_12,null)
                .findViewById(R.id.ConstraintLayout_12players);
        LinearLayout_playerStatePart.addView(user8);

    }

    @Override
    protected void InitData() {

    }

    @Override
    protected void InitOther() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game_lrsmain;
    }
}
