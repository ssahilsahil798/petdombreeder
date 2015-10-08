package com.petdom.breeder.http.operations;

import android.util.Log;

import com.google.gson.Gson;
import com.petdom.breeder.events.Error;
import com.petdom.breeder.events.Event;
import com.petdom.breeder.http.HttpUtil;
import com.petdom.breeder.http.InvalidResponseCodeException;
import com.petdom.breeder.http.URLConstants;
import com.petdom.breeder.modal.Breeder;
import com.petdom.breeder.modal.Dog;
import com.petdom.breeder.modal.Photo;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by diwakar.mishra on 9/24/2015.
 */
public class CreateDogOperation extends Operation {

    private Dog dog;
    private Breeder breeder;

    public CreateDogOperation(Dog dog) {
        super(Event.EVENT_CREATE_NEW_DOG);
        this.dog = dog;
        this.breeder = this.dog.getBreeder();
        this.dog.setBreeder(null);
    }

    @Override
    public void run() {
        String url = URLConstants.URL_CREATE_DOG + "?" + URLConstants.URL_DEFAULT_PARAMS_V2 + "&breeder=" + breeder.getId();

        try {
            Gson gson = new Gson();
            String body = gson.toJson(dog, Dog.class);
            JSONObject js = new JSONObject(body);
            js.remove("id");
            body = js.toString();
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            Response response = HttpUtil.post(url, headers, body);
            int code = response.code();
            if (code != 201) {
//                String error =response.body().string();
//                Log.d("HttpUtil","RESPONSE: "+ error);
                throw new InvalidResponseCodeException(code);
            }
            String str = response.body().string();
            JSONObject json = new JSONObject(str);
            int id = json.getInt("id");
            Log.d("HttpUtil", "RESPONSE: " + str);
            onOperationSuccess(id);
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
