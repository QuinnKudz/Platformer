package com.quinnkudzma.platformer.entities;

import com.quinnkudzma.platformer.levels.LevelData;

public class EntityFactory {
    public static final String TAG = "EntitiyFaactory";

    public static Entity makeEntity(final String sprite, final float x, final float y){
        Entity e = null;
        if (sprite.equalsIgnoreCase(LevelData.PLAYER)) {
            e = new Player(sprite);
        }else if( sprite.equalsIgnoreCase(LevelData.COIN)){
            e = new Coin(sprite);
        }else if( sprite.equalsIgnoreCase(LevelData.SPIKE)){
            e = new Spike(sprite);
        }else{
            e = new Entity(sprite);
        }
        e.setPosition(x,y);

        return e;
    }
}
