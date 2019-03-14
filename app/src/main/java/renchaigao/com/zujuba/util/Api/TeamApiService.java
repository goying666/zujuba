package renchaigao.com.zujuba.util.Api;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.domain.response.ResponseEntity;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import renchaigao.com.zujuba.Bean.AndroidMessageContent;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2019/2/26/026.
 */

public interface TeamApiService {


    @POST("team/{firstStr}/{secondStr}/{thirdStr}/{fourthStr}")
    Observable<ResponseEntity> FourParameterJsonPost(
            @Path("firstStr") String firstStr,
            @Path("secondStr") String secondStr,
            @Path("thirdStr") String thirdStr,
            @Path("fourthStr") String fourthStr,
            @Body JSONObject requestBody);


    @POST("team/{firstStr}/{secondStr}/{thirdStr}/{fourthStr}")
    Observable<ResponseEntity> FourParameterBodyPost(
            @Path("firstStr") String firstStr,
            @Path("secondStr") String secondStr,
            @Path("thirdStr") String thirdStr,
            @Path("fourthStr") String fourthStr,
            @Body RequestBody requestBody
    );


    @POST("team/join")
    Observable<ResponseEntity> JoinTeam(
            @Query("userId") String userId,
            @Query("teamId") String teamId,
            @Query("token") String token);


}
