package renchaigao.com.zujuba.util.http;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;
import renchaigao.com.zujuba.util.OkhttpFunc;
import renchaigao.com.zujuba.util.PropertiesConfig;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Getter
@Setter
public class RetrofitServiceManager {
    private static final int DEFAULT_TIME_OUT = 3000;//超时时间
    private static final int DEFAULT_READ_TIME_OUT = 3000;//读取时间
    private static final int DEFAULT_WRITE_TIME_OUT = 3000;//读取时间
    private static RetrofitServiceManager mRetrofitServiceManager;
    public Retrofit mRetrofit;
    private RetrofitDownloadListener retrofitDownloadListener;

    private RetrofitServiceManager() {
        /**
         * 设置证书的三种方式
         */
        /**
         * 设置可访问所有的https网站
         */
//        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
////                其他配置
//                .build();

        /**
         *  设置具体的证书
         */

//        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(证书的inputstream, null, null);
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager))
////        //其他配置
//         .build();

        /**双向认证
         *
         */
//        HttpsUtils.getSslSocketFactory(
//                证书的inputstream,
//                本地证书的inputstream,
//                本地证书的密码)
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_WRITE_TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        builder.sslSocketFactory(createSSLSocketFactory());
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
//        //设置支持所有https请求
//        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
//        builder.hostnameVerifier((hostname, session) -> true).sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
//        builder.cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(App.getInstance())));
//        addInterceptor(builder);
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
//                .baseUrl(PropertiesConfig.serverUrlAL)
//                .baseUrl(PropertiesConfig.userServerUrl)
                .baseUrl(PropertiesConfig.serverUrlAL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
    public void DestroyRetrofit(){

    }
    public static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new OkhttpFunc.TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();


        } catch (Exception e) {
        }

        return ssfFactory;
    }

    public static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /**
     * //     * 添加各种拦截器
     * //     *
     * //     * @param builder
     * <p>
     * <p>
     * private void addInterceptor(OkHttpClient.Builder builder) {
     * // 添加日志拦截器，非debug模式不打印任何日志
     * HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
     * loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
     * <p>
     * RetrofitDownloadInterceptor retrofitDownloadInterceptor = new RetrofitDownloadInterceptor(retrofitDownloadListener);
     * <p>
     * HttpHeaderInterceptor httpHeaderInterceptor = new HttpHeaderInterceptor.Builder().build();
     * HttpCacheInterceptor httpCacheInterceptor = new HttpCacheInterceptor();
     * if (retrofitDownloadListener != null) {
     * builder.addInterceptor(retrofitDownloadInterceptor);
     * }
     * builder.addInterceptor(loggingInterceptor);
     * <p>
     * builder.addInterceptor(httpHeaderInterceptor);
     * builder.addInterceptor(new CommonParamsInterceptor());
     * builder.addInterceptor(httpCacheInterceptor);
     * builder.addNetworkInterceptor(httpCacheInterceptor);
     * builder.cache(new Cache(new File(Environment.getExternalStorageDirectory() + "/RxJavaDemo"), 1024 * 1024 * 10));
     * <p>
     * }
     */

    public static RetrofitServiceManager getInstance() {
        return SingletonHolder.retrofitServiceManager;

    }

    public <T> T creat(Class<T> tClass) {
        return mRetrofit.create(tClass);
    }

    private static class SingletonHolder {
        private static RetrofitServiceManager retrofitServiceManager = new RetrofitServiceManager();
    }

}
//
//package renchaigao.com.zujuba.util.http;
//
//        import java.security.SecureRandom;
//        import java.security.cert.X509Certificate;
//        import java.util.concurrent.TimeUnit;
//
//        import javax.net.ssl.HostnameVerifier;
//        import javax.net.ssl.SSLContext;
//        import javax.net.ssl.SSLSession;
//        import javax.net.ssl.SSLSocketFactory;
//        import javax.net.ssl.TrustManager;
//        import javax.net.ssl.X509TrustManager;
//
//        import lombok.Getter;
//        import lombok.Setter;
//        import okhttp3.OkHttpClient;
//        import renchaigao.com.zujuba.util.OkhttpFunc;
//        import retrofit2.Retrofit;
//        import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
//        import retrofit2.converter.gson.GsonConverterFactory;
//
//@Getter
//@Setter
//public class RetrofitServiceManager {
//    private static final int DEFAULT_TIME_OUT = 10;//超时时间
//    private static final int DEFAULT_READ_TIME_OUT = 10;//读取时间
//    private static final int DEFAULT_WRITE_TIME_OUT = 10;//读取时间
//    private static RetrofitServiceManager mRetrofitServiceManager;
//    public Retrofit mRetrofit;
//    private RetrofitDownloadListener retrofitDownloadListener;
//
//    private RetrofitServiceManager(String path) {
//
//        /**
//         * 设置证书的三种方式
//         */
//        /**
//         * 设置可访问所有的https网站
//         */
////        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
////        OkHttpClient okHttpClient = new OkHttpClient.Builder()
////                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
//////                其他配置
////                .build();
//
//        /**
//         *  设置具体的证书
//         */
//
////        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(证书的inputstream, null, null);
////        OkHttpClient okHttpClient = new OkHttpClient.Builder()
////                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager))
//////        //其他配置
////         .build();
//
//        /**双向认证
//         *
//         */
////        HttpsUtils.getSslSocketFactory(
////                证书的inputstream,
////                本地证书的inputstream,
////                本地证书的密码)
//
//
//        OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
//                .readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS)
//                .writeTimeout(DEFAULT_WRITE_TIME_OUT, TimeUnit.SECONDS)
//                .retryOnConnectionFailure(true);
//        builder.sslSocketFactory(createSSLSocketFactory());
//        builder.hostnameVerifier(new HostnameVerifier() {
//            @Override
//            public boolean verify(String hostname, SSLSession session) {
//                return true;
//            }
//        });
////        //设置支持所有https请求
////        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
////        builder.hostnameVerifier((hostname, session) -> true).sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
////        builder.cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(App.getInstance())));
////        addInterceptor(builder);
//        mRetrofit = new Retrofit.Builder()
//                .client(builder.build())
////                .baseUrl(PropertiesConfig.serverUrlAL)
//                .baseUrl(path)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build();
//    }
//
//    public static SSLSocketFactory createSSLSocketFactory() {
//        SSLSocketFactory ssfFactory = null;
//        try {
//            SSLContext sc = SSLContext.getInstance("TLS");
//            sc.init(null, new TrustManager[]{new OkhttpFunc.TrustAllCerts()}, new SecureRandom());
//
//            ssfFactory = sc.getSocketFactory();
//
//
//        } catch (Exception e) {
//        }
//
//        return ssfFactory;
//    }
//
//    public static class TrustAllCerts implements X509TrustManager {
//        @Override
//        public void checkClientTrusted(X509Certificate[] chain, String authType) {
//        }
//
//        @Override
//        public void checkServerTrusted(X509Certificate[] chain, String authType) {
//        }
//
//        @Override
//        public X509Certificate[] getAcceptedIssuers() {
//            return new X509Certificate[0];
//        }
//    }
//
//    /**
//     * //     * 添加各种拦截器
//     * //     *
//     * //     * @param builder
//     * <p>
//     * <p>
//     * private void addInterceptor(OkHttpClient.Builder builder) {
//     * // 添加日志拦截器，非debug模式不打印任何日志
//     * HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//     * loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//     * <p>
//     * RetrofitDownloadInterceptor retrofitDownloadInterceptor = new RetrofitDownloadInterceptor(retrofitDownloadListener);
//     * <p>
//     * HttpHeaderInterceptor httpHeaderInterceptor = new HttpHeaderInterceptor.Builder().build();
//     * HttpCacheInterceptor httpCacheInterceptor = new HttpCacheInterceptor();
//     * if (retrofitDownloadListener != null) {
//     * builder.addInterceptor(retrofitDownloadInterceptor);
//     * }
//     * builder.addInterceptor(loggingInterceptor);
//     * <p>
//     * builder.addInterceptor(httpHeaderInterceptor);
//     * builder.addInterceptor(new CommonParamsInterceptor());
//     * builder.addInterceptor(httpCacheInterceptor);
//     * builder.addNetworkInterceptor(httpCacheInterceptor);
//     * builder.cache(new Cache(new File(Environment.getExternalStorageDirectory() + "/RxJavaDemo"), 1024 * 1024 * 10));
//     * <p>
//     * }
//     */
//
//    public static RetrofitServiceManager getInstance(String path) {
//        SingletonHolder singletonHolder = new SingletonHolder();
//        singletonHolder.setPath(path);
//        return singletonHolder.retrofitServiceManager;
//
//    }
//
//    public <T> T creat(Class<T> tClass) {
//        return mRetrofit.create(tClass);
//    }
//    @Getter
//    @Setter
//    private static class SingletonHolder {
//        private static RetrofitServiceManager retrofitServiceManager;
//        public void setPath(String path){
//            retrofitServiceManager = new RetrofitServiceManager(path);
//        }
//    }
//
//}

