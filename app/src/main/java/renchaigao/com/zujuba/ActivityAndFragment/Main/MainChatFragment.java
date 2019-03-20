package renchaigao.com.zujuba.ActivityAndFragment.Main;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.util.ArrayList;
import java.util.List;

import renchaigao.com.zujuba.ActivityAndFragment.BaseFragment;
import renchaigao.com.zujuba.R;


public class MainChatFragment extends BaseFragment {

    private static String TAG = "MainChatFragment";
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
        final MainClubFragment mainClubFragment = new MainClubFragment();
        final MainMessageFragment mainMessageFragment = new MainMessageFragment();
        CustomViewPagerAdapter customViewPagerAdapter = new CustomViewPagerAdapter(getChildFragmentManager());
        customViewPagerAdapter.addFragment(mainMessageFragment);
        customViewPagerAdapter.addFragment(mainClubFragment);
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
