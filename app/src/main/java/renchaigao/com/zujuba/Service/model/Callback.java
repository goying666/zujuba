package renchaigao.com.zujuba.Service.model;

public interface Callback<T> {
    void onEvent(int code, String msg, T t);
}
