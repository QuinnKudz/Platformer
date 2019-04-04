package com.quinnkudzma.platformer.utilities;

import android.graphics.Bitmap;

import com.quinnkudzma.platformer.Game;

import java.util.HashMap;
import android.graphics.Bitmap;
import java.util.HashMap;

/**
 * Created by Quinn on 3/13/2018.
 */

public class BitmapPool {

    //https://pastebin.com/KywhnEiA
        private static final String TAG = "BitmapPool";
        private static final HashMap<String, Bitmap> mBitmaps = new HashMap<>();
        private BitmapPool(){super();}

        public static Bitmap createBitmap(final Game engine, final String sprite, float widthMeters, float heightMeters){
            final String key = BitmapPool.makeKey(sprite, widthMeters, heightMeters);
            Bitmap bmp = BitmapPool.getBitmap(key);
            if(bmp != null){
                return bmp;
            }
            try {
                bmp = BitmapUtils.loadScaledBitmap(engine.getAppContext(), sprite, (int)engine.worldToScreenX(widthMeters), (int)engine.worldToScreenY(heightMeters));
                BitmapPool.put(key, bmp);
            }catch(final Exception e){
                throw new AssertionError(e.toString());
            }
            return bmp;
        }

        public static int size(){ return mBitmaps.size(); }
        public static String makeKey(final String name, final float width, final float height){
            return name+"_"+width+"_"+height;
        }
        public static void put(final String key, final Bitmap bmp){
            if(mBitmaps.containsKey(key)) {
                return;
            }
            mBitmaps.put(key, bmp);
        }
        public static boolean contains(final String key){
            return mBitmaps.containsKey(key);
        }
        public static boolean contains(final Bitmap bmp){ return mBitmaps.containsValue(bmp); }
        public static Bitmap getBitmap(final String key){
            return mBitmaps.get(key);
        }
        private static String getKey(final Bitmap bmp){
            if(bmp != null) {
                for (HashMap.Entry<String, Bitmap> entry : mBitmaps.entrySet()) {
                    if (bmp == entry.getValue()) {
                        return entry.getKey();
                    }
                }
            }
            return "";
        }
        private static void remove(final String key){
            Bitmap tmp = mBitmaps.get(key);
            if(tmp != null){
                mBitmaps.remove(key);
                tmp.recycle();
            }
        }
        public static void remove(Bitmap bmp){
            if(bmp == null){return;}
            remove(getKey(bmp));
        }
        public static void empty(){
            for (final HashMap.Entry<String, Bitmap> entry : mBitmaps.entrySet()) {
                entry.getValue().recycle();
            }
            mBitmaps.clear();
        }
}
