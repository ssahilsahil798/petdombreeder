package com.petdom.breeder.events;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by diwakar.mishra on 9/24/2015.
 */
public class EventManager {

    private static final EventManager INSTANCE = new EventManager();
    private Bus bus;
    private  EventManager(){
        this.bus = new Bus(ThreadEnforcer.MAIN);
    }
    public static EventManager getInstance(){
        return INSTANCE;
    }
    public void register(EventListener listener){
        this.bus.register(listener);
    }
    public void remove(EventListener listener){
        this.bus.unregister(listener);
    }
    public void notify(Event event){
        this.bus.post(event);
    }

}
