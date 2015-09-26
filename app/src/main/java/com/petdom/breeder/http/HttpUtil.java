package com.petdom.breeder.http;

import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.spdy.Header;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by diwakar.mishra on 9/24/2015.
 */
public class HttpUtil {

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private static final String TAG = HttpUtil.class.getSimpleName();

    public static Response get(String url, HashMap<String, String> headers) throws IOException {
        Log.d(TAG, url);
        Request.Builder builder = new Request.Builder();

        //add url
        builder.url(url);

        //add headers
        if (headers != null && headers.size() > 0) {
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            Iterator<Map.Entry<String, String>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Request request = builder.build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        Log.d(TAG, response.toString());
        return response;
    }

    public static Response post(String url, HashMap<String, String> headers, String requestJson) throws IOException {

        Log.d(TAG, url);
        Log.d(TAG, requestJson);
        Request.Builder builder = new Request.Builder();

        //add url
        builder.url(url);

        //add body post data
        builder.post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestJson));

        //add headers
        if (headers != null && headers.size() > 0) {
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            Iterator<Map.Entry<String, String>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Request request = builder.build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        Log.d(TAG, response.toString());
        return response;
    }
}
