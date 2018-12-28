package renchaigao.com.zujuba.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.dao.Address;
import com.renchaigao.zujuba.dao.User;
import com.renchaigao.zujuba.mongoDB.info.AddressInfo;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import renchaigao.com.zujuba.Fragment.GameFragment;
import renchaigao.com.zujuba.Fragment.HallFragment;
import renchaigao.com.zujuba.Fragment.MessageFragment;
import renchaigao.com.zujuba.Fragment.ShopFragment;
import renchaigao.com.zujuba.Fragment.TeamFragment;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.BottomNavigationViewHelper;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.FinalDefine;
import renchaigao.com.zujuba.util.OkhttpFunc;
import renchaigao.com.zujuba.util.PropertiesConfig;
import renchaigao.com.zujuba.widgets.CustomViewPager;

import static renchaigao.com.zujuba.Activity.GaoDeMapActivity.GAODE_MAP;

public class MainActivity extends AppCompatActivity {

    protected static String TAG = "MainActivity";
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private CustomViewPager customViewPager;

    private AlertDialog.Builder builder;
    private User user;
    private UserInfo userInfo;
    private String userPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
        initData();
        Log.e(TAG, pref.getString("token", "fail find"));
//        Permission.selfPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION, getApplicationContext())){
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                    1);
//                    /*Constant.LOCATION_STATE 为自己定义的一个常量，为权限弹窗回调时使用*/
//        }
        drawerLayout = findViewById(R.id.main_drawerLayout);
        customViewPager = findViewById(R.id.main_customView);
        bottomNavigationView = findViewById(R.id.main_bootomNavigationView);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        setNavigationView();
        setToolBar();
        setViewPager();
        setUpDrawer();
        setLocationPart();
//        getLocation();
    }

    // Handler内部类，它的引用在子线程中被使用，发送mesage，被handlerMesage方法接收
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            String str = (String) msg.obj;
            Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
        }

        ;
    };

    private void initData() {
        userInfo = DataUtil.getUserInfoData(MainActivity.this);
        user = DataUtil.getUserData(MainActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MAIN_ADDRESS://是地图api返回的；
                AddressInfo addressUse = JSONObject.parseObject(data.getStringExtra("addressStoreJsonStr"), AddressInfo.class);
                userInfo.setMyAddressInfo(addressUse);
                user = userInfo;
                DataUtil.saveUserInfoData(MainActivity.this, userInfo);
                DataUtil.saveUserData(MainActivity.this, user);
                sendAddressToService();
                //系统需要同步更新一次用户的位置信息；
//                userPlace = addressUse.getFormatAddress();
                break;
        }
    }

    public static final int MAIN_ADDRESS = 1001;

    private void setLocationPart() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("当前默认位置为“深圳”");
        builder.setMessage("是否实际定位(获取所在地的真实市场信息)");
        builder.setNegativeButton("否", null);
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, GaoDeMapActivity.class);
                intent.putExtra("whereCome", "MainActivity");
                startActivityForResult(intent, MAIN_ADDRESS);
            }
        });
        if (userInfo != null) {
            if (userInfo.getMyAddressInfo() == null) {

                builder.show();
            } else {
                if (userInfo.getMyAddressInfo().getLatitude() == null || userInfo.getMyAddressInfo().getLongitude() == null) {
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

        Log.d(TAG, "onCreate: " + (location == null) + "..");
        if (location != null) {
            Log.d(TAG, "onCreate: location");
            //不为空,显示地理位置经纬度
            Log.d(TAG, "定位成功------->" + "location------>经度为：" + location.getLatitude() + "\n纬度为" + location.getLongitude());
        }
    }

    private LinearLayout nva_1;

    private void setNavigationView() {
        navigationView = findViewById(R.id.main_navigationView);
//        Menu  menu =navigationView.getMenu();
//        MenuItem menuItem = menu.findItem(R.id.nav_person);
//        View actionView = menuItem.getActionView();
//        nva_1 = (LinearLayout)actionView;
        nva_1 = (LinearLayout) navigationView.getMenu().findItem(R.id.nav_person).getActionView();
        TextView textView = (TextView) nva_1.findViewById(R.id.menu_use_text);
        textView.setText("1");
    }

    private void updateSystemData() {
        updateUserData();
        updateTeamData();
        updateBusinessData();
        updateGameData();
    }

    private void updateUserData() {

    }

    private void updateTeamData() {

    }

    private void updateBusinessData() {

    }

    private void updateGameData() {

    }

    private void setToolBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void setUpDrawer() {
//        navigationView.setCheckedItem(R.id.nav_call);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                drawerLayout.closeDrawers();
//                return true;
//            }
//        });
    }

    private void setViewPager() {
        final CustomViewPager customViewPager = findViewById(R.id.main_customView);
        final HallFragment hallFragment = new HallFragment();
        final GameFragment gameFragment = new GameFragment();
        final MessageFragment messageFragment = new MessageFragment();
        final ShopFragment shopFragment = new ShopFragment();
        final TeamFragment teamFragment = new TeamFragment();
        CustomViewPagerAdapter customViewPagerAdapter = new CustomViewPagerAdapter(getSupportFragmentManager());
        customViewPagerAdapter.addFragment(messageFragment);
        customViewPagerAdapter.addFragment(teamFragment);
        customViewPagerAdapter.addFragment(hallFragment);
        customViewPagerAdapter.addFragment(gameFragment);
        customViewPagerAdapter.addFragment(shopFragment);
        customViewPager.setAdapter(customViewPagerAdapter);
        customViewPager.setCurrentItem(2);
        bottomNavigationView.setSelectedItemId(R.id.navigation_dating);
        customViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_message);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_team);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_dating);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_game);
                        break;
                    case 4:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_shop);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initBottomNavigationView(customViewPager);

