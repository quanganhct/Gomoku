package com.quanganhct.simplegame.uicomponent;

import com.quanganhct.simplegame.gameobject.Player;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by quanganh.nguyen on 1/30/2016.
 */
public class GomokuTiledSprite extends TiledSprite {

    private GomokuSpriteOnClickListener onClickListener;
    private int xCoord, yCoord;
    private boolean isEmpty;

    public static interface GomokuSpriteOnClickListener {
        void onClick(GomokuTiledSprite sprite, float pX, float pY);
    }

    public GomokuTiledSprite(float pX, float pY, ITextureRegion s1, ITextureRegion s2, ITextureRegion s3, ITextureRegion s4, VertexBufferObjectManager pVertexBufferObjectManager, GomokuSpriteOnClickListener pOnClickListener) {
        super(pX, pY, new TiledTextureRegion(s1.getTexture(), s1, s2, s3, s4), pVertexBufferObjectManager);
        this.onClickListener = pOnClickListener;
        this.isEmpty = true;
    }

    public void setCoordinates(int x, int y) {
        this.xCoord = x;
        this.yCoord = y;
    }

    public int getXCoord() {
        return xCoord;
    }

    public int getYCoord() {
        return yCoord;
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if (isEmpty && pSceneTouchEvent.isActionDown()) {
            if (onClickListener != null) {
                onClickListener.onClick(this, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        }
        return true;
    }

    public void markPlayer(Player p) {
        this.setCurrentTileIndex(p.getSign());
        this.isEmpty = false;
    }
}
