

package com.sum.cache;

import com.esotericsoftware.kryo.Serializer;

import java.io.Serializable;

import com.sum.cache.util.CacheLog;

/**
 * 缓存加载管理者
 */
public class CacheManager {
    private volatile static CacheManager sInstance;

    private CacheConfiguration mCacheLoaderConfiguration;
    private CacheEngine mCacheEngine;

    /**
     * 基础的缓存加载任务
     */
    private CacheHelper mCacheHelper;

    public static CacheManager getInstance() {
        if (sInstance == null) {
            synchronized (CacheManager.class) {
                if (sInstance == null) {
                    sInstance = new CacheManager();
                }
            }
        }
        return sInstance;
    }

    private CacheManager() {

    }


    /**
     * 初始化缓存配置
     *
     * @param cacheLoaderConfiguration 初始化配置
     * @see CacheConfiguration
     */
    public void init(CacheConfiguration cacheLoaderConfiguration) {
        if (this.mCacheLoaderConfiguration != null) {
            this.mCacheLoaderConfiguration.close();
            this.mCacheLoaderConfiguration = null;
        }
        this.mCacheLoaderConfiguration = cacheLoaderConfiguration;
        mCacheEngine = new CacheEngine(cacheLoaderConfiguration);
        mCacheHelper = new CacheHelper(
                cacheLoaderConfiguration.getLimitAgeDiskCache(),
                cacheLoaderConfiguration.getLimitedAgeMemoryCache(),
                cacheLoaderConfiguration.getStoragePlainFile());
    }

    /***
     * 加载缓存中POJO对象数据
     *
     * @param key 缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @return 返回关键字Key对应的数据
     */
    public <V> V load(String key) {
        if (!isInitialize()) {
            return null;
        }
        return load(key, null);
    }

    /***
     * 加载缓存中POJO对象数据
     *
     * @param key 缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param defaultValue 默认值
     * @return 返回关键字Key对应的字节数组
     */
    public <V> V load(String key, V defaultValue) {
        if (!isInitialize()) {
            return defaultValue;
        }
        V value = mCacheHelper.query(key);
        return value == null ? defaultValue : value;
    }

    /***
     * 加载缓存中POJO对象数据
     *
     * @param key           缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param isCacheMemory 读取磁盘中缓存的数据，是否缓存到内存
     * @return 返回关键字Key对应的数据，
     */
    public  <V> V load(String key, boolean isCacheMemory) {
        if (!isInitialize()) {
            return null;
        }
        return load(key, null, isCacheMemory);
    }

    /***
     * 加载缓存中POJO对象数据,如果无相关数据，则返回默认值
     *
     * @param key           缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param defaultValue 默认值
     * @param isCacheMemory 读取磁盘中缓存的数据，是否缓存到内存
     * @return 返回关键字Key对应的数据，
     */
    public <V> V load(String key, V defaultValue, boolean isCacheMemory) {
        if (!isInitialize()) {
            return defaultValue;
        }
        V value = mCacheHelper.query(key, isCacheMemory);
        return value == null ? defaultValue : value;
    }

    /***
     * 加载缓存中对应的字节数组
     *
     * @param key 缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @return 返回关键字Key对应的字节数组
     * @see CacheManager#load(String)
     */
    public byte[] loadBytes(String key) {
        return load(key);
    }

    /***
     * 加载缓存中对应的字节数组
     *
     * @param key           缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param isCacheMemory 读取磁盘中缓存的数据，是否缓存到内存
     * @return 返回关键字Key对应的字节数组
     * @see CacheManager#load(String)
     */
    public byte[] loadBytes(String key, boolean isCacheMemory) {
        return load(key, isCacheMemory);
    }


    /***
     * 加载缓存中对应的字节数组
     *
     * @param key           缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @return 返回关键字Key对应的int类型值, 如果不存在返回0
     *
     */

    public int loadInt(String key) {
        return load(key, 0);
    }

    /***
     * 加载缓存中对应的字节数组
     *
     * @param key           缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param isCacheMemory 读取磁盘中缓存的数据，是否缓存到内存
     * @return 返回关键字Key对应的int类型值, 如果不存在返回0
     */

    public int loadInt(String key, boolean isCacheMemory) {
        return load(key, 0, isCacheMemory);
    }

    /***
     * 加载缓存中对应的float类型
     *
     * @param key 缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @return 返回关键字Key对应的float类型数据, 如果不存在返回0
     */

    public float loadFloat(String key) {
        return load(key, 0F);
    }

