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
 * Created by Administrator on 2018/7/17/017.
 */

public class MainStoreFragmentAdapter extends CommonRecycleAdapter<JSONObject> implements MultiTypeSupport<JSONObject> {
    private CommonViewHolder.onItemCommonClickListener commonClickListener;
    private Context mContext;

    public MainStoreFragmentAdapter(Context context, ArrayList<JSONObject> dataList,
                                    CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.card_place_page);
        this.commonClickListener = commonClickListener;
        this.mContext = context;
    }

    @Override
    public void bindData(CommonViewHolder holder, JSONObject data) {

        holder
//        1组装店铺名字
                .setText(R.id.store_cardview_name, data.getString("name"))
//        2组装店铺状态
                .setText(R.id.store_cardview_style, data.getString("state"))
//        3组装店铺评分
                .setText(R.id.store_score, data.getString("score"))
//        4组装店铺多少人玩过
                .setText(R.id.c_store_allpeoplenum, data.getString("allpeoplenum"))
//        5组装店铺人均消费
                .setText(R.id.store_spend, data.getString("spend"))
//        6组装店铺桌数
                .setText(R.id.store_desk_info, data.getString("desknum"))
//        7组装店铺人数
                .setText(R.id.store_people_info, data.getString("todaypeople"))
//        8组装店铺距离
                .setText(R.id.store_place_howlong, data.getString("distance"))
//        9组装店铺时段
                .setText(R.id.store_start_time, data.getString("time"))
//        10组装店铺排名、等荣誉
                .setText(R.id.store_store_rank, data.getString("class"))
//        11组装店铺备注
                .setText(R.id.store_tips, data.getString("note"))
//        12组装店铺评论1
                .setText(R.id.store_user_evaluate_1, data.getString("evaluate_1"))
//        13组装店铺评论2
                .setText(R.id.store_user_evaluate_2, data.getString("evaluate_2"))
                .setGlideImageResource(R.id.store_image,
                        PropertiesConfig.photoUrl
                                + "showimage/"
                                + data.get("ownerid")
                                + "/"
                                + data.get("placeid")
                                + "/photo1.jpg"
                        , mContext)
                .setCommonClickListener(commonClickListener);
    }

    @Override
    public int getLayoutId(JSONObject item, int position) {
        return R.layout.card_place_page;
    }


}
