package renchaigao.com.zujuba.ActivityAndFragment.Place;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonRecycleAdapter;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.MultiTypeSupport;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.PropertiesConfig;

/**
 * Created by Administrator on 2019/2/21/021.
 */

public class PlaceCardAdapter extends CommonRecycleAdapter<JSONObject> implements MultiTypeSupport<JSONObject> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;

    public PlaceCardAdapter(Context context, ArrayList<JSONObject> dataList, int layoutId) {
        super(context, dataList, layoutId);
    }
    public PlaceCardAdapter(Context context, ArrayList<JSONObject> dataList, CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.card_place_page);
        this.commonClickListener = commonClickListener;
    }

    @Override
    public void bindData(CommonViewHolder holder, JSONObject data) {
        holder
//        1组装店铺名字
                .setText(R.id.store_cardview_name, data.get("name").toString())
//        2组装店铺状态
                .setText(R.id.store_cardview_style, data.get("state").toString())
//        3组装店铺评分
                .setText(R.id.store_score, data.get("score").toString())
//        4组装店铺多少人玩过
                .setText(R.id.c_store_allpeoplenum, data.get("allpeoplenum").toString())
//        5组装店铺人均消费
                .setText(R.id.store_spend, data.get("spend").toString())
//        6组装店铺桌数
                .setText(R.id.store_desk_info, data.get("desknum").toString())
//        7组装店铺人数
                .setText(R.id.store_people_info, data.get("todaypeople").toString())
//        8组装店铺距离
                .setText(R.id.store_place_howlong, data.get("distance").toString())
//        9组装店铺时段
                .setText(R.id.store_start_time, data.get("time").toString())
//        10组装店铺排名、等荣誉
                .setText(R.id.store_store_rank, data.get("class").toString())
//        11组装店铺备注
                .setText(R.id.store_tips, data.get("note").toString())
//        12组装店铺评论1
                .setText(R.id.store_user_evaluate_1, data.get("evaluate_1").toString())
//        13组装店铺评论2
                .setText(R.id.store_user_evaluate_2, data.get("evaluate_2").toString())

//                设置图片显示
                .setGlideImageResource(R.id.store_image, PropertiesConfig.photoUrl + "showimage/" + data.get("ownerid") + "/" + data.get("placeid") + "/photo1.jpg",this.layoutInflater.getContext())

                .setCommonClickListener(commonClickListener);



    }


    @Override
    public int getLayoutId(JSONObject item, int position) {
        return R.layout.card_place_page;
    }
}
