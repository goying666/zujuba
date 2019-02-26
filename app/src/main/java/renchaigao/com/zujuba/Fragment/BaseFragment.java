package renchaigao.com.zujuba.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2019/1/25/025.
 */

public abstract class BaseFragment extends Fragment {
    protected BaseFragment baseFragment;
    public CompositeDisposable compositeDisposable;
    public Context mContext;
    //    public Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(mContext);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        baseFragment = this;
        InitView(rootView);
        InitData(rootView);
        InitOther(rootView);
        return rootView;
    }

    protected abstract void InitView(View rootView);

    protected abstract void InitData(View rootView);

    protected abstract void InitOther(View rootView);

    protected abstract int getLayoutId();

    protected void addSubscribe(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}
