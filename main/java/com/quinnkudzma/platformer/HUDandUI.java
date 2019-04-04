package com.quinnkudzma.platformer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.quinnkudzma.platformer.entities.Player;


public class HUDandUI{

    public static final int HUD_SIZE = 10;
    public static final int SCALE_FACTOR_DIVISON = 200;
    public static final int HALF = 2;
    public static final int HEALTH_X = 10;
    public static final String HEALTH = "Health: ";
    public static final String COLLECTIBLE = "Collectibles: ";
    public static final String WIN_MESSAGE = "You Win!";


    public static void drawHUD(Canvas mCanvas, Paint mPaint, int collectibles , int health, int maxColl){
        float scaleFactor = (float) Math.sqrt(mCanvas.getWidth() * mCanvas.getHeight()) / SCALE_FACTOR_DIVISON;
        float scaledSize = HUD_SIZE * scaleFactor;
        mPaint.setColor(Color.BLUE);
        mPaint.setTextSize(scaledSize);
        mCanvas.drawText(HEALTH + health, HEALTH_X, scaledSize, mPaint);
        mCanvas.drawText(COLLECTIBLE + collectibles + "/" + maxColl, HEALTH_X * (scaledSize/HALF), scaledSize, mPaint);
        if(collectibles == maxColl){
            mCanvas.drawText(WIN_MESSAGE, mCanvas.getWidth()/HALF, mCanvas.getHeight()/HALF, mPaint);
        }
    }
}
