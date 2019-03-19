package renchaigao.com.zujuba.Activity.Store;

import android.content.Context;

import com.renchaigao.zujuba.PageBean.CardClubFragmentTipBean;
import com.renchaigao.zujuba.PageBean.CardStoreTeamFragmentBean;

import java.util.ArrayList;

import renchaigao.com.zujuba.Activity.Adapter.CommonRecycleAdapter;
import renchaigao.com.zujuba.Activity.Adapter.CommonViewHolder;
import renchaigao.com.zujuba.Activity.Adapter.MultiTypeSupport;
import renchaigao.com.zujuba.R;

/**
 * Created by Administrator on 2018/11/28/028.
 */

public class StoreTeamFragmentAdapter extends CommonRecycleAdapter<CardStoreTeamFragmentBean> implements MultiTypeSupport<CardStoreTeamFragmentBean> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;
    private Context mContext;

    public StoreTeamFragmentAdapter(Context context, ArrayList<CardStoreTeamFragmentBean> dataList, int layoutId) {
        super(context, dataList, layoutId);
    }

    public StoreTeamFragmentAdapter(Context context, ArrayList<CardStoreTeamFragmentBean> dataList,
                                    CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.card_store_team_note);
        this.commonClickListener = commonClickListener;
        this.mContext = context;
    }

    @Override
    public void bindData(CommonViewHolder holder, CardStoreTeamFragmentBean data) {
        holder.setText(R.id.textView153,data.getTeamName())
                .setText(R.id.textView154,data.getState())
                .setText(R.id.textView155,data.getCreateTime())
                .setText(R.id.textView156,data.getCreaterNickname())
                .setText(R.id.textView157,data.getPlayerNum().toString())
                .setText(R.id.textView158,data.getMainGame())
                .setCommonClickListener(commonClickListener);
    }

    @Override
    public int getLayoutId(CardStoreTeamFragmentBean item, int position) {
        return R.layout.card_store_team_note;
    }

}
