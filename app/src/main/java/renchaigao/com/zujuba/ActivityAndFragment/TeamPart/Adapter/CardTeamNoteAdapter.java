package renchaigao.com.zujuba.ActivityAndFragment.TeamPart.Adapter;

import android.content.Context;

import com.renchaigao.zujuba.PageBean.CardTeamNotesBean;


import java.util.ArrayList;

import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonRecycleAdapter;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.R;

/**
 * Created by Administrator on 2019/2/17/017.
 */

public class CardTeamNoteAdapter extends CommonRecycleAdapter<CardTeamNotesBean> {

    private Context mContext;

    public CardTeamNoteAdapter(Context context, ArrayList<CardTeamNotesBean> dataList) {
        super(context, dataList, R.layout.card_team_note);
        this.mContext = context;
    }

    @Override
    public void bindData(CommonViewHolder holder, CardTeamNotesBean data) {

        holder.setText(R.id.card_time, data.getTime())
                .setText(R.id.team_note_conten, data.getContent());

    }
}
