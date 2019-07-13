package com.restoran.adapter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.restoran.Interfase.Api;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private final static String BaseUrl = "http://192.168.1.3";

    private static Gson gson = new GsonBuilder().setLenient().create();

    private static Retrofit getInstanse() {
        OkHttpClient client = new OkHttpClient();
        return new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }


    static Interceptor onlineInterceptor = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            okhttp3.Response response = chain.proceed(chain.request());
            int maxAge = 1;
            return response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .removeHeader("Pragma")
                    .build();
        }
    };
    private static Retrofit getCacheInstanse(Context ctx) {
        int size = 10 * 1024 * 1024;
        Cache cache = new Cache(ctx.getCacheDir(), size);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addNetworkInterceptor(onlineInterceptor)
                .build();
        return new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }


    public static Api getApi() {
        return getInstanse().create(Api.class);
    }


    public static Api getApiWithCache(Context c) {
        return getCacheInstanse(c).create(Api.class);
    }

}
