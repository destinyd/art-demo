package com.mindpin.art_demo.samples;

import android.app.Application;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by DD on 15/9/22.
 */
public class ArtDemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        init_image_config();
    }


    public void init_image_config() {
        DisplayImageOptions options;
        ImageLoaderConfiguration config;

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        config = new ImageLoaderConfiguration.Builder(getApplicationContext())

                // 设置缓存图片的宽度跟高度
                .memoryCacheExtraOptions(100, 100)
                .diskCacheExtraOptions(100, 100, null)

                        // 通过 LruMemoryCache 实现缓存机制
//                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
//                .memoryCacheSize(2 * 1024 * 1024)

                        // 限制缓存文件数量百分比
                .memoryCacheSizePercentage(13)

                .diskCacheSize(50 * 1024 * 1024)

                        // 硬盘缓存文件数量
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .defaultDisplayImageOptions(options)


                .build();

        ImageLoader.getInstance().init(config);
    }

}
