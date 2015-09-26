package com.petdom.breeder.http.operations;

import com.petdom.breeder.events.Event;

import java.util.ArrayList;

/**
 * Created by diwakar.mishra on 9/26/2015.
 */
public class MultiOperation extends Operation {
    private ArrayList<Operation>queue;
    public MultiOperation() {
        super(Event.EVENT_NONE);
        this.queue = new ArrayList<>(5);
    }

    public void add(Operation operation){
        queue.add(operation);
    }
    @Override
    public void run() {
        for (int i=0;i<queue.size();i++){
            queue.get(i).run();
        }
    }
}
