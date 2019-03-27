package renchaigao.com.zujuba.ActivityAndFragment.Store.Manager;


import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import renchaigao.com.zujuba.ActivityAndFragment.BaseActivity;
import renchaigao.com.zujuba.ActivityAndFragment.CustomViewPagerAdapter;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.widgets.CustomViewPager;

public class StoreManagerActivity extends BaseActivity {

    private TextView titleTextView, secondTitleTextView;
    private ConstraintLayout toolbar;
    private String userId, token, storeId;
    private CustomViewPager customViewPager;
    private TabLayout tab;

    private void initToolbar() {
        toolbar = (ConstraintLayout) findViewById(R.id.place_manager_toolbar);
        titleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        secondTitleTextView = (TextView) toolbar.findViewById(R.id.toolbarSecondTitle);
        ImageView goback = (ImageView) toolbar.findViewById(R.id.toolbarBack);
        titleTextView.setText("店铺管理");
        secondTitleTextView.setText("");
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void InitView() {
        initToolbar();
        customViewPager = (CustomViewPager) findViewById(R.id.store_manager_custom);
        tab = (TabLayout) findViewById(R.id.store_manager_TabLayout);
    }

    @Override
    protected void InitData() {
        UserInfo userInfo = DataUtil.GetUserInfoData(this);
        userId = userInfo.getId();
        token = userInfo.getToken();
        storeId = getIntent().getStringExtra("placeId");
    }

    @Override
    protected void InitOther() {
        setViewPager();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_store_manager;
    }

    private void setViewPager() {
        final StoreManagerBasicFragment storeManagerBasicFragment = new StoreManagerBasicFragment();
//        final StoreManageOperateFragment storeManageOperateFragment = new StoreManageOperateFragment();
        CustomViewPagerAdapter customViewPagerAdapter =
                new CustomViewPagerAdapter(getSupportFragmentManager());
        customViewPagerAdapter.addFragment(storeManagerBasicFragment);
//        customViewPagerAdapter.addFragment(storeManageOperateFragment);

        customViewPager.setAdapter(customViewPagerAdapter);
        customViewPager.setCurrentItem(0);
        tab.setupWithViewPager(customViewPager);
        tab.getTabAt(0).setCustomView(tab_icon("基础"));
//        tab.getTabAt(1).setCustomView(tab_icon("运营"));
//        store_info_tablayout.getTabAt(2).setCustomView(tab_icon("评价"));
    }

    private View tab_icon(String name) {
        View newtab = LayoutInflater.from(this).inflate(R.layout.widget_tablayout_text_item, null);
        TextView tv = (TextView) newtab.findViewById(R.id.widget_tablyout_text_item_title);
        tv.setText(name);
        tv.setTextSize(20);
        tv.setTextColor(Color.rgb(0, 0, 0));
        return newtab;
    }
}
