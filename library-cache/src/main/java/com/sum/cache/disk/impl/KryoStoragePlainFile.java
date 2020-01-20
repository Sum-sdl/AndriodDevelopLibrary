package com.sum.cache.disk.impl;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;

import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import com.sum.cache.disk.StoragePlainFile;
import com.sum.cache.disk.serializer.ArraysAsListSerializer;
import com.sum.cache.disk.serializer.NoArgCollectionSerializer;
import com.sum.cache.disk.serializer.SynchronizedCollectionsSerializer;
import com.sum.cache.disk.serializer.UnmodifiableCollectionsSerializer;
import com.sum.cache.util.CacheLog;

public class KryoStoragePlainFile implements StoragePlainFile {
    private static final HashMap<Class, Serializer> sCustomSerializers = new HashMap<>();

    private Kryo getKryo() {
        return mKryo.get();
    }

    private final ThreadLocal<Kryo> mKryo = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            return createKryoInstance(false);
        }
    };

    private Kryo createKryoInstance(boolean compatibilityMode) {
        Kryo kryo = new Kryo();

        if (compatibilityMode) {
            kryo.getFieldSerializerConfig().setOptimizedGenerics(true);
        }

        kryo.register(CacheTable.class);
        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        kryo.setReferences(false);

        // Serialize Arrays$ArrayList
        //noinspection ArraysAsListWithZeroOrOneArgument
        kryo.register(Arrays.asList("").getClass(), new ArraysAsListSerializer());
        UnmodifiableCollectionsSerializer.registerSerializers(kryo);
        SynchronizedCollectionsSerializer.registerSerializers(kryo);
        // Serialize inner AbstractList$SubAbstractListRandomAccess
        kryo.addDefaultSerializer(new ArrayList<>().subList(0, 0).getClass(),
                new NoArgCollectionSerializer());
        // Serialize AbstractList$SubAbstractList
        kryo.addDefaultSerializer(new LinkedList<>().subList(0, 0).getClass(),
                new NoArgCollectionSerializer());
        // To keep backward compatibility don't change the order of serializers above

        // UUID support
        // kryo.register(UUID.class, new UUIDSerializer());

        for (Class<?> clazz : sCustomSerializers.keySet()) {
            kryo.register(clazz, sCustomSerializers.get(clazz));
        }

        kryo.setInstantiatorStrategy(
                new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));

        return kryo;
    }

    /**
     * Attempt to write the file, delete the backup and return true as atomically as
     * possible.  If any exception occurs, delete the new file; next time we will restore
     * from the backup.
     *
     * @param key          table key
     * @param paperTable   table instance
     * @param originalFile file to write new data stream
     */
    public <E> boolean writeTableFile(String key, CacheTable<E> paperTable,
            OutputStream originalFile) {
        boolean savedSuccessfully;
        try {
            final Output kryoOutput = new Output(originalFile);
            getKryo().writeObject(kryoOutput, paperTable);
            kryoOutput.flush();
            kryoOutput.close();

            //noinspection ResultOfMethodCallIgnored
            savedSuccessfully = true;
        } catch (KryoException e) {
            // Clean up an unsuccessfully written file

            CacheLog.e("Couldn't save table: " + key + ". " +
                    "Backed up table will be used on next read attempt");
            savedSuccessfully = false;
        }
        return savedSuccessfully;
    }

    /**
     * Attempt to write the file, delete the backup and return true as atomically as
     * possible.  If any exception occurs, delete the new file; next time we will restore
     * from the backup.
     *
     * @param key          table key
     * @param paperTable   table instance
     * @param originalFile file to write new data
     * @param backupFile   backup file to be used if write is failed
     */
    public <E> boolean writeTableFile(String key, CacheTable<E> paperTable,
            File originalFile, File backupFile) {
        boolean savedSuccessfully;
        try {
            FileOutputStream fileStream = new FileOutputStream(originalFile);

            final Output kryoOutput = new Output(fileStream);
            getKryo().writeObject(kryoOutput, paperTable);
            kryoOutput.flush();
            fileStream.flush();
            sync(fileStream);
            kryoOutput.close(); //also close file stream

            // Writing was successful, delete the backup file if there is one.
            //noinspection ResultOfMethodCallIgnored
            backupFile.delete();
            savedSuccessfully = true;
        } catch (IOException | KryoException e) {
            // Clean up an unsuccessfully written file
            if (originalFile.exists()) {
                if (!originalFile.delete()) {
                    CacheLog.e("Couldn't clean up partially-written file ");
                }
            }
            CacheLog.e("Couldn't save table: " + key + ". " +
                    "Backed up table will be used on next read attempt");
            savedSuccessfully = false;
        }
        return savedSuccessfully;
    }

    public <E> E readTableFile(String key, File originalFile) {
        try {
            return readContent(originalFile, getKryo());
        } catch (FileNotFoundException | KryoException | ClassCastException e) {
            Throwable exception = e;
            // Give one more chance, read data in paper 1.x compatibility mode
            if (e instanceof KryoException) {
                try {
                    return readContent(originalFile, createKryoInstance(true));
                } catch (FileNotFoundException | KryoException | ClassCastException
                        compatibleReadException) {
                    exception = compatibleReadException;
                }
            }
            String errorMessage = "Couldn't read/deserialize file "
                    + originalFile + " for table " + key;
            CacheLog.e(errorMessage);
            return null;
        }
    }

    @Override
    public <T> void addSerializer(Class<T> clazz, Serializer<T> serializer) {
        if (!sCustomSerializers.containsKey(clazz)) {
            sCustomSerializers.put(clazz, serializer);
        }
    }

    private <E> E readContent(File originalFile, Kryo kryo)
            throws FileNotFoundException, KryoException {
        final Input i = new Input(new FileInputStream(originalFile));
        //noinspection TryFinallyCanBeTryWithResources
        try {
            //noinspection unchecked
            final CacheTable<E> paperTable = kryo.readObject(i, CacheTable.class);
            return paperTable.mContent;
        } finally {
            i.close();
        }
    }

    /**
     * Perform an fsync on the given FileOutputStream.  The stream at this
     * point must be flushed but not yet closed.
     */
    private static void sync(FileOutputStream stream) {
        //noinspection EmptyCatchBlock
        try {
            if (stream != null) {
                stream.getFD().sync();
            }
        } catch (IOException e) {
        }
    }

}
