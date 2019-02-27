package renchaigao.com.zujuba.Fragment.Adapter;

import android.content.Context;

import com.renchaigao.zujuba.PageBean.CardMessageFragmentTipBean;

import java.util.ArrayList;

import renchaigao.com.zujuba.Activity.Adapter.CommonRecycleAdapter;
import renchaigao.com.zujuba.Activity.Adapter.CommonViewHolder;
import renchaigao.com.zujuba.Activity.Adapter.MultiTypeSupport;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.PropertiesConfig;

/**
 * Created by Administrator on 2018/11/28/028.
 */

public class MessageFragmentAdapter extends CommonRecycleAdapter<CardMessageFragmentTipBean> implements MultiTypeSupport<CardMessageFragmentTipBean> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;
    private Context mContext;

    public MessageFragmentAdapter(Context context, ArrayList<CardMessageFragmentTipBean> dataList, int layoutId) {
        super(context, dataList, layoutId);
    }

    public MessageFragmentAdapter(Context context, ArrayList<CardMessageFragmentTipBean> dataList, CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.card_message_tip);
        this.commonClickListener = commonClickListener;
        this.mContext = context;
    }


    @Override
    public void bindData(CommonViewHolder holder, CardMessageFragmentTipBean data) {
        holder.setGlideImageResource(R.id.card_message_image, PropertiesConfig.photoUrl + data.getImageUrl(), mContext)
                .setText(R.id.card_message_name, data.getName())
                .setText(R.id.card_message_current_content, data.getContent())
                .setText(R.id.card_message_state, data.getTime())
                .setCommonClickListener(commonClickListener);
    }

    @Override
    public int getLayoutId(CardMessageFragmentTipBean item, int position) {
        return R.layout.card_message_tip;
    }

}
