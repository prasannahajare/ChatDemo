package com.sample.android.chat;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.sample.android.chat.database.ChatHelper;

/**
 * Created by sa on 6/30/16.
 */
public class ChatApplication extends Application {

    private static ChatHelper chatHelper;
    public static SQLiteDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();

        chatHelper = new ChatHelper(this);
        database = chatHelper.getWritableDatabase();
        initImageLoader(this);
    }


    public static SQLiteDatabase getDatabseInstance(Context context){
        if(database == null){
            chatHelper = new ChatHelper(context);
            database = chatHelper.getWritableDatabase();
        }
        return database;
    }

    public static void initImageLoader(Context context) {
        // Initialize Universal Image Loader
        // Create global configuration and initialize ImageLoader with this
        // configuration
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                 .showImageOnLoading(R.mipmap.ic_launcher) // resource or  // drawable
                 .showImageForEmptyUri(R.mipmap.ic_launcher) // resource or drawable
                 .showImageOnFail(R.mipmap.ic_launcher) // resource or drawable
                 .cacheInMemory(true) // default
                 .cacheOnDisk(true) // default
                 .imageScaleType(ImageScaleType.IN_SAMPLE_INT/* EXACTLY_STRETCHED */)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSizePercentage(13) // default
                .defaultDisplayImageOptions(options) // default
                .build();
        ImageLoader.getInstance().init(config);
    }
}
