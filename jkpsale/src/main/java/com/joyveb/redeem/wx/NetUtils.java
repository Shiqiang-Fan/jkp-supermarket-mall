package com.joyveb.redeem.wx;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class NetUtils {



    public static String get(String url) {
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().url(url)
                .get().build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            String s = response.body().string();
            System.out.println(s);
            return s;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
