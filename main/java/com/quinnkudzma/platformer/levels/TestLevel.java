package com.quinnkudzma.platformer.levels;

import android.util.SparseArray;

public class TestLevel extends LevelData {

    private final SparseArray<String> mTileIdToSpriteName = new SparseArray<>();
    public static final String PLAYER = "brown_left1";

    public TestLevel(){
       mTileIdToSpriteName.put(0, "backround");
       mTileIdToSpriteName.put(1, PLAYER);
       mTileIdToSpriteName.put(2, "waveforest_square");
       mTileIdToSpriteName.put(3, "waveforest_2roundleft");
       mTileIdToSpriteName.put(4, "waveforest_2roundright");
       mTileIdToSpriteName.put(5, "coinyellow");
       mTileIdToSpriteName.put(6, "spearsup_brown");
       mTileIdToSpriteName.put(7, "dirt");
       mTileIdToSpriteName.put(8, "dirt_left");
       mTileIdToSpriteName.put(9, "dirt_right");
       mTileIdToSpriteName.put(10, "top_round_right");
       mTileIdToSpriteName.put(11, "top_round_left");

       mTiles = new int[][]{
               {0,1,0,0,0,0,0,0,0,0,0,0,0,0,5,0,0,0,0,0,0,5},
               {0,0,0,0,5,0,0,0,0,0,0,0,0,3,2,2,2,10,0,0,11,4},
               {0,0,0,3,2,2,4,0,0,0,0,0,0,0,0,0,0,7,0,0,7,0},
               {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,7,0},
               {0,0,0,0,0,0,0,0,0,3,2,4,0,0,0,0,0,7,0,0,7,0},
               {0,0,0,0,5,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,7,0},
               {0,0,0,0,0,0,0,0,0,0,0,5,0,0,0,0,0,7,0,0,7,0},
               {3,2,2,2,2,10,6,6,11,2,2,2,2,4,0,0,0,7,0,0,7,0},
               {0,0,0,0,0,8,7,7,9,0,0,0,0,0,0,0,0,7,0,0,7,0},
               {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,6,6,7,0},
               {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,8,7,7,9,0}
        };
       updateLevelDimensions();
    }

    @Override
    public String getSpriteName(int tileType) {
        final String fileName = mTileIdToSpriteName.get(tileType);
        if(fileName != null){
            return fileName;
        }
        return NULLSPRITE;
    }

}
