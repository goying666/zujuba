package renchaigao.com.zujuba.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.store.StoreInfo;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import renchaigao.com.zujuba.Activity.Center.CreateStoreActivity;
import renchaigao.com.zujuba.Activity.Center.JoinUsActivity;
import renchaigao.com.zujuba.Activity.MainActivity;
import renchaigao.com.zujuba.Activity.Normal.AdvertisingActivity;
import renchaigao.com.zujuba.Fragment.Adapter.HallFragmentAdapter;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.OkhttpFunc;
import renchaigao.com.zujuba.util.PropertiesConfig;
import renchaigao.com.zujuba.util.http.ApiService;
import renchaigao.com.zujuba.util.http.OkHttpUtil;
import renchaigao.com.zujuba.widgets.DividerItemDecoration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static renchaigao.com.zujuba.util.OkhttpFunc.createSSLSocketFactory;

public class HallFragment extends Fragment implements OnBannerListener {


    private OnFragmentInteractionListener mListener;

    public Activity mContext;
//
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    public HallFragment() {
//        this.mContext = getActivity();
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HallFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HallFragment newInstance(String param1, String param2) {
        HallFragment fragment = new HallFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();  }

    private UserInfo userInfo;

    private void initData() {
        userInfo = DataUtil.GetUserInfoData(mContext);

    }

    final private String TAG = "HallFragment";
    private Banner banner;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;
    private Button button_joinUs, button_business;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private HallFragmentAdapter hallFragmentAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_hall, container, false);
        setSwipeRefresh(rootView);
        setRecyclerView(rootView);
        setFloatingActionButton(rootView);
        setBanner(rootView);
        setButton(rootView);
//        InitRxJavaAndRetrofit();
//        reloadAdapter();
        return rootView;
    }

    ApiService apiService;

    private void InitRxJavaAndRetrofit() {
//        OkHttpUtil okHttpUtil = new OkHttpUtil();
        Retrofit retrofit = new Retrofit.Builder()
                .client(OkHttpUtil.builder.build())
//                .client(okHttpUtil.getBuilder().build())
                .baseUrl(PropertiesConfig.storeServerUrl)
                //设置数据解析器
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    private void setSwipeRefresh(View view) {
        swipeRefreshLayout = view.findViewById(R.id.hall_SwipeRefreshLayout); //设置没有item动画
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                reloadAdapter();
            }
        });
    }
    private void setRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.hall_recyclerView);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        hallFragmentAdapter = new HallFragmentAdapter(mContext);
        recyclerView.setAdapter(hallFragmentAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }
    private void setFloatingActionButton(View view) {
        floatingActionButton = view.findViewById(R.id.hall_float_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(1);
            }
        });
    }

    private void setButton(View view) {
        button_joinUs = view.findViewById(R.id.hall_button_joinUs);
        button_business = view.findViewById(R.id.hall_button_business);
        button_joinUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getActivity(), JoinUsActivity.class);
                getActivity().startActivity(intent);
            }
        });
        button_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getActivity(), CreateStoreActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    private void setBanner(View view) {
        banner = view.findViewById(R.id.hall_banner);
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        }
    }
    @Override
    public void onResume() {
        super.onResume();
//        reloadAdapter();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//        }
//        reloadAdapter();
    }

    @SuppressLint("StaticFieldLeak")
    public void reloadAdapter() {
        apiService.UserServicePost("get","storeinfo",userInfo.getId(),  "null",
                JSONObject.parseObject(JSONObject.toJSONString(userInfo), JSONObject.class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "onSubscribe:");
                    }

                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            JSONArray responseJsonData = responseJson.getJSONArray("data");
                            Log.e(TAG, "onResponse CODE OUT");
                            Log.e(TAG, "onResponse CODE is" + code);
                            switch (code) {
                                case 0: //在数据库中更新用户数据出错；
                                    ArrayList<StoreInfo> mStores = new ArrayList();
                                    for (Object m : responseJsonData) {
                                        mStores.add(JSONObject.parseObject(JSONObject.toJSONString(m), StoreInfo.class));
                                    }
                                    if (hallFragmentAdapter == null) {
                                        hallFragmentAdapter = new HallFragmentAdapter(mContext);
                                    }
                                    hallFragmentAdapter.updateResults(mStores);
                                    hallFragmentAdapter.notifyDataSetChanged();
                                    Log.e(TAG, "onResponse");
                                    break;
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError:");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete:");
                    }
                });
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                reloadFlag = "onPreExecute";
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
//                Log.e(TAG, "doInBackground");
//
//                String path = PropertiesConfig.storeServerUrl + "get/storeinfo/" + userInfo.getId();
////              String path = PropertiesConfig.serverUrl + "store/get/storeinfo/" + JSONObject.parseObject(getActivity().getSharedPreferences("userData",getActivity().MODE_PRIVATE).getString("responseJsonDataString",null)).get("id").toString();
//                final OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                        .connectTimeout(15, TimeUnit.SECONDS)
//                        .readTimeout(15, TimeUnit.SECONDS)
//                        .writeTimeout(15, TimeUnit.SECONDS)
//                        .retryOnConnectionFailure(true);
//                builder.sslSocketFactory(createSSLSocketFactory());
//                builder.hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                });
//                final Request request = new Request.Builder()
//                        .url(path)
//                        .header("Content-Type", "application/json")
//                        .get()
//                        .build();
//                final Long startTimeMil = System.currentTimeMillis();
//
//
////                OkHttpClient client = new OkHttpClient();
////                Call call = client.newCall(request);
////                //异步调用并设置回调函数
////                call.enqueue(new Callback() {
////                    @Override
////                    public void onFailure(Call call, IOException e) {
////                    }
////
////                    @Override
////                    public void onResponse(Call call, final Response response) throws IOException {
////
////                    }
////                });
//
//                builder.build().newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        Log.e("onFailure", e.toString());
//                        Log.i(TAG, String.valueOf(System.currentTimeMillis() - startTimeMil));
//
//                        reloadFlag = "doInBackground";
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        try {
//
//                            JSONObject responseJson = JSONObject.parseObject(response.body().string());
//                            String responseJsoStr = responseJson.toJSONString();
//                            int code = Integer.valueOf(responseJson.get("code").toString());
//                            JSONArray responseJsonData = responseJson.getJSONArray("data");
//
//                            Log.e(TAG, "onResponse CODE OUT");
//                            Log.e(TAG, "onResponse CODE is" + code);
//
////                            ArrayList<StoreInfo> mStores = new ArrayList<>();
//                            Log.i(TAG, String.valueOf(System.currentTimeMillis() - startTimeMil));
//                            switch (code) {
//                                case 0: //在数据库中更新用户数据出错；
//                                    ArrayList<StoreInfo> mStores = new ArrayList();
//                                    for (Object m : responseJsonData) {
//                                        mStores.add(JSONObject.parseObject(JSONObject.toJSONString(m), StoreInfo.class));
//                                    }
////                                    Log.e("responseJsonData",responseJsonData.toJSONString());
//                                    if (hallFragmentAdapter == null) {
//                                        hallFragmentAdapter = new HallFragmentAdapter(mContext);
//                                    }
//                                    hallFragmentAdapter.updateResults(mStores);
////                                    hallFragmentAdapter.notifyDataSetChanged();
//                                    Log.e(TAG, "onResponse");
//                                    break;
//                            }
//                            reloadFlag = "doInBackground";
////                            swipeRefreshLayout.setRefreshing(false);
//                        } catch (Exception e) {
//                        }
//                    }
//                });
//                while (!reloadFlag.equals("doInBackground")) ;
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                Log.e(TAG, "onPostExecute");
//                if (mContext == null)
//                    return;
//                hallFragmentAdapter.notifyDataSetChanged();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        }.execute();
    }


    @Override
    public void OnBannerClick(int position) {
        Log.i("tag", "你点了第" + position + "张轮播图");
    }

    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
