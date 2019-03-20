package renchaigao.com.zujuba.ActivityAndFragment.Main.Adapter;

import android.content.Context;

import com.renchaigao.zujuba.PageBean.CardClubFragmentTipBean;

import java.util.ArrayList;

import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonRecycleAdapter;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.MultiTypeSupport;

import renchaigao.com.zujuba.R;

/**
 * Created by Administrator on 2018/11/28/028.
 */

public class MainClubFragmentAdapter extends CommonRecycleAdapter<CardClubFragmentTipBean> implements MultiTypeSupport<CardClubFragmentTipBean> {

    private CommonViewHolder.onItemCommonClickListener commonClickListener;
    private Context mContext;

    public MainClubFragmentAdapter(Context context, ArrayList<CardClubFragmentTipBean> dataList, int layoutId) {
        super(context, dataList, layoutId);
    }

    public MainClubFragmentAdapter(Context context, ArrayList<CardClubFragmentTipBean> dataList,
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
