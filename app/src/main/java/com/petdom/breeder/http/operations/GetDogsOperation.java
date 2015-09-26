package com.petdom.breeder.http.operations;

import android.util.Log;

import com.google.gson.Gson;
import com.petdom.breeder.events.Error;
import com.petdom.breeder.events.Event;
import com.petdom.breeder.http.HttpUtil;
import com.petdom.breeder.http.InvalidResponseCodeException;
import com.petdom.breeder.http.URLConstants;
import com.petdom.breeder.modal.Breeder;
import com.petdom.breeder.modal.BreederList;
import com.petdom.breeder.modal.DataController;
import com.petdom.breeder.modal.Dog;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by diwakar.mishra on 9/24/2015.
 */
public class GetDogsOperation extends Operation {
    private Breeder breeder;

    public GetDogsOperation(Breeder breeder) {
        super(Event.EVENT_DOWNLOAD_DOGS);
        this.breeder = breeder;
    }

    @Override
    public void run() {
        String url = URLConstants.URL_GET_DOGS + "?" + URLConstants.URL_DEFAULT_PARAMS_V1 + "&limit=100&breeder=" + breeder.getId();

        try {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            Response response = HttpUtil.get(url, headers);
            int code = response.code();
            if (code==204){
                //no dogs under this breeder
                onOperationSuccess(null);
                return;
            }
            if (code != 200) {
                throw new InvalidResponseCodeException(code);
            }
            String data = response.body().string();
            Log.d("HttpUtil", data);
            Gson gson = new Gson();
            DogList list = gson.fromJson(data, DogList.class);
            ArrayList<Dog> al = new ArrayList<>(Arrays.asList(list.objects));
            breeder.setDogs(al);
            onOperationSuccess(al);
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

    private static class DogList {
        private Dog[] objects;
    }

}