    /***
     * 加载缓存中对应的字节数组
     *
     * @param key           缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param isCacheMemory 读取磁盘中缓存的数据，是否缓存到内存，如果不存在返回0
     * @return 返回关键字Key对应的float类型数据，如果不存在返回0
     */

    public float loadFloat(String key, boolean isCacheMemory) {
        return load(key, 0F, isCacheMemory);
    }

    /***
     * 加载缓存中对应的double类型
     *
     * @param key 缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @return 返回关键字Key对应的double类型值, 如果不存在返回0
     */

    public double loadDouble(String key) {
        return load(key, 0.00);
    }

    /***
     * 加载缓存中对应的double类型
     *
     * @param key           缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param isCacheMemory 读取磁盘中缓存的数据，是否缓存到内存
     * @return 返回关键字Key对应的double类型值, 如果不存在返回0
     */

    public double loadDouble(String key, boolean isCacheMemory) {
        return load(key, 0.00, isCacheMemory);
    }

    /***
     * 加载缓存中对应的boolean类型
     *
     * @param key 缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @return 返回关键字Key对应的boolean类型值, 如果不存在返回false
     */

    public boolean loadBoolean(String key) {
        return load(key, false,false);
    }

    /***
     * 加载缓存中对应的boolean类型
     *
     * @param key           缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param isCacheMemory 读取磁盘中缓存的数据，是否缓存到内存
     * @return 返回关键字Key对应的boolean类型值, 如果不存在返回false
     */

    public boolean loadBoolean(String key, boolean isCacheMemory) {
        return load(key, false, isCacheMemory);
    }


    /**
     * 加载字符串
     *
     * @param key 缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @return 对应的缓存数据String
     * @see CacheManager#load(String)
     */
    public String loadString(String key) {
        return load(key);
    }

    /**
     * 加载字符串
     *
     * @param key           缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param isCacheMemory 读取磁盘中缓存的数据，是否缓存到内存
     * @return 对应的缓存数据String
     * @see CacheManager#load(String)
     */
    public String loadString(String key, boolean isCacheMemory) {
        return load(key, isCacheMemory);
    }

    /**
     * 获取Serializable序列化对象，已经废弃，请使用{@link CacheManager#load(String)}方式
     *
     * @param key 缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @return 关键字KEY对应的序列化对象
     * @see CacheManager#load(String)
     */
    @Deprecated
    public <V extends Serializable> V loadSerializable(String key) {
        return load(key);
    }

    /**
     * 获取Serializable序列化对象,已经废弃，请使用{@link CacheManager#load(String)}方式
     *
     * @param key           缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param isCacheMemory 读取磁盘中缓存的数据，是否缓存到内存
     * @return 关键字KEY对应的序列化对象
     * @see CacheManager#load(String)
     */
    @Deprecated
    public <V extends Serializable> V loadSerializable(String key, boolean isCacheMemory) {
        return load(key, isCacheMemory);
    }

    /**
     * 保存字符数组,使用默认存储配置 {@link CacheOption}
     *
     * @param key 缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     */
    public <V> void save(String key, V value) {
        if (!isInitialize()) {
            return;
        }
        CacheOption option = mCacheLoaderConfiguration.cacheOptionDefault;
        mCacheHelper.insert(key, value, option);
    }

    /**
     * 保存字符数组,使用默认存储配置 {@link CacheOption}
     *
     * @param key    缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param value  需要保存的字符数组
     * @param option 配置信息
     */
    public <V> void save(final String key, final V value, final CacheOption option) {
        if (!isInitialize()) {
            return;
        }
        if (option == null) {
            return;
        }
        if (option.isSyncRun()) {
            mCacheHelper.insert(key, value, option);
        } else {
            mCacheEngine.submit(new Runnable() {
                @Override
                public void run() {
                    mCacheHelper.insert(key, value, option);
                }
            });
        }
    }

    /**
     * 保存数据,使用默认存储配置 {@link CacheOption}
     *
     * @param key   缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param value 需要保存的字符数组
     */
    @Deprecated
    public void saveBytes(String key, byte[] value) {
        save(key, value);
    }

    /**
     * 保存字符数组,使用默认存储配置 {@link CacheOption}
     *
     * @param key    缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param value  需要保存的字符数组
     * @param option 配置信息
     */
    @Deprecated
    public void saveBytes(final String key, final byte[] value, final CacheOption option) {
        save(key, value, option);
    }


    /**
     * 保存字符串String到缓存
     *
     * @param key   缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param value 要缓存的值
     * @see
     */
    @Deprecated
    public void saveString(String key, String value) {
        save(key, value);
    }

