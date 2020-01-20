package com.sum.cache.disk.impl;

public class CacheTable<T> {

    @SuppressWarnings("UnusedDeclaration")
    public CacheTable() {
    }

    public CacheTable(T content) {
        mContent = content;
    }

    // Serialized content
    T mContent;
}
