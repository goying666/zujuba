package renchaigao.com.zujuba.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/7/28/028.
 */

public class PatternUtil {

    public final static String FUNC_1 = "^[\\u0391-\\uFFE5]+$"; //
    public final static String FUNC_2 = "^[1-9]\\d{5}$"; //
    public final static String FUNC_3 = "^[1-9]\\d{4,10}$"; //
    public final static String FUNC_4 = "^[a-zA-Z_]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}$"; //
    public final static String FUNC_5 = "^[A-Za-z][A-Za-z1-9_-]+$"; //
    public final static String FUNC_6 = "^1[3|4|5|8][0-9]\\d{8}$"; //
    public final static String FUNC_7 = "^((http|https)://)?([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$"; //
    public final static String FUNC_8 = "^(\\d{6})(18|19|20)?(\\d{2})([01]\\d)([0123]\\d)(\\d{3})(\\d|X|x)?$"; //

    public final static String FUNC_TELEPHONE_NUMBER = "^[1-9]\\d{10}$"; //
    public final static String FUNC_NUMBER_1_99 = "^[1-9]\\d?$"; //
    public final static String FUNC_NUMBER_0000_9999 = "^[1-9]\\d{4}$"; //


    public static boolean strMatcher(String s,String _FUNC_){
        Pattern pattern = Pattern.compile(_FUNC_);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }


}
