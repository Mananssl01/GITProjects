package com.github.home.impl;



import com.github.home.DBRecordClass;
import com.github.home.User;
import com.github.home.abstracts.EvictionPolicy;
import com.github.home.abstracts.GenericDBRecordCache;
import com.github.home.abstracts.RecordSupplier;

import java.util.Date;


public class PolicyBasedCache extends GenericDBRecordCache<String,User> {

    private static final int MAX_NUMBER=3;
    private EvictionPolicy evictionPolicy;
    private volatile static PolicyBasedCache cache;

    protected PolicyBasedCache(RecordSupplier supplier) {
        super(supplier);
    }

     public void setEvictionPolicy(final EvictionPolicy evictionPolicy) {
        this.evictionPolicy = evictionPolicy;
    }

    @Override
    public User supply(String args) throws InterruptedException
    {
        if(this.evictionPolicy!=null)
            this.evictionPolicy.applyPolicy(args);
        User user = super.supply(args);
        // the following 2 lines (behaviours) must be in 2 different classes.Just extend this class n override the 'supply' method.
        // these 2 lines in one class voilates 'One class One responsibility' principle.
        user.setCount(1);                                    // for LFU
        user.setLastAccess(new Date());                      // for LRU
        return user;
    }

    //singleton access
    public static PolicyBasedCache getCache()
    {
     if(cache==null)
     {
         synchronized (PolicyBasedCache.class)
         {
             if(cache==null)
             {
                 cache=new PolicyBasedCache(new DBRecordClass());
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

    public static int getMaxNumber()
    {
        return MAX_NUMBER;
    }
}
