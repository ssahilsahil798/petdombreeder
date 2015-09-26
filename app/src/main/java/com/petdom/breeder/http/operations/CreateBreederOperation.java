package com.petdom.breeder.http.operations;

import android.util.Log;

import com.google.gson.Gson;
import com.petdom.breeder.events.Error;
import com.petdom.breeder.events.Event;
import com.petdom.breeder.http.HttpUtil;
import com.petdom.breeder.http.InvalidResponseCodeException;
import com.petdom.breeder.http.URLConstants;
import com.petdom.breeder.modal.Breeder;
import com.squareup.okhttp.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by diwakar.mishra on 9/24/2015.
 */
public class CreateBreederOperation extends Operation {
    private Breeder breeder;

    public CreateBreederOperation(Breeder breeder) {
        super(Event.EVENT_CREATE_NEW_BREEDER);
        this.breeder = breeder;
    }

    @Override
    public void run() {
        String url = URLConstants.URL_GET_BREEDERS + "/?" + URLConstants.URL_DEFAULT_PARAMS_V2;

        try {
            Gson gson=new Gson();
            String body = gson.toJson(breeder, Breeder.class);

            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            Response response = HttpUtil.post(url, headers,body);
            int code = response.code();
            if (code != 201) {
                throw new InvalidResponseCodeException(code);
            }
            onOperationSuccess(null);
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
