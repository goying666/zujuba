package renchaigao.com.zujuba.util.http;

/**
 *Retrofit下载回调
 * @author Administrator
 * @date 2018/6/22
 */

public interface RetrofitDownloadListener {

    void onStartDownload();

    void onProgress(int progress);

    void onFinishDownload();

    void onFail(String errorInfo);
}
