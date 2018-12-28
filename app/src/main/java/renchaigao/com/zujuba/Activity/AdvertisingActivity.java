package renchaigao.com.zujuba.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.renchaigao.zujuba.dao.User;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.FinalDefine;
import renchaigao.com.zujuba.util.OkhttpFunc;
import renchaigao.com.zujuba.util.PropertiesConfig;

public class AdvertisingActivity extends AppCompatActivity implements OnBannerListener {
    final static String TAG = "AdvertisingActivity";
    private User userApp;
    private String userString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertising);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setBanner();
        SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
        userString = pref.getString("user", null);
//        userApp = JSONObject.parseObject(userString,User.class);


    }


    private Banner banner;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;

    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }

    private void setBanner() {
        banner = findViewById(R.id.adver_banner);
        //放图片地址的集合
        list_path = new ArrayList<>();
        //放标题的集合
        list_title = new ArrayList<>();
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg");
        list_title.add("好好学习");
        list_title.add("天天向上");
        list_title.add("热爱劳动");
        list_title.add("不搞对象");
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(2);//0:没有点点  1：中间有点点  2：右下角分数  3：下面一条黑，左边有标题，右边有分数 4：下面一条黑，左边有标题
//        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        banner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(this)
                //必须最后调用的方法，启动轮播图。
                .start();
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
        try {
            Thread.sleep(2000);       //此界面沉睡5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void getUserInfo() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }

            @Override
            protected void onCancelled(Void aVoid) {
                super.onCancelled(aVoid);
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected Void doInBackground(Void... params) {
                String path = PropertiesConfig.userServerUrl + "info/get";
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                OkhttpFunc okhttpFunc = new OkhttpFunc();
                builder.sslSocketFactory(okhttpFunc.createSSLSocketFactory());
                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
                final RequestBody body = RequestBody.create(FinalDefine.MEDIA_TYPE_JSON, userString);
                final Request request = new Request.Builder()
                        .url(path)
                        .header("Content-Type", "application/json")
                        .post(body)
                        .build();
                builder.build().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, call.request().body().toString());
                        Intent intent = new Intent(AdvertisingActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
//                        join_progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(response.body().string());
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            JSONObject responseJsonData;
                            SharedPreferences.Editor editor;
                            Intent intent;
                            UserInfo userInfo;
                            switch (code) {
                                //                                    用户首次登陆系统进行创建账号，
                                case 500:
                                    Toast.makeText(AdvertisingActivity.this, "错误500", Toast.LENGTH_LONG).show();
                                    break;
                                case 0://
                                    responseJsonData = (JSONObject) responseJson.getJSONObject("data");
                                    String userInfoString = JSONObject.toJSONString(responseJsonData);
                                    DataUtil.saveUserInfoData(AdvertisingActivity.this, userInfoString);
                                    intent = new Intent(AdvertisingActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case 1: //在数据库中更新用户数据出错；
                                    Toast.makeText(AdvertisingActivity.this, "在数据库中更新用户数据出错", Toast.LENGTH_LONG).show();
                                    break;
                                case 1002: //UserInfo 新增成功；
                                    responseJsonData = (JSONObject) responseJson.getJSONObject("data");
                                    String userInfoStringa = JSONObject.toJSONString(responseJsonData);
                                    editor = getSharedPreferences("userData", MODE_PRIVATE).edit();
                                    editor.putString("userInfo", responseJsonData.toJSONString());
                                    editor.apply();
                                    intent = new Intent(AdvertisingActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case -1001://用户登录，密码错误；
                                    Toast.makeText(AdvertisingActivity.this, "用户登录，密码错误", Toast.LENGTH_LONG).show();
                                    break;
                                case -1002: //创建新用户，电话号码错误
                                    Toast.makeText(AdvertisingActivity.this, "创建新用户，电话号码错误", Toast.LENGTH_LONG).show();
                                    break;
                                case -1003: //用户是存在的，本地的TOKEN超时，需要重新登录；
                                    Toast.makeText(AdvertisingActivity.this, "本地的TOKEN超时，需要重新登录", Toast.LENGTH_LONG).show();
                                    break;
                                case -1004: //用户是存在的，本地的TOKEN错误；
                                    Toast.makeText(AdvertisingActivity.this, "本地的TOKEN错误", Toast.LENGTH_LONG).show();
                                    break;
                                case -1009: //用户是存在的，本地的TOKEN错误；
                                    Toast.makeText(AdvertisingActivity.this, "该手机号还未注册", Toast.LENGTH_LONG).show();
                                    break;
                            }
                            //                        switch (jsonObject1)
                            Log.e(TAG, response.body().string());

                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                        Intent intent = new Intent(AdvertisingActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    @Override
    public void OnBannerClick(int position) {
        Log.i("tag", "你点了第" + position + "张轮播图");
    }

}
