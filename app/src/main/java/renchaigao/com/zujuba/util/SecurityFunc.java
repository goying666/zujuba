package renchaigao.com.zujuba.util;

/**
 * Created by Administrator on 2018/7/25/025.
 */

public class SecurityFunc {
    public static boolean checkPWDisOk(String pwd) {
//        密码要超过6位数
        if (pwd.length() > 5) {
            return true;
        } else
            return false;
    }

}
