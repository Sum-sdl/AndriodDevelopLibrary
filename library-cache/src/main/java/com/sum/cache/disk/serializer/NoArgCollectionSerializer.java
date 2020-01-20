package com.sum.cache.disk.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;

import java.util.ArrayList;
import java.util.Collection;

public class NoArgCollectionSerializer extends CollectionSerializer {
    @Override
    protected Collection create(Kryo kryo, Input input, Class<Collection> type) {
        return new ArrayList();
    }
}
