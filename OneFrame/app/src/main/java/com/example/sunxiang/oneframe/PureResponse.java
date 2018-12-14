package com.example.sunxiang.oneframe;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sunxiang on 2018/12/13.
 */

public class PureResponse {
  @SerializedName("code") private int code;
  @SerializedName("data") private JsonObject data;
  @SerializedName("msg") private String msg;
}
