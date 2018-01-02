package com.sum.library.net;

import android.content.Context;
import android.graphics.Bitmap;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.disk.NoOpDiskTrimmableRegistry;
import com.facebook.common.internal.Supplier;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.common.util.ByteConstants;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.sum.library.AppFileConfig;

/**
 * Created by Administrator on 2017/5/31.
 */

public class ImagePipelineConfigUtils {

    //分配的可用内存
    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();
    //使用的缓存数量
    private static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4;
    //默认图极低磁盘空间缓存的最大值
    private static final int MAX_DISK_CACHE_VERYLOW_SIZE = 20 * ByteConstants.MB;
    //默认图低磁盘空间缓存的最大值
    private static final int MAX_DISK_CACHE_LOW_SIZE = 60 * ByteConstants.MB;
    //默认图磁盘缓存的最大值
    private static final int MAX_DISK_CACHE_SIZE = 100 * ByteConstants.MB;

    public static ImagePipelineConfig getDefaultImagePipelineConfig(Context context) {
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                MAX_MEMORY_CACHE_SIZE,
                Integer.MAX_VALUE,
                MAX_MEMORY_CACHE_SIZE,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE);
        Supplier<MemoryCacheParams> mSupplierMemoryCacheParams = () -> bitmapCacheParams;
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)
                // 缓存文件目录
                .setBaseDirectoryPath(AppFileConfig.getCacheDirectoryFile())
                // 缓存文件夹名
                .setBaseDirectoryName("fresco_cache")
                .setMaxCacheSize(MAX_DISK_CACHE_SIZE)
                .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_CACHE_LOW_SIZE)
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_DISK_CACHE_VERYLOW_SIZE)
                .setDiskTrimmableRegistry(NoOpDiskTrimmableRegistry.getInstance())
                .build();
        ImagePipelineConfig.Builder configBuilder = ImagePipelineConfig.newBuilder(context)
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                .setMainDiskCacheConfig(diskCacheConfig)
                .setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .setMemoryTrimmableRegistry(NoOpMemoryTrimmableRegistry.getInstance())
                .setResizeAndRotateEnabledForNetwork(true)
                .setDownsampleEnabled(true);
        NoOpMemoryTrimmableRegistry.getInstance().registerMemoryTrimmable(memoryTrimType -> {
            final double suggestedTrimRatio = memoryTrimType.getSuggestedTrimRatio();
            if (MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio() == suggestedTrimRatio
                    || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio() == suggestedTrimRatio
                    || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio() == suggestedTrimRatio
                    ) {
                ImagePipelineFactory.getInstance().getImagePipeline().clearMemoryCaches();
            }
        });
        return configBuilder.build();
    }
}
