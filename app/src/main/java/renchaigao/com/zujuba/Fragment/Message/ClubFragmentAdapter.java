package renchaigao.com.zujuba.Fragment.Message;

import android.content.Context;

import com.renchaigao.zujuba.PageBean.CardClubFragmentTipBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import normal.dateUse;
import renchaigao.com.zujuba.Activity.Adapter.CommonRecycleAdapter;
import renchaigao.com.zujuba.Activity.Adapter.CommonViewHolder;
import renchaigao.com.zujuba.Activity.Adapter.MultiTypeSupport;

import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.PropertiesConfig;

/**
 * Created by Administrator on 2018/11/28/028.
 */

public class ClubFragmentAdapter extends CommonRecycleAdapter<CardClubFragmentTipBean> implements MultiTypeSupport<CardClubFragmentTipBean> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;
    private Context mContext;

    public ClubFragmentAdapter(Context context, ArrayList<CardClubFragmentTipBean> dataList, int layoutId) {
        super(context, dataList, layoutId);
    }

    public ClubFragmentAdapter(Context context, ArrayList<CardClubFragmentTipBean> dataList,
                               CommonViewHolder.onItemCommonClickListener commonClickListener) {
        super(context, dataList, R.layout.card_club_tip);
        this.commonClickListener = commonClickListener;
        this.mContext = context;
    }

    @Override
    public void bindData(CommonViewHolder holder, CardClubFragmentTipBean data) {
//        holder.setGlideImageResource(R.id.card_message_image, PropertiesConfig.photoUrl + data.getImageUrl(), mContext);
        holder.setText(R.id.card_club_name,data.getClubName())
                .setText(R.id.card_club_address,data.getPlaceName())
                .setText(R.id.card_club_peopleNum,data.getAllPeopleNum())
                .setText(R.id.textView119,data.getActivitiesNum())
//                .setText(R.id.textView120,data.get())
                .setText(R.id.textView121,data.getWhoAmI())
                .setCommonClickListener(commonClickListener);

    }

    @Override
    public int getLayoutId(CardClubFragmentTipBean item, int position) {
        return R.layout.card_club_tip;
    }

}
