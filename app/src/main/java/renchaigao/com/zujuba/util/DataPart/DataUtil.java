package renchaigao.com.zujuba.util.DataPart;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.dao.User;
import com.renchaigao.zujuba.mongoDB.info.team.TeamInfo;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;


import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import okhttp3.Response;
import renchaigao.com.zujuba.util.dateUse;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2018/7/27/027.
 */

public class DataUtil {
    private String userDataString;

    public static Boolean SaveNewUserAllInfo(Activity activity, String userInfoData, String token) {
        try {
            SharedPreferences.Editor editor = activity.getSharedPreferences("userData", MODE_PRIVATE).edit();
            editor.putString("userInfo", userInfoData);
            editor.putString("token", token);
            editor.apply();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean SaveNewUserAllInfo(Activity activity, UserInfo userInfoData, String token) {
        String userInfoDataStr = JSONObject.toJSONString(userInfoData);
        try {
            SharedPreferences.Editor editor = activity.getSharedPreferences("userData", MODE_PRIVATE).edit();
            editor.putString("userInfo", userInfoDataStr);
            editor.putString("token", token);
            editor.apply();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getToken(Context context) {
        try {
            SharedPreferences pref = context.getSharedPreferences("userData", MODE_PRIVATE);
            String tokenString = pref.getString("token", null);
            if (null != tokenString)
                return tokenString;
            else return null;
        } catch (Exception e) {
            return null;
        }
    }


    public static UserInfo GetUserInfoData(Context context) {
        try {
            SharedPreferences pref = context.getSharedPreferences("userData", MODE_PRIVATE);
            String dataJsonString = pref.getString("userInfo", null);
            JSONObject jsonObject = JSONObject.parseObject(dataJsonString);
            if (null != jsonObject)
                return JSONObject.parseObject(dataJsonString, UserInfo.class);
            else return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static String GetUserInfoStringData(Context context) {
        try {
            SharedPreferences pref = context.getSharedPreferences("userData", MODE_PRIVATE);
            String dataJsonString = pref.getString("userInfo", null);
            JSONObject jsonObject = JSONObject.parseObject(dataJsonString);
            if (null != jsonObject)
                return dataJsonString;
            else return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static Boolean saveToken(Activity activity, String token) {
        try {
            SharedPreferences.Editor editor = activity.getSharedPreferences("userData", MODE_PRIVATE).edit();
            editor.putString("token", token);
            editor.apply();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static User GetUserData(Context context) {
        try {
            return JSONObject.parseObject(JSONObject.toJSONString(GetUserInfoData(context)), User.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static Boolean saveUserData(Activity activity, User userData) {
        userData.setUpTime(dateUse.DateToString(new Date()));
        return saveUserData(activity, JSONObject.toJSONString(userData));
    }

    public static Boolean saveUserData(Activity activity, String userData) {
        try {
            SharedPreferences.Editor editor = activity.getSharedPreferences("userData", MODE_PRIVATE).edit();
            editor.putString("user", userData);
            editor.apply();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean SaveUserInfoData(Activity activity, String userInfoData) {
        try {
            SharedPreferences.Editor editor = activity.getSharedPreferences("userData", MODE_PRIVATE).edit();
            editor.putString("userInfo", userInfoData);
            editor.apply();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static TeamInfo getTeamInfo(Activity activity) {
        try {
            SharedPreferences pref = activity.getSharedPreferences("teamData", MODE_PRIVATE);
            String dataJsonString = pref.getString("teaminfo", null);
            JSONObject jsonObject = JSONObject.parseObject(dataJsonString);
            if (null != jsonObject)
                return JSONObject.parseObject(dataJsonString, TeamInfo.class);
            else return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static Boolean saveTeamInfo(Activity activity, TeamInfo teamInfo) {
        teamInfo.setUpTime(dateUse.DateToString(new Date()));
        try {
            SharedPreferences.Editor editor = activity.getSharedPreferences("teamData", MODE_PRIVATE).edit();
            editor.putString("teaminfo", JSONObject.toJSONString(teamInfo));
            editor.apply();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean SaveResponse(Activity activity, ResponseData responseData) {
        try {
            SharedPreferences.Editor editor = activity.getSharedPreferences("httpResponseData", MODE_PRIVATE).edit();
            editor.putString("responseData", JSONObject.toJSONString(responseData));
            editor.apply();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static ResponseData GetResponse(Activity activity) {
        try {
            SharedPreferences pref = activity.getSharedPreferences("httpResponseData", MODE_PRIVATE);
            String responseDataStr = pref.getString("responseData", null);
            ResponseData responseData = JSONObject.parseObject(responseDataStr,ResponseData.class);
            if (null != responseData)
                return responseData;
            else return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Getter
    @Setter
    public static class ResponseData {
        Boolean ret;
        String id;
        String sendTime;
        String responseStr;
    }
}
