package renchaigao.com.zujuba.ActivityAndFragment.User.Team;

import android.content.Context;

import com.renchaigao.zujuba.PageBean.CardUserTeamBean;

import java.util.ArrayList;

import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonRecycleAdapter;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.MultiTypeSupport;
import renchaigao.com.zujuba.R;

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
                .setText(R.id.TextView_teamNameTextView,data.getTeamName())
                .setText(R.id.TextView_teamStateTextView,data.getTeamState())
                .setText(R.id.TextView_teamPlaceNameTextView,data.getPlaceName())
                .setText(R.id.TextView_manTextView,data.getMainGame())
                .setText(R.id.TextView_distanceTextView,data.getDistance())
                .setText(R.id.TextView_playerNumberTextView,data.getPlayerNumber())
                .setText(R.id.TextView_startDayTextView,data.getStartDay())
                .setText(R.id.TextView_startTimeTextView,data.getStartTime())
                .setText(R.id.TextView_laveTimeTextView,data.getLaveTime())
                .setCommonClickListener(commonClickListener);
    }

    @Override
    public int getLayoutId(CardUserTeamBean item, int position) {
        return R.layout.card_user_team;
    }
}
