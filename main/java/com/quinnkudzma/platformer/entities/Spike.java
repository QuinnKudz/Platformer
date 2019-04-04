package com.quinnkudzma.platformer.entities;

import com.quinnkudzma.platformer.Game;

public class Spike extends Entity {

    private static final float SPIKE_HEIGHT = 1.0f;
    private static final float SPIKE_WIDTH = 1.0f;

    public Spike(String spriteName) {
        super(spriteName, SPIKE_WIDTH, SPIKE_HEIGHT);
    }


}
