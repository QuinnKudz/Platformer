package com.quinnkudzma.platformer;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.quinnkudzma.platformer.input.Accelerometer;
import com.quinnkudzma.platformer.input.CompositeControl;
import com.quinnkudzma.platformer.input.Gamepad;
import com.quinnkudzma.platformer.input.InputManager;
import com.quinnkudzma.platformer.input.TouchController;
import com.quinnkudzma.platformer.input.VirtualJoystick;

//Created by Quinn Kudzma Park

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Game mGame;
    private GamepadListener mGamepadListener = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GameView view = (GameView) findViewById(R.id.gameView);
        //InputManager input = new CompositeControl(new TouchController(touchControl)); //Uncomment for touch pad
        InputManager input = new CompositeControl(
                new VirtualJoystick(findViewById(R.id.virtual_joystick)),
                new Gamepad(this),
                new Accelerometer(this)
        );
        mGame = new Game(this, view, input);
    }


    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        if(isGameControllerConnected()){
            Toast.makeText(this, "Gamepad detected!", Toast.LENGTH_LONG).show();
        }
        super.onResume();
        mGame.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        mGame.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        mGame.onDestory();
        super.onDestroy();
    }

    public interface GamepadListener {
        boolean dispatchGenericMotionEvent(MotionEvent event);
        boolean dispatchKeyEvent(KeyEvent event);
    }

    public void setGamepadListener(GamepadListener listener) {
        mGamepadListener = listener;
    }

    public boolean isGameControllerConnected() {
        int[] deviceIds = InputDevice.getDeviceIds();
        for (int deviceId : deviceIds) {
            InputDevice dev = InputDevice.getDevice(deviceId);
            int sources = dev.getSources();
            if (((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) ||
                    ((sources & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean dispatchGenericMotionEvent(final MotionEvent ev) {
        if(mGamepadListener != null){
            if(mGamepadListener.dispatchGenericMotionEvent(ev)){
                return true;
            }
        }
        return super.dispatchGenericMotionEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(final KeyEvent ev) {
        if(mGamepadListener != null){
            if(mGamepadListener.dispatchKeyEvent(ev)){
                return true;
            }
        }
        return super.dispatchKeyEvent(ev);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!hasFocus) {
            return;
        }
        View decorView = getWindow().getDecorView();
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LOW_PROFILE
            );
        } else {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }

}
