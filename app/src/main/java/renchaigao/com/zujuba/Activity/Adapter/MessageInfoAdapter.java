package renchaigao.com.zujuba.Activity.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.util.ArrayList;

import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.Bean.MessageContent;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.PropertiesConfig;

import static android.view.View.TEXT_ALIGNMENT_TEXT_END;
import static android.view.View.TEXT_ALIGNMENT_TEXT_START;

/**
 * Created by Administrator on 2018/11/27/027.
 */

public class MessageInfoAdapter extends CommonRecycleAdapter<MessageContent> implements MultiTypeSupport<MessageContent> {

    private Context mContext;
    private CommonViewHolder.onItemCommonClickListener commonClickListener;

    public MessageInfoAdapter(Context context, ArrayList<MessageContent> dataList,CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.card_message_info);
        this.mContext = context;
        this.commonClickListener = commonClickListener;
    }

    @Override
    public int getLayoutId(MessageContent item, int position) {
        return 0;
    }

    @Override
    public void bindData(CommonViewHolder holder, MessageContent data) {
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
