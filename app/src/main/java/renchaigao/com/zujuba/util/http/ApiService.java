package renchaigao.com.zujuba.util.http;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.domain.response.ResponseEntity;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import renchaigao.com.zujuba.util.PropertiesConfig;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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

    @Multipart
    @POST("{firstStr}/{secondStr}/{thirdStr}/{fourthStr}")
    Observable<ResponseEntity> PlaceServicePost(
            @Path("firstStr") String firstStr,
            @Path("secondStr") String secondStr,
            @Path("thirdStr") String thirdStr,
            @Path("fourthStr") String fourthStr,
//            @Body RequestBody Body
            @Part MultipartBody.Part a
            );


}
