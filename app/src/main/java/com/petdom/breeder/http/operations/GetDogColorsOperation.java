package com.petdom.breeder.http.operations;

import android.util.Log;

import com.google.gson.Gson;
import com.petdom.breeder.events.Error;
import com.petdom.breeder.events.Event;
import com.petdom.breeder.http.HttpUtil;
import com.petdom.breeder.http.InvalidResponseCodeException;
import com.petdom.breeder.http.URLConstants;
import com.petdom.breeder.modal.ColorList;
import com.petdom.breeder.modal.DataController;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by diwakar.mishra on 9/24/2015.
 */
public class GetDogColorsOperation extends Operation {
    public GetDogColorsOperation() {
        super(Event.EVENT_DOWNLOAD_DOG_COLORS);
    }

    @Override
    public void run() {
        String url = URLConstants.URL_GET_DOG_COLORS + "?" + URLConstants.URL_DEFAULT_PARAMS_V1;

        try {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            Response response = HttpUtil.get(url, headers);
            int code = response.code();
            if (code != 200) {
                throw new InvalidResponseCodeException(code);
            }
            String data = response.body().string();
            Log.d("HttpUtil", data);
            Gson gson = new Gson();
            ColorList list = gson.fromJson(data, ColorList.class);
            DataController.getInstance().setColorList(list);
            onOperationSuccess(list);
        } catch (IOException e) {
            Error error = new Error(Error.TYPE_INTERNET_CONNECTION, e);
            onOperationError(error);
        } catch (InvalidResponseCodeException e) {
            Error error = new Error(Error.TYPE_INVALID_RESPONSE_CODE, e);
            onOperationError(error);
        } catch (Exception e) {
            Error error = new Error(Error.TYPE_UNKNOWN, e);
            onOperationError(error);
        }
    }
}
