package renchaigao.com.zujuba.ActivityAndFragment.Main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.dao.User;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.AddressInfo;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.ActivityAndFragment.BaseActivity;
import renchaigao.com.zujuba.ActivityAndFragment.Function.GaoDeMapActivity;
import renchaigao.com.zujuba.ActivityAndFragment.TeamPart.TeamCreateActivity;
import renchaigao.com.zujuba.ActivityAndFragment.User.UserSettingActivity;
import renchaigao.com.zujuba.ActivityAndFragment.Game.GameFragment;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.UserApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;
import renchaigao.com.zujuba.widgets.CustomViewPager;

import static com.renchaigao.zujuba.PropertiesConfig.UserConstant.USER_UPDATE_INFO_CLASS_ADDRESS;

public class MainActivity extends BaseActivity {
    protected static String TAG = "MainActivity";
    private DrawerLayout drawerLayout;
    private CustomViewPager customViewPager;
    private AlertDialog.Builder builder;
    private User user;
    private UserInfo userInfo;
    private String userPlace, userId, token;
    private Toolbar toolbar;
    private TabLayout bottom_tableLayout;
    TabItem tab1;

    // Handler内部类，它的引用在子线程中被使用，发送mesage，被handlerMesage方法接收
    @Override
    protected void InitView() {
        customViewPager = (CustomViewPager) findViewById(R.id.main_customView);
        bottom_tableLayout = (TabLayout) findViewById(R.id.bottom_tableLayout);
//        tab1 = findViewById(R.id.)
        setToolBar();

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            String str = (String) msg.obj;
            Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
        }


    };

    private void setToolBar() {
        toolbar = (Toolbar) findViewById(R.id.a_main_Toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("组局吧");
        toolbar.inflateMenu(R.menu.main_toolbar_hall);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_location:
                        break;
                    case R.id.item_location_name:
                        break;
                }
                Intent intent = new Intent(MainActivity.this, GaoDeMapActivity.class);
                intent.putExtra("whereCome", "MainActivity");
                startActivityForResult(intent, MAIN_ADDRESS);
                return false;
            }
        });
    }

    @Override
    protected void InitData() {
        userInfo = DataUtil.GetUserInfoData(MainActivity.this);
        user = DataUtil.GetUserData(MainActivity.this);
        userId = userInfo.getId();
        token = userInfo.getToken();
    }

    @Override
    protected void InitOther() {
        setViewPager();
        setLocationPart();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MAIN_ADDRESS://是地图api返回的；
                AddressInfo addressUse = JSONObject.parseObject(data.getStringExtra("addressStoreJsonStr"), AddressInfo.class);
                userInfo.setAddressInfo(addressUse);
                userInfo.getAddressInfo().setSelectCityCode(addressUse.getCitycode());
                sendAddressToService();
                //系统需要同步更新一次用户的位置信息；
//                userPlace = addressUse.getFormatAddress();
                break;
        }
    }

    public static final int MAIN_ADDRESS = 1001;

    private void setLocationPart() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("当前默认深圳为“深圳”");
        builder.setMessage("是否进行切换");
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getLocation();
            }
        });
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, GaoDeMapActivity.class);
                intent.putExtra("whereCome", "MainActivity");
                startActivityForResult(intent, MAIN_ADDRESS);
            }
        });
        if (userInfo != null) {
            if (userInfo.getAddressInfo() == null) {
                builder.show();
            } else {
                if (userInfo.getAddressInfo().getLatitude() == null || userInfo.getAddressInfo().getLongitude() == null) {
                    builder.show();
                }
            }
        }

    }

    private void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);//低精度，如果设置为高精度，依然获取不了location。
        criteria.setAltitudeRequired(false);//不要求海拔
        criteria.setBearingRequired(false);//不要求方位
        criteria.setCostAllowed(true);//允许有花费
        criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗
        //从可用的位置提供器中，匹配以上标准的最佳提供器
        String locationProvider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onCreate: 没有权限 ");
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        userInfo.setAddressInfo(new AddressInfo());
        userInfo.getAddressInfo().setId(userInfo.getId());
        userInfo.getAddressInfo().setOwnerId(userInfo.getId());
        userInfo.getAddressInfo().setAddressClass("user");
        userInfo.getAddressInfo().setCitycode("0755");
        userInfo.getAddressInfo().setSelectCityCode("0755");
        userInfo.getAddressInfo().setLatitude(location.getLatitude());
        userInfo.getAddressInfo().setLongitude(location.getLongitude());

        Log.d(TAG, "onCreate: " + (location == null) + "..");
        if (location != null) {
            Log.d(TAG, "onCreate: location");
            //不为空,显示地理位置经纬度
            sendAddressToService();
            Log.d(TAG, "定位成功------->" + "location------>经度为：" + location.getLatitude() + "\n纬度为" + location.getLongitude());
        }
    }

    private void setViewPager() {

        final MainStoreFragment mainStoreFragment = new MainStoreFragment();
        final GameFragment gameFragment = new GameFragment();
        final MainChatFragment mainChatFragment = new MainChatFragment();
        final MainMineFragment mainMineFragment = new MainMineFragment();
        final MainTeamFragment mainTeamFragment = new MainTeamFragment();

        CustomViewPagerAdapter customViewPagerAdapter =
                new CustomViewPagerAdapter(getSupportFragmentManager());

        customViewPagerAdapter.addFragment(mainChatFragment);
        customViewPagerAdapter.addFragment(mainTeamFragment);
        customViewPagerAdapter.addFragment(mainStoreFragment);
        customViewPagerAdapter.addFragment(gameFragment);
        customViewPagerAdapter.addFragment(mainMineFragment);

        customViewPager.setAdapter(customViewPagerAdapter);
        customViewPager.setCurrentItem(2);
        customViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Menu menu = toolbar.getMenu();
                menu.clear();
                switch (position) {
                    case 0:
                        toolbar.setVisibility(View.GONE);
                        break;
                    case 1:
                        toolbar.setVisibility(View.VISIBLE);
                        toolbar.inflateMenu(R.menu.main_toolbar_team);
                        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                final Intent intent = new Intent(MainActivity.this, TeamCreateActivity.class);
                                startActivity(intent);
                                return false;
                            }
                        });
                        break;
                    case 2:
                        toolbar.setVisibility(View.VISIBLE);
                        toolbar.inflateMenu(R.menu.main_toolbar_hall);
                        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.item_location:
                                        break;
                                    case R.id.item_location_name:
                                        break;
                                }
                                Intent intent = new Intent(MainActivity.this, GaoDeMapActivity.class);
                                intent.putExtra("whereCome", "MainActivity");
                                startActivityForResult(intent, MAIN_ADDRESS);
                                return false;
                            }
                        });
                        break;
                    case 3:
                        toolbar.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        toolbar.setVisibility(View.VISIBLE);
                        toolbar.inflateMenu(R.menu.main_toolbar_mine);
                        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Intent intent = new Intent(MainActivity.this, UserSettingActivity.class);
                                startActivity(intent);
                                return false;
                            }
                        });
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottom_tableLayout.setupWithViewPager(customViewPager);
        View tab1 = LayoutInflater.from(this).inflate(R.layout.widget_bottom_item, null);

        bottom_tableLayout.getTabAt(0).setCustomView(tab_icon("聊天", R.drawable.bottom_item_message_select));
        bottom_tableLayout.getTabAt(1).setCustomView(tab_icon("组局", R.drawable.bottom_item_team_select));
        bottom_tableLayout.getTabAt(2).setCustomView(tab_icon("大厅", R.drawable.bottom_item_home_select));
        bottom_tableLayout.getTabAt(3).setCustomView(tab_icon("游戏", R.drawable.bottom_item_game_select));
        bottom_tableLayout.getTabAt(4).setCustomView(tab_icon("我的", R.drawable.bottom_item_mine_select));
