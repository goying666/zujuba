package renchaigao.com.zujuba.ActivityAndFragment.User.Place;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import renchaigao.com.zujuba.ActivityAndFragment.BaseFragment;
import renchaigao.com.zujuba.R;

public class UserPlaceManagerTeamPartFragment extends BaseFragment {

    public Activity mContext;
    private TextView  todayTeamNum, weekTeamNum, allTeamNum, todayTeamMore, weekTeamMore, allTeamMore, todayTeamMore0, weekTeamMore0, allTeamMore0;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    @Override
    protected void InitView(View rootView) {

        todayTeamNum = (TextView) rootView.findViewById(R.id.textView85);
        weekTeamNum = (TextView) rootView.findViewById(R.id.textView90);
        allTeamNum = (TextView) rootView.findViewById(R.id.textView86);
        todayTeamMore = (TextView) rootView.findViewById(R.id.textView92);
        weekTeamMore = (TextView) rootView.findViewById(R.id.textView98);
        allTeamMore = (TextView) rootView.findViewById(R.id.textView97);
        todayTeamMore0 = (TextView) rootView.findViewById(R.id.textView87);
        weekTeamMore0 = (TextView) rootView.findViewById(R.id.textView91);
        allTeamMore0 = (TextView) rootView.findViewById(R.id.textView101);

    }

    @Override
    protected void InitData(View rootView) {

    }

    @Override
    protected void InitOther(View rootView) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_place_manager_team_part;
    }

}
