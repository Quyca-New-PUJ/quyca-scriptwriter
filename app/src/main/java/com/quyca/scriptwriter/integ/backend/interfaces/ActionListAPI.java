package com.quyca.scriptwriter.integ.backend.interfaces;

import com.quyca.scriptwriter.integ.backend.model.ActionList;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ActionListAPI {

    String API_ROUTE = "/";

    @GET("")
    Observable<ActionList> getAllActionLists();

}
