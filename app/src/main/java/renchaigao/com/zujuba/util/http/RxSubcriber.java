package renchaigao.com.zujuba.util.http;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import renchaigao.com.zujuba.Activity.BaseActivity;
import retrofit2.HttpException;

/**
 * Created by Administrator on 2019/1/23/023.
 */
public  abstract class RxSubcriber<T> implements Observer<BaseResponse<T>> {

    private ProgressDialog mProgressDialog;
    private Disposable disposable;
    private BaseActivity context;
    private String errorMsg;

    public RxSubcriber(BaseActivity context){
        this.context = context;
    }


    @Override
    public void onSubscribe(Disposable d) {
        disposable = new CompositeDisposable();
        showLoading();
    }

    @Override
    public void onNext(BaseResponse<T> value) {
        if(!value.isSuccess()){
            ApiException apiException = new ApiException(value.getCode(),value.getMsg());
            onError(apiException);
            return;
        }
        onSuccess(value.getData());
    }


    @Override
    public void onError(Throwable e) {
        if (e instanceof IOException) {
            /** 没有网络 */
            errorMsg = "Please check your network status";
        } else if (e instanceof HttpException) {
            /** 网络异常，http 请求失败，即 http 状态码不在 [200, 300) 之间, such as: "server internal error". */
            errorMsg = ((HttpException) e).response().message();
        } else if (e instanceof ApiException) {
            /** 网络正常，http 请求成功，服务器返回逻辑错误 */
            errorMsg = ((ApiException)e).getMsg();
        } else {
            /** 其他未知错误 */
            errorMsg = !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "unknown error";
        }


        dismissLoading();

        new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage(errorMsg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if(!disposable.isDisposed()){
                            disposable.dispose();
                        }
                    }
                }).show();
    }

    @Override
    public void onComplete() {
        dismissLoading();
        Log.d("print", "-->执行了完成的方法");
    }

    public abstract void onSuccess(T t);

    private void showLoading(){
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("正在加载中...");
            mProgressDialog.show();
        }
    }

    private void dismissLoading(){
        if(mProgressDialog != null){
            mProgressDialog.dismiss();
        }
    }
}