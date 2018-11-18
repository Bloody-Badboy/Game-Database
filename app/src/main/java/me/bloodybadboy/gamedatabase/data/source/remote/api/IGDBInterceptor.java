package me.bloodybadboy.gamedatabase.data.source.remote.api;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;

public class IGDBInterceptor implements Interceptor {

  private String userKey;

  public IGDBInterceptor(String userKey) {
    this.userKey = userKey;
  }

  @Override public Response intercept(Chain chain) throws IOException {
    return chain.proceed(chain.request().newBuilder()
        .addHeader("Accept", "application/json")
        .addHeader("user-key", userKey)
        .build());
  }
}
