package renchaigao.com.zujuba.Fragment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import renchaigao.com.zujuba.Activity.MessageInfoActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.Bean.MessageNoteInfo;

/**
 * Created by Administrator on 2018/11/28/028.
 */

public class MessageFragmentAdapter extends RecyclerView.Adapter<MessageFragmentAdapter.ItemHolder>{
    final static String TAG = "TeamFragmentAdapter";
    private ArrayList<MessageNoteInfo> messageNoteInfoArrayList;
    private Context mContext;

    public MessageFragmentAdapter(Context context) {
        this.mContext = context;
    }


    public void updateResults(ArrayList<MessageNoteInfo> messageNoteInfoArrayList) {
        this.messageNoteInfoArrayList = messageNoteInfoArrayList;
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_message_tip, parent, false);
        return new ItemHolder(view);
    }


    @Override
    public void onBindViewHolder(ItemHolder holder,final int position) {
        MessageNoteInfo messageNoteInfo =  messageNoteInfoArrayList.get(position);
        holder.card_message_image.setImageResource(messageNoteInfo.getMessageImageUrl());
        holder.card_message_name.setText(messageNoteInfo.getMessageName());
        holder.card_message_current_content.setText(messageNoteInfo.getCurrentContent());
        holder.card_message_state.setText(messageNoteInfo.getState());



        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageNoteInfo messageNoteInfo_ =  messageNoteInfoArrayList.get(position);
                Intent intent = new Intent(mContext, MessageInfoActivity.class);
                intent.putExtra("messageNoteInfo", JSONObject.toJSONString(messageNoteInfo_));
                intent.putExtra("COME_FROM", "FRAGMENT_MESSAGE");
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (messageNoteInfoArrayList == null) {
            return 0;
        } else
            return messageNoteInfoArrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView card_message_image;
        private TextView card_message_name,card_message_current_content,card_message_state;
        private CardView cardView;
        private View thisView;

        public ItemHolder(View view) {
            super(view);
            this.thisView = view;
            this.cardView = (CardView) view;
            this.card_message_image = view.findViewById(R.id.card_message_image);
            this.card_message_name = view.findViewById(R.id.card_message_name);
            this.card_message_current_content = view.findViewById(R.id.card_message_current_content);
            this.card_message_state = view.findViewById(R.id.card_message_state);
        }
    }
}
