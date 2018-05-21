package com.sum.andrioddeveloplibrary.net;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by sdl on 2017/12/28.
 */

public interface Api {

    @GET("/index.php?tn=45086098_adr")
    Call<Object> getExampleValue(@Query("method") String getProRecommend);

    @GET("appapi/orderStat")
    Call<String> orderStat(@QueryMap Map<String, Object> params);



    @POST("appapi/getAuthCode")
    Call<Object> getAuthCode2(@Field("mobile") String phone, @Field("is_new") String is_new);

    @POST("appapi/updatePhone")
    @FormUrlEncoded
    Call<Object> selfUpdatePhone(@FieldMap Map<String, String> params);


}
