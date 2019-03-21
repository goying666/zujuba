package renchaigao.com.zujuba.ActivityAndFragment.Main.Adapter;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonRecycleAdapter;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.MultiTypeSupport;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.PropertiesConfig;

/**
 * Created by Administrator on 2018/8/1/001.
 */

public class MainTeamFragmentAdapter extends CommonRecycleAdapter<JSONObject> implements MultiTypeSupport<JSONObject> {


    private CommonViewHolder.onItemCommonClickListener commonClickListener;
    private Context mContext;

    public MainTeamFragmentAdapter(Context context, ArrayList<JSONObject> dataList, int layoutId) {
        super(context, dataList, layoutId);
    }

    public MainTeamFragmentAdapter(Context context, ArrayList<JSONObject> dataList, CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.card_team_main);
        this.commonClickListener = commonClickListener;
        this.mContext = context;
    }


    @Override
    public void bindData(CommonViewHolder holder, JSONObject data) {
//        holder.setImageResource(R.id.team_image, R.drawable.thqby)
        holder.setGlideImageResource(R.id.team_image, PropertiesConfig.photoUrl + data.getString("imageurl"), mContext)
                .setText(R.id.teamName, data.getString("name"))
                .setText(R.id.teamState, data.getString("state"))
                .setText(R.id.teamPlaceName, data.getString("place"))
                .setRatingBar(R.id.place_star_num, data.getFloatValue("rating"))
                .setText(R.id.team_shop_info, data.getString("placeNote"))
                .setText(R.id.distance, data.getString("distance"))
                .setText(R.id.mainGame, data.getString("boynum") + "人")
                .setText(R.id.team_girl_number, data.getString("girlnum") + "人")
                .setText(R.id.playerNumber, data.getString("currentPlayer"))
                .setText(R.id.startDay, data.getString("date"))
                .setText(R.id.startTime, data.getString("time"))
                .setText(R.id.laveTime, data.getString("lave"))
                .setCommonClickListener(commonClickListener);
    }

    @Override
    public int getLayoutId(JSONObject item, int position) {
        return R.layout.card_team_main;
    }
}
