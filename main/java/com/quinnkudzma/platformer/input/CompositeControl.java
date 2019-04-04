package com.quinnkudzma.platformer.input;

import com.quinnkudzma.platformer.utilities.Utils;

import java.util.ArrayList;

/**
 * Created wth help from course page "SUPPORTING MULTIPLE CONTROLLERS"
 */

public class CompositeControl extends InputManager {
    private ArrayList<InputManager> mInputs = new ArrayList<>();
    private int mCount = 0;

    public CompositeControl(InputManager... inputs) {
        for(InputManager im : inputs){
            mInputs.add(im);
        }
        mCount = mInputs.size();
    }

    @Override
    public void update(float dt) {
        InputManager temp;
        mIsJumping = false;
        mHorizontalFactor = 0.0f;
        mVerticalFactor = 0.0f;
        for(int i = 0; i < mCount; i++){
            temp = mInputs.get(i);
            temp.update(dt);
            mIsJumping = mIsJumping || temp.mIsJumping;
            mHorizontalFactor += temp.mHorizontalFactor;
            mVerticalFactor += temp.mVerticalFactor;
        }
        mHorizontalFactor = Utils.clamp(mHorizontalFactor, -1f, 1f);
        mVerticalFactor = Utils.clamp(mVerticalFactor, -1f, 1f);
    }

    public void addInput(InputManager im){
        mInputs.add(im);
        mCount = mInputs.size();
    }

    @Override
    public void onStart() {
        for(InputManager im : mInputs){
            im.onStart();
        }
    }

    @Override
    public void onStop() {
        for(InputManager im : mInputs){
            im.onStop();
        }
    }

    @Override
    public void onPause() {
        for(InputManager im : mInputs){
            im.onPause();
        }
    }

    @Override
    public void onResume() {
        for(InputManager im : mInputs){
            im.onResume();
        }
    }
}
