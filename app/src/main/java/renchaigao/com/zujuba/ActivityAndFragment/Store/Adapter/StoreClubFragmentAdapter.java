package renchaigao.com.zujuba.ActivityAndFragment.Store.Adapter;

import android.content.Context;

import com.renchaigao.zujuba.PageBean.CardStoreClubFragmentBean;

import java.util.ArrayList;

import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonRecycleAdapter;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.MultiTypeSupport;
import renchaigao.com.zujuba.R;

/**
 * Created by Administrator on 2018/11/28/028.
 */

public class StoreClubFragmentAdapter extends CommonRecycleAdapter<CardStoreClubFragmentBean> implements MultiTypeSupport<CardStoreClubFragmentBean> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;
    private Context mContext;

    public StoreClubFragmentAdapter(Context context, ArrayList<CardStoreClubFragmentBean> dataList, int layoutId) {
        super(context, dataList, layoutId);
    }

    public StoreClubFragmentAdapter(Context context, ArrayList<CardStoreClubFragmentBean> dataList,
                                    CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.card_store_club_note);
        this.commonClickListener = commonClickListener;
        this.mContext = context;
    }

    @Override
    public void bindData(CommonViewHolder holder, CardStoreClubFragmentBean data) {
        holder.setText(R.id.textView153,data.getClubName())
                .setText(R.id.textView154,data.getClubLevel().toString())
                .setText(R.id.textView155,data.getCreateTime())
                .setText(R.id.textView156,data.getCreaterNickName())
                .setText(R.id.textView157,data.getSumOfPlayers().toString())
                .setText(R.id.textView158,data.getSumOfTeams().toString())
                .setText(R.id.textView152,data.getSumOfGames().toString())
                .setCommonClickListener(commonClickListener);
    }

    @Override
    public int getLayoutId(CardStoreClubFragmentBean item, int position) {
        return R.layout.card_store_club_note;
    }

}
