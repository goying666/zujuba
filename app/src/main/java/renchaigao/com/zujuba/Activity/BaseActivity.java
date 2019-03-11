package renchaigao.com.zujuba.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/3/25.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected BaseActivity baseActivity;
    public CompositeDisposable compositeDisposable;
    private ProgressDialog mProgressDialog;


    public void showLoading(){
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(baseActivity);
        }
        mProgressDialog.setMessage("正在加载中...");
        mProgressDialog.show();
    }

    public void dismissLoading(Boolean result){
        if(mProgressDialog != null){
            if (result)
                mProgressDialog.setMessage("完成");
            else
                mProgressDialog.setMessage("失败");
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        baseActivity = this;
        InitView();
        InitData();
        InitOther();
    }

    protected abstract void InitView();
    protected abstract void InitData();
    protected abstract void InitOther();
    protected abstract int getLayoutId();

    protected void addSubscribe(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}
