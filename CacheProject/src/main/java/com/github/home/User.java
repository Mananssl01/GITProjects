package com.github.home;

import java.util.Date;

public class User {


        private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count += count;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    // although bad design , put meta data withinthe object only
        private int count;
        private Date lastAccess;
        public User(String name) {
            this.name = name;
        }







}
