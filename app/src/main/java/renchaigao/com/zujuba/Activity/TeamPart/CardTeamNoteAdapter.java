package renchaigao.com.zujuba.Activity.TeamPart;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.PageBean.CardPlayerInfoBean;
import com.renchaigao.zujuba.PageBean.CardTeamNotesBean;


import java.util.ArrayList;

import renchaigao.com.zujuba.Activity.Adapter.CommonRecycleAdapter;
import renchaigao.com.zujuba.Activity.Adapter.CommonViewHolder;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.PropertiesConfig;

import static com.renchaigao.zujuba.PropertiesConfig.UserConstant.GENDER_BOY;

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
