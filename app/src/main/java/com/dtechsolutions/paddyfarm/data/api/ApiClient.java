package com.dtechsolutions.paddyfarm.data.api;

import com.dtechsolutions.paddyfarm.utils.TokenProvider.TokenProvider;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static ApiService apiService;
//    public final static String baseUrl = "http://10.0.2.2:3000/"; // when using emulator
    public final static String baseUrl = "https://f1d8-212-104-226-43.ngrok-free.app/"; // when using a real device

    public static ApiService getInstance(TokenProvider tokenProvider) {
        if (apiService == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(tokenProvider))
                    .addInterceptor(logging)
                    .writeTimeout(1000000, TimeUnit.MILLISECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);
        }

        return apiService;
    }

    public static EventSource createSseConnection(String relativeUrl, TokenProvider tokenProvider, EventSourceListener listener) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        OkHttpClient sseClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(0, TimeUnit.SECONDS)
                .addInterceptor(new AuthInterceptor(tokenProvider))
                .addInterceptor(logging)
                .build();

        Request request = new Request.Builder()
                .url(baseUrl + relativeUrl)
                .header("Accept", "text/event-stream")
                .header("Cache-Control", "no-cache")
                .build();

        return EventSources.createFactory(sseClient).newEventSource(request, listener);
    }
}
