package com.quinnkudzma.platformer.levels;


public abstract class LevelData {
    public static final String NULLSPRITE = "null_sprite";
    public static final String PLAYER = "brown_left1";
    public static final String COIN = "coinyellow";
    public static final String SPIKE = "spearsup_brown";
    public static final int NO_TILE = 0;
    int [][] mTiles;
    int mHeight;
    int mWidth;

    public int getTile(final int x, final int y){
        return mTiles[y][x];
    }

    public int[] getRow(final int y){
        return mTiles[y];
    }

    protected void updateLevelDimensions(){
        mHeight = mTiles.length;
        mWidth = mTiles[0].length;
    }

    abstract public String getSpriteName(final int tileType);

}
