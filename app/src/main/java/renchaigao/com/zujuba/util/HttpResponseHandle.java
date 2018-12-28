package renchaigao.com.zujuba.util;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/7/27/027.
 */
@Getter
@Setter
public class HttpResponseHandle {
    /*待开发，此类用作所有服务器回传结果的处理*/
    private Response response;
    private JSONObject responseJson ;
    private int code = Integer.valueOf(responseJson.get("code").toString());
    private JSONObject responseJsonData = (JSONObject) responseJson.getJSONObject("data");

}
