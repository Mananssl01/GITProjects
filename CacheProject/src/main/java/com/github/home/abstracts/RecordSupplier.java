package com.github.home.abstracts;


public interface RecordSupplier<A,V> {

   public V supply(A args) throws InterruptedException;
}
