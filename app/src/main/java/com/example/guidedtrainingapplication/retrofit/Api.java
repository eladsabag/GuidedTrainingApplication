package com.example.guidedtrainingapplication.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Api {
    String BASE_URL = "https://avatars.dicebear.com/api/initials/";
    @GET
    Call<ResponseBody> getInitialsImage(@Url String url);
}
