package renchaigao.com.zujuba.util;

import android.content.Context;
import android.provider.Telephony;
import android.telephony.TelephonyManager;

import com.alibaba.idst.nls.internal.protocol.Content;

import renchaigao.com.zujuba.Activity.Normal.LoginActivity;

/**
 * Created by Administrator on 2018/7/25/025.
 */

public class SecurityFunc {
    public static Boolean checkPWDisOk(String pwd) {
//        密码要超过6位数
        if (pwd.length() > 5 && pwd.length() < 9) {
            return true;
        } else
            return false;
    }

    public static Boolean CheckTelephoneNumberIsRight(String telephoneNumber){
        if(!PatternUtil.strMatcher(telephoneNumber,PatternUtil.FUNC_TELEPHONE_NUMBER))
            return false;
        return true;
    }

    public static Boolean CheckVerCodeIsRight(String verCode){
//        try {
//            Integer integer = Integer.getInteger(verCode);
//            if(integer<10000 && integer>999)
//                return true;
//            else
//                return false;
//        }catch (Exception e){
//            return false;
//        }
        if(!PatternUtil.strMatcher(verCode,PatternUtil.FUNC_NUMBER_1000_9999))
            return false;
        return true;
    }

}
