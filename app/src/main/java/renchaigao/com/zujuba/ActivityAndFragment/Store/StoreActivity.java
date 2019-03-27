package renchaigao.com.zujuba.ActivityAndFragment.Store;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.renchaigao.zujuba.PageBean.StoreActivityBean;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.store.BusinessPart.BusinessTimeInfo;
import com.renchaigao.zujuba.mongoDB.info.store.HardwarePart.Hardware;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonRecycleAdapter;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.BaseActivity;
import renchaigao.com.zujuba.ActivityAndFragment.Club.CreateClubActivity;
import renchaigao.com.zujuba.ActivityAndFragment.CustomViewPagerAdapter;
import renchaigao.com.zujuba.ActivityAndFragment.Store.Manager.StoreManagerActivity;
import renchaigao.com.zujuba.ActivityAndFragment.TeamPart.TeamCreateActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.StoreApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.PropertiesConfig;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;
import renchaigao.com.zujuba.widgets.CustomViewPager;

import static com.renchaigao.zujuba.PropertiesConfig.ConstantManagement.ADDRESS_CLASS_STORE;
import static com.renchaigao.zujuba.mongoDB.info.store.HardwarePart.Hardware.*;
import static com.renchaigao.zujuba.mongoDB.info.store.StoreInfo.*;

public class StoreActivity extends BaseActivity {

    final static String TAG = "StoreActivity";
    private Banner banner;
    private String userId, token, storeId;
    private int lastTime = 0;
    private CustomViewPager customViewPager;
    private RecyclerView recyclerView;
    private StoreActivityBean storeActivityBean = new StoreActivityBean();

    private TextView allPlayNum, storeStarsText, storeTeamNum, distance, storeClubNum, store_class, store_name, store_state,
            store_socre, storeEvaluateAllPeopleNum, storeSpendPerUser, store_address, store_address_note, store_note;
    private ImageView store_phone_image;
    private RatingBar storeStars;
    private TabLayout store_info_tablayout;
    private TextView titleTextView, secondTitleTextView;
    private ConstraintLayout toolbar;
    private LinearLayout store_hd_cold, store_hd_wifi, store_hd_hot, store_hd_charge, store_hd_wc;

    // Handler内部类，它的引用在子线程中被使用，发送mesage，被handlerMesage方法接收

    @Override
    protected void InitView() {
        initToolbar();
        customViewPager = (CustomViewPager) findViewById(R.id.store_customView);
        storeStarsText = (TextView) findViewById(R.id.store_page_ratingbar_text);
        store_name = (TextView) findViewById(R.id.store_info_name);
        store_state = (TextView) findViewById(R.id.store_info_state);
        distance = (TextView) findViewById(R.id.ti_text_distance);
        storeClubNum = (TextView) findViewById(R.id.textView149);
        storeTeamNum = (TextView) findViewById(R.id.textView148);
        store_socre = (TextView) findViewById(R.id.store_page_score);
        storeEvaluateAllPeopleNum = (TextView) findViewById(R.id.store_page_evaluation_people_number);
        storeSpendPerUser = (TextView) findViewById(R.id.store_page_spend);
        store_address = (TextView) findViewById(R.id.store_page_address_info);
        allPlayNum = (TextView) findViewById(R.id.ti_text_playtimes);
        store_address_note = (TextView) findViewById(R.id.store_address_note);
        store_note = (TextView) findViewById(R.id.a_store_info_note);
        store_class = (TextView) findViewById(R.id.store_class);
        banner = (Banner) findViewById(R.id.a_place_page_banner);
        storeStars = (RatingBar) findViewById(R.id.store_page_ratingbar);
        store_phone_image = (ImageView) findViewById(R.id.store_page_phone_imge);
        store_info_tablayout = (TabLayout) findViewById(R.id.store_info_tablayout);
        recyclerView = (RecyclerView) findViewById(R.id.store_info_openTimes);

        store_hd_cold = (LinearLayout) findViewById(R.id.store_hd_code);
        store_hd_wifi = (LinearLayout) findViewById(R.id.store_hd_wifi);
        store_hd_hot = (LinearLayout) findViewById(R.id.store_hd_hot);
        store_hd_charge = (LinearLayout) findViewById(R.id.store_hd_charge);
        store_hd_wc= (LinearLayout) findViewById(R.id.store_hd_wc);
        updateBasicView();
    }

