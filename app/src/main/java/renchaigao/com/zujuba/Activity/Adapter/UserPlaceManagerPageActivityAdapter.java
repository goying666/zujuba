package renchaigao.com.zujuba.Activity.Adapter;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import renchaigao.com.zujuba.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2019/2/17/017.
 */

public class UserPlaceManagerPageActivityAdapter extends CommonRecycleAdapter<JSONObject> implements MultiTypeSupport<JSONObject> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;

    private Context mContext;

    public UserPlaceManagerPageActivityAdapter(Context context, ArrayList<JSONObject> dataList, int layoutId) {
        super(context, dataList, layoutId);
        this.mContext = context;
    }

    public UserPlaceManagerPageActivityAdapter(Context context, ArrayList<JSONObject> dataList, CommonViewHolder.onItemCommonClickListener commonClickListener, int layoutId) {
        super(context, dataList, layoutId);
        this.commonClickListener = commonClickListener;
        this.mContext = context;
    }

    @Override
    public void bindData(CommonViewHolder holder, JSONObject data) {
        switch (data.getString("WHICHCARD")) {
            case "BASIC_DESK":
                holder.setGlideImageResource(R.id.imageView23, data.getString("imageUrl"), mContext)
                        .setText(R.id.textView104, data.getInteger("maxPeopleSum").toString())
                        .setText(R.id.textView106, data.getInteger("minPeopleNum").toString())
                        .setText(R.id.textView102, "第" + data.getInteger("deskNumber").toString() + "号桌")
                        .setViewVisibility(R.id.imageView24, GONE)
                        .setCommonClickListener(commonClickListener);
                break;
            case "BASIC_DESK_EDIT":
                holder.setGlideImageResource(R.id.imageView23, data.getString("imageUrl"), mContext)
                        .setText(R.id.textView104, data.getInteger("maxPeopleSum").toString())
                        .setText(R.id.textView106, data.getInteger("minPeopleNum").toString())
                        .setText(R.id.textView102, "第" + data.getInteger("deskNumber").toString() + "号桌")
                        .setViewVisibility(R.id.imageView24, VISIBLE)
                        .setCommonClickListener(commonClickListener);
                break;
            case "b":
                break;
        }
    }

    @Override
    public int getLayoutId(JSONObject item, int position) {
        switch (item.getString("WHICHCARD")) {
            case "BASIC_DESK":
                return R.layout.card_place_manager_desk;
            case "b":
                return R.layout.card_place_page;
            case "c":
                return R.layout.card_player_info;
        }
        return R.id.card_equip_effect;
    }
}
