package com.quanganhct.simplegame;

import android.util.Log;

import com.quanganhct.simplegame.gameobject.Player;
import com.quanganhct.simplegame.manager.GomokuGameManager;
import com.quanganhct.simplegame.uicomponent.GomokuTiledSprite;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import java.io.IOException;

public class MainActivity extends SimpleBaseGameActivity implements GomokuTiledSprite.GomokuSpriteOnClickListener, GomokuGameManager.WinnerCallback {
    private Camera mCamera;
    public Scene mScene;
    private int CAMERA_HEIGHT = 270;
    private int CAMERA_WIDTH = 450;
    private int STROKE_WIDTH = 10;
    private int COLUMNS = 16, ROWS = 8;
    private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
    private ITextureRegion blankRegion, xRegion, oRegion, winRegion;
    private float[] xCoords, yCoords;
    private GomokuTiledSprite[][] maps;
    private boolean xTurn = true;
    private GomokuGameManager gameManager;

    @Override
    public EngineOptions onCreateEngineOptions() {
        this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        EngineOptions options = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
                new FillResolutionPolicy(), mCamera);
        options.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
        options.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
        return options;
    }


    @Override
    protected void onCreateResources() throws IOException {
        this.gameManager = GomokuGameManager.getInstance();
        this.gameManager.setCallback(this);
        this.gameManager.initialize(COLUMNS - 1, ROWS - 1);
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        this.mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(this.getTextureManager(), 512, 512);
        this.blankRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, this, "blankIcon.png");
        this.xRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, this, "xIcon.png");
        this.oRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, this, "oIcon.png");
        this.winRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, this, "blueIcon.png");
        try {
            this.mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
            this.mBitmapTextureAtlas.load();
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Scene onCreateScene() {
        this.mScene = new Scene();
        this.xCoords = new float[COLUMNS];
        this.yCoords = new float[ROWS];
        float space = Math.min((float) CAMERA_HEIGHT / (float) ROWS, (float) CAMERA_WIDTH / (float) COLUMNS);
        float xCord = (CAMERA_WIDTH - space * (COLUMNS - 1)) / 2f;
        float yCord = (CAMERA_HEIGHT - space * (ROWS - 1)) / 2f;
        this.maps = new GomokuTiledSprite[COLUMNS - 1][ROWS - 1];
        VertexBufferObjectManager VBOManager = this.getVertexBufferObjectManager();
        Log.w("SPACE", "" + space);

        for (int i = 0; i < COLUMNS; i++) {
            xCoords[i] = xCord + i * space;
        }

        for (int i = 0; i < ROWS; i++) {
            yCoords[i] = yCord + i * space;
        }

        for (int i = 0; i < COLUMNS; i++) {
            Line line = new Line(xCoords[i], yCoords[0], xCoords[i], yCoords[ROWS - 1], STROKE_WIDTH, VBOManager);
            line.setColor(0f, 1f, 1f);
            mScene.attachChild(line);
        }

        for (int i = 0; i < ROWS; i++) {
            Line line = new Line(xCoords[0], yCoords[i], xCoords[COLUMNS - 1], yCoords[i], STROKE_WIDTH, VBOManager);
            line.setColor(0f, 1f, 1f);
            mScene.attachChild(line);
        }

        mScene.setTouchAreaBindingOnActionDownEnabled(true);
        mScene.setTouchAreaBindingOnActionMoveEnabled(false);

        for (int i = 0; i < COLUMNS - 1; i++) {
            for (int j = 0; j < ROWS - 1; j++) {
                this.maps[i][j] = new GomokuTiledSprite(xCoords[i] + space / 2f, yCoords[j] + space / 2f
                        , blankRegion, xRegion, oRegion, winRegion, VBOManager, this);
                mScene.registerTouchArea(maps[i][j]);
                mScene.attachChild(maps[i][j]);
                maps[i][j].setCoordinates(i, j);
            }
        }

        return mScene;
    }

    @Override
    public void onClick(GomokuTiledSprite sprite, float pX, float pY) {
        if (!this.gameManager.haveWinner()) {
            this.gameManager.makeMoveAt(sprite);
            this.gameManager.switchTurn();
        }
    }

    @Override
    public void haveWinner(Player player) {
        GomokuTiledSprite[] sprites = this.gameManager.getWinMove(maps, player.getWinSet());
        for (int i = 0; i < sprites.length; i++) {
            sprites[i].setCurrentTileIndex(3);
        }
    }
}
