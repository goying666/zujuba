package renchaigao.com.zujuba.Fragment.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.renchaigao.zujuba.mongoDB.info.store.StoreInfo;

import java.util.ArrayList;

import renchaigao.com.zujuba.Activity.Place.StoreActivity;
import renchaigao.com.zujuba.Activity.PlaceListActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.PropertiesConfig;

/**
 * Created by Administrator on 2018/7/17/017.
 */

public class HallFragmentAdapter extends RecyclerView.Adapter<HallFragmentAdapter.ItemHolder> {
    final static String TAG = "HallFragmentAdapter";
    private ArrayList<StoreInfo> mStore;
    private ArrayList<JSONObject> mJsonObject;
    private Context mContext;

    public HallFragmentAdapter(Context context) {
        this.mContext = context;
    }

    public void updateResults(ArrayList<JSONObject> mJsonObject) {
        this.mJsonObject = mJsonObject;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_hall_store, parent, false);
        return new ItemHolder(view);
    }

    /*界面和数据关联部分*/
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ItemHolder holder, final int position) {
        JSONObject store = mJsonObject.get(position);
//        StoreInfo store = mStore.get(position);
        String str = JSONObject.toJSONString(store);

        Glide.with(mContext)
                .load(PropertiesConfig.photoUrl + "showimage/" + store.get("ownerid") + "/" + store.get("placeid") + "/photo2.jpg")
//                .load(PropertiesConfig.photoUrl + "showimage/" + store.getOwnerId() + "/" + store.getId() + "/photo2.jpg")
                .dontAnimate()
                .skipMemoryCache(false)
//                .load("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg")
                .into(holder.store_image);

//        holder.place_star_num.setNumStars(store.getStoreIntegrationInfo().getStartNum());

//        1组装店铺名字
        holder.store_cardview_name.setText(store.get("name").toString());
//        2组装店铺状态
        holder.store_cardview_style.setText(store.get("state").toString());
//        3组装店铺评分
        holder.store_score.setText(store.get("score").toString());
//        4组装店铺多少人玩过
        holder.c_store_allpeoplenum.setText(store.get("allpeoplenum").toString());
//        5组装店铺人均消费
        holder.store_spend.setText(store.get("spend").toString());
//        6组装店铺桌数
        holder.store_desk_info.setText(store.get("desknum").toString());
//        7组装店铺人数
        holder.store_people_info.setText(store.get("todaypeople").toString());
//        8组装店铺距离
        holder.store_place_howlong.setText(store.get("distance").toString());
//        9组装店铺时段
        holder.store_start_time.setText(store.get("time").toString());
//        10组装店铺排名、等荣誉
        holder.store_store_rank.setText(store.get("class").toString());
//        11组装店铺备注
        holder.store_tips.setText(store.get("note").toString());
//        12组装店铺评论1
        holder.store_user_evaluate_1.setText(store.get("evaluate_1").toString());
//        13组装店铺评论2
        holder.store_user_evaluate_2.setText(store.get("evaluate_2").toString());


//        holder.store_cardview_place.setText(store.getAddressInfo().getFormatAddress());

//        holder.store_image.setImageResource(R.drawable.boy);

//        holder.store_team_info.setText(store.get);
//        holder.store_user_evaluate_3.setText();

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreInfo storeInfo = mStore.get(position);
                Intent intent = new Intent(mContext, StoreActivity.class);
//                intent.putExtra("distance",storeInfo.getDistance());
                intent.putExtra("json", JSONObject.toJSONString(storeInfo));
                mContext.startActivity(intent);
            }
        });
        if (mOnItemClickListener != null) {
            holder.thisView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.thisView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mStore == null) {
            return 0;
        } else
            return mStore.size();
    }

    private PlaceListActivity.OnItemClickListener mOnItemClickListener;//声明接口

    public void setOnItemClickListener(PlaceListActivity.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        private View thisView;
        private CardView cardView;
        private ImageView store_cardview_place_icon, store_time_icon, store_image, store_boy_icon, store_girl_icon;
        private RatingBar place_star_num;
        private TextView store_desk_info, store_people_info, store_team_info, store_user_evaluate_1,
                store_user_evaluate_2, store_user_evaluate_3, store_cardview_name, store_cardview_style,
                store_cardview_place, store_place_howlong, store_start_time, store_store_rank, store_tips, store_score, store_spend, c_store_allpeoplenum;

        public ItemHolder(View view) {
            super(view);
            this.thisView = view;
            this.cardView = (CardView) view;
            this.store_time_icon = view.findViewById(R.id.store_time_icon);
            this.store_image = view.findViewById(R.id.store_image);
            this.store_boy_icon = view.findViewById(R.id.store_boy_icon);
            this.store_girl_icon = view.findViewById(R.id.store_girl_icon);
//            this.place_star_num = view.findViewById(R.id.place_star_num);
            this.store_desk_info = view.findViewById(R.id.store_desk_info);
            this.store_people_info = view.findViewById(R.id.store_people_info);
            this.store_user_evaluate_1 = view.findViewById(R.id.store_user_evaluate_1);
            this.store_user_evaluate_2 = view.findViewById(R.id.store_user_evaluate_2);
            this.store_user_evaluate_3 = view.findViewById(R.id.store_user_evaluate_3);
            this.store_cardview_name = view.findViewById(R.id.store_cardview_name);
            this.store_cardview_style = view.findViewById(R.id.store_cardview_style);
            this.store_place_howlong = view.findViewById(R.id.store_place_howlong);
            this.store_start_time = view.findViewById(R.id.store_start_time);
            this.store_store_rank = view.findViewById(R.id.store_store_rank);
            this.store_tips = view.findViewById(R.id.store_tips);
            this.store_score = view.findViewById(R.id.store_score);
            this.store_spend = view.findViewById(R.id.store_spend);
            this.c_store_allpeoplenum = view.findViewById(R.id.c_store_allpeoplenum);

//            cardView = (CardView) itemView;
        }
    }
}
