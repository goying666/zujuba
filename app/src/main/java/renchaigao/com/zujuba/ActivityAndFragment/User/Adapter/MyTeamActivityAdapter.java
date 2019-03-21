package renchaigao.com.zujuba.ActivityAndFragment.User.Adapter;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.PageBean.CardUserTeamBean;

import java.util.ArrayList;

import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonRecycleAdapter;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.MultiTypeSupport;
import renchaigao.com.zujuba.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2019/2/17/017.
 */

public class MyTeamActivityAdapter extends CommonRecycleAdapter<CardUserTeamBean> implements MultiTypeSupport<CardUserTeamBean> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;

    private Context mContext;

    public MyTeamActivityAdapter(Context context, ArrayList<CardUserTeamBean> dataList, CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.card_user_team);
        this.commonClickListener = commonClickListener;
        this.mContext = context;
    }

    @Override
    public void bindData(CommonViewHolder holder, CardUserTeamBean data) {
        holder
                .setText(R.id.teamName,data.getTeamName())
                .setText(R.id.teamState,data.getTeamState())
                .setText(R.id.teamPlaceName,data.getPlaceName())
                .setText(R.id.mainGame,data.getMainGame())
                .setText(R.id.distance,data.getDistance())
                .setText(R.id.playerNumber,data.getPlayerNumber())
                .setText(R.id.startDay,data.getStartDay())
                .setText(R.id.startTime,data.getStartTime())
                .setText(R.id.laveTime,data.getLaveTime())
                .setCommonClickListener(commonClickListener);
    }

    @Override
    public int getLayoutId(CardUserTeamBean item, int position) {
        return R.layout.card_user_team;
    }
}
