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
import com.renchaigao.zujuba.mongoDB.info.team.TeamInfo;

import java.util.ArrayList;

import renchaigao.com.zujuba.Activity.TeamPart.TeamActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.dateUse;

/**
 * Created by Administrator on 2018/8/1/001.
 */

public class TeamFragmentAdapter extends RecyclerView.Adapter<TeamFragmentAdapter.ItemHolder> {
    final static String TAG = "TeamFragmentAdapter";
    private ArrayList<TeamInfo> mTeamList;
    private Context mContext;


    public TeamFragmentAdapter(Context context) {
        this.mContext = context;
    }

    public void updateResults(ArrayList<TeamInfo> mStore) {
        this.mTeamList = mStore;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_team_main, parent, false);
        return new ItemHolder(view);
    }

//    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {
        TeamInfo teamInfo = mTeamList.get(position);
        String teamJson = JSONObject.toJSONString(teamInfo);
//        holder.place_star_num.setNumStars(teamInfo.getAddressInfo().getStarValue());
        holder.team_image.setImageResource(R.drawable.lrs_image);
        holder.team_boy_number.setText(teamInfo.getTeamPlayerInfo().getBoySum().toString() + "人");
        holder.team_girl_number.setText(teamInfo.getTeamPlayerInfo().getGirlSum().toString() + "人");
        holder.team_cardview_name.setText(teamInfo.getTeamName());
        holder.team_cardview_state.setText(teamInfo.getState());
        holder.team_cardview_place.setText(teamInfo.getAddressInfo().getAddressName());
        holder.team_place_howlong.setText(teamInfo.getAddressInfo().getDistance().toString());
        holder.my_team_cardview_people_number.setText(teamInfo.getTeamPlayerInfo().getWatingSum() + "/" + teamInfo.getPlayerMin() + "~" + teamInfo.getPlayerMax());
        holder.team_start_date.setText(teamInfo.getStartDate());
        holder.team_start_time.setText(teamInfo.getStartTime());
        holder.team_lave_time.setText(dateUse.GameBeginCountDown(teamInfo.getStartAllTime()));
//        holder.team_tips.setText(teamInfo.team_tips);
//        holder.team_shop_info.setText(teamInfo.team_shop_info);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeamInfo teamInfo1 = mTeamList.get(position);
                Intent intent = new Intent(mContext, TeamActivity.class);
                intent.putExtra("teamInfo", JSONObject.toJSONString(teamInfo1));
                intent.putExtra("COME_FROM", "FRAGMENT_TEAM");
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mTeamList == null) {
            return 0;
        } else
            return mTeamList.size();
    }
//    private PlaceListActivity.OnItemClickListener mOnItemClickListener;//声明接口
//
//    public void setOnItemClickListener(PlaceListActivity.OnItemClickListener onItemClickListener) {
//        mOnItemClickListener = onItemClickListener;
//    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView team_boy_number,
                team_girl_number,
                team_cardview_name,
                team_cardview_state,
                team_cardview_place,
                team_place_howlong,
                my_team_cardview_people_number,
                team_start_date,
                team_start_time,
                team_lave_time,
                team_tips,
                team_shop_info;
        private RatingBar place_star_num;
        private ImageView team_image;

        private View thisView;
        private CardView cardView;

        public ItemHolder(View view) {
            super(view);
            this.thisView = view;
            this.cardView = (CardView) view;
            this.place_star_num = view.findViewById(R.id.place_star_num);
            this.team_image = view.findViewById(R.id.team_image);
            this.team_boy_number = view.findViewById(R.id.team_boy_number);
            this.team_girl_number = view.findViewById(R.id.team_girl_number);
            this.team_cardview_name = view.findViewById(R.id.team_cardview_name);
            this.team_cardview_state = view.findViewById(R.id.team_cardview_state);
            this.team_cardview_place = view.findViewById(R.id.team_cardview_place);
            this.team_place_howlong = view.findViewById(R.id.team_place_howlong);
            this.my_team_cardview_people_number = view.findViewById(R.id.my_team_cardview_people_number);
            this.team_start_date = view.findViewById(R.id.team_start_date);
            this.team_start_time = view.findViewById(R.id.team_start_time);
            this.team_lave_time = view.findViewById(R.id.team_lave_time);
            this.team_tips = view.findViewById(R.id.team_tips);
            this.team_shop_info = view.findViewById(R.id.team_shop_info);
        }
    }

}
