package com.sum.cache.entity;

import com.sum.cache.LimitedAge;

public class CacheEntity <V>  {

    LimitedAge mLimitedAge;
    V mValue;
    public CacheEntity(LimitedAge limitedAge, V value) {
        mLimitedAge = limitedAge;
        mValue = value;
    }

    public LimitedAge getLimitedAge() {
        return mLimitedAge;
    }

    public V getValue() {
        return mValue;
    }
}
