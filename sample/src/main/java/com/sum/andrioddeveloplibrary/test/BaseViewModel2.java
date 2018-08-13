package com.sum.andrioddeveloplibrary.test;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.sum.library.domain.BaseRepository;

/**
 * Created by sdl on 2018/8/13.
 */
public class BaseViewModel2 {


    private BaseRepository repository;

    @NonNull
    @MainThread
    public <T extends BaseRepository> T get(@NonNull Class<T> modelClass) {
        String canonicalName = modelClass.getCanonicalName();
        if (canonicalName == null) {
            throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
        }
        return create(modelClass);
    }


    @SuppressWarnings("ClassNewInstance")
    @NonNull
    public <T extends BaseRepository> T create(@NonNull Class<T> modelClass) {
        //noinspection TryWithIdenticalCatches
        try {
            return modelClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        }
    }


}
