package renchaigao.com.zujuba.Activity.Adapter;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.PageBean.CardPlayerInfoBean;

import java.util.ArrayList;

import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.PropertiesConfig;

/**
 * Created by Administrator on 2019/2/17/017.
 */

public class CardPlayerAdapter extends CommonRecycleAdapter<CardPlayerInfoBean> {

    private Context mContext;
    private CommonViewHolder.onItemCommonClickListener commonClickListener;

    public CardPlayerAdapter(Context context, ArrayList<CardPlayerInfoBean> dataList) {
        super(context, dataList, R.layout.card_player_info);
        this.mContext = context;
    }


    public CardPlayerAdapter(Context context, ArrayList<CardPlayerInfoBean> dataList,
                                    CommonViewHolder.onItemCommonClickListener commonClickListener, int layoutId) {
        super(context, dataList, R.layout.card_player_info);
        this.commonClickListener = commonClickListener;
        this.mContext = context;
    }

    @Override
    public void bindData(CommonViewHolder holder, CardPlayerInfoBean data) {
        holder.setGlideImageResource(R.id.card_player_image, PropertiesConfig.photoUrl + data.getImageUrl(),mContext)
                .setText(R.id.card_player_name, data.getNickName())
                .setText(R.id.card_player_state, data.getTeamState())
                .setText(R.id.card_player_zu_number, data.getTeamTimes())
                .setText(R.id.card_player_you_number, data.getGameTimes())
//                .setText(R.id.card_player_name, data.getNickName())

                .setText(R.id.card_player_rank, data.getUserNote())
                .setCommonClickListener(commonClickListener);
    }
}
