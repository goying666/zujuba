package renchaigao.com.zujuba.util.http;

/**
 * Created by Administrator on 2019/1/23/023.
 */

public class BaseResponse<T> {
    private int code;
    private String msg;
    private T model;

    public T getData() {
        return model;
    }

    public void setData(T model) {
        this.model = model;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;

    }

    public boolean isSuccess(){
        return code == 0;
    }

}