//        bottom_tableLayout.getTabAt(0).setText("消息").setIcon(R.drawable.bottom_item_message_select);
//        bottom_tableLayout.getTabAt(1).setText("组局").setIcon(R.drawable.bottom_item_team_select);
//        bottom_tableLayout.getTabAt(2).setText("大厅").setIcon(R.drawable.bottom_item_home_select);
//        bottom_tableLayout.getTabAt(3).setText("游戏").setIcon(R.drawable.bottom_item_game_select);
//        bottom_tableLayout.getTabAt(4).setText("我的").setIcon(R.drawable.bottom_item_mine_select);

    }

    private View tab_icon(String name, int iconID) {
        View newtab = LayoutInflater.from(this).inflate(R.layout.widget_bottom_item, null);
        TextView tv = (TextView) newtab.findViewById(R.id.widget_bottom_item_title);
        tv.setText(name);
        ImageView im = (ImageView) newtab.findViewById(R.id.widget_bottom_item_image);
        im.setImageResource(iconID);
        return newtab;
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

    /**
     * 双击返回桌面
     */
    private long time = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - time > 1000)) {
                Toast.makeText(this, "再按一次返回桌面", Toast.LENGTH_SHORT).show();
                time = System.currentTimeMillis();
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }


    private void sendAddressToService() {
        addSubscribe(
                RetrofitServiceManager.getInstance().creat(UserApiService.class)
                        .UpdateUserInfo(USER_UPDATE_INFO_CLASS_ADDRESS
                                , userId
                                , token
                                , JSONObject.parseObject(JSONObject.toJSONString(userInfo.getAddressInfo())))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new BaseObserver<ResponseEntity>(this) {

                            @Override
                            protected void onSuccess(ResponseEntity responseEntity) {

                            }

                            @Override
                            public void onNext(ResponseEntity value) {
                                try {
                                    JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                                    int code = Integer.valueOf(responseJson.get("code").toString());
                                    JSONObject responseJsonData;
                                    switch (code) {
                                        case RespCodeNumber.SUCCESS://
                                            responseJsonData = responseJson.getJSONObject("data");
                                            String userInfoString = JSONObject.toJSONString(responseJsonData);
                                            DataUtil.SaveUserInfoData(MainActivity.this, userInfoString);
                                            Message msg = new Message();
                                            msg.obj = "地址更新成功";
                                            // 把消息发送到主线程，在主线程里现实Toast
                                            handler.sendMessage(msg);
                                            break;
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, e.toString());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onComplete() {
                                Log.e(TAG, "onComplete:");
                            }
                        }));
    }

}
