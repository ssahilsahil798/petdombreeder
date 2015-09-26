package com.petdom.breeder.http.operations;

import com.petdom.breeder.BreederApplication;
import com.petdom.breeder.events.*;
import com.petdom.breeder.events.Error;

/**
 * Created by diwakar.mishra on 9/24/2015.
 */
public abstract class Operation implements Runnable {
    private int event;

    protected Operation(int event) {
        this.event = event;
    }

    protected void onOperationSuccess(Object data) {
        final Event e = new Event(this.event, Event.STATUS_SUCCESS);
        e.setData(data);
        BreederApplication.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EventManager.getInstance().notify(e);
            }
        });
    }

    protected void onOperationError(Error error) {
        final Event e = new Event(this.event, Event.STATUS_ERROR);
        e.setError(error);
        BreederApplication.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EventManager.getInstance().notify(e);
            }
        });

    }
}
