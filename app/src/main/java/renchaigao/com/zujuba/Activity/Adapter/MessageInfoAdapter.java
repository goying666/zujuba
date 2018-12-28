package renchaigao.com.zujuba.Activity.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.util.ArrayList;

import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.Bean.MessageContentInfo;
import renchaigao.com.zujuba.util.DataPart.DataUtil;

import static android.view.View.TEXT_ALIGNMENT_TEXT_END;
import static android.view.View.TEXT_ALIGNMENT_TEXT_START;

/**
 * Created by Administrator on 2018/11/27/027.
 */

public class MessageInfoAdapter extends RecyclerView.Adapter<MessageInfoAdapter.ViewHolder> {

    final static String TAG = "PlaceListActivityAdapter";
    private ArrayList<MessageContentInfo> messageContentArrayList;
    private Context mContext;

    private String userId;

    public MessageInfoAdapter(Context context) {
        this.mContext = context;
        UserInfo userInfo = DataUtil.getUserInfoData(this.mContext);
        userId = userInfo.getId();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_message_info, parent, false);
        return new ViewHolder(view);
    }

    public void updateResults(ArrayList<MessageContentInfo> messageContents) {
        this.messageContentArrayList = messageContents;
    }


    @Override
    public int getItemCount() {
        if (messageContentArrayList == null) {
            return 0;
        } else
            return messageContentArrayList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MessageContentInfo messageContent = new MessageContentInfo();
        messageContent = messageContentArrayList.get(position);
        if (messageContent.getSenderId().equals(userId)) {
            holder.message_info_me.setImageResource(R.drawable.boy);
            holder.message_info_other.setVisibility(View.GONE);
            holder.message_info_content.setTextAlignment(TEXT_ALIGNMENT_TEXT_END);
//            holder.message_info_other_content.setVisibility(View.GONE);
//            holder.message_info_me_content.setText(messageContent.getContent());
//            holder.message_info_me.setImageResource(messageContent.getMyImageUrl());
        } else {                                                     
            holder.message_info_other.setImageResource(R.drawable.girl);
            holder.message_info_me.setVisibility(View.GONE);
            holder.message_info_content.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
//            holder.message_info_other_content.setText(messageContent.getContent());
//            holder.message_info_me_content.setVisibility(View.GONE);
//            holder.message_info_other.setImageResource(messageContent.getSenderImageUrl());
        }holder.message_info_content.setText(messageContent.getContent());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView message_info_other, message_info_me;
        private TextView message_info_content;
        private CardView cardView;

        private View thisView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.thisView = itemView;
            this.cardView = (CardView) itemView;
            this.message_info_other = itemView.findViewById(R.id.message_info_other);
            this.message_info_me = itemView.findViewById(R.id.message_info_me);
            this.message_info_content = itemView.findViewById(R.id.message_info_content);
//            this.message_info_me_content = itemView.findViewById(R.id.message_info_me_content);

        }
    }

}