//        hallFragment.reloadAdapter();
    }

    private void initBottomNavigationView(final CustomViewPager customViewPager) {
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_message:
                        customViewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_team:
                        customViewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_dating:
                        customViewPager.setCurrentItem(2);
                        return true;
                    case R.id.navigation_game:
                        customViewPager.setCurrentItem(3);
                        return true;
                    case R.id.navigation_shop:
                        customViewPager.setCurrentItem(4);
                        return true;
                }
                return true;
            }
        };
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = PropertiesConfig.userServerUrl + "info/update/address";
                OkHttpClient.Builder builder = new OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .writeTimeout(15, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true);
                builder.sslSocketFactory(OkhttpFunc.createSSLSocketFactory());
                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
                String str = JSONObject.toJSONString(userInfo);
                final RequestBody body = RequestBody.create(FinalDefine.MEDIA_TYPE_JSON, JSONObject.toJSONString(userInfo));
                final Request request = new Request.Builder()
                        .url(path)
                        .header("Content-Type", "application/json")
                        .post(body)
                        .build();
                builder.build().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, call.request().body().toString());
                        Message msg = new Message();
                        msg.obj = "发送失败，e";
                        // 把消息发送到主线程，在主线程里现实Toast
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(response.body().string());
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            JSONObject responseJsonData = (JSONObject) responseJson.getJSONObject("data");
                            switch (code) {
                                case 0:
                                    DataUtil.saveUserInfoData(MainActivity.this, JSONObject.toJSONString(responseJsonData));
                                    UserInfo userTestINFO = DataUtil.getUserInfoData(MainActivity.this);
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
                });

            }
        }).start();
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected void onCancelled() {
//                super.onCancelled();
//            }
//
//            @Override
//            protected void onCancelled(Void aVoid) {
//                super.onCancelled(aVoid);
//            }
//
//            @Override
//            protected void onProgressUpdate(Void... values) {
//                super.onProgressUpdate(values);
//            }
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                String path = PropertiesConfig.testServerUrl + "/get/storeinfo/address";
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
////                                    DataUtil.saveUserInfoData(MainActivity.this,JSONObject.toJSONString(responseJsonData));
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
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                Log.e(TAG, "onPostExecute");
//            }
//        }.execute();
    }

}
