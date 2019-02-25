package renchaigao.com.zujuba.Activity.Place;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import renchaigao.com.zujuba.Activity.BaseActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.PropertiesConfig;
import renchaigao.com.zujuba.util.http.ApiService;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;
import renchaigao.com.zujuba.widgets.CustomViewPager;

public class UserPlaceManagerActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {


    private static String TAG = "UserPlaceManagerActivity";

    private TextView place_name, todayTeamNum, weekTeamNum, allTeamNum, todayTeamMore, weekTeamMore, allTeamMore, todayTeamMore0, weekTeamMore0, allTeamMore0;
    private TextView place_state, time1, time2, time3, time4, mapDetail, mapNote, allPeopleNum, allDeskNum, placeInfoMore;
    private Button addDesk, cancleEdit, submitEdit, reloadMapInfo;
    private ImageView goBack, setting, time1Cancle, time2Cancle, time3Cancle, time4Cancle, time1Add, time2Add, time3Add, placeInfoMoreCancle;
    private Switch placeStateSwitch;
    private TabLayout tabLayout1;
    private CustomViewPager customViewPager;
    private AlertDialog.Builder builder;


    @Override
    protected void InitView() {
        place_name = findViewById(R.id.textView60);
        goBack = findViewById(R.id.imageView12);
        setting = findViewById(R.id.imageView13);
        tabLayout1 = findViewById(R.id.tabLayout1);
    }

    JSONObject jsonObjectIntent = new JSONObject();
    JSONObject jsonToFragment = new JSONObject();

    UserInfo userInfo = new UserInfo();

    @Override
    protected void InitData() {
        userInfo = DataUtil.GetUserInfoData(this);
        jsonToFragment = JSONObject.parseObject(getIntent().getStringExtra("storeinfo"));
    }

    @Override
    protected void InitOther() {
        setViewPager();
//        reloadAdapter();
    }

    private void setViewPager() {
        String[] title = {"基础", "组局", "运营", "评论"};
        customViewPager = findViewById(R.id.CustomViewPager1);
        final UserPlaceManagerBasicPartFragment userPlaceManagerBasicPartFragment = new UserPlaceManagerBasicPartFragment();
        final UserPlaceManagerTeamPartFragment userPlaceManagerTeamPartFragment = new UserPlaceManagerTeamPartFragment();
        final UserPlaceManagerOperationPartFragment userPlaceManagerOperationPartFragment = new UserPlaceManagerOperationPartFragment();
        CustomViewPagerAdapter customViewPagerAdapter = new CustomViewPagerAdapter(getSupportFragmentManager());
        customViewPagerAdapter.addFragment(userPlaceManagerBasicPartFragment);
        customViewPagerAdapter.addFragment(userPlaceManagerTeamPartFragment);
        customViewPagerAdapter.addFragment(userPlaceManagerOperationPartFragment);
        customViewPager.setAdapter(customViewPagerAdapter);
        tabLayout1.setupWithViewPager(customViewPager);
        for (int i = 0; i < customViewPagerAdapter.getCount(); i++) {
            TabLayout.Tab tab = tabLayout1.getTabAt(i);//获得每一个tab
            tab.setCustomView(R.layout.tab_item);//给每一个tab设置view
            if (i == 0) {
                // 设置第一个tab的TextView是被选择的样式
                tab.getCustomView().findViewById(R.id.tab_text).setSelected(true);//第一个tab被选中
            }
            TextView textView = (TextView) tab.getCustomView().findViewById(R.id.tab_text);
            textView.setText(title[i]);//设置tab上的文字
        }
        tabLayout1.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.tab_text).setSelected(true);
                customViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.tab_text).setSelected(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//        tabLayout1.getTabAt(0).setCustomView(partOne);
//        tabLayout1.getTabAt(1).setCustomView(partTwo);
//        tabLayout1.getTabAt(2).setCustomView(partThree);
//        partOne.setSelected(true);

//        customViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                switch (position) {
//                    case 0:
//                        break;
//                    case 1:
//                        break;
//                    case 2:
//                        break;
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_place_manager;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        customViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public JSONObject toFragementData() {
//        jsonToFragment.put("placeid",)
        return jsonToFragment;
    }

    private void reloadAdapter() {
        RetrofitServiceManager.getInstance().SetRetrofit(PropertiesConfig.placeServerUrl);
        RequestBody multiBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("json", "")
                .build();
        addSubscribe(RetrofitServiceManager.getInstance().creat(ApiService.class)
                .FourParameterBodyPost("user",
                        "one",
                        userInfo.getId(),
                        jsonObjectIntent.getString("placeid"),
                        multiBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(this) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            JSONObject responseJsonData = responseJson.getJSONObject("data");
                            switch (code) {
                                case RespCodeNumber.SUCCESS:
                                    jsonToFragment = responseJsonData;
                                    break;
                            }
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    protected void onSuccess(ResponseEntity responseEntity) {

                    }


                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.e(TAG, "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete");
                    }
                }));
    }


    static class CustomViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();

        public CustomViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment) {
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
