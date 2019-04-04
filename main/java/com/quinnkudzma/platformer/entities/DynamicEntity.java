package com.quinnkudzma.platformer.entities;

import android.graphics.PointF;

import com.quinnkudzma.platformer.utilities.Random;
import com.quinnkudzma.platformer.utilities.Utils;

public class DynamicEntity extends Entity {
    private static final String TAG = "DynamicEntity";
    private static final float MAX_DELTA = 0.48f; //Max change in pos over a frame
    static final float GRAVITATIONAL_ACCELERATION = 40f;
    public PointF mVelocity = new PointF(0f, 0f);
    PointF mAccel = new PointF(1f, 1f);
    PointF mTargetSpeed = new PointF(0f, 0f);

    float mGravity = GRAVITATIONAL_ACCELERATION;
    boolean mIsOnGround = false;

    public DynamicEntity(String spriteName) {
        super(spriteName);
    }

    public DynamicEntity(final String spriteName, final float width, final float height){
        super(spriteName, width, height);
    }

    @Override
    public void update(float deltaTime) {
        mVelocity.x += (mAccel.x) * (mTargetSpeed.x);
        if(Math.abs(mVelocity.x) > Math.abs(mTargetSpeed.x)){
            mVelocity.x = mTargetSpeed.x;
        }
        x+= Utils.clamp(mVelocity.x*deltaTime, -MAX_DELTA, MAX_DELTA);
        if(!mIsOnGround) {
            final float gravityThisTick = mGravity * deltaTime;
            mVelocity.y += gravityThisTick;
            y += Utils.clamp(mVelocity.y*deltaTime, -MAX_DELTA, MAX_DELTA);
        }

        if(y > mEngine.getWorldHeight()){
            y = Random.between(-5,-2);
        }
        mIsOnGround = false;
    }

    @Override
    public void onCollision(Entity that) {
        Entity.getOverlap(this, that, Entity.overlap);
        x += Entity.overlap.x;
        y += Entity.overlap.y;
        if(Entity.overlap.y != 0f){ //feat
            mVelocity.y =0f;
            mTargetSpeed.y = 0f;
            if(Entity.overlap.y < 0f){
                mIsOnGround = true;
            }
        }
        super.onCollision(that);
    }

}
