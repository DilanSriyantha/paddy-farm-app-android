package com.dtechsolutions.paddyfarm.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.dtechsolutions.paddyfarm.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {
    private static final String TAG = "ImageLoader";
    private static ImageLoader mInstance;
    private static final Map<String, Bitmap> mInMemCache = new HashMap<>();

    public ImageLoader() {}

    public static ImageLoader getInstance() {
        if(mInstance == null)
            mInstance = new ImageLoader();

        return mInstance;
    }

    public void loadImage(Context ctx, ImageView imageView, String url){
        // declaring executor to parse the url
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // once the executor parses the url
        // and receives the image, handler will load it
        // in the image view
        Handler handler = new Handler(Looper.getMainLooper());

        // initializing the image
        Bitmap image = null;

        // set image if in memory cache available
        if(isStored(ctx, url)){
            imageView.setImageBitmap(mInMemCache.get(url));
            return;
        }

        // set image placeholder temporarily
        imageView.setImageResource(R.drawable.image_placeholder);

        // only for background process (can take time depending on the internet efficiency
        executorService.execute(new Runnable() {
            final String _url = url;
            Bitmap _image = image;
            @Override
            public void run() {
                try{
                    InputStream is = new URL(_url).openStream();
                    _image = BitmapFactory.decodeStream(is);

                    if(!isStored(ctx, _url)) {
                        saveToImMemoryCache(_url, _image);
                        saveToLocalCache(ctx, _url, _image);
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(_image);
                        }
                    });
                } catch (Exception e) {
                    Log.d(TAG, Objects.requireNonNull(e.getLocalizedMessage()));
                }
            }
        });
    }

    private boolean isStored(Context ctx, String url) {
        return mInMemCache.containsKey(url) || isStoredLocally(ctx, url);
    }

    private boolean isStoredLocally(Context ctx, String key){
        if(key == null) return false;

        File cacheDir = ctx.getCacheDir();

        File cacheFile = new File(cacheDir, key.replace('/',  '_') + ".png");
        if(cacheFile.exists()) {
            mInMemCache.put(key, BitmapFactory.decodeFile(cacheFile.getAbsolutePath()));
            return true;
        }else{
            return false;
        }
    }

    private void saveToImMemoryCache(String key, Bitmap bmp){
        mInMemCache.put(key, bmp);
    }

    private void saveToLocalCache(Context ctx, String key, Bitmap bmp){
        File cacheDir = ctx.getCacheDir();

        File file = new File(cacheDir, key.replace('/', '_') + ".png");

        try(FileOutputStream fos = new FileOutputStream(file)){
            bmp.compress(Bitmap.CompressFormat.PNG, 60, fos);
            fos.flush();
            Log.d(TAG, key + " saved to localCacheDir");
        }catch (Exception ex){
            Log.e(TAG, Objects.requireNonNull(ex.getLocalizedMessage()));
        }
    }

    public void loadImage(Context ctx, ImageView imageView, Uri uri){
        try{
            URL url = new URL(uri.toString());
            loadImage(ctx, imageView, url.toString());
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}