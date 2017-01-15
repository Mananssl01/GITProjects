package com.github.home;


import com.github.home.abstracts.RecordSupplier;

public class DBRecordClass implements RecordSupplier<String,User> {

    @Override
    public User supply(String args) {
        // waste time for 1 second.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new User(args);
    }
}


