package renchaigao.com.zujuba.Activity;

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
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import renchaigao.com.zujuba.Activity.User.UserSettingActivity;
import renchaigao.com.zujuba.Fragment.GameFragment;
import renchaigao.com.zujuba.Fragment.HallFragment;
import renchaigao.com.zujuba.Fragment.Message.MessagePartFragment;
import renchaigao.com.zujuba.Fragment.MineFragment;
import renchaigao.com.zujuba.Fragment.TeamFragment;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.UserApiService;
import renchaigao.com.zujuba.util.BottomNavigationViewHelper;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;
import renchaigao.com.zujuba.widgets.CustomViewPager;

public class MainActivity extends BaseActivity {
    protected static String TAG = "MainActivity";
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private CustomViewPager customViewPager;
    private AlertDialog.Builder builder;
    private User user;
    private UserInfo userInfo;
    private String userPlace;
    private ConstraintLayout header_layout, a_main_toolbar_constraintlayout;
    private ImageView a_main_toolbar_usericon;
    private TextView a_main_toolbar_location_text;
    private Toolbar toolbar;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
////        SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
////        initData();
////        Log.e(TAG, pref.getString("token", "fail find"));
////        drawerLayout = findViewById(R.id.main_drawerLayout);
////        setNavigationView();
////        setLocationPart();
//
////        getLocation();
//    }

