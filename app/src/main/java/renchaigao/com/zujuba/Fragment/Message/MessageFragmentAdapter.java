package renchaigao.com.zujuba.Fragment.Message;

import android.content.Context;

import com.renchaigao.zujuba.PageBean.CardMessageFragmentTipBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import normal.dateUse;
import renchaigao.com.zujuba.Activity.Adapter.CommonRecycleAdapter;
import renchaigao.com.zujuba.Activity.Adapter.CommonViewHolder;
import renchaigao.com.zujuba.Activity.Adapter.MultiTypeSupport;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.PropertiesConfig;

import static com.renchaigao.zujuba.PropertiesConfig.ConstantManagement.CLUB_SEND_MESSAGE;
import static com.renchaigao.zujuba.PropertiesConfig.ConstantManagement.FRIEND_SEND_MESSAGE;
import static com.renchaigao.zujuba.PropertiesConfig.ConstantManagement.SYSTEM_SEND_MESSAGE;
import static com.renchaigao.zujuba.PropertiesConfig.ConstantManagement.TEAM_SEND_MESSAGE;

/**
 * Created by Administrator on 2018/11/28/028.
 */

public class MessageFragmentAdapter extends CommonRecycleAdapter<CardMessageFragmentTipBean> implements MultiTypeSupport<CardMessageFragmentTipBean> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;
    private Context mContext;

    public MessageFragmentAdapter(Context context, ArrayList<CardMessageFragmentTipBean> dataList, int layoutId) {
        super(context, dataList, layoutId);
    }

    public MessageFragmentAdapter(Context context, ArrayList<CardMessageFragmentTipBean> dataList,
                                  CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.card_message_tip);
        this.commonClickListener = commonClickListener;
        this.mContext = context;
    }

    @Override
    public void bindData(CommonViewHolder holder, CardMessageFragmentTipBean data) {

        holder.setText(R.id.card_message_name, data.getName())
                .setText(R.id.card_message_current_content, data.getContent())
                .setCommonClickListener(commonClickListener);
        switch (data.getMClass()){
            case TEAM_SEND_MESSAGE:
                holder.setGlideImageResource(R.id.card_message_image, PropertiesConfig.photoUrl + data.getImageUrl(), mContext);
                holder.setText(R.id.textView145,"组局");
                break;
            case SYSTEM_SEND_MESSAGE:
                holder.setImageResource(R.id.card_message_image,R.drawable.logo);
                holder.setText(R.id.textView145,"系统");
                break;
            case CLUB_SEND_MESSAGE:
                holder.setGlideImageResource(R.id.card_message_image, PropertiesConfig.photoUrl + data.getImageUrl(), mContext);
                holder.setText(R.id.textView145,"俱乐部");
                break;
            case FRIEND_SEND_MESSAGE:
                holder.setGlideImageResource(R.id.card_message_image, PropertiesConfig.photoUrl + data.getImageUrl(), mContext);
                holder.setText(R.id.textView145,"好友");
                break;
        }
        int minuteTime = (int) ((dateUse.getNowTimeLong() - data.getLastTime()) / 60000);
        Calendar lastTimeCalendar = Calendar.getInstance();
        lastTimeCalendar.setTimeInMillis(data.getLastTime());
        String strState = "";
        SimpleDateFormat dateFormat;
        if (minuteTime == 0) {
            //        一般格式：0、1分钟内：刚刚
            strState = "刚刚";
        } else if (minuteTime < 60) {
            //        一般格式：1、60分钟内：5分钟前
            strState = String.valueOf(minuteTime) + "分钟前";
        } else if (minuteTime <= 60 * 24) {
            //                  2、1天内：   早上/中午/下午 + 3:00
            dateFormat = new SimpleDateFormat("ah:mm");
            strState = dateFormat.format(lastTimeCalendar.getTime());
        } else if (minuteTime <= 60 * 24) {
            //                  3、两天内：   昨天
            strState = "昨天";
        } else if (minuteTime <= 60 * 24 * 2) {
            //                  4、三天内：   前天
            strState = "前天";
        } else if (minuteTime <= 60 * 24 * 7) {
            //                  5、1周内：   周一/二/三/四/五/六/日
            dateFormat = new SimpleDateFormat("E");
            strState = dateFormat.format(lastTimeCalendar.getTime());
        } else if (minuteTime >= 60 * 24 * 7) {
            //                  6、一周以外：   x月x日
            dateFormat = new SimpleDateFormat("M月dd日");
            strState = dateFormat.format(lastTimeCalendar.getTime());
        }
        holder.setText(R.id.card_message_state, strState);
    }

    @Override
    public int getLayoutId(CardMessageFragmentTipBean item, int position) {
        return R.layout.card_message_tip;
    }

}
