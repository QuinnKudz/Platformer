package com.quinnkudzma.platformer.entities;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.quinnkudzma.platformer.Game;
import com.quinnkudzma.platformer.input.InputManager;
import com.quinnkudzma.platformer.levels.LevelManager;
import com.quinnkudzma.platformer.utilities.BitmapPool;

public class Player extends DynamicEntity {
    private static final String TAG = "Player";
    private static final float PLAYER_WIDTH = 0.9f; //meters
    private static final float PLAYER_HEIGHT = 1f; //meters
    private static final float PLAYER_RUN_SPEED = 6f;    //meters per sec
    private static final float PLAYER_JUMP_FORCE = -(GRAVITATIONAL_ACCELERATION/3f + 4f) ;
    private static final float MIN_INPUT_TO_TURN = 0.0f; //percent of joystick needed to turn player
    private final int LEFT = 1;
    private final int RIGHT = -1;
    private int mFacing = LEFT;
    public int PLAYER_HEALTH = 3;
    public boolean IS_DEAD = false;
    public int PLAYERS_COLL = 0;
    private float INVUL_TIMER = 0;


    public Player(String spriteName) {
        super(spriteName, PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    @Override
    public void update(float deltaTime) {
        final InputManager controls = mEngine.getControls();
        float tempX = mTargetSpeed.x;
        final float direction = controls.mHorizontalFactor;
        mTargetSpeed.x = direction * PLAYER_RUN_SPEED;
        updateFacingDirection(direction);
        if(controls.mIsJumping && mIsOnGround){
            mVelocity.y = PLAYER_JUMP_FORCE;
            mIsOnGround = false;
            mEngine.onGameEvent(Game.GameEvent.Jump, this); //TODO: implement this!
        }
        if(PLAYER_HEALTH < 0){
            IS_DEAD = true;
        }
        if(INVUL_TIMER > 0){
            INVUL_TIMER --;
        }
        if(INVUL_TIMER == 0){
            mBitmap = BitmapPool.createBitmap(mEngine, "brown_left1", width, height);
        }
        if(tempX != mTargetSpeed.x && tempX == 0){
            mEngine.onGameEvent(Game.GameEvent.Movement, this);
        }
        super.update(deltaTime);
    }

    public void respawn(){
        LevelManager.removeEntity(this);
        LevelManager.addEntity(this);
    }

    private void updateFacingDirection(final float controlDir){
        if(Math.abs(controlDir) < MIN_INPUT_TO_TURN){
            return;
        }
        if(controlDir < 0){
            mFacing = LEFT;
        }else if (controlDir >0){
            mFacing = RIGHT;
        }
    }

    public void setPlayerCoin(int num){
        PLAYERS_COLL += num;
    }

    @Override
    public void onCollision(Entity that){
        super.onCollision(that);
        if((that instanceof Coin)) {
            PLAYERS_COLL++;
        }else if((that instanceof Spike && INVUL_TIMER ==0) ){
            PLAYER_HEALTH --;
            if(PLAYER_HEALTH < 0){
                return;
            }
            mEngine.onGameEvent(Game.GameEvent.Damage, this);
            INVUL_TIMER = 60;
            mBitmap = BitmapPool.createBitmap(mEngine, "brown_dam", width, height);
            Log.d(TAG, "Player Health: " + PLAYER_HEALTH);
        }


    }

    @Override
    public void render(Canvas canvas, Matrix transform, Paint paint) {
        transform.preScale(mFacing, 1.0f);
        if(mFacing == RIGHT){
            final float offset = mEngine.worldToScreenX(width);
            transform.postTranslate(offset, 0.0f);
        }
        super.render(canvas, transform, paint);
    }
}
