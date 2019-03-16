package renchaigao.com.zujuba.Activity.Normal;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.Activity.BaseActivity;
import renchaigao.com.zujuba.Activity.MainActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.UserApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;

public class AdvertisingActivity extends BaseActivity implements OnBannerListener {
    final static String TAG = "AdvertisingActivity";
    private String userString;
    private UserInfo userInfo;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_advertising);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.hide();
//        }
////        InitRxJavaAndRetrofit();
//    }

    @Override
    protected void InitView() {
        setBanner();
    }

    @Override
    protected void InitData() {

        userString = DataUtil.GetUserInfoStringData(this);
        userInfo = DataUtil.GetUserInfoData(this);
    }

    @Override
    protected void InitOther() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_advertising;
    }


//    private void InitRxJavaAndRetrofit() {
////        OkHttpUtil okHttpUtil = new OkHttpUtil();
//        Retrofit retrofit = new Retrofit.Builder()
//                .client(OkHttpUtil.builder.build())
////                .client(okHttpUtil.getBuilder().build())
//                .baseUrl(PropertiesConfig.userServerUrl)
//                //设置数据解析器
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build();
//        apiService = retrofit.create(ApiService.class);
//    }

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
        banner = (Banner) findViewById(R.id.adver_banner);
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

    private void getUserInfo() {
        addSubscribe(RetrofitServiceManager.getInstance().creat(UserApiService.class)
                .FourParameterJsonPost(
//        apiService.FourParameterJsonPost(
                "get", userInfo.getId(), "nul", "null",
                JSONObject.parseObject(JSONObject.toJSONString(userInfo), JSONObject.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ResponseEntity>() {
                .subscribeWith(new BaseObserver<ResponseEntity>(this) {

                    @Override
                    protected void onSuccess(ResponseEntity responseEntity) {

                    }
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        Log.e(TAG, "onSubscribe:");
//                    }

                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            JSONObject responseJsonData;
                            Intent intent = new Intent(AdvertisingActivity.this, MainActivity.class);
                            switch (code) {
                                case RespCodeNumber.SUCCESS://
                                    responseJsonData = responseJson.getJSONObject("data");
                                    String userInfoString = JSONObject.toJSONString(responseJsonData);
                                    DataUtil.SaveUserInfoData(AdvertisingActivity.this, userInfoString);
                                    break;
                            }
                            startActivity(intent);
                            finish();
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

    @Override
    public void OnBannerClick(int position) {
        Log.i("tag", "你点了第" + position + "张轮播图");
    }

}
