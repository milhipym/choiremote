package com.choi.monitoring.callapi;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CHOICAllApiInterface {

    //@GET("/mobile/monitoring/{path}")
    @GET("{path}")
    @Headers({"accept: application/json",
              "content-type: application/json"})
    Call<ResponseBody> cusAgree(@Path(value = "path", encoded = true) String path);
}
