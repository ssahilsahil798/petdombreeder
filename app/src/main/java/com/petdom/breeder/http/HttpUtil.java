package com.petdom.breeder.http;

import android.graphics.Bitmap;
import android.util.Log;

import com.petdom.breeder.modal.Photo;
import com.petdom.breeder.utils.UiUtils;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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


    public static Response upload(String url, Photo photo) throws IOException {

        Log.d(TAG, url);

        MultipartBuilder multipartBuilder = new MultipartBuilder()
                .type(MultipartBuilder.FORM);


        Bitmap bmp = UiUtils.scaleImage(photo.getLocalPath(), 640, 640);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        byte[]data = bos.toByteArray();
        bos.close();
        Log.d(TAG,"IMAGE: "+photo.getKey()+"  SIZE= "+(data.length/1024)+" KB");

        File file = new File(photo.getLocalPath());
        multipartBuilder.addFormDataPart(photo.getKey(), file.getName(),
                     RequestBody.create(MediaType.parse("image/jpeg"),data,0,data.length));

        RequestBody requestBody = multipartBuilder.build();

        Request.Builder builder = new Request.Builder();
        //add url
        builder.url(url);

        //add body post data
        builder.post(requestBody);


        Request request = builder.build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        Log.d(TAG, response.toString());
        return response;
    }



}