    /**
     * 保存字符串String到缓存
     *
     * @param key    缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param value  要缓存的值
     * @param option 保存缓存的配置信息
     * @see
     */
    @Deprecated
    public void saveString(final String key, final String value, final CacheOption option) {
        save(key, value, option);
    }

    /**
     * 保存int数值到缓存
     *
     * @param key   缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param value 要缓存的值
     * @see
     */
    @Deprecated
    public void saveInt(String key, int value) {
        save(key, value);
    }

    /**
     * 保存int数值到缓存
     *
     * @param key    缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param value  要缓存的值
     * @param option 保存缓存的配置信息
     * @see
     */
    @Deprecated
    public void saveInt(final String key, final int value, final CacheOption option) {
        save(key, value, option);
    }

    /**
     * 保存float数值到缓存
     *
     * @param key    缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param value  要缓存的值
     * @param option 保存缓存的配置信息
     * @see
     */
    @Deprecated
    public void saveFloat(final String key, final float value, final CacheOption option) {
        save(key, value, option);
    }

    /**
     * 保存float数值到缓存
     *
     * @param key   缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param value 要缓存的值
     * @see
     */
    @Deprecated
    public void saveFloat(String key, float value) {
        save(key, value);
    }

    /**
     * 保存double数值到缓存
     *
     * @param key    缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param value  要缓存的值
     * @param option 保存缓存的配置信息
     * @see
     */
    @Deprecated
    public void saveDouble(final String key, final double value, final CacheOption option) {
        save(key, value, option);
    }

    /**
     * 保存double数值到缓存
     *
     * @param key   缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param value 要缓存的值
     * @see
     */
    @Deprecated
    public void saveDouble(String key, double value) {
        save(key, value);
    }

    /**
     * 保存boolean数值到缓存
     *
     * @param key    缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param value  要缓存的值
     * @param option 保存缓存的配置信息
     */
    @Deprecated
    public void saveBoolean(final String key, final boolean value, final CacheOption option) {
        save(key, value, option);
    }

    /**
     * 保存boolean数值到缓存
     *
     * @param key   缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param value 要缓存的值
     * @see
     */
    @Deprecated
    public void saveBoolean(String key, boolean value) {
        save(key, value);
    }

    /**
     * 保存Serializable对象到缓存
     *
     * @param key    缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param values 序列化对象，继承Serializable
     */
    @Deprecated
    public <V extends Serializable> void saveSerializable(String key, V values) {
        save(key, values);
    }

    /**
     * 保存Serializable对象到缓存
     *
     * @param key    缓存中对应的关键字，譬如可能是网络图片的地址，也可能是自命名的方式，要保证唯一。
     * @param values 序列化对象，继承Serializable
     * @param option 保存缓存的配置信息
     */
    @Deprecated
    public <V extends Serializable> void saveSerializable(final String key, final V values,
            final CacheOption option) {
        save(key, values, option);
    }

    /***
     * 删除一条缓存数据
     *
     * @param key 数据标识
     * @return 是否删除成功 boolean
     */
    public boolean delete(String key) {
        if (!isInitialize()) {
            return false;
        }
        return mCacheHelper.delete(key);
    }

    /**
     * 获取缓存大小
     *
     * @return long
     * @see [类、类#方法、类#成员]
     */
    public long size() {
        if (!isInitialize()) {
            return 0;
        }
        return mCacheHelper.size();
    }

    /**
     * 是否初始化
     *
     * @return boolean
     * @see [类、类#方法、类#成员]
     */
    private boolean isInitialize() {
        if (mCacheHelper == null) {
            CacheLog.e("The loadTask is not running");
            return false;
        }
        return true;
    }
    /**
     * Adds a custom serializer for a specific class
     * When used, must be called right after Paper.init()
     *
     * @param clazz      type of the custom serializer
     * @param serializer the serializer instance
     * @param <T>        type of the serializer
     */
    public  <T> void addSerializer(Class<T> clazz, Serializer<T> serializer) {
        if (mCacheHelper != null) {
            mCacheHelper.addSerializer(clazz,serializer);
        }

    }
    /***
     * 清理掉当前缓存数据,可以继续使用
     *
     * @see [类、类#方法、类#成员]
     */
    public void clear() {
        if (mCacheHelper != null) {
            mCacheHelper.clear();
        }
    }

    /**
     * 关闭缓存,关闭后将不能再使用缓存了，如果重新使用需要重新初始化
     */
    public void close() {
        if (mCacheHelper != null) {
            mCacheHelper.close();
            mCacheHelper = null;
        }
        if (sInstance != null) {
            sInstance = null;
        }
    }

}
