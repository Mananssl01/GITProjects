package com.github.home.abstracts;


public interface EvictionPolicy<T> {

    public void applyPolicy(T t);
}
