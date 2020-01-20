
package com.sum.cache;

import java.io.Serializable;

/**
 * 数据有效时间处理类
 */
public class LimitedAge implements Serializable {
    private static final long serialVersionUID = -2606340992667456390L;
    /**
     * 保存时候的时间(单位毫秒)
     */
    private long saveTime;
    /**
     * 磁盘缓存文件的最大有效时间(单位秒)
     */
    private long maxLimitTime;

    public LimitedAge(long saveTime, long maxLimitTime) {
        this.saveTime = saveTime;
        this.maxLimitTime = maxLimitTime;
        if (this.maxLimitTime <= 0) {
            this.maxLimitTime = Long.MAX_VALUE;
        }
    }

    /***
     * 检测是否过期
     *
     * @return boolean 返回是否过期(为true则过期)
     * @throws
     * @see [类、类#方法、类#成员]
     */
    public boolean checkExpire() {
        if (System.currentTimeMillis() - saveTime > (maxLimitTime)) {
            return true;
        }
        return false;
    }

    /**
     * 计算数据剩余有效时间(单位秒)
     *
     * @return long
     * @throws
     * @see [类、类#方法、类#成员]
     */
    public long limitedTime() {
        return (maxLimitTime - (System.currentTimeMillis() - saveTime));
    }

    /***
     * 获取数据保存时的时间(单位毫秒)
     *
     * @return long
     * @throws
     * @see [类、类#方法、类#成员]
     */
    public long getSaveTime() {
        return saveTime;
    }

    /***
     * 设置缓数据存时的时间(单位毫秒)
     *
     * @param saveTime
     *            void
     * @throws
     * @see [类、类#方法、类#成员]
     */
    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    /***
     * 获取缓存数据的有效时间(单位秒)
     *
     * @return long
     * @throws
     * @see [类、类#方法、类#成员]
     */
    public long getMaxLimitTime() {
        return maxLimitTime;
    }

    /**
     * 设置缓存的有效时间(单位秒)
     *
     * @param maxLimitTime void
     * @throws
     * @see [类、类#方法、类#成员]
     */
    public void setMaxLimitTime(long maxLimitTime) {
        this.maxLimitTime = maxLimitTime;
    }

}
