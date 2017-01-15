package com.github.home.impl;

import com.github.home.DBRecordClass;
import com.github.home.User;
import com.github.home.abstracts.GenericDBRecordCache;
import com.github.home.abstracts.RecordSupplier;

import javax.jws.soap.SOAPBinding;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;


public class EventBasedCache extends GenericDBRecordCache<String,User>
{   private volatile static EventBasedCache cache;

    private Calendar calendar;
    final Set<String> set = new HashSet<String>();
    protected EventBasedCache(RecordSupplier<String, User> supplier) {
        super(supplier);
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }


    @Override
    public User supply(String args) throws InterruptedException
    {
        User user = super.supply(args);
        user.setLastAccess(new Date());
        return user;
    }
    private void clean()
    {
        Executors.newScheduledThreadPool(1)
                .scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
                        if(!set.isEmpty())
                            set.clear();
                        //collect the invalid entries in a set as we cannot delete entries from map while iteratinf over it
                     Map<String,FutureTask<User>> map = cache.getMap();
                     map.forEach((k,v)->
                     {
                         User user=null;
                         try {
                             if(v.isDone())
                                 user=v.get();
                         } catch (InterruptedException e) {
                             e.printStackTrace();
                         } catch (ExecutionException e) {
                             e.printStackTrace();
                         }
                         if(calendar!=null&&user!=null&&user.getLastAccess().getTime()<calendar.getTime().getTime())
                             set.add(k);
                     });

                     // now remove the invalid entries from the map
                        for (String str:set)
                              if(map.containsKey(str))
                                  map.remove(str);

                    }
                }, 7, 5, TimeUnit.SECONDS);
    }

    //singleton access
    public static EventBasedCache getCache()
    {
        if(cache==null)
        {
            synchronized (EventBasedCache.class)
            {
                if(cache==null)
                {
                    cache=new EventBasedCache(new DBRecordClass());
                    cache.clean();
                }
            }
        }
        return cache;
    }

    public Object clone()
    {
        try {
            throw new CloneNotSupportedException();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
