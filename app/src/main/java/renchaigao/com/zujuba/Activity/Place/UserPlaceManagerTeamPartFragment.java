package renchaigao.com.zujuba.Activity.Place;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import renchaigao.com.zujuba.Fragment.BaseFragment;
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

        todayTeamNum = rootView.findViewById(R.id.textView85);
        weekTeamNum = rootView.findViewById(R.id.textView90);
        allTeamNum = rootView.findViewById(R.id.textView86);
        todayTeamMore = rootView.findViewById(R.id.textView92);
        weekTeamMore = rootView.findViewById(R.id.textView98);
        allTeamMore = rootView.findViewById(R.id.textView97);
        todayTeamMore0 = rootView.findViewById(R.id.textView87);
        weekTeamMore0 = rootView.findViewById(R.id.textView91);
        allTeamMore0 = rootView.findViewById(R.id.textView101);

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
