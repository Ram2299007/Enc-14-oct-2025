package com.Appzia.enclosure.Utils.WebserviceRetrofits;

import com.Appzia.enclosure.Utils.Webservice;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;
    private static OkHttpClient client = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            clientBuilder.connectTimeout(5, TimeUnit.MINUTES); // Connection timeout
            clientBuilder.readTimeout(5, TimeUnit.MINUTES);    // Read timeout
            client = clientBuilder.build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(Webservice.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                    .client(client)
                    .build();
        }
        return retrofit;
    }


    public static void cancelAllRequests() {
        if (client != null) {
            client.dispatcher().cancelAll();
        }
    }
}
