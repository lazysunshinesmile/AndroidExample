package com.sun.rxjava2examples.util;

import com.sun.rxjava2examples.model.FoodList;

/**
 * 缓存管理器
 *
 * Author: sun
 */

public class CacheManager {
    private static CacheManager instance;
    private FoodList footListJson;

    private CacheManager(){}

    public static CacheManager getInstance(){
        if (instance == null){
            instance = new CacheManager();
        }
        return instance;
    }

    public FoodList getFoodListData(){
        return this.footListJson;
    }

    public void setFoodListData(FoodList data){
        this.footListJson = data;
    }
}
