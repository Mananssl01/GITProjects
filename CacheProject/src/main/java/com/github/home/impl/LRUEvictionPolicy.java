package com.github.home.impl;



import com.github.home.User;
import com.github.home.abstracts.EvictionPolicy;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class LRUEvictionPolicy implements EvictionPolicy<String> {
    @Override
    public void applyPolicy(String str) {
        PolicyBasedCache cache = PolicyBasedCache.getCache();
        Map<String,Future<User>> map = cache.getMap();
        if(map.size()>=PolicyBasedCache.getMaxNumber())
        {
            long min = Long.MAX_VALUE;
            String key=null;User user=null;
            for (Future<User> future :map.values())
            {
                try {
                    if(future.isDone())
                        user=future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if(user!=null&&user.getLastAccess().getTime()<min)
                {
                    min=user.getLastAccess().getTime();
                    key=user.getName();
                }
            }
            if(!str.equals(key))
                map.remove(key);
        }

    }
}
