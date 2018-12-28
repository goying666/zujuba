package renchaigao.com.zujuba.util;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum  RespCode {
    EXCEPTION(1, "抛出异常"),
    SUCCESS(0, "请求成功"),
    WARN(-1, "网络异常，请稍后重试"),
    USERHAD(1001,"用户已存在"),
    WRONGPWD(-1001,"用户密码错误"),
    USERNOTEL(-1002,"用户没有电话信息");



    private int code;
    private String msg;

    RespCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}
