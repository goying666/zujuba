package renchaigao.com.zujuba.Activity.Adapter;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import renchaigao.com.zujuba.R;

/**
 * Created by Administrator on 2019/2/17/017.
 */

public class PlaceListAdapter extends CommonRecycleAdapter<JSONObject> implements MultiTypeSupport<JSONObject> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;

    public PlaceListAdapter(Context context, ArrayList<JSONObject> dataList, int layoutId) {
        super(context, dataList, layoutId);
    }

    public PlaceListAdapter(Context context, ArrayList<JSONObject> dataList, CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.card_hall_store);
        this.commonClickListener = commonClickListener;
    }

    @Override
    void bindData(CommonViewHolder holder, JSONObject data) {
        holder.setText(R.id.store_user_evaluate_1, "aa")
                .setCommonClickListener(commonClickListener);
    }

    @Override
    public int getLayoutId(JSONObject item, int position) {
        switch (item.getString("ADAPTER")) {
            case "a":
                return R.layout.card_place_list_page;
            case "b":
                return R.layout.card_hall_store;
            case "c":
                return R.layout.card_player_info;
        }
        return R.id.card_equip_effect;
    }
}
