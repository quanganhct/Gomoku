package com.quanganhct.simplegame.manager;

import android.util.Log;

import com.quanganhct.simplegame.gameobject.Coordinate;
import com.quanganhct.simplegame.gameobject.Player;
import com.quanganhct.simplegame.uicomponent.GomokuTiledSprite;

/**
 * Created by quanganh.nguyen on 1/27/2016.
 */
public class GomokuGameManager {
    private static GomokuGameManager instance;
    private WinnerCallback callback;

    private GomokuGameManager() {
    }

    public static GomokuGameManager getInstance() {
        if (instance == null) {
            instance = new GomokuGameManager();
        }
        return instance;
    }

    private int[][] maps;
    private boolean xPlayerTurn;
    private boolean haveWinner;
    private int columns, rows;
    private Player[] players;
    private Player winner;

    public void initialize(int columns, int rows) {
        this.xPlayerTurn = true;
        this.players = new Player[2];
        this.players[0] = new Player(Player.X, rows, columns);
        this.players[1] = new Player(Player.Y, rows, columns);
        this.maps = new int[columns][rows];
        this.columns = columns;
        this.rows = rows;
        this.haveWinner = false;
        this.winner = null;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                maps[i][j] = 0;
            }
        }
    }

    public Player getCurrentPlayer() {
        if (xPlayerTurn) {
            return players[0];
        } else {
            return players[1];
        }
    }

    public void switchTurn() {
        if (!haveWinner()) {
            this.xPlayerTurn = !xPlayerTurn;
        }
    }

    public boolean ifXTurn() {
        return xPlayerTurn;
    }

    public void makeMoveAt(GomokuTiledSprite sprite) {
        if (!haveWinner()) {
            this.maps[sprite.getXCoord()][sprite.getYCoord()] = (xPlayerTurn ? 1 : 2);
            getCurrentPlayer().makeMove(sprite);
            if (xPlayerTurn) {
                sprite.setCurrentTileIndex(1);
            } else {
                sprite.setCurrentTileIndex(2);
            }
            if (getCurrentPlayer().checkWin(sprite.getXCoord(), sprite.getYCoord())) {
                Log.w("WINNER", getCurrentPlayer().getSign() == 1 ? "X" : "O");
                this.haveWinner = true;
                this.winner = getCurrentPlayer();
                Coordinate[] winset = this.winner.getWinSet();
                if (winset != null) {
                    for (Coordinate c : winset) {
                        Log.w("Coord", c.toString());
                    }
                }
            }
        }
    }

    public GomokuTiledSprite[] getWinMove(GomokuTiledSprite[][] gomoku, Coordinate[] coordinates) {
        assert coordinates != null;
        GomokuTiledSprite[] sprites = new GomokuTiledSprite[coordinates.length];
        for (int i = 0; i < sprites.length; i++) {
            Coordinate c = coordinates[i];
            sprites[i] = gomoku[c.getX()][c.getY()];
        }
        return sprites;
    }

    public void setCallback(WinnerCallback callback) {
        this.callback = callback;
    }

    public boolean haveWinner() {
        if (haveWinner && callback != null){
            callback.haveWinner(getCurrentPlayer());
        }
        return haveWinner;
    }

    public static interface WinnerCallback {
        void haveWinner(Player player);
    }
}
