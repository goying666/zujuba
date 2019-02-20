package renchaigao.com.zujuba.Activity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import renchaigao.com.zujuba.Activity.Place.UserPlaceManagerActivity;
import renchaigao.com.zujuba.Activity.PlaceListActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.PropertiesConfig;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2018/8/7/007.
 */

public class UserPlaceListPageAdapter extends CommonRecycleAdapter<JSONObject> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;

    private Context mContext;

    public UserPlaceListPageAdapter(Context context, ArrayList<JSONObject> dataList,
                                    CommonViewHolder.onItemCommonClickListener commonClickListener, int layoutId) {
        super(context, dataList, layoutId);
        this.commonClickListener = commonClickListener;
        this.mContext = context;
    }
    @Override
    void bindData(CommonViewHolder holder, JSONObject data) {
        holder.setGlideImageResource(R.id.imageView11, PropertiesConfig.photoUrl + data.getString("imageurl"), mContext)
                .setText(R.id.textView57, data.getString("name"))
                .setText(R.id.textView58, data.getString("state"))
                .setText(R.id.textView59, data.getString("sysnote"))
                .setCommonClickListener(commonClickListener);
    }
}