    private void updateBasicView() {
        store_name.setText(storeActivityBean.getStoreName());
        store_socre.setText(storeActivityBean.getCommentScore());
        allPlayNum.setText(storeActivityBean.getSumOfPlayer());
        storeSpendPerUser.setText(storeActivityBean.getSpendMoney());
        distance.setText(storeActivityBean.getDistance());
        switch (storeActivityBean.getStoreClassInt()){
            case PLACE_CLASS_ZYB:
                store_phone_image.setVisibility(View.VISIBLE);
                break;
            case PLACE_CLASS_RESTAURANT:
                store_phone_image.setVisibility(View.VISIBLE);
                break;
            case PLACE_CLASS_HOMESTAY:
                store_phone_image.setVisibility(View.VISIBLE);
                break;
            case PLACE_CLASS_SCHOOL:
                store_phone_image.setVisibility(View.GONE);
                break;
            case PLACE_CLASS_SQUARE:
                store_phone_image.setVisibility(View.GONE);
                break;
            case PLACE_CLASS_COMMUNITY:
                store_phone_image.setVisibility(View.GONE);
                break;
        }
        store_class.setText(storeActivityBean.getStoreclass());
        store_note.setText(storeActivityBean.getPlaceinfo());
        storeClubNum.setText(storeActivityBean.getClubNum() + "个俱乐部");
        storeTeamNum.setText(storeActivityBean.getSumOfTeams() + "次组局");
        titleTextView.setText("组局吧");
        if (storeActivityBean.getIsCreater()) {
            store_state.setText(storeActivityBean.getState());
            secondTitleTextView.setVisibility(View.VISIBLE);
            secondTitleTextView.setText("管理本店");
            secondTitleTextView.setTextColor(Color.rgb(0xe9, 0x00, 0x06));
        } else {
            secondTitleTextView.setVisibility(View.GONE);
        }

        storeStarsText.setText(String.valueOf(storeActivityBean.getStar()) + "星场地");
        storeStars.setNumStars(storeActivityBean.getStar());

        storeEvaluateAllPeopleNum.setText(storeActivityBean.getCommentTimes());
        store_address.setText(storeActivityBean.getAddressAbstract());
        store_address_note.setText(storeActivityBean.getAddressNotes());
        for (Hardware h:storeActivityBean.getHardwareList()){
            switch (h.getHardwareClass()){
                case HARDWARE_WIFI:
                    store_hd_wifi.setVisibility(View.VISIBLE);
                    break;
                case HARDWARE_WC:
                    store_hd_wc.setVisibility(View.VISIBLE);
                    break;
                case HARDWARE_COLD:
                    store_hd_cold.setVisibility(View.VISIBLE);
                    break;
                case HARDWARE_HOT:
                    store_hd_hot.setVisibility(View.VISIBLE);
                    break;
                case HARDWARE_CHARGE:
                    store_hd_charge.setVisibility(View.VISIBLE);
                    break;
            }
        }

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            UpdateView();
        }
    };

    private void UpdateView() {
        updateBasicView();
        updateBanner();
        timeAdapter.updateResults(storeActivityBean.getBusinessTimeList());
        timeAdapter.notifyDataSetChanged();
    }

    @Override
    protected void InitData() {
        UserInfo userInfo = DataUtil.GetUserInfoData(this);
        userId = userInfo.getId();
        token = userInfo.getToken();
        storeId = getIntent().getStringExtra("placeId");
    }

    private AlertDialog.Builder alertDialog;
    private String[] placeNote = {"打电话给他/她", "给他/她发消息"};

    @Override
    protected void InitOther() {
        reloadAdapter();
        setRecyclerView();
        setBanner();
        initAddressPhonePart();
        setViewPager();
        setClick();
    }

    private void setClick() {
        secondTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreActivity.this, StoreManagerActivity.class);
                intent.putExtra("placeId", storeId);
                startActivity(intent);
            }
        });
    }

    private LinearLayoutManager layoutManager;
    private timeAdapter timeAdapter;

    private void setRecyclerView() {
        layoutManager = new LinearLayoutManager(StoreActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        timeAdapter = new timeAdapter(StoreActivity.this, storeActivityBean.getBusinessTimeList());
        recyclerView.setAdapter(timeAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void initAddressPhonePart() {
        store_phone_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog = new AlertDialog.Builder(StoreActivity.this);
                alertDialog.setTitle("联系场地负责人");
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.setItems(placeNote, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });
    }

    private void initToolbar() {
        toolbar = (ConstraintLayout) findViewById(R.id.store_info_toolbar);
        titleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        secondTitleTextView = (TextView) toolbar.findViewById(R.id.toolbarSecondTitle);
        ImageView goback = (ImageView) toolbar.findViewById(R.id.toolbarBack);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_store;
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadAdapter();
    }

    private void setBanner() {

        //放标题的集合
//        list_title = new ArrayList<>();
// 设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(1);//0:没有点点  1：中间有点点  2：右下角分数  3：下面一条黑，左边有标题，右边有分数 4：下面一条黑，左边有标题
//        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
//        banner.setImages(storeActivityBean.getImagePaths());
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
//        banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        banner.setDelayTime(5000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
//                .setOnBannerListener(new OnBannerListener() {
//                    @Override
//                    public void OnBannerClick(int position) {
//                        Log.e("StoreActivity", "click banner");
//                    }
//                })
        //必须最后调用的方法，启动轮播图。
//                .start();
    }

    private void updateBanner() {
        ArrayList<String> bannerPathList = new ArrayList<>();
        for (String s : storeActivityBean.getImagePaths()) {
            bannerPathList.add(PropertiesConfig.photoUrl + "showimage/" + s);
        }
        banner.setImages(bannerPathList)
                .start();
    }

    private void reloadAdapter() {
        addSubscribe(RetrofitServiceManager.getInstance().creat(StoreApiService.class)
                .GetOneStoreInfo("main", userId, storeId, token, lastTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(StoreActivity.this) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            Message msg = new Message();
                            switch (code) {
                                case RespCodeNumber.STORE_INFO_GET_SUCCESS:
                                    storeActivityBean = JSONObject.parseObject(responseJson.getJSONObject("data").toJSONString()
                                            , StoreActivityBean.class);
                                    handler.sendMessage(msg);
                                    break;
                                case RespCodeNumber.STORE_INFO_GET_FAIL:
                                    break;
                                case RespCodeNumber.STORE_NOT_FOUND:
                                    break;
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
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

    private void setViewPager() {
        final StoreTeamsFragment storeTeamsFragment = new StoreTeamsFragment();
        final StoreClubFragment storeClubFragment = new StoreClubFragment();
//        final StoreEvaluationFragment storeEvaluationFragment = new StoreEvaluationFragment();
//        final StoreGameFragment storeGameFragment = new StoreGameFragment();

        CustomViewPagerAdapter customViewPagerAdapter =
                new CustomViewPagerAdapter(getSupportFragmentManager());
        customViewPagerAdapter.addFragment(storeTeamsFragment);
        customViewPagerAdapter.addFragment(storeClubFragment);
//        customViewPagerAdapter.addFragment(storeEvaluationFragment);
//        customViewPagerAdapter.addFragment(storeGameFragment);
        customViewPager.setAdapter(customViewPagerAdapter);
        customViewPager.setCurrentItem(0);
        store_info_tablayout.setupWithViewPager(customViewPager);
        store_info_tablayout.getTabAt(0).setCustomView(tab_icon("组局"));
        store_info_tablayout.getTabAt(1).setCustomView(tab_icon("俱乐部"));
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

    public void createNewTeam(View view) {
        Intent intent = new Intent(StoreActivity.this, TeamCreateActivity.class);
        intent.putExtra("placeId", storeId);
        intent.putExtra("placeClass", ADDRESS_CLASS_STORE);
        startActivity(intent);
    }

    public void createNewClub(View view) {
        Intent intent = new Intent(StoreActivity.this, CreateClubActivity.class);
        intent.putExtra("placeId", storeId);
        intent.putExtra("placeClass", ADDRESS_CLASS_STORE);
        startActivity(intent);
    }

    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }

    private class timeAdapter extends CommonRecycleAdapter<BusinessTimeInfo> {

        public timeAdapter(Context context, ArrayList<BusinessTimeInfo> dataList) {
            super(context, dataList, R.layout.card_store_activity_business_time_list);
        }

        @Override
        public void bindData(CommonViewHolder holder, BusinessTimeInfo data) {
            holder.setText(R.id.textView169, data.getStartTime())
                    .setText(R.id.textView170, data.getEndTime());
        }
    }
}