    // Handler内部类，它的引用在子线程中被使用，发送mesage，被handlerMesage方法接收
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            String str = (String) msg.obj;
            Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
        }


    };

    private void setToolBar() {
        toolbar = findViewById(R.id.a_main_Toolbar);
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
    protected void InitView() {
        setToolBar();
//        a_main_toolbar_location_text = findViewById(R.id.a_main_toolbar_location_text);
//        a_main_toolbar_usericon = findViewById(R.id.a_main_toolbar_usericon);
        customViewPager = findViewById(R.id.main_customView);
//        a_main_toolbar_constraintlayout = findViewById(R.id.a_main_toolbar_constraintlayout);
        bottomNavigationView = findViewById(R.id.main_bootomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_message:
//                        item.setIcon(R.drawable.message_select);
                        customViewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_team:
                        item.setIcon(R.drawable.team_select);
                        customViewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_dating:
                        item.setIcon(R.drawable.home_select);
                        customViewPager.setCurrentItem(2);
                        return true;
                    case R.id.navigation_game:
                        item.setIcon(R.drawable.game_select);
                        customViewPager.setCurrentItem(3);
                        return true;
                    case R.id.navigation_mine:
                        item.setIcon(R.drawable.message_select);
                        customViewPager.setCurrentItem(4);
                        return true;
                }
                return false;
            }
        });
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
    }

    @Override
    protected void InitData() {
        userInfo = DataUtil.GetUserInfoData(MainActivity.this);
        user = DataUtil.GetUserData(MainActivity.this);
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

    private LocationManager locationManager;
    private String locationProvider;

    private void getLocation() {
        locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);//低精度，如果设置为高精度，依然获取不了location。
        criteria.setAltitudeRequired(false);//不要求海拔
        criteria.setBearingRequired(false);//不要求方位
        criteria.setCostAllowed(true);//允许有花费
        criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗
        //从可用的位置提供器中，匹配以上标准的最佳提供器
        locationProvider = locationManager.getBestProvider(criteria, true);
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

    private LinearLayout nva_1;

//    private void setNavigationView() {
////        navigationView = findViewById(R.id.main_navigationView);
//
//        View headview = navigationView.inflateHeaderView(R.layout.left_page_layout);
////        Menu  menu =navigationView.getMenu();
////        MenuItem menuItem = menu.findItem(R.id.nav_person);
////        View actionView = menuItem.getActionView();
////        nva_1 = (LinearLayout)actionView;
//        nva_1 = (LinearLayout) navigationView.getMenu().findItem(R.id.nav_person).getActionView();
//        TextView textView = (TextView) nva_1.findViewById(R.id.menu_use_text);
//        textView.setText("1");
//
//        header_layout = headview.findViewById(R.id.navigation_header_layout);
//        header_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, UserActivity.class);
//                startActivity(intent);
//            }
//        });
//    }


    private void setViewPager() {

        final CustomViewPager customViewPager = findViewById(R.id.main_customView);
        final HallFragment hallFragment = new HallFragment();
        final GameFragment gameFragment = new GameFragment();
        final MessagePartFragment messagePartFragment = new MessagePartFragment();
        final MineFragment mineFragment = new MineFragment();
        final TeamFragment teamFragment = new TeamFragment();

        CustomViewPagerAdapter customViewPagerAdapter =
                new CustomViewPagerAdapter(getSupportFragmentManager());

        customViewPagerAdapter.addFragment(messagePartFragment);
        customViewPagerAdapter.addFragment(teamFragment);
        customViewPagerAdapter.addFragment(hallFragment);
        customViewPagerAdapter.addFragment(gameFragment);
        customViewPagerAdapter.addFragment(mineFragment);

        customViewPager.setAdapter(customViewPagerAdapter);
        customViewPager.setCurrentItem(2);

        bottomNavigationView.setSelectedItemId(R.id.navigation_dating);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.navigation_message:
//                        //在这里替换图标
//                        item.setIcon(R.mipmap.ic_home_selected);
//                        return true;
//                }
//                return false;
//            }
//        });
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
                        bottomNavigationView.setSelectedItemId(R.id.navigation_message);
                        break;
                    case 1:
                        toolbar.setVisibility(View.VISIBLE);
                        bottomNavigationView.setSelectedItemId(R.id.navigation_team);
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
                        bottomNavigationView.setSelectedItemId(R.id.navigation_dating);
                        break;
                    case 3:
                        toolbar.setVisibility(View.VISIBLE);
                        bottomNavigationView.setSelectedItemId(R.id.navigation_game);
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
//                        a_main_toolbar_constraintlayout.setVisibility(View.VISIBLE);
//                        a_main_toolbar_constraintlayout.setVisibility(View.GONE);
                        bottomNavigationView.setSelectedItemId(R.id.navigation_mine);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        navigationView.setFocusable(true);

//        initUserImageView();

//        hallFragment.reloadAdapter();
    }

//    private void initBottomNavigationView(final CustomViewPager customViewPager) {
//        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//                = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.navigation_message:
//                        customViewPager.setCurrentItem(0);
//                        return true;
//                    case R.id.navigation_team:
//                        customViewPager.setCurrentItem(1);
//                        return true;
//                    case R.id.navigation_dating:
//                        customViewPager.setCurrentItem(2);
//                        return true;
//                    case R.id.navigation_game:
//                        customViewPager.setCurrentItem(3);
//                        return true;
//                    case R.id.navigation_mine:
//                        customViewPager.setCurrentItem(4);
//                        return true;
//                }
//                return true;
//            }
//        };
//        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//    }

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

//    private void initUserImageView(){
//        main__user_icon = findViewById(R.id.main__user_icon);
//        main__user_icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                navigationView.setClickable(true);
//            }
//        });
//    }

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

//     private void sendAddressToService1() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String path = PropertiesConfig.userServerUrl + "info/update/address";
//                OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                        .connectTimeout(15, TimeUnit.SECONDS)
//                        .readTimeout(15, TimeUnit.SECONDS)
//                        .writeTimeout(15, TimeUnit.SECONDS)
//                        .retryOnConnectionFailure(true);
//                builder.sslSocketFactory(OkhttpFunc.createSSLSocketFactory());
//                builder.hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                });
//                String str = JSONObject.toJSONString(userInfo);
//                final RequestBody body = RequestBody.create(FinalDefine.MEDIA_TYPE_JSON, JSONObject.toJSONString(userInfo));
//                final Request request = new Request.Builder()
//                        .url(path)
//                        .header("Content-Type", "application/json")
//                        .post(body)
//                        .build();
//                builder.build().newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        Log.e(TAG, call.request().body().toString());
//                        Message msg = new Message();
//                        msg.obj = "发送失败，e";
//                        // 把消息发送到主线程，在主线程里现实Toast
//                        handler.sendMessage(msg);
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        try {
//                            JSONObject responseJson = JSONObject.parseObject(response.body().string());
//                            int code = Integer.valueOf(responseJson.get("code").toString());
//                            JSONObject responseJsonData = (JSONObject) responseJson.getJSONObject("data");
//                            switch (code) {
//                                case 0:
//                                    DataUtil.SaveUserInfoData(MainActivity.this, JSONObject.toJSONString(responseJsonData));
////                                    UserInfo userTestINFO = DataUtil.GetUserInfoData(MainActivity.this);
//
//                                    Message msg = new Message();
//                                    msg.obj = "地址更新成功";
//                                    // 把消息发送到主线程，在主线程里现实Toast
//                                    handler.sendMessage(msg);
//                                    break;
//                            }
//                        } catch (Exception e) {
//                            Log.e(TAG, e.toString());
//                        }
//                    }
//                });
//
//            }
//        }).start();
//    }


    private void sendAddressToService() {
        addSubscribe(RetrofitServiceManager.getInstance().creat(UserApiService.class)
                .FourParameterJsonPost(
                        "update", "addressInfo", userInfo.getId(), "null",
                        JSONObject.parseObject(JSONObject.toJSONString(userInfo.getAddressInfo()), JSONObject.class))
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
