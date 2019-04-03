package renchaigao.com.zujuba.Service;

import renchaigao.com.zujuba.Service.model.LoginInfo;

/**
 * Created by hzwangchenyan on 2017/12/26.
 */
public class AppCache {
    private static GameService service;
    private static LoginInfo myInfo;

    public static GameService getService() {
        return service;
    }

    public static void setService(GameService service) {
        AppCache.service = service;
    }

    public static LoginInfo getMyInfo() {
        return myInfo;
    }

    public static void setMyInfo(LoginInfo myInfo) {
        AppCache.myInfo = myInfo;
    }
}
