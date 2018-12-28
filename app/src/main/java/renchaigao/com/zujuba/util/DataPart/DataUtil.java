package renchaigao.com.zujuba.util.DataPart;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.dao.User;
import com.renchaigao.zujuba.mongoDB.info.team.TeamInfo;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;


import java.util.Date;

import renchaigao.com.zujuba.util.dateUse;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2018/7/27/027.
 */

public class DataUtil {
    private String userDataString;

    public static User getUserData(Context context) {
        try {
            SharedPreferences pref = context.getSharedPreferences("userData", MODE_PRIVATE);
            String dataJsonString = pref.getString("user", null);
            JSONObject jsonObject = JSONObject.parseObject(dataJsonString);
            if (null != jsonObject)
                return JSONObject.parseObject(dataJsonString, User.class);
            else return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static UserInfo getUserInfoData(Context context) {
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

    public static Boolean saveUserInfoData(Activity activity, UserInfo userInfoData) {
        userInfoData.setUpTime(dateUse.DateToString(new Date()));
        return saveUserData(activity, JSONObject.toJSONString(userInfoData));
    }

    public static Boolean saveUserInfoData(Activity activity, String userInfoData) {
        try {
            SharedPreferences.Editor editor = activity.getSharedPreferences("userData", MODE_PRIVATE).edit();
            editor.putString("userInfo", userInfoData);
            editor.apply();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static TeamInfo getTeamInfo(Context context){
        try {
            SharedPreferences pref = context.getSharedPreferences("teamData", MODE_PRIVATE);
            String dataJsonString = pref.getString("teaminfo", null);
            JSONObject jsonObject = JSONObject.parseObject(dataJsonString);
            if (null != jsonObject)
                return JSONObject.parseObject(dataJsonString, TeamInfo.class);
            else return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static Boolean saveTeamInfo(Activity activity,TeamInfo teamInfo){
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

}
