package renchaigao.com.zujuba.Activity.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.renchaigao.zujuba.mongoDB.info.team.TeamInfo;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import renchaigao.com.zujuba.R;

/**
 * Created by Administrator on 2018/8/7/007.
 */

public class TeamActivityAdapter extends RecyclerView.Adapter<TeamActivityAdapter.ViewHolder> {
    final static String TAG = "PlaceListActivityAdapter";
    private ArrayList<TeamInfo> teamInfoArrayList;
    private Context mContext;

    public TeamActivityAdapter(Context context) {
        this.mContext = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    public void updateResults(ArrayList<TeamInfo> teamInfos) {
        this.teamInfoArrayList = teamInfos;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TeamInfo teamInfo ;
        teamInfo = new TeamInfo();
//        holder.card_player_name.setText(teamInfo.getAddressInfo().getStoreInfo());
//        holder.card_player_zu_number.setText(teamInfo.getName());
//        holder.card_player_you_number.setText(teamInfo.getName());
//        holder.card_player_state.setText(teamInfo.getName());
//        holder.card_player_rank.setText(teamInfo.getName());
//        holder.card_player_image.setText(teamInfo.getName());
//        holder.card_player_sex.setText(teamInfo.getName());
//        holder.card_player_age.setText(teamInfo.getName());

    }

    @Override
    public int getItemCount() {
        if (teamInfoArrayList == null) {
            return 0;
        } else
            return teamInfoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView card_player_name, card_player_zu_number, card_player_you_number, card_player_state, card_player_rank;
        private CircleImageView card_player_image;
        private ImageView card_player_sex, card_player_age;
        private View thisView;
        private CardView cardView;

        public ViewHolder(View view) {
            super(view);
            this.thisView = view;
            this.cardView = (CardView) view;
            this.card_player_name = view.findViewById(R.id.card_player_name);
            this.card_player_zu_number = view.findViewById(R.id.card_player_zu_number);
            this.card_player_you_number = view.findViewById(R.id.card_player_you_number);
            this.card_player_state = view.findViewById(R.id.card_player_state);
            this.card_player_rank = view.findViewById(R.id.card_player_rank);
            this.card_player_image = view.findViewById(R.id.card_player_image);
            this.card_player_sex = view.findViewById(R.id.card_player_sex);
            this.card_player_age = view.findViewById(R.id.card_player_age);
        }
    }
}