package com.quinnkudzma.platformer.input;

import android.view.MotionEvent;
import android.view.View;

import com.quinnkudzma.platformer.R;


/**
 * Code source from course page "CREATING A VIRTUAL JOYSTICK"
 */

public class TouchController extends InputManager
        implements View.OnTouchListener {

    public TouchController(View view){
        view.findViewById(R.id.keypad_up)
                .setOnTouchListener(this);
        view.findViewById(R.id.keypad_down)
                .setOnTouchListener(this);
        view.findViewById(R.id.keypad_left)
                .setOnTouchListener(this);
        view.findViewById(R.id.keypad_right)
                .setOnTouchListener(this);
        view.findViewById(R.id.keypad_jump)
                .setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        int action = event.getActionMasked();
        int id = v.getId();
        if(action == MotionEvent.ACTION_DOWN){
            // User started pressing a key
            if(id ==R.id.keypad_up){
                mVerticalFactor -= 1;
            }else if (id == R.id.keypad_down) {
                mVerticalFactor += 1;
            }
            if (id == R.id.keypad_left) {
                mHorizontalFactor -= 1;
            } else if(id == R.id.keypad_right) {
                mHorizontalFactor += 1;
            }
            if (id == R.id.keypad_jump) {
                mIsJumping = true;
            }
        } else if(action == MotionEvent.ACTION_UP) {
            // User released a key
            if (id == R.id.keypad_up) {
                mVerticalFactor += 1;
            } else if (id == R.id.keypad_down) {
                mVerticalFactor -= 1;
            }
            if (id == R.id.keypad_left) {
                mHorizontalFactor += 1;
            } else if (id == R.id.keypad_right) {
                mHorizontalFactor -= 1;
            }
            if (id == R.id.keypad_jump) {
                mIsJumping = false;
            }
        }
        return false;
    }
}
