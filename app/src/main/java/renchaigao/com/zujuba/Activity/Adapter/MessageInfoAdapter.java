package renchaigao.com.zujuba.Activity.Adapter;

import android.content.Context;

import java.util.ArrayList;

import renchaigao.com.zujuba.Bean.AndroidMessageContent;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.PropertiesConfig;

/**
 * Created by Administrator on 2018/11/27/027.
 */

public class MessageInfoAdapter extends CommonRecycleAdapter<AndroidMessageContent> implements MultiTypeSupport<AndroidMessageContent> {

    private Context mContext;
    private CommonViewHolder.onItemCommonClickListener commonClickListener;

    public MessageInfoAdapter(Context context, ArrayList<AndroidMessageContent> dataList, CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.card_message_info);
        this.mContext = context;
        this.commonClickListener = commonClickListener;
    }

    @Override
    public int getLayoutId(AndroidMessageContent item, int position) {
        return 0;
    }

    @Override
    public void bindData(CommonViewHolder holder, AndroidMessageContent data) {
        if (data.getIsMe()) {
            holder
                    .setImagelucency(R.id.message_info_other)
                    .setGlideImageResource(R.id.message_info_me, PropertiesConfig.photoUrl + data.getMyImageUrl(), mContext)
                    .setText(R.id.message_info_content, data.getContent())
                    .setCommonClickListener(commonClickListener);
        } else {
            holder
                    .setImagelucency(R.id.message_info_me)
                    .setGlideImageResource(R.id.message_info_other, PropertiesConfig.photoUrl + data.getSenderImageUrl(), mContext)
                    .setText(R.id.message_info_content, data.getContent())
                    .setCommonClickListener(commonClickListener);
        }
    }
}
