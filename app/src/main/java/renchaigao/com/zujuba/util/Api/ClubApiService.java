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

    @GET("club/getone")
    Observable<ResponseEntity> GetClubInfo(
            @Query("userId") String userId,
            @Query("token") String token,
            @Query("clubId") String clubId);

    @GET("club/getall")
    Observable<ResponseEntity> GetMyAllClub(
            @Query("userId") String userId,
            @Query("token") String token);

    @POST("club/create")
    Observable<ResponseEntity> CreateClub(
            @Query("userId") String userId,
            @Query("placeId") String placeId,
            @Query("token") String token,
            @Body ClubInfo clubInfo);

}
