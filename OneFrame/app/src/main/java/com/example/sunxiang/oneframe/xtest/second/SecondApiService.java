package com.example.sunxiang.oneframe.xtest.second;

import com.example.sunxiang.oneframe.BuildConfig;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by sunxiang on 2018/12/14.
 */
public interface SecondApiService {
  @POST("/product/home")
  Call<ResponseBody> getProducHome(@Header(BuildConfig.API_HEADER) String basicParams);

}
