package renchaigao.com.zujuba.ActivityAndFragment.Store.Manager;

import android.content.Context;
import android.graphics.Color;

import com.renchaigao.zujuba.PageBean.CardStoreManagerBasicHardwareBean;
import com.renchaigao.zujuba.PageBean.CardStoreTeamFragmentBean;

import java.util.ArrayList;

import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonRecycleAdapter;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.MultiTypeSupport;
import renchaigao.com.zujuba.R;

/**
 * Created by Administrator on 2018/11/28/028.
 */

public class StoreManagerBasicHardwareAdapter extends CommonRecycleAdapter<CardStoreManagerBasicHardwareBean> implements MultiTypeSupport<CardStoreManagerBasicHardwareBean> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;
    private Context mContext;

    public StoreManagerBasicHardwareAdapter(Context context, ArrayList<CardStoreManagerBasicHardwareBean> dataList, int layoutId) {
        super(context, dataList, layoutId);
    }

    public StoreManagerBasicHardwareAdapter(Context context, ArrayList<CardStoreManagerBasicHardwareBean> dataList,
                                            CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.card_store_hardware_list);
        this.commonClickListener = commonClickListener;
        this.mContext = context;
    }

    @Override
    public void bindData(CommonViewHolder holder, CardStoreManagerBasicHardwareBean data) {
        holder.setImageResource(R.id.imageView47, R.drawable.hd_wifi);
        if (data.getState()) {
            holder.setText(R.id.textView188, "有")
                    .setTextColor(R.id.textView188, Color.rgb(0xff, 0, 0));

        } else {
            holder.setText(R.id.textView188, "无")
                    .setTextColor(R.id.textView188, Color.rgb(0, 0xff, 0));
        }
        holder.setText(R.id.textView186, data.getName())
                .setText(R.id.textView187, data.getNote())
                .setCommonClickListener(commonClickListener);
    }

    @Override
    public int getLayoutId(CardStoreManagerBasicHardwareBean item, int position) {
        return R.layout.card_store_hardware_list;
    }

}
