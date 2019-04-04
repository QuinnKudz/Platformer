package com.quinnkudzma.platformer.entities;

import com.quinnkudzma.platformer.Game;

public class Coin extends Entity {
    private static final float COIN_HEIGHT = 0.5f;
    private static final float COIN_WIDTH = 0.5f;

    public Coin(String spriteName) {
        super(spriteName, COIN_HEIGHT, COIN_WIDTH);
    }

    @Override
    public void onCollision(Entity that){
        super.onCollision(that);
        if(that instanceof Player) {
            mEngine.onGameEvent(Game.GameEvent.CoinPickup, this);
            destory();
        }
    }


}
