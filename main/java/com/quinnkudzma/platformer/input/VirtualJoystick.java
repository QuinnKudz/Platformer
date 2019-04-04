package com.quinnkudzma.platformer.input;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.quinnkudzma.platformer.R;
import com.quinnkudzma.platformer.utilities.Utils;

/**
 * Code source from course page "MANAGING USER INPUT"
 */

public class VirtualJoystick extends InputManager {
    public float mMaxDistance = 10f;
    public float mStartingPositionX = 0.0f;
    public float mStartingPositionY = 0.0f;
    public static final String TAG = "Joystick";

    public VirtualJoystick(View view) {
        view.findViewById(R.id.joystick_region)
                .setOnTouchListener(new JoystickTouchListener());
        view.findViewById(R.id.button_region)
                .setOnTouchListener(new ActionButtonTouchListener());
        mMaxDistance = Utils.dpToPx(48*2); //48dp = minimum hit target. maxDistance is in pixels.
        Log.d(TAG, "MaxDistance (pixels): " + mMaxDistance);
    }

    private class ActionButtonTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event){
            int action = event.getActionMasked();
            if(action == MotionEvent.ACTION_DOWN){
                mIsJumping = true;
            }else if(action == MotionEvent.ACTION_UP){
                mIsJumping = false;
            }
            return true;
        }
    }

    private class JoystickTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event){
            int action = event.getActionMasked();
            if(action == MotionEvent.ACTION_DOWN){
                mStartingPositionX = event.getX(0);
                mStartingPositionY = event.getY(0);
            }else if(action == MotionEvent.ACTION_UP){
                mHorizontalFactor = 0.0f;
                mVerticalFactor = 0.0f;
            }else if(action == MotionEvent.ACTION_MOVE){
                //get the proportion to the maxDistance
                mHorizontalFactor = (event.getX(0) - mStartingPositionX)/mMaxDistance;
                mHorizontalFactor = Utils.clamp(mHorizontalFactor, -1.0f, 1.0f);

                mVerticalFactor = (event.getY(0) - mStartingPositionY)/mMaxDistance;
                mVerticalFactor = Utils.clamp(mVerticalFactor, -1.0f, 1.0f);
            }
            return true;
        }
    }
}

