package com.sum.cache.disk;

import com.esotericsoftware.kryo.Serializer;

import java.io.File;
import java.io.OutputStream;

import com.sum.cache.disk.impl.CacheTable;

public interface StoragePlainFile {
    <V> boolean writeTableFile(String key, CacheTable<V> paperTable, File originalFile, File backupFile);
    

    <V> boolean writeTableFile(String key, CacheTable<V> paperTable, OutputStream outputStream);

    <V> V readTableFile(String key, File originalFile);

    <T> void addSerializer(Class<T> clazz, Serializer<T> serializer);
}
