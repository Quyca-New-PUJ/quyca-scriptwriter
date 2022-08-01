package com.quyca.scriptwriter.integ.backend.service;

import com.quyca.scriptwriter.integ.backend.interfaces.ActionListAPI;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActionListService {
    private ActionListAPI actionListAPI;

    public ActionListService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        actionListAPI = retrofit.create(ActionListAPI.class);
    }


}
