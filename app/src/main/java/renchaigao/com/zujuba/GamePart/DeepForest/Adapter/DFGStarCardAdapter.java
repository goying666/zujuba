package renchaigao.com.zujuba.GamePart.DeepForest.Adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.renchaigao.zujuba.mongoDB.info.game.DeepForest.CivilizationInfo;
import com.renchaigao.zujuba.mongoDB.info.game.DeepForest.StarInfo;

import java.util.ArrayList;

import renchaigao.com.zujuba.R;

/**
 * Created by goying on 2018/9/8.
 */

public class DFGStarCardAdapter extends RecyclerView.Adapter<DFGStarCardAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<StarInfo> starInfoArrayList;
    private CivilizationInfo civilizationInfo;

    public DFGStarCardAdapter(Context context) {
        this.mContext = context;
    }

    public void updateResults(ArrayList<StarInfo> starInfoArrayList,CivilizationInfo civilizationInfo) {
        this.starInfoArrayList = starInfoArrayList;
        this.civilizationInfo = civilizationInfo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StarInfo starInfo = starInfoArrayList.get(position);
//        holder.card_game_deep_forest_star_image.setImageResource(starInfo.get);
//        holder.card_game_deep_forest_star_select_Image.setText(starInfo.get);
//        holder.card_game_deep_forest_star_message.setText(starInfo.);
//        holder.card_game_deep_forest_star_name.setText(starInfo.get);
//        holder.card_game_deep_forest_star_timeValue.setText(starInfo.get);
//        holder.card_game_deep_forest_star_technologyValue.setText(starInfo.get);
//        holder.card_game_deep_forest_star_defenceValue.setText(starInfo.get);
//        holder.card_game_deep_forest_star_number.setText(starInfo.get);
//        holder.card_game_deep_forest_star_levelValue.setText(starInfo.get);
//        holder.card_game_deep_forest_star_ownerName.setText(starInfo.get);
//
//        holder.card_game_deep_forest_star_message_infoPart.setText(starInfo.get);
//        holder.card_game_deep_forest_star_messagePart.setText(starInfo.get);

    }

    @Override
    public int getItemCount() {
        if (starInfoArrayList == null) {
            return 0;
        } else
            return starInfoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View thisView;
        private CardView cardView;
        private ImageView card_game_deep_forest_star_image,
                card_game_deep_forest_star_select_Image;
        private TextView card_game_deep_forest_star_message;
        private TextView card_game_deep_forest_star_name;
        private TextView card_game_deep_forest_star_timeValue;
        private TextView card_game_deep_forest_star_technologyValue;
        private TextView card_game_deep_forest_star_defenceValue;
        private TextView card_game_deep_forest_star_number;
        private TextView card_game_deep_forest_star_levelValue;
        private TextView card_game_deep_forest_star_ownerName;

        private ConstraintLayout card_game_deep_forest_star_message_infoPart;
        private ConstraintLayout card_game_deep_forest_star_messagePart;

        public ViewHolder(View itemView) {
            super(itemView);
            this.thisView = itemView;
            this.cardView = (CardView) itemView;
            this.card_game_deep_forest_star_image = (ImageView) itemView.findViewById(R.id.card_game_deep_forest_star_image);
            this.card_game_deep_forest_star_select_Image = (ImageView) itemView.findViewById(R.id.card_game_deep_forest_star_select_Image);
            this.card_game_deep_forest_star_message = (TextView) itemView.findViewById(R.id.card_game_deep_forest_star_message);
            this.card_game_deep_forest_star_name = (TextView) itemView.findViewById(R.id.card_game_deep_forest_star_name);
            this.card_game_deep_forest_star_timeValue = (TextView) itemView.findViewById(R.id.card_game_deep_forest_star_timeValue);
            this.card_game_deep_forest_star_technologyValue = (TextView) itemView.findViewById(R.id.card_game_deep_forest_star_technologyValue);
            this.card_game_deep_forest_star_defenceValue = (TextView) itemView.findViewById(R.id.card_game_deep_forest_star_defenceValue);
            this.card_game_deep_forest_star_number = (TextView) itemView.findViewById(R.id.card_game_deep_forest_star_number);
            this.card_game_deep_forest_star_levelValue = (TextView) itemView.findViewById(R.id.card_game_deep_forest_star_levelValue);
            this.card_game_deep_forest_star_ownerName = (TextView) itemView.findViewById(R.id.card_game_deep_forest_star_ownerName);
            this.card_game_deep_forest_star_message_infoPart = (ConstraintLayout) itemView.findViewById(R.id.card_game_deep_forest_star_message_infoPart);
            this.card_game_deep_forest_star_messagePart = (ConstraintLayout) itemView.findViewById(R.id.card_game_deep_forest_star_messagePart);
        }
    }
}
