package renchaigao.com.zujuba.util.Api;

import com.renchaigao.zujuba.domain.response.ResponseEntity;
import renchaigao.com.zujuba.Bean.AndroidMessageContent;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2019/2/26/026.
 */

public interface MessageApiService {

    @GET("message/get")
    Observable<ResponseEntity> GetMessageInfo(
            @Query("userId") String userId,
            @Query("ownerId") String ownerId,
            @Query("messageClass") String messageClass,
            @Query("lastTime") long lastTime);

    @GET("message/getall")
    Observable<ResponseEntity> GetMessageFragmentBean(
            @Query("userId") String userId);

    @POST("message/add")
    Observable<ResponseEntity> AddMessageInfo(
            @Query("userId") String userId,
            @Query("token") String token,
            @Body AndroidMessageContent androidMessageContent);

}
