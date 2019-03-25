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


    @POST("store/add")
    Observable<ResponseEntity> AddStore(
            @Query("userId") String userId,
            @Query("storeId") String storeId,
            @Query("token") String token,
            @Body RequestBody requestBody);



    @GET("store/getnear")
    Observable<ResponseEntity> GetNearlyStoreInfo(
            @Query("userId") String userId,
            @Query("token") String token);


    @GET("store/getone/{firstStr}")
    Observable<ResponseEntity> GetOneStoreInfo(
            @Path("firstStr") String firstStr,
            @Query("userId") String userId,
            @Query("storeId") String storeId,
            @Query("token") String token,
            @Query("lastTime") long lastTime);

    @GET("store/manager/get/{firstStr}")
    Observable<ResponseEntity> ManagerGetOneStoreInfo(
            @Path("firstStr") String firstStr,
            @Query("userId") String userId,
            @Query("storeId") String storeId,
            @Query("token") String token);

}
