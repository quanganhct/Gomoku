package com.quanganhct.simplegame.gameobject;

import android.util.Log;

import com.quanganhct.simplegame.Util.MLog;
import com.quanganhct.simplegame.uicomponent.GomokuTiledSprite;

import java.util.BitSet;

/**
 * Created by quanganh.nguyen on 1/28/2016.
 */
public class Player {
    private int sign;
    public static final int X = 1, Y = 2;
    private int rows, columns;
    private BitSet[] original, flip, diagonal, antiDiagonal;
    private Coordinate[] winSet;

    public Player(int sign, int rows, int columns) {
        assert sign == X || sign == Y;
        this.sign = sign;
        this.columns = columns;
        this.rows = rows;

        this.initializeBitMatrix();
    }

    public int getSign() {
        return this.sign;
    }

    private void initializeBitMatrix() {
        this.original = new BitSet[rows];
        this.flip = new BitSet[columns];
        this.diagonal = new BitSet[rows];
        this.antiDiagonal = new BitSet[rows];

        for (int i = 0; i < rows; i++) {
            this.original[i] = new BitSet(columns);
            this.original[i].clear();
            this.diagonal[i] = new BitSet(columns);
            this.diagonal[i].clear();
            this.antiDiagonal[i] = new BitSet(columns);
            this.antiDiagonal[i].clear();
        }

        for (int j = 0; j < columns; j++) {
            this.flip[j] = new BitSet(rows);
            this.flip[j].clear();
        }
    }

    public void makeMove(GomokuTiledSprite sprite) {
        int x = sprite.getXCoord();
        int y = sprite.getYCoord();
        sprite.markPlayer(this);
        assert 0 <= x && x < columns && 0 <= y && y < rows;
        this.original[y].set(x, true);
        this.flip[x].set(y, true);
        this.diagonal[(x + y) % rows].set(x, true);
        int s = (y - x) % rows;
        s = (s >= 0 ? s : s + rows);
        this.antiDiagonal[s].set(x, true);
    }

    private void print(BitSet[] sets) {
        for (int i = sets.length - 1; i >= 0; i--) {
            String s = "";
            for (int j = 0; j < sets[i].length(); j++) {
                s += sets[i].get(j) ? "1" : "0";
            }
            MLog.w("bitset", s);
        }
    }

    public boolean checkWin(int x, int y) {
        MLog.w("coord", String.format("%d %d", x, y));
        print(antiDiagonal);
        return checkWinOriginal(x, y) ||
                checkWinFlip(x, y) ||
                checkWinDiagonal(x, y) ||
                checkWinAntiDiagonal(x, y);
    }

    public boolean checkWinOriginal(int x, int y) {
        int startOriginal = x - 5;
        if (startOriginal < 0) {
            startOriginal = 0;
        }
        while (startOriginal <= x && startOriginal + 5 <= columns) {
            BitSet set = this.original[y].get(startOriginal, startOriginal + 5);
            if (set.cardinality() == 5) {
                this.winSet = new Coordinate[5];
                for (int i = 0; i < winSet.length; i++) {
                    winSet[i] = new Coordinate(startOriginal + i, y);
                }
                return true;
            }
            startOriginal++;
        }
        return false;
    }

    public boolean checkWinAntiDiagonal(int x, int y) {

        int s = (y - x) % rows;
        while (s < 0) {
            s = (s >= 0 ? s : s + rows);
        }
        int startBound, endBound;
        int margin = rows - s;
        if (x < margin) {
            startBound = 0;
            endBound = margin;
        } else {
            int div = (x - margin) / rows;
            div = div * rows + margin;
            startBound = div;
            endBound = Math.min(startBound + rows, columns);
        }
        int startAntiDiagonal = Math.max(x - 5, startBound);
//        Log.w("anti_bound", String.format("%d %d %d", startBound, endBound, startAntiDiagonal));
        while (startAntiDiagonal <= x && startAntiDiagonal + 5 <= endBound) {
            BitSet set = this.antiDiagonal[s].get(startAntiDiagonal, startAntiDiagonal + 5);
            if (set.cardinality() == 5) {
                this.winSet = new Coordinate[5];
                for (int i = 0; i < winSet.length; i++) {
                    winSet[i] = new Coordinate(startAntiDiagonal + i, (s + startAntiDiagonal + i) % rows);
                }
                return true;
            }
            startAntiDiagonal++;
        }
        return false;
    }

    public boolean checkWinDiagonal(int x, int y) {
        int s = (x + y) % rows;
        int startBound, endBound;
        if (x <= s) {
            startBound = 0;
            endBound = s + 1;
        } else {
            int div = (x - s - 1) / rows;
            div = div * rows + s;
            startBound = div + 1;
            endBound = Math.min(startBound + rows, columns);
        }
        int startDiagonal = Math.max(x - 5, startBound);
//        Log.w("bound", String.format("%d %d %d", startBound, endBound, startDiagonal));

        while (startDiagonal <= x && startDiagonal + 5 <= endBound) {
            BitSet set = this.diagonal[s].get(startDiagonal, startDiagonal + 5);
            if (set.cardinality() == 5) {
                this.winSet = new Coordinate[5];
                for (int i = 0; i < winSet.length; i++) {
                    int r = (s - startDiagonal - i) % rows;
                    while (r < 0) {
                        r = (r < 0 ? r + rows : r);
                    }
                    winSet[i] = new Coordinate(startDiagonal + i, r);
                }
                return true;
            }
            startDiagonal++;
        }
        return false;
    }

    public boolean checkWinFlip(int x, int y) {
        int startFlip = y - 5;
        if (startFlip < 0) {
            startFlip = 0;
        }
        while (startFlip <= y && startFlip + 5 <= rows) {
            BitSet set = this.flip[x].get(startFlip, startFlip + 5);
            if (set.cardinality() == 5) {
                this.winSet = new Coordinate[5];
                for (int i = 0; i < winSet.length; i++) {
                    winSet[i] = new Coordinate(x, startFlip + i);
                }
                return true;
            }
            startFlip++;
        }
        return false;
    }

    public Coordinate[] getWinSet() {
        return this.winSet;
    }
}
