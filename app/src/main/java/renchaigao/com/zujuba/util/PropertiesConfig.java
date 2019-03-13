package renchaigao.com.zujuba.util;


public class PropertiesConfig {

    private final static String macIp = "http://192.168.1.190";
    public final static String homeIp = "http://192.168.199.155";

//    public final static String serverUrlAL = "https://www.zujuba.com/";
    public final static String serverUrlAL = "http://192.168.199.155:7814/";
//    public final static String serverUrlBD = homeIp;
//    public final static String serverUrlBD = "http://192.168.199.155";

//    public final static String userServerUrl = serverUrlAL + "7801/";
//    public final static String userServerUrl = serverUrlAL + "user/";
//    public final static String userServerUrl = serverUrlBD + ":7801/";

    public final static String teamServerUrl = serverUrlAL + "team/";
//    public final static String teamServerUrl = serverUrlBD + ":7802/";

    //    public final static String storeServerUrl = serverUrlAL + "store/";//
//    public final static String storeServerUrl = serverUrlBD + ":7803/";

    public final static String placeServerUrl = serverUrlAL + "place/";
    //    public final static String placeServerUrl = serverUrlBD + ":7809/";
//
    public final static String playerServerUrl = serverUrlAL + "player/";//
//    public final static String playerServerUrl = serverUrlBD + ":7804/";

    public final static String photoUrl = serverUrlAL + "photo/";//
//    public final static String photoUrl = serverUrlBD + ":7811/";

    public final static String messageUrl = serverUrlAL + "message/";//
//    public final static String messageUrl = serverUrlBD + ":7807/";

//    public final static String clubUrl = serverUrlAL + "club/";//
    public final static String clubUrl = serverUrlAL + "7812/";//

    //    public final static String mxtWorldGameServerUrl = serverUrlAL + "mxt/";
//    public final static String mxtWorldGameServerUrl = serverUrlBD + ":7805/";

//    public final static String deepForestGameUrl = serverUrlBD + ":7806/";


    public static final int FRAGMENT_TEAM_PAGE = 1101;
    public final static int FRAGMENT_MESSAGE_PAGE = 1102;

    public final static int ACTIVITY_MESSAGE_PAGE = 1001;
    public final static int ACTIVITY_TEAM_PAGE = 1002;

    public final static String SOFTWARE_CODE = "20190122.1.0.1";

    final static public int CREATE_CLUB_ADDRESS_STORE = 1010;
    final static public int CREATE_CLUB_ADDRESS_OPEN = 1011;
    final static public int CREATE_CLUB_ADDRESS_MINE = 1012;

}
