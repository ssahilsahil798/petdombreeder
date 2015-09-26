package com.petdom.breeder.http.operations;

import android.util.Log;

import com.google.gson.Gson;
import com.petdom.breeder.events.Error;
import com.petdom.breeder.events.Event;
import com.petdom.breeder.http.HttpUtil;
import com.petdom.breeder.http.InvalidResponseCodeException;
import com.petdom.breeder.http.URLConstants;
import com.petdom.breeder.modal.DataController;
import com.petdom.breeder.modal.PetTypeList;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by diwakar.mishra on 9/24/2015.
 */
public class GetPetTypesOperation extends Operation {
    public GetPetTypesOperation() {
        super(Event.EVENT_DOWNLOAD_PET_TYPES);
    }

    @Override
    public void run() {
        String url = URLConstants.URL_GET_PET_TYPES + "?" + URLConstants.URL_DEFAULT_PARAMS_V1;

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
            PetTypeList list = gson.fromJson(data, PetTypeList.class);
            DataController.getInstance().setPetTypeList(list);
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
