package renchaigao.com.zujuba.ActivityAndFragment.Store.Manager;

import android.content.Context;

import com.renchaigao.zujuba.PageBean.CardStoreManagerBasicWorkLimitBean;
import com.renchaigao.zujuba.PageBean.CardStoreTeamFragmentBean;

import java.util.ArrayList;

import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonRecycleAdapter;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.MultiTypeSupport;
import renchaigao.com.zujuba.R;

/**
 * Created by Administrator on 2018/11/28/028.
 */

public class StoreManagerBasicWorkLimitAdapter extends CommonRecycleAdapter<CardStoreManagerBasicWorkLimitBean> implements MultiTypeSupport<CardStoreManagerBasicWorkLimitBean> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;
    private Context mContext;

    public StoreManagerBasicWorkLimitAdapter(Context context, ArrayList<CardStoreManagerBasicWorkLimitBean> dataList, int layoutId) {
        super(context, dataList, layoutId);
    }

    public StoreManagerBasicWorkLimitAdapter(Context context, ArrayList<CardStoreManagerBasicWorkLimitBean> dataList,
                                             CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.card_store_time_list);
        this.commonClickListener = commonClickListener;
        this.mContext = context;
    }

    @Override
    public void bindData(CommonViewHolder holder, CardStoreManagerBasicWorkLimitBean data) {
        holder.setText(R.id.textView167,data.getLimitClass())
                .setText(R.id.textView168,data.getDate())
                .setText(R.id.textView169,data.getStartTime())
                .setText(R.id.textView170,data.getEndTime())
                .setCommonClickListener(commonClickListener);
    }

    @Override
    public int getLayoutId(CardStoreManagerBasicWorkLimitBean item, int position) {
        return R.layout.card_store_time_list;
    }

}
