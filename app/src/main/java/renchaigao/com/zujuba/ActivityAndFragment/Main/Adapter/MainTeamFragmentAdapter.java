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
        holder
//                .setText(R.id.TextView_mainGameTextView, data.getString("mainGame"))
                .setText(R.id.TextView_teamNameTextView, data.getString("name"))
                .setText(R.id.TextView_teamStateTextView, data.getString("state"))
                .setText(R.id.TextView_teamPlaceNameTextView, data.getString("place"))
                .setText(R.id.TextView_distanceTextView, data.getString("distance"))
                .setText(R.id.TextView_manTextView, data.getString("boynum") + "人")
                .setText(R.id.TextView_girlTextView, data.getString("girlnum") + "人")
                .setText(R.id.TextView_playerNumberTextView, data.getString("currentPlayer"))
                .setText(R.id.TextView_startDayTextView, data.getString("date"))
                .setText(R.id.TextView_startTimeTextView, data.getString("time"))
                .setText(R.id.TextView_laveTimeTextView, data.getString("lave"))
                .setCommonClickListener(commonClickListener);
    }

    @Override
    public int getLayoutId(JSONObject item, int position) {
        return R.layout.card_team_main;
    }
}
