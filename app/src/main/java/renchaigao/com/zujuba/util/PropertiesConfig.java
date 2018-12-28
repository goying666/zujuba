package renchaigao.com.zujuba.util;


public class PropertiesConfig {

//    public final static String serverUrl = "http://www.zujuba.com/zujuba/";

//    public final static String serverUrl = "http://192.168.199.155";
//    public final static String userServerUrl = serverUrl + ":7801/";
//    public final static String teamServerUrl = serverUrl + ":7802/";
//    public final static String storeServerUrl = serverUrl + ":7803/";
//    public final static String playerServerUrl = serverUrl + ":7804/";

//    public final static String serverUrl = "https://www.zujuba.com/";
//    public final static String userServerUrl = serverUrl+ "user/";
//    public final static String teamServerUrl = serverUrl+ "team/" ;
//    public final static String storeServerUrl = serverUrl+ "store/" ;
//    public final static String playerServerUrl = serverUrl+ "player/" ;

//    private final static String windowsIp = "http://192.168.199.155";
    private final static String macIp = "http://192.168.1.190";
    public final static String homeIp="http://192.168.199.155";

    public final static String serverUrlAL = "https://www.zujuba.com/";
    public final static String serverUrlBD = homeIp;
//    public final static String serverUrlBD = "http://192.168.199.155";

    public final static String userServerUrl = serverUrlAL + "user/";
//    public final static String userServerUrl = serverUrlBD + ":7801/";

//    public final static String teamServerUrl = serverUrlAL + "team/";
    public final static String teamServerUrl = serverUrlBD + ":7802/";

    public final static String storeServerUrl = serverUrlAL + "store/";//
//    public final static String storeServerUrl = serverUrlBD + ":7803/";
//
//    public final static String playerServerUrl = serverUrlAL + "player/";
//
//    public final static String playerServerUrl = serverUrlBD + ":7804/";

//    public final static String mxtWorldGameServerUrl = serverUrlAL + "mxt/";
//
    public final static String mxtWorldGameServerUrl = serverUrlBD + ":7805/";

    public final static String deepForestGameUrl = serverUrlBD + ":7806/";

    //    public final static String messageUrl = serverUrlAL + "message/";//
    public final static String messageUrl = serverUrlBD + ":7807/";


    public final static int ACTIVITY_MESSAGE = 1;
    public final static int ACTIVITY_TEAM = 2;
    public final static int ACTIVITY_TEAM_CREATE = 3;
    public final static int FRAGMENT_MESSAGE = 4;
    public final static int FRAGMENT_TEAM = 5;

//    public final static String ;




}
