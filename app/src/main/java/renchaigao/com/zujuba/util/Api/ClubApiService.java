package renchaigao.com.zujuba.util.Api;

import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.club.ClubInfo;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2019/2/26/026.
 */

public interface ClubApiService {

    @GET("club/get")
    Observable<ResponseEntity> GetClub(
            @Query("userId") String userId,
            @Query("clubId") String clubId,
            @Query("token") String token);

    @POST("club/create")
    Observable<ResponseEntity> CreateClub(
            @Query("userId") String userId,
            @Query("placeId") String placeId,
            @Body ClubInfo clubInfo);

}
