package com.petdom.breeder;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.petdom.breeder.http.PhotoUploader;
import com.petdom.breeder.http.operations.GetDogBreedsOperation;
import com.petdom.breeder.http.operations.GetDogColorPatternsOperation;
import com.petdom.breeder.http.operations.GetDogColorsOperation;
import com.petdom.breeder.http.operations.GetPetTypesOperation;
import com.petdom.breeder.http.operations.MultiOperation;
import com.petdom.breeder.modal.DataController;
import com.petdom.breeder.modal.User;
import com.petdom.breeder.utils.BackgroundExecutor;
import com.petdom.breeder.utils.LocationTracker;


/**
 * Created by diwakar.mishra on 9/21/2015.
 */
public class BreederApplication extends Application {

    private static BreederApplication instance;
    private Handler handler;
    public static BreederApplication getInstance() {
        if (instance == null) {
            throw new IllegalStateException("BreederApplication instance not initialized!");
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //create default user
        if (AppConfig.APP_PRODUCTION){
            User user = DataController.getInstance().createUser("apiuser","");
            user.setApiKey("304130121297b06377293215977b68183493a771");

        }else {

            User user = DataController.getInstance().createUser("ppp3","");
            user.setApiKey("170a41767f6ba69112aa51629e65490266110c7a");
        }

        //Init handler for UI posts
        handler= new Handler(Looper.getMainLooper());

        //start multiple operation
        MultiOperation operation =new MultiOperation();
        operation.add(new GetPetTypesOperation());
        operation.add(new GetDogBreedsOperation());
        operation.add(new GetDogColorsOperation());
        operation.add(new GetDogColorPatternsOperation());
        BackgroundExecutor.getInstance().run(operation);

        PhotoUploader.getInstance().prepare();



    }
    public void runOnUiThread(Runnable runnable){
        handler.post(runnable);
    }
    public void runOnUiThread(Runnable runnable,long delay){
        handler.postDelayed(runnable,delay);
    }

}
