package com.quinnkudzma.platformer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.quinnkudzma.platformer.entities.Entity;

import java.util.ArrayList;


public class GameView extends SurfaceView {
    private static final int BG_COLOR = Color.rgb(135, 206, 235);
    private SurfaceHolder mHolder;
    public Paint mPaint = new Paint();
    public Canvas mCanvas = null;
    private Matrix mTransform = new Matrix();
    private Point mScreenCoord = new Point();

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mHolder = getHolder();
    }

    public void render(final ArrayList<Entity> visableEntities, final Viewport camera,
                       int collectibles, int playerHealth, int maxColl){
        if(!lockAndAquireCanvas()){
            return;
        }
        try {
            mCanvas.drawColor(BG_COLOR);
            for (final Entity e : visableEntities) {
                camera.worldToScreen(e, mScreenCoord);
                mTransform.reset();
                mTransform.postTranslate(mScreenCoord.x, mScreenCoord.y);
                e.render(mCanvas, mTransform, mPaint);
                HUDandUI.drawHUD(mCanvas, mPaint, collectibles, playerHealth , maxColl);
            }
            DebugTextRenderer.render(mCanvas, camera, mPaint);
        }finally {
            if(mCanvas != null){
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }

    }

    private boolean lockAndAquireCanvas(){
        if(!mHolder.getSurface().isValid()){
            return false;
        }
        mCanvas = mHolder.lockCanvas();
        return(mCanvas != null);
    }

}
