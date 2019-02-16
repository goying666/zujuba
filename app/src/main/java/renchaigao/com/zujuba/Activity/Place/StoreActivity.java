package renchaigao.com.zujuba.Activity.Place;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import normal.UUIDUtil;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import renchaigao.com.zujuba.Activity.BaseActivity;
import renchaigao.com.zujuba.Activity.Normal.StartActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.FinalDefine;
import renchaigao.com.zujuba.util.PictureRAR;
import renchaigao.com.zujuba.util.PropertiesConfig;
import renchaigao.com.zujuba.util.http.ApiService;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;

public class StoreActivity extends BaseActivity {

    final static String TAG = "StoreActivity";

    // Handler内部类，它的引用在子线程中被使用，发送mesage，被handlerMesage方法接收
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            JSONObject jsonObject = (JSONObject) msg.obj;
            UpdateView(jsonObject);
            Toast.makeText(StoreActivity.this, jsonObject.getString("msgstr"), Toast.LENGTH_SHORT).show();
        }
    };

    private void UpdateView(JSONObject jsonObject) {
        //放图片地址的集合
        list_path = new ArrayList<>();
        list_path.add(PropertiesConfig.photoUrl + "showimage/"+ jsonObject.getString("userid")+"/"+ jsonObject.getString("placeid") +"/photo1.jpg");
        list_path.add(PropertiesConfig.photoUrl + "showimage/"+ jsonObject.getString("userid")+"/"+ jsonObject.getString("placeid") +"/photo2.jpg");
        list_path.add(PropertiesConfig.photoUrl + "showimage/"+ jsonObject.getString("userid")+"/"+ jsonObject.getString("placeid") +"/photo3.jpg");
        list_path.add(PropertiesConfig.photoUrl + "showimage/"+ jsonObject.getString("userid")+"/"+ jsonObject.getString("placeid") +"/photo4.jpg");


        setBanner();
    }

    private TextView store_name, store_socre, store_allpeoplenum, store_spend, store_stars,
            store_time1, store_time2, store_time3, store_time4, store_address, store_environment,
            store_note, store_desk, store_nowpeople,a_store_info_team_note;
    private ImageView store_phone_image;
    private RatingBar store_page_ratingbar;

    @Override
    protected void InitView() {
        banner = findViewById(R.id.a_place_page_banner);
        store_page_ratingbar = findViewById(R.id.store_page_ratingbar);
        store_phone_image = findViewById(R.id.store_page_phone_imge);
        store_name = findViewById(R.id.store_name_text);
        store_socre = findViewById(R.id.store_page_score);
        store_allpeoplenum = findViewById(R.id.store_page_evaluation_people_number);
        store_spend = findViewById(R.id.store_page_spend);
        store_stars = findViewById(R.id.store_page_ratingbar_text);
        store_time1 = findViewById(R.id.store_page_time_1);
        store_time2 = findViewById(R.id.store_page_time_2);
        store_time3 = findViewById(R.id.store_page_time_3);
        store_time4 = findViewById(R.id.store_page_time_4);
        store_address = findViewById(R.id.store_page_address_info);
        store_environment = findViewById(R.id.a_store_info_environment);
        store_note = findViewById(R.id.a_store_info_note);
        store_desk = findViewById(R.id.store_page_null7);
        store_nowpeople = findViewById(R.id.store_page_null9);
        a_store_info_team_note = findViewById(R.id.a_store_info_team_note);
        ;
    }

    JSONObject jsonObjectUP;

    @Override
    protected void InitData() {
        jsonObjectUP = JSONObject.parseObject(getIntent().getStringExtra("storeinfo"));
    }

    @Override
    protected void InitOther() {
        reloadAdapter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_store;
    }

    private ArrayList<String> list_path;
    private Banner banner;

    private void setBanner() {

        //放标题的集合
//        list_title = new ArrayList<>();
// 设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(2);//0:没有点点  1：中间有点点  2：右下角分数  3：下面一条黑，左边有标题，右边有分数 4：下面一条黑，左边有标题
//        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        banner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
//        banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
//                .setOnBannerListener(new OnBannerListener() {
//                    @Override
//                    public void OnBannerClick(int position) {
//                        Log.e("StoreActivity", "click banner");
//                    }
//                })
                //必须最后调用的方法，启动轮播图。
                .start();
    }

    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }

    private void reloadAdapter() {
        RetrofitServiceManager.getInstance().SetRetrofit(PropertiesConfig.placeServerUrl);
        String storeInfoString = getIntent().getStringExtra("storeinfo");
        RequestBody multiBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("json", storeInfoString)
                .build();
        addSubscribe(RetrofitServiceManager.getInstance().creat(ApiService.class)
                .PlaceServicePost("store",
                        "getone",
                        jsonObjectUP.get("placeid").toString(),
                        jsonObjectUP.get("placeid").toString(),
                        multiBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(StoreActivity.this) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            JSONObject jsonObject = responseJson.getJSONObject("data");
                            Message msg = new Message();
                            jsonObject.put("msgstr", code);
                            switch (code) {
                                case RespCodeNumber.SUCCESS:
                                    msg.obj = jsonObject;
                                    handler.sendMessage(msg);
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


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

}
