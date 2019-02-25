package renchaigao.com.zujuba.Activity.Adapter;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

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
        super(context, dataList, R.layout.card_place_page);
        this.commonClickListener = commonClickListener;
    }

    @Override
    public void bindData(CommonViewHolder holder, JSONObject data) {
        holder.setText(R.id.store_user_evaluate_1, "aa")
                .setCommonClickListener(commonClickListener);
    }

    @Override
    public int getLayoutId(JSONObject item, int position) {
        return R.layout.card_place_page;
    }
}
