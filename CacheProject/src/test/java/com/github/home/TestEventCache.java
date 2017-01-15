package com.github.home;

import com.github.home.impl.EventBasedCache;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Map;

/**
 * Created by Manan on 1/10/2017.
 */
public class TestEventCache {
    EventBasedCache cache = EventBasedCache.getCache();
    String[]array = {"a","b","c","d","g","e","a","e","f"};

    @Test
    public void testEventWithoutTimer() throws InterruptedException
    {
        Map<String,User> map = cache.getMap();
        if(!map.isEmpty())
               map.clear();
        // put things into map
        for (String str :array)
        {
            cache.supply(str);
        }
        //cache should have all the entries as eviction time was not supplied.
        Assert.assertTrue(map.size()==7);
    }

    @Test
    public void testEventAfterTimer() throws InterruptedException
    {
        Map<String,User> map = cache.getMap();
        if(!map.isEmpty())
            map.clear();

        Calendar calendar = Calendar.getInstance();
        cache.setCalendar(calendar);
        // put things into map
        for (String str :array)
                cache.supply(str);

        //since all the entries are supplied AFTER the timer is set, all entries should be there
        Assert.assertTrue(map.size()==7);
    }

    @Test
    public void testEventBeforeTimer() throws InterruptedException
    {
        Map<String,User> map = cache.getMap();
        if(!map.isEmpty())
            map.clear();
        // put things into map
        for (String str :array)
            cache.supply(str);
        // set timer here
        Calendar calendar = Calendar.getInstance();
        cache.setCalendar(calendar);
        //since all the entries are supplied BEFORE the timer is set, some entries can be removed by event thread
        // Here we are sleeping the current thread so that other thread can get a chance for some cleanup, though
        // this is not a guaranteed solution.
        Thread.sleep(3000);

        Assert.assertTrue(map.size()<7);
    }
}
