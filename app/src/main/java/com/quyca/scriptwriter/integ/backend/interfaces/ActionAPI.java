package com.quyca.scriptwriter.integ.backend.interfaces;

import com.quyca.scriptwriter.integ.backend.model.ActionDTO;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ActionAPI {

    String API_ROUTE = "/";

    @GET("")
    Observable<ActionDTO> getAllActions();

}
