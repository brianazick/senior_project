package com.example.myfirstapp;

import com.loopj.android.http.*;

public class ParticleRestClient {
    private static final String FULL_URL = "https://api.particle.io/v1/devices/56002d001950483553353620/mosfet -d access_token=013b79d222f5e7dd12698c8ad79684755d8468cf -d arg=\"power_button\"";
    private static final String BASE_URL = "https://api.particle.io/v1/devices/56002d001950483553353620/mosfet";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post_full_url(AsyncHttpResponseHandler responseHandler) {
        client.post(BASE_URL, null, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}