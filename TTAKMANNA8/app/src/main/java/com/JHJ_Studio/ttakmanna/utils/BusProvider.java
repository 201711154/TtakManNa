package com.JHJ_Studio.ttakmanna.utils;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class BusProvider {
    public static final Bus bus = new Bus(ThreadEnforcer.ANY);

    public static Bus getInstance(){
        return bus;
    }

    public BusProvider() {

    }
}
