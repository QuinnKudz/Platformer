package com.quinnkudzma.platformer.utilities;

/**
 * Created by Quinn on 3/13/2018.
 */

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;

public class Utils {
    public static float Wrap(float val, final float min, final float max) {
        if (val < min) {
            val = max;
        } else if (val > max) {
            val = min;
        }
        return val;
    }

    public static void clamp(final PointF val, final PointF min, final PointF max) {
        if (val.x < min.x) {
            val.x = min.x;
        } else if (val.x > max.x) {
            val.x = max.x;
        }
        if (val.y < min.y) {
            val.y = min.y;
        } else if (val.y > max.y) {
            val.y = max.y;
        }
    }

    public static float clamp(float val, final float min, final float max) {
        if (val < min) {
            val = min;
        } else if (val > max) {
            val = max;
        }
        return val;
    }

    public static int clamp(int val, final int min, final int max) {
        if (val < min) {
            val = min;
        } else if (val > max) {
            val = max;
        }
        return val;
    }

    public static Bitmap flipBitmap(Bitmap source, boolean horizonally){
        Matrix matrix = new Matrix();
        int cX = source.getWidth()/2;
        int cY = source.getHeight()/2;
        if(horizonally){
            matrix.postScale(1, -1, cX, cY);
        }else{
            matrix.postScale(-1, 1, cX, cY);
        }
        return Bitmap.createBitmap(source, 0, 0,
                source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap ScaleToTargetHeight(Bitmap source, int height){
        float ratio = height / (float) source.getHeight();
        int newHeight = (int) (source.getHeight() * ratio);
        int newWidth =  (int) (source.getWidth() * ratio);
        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }


    //From course page for joystick
    public static int pxToDp(final int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(final int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
