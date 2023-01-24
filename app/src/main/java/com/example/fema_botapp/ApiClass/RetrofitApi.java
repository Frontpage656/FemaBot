package com.example.fema_botapp.ApiClass;


import com.example.fema_botapp.PojoClass.MessageModeClass;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitApi {
    @GET
    Call<MessageModeClass> getMessage(@Url String url);
}
