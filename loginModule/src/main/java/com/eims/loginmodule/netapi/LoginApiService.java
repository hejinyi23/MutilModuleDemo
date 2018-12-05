package com.eims.loginmodule.netapi;


import com.eims.loginmodule.entity.LoginEntity;
import com.eims.netlib.http.BaseResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;

import retrofit2.http.POST;

public interface LoginApiService {

    @FormUrlEncoded
    @POST("action/apiv2/banner")
    Observable<BaseResponse<LoginEntity>> loginRequest(@Field("name") String name, @Field("psd") String psd);

}
