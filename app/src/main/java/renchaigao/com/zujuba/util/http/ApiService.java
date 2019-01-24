package renchaigao.com.zujuba.util.http;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.domain.response.ResponseEntity;

import io.reactivex.Observable;
import okhttp3.Response;
import okhttp3.ResponseBody;
import renchaigao.com.zujuba.util.PropertiesConfig;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {


//    String BASR_URL = PropertiesConfig.serverUrlAL;
//    String BASR_URL = "http://192.168.199.155";

//    @FormUrlEncoded
//    @POST("{firstStr}/{secondStr}/{thirdStr}/{fourthStr}")
    @POST("{firstStr}/{secondStr}/{thirdStr}/{fourthStr}")
    Observable<ResponseEntity> UserServicePost(
            @Path("firstStr") String firstStr,
            @Path("secondStr") String secondStr,
            @Path("thirdStr") String thirdStr,
            @Path("fourthStr") String fourthStr,
            @Body JSONObject requestBody);

}
