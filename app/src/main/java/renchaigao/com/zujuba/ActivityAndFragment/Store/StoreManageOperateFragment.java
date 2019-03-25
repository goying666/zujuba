package renchaigao.com.zujuba.ActivityAndFragment.Store;


import android.view.View;
import android.widget.TextView;

import renchaigao.com.zujuba.ActivityAndFragment.BaseFragment;
import renchaigao.com.zujuba.R;

public class StoreManageOperateFragment extends BaseFragment {

    private TextView workTimes;
    private TextView workRecyclerView;
    private TextView updateStoreState;
    private TextView updateStoreName;
    private TextView updateHardwareNote;
    private TextView updateAddressNote;
    private TextView updateAddress;
    private TextView textView61;
    private TextView textView49;
    private TextView textView2;
    private TextView textView190;
    private TextView textView185;
    private TextView textView183;
    private TextView textView182;
    private TextView textView181;
    private TextView textView180;
    private TextView textView179;
    private TextView textView178;
    private TextView textView177;
    private TextView textView172;
    private TextView textView171;
    private TextView textView165;
    private TextView textView164;
    private TextView textView163;
    private TextView textView162;
    private TextView textView161;
    private TextView textView159;
    private TextView textView1;
    private TextView textView;
    private TextView sumOfHardware;
    private TextView storeState;
    private TextView storeNameIntroduce;
    private TextView storeName;
    private TextView store_manager_submit;
    private TextView restTimes;
    private TextView NestedScrollView2;
    private TextView hardwareRecyclerView;
    private TextView hardwareNotes;
    private TextView addressNotes;
    private TextView addressInfo;
    private TextView addNewLimit;

    @Override
    protected void InitView(View rootView) {
        workTimes = (TextView) rootView.findViewById(R.id.workTimes);
        workRecyclerView = (TextView) rootView.findViewById(R.id.workRecyclerView);
        updateStoreState = (TextView) rootView.findViewById(R.id.updateStoreState);
        updateStoreName = (TextView) rootView.findViewById(R.id.updateStoreName);
        updateHardwareNote = (TextView) rootView.findViewById(R.id.updateHardwareNote);
        updateAddressNote = (TextView) rootView.findViewById(R.id.updateAddressNote);
        updateAddress = (TextView) rootView.findViewById(R.id.updateAddress);
        textView61 = (TextView) rootView.findViewById(R.id.textView61);
        textView49 = (TextView) rootView.findViewById(R.id.textView49);
        textView2 = (TextView) rootView.findViewById(R.id.textView2);
        textView190 = (TextView) rootView.findViewById(R.id.textView190);
        textView185 = (TextView) rootView.findViewById(R.id.textView185);
        textView183 = (TextView) rootView.findViewById(R.id.textView183);
        textView182 = (TextView) rootView.findViewById(R.id.textView182);
        textView181 = (TextView) rootView.findViewById(R.id.textView181);
        textView180 = (TextView) rootView.findViewById(R.id.textView180);
        textView179 = (TextView) rootView.findViewById(R.id.textView179);
        textView178 = (TextView) rootView.findViewById(R.id.textView178);
        textView177 = (TextView) rootView.findViewById(R.id.textView177);
        textView172 = (TextView) rootView.findViewById(R.id.textView172);
        textView171 = (TextView) rootView.findViewById(R.id.textView171);
        textView165 = (TextView) rootView.findViewById(R.id.textView165);
        textView164 = (TextView) rootView.findViewById(R.id.textView164);
        textView163 = (TextView) rootView.findViewById(R.id.textView163);
        textView162 = (TextView) rootView.findViewById(R.id.textView162);
        textView161 = (TextView) rootView.findViewById(R.id.textView161);
        textView159 = (TextView) rootView.findViewById(R.id.textView159);
        textView1 = (TextView) rootView.findViewById(R.id.textView1);
        textView = (TextView) rootView.findViewById(R.id.textView);
        sumOfHardware = (TextView) rootView.findViewById(R.id.sumOfHardware);
        storeState = (TextView) rootView.findViewById(R.id.storeState);
        storeNameIntroduce = (TextView) rootView.findViewById(R.id.storeNameIntroduce);
        storeName = (TextView) rootView.findViewById(R.id.storeName);
        store_manager_submit = (TextView) rootView.findViewById(R.id.store_manager_submit);
        restTimes = (TextView) rootView.findViewById(R.id.restTimes);
        NestedScrollView2 = (TextView) rootView.findViewById(R.id.NestedScrollView2);
        hardwareRecyclerView = (TextView) rootView.findViewById(R.id.hardwareRecyclerView);
        hardwareNotes = (TextView) rootView.findViewById(R.id.hardwareNotes);
        addressNotes = (TextView) rootView.findViewById(R.id.addressNotes);
        addressInfo = (TextView) rootView.findViewById(R.id.addressInfo);
        addNewLimit = (TextView) rootView.findViewById(R.id.addNewLimit);

    }

    @Override
    protected void InitData(View rootView) {

    }

    @Override
    protected void InitOther(View rootView) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_store_manager_operate_part;
    }
}
