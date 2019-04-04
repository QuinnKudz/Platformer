package com.quinnkudzma.platformer.utilities;

/**
 * Created by Quinn on 3/10/2018.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.Log;

public class BitmapUtils {
    private static final String TAG = "BitmapUtils";
    private static final boolean FILTER = false;
    private static final BitmapFactory.Options mOptions = new BitmapFactory.Options(); //Q&D pool
    private static final Point mDimensions = new Point(0,0); //Q&D  pool

    private BitmapUtils(){super();}

    public static Bitmap scaleBitmap(final Bitmap bmp, final int targetWidth, final int targetHeight){
        if(targetWidth == bmp.getWidth() && targetHeight == bmp.getHeight()){
            return bmp;
        }
        calculateScaling(mDimensions, targetWidth, targetHeight, bmp.getWidth(), bmp.getHeight());
        return Bitmap.createScaledBitmap(bmp, mDimensions.x, mDimensions.y, FILTER);
    }

    public static Bitmap loadScaledBitmap(final Context context, final String bitmapName,
                                          final int targetWidth, final int targetHeight) throws Exception {
        final Resources res = context.getResources();
        final int resID = res.getIdentifier(bitmapName, "drawable", context.getPackageName());
        return loadScaledBitmap(res, resID, targetWidth, targetHeight);
    }

    //Set either of the dimensions for aspect-correct scaling, or both to force the aspect.
    private static Bitmap loadScaledBitmap(final Resources res, final int resID, final int targetWidth, final int targetHeight) throws Exception{
        getSrcInfo(res, resID, mOptions); //parse the raw file info
        calculateScaling(mDimensions, targetWidth, targetHeight, mOptions.outWidth, mOptions.outHeight);
        try {
            Bitmap bitmap = decodeSampledBitmapFromResource(res, resID, targetWidth, targetHeight, mOptions); //loads file at closest POT dimensions
            if(bitmap != null){
                if(bitmap.getHeight() != mDimensions.y || bitmap.getWidth() != mDimensions.x) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, mDimensions.x, mDimensions.y, FILTER);  //scale to pixel-perfect dimensions
                }
                return bitmap;
            }
        } catch (final OutOfMemoryError oom) {
            Log.d(TAG, "prepareBitmap(): Out of Memory.");
        }
        throw new Exception("Failed to load bitmap: " + resID);
    }

    private static Bitmap decodeSampledBitmapFromResource(final Resources res, final int resId, final int reqWidth, final int reqHeight, final BitmapFactory.Options opts) {
        opts.inSampleSize = calculateInSampleSize(opts, reqWidth, reqHeight);
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, opts);
    }

    private static Bitmap decodeSampledBitmapFromResource(final Resources res, final int resId, final int reqWidth, final int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private static int calculateInSampleSize(final BitmapFactory.Options options, final int reqWidth, final int reqHeight) {
        final int height = options.outHeight;// Raw height and width of image
        final int width = options.outWidth;
        int inSampleSize = 1;
        if(height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private static void getSrcInfo(final Resources res, final int resID, final BitmapFactory.Options opts){
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resID, opts);
        opts.inJustDecodeBounds = false;
    }

    //provide both or either of targetWidth / targetHeight. If one is left at 0, the other is calculated
    //based on source-dimensions. Ergo; should scale and keep aspect ratio.
    private static void calculateScaling(final Point out, float targetWidth, float targetHeight, final float srcWidth, final float srcHeight){
        if (targetWidth <= 0 && targetHeight <= 0){
            targetWidth = srcWidth;
            targetHeight = srcHeight;
        }
        //formula: new height = (original height / original width) x new width
        out.x = (int) targetWidth;
        out.y = (int) targetHeight;
        if(targetWidth == 0 || targetHeight == 0){
            if(targetHeight > 0) { //if Y is configured, calculate X
                out.x = (int) ((srcWidth / srcHeight) * targetHeight);
            }else { //calculate Y
                out.y = (int) ((srcHeight / srcWidth) * targetWidth);
            }
        }
    }

    public static Bitmap rotateBitmap(final Bitmap source, final float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap flipBitmap(final Bitmap source, final boolean horizontally) {
        Matrix matrix = new Matrix();
        int cx = source.getWidth()/2;
        int cy = source.getHeight()/2;
        if(horizontally){
            matrix.postScale(1, -1, cx, cy);
        }else{
            matrix.postScale(-1, 1, cx, cy);
        }
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap scaleToTargetHeight(final Bitmap source, final int height){
        float ratio = height / (float) source.getHeight();
        int newHeight = (int) (source.getHeight() * ratio);
        int newWidth = (int) (source.getWidth() * ratio);
        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }
}