package com.sum.andrioddeveloplibrary.net;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by sdl on 2017/12/28.
 * <p>
 * 如果你在注解中提供的url是完整的url，则url将作为请求的url。
 * 如果你在注解中提供的url是不完整的url，且不以 / 开头，则请求的url为baseUrl+注解中提供的值
 * 如果你在注解中提供的url是不完整的url，且以 / 开头，则请求的url为baseUrl的主机部分+注解中提供的值
 * <p>
 * https://www.jianshu.com/p/308f3c54abdd
 */
public interface Api {

    //TODO GET传参数方式

    /**
     * method 表示请求的方法，区分大小写
     * path表示路径
     * hasBody表示是否有请求体
     */
    @HTTP(method = "GET", path = "blog/{id}", hasBody = false)
    Call<ResponseBody> getBlog(@Path("id") int id);

    @GET("appapi/orderStat")
    Call<Object> apiGet(@QueryMap Map<String, String> params);


    Call<String> apiGet2(@Query("lat") double lat);


    //TODO POST传参数方式
    //TODO POST传参数方式
    //TODO POST传参数方式

    /*
     *Post注解使用
     *  @Url 用于重新定义请求地址
     *  @Body 用于非表单提交
     *  @Field，@FieldMap用于Form表单提交数据
     */

    //https://github.com/ikidou/Retrofit2Demo/blob/master/client/src/main/java/com/github/ikidou/Example03.java

    @POST("form")
    Call<Object> apiPost1(@Field("mobile") String phone, @Field("is_new") String is_new);

    @POST("form")
    @FormUrlEncoded
    Call<Object> apiPost2(@FieldMap Map<String, Object> params);

    /**
     * {@link Part} 后面支持三种类型，
     * {@link RequestBody}、
     * {@link okhttp3.MultipartBody.Part} 、
     * 任意类型除 {@link okhttp3.MultipartBody.Part} 以外，其它类型都必须带上
     * 表单字段({@link okhttp3.MultipartBody.Part} 中已经包含了表单字段的信息)，
     */
    @POST("topic/imageUpload")
    @Multipart
    Call<ResponseBody> testFileUpload1(@Part("name") RequestBody name, @Part MultipartBody.Part file);

    /**
     * PartMap 注解支持一个Map作为参数，支持 {@link RequestBody } 类型，
     */
    @POST("form")
    @Multipart
    Call<ResponseBody> testFileUpload2(@PartMap Map<String, RequestBody> args, @Part MultipartBody.Part file);

    @POST("form")
    @Multipart
    Call<ResponseBody> testFileUpload3(@PartMap Map<String, RequestBody> args);


    /*
     * 接口测试
     * */

    @POST("topic/search")
    Call<Object> apiPost_Search(@Body RequestBody requestBody);

    @POST("topic/search ")
    Call<Object> apiPost_Search(@Body Object requestBody);

    //添加过.addConverterFactory(GsonConverterFactory.create())方案
    //被@Body注解的的对象将会被Gson转换成RequestBody发送到服务器。




    /**
     * @param url 会重新调用全路劲的，（search接口不会调用）
     */
    @POST("search")
    Call<String> newUrlRequest(@Url String url, @Body RequestBody requestBody);


}
