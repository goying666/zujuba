package renchaigao.com.zujuba.util.Api;

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

public interface GameApiService {


    @POST("game/create")
    Observable<ResponseEntity> CreateGame(
            @Query("userId") String userId,
            @Query("gameId") String gameId,
            @Query("token") String token,
            @Body RequestBody requestBody);


    @POST("game/edit")
    Observable<ResponseEntity> EditGame(
            @Query("userId") String userId,
            @Query("gameId") String gameId,
            @Query("token") String token,
            @Body RequestBody requestBody);

}
