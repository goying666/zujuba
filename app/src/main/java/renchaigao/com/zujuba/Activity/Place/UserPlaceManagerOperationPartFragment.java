package renchaigao.com.zujuba.Activity.Place;

import android.app.Activity;
import android.view.View;

import renchaigao.com.zujuba.Fragment.BaseFragment;
import renchaigao.com.zujuba.R;

public class UserPlaceManagerOperationPartFragment extends BaseFragment {

    public Activity mContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    @Override
    protected void InitView(View rootView) {

    }

    @Override
    protected void InitData(View rootView) {

    }

    @Override
    protected void InitOther(View rootView) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_place_manager_operation_part;
    }

}
