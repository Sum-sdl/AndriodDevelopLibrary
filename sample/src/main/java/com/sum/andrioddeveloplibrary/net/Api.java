package com.sum.andrioddeveloplibrary.net;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sdl on 2017/12/28.
 */

public interface Api {

    @GET("index.php?")
    Call<Respone> getExampleValue(@Query("method") String getProRecommend);

}
