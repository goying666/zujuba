package renchaigao.com.zujuba.Fragment.Adapter;

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
import com.renchaigao.zujuba.mongoDB.info.store.StoreInfo;

import java.util.ArrayList;

import renchaigao.com.zujuba.Activity.PlaceListActivity;
import renchaigao.com.zujuba.Activity.Center.StoreActivity;
import renchaigao.com.zujuba.R;

/**
 * Created by Administrator on 2018/7/17/017.
 */

public class HallFragmentAdapter extends RecyclerView.Adapter<HallFragmentAdapter.ItemHolder> {
    final static String TAG = "HallFragmentAdapter";
    private ArrayList<StoreInfo> mStore;
    private Context mContext;

    public HallFragmentAdapter(Context context) {
        this.mContext = context;
    }

    public void updateResults(ArrayList<StoreInfo> mStore) {
        this.mStore = mStore;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_hall_store, parent, false);
        return new ItemHolder(view);
    }

    /*界面和数据关联部分*/
    @Override
    public void onBindViewHolder(final ItemHolder holder, final int position) {
        StoreInfo store = mStore.get(position);
        String str = JSONObject.toJSONString(store);
        holder.place_star_num.setNumStars(store.getStoreIntegrationInfo().getStartNum());
        holder.store_cardview_name.setText(store.getName());
        holder.store_cardview_place.setText(store.getAddressInfo().getFormatAddress());

//        holder.store_image.setImageResource(R.drawable.boy);

        holder.store_desk_info.setText(store.getStoreTeamInfo().getTodayDesk()+"/"+store.getMaxDeskSum());
        holder.store_people_info.setText(store.getStoreTeamInfo().getTodayPeople()+"/"+store.getMaxPeopleSum());
//        holder.store_team_info.setText(store.get);
//        holder.store_user_evaluate_1.setText(store.getStoreEvaluationInfo().getStoreScore());
//        holder.store_user_evaluate_2.setText();
//        holder.store_user_evaluate_3.setText();
        holder.store_cardview_style.setText(store.getState());
        holder.store_place_howlong.setText(store.getAddressInfo().getDistance().toString()+"米");
//        holder.store_start_time.setText(store.getWorkingtimeid().toString());
        holder.store_store_rank.setText(store.getStoreRankInfo().getCityIntegralRank().toString());
        holder.store_tips.setText(store.getTipsinfo());
        holder.store_score.setText(store.getStoreEvaluationInfo().getStoreScore().toString());
        holder.store_spend.setText(store.getStoreShoppingInfo().getAverageSpend().toString());
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
                store_cardview_place, store_place_howlong, store_start_time, store_store_rank, store_tips, store_score, store_spend;

        public ItemHolder(View view) {
            super(view);
            this.thisView = view;
            this.cardView =(CardView) view;
            this.store_cardview_place_icon = view.findViewById(R.id.store_cardview_place_icon);
            this.store_time_icon = view.findViewById(R.id.store_time_icon);
            this.store_image = view.findViewById(R.id.store_image);
            this.store_boy_icon = view.findViewById(R.id.store_boy_icon);
            this.store_girl_icon = view.findViewById(R.id.store_girl_icon);
            this.place_star_num = view.findViewById(R.id.place_star_num);
            this.store_desk_info = view.findViewById(R.id.store_desk_info);
            this.store_people_info = view.findViewById(R.id.store_people_info);
            this.store_team_info = view.findViewById(R.id.store_team_info);
            this.store_user_evaluate_1 = view.findViewById(R.id.store_user_evaluate_1);
            this.store_user_evaluate_2 = view.findViewById(R.id.store_user_evaluate_2);
            this.store_user_evaluate_3 = view.findViewById(R.id.store_user_evaluate_3);
            this.store_cardview_name = view.findViewById(R.id.store_cardview_name);
            this.store_cardview_style = view.findViewById(R.id.store_cardview_style);
            this.store_cardview_place = view.findViewById(R.id.store_cardview_place);
            this.store_place_howlong = view.findViewById(R.id.store_place_howlong);
            this.store_start_time = view.findViewById(R.id.store_start_time);
            this.store_store_rank = view.findViewById(R.id.store_store_rank);
            this.store_tips = view.findViewById(R.id.store_tips);
            this.store_score = view.findViewById(R.id.store_score);
            this.store_spend = view.findViewById(R.id.store_spend);
//            cardView = (CardView) itemView;
        }
    }
}
