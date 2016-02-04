package com.quanganhct.simplegame.gameobject;

/**
 * Created by quanganh.nguyen on 1/30/2016.
 */
public class Coordinate {
    private int X, Y;

    public Coordinate(int x, int y) {
        X = x;
        Y = y;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public String toString() {
        return String.format("%s:%d %s:%d", "X", X, "Y", Y);
    }
}