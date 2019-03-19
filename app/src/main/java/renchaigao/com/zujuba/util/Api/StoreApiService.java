package renchaigao.com.zujuba.util.Api;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.domain.response.ResponseEntity;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2019/2/26/026.
 */

public interface StoreApiService {


    @POST("store/{firstStr}/{secondStr}/{thirdStr}/{fourthStr}")
    Observable<ResponseEntity> FourParameterJsonPost(
            @Path("firstStr") String firstStr,
            @Path("secondStr") String secondStr,
            @Path("thirdStr") String thirdStr,
            @Path("fourthStr") String fourthStr,
            @Body JSONObject requestBody);


    @POST("store/{firstStr}/{secondStr}/{thirdStr}/{fourthStr}")
    Observable<ResponseEntity> FourParameterBodyPost(
            @Path("firstStr") String firstStr,
            @Path("secondStr") String secondStr,
            @Path("thirdStr") String thirdStr,
            @Path("fourthStr") String fourthStr,
            @Body RequestBody requestBody
    );

    @GET("store/getone")
    Observable<ResponseEntity> GetStoreInfo(
            @Query("userId") String userId,
            @Query("storeId") String storeId,
            @Query("token") String token,
            @Query("lastTime") long lastTime);


}
