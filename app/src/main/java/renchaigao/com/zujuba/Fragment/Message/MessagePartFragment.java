package renchaigao.com.zujuba.Fragment.Message;

import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.PageBean.CardMessageFragmentTipBean;
import com.renchaigao.zujuba.PageBean.MessageFragmentCardBean;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.Bean.AndroidCardMessageFragmentTipBean;
import renchaigao.com.zujuba.Fragment.BaseFragment;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.MessageApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;
import renchaigao.com.zujuba.widgets.CustomViewPager;


public class MessagePartFragment extends BaseFragment {

    private TabLayout tabLayout;
    private int tabSelecte = 0;

    @Override
    protected void InitView(View rootView) {
        tabLayout = (TabLayout) rootView.findViewById(R.id.f_message_tabLayout);
        tabLayout.getTabAt(0).select();
        setViewPager(rootView);
    }


    private UserInfo userInfo;

    @Override
    protected void InitData(View rootView) {
//        userInfo = DataUtil.GetUserInfoData(mContext);
    }

    @Override
    protected void InitOther(View rootView) {
//        askMessageInfo();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    private void setViewPager(View view) {

        ViewPager customViewPager = (ViewPager) view.findViewById(R.id.fragment_message_customView);
        final ClubFragment clubFragment = new ClubFragment();
        final MessageListFragment messageListFragment = new MessageListFragment();
        CustomViewPagerAdapter customViewPagerAdapter = new CustomViewPagerAdapter(getChildFragmentManager());
        customViewPagerAdapter.addFragment(messageListFragment);
        customViewPagerAdapter.addFragment(clubFragment);
        customViewPager.setAdapter(customViewPagerAdapter);
        customViewPager.setCurrentItem(0);
        customViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        tabLayout.setupWithViewPager(customViewPager);
        tabLayout.getTabAt(0).setText("消息");
        tabLayout.getTabAt(1).setText("俱乐部");

    }

    static class CustomViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();

        CustomViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        void addFragment(Fragment fragment) {
            mFragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

    }


}
