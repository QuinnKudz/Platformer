package com.quinnkudzma.platformer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PointF;
import android.util.Log;
import android.view.SurfaceHolder;

import com.quinnkudzma.platformer.entities.Entity;
import com.quinnkudzma.platformer.input.InputManager;
import com.quinnkudzma.platformer.levels.LevelManager;

import java.util.ArrayList;

public class Game implements Runnable, SurfaceHolder.Callback {

    private static final String TAG = "Game";
    private GameView mView;
    private MainActivity mActivity;
    private Thread mGameThread;
    private volatile boolean mIsRunning = false;
    private LevelManager mLevel = null;
    Viewport mCamera = null;
    private ArrayList<Entity> mVisibleEntities = new ArrayList<>();
    private InputManager mControls = null;
    private JukeBox mJukeBox = null;
    PointF mCameraPos = new PointF(0f, 0f);
    float mScrollSpeed = 2.0f; //meters per second

    private boolean WIN = false;
    private int COLLECTIBLES = 0;
    private static final float METERS_TO_SHOW_X = 20f; //set the value you want fixed
    private static final float METERS_TO_SHOW_Y = 0f;
    private int FRAMEBUFFER_WIDTH = 0;
    private int FRAMEBUFFER_HEIGHT = 0;
    private static final float NANOS_TO_SECONDS = 1.0f/1000000000;

    public Game(Context context, final GameView view, final InputManager input) {
        mView = view; //, final int width, final int height
        mActivity = (MainActivity) context;
        mControls = input;
        mJukeBox = new JukeBox(context);
        Entity.mEngine = this;
        SurfaceHolder holder = mView.getHolder();
        holder.addCallback(this);
        getScreenDim();
        holder.setFixedSize( FRAMEBUFFER_WIDTH, FRAMEBUFFER_HEIGHT);
        mCamera = new Viewport( (int) (FRAMEBUFFER_WIDTH ), (int) (FRAMEBUFFER_HEIGHT ), METERS_TO_SHOW_X, METERS_TO_SHOW_Y);
        mLevel = new LevelManager();
        COLLECTIBLES = mLevel.findCOllectibles();
        Log.d(TAG, "Game Created");
    }

    public enum GameEvent {
        LevelStart,
        Jump,
        Damage,
        CoinPickup,
        Movement,
        Death,
        LevelGoal
    }

    public void onGameEvent(GameEvent gameEvent, Entity e /*can be null!*/) {
        mJukeBox.playSoundForGameEvent(gameEvent);
    }

    public void getScreenDim(){
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        FRAMEBUFFER_WIDTH = (int) (screenWidth );
        FRAMEBUFFER_HEIGHT = (int) (screenHeight );
        Log.d(TAG, "Width: " + screenWidth + " Height: " + screenHeight);
    }

    public InputManager getControls(){return mControls;}

    public Context getAppContext(){
        return mActivity.getApplicationContext();
    }

    public float getWorldHeight(){
        return mLevel.mLevelHeght;
    }

    public float getWorldWidth(){
        return mLevel.mLevelWidth;
    }

    public float screenToWorldX(final float pixelDistance){
        return (float) pixelDistance / mCamera.getPixelsPerMeterX();
    }
    public float screenToWorldY(final float pixelDistance){
        return (float) pixelDistance / mCamera.getPixelsPerMeterY();
    }
    public int worldToScreenX(final float worldDistance){
        return (int) (worldDistance * mCamera.getPixelsPerMeterX());
    }
    public int worldToScreenY(final float worldDistance){
        return (int) (worldDistance * mCamera.getPixelsPerMeterY());
    }

    @Override
    public void run() {
        long lastFrame = System.nanoTime();
        while (mIsRunning){
            final float delataTime = (System.nanoTime()-lastFrame)*NANOS_TO_SECONDS;
            lastFrame = System.nanoTime();
            update(delataTime);
            render(mVisibleEntities, mCamera);
        }
    }

    private void update(final float deltaTime){
        mControls.update(deltaTime);
        mCamera.lookAt(mLevel.mPlayer);
        mLevel.update(deltaTime);
        buildVisibleSet();

        DebugTextRenderer.CAMERA_INFO = mCamera.toString();
        DebugTextRenderer.PLAYER_POSITION.x = mLevel.mPlayer.x;
        DebugTextRenderer.PLAYER_POSITION.y = mLevel.mPlayer.y;
        DebugTextRenderer.PLAYER_VEL.x = mLevel.mPlayer.mVelocity.x;
        DebugTextRenderer.PLAYER_VEL.y = mLevel.mPlayer.mVelocity.y;
        DebugTextRenderer.TOTAL_OBJECT_COUNT = mLevel.mEntities.size();
        DebugTextRenderer.VISIBLE_OBJECTS = mVisibleEntities.size();
        DebugTextRenderer.FRAMERATE = 0;
        if(mLevel.mPlayer.IS_DEAD == true){
            onGameEvent(GameEvent.Death, null);
            WIN = false;
            mLevel = new LevelManager();
        }
        if(mLevel.mPlayer.PLAYERS_COLL == COLLECTIBLES && !WIN){
            onGameEvent(GameEvent.LevelGoal, null);
            WIN = true;
        }
    }

    private void render(final ArrayList<Entity> visibleSet, final Viewport camera){
        mView.render(visibleSet, camera, mLevel.mPlayer.PLAYERS_COLL, mLevel.mPlayer.PLAYER_HEALTH, COLLECTIBLES);
    }

    private void buildVisibleSet(){
        mVisibleEntities.clear();
        for(final Entity e : mLevel.mEntities){
            if(mCamera.inView(e)){
                mVisibleEntities.add(e);
            }
        }
    }

    //Below here executes on UI thread
    public void onResume(){
        mControls.onResume();
        mJukeBox.resumeBgMusic();
        mIsRunning = true;
        mGameThread = new Thread(this);
    }

    public void onPause(){
        mControls.onPause();
        mJukeBox.pauseBgMusic();
        mIsRunning = false;
        while(mGameThread.getState() != Thread.State.TERMINATED) {
            try {
                mGameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "GameThread is Terminated");
    }

    public void onDestory(){
        if(mLevel != null){
            mLevel.destroy();
        }
        if(mView != null){
            mView.getHolder().removeCallback(this);
        }
        mControls.onDestroy();
        Entity.mEngine = null;
    }

    @Override
    public void surfaceCreated(final SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");
    }

    @Override
    public void surfaceChanged(final SurfaceHolder holder, final int format, final int width, final int height) {
        Log.d(TAG, "surfaceChanged");
        Log.d(TAG, "\t Dimensions: " + width + " : " + height );
        if(mGameThread != null && mIsRunning){
            Log.d(TAG, "GameThread is started");
            mGameThread.start();
        }
    }

    @Override
    public void surfaceDestroyed(final SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
    }

}
