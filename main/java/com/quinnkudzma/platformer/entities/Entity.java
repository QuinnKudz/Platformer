package com.quinnkudzma.platformer.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

import com.quinnkudzma.platformer.Game;
import com.quinnkudzma.platformer.levels.LevelManager;
import com.quinnkudzma.platformer.utilities.BitmapPool;


public class Entity {
    public static Game mEngine; //Not owned by entity, must be nulled by game
    private static final String TAG = "Entity";
    public static final float DEFAULT_LOCATION = 0f; //meters
    public static final float DEFAULT_DIMENSION = 1f; //meters
    public float x = DEFAULT_LOCATION;
    public float y = DEFAULT_LOCATION;
    public float width = DEFAULT_DIMENSION;
    public float height = DEFAULT_DIMENSION;
    public Bitmap mBitmap = null; //TODO: move to bitmap pool
    private String mSprite;

    public Entity(final String spriteName){
        init(spriteName, DEFAULT_DIMENSION, DEFAULT_DIMENSION);
    }
    public Entity(final String spriteName, final float width, final float height){
        init(spriteName, width, height);
    }

    private void init(final String spriteName, float width, float height){
        mSprite = spriteName;
        this.width = width;
        this.height = height;
        if(mSprite.isEmpty()){
            return;
        }
        loadSprite();
    }

    public void loadSprite(){
        if(mBitmap != null){
            BitmapPool.remove(mBitmap);
        }
        mBitmap = BitmapPool.createBitmap(mEngine, mSprite, width, height);
        if(mBitmap == null){
            Log.e(TAG, "Failed to creat entitiy bitmap: " + mSprite);
        }
    }

    public void update(final float deltaTime){

    }

    public void render(final Canvas canvas, final Matrix transform,  final Paint paint){
        canvas.drawBitmap(mBitmap, transform, paint);
    }

    public boolean isColliding(Entity that){
        return Entity.intersectsAABB(this, that);
    }

    public void onCollision(Entity that){
        // no default implementation
    }

    public static boolean intersectsAABB(Entity a, Entity b) {
        return !(a.right() < b.left()
                || b.right() < a.left()
                || a.bottom() < b.top()
                || b.bottom() < a.top());
    }

    //https://pastebin.com/vDzzZVvD
    //SAT intersection test. http://www.metanetsoftware.com/technique/tutorialA.html
    //returns true on intersection, and sets the least intersecting axis in overlap
    static final PointF overlap = new PointF(0,0); //Q&D PointF pool for collision detection. Assumes single threading.
    @SuppressWarnings("UnusedReturnValue")
    static boolean getOverlap(final Entity a, final Entity b, final PointF overlap) {
        overlap.x = 0.0f;
        overlap.y = 0.0f;
        final float centerDeltaX = a.centerX() - b.centerX();
        final float halfWidths = (a.width() + b.width()) * 0.5f;
        float dx = Math.abs(centerDeltaX);//cache the abs, we need it twice

        if (dx > halfWidths) return false; //no overlap on x == no collision

        final float centerDeltaY = a.centerY() - b.centerY();
        final float halfHeights = (a.height() + b.height()) * 0.5f;
        float dy = Math.abs(centerDeltaY);

        if (dy > halfHeights) return false; //no overlap on y == no collision

        dx = halfWidths - dx; //overlap on x
        dy = halfHeights - dy; //overlap on y
        if (dy < dx) {
            overlap.y = (centerDeltaY < 0) ? -dy : dy;
        } else if (dy > dx) {
            overlap.x = (centerDeltaX < 0) ? -dx : dx;
        } else {
            overlap.x = (centerDeltaX < 0) ? -dx : dx;
            overlap.y = (centerDeltaY < 0) ? -dy : dy;
        }
        return true;
    }

    public float x(){return x;}
    public float y(){return y;}
    public float width(){return width;}
    public float height(){return height;}
    public float centerX(){return x+width*0.5f;}
    public float centerY(){return y+height*0.0f;}
    public float left(){return x;}
    public float right(){return x+width;}
    public float top(){return y;}
    public float bottom(){return y+height;}

    public void setPosition(final float x, final float y){
        this.x = x;
        this.y = y;
    }

    public void destory(){
        LevelManager.removeEntity(this);
    }
}
