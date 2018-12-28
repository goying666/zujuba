package renchaigao.com.zujuba.GamePart.DeepForest.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.renchaigao.zujuba.mongoDB.info.game.DeepForest.CivilizationInfo;

import java.util.ArrayList;

import renchaigao.com.zujuba.R;

/**
 * Created by goying on 2018/9/8.
 */

public class DFGPlayerAdapter extends RecyclerView.Adapter<DFGPlayerAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<CivilizationInfo> civilizationInfos;

    public DFGPlayerAdapter(Context context) {
        this.mContext = context;
    }


    public void updateResults(ArrayList<CivilizationInfo> civilizationInfos) {
        this.civilizationInfos = civilizationInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CivilizationInfo civilizationInfo = civilizationInfos.get(position);
        holder.dfg_card_main_player_active.setText(civilizationInfo.getActive());
//        holder.dfg_card_main_player_name.setText(civilizationInfo.getUserName());
        holder.dfg_card_main_player_number.setText(civilizationInfo.getNumber());
//        holder.dfg_card_main_player_signature.setText(civilizationInfo.getSignature());
//        holder.dfg_card_main_player_image.setImageResource(civilizationInfo.getUserImage());

    }

    @Override
    public int getItemCount() {
        if (civilizationInfos == null) {
            return 0;
        } else
            return civilizationInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View thisView;
        private CardView cardView;
        private ImageView dfg_card_main_player_image;
        private TextView
                dfg_card_main_player_active,
                dfg_card_main_player_name,
                dfg_card_main_player_number,
                dfg_card_main_player_signature;

        public ViewHolder(View itemView) {
            super(itemView);
            this.thisView = itemView;
            this.cardView = (CardView) itemView;
            this.dfg_card_main_player_image = itemView.findViewById(R.id.dfg_card_main_player_image);
            this.dfg_card_main_player_active = itemView.findViewById(R.id.dfg_card_main_player_active);
            this.dfg_card_main_player_name = itemView.findViewById(R.id.dfg_card_main_player_name);
            this.dfg_card_main_player_number = itemView.findViewById(R.id.dfg_card_main_player_number);
            this.dfg_card_main_player_signature = itemView.findViewById(R.id.dfg_card_main_player_signature);
        }
    }
}
