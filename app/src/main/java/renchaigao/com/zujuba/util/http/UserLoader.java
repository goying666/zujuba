//package renchaigao.com.zujuba.util.http;
//
//import android.graphics.Movie;
//
//import com.renchaigao.zujuba.domain.response.ResponseEntity;
//
//import java.util.List;
//
//import io.reactivex.Observable;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.schedulers.Schedulers;
//
///**
// * Created by Administrator on 2019/1/23/023.
// */
//
//public class UserLoader extends ObjectLoader {
//
//    private ApiService apiService;
//
//    public UserLoader() {
//        apiService = RetrofitServiceManager.getInstance().create(ApiService.class);
//    }
//
//    public Observable<ResponseEntity> get() {
//        return observe(apiService.UserServicePost("", "", "", "", null)).map(new ){
////            @Override
////            public List<Movie> call(MovieSubject movieSubject) {
////                return movieSubject.subjects;
////            }
//        });
//    }
//
//    public Observable<String> getWeatherList(String cityId, String key) {
//        return observe(mMovieService.getWeather(cityId, key)).map(new Func1<String, String>() {
//            @Override
//            public String call(String s) {
//                //可以处理对应的逻辑后在返回
//                return s;
//            }
//        });
//        +
//    }
//}
//
///**
// * 将一些重复的操作提出来，放到父类以免Loader 里每个接口都有重复代码
// */
//public class ObjectLoader {
//    /**
//     * @param observable
//     * @param <T>
//     * @return
//     */
//    protected <T> Observable<T> observe(Observable<T> observable) {
//        return observable
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
