// IServiceWorker.aidl
package com.sum.andrioddeveloplibrary.service.aidl;

// Declare any non-default types here with import statements

interface IServiceWorker {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);

    int getIndex();

    void addSize(int size);
}
