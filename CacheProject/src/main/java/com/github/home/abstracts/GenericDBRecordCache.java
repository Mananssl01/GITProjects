package com.github.home.abstracts;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public abstract class GenericDBRecordCache<A,V> implements RecordSupplier<A,V>
{
    private RecordSupplier<A,V> real;
    protected GenericDBRecordCache(RecordSupplier<A,V> supplier)
    {
        this.real=supplier;
    }
    protected   Map<A,Future<V>> map = new ConcurrentHashMap<A,Future<V>>();

    @Override
    public V supply(A args) throws InterruptedException {
        Future<V> future= map.get(args);
        if(future==null)
        {
            Callable<V> called = new Callable<V>() {
                public V call() throws InterruptedException {
                    return real.supply(args); }};

               FutureTask<V> task = new FutureTask<V>(called);
                future=map.putIfAbsent(args,task);     // atomic instruction for concurrent accesses.
                if(future==null)
                {
                    future=task;
                    task.run();
                }
        }
        try {
            return future.get();
        } catch (ExecutionException e) {
            map.remove(args);
            e.printStackTrace();
        }
            return  null;
    }

    public Map getMap()
    {
        return map;
    }


}
