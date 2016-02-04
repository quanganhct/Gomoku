package com.quanganhct.simplegame.gamelogic;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by quanganh.nguyen on 2/1/2016.
 */
public class GomokuMessageBuilder {

    private Map<String, String> maps;

    private static GomokuMessageBuilder builder;

    private GomokuMessageBuilder() {
    }

    public static GomokuMessageBuilder getInstance() {
        if (builder == null) {
            builder = new GomokuMessageBuilder();
        }
        builder.maps = new HashMap<>();
        return builder;
    }

    public GomokuMessageBuilder setX(int x) {
        this.maps.put("X", String.valueOf(x));
        return this;
    }

    public GomokuMessageBuilder setY(int y) {
        this.maps.put("Y", String.valueOf(y));
        return this;
    }

    public Map<String, String> get() {
        return this.maps;
    }
}
