package renchaigao.com.zujuba.util.Api;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.club.ClubInfo;

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

public interface UserApiService {


    @POST("user/{firstStr}/{secondStr}/{thirdStr}/{fourthStr}")
    Observable<ResponseEntity> FourParameterJsonPost(
            @Path("firstStr") String firstStr,
            @Path("secondStr") String secondStr,
            @Path("thirdStr") String thirdStr,
            @Path("fourthStr") String fourthStr,
            @Body JSONObject requestBody);


    @POST("user/{firstStr}/{secondStr}/{thirdStr}/{fourthStr}")
    Observable<ResponseEntity> FourParameterBodyPost(
            @Path("firstStr") String firstStr,
            @Path("secondStr") String secondStr,
            @Path("thirdStr") String thirdStr,
            @Path("fourthStr") String fourthStr,
            @Body RequestBody requestBody
    );


    @GET("user/{firstStr}/{secondStr}/{thirdStr}/{fourthStr}")
    Observable<ResponseEntity> PlaceServiceGet(
            @Path("firstStr") String firstStr,
            @Path("secondStr") String secondStr,
            @Path("thirdStr") String thirdStr,
            @Path("fourthStr") String fourthStr);


    @POST("user/update")
    Observable<ResponseEntity> UpdateUserInfo(
            @Query("updateStyle") String updateStyle,
            @Query("userId") String userId,
            @Query("token") String token,
            @Body JSONObject jsonObject);

    @GET("user/placeallcreate")
    Observable<ResponseEntity> GetUserPlaceList(
            @Query("userId") String userId,
            @Query("token") String token);
    @GET("user/oneplace")
    Observable<ResponseEntity> GetUserOnePlaceInfo(
            @Query("userId") String userId,
            @Query("placeId") String placeId,
            @Query("token") String token);

}
