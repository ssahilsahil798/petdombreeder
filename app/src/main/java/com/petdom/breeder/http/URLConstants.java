package com.petdom.breeder.http;

import com.petdom.breeder.AppConfig;
import com.petdom.breeder.modal.DataController;

/**
 * Created by diwakar.mishra on 9/24/2015.
 */
public class URLConstants {

    private static final String BASE_URL_DEVELOPMENT="http://169.54.252.164:8000/api/";
    private static final String BASE_URL_PRODUCTION="http://169.54.252.164:8000/api/";
    public static final String BASE_URL= AppConfig.APP_PRODUCTION?BASE_URL_PRODUCTION:BASE_URL_DEVELOPMENT;

    public static final String RESOURCE_DOGBREED = "v1/dogbreed/";
    public static final String URL_GET_BREEDERS = BASE_URL +"v1/breeder/";
    public static final String URL_GET_DOG_BREEDS = BASE_URL +RESOURCE_DOGBREED;
    public static final String URL_GET_PET_TYPES = BASE_URL +"v1/pettypes/";
    public static final String URL_GET_DOG_COLORS = BASE_URL +"v1/dogcolors/";
    public static final String URL_GET_DOG_COLOR_PATTERNS = BASE_URL +"v1/dogpatterns/";
    public static final String URL_CREATE_DOG = BASE_URL +"v1/dog/";
    public static final String URL_GET_DOGS = BASE_URL +"v1/dog/breederdogs/";




    public static final String URL_DEFAULT_PARAMS_V1 = createDefaultParamsV1();
    private static String createDefaultParamsV1() {
        StringBuilder sb = new StringBuilder();
                sb.append("username=").append(DataController.getInstance().getUser().getName());
                sb.append('&');
        sb.append("api_key=").append(DataController.getInstance().getUser().getApiKey());
        sb.append('&');
        sb.append("format=").append("json");
        return sb.toString();
    }
    public static final String URL_DEFAULT_PARAMS_V2 = createDefaultParamsV2();
    private static String createDefaultParamsV2() {
        StringBuilder sb = new StringBuilder();
        sb.append("username=").append(DataController.getInstance().getUser().getName());
        sb.append('&');
        sb.append("api_key=").append(DataController.getInstance().getUser().getApiKey());
        return sb.toString();
    }
}
