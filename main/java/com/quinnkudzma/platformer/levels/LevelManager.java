package com.quinnkudzma.platformer.levels;

import android.util.Log;

import com.quinnkudzma.platformer.entities.Coin;
import com.quinnkudzma.platformer.entities.Entity;
import com.quinnkudzma.platformer.entities.EntityFactory;
import com.quinnkudzma.platformer.entities.Player;
import com.quinnkudzma.platformer.utilities.BitmapPool;

import java.util.ArrayList;

public class LevelManager {
    public final ArrayList <Entity> mEntities = new ArrayList<>();
    public static final ArrayList <Entity> mEntitiesToAdd = new ArrayList<>();
    public static final ArrayList <Entity> mEntitiesToDelete = new ArrayList<>();
    public Player mPlayer = null;
    public int mLevelWidth = 0;
    public int mLevelHeght = 0;
    private static final String TAG = "LevelManager";

    public LevelManager(){
        loadMapAssets(new TestLevel());
    }

    public void update(final float deltaTime){
        final int ic = mEntities.size();
        for(int i = 0; i < ic; i++){
            mEntities.get(i).update(deltaTime);
        }
        checkCollisions();
        addAndRemoveEntities();
    }

    private void checkCollisions(){
        final int count = mEntities.size();
        Entity a, b;
        for(int i = 0; i < count - 1; i++){
            a = mEntities.get(i);
            for(int j = i +1; j < count; j++){
                b = mEntities.get(j);
                if( a.isColliding(b)){
                    a.onCollision(b);
                    b.onCollision(a);
                }
            }
        }
    }

    private void addAndRemoveEntities(){
        Entity temp;
        for(int i = mEntitiesToDelete.size()-1; i >= 0; i--){
            temp = mEntitiesToDelete.remove(i);
            mEntities.remove(temp);
        }
        for(int i = mEntitiesToAdd.size()-1; i >= 0; i--){
            temp = mEntitiesToAdd.remove(i);
            mEntities.add(temp);
        }
    }

    public static void addEntity(final Entity e){
        if(e != null){mEntitiesToAdd.add(e);}
    }

    public static void removeEntity(final Entity e){
        if(e != null){mEntitiesToDelete.add(e);}
    }

    private void loadMapAssets(final LevelData data){
        cleanup();
        mLevelHeght = data.mHeight;
        mLevelWidth = data.mWidth;
        for(int y = 0; y < mLevelHeght; y++){
            final int[] row = data.getRow(y);
            for(int x = 0; x< row.length; x++){
                final int tileType = row[x];
                if(tileType == LevelData.NO_TILE){ continue; }
                final String spriteName = data.getSpriteName(tileType);
                mEntities.add(EntityFactory.makeEntity(spriteName, x, y));
            }
        }
        mPlayer = findPlayerInstance();
    }

    private Player findPlayerInstance(){
        for(final Entity e : mEntities){
            if(Player.class.isInstance(e)){
                return (Player) e;
            }
        }
        throw new AssertionError("Player not found in level!" );
    }

    public int findCOllectibles(){
        int Collectibles = 0;
        for(final Entity e : mEntities){
            if (Coin.class.isInstance(e)){
                Collectibles ++;
            }
        }
        return Collectibles;
    }

    private void cleanup(){
        for(final Entity e : mEntities){
            e.destory();
        }
        mEntitiesToAdd.clear();
        mEntitiesToDelete.clear();
        mEntities.clear();
        mPlayer = null;
    }

    public void destroy(){
        cleanup();
        BitmapPool.empty();
    }

}
