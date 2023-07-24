package com.example.socialnetwork.api;

import okhttp3.*;

import java.io.IOException;

public class SendNotificationsExpo {
    public static void send(String token,String title, String data){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "to="+token+"&title="+title+
                "&body="+data+"&sound=default");
        Request request = new Request.Builder()
                .url("https://api.expo.dev/v2/push/send")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println(response.body() + "----------------Cal API");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
