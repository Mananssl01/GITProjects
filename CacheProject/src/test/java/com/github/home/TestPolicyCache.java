package com.github.home;

import com.github.home.impl.LFUEvictionPolicy;
import com.github.home.impl.LRUEvictionPolicy;
import com.github.home.impl.PolicyBasedCache;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.Map;


public class TestPolicyCache {
    PolicyBasedCache cache= PolicyBasedCache.getCache();
    String[]array = {"a","c","a","a","b","b","c","e","f"};


    @Test
    public void testLRUPolicy() throws InterruptedException
    {
        Map<String,User> map = cache.getMap();
        if(!map.isEmpty())
            map.clear();
        cache.setEvictionPolicy(new LRUEvictionPolicy());
        // record time taken buy cache
        Date start = new Date();
        for (String i:array)
            cache.supply(i);
        Date end = new Date();

        // these entries should be in map
        Assert.assertTrue(map.containsKey("f"));
        Assert.assertTrue(map.containsKey("e"));
        Assert.assertTrue(map.containsKey("c"));
        // these entries should be deleted.
        Assert.assertFalse(map.containsKey("a"));
        Assert.assertFalse(map.containsKey("b"));

         /*since for each element in the array , thread sleeps for 1 sec,total time to process
        all entries will be 1000*array.lenght*/

        Assert.assertTrue(end.getTime()-start.getTime()<(1000*array.length));

    }

    @Test
    public void testLFUPolicy() throws InterruptedException
    {
        Map<String,User> map = cache.getMap();
        if(!map.isEmpty())
            map.clear();
        cache.setEvictionPolicy(new LFUEvictionPolicy());
        Date start = new Date();
        for (String i:array)
            cache.supply(i);
        Date end = new Date();


        // these entries should be in map
        Assert.assertTrue(map.containsKey("f"));
        Assert.assertTrue(map.containsKey("a"));
        Assert.assertTrue(map.containsKey("c")||map.containsKey("b"));
        // these entries should be deleted.
        Assert.assertFalse(map.containsKey("e"));

        /*since for each element in the array , thread sleeps for 1 sec,total time to process
        all entries will be 1000*array.lenght*/

        Assert.assertTrue(end.getTime()-start.getTime()<(1000*array.length));


    }
}